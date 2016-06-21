package cn.hugoz.amc.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cn.hugoz.amc.parser.ClassEntity;
import cn.hugoz.amc.parser.ConstructorEntity;
import cn.hugoz.amc.parser.FieldEntity;
import cn.hugoz.amc.parser.FileEntity;
import cn.hugoz.amc.parser.MethodEntity;
import cn.hugoz.amc.parser.TypeEntity;

public class ClassGenerator implements Opcodes {

	private String destDir;
	private FileEntity fileEntity;

	private HashMap<String, byte[]> classDatas = new HashMap<>();

	public ClassGenerator(String destDir, FileEntity fileEntity) {
		this.destDir = destDir;
		this.fileEntity = fileEntity;
	}

	private static String getInerternalName(String fullName) {
		return fullName.replace(".", "/");
	}

	private static String getSeprateFullName(TypeEntity typeEntity) {
		String name = typeEntity.getFullName();
		return getInerternalName(name);
	}

	private static String getExtendsName(ClassEntity entity) {
		TypeEntity extendsNameType = entity.getExtendsName();
		String superName;
		if (extendsNameType != null) {
			superName = getSeprateFullName(extendsNameType);
		} else {
			if (entity.isInterfaceClass()) {
				superName = null;
			} else {
				superName = "java/lang/Object";
			}
		}
		return superName;
	}

	private static String[] getImplements(ClassEntity entity) {
		List<TypeEntity> list = entity.getImplementsList();
		if (list != null && list.size() > 0) {
			String[] implementsArray = new String[list.size()];
			int i = 0;
			for (TypeEntity type : list) {
				implementsArray[i++] = getSeprateFullName(type);
			}
			return implementsArray;
		}
		return null;
	}

	private static void addThrow(MethodVisitor mv) {
		String exceptionClassNm = "java/lang/RuntimeException";
		String d = "(Ljava/lang/String;)V";
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitTypeInsn(Opcodes.NEW, exceptionClassNm);
		mv.visitInsn(Opcodes.DUP);
		mv.visitLdcInsn("Stub");
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, exceptionClassNm, "<init>", d, false);
		mv.visitInsn(Opcodes.ATHROW);
		mv.visitEnd();
	}

	private void writeConstructors(ClassEntity entity, ClassWriter cw) {
		List<ConstructorEntity> constructors = entity.getConstructors();
		if (constructors != null && constructors.size() > 0) {
			for (ConstructorEntity ce : constructors) {
				MethodVisitor mw = cw.visitMethod(ce.getModifiers(), "<init>",
						SignatureUtil.getConstructorSignature(ce), null, null);
				addThrow(mw);
			}
		} else {
			MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			addThrow(mw);
		}
	}

	private void writeFields(ClassEntity entity, ClassWriter cw) {
		List<FieldEntity> fields = entity.getFields();
		if (fields != null && fields.size() > 0) {
			for (FieldEntity fe : fields) {
				FieldVisitor fv = cw.visitField(fe.getModifiers(), fe.getFiledName(),
						SignatureUtil.getTypeSignature(fe.getFieldType()), null, null);
				fv.visitEnd();
			}
		}
	}

	private void writeMethods(ClassEntity entity, ClassWriter cw) {
		List<MethodEntity> methods = entity.getMethods();
		if (methods != null && methods.size() > 0) {
			for (MethodEntity me : methods) {
				MethodVisitor mw = cw.visitMethod(me.getModifiers(), me.getMethodName(),
						SignatureUtil.getMethodSignature(me), null, null);
				if (!entity.isInterfaceClass()) {
					addThrow(mw);
				}
			}
		}
	}

	private int innerNameIndex;

	private String generateInnerName() {
		return "C" + innerNameIndex++;
	}

	private void writeInnerClasses(ClassEntity entity, ClassWriter cw) {
		ArrayList<ClassEntity> innerClasses = entity.getInnerClasses();
		if (innerClasses != null && innerClasses.size() > 0) {
			for (ClassEntity innerClass : innerClasses) {
				if (innerClass.isNeedExport()) {
					innerClass.setInnerName(generateInnerName());
					cw.visitInnerClass(getInerternalName(innerClass.getFullName()),
							getInerternalName(innerClass.getOuterClass().getFullName()), innerClass.getInnerName(),
							innerClass.getModifiers());

					writeInnerClass(innerClass);
				}
			}
		}
	}

	private void writeInnerClass(ClassEntity innerClass) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

		String seperateName = getInerternalName(innerClass.getFullName());
		String seperateOuterName = getInerternalName(innerClass.getOuterClass().getFullName());

		cw.visit(V1_5, innerClass.getModifiers(), seperateName, null, getExtendsName(innerClass),
				getImplements(innerClass));

		cw.visitInnerClass(seperateName, seperateOuterName, innerClass.getInnerName(), innerClass.getModifiers());

		FieldVisitor fv = cw.visitField(ACC_FINAL + ACC_SYNTHETIC, "this$0", "L" + seperateOuterName + ";", null, null);
		fv.visitEnd();

		if (!innerClass.isInterfaceClass()) {
			writeConstructors(innerClass, cw);
		}

		writeFields(innerClass, cw);
		writeMethods(innerClass, cw);
		writeInnerClasses(innerClass, cw);

		cw.visitEnd();

		classDatas.put(seperateName, cw.toByteArray());
	}

	public void generate() throws IOException {
		ArrayList<ClassEntity> classEntities = fileEntity.getClassEntities();
		String packageName = fileEntity.getPackageName();
		if (packageName != null) {
			packageName = getInerternalName(packageName);
		}
		if (classEntities != null) {
			for (ClassEntity entity : classEntities) {
				if (entity.isNeedExport()) {
					// System.out.println("generate "+entity.getFullName());
					ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

					String className;
					if (packageName != null) {
						className = packageName + "/" + entity.getClassName();
					} else {
						className = entity.getClassName();
					}
					cw.visit(V1_5, entity.getModifiers(), className, null, getExtendsName(entity),
							getImplements(entity));

					if (!entity.isInterfaceClass()) {
						writeConstructors(entity, cw);
					}

					writeFields(entity, cw);

					writeMethods(entity, cw);

					writeInnerClasses(entity, cw);

					cw.visitEnd();
					classDatas.put(className, cw.toByteArray());
				}
			}
		}
		for (Entry<String, byte[]> entry : classDatas.entrySet()) {
			generate(destDir, entry.getKey() + ".class", entry.getValue());
		}
	}

	private void generate(final String dir, final String path, final byte[] clazz) throws IOException {
		File f = new File(new File(dir), path);
		if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) {
			throw new IOException("Cannot create directory " + f.getParentFile());
		}
		FileOutputStream o = new FileOutputStream(f);
		o.write(clazz);
		o.close();
	}

}
