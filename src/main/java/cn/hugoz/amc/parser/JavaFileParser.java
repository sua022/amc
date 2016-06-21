package cn.hugoz.amc.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.QualifiedNameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;

public class JavaFileParser {

	private File javaFile;
	private FileEntity currentFileEntity;
	private String targetAnnotation;

	public JavaFileParser(String targetAnnotation, String javaFilePath) {
		this(targetAnnotation, new File(javaFilePath));
	}

	public JavaFileParser(String targetAnnotation, File javaFile) {
		this.javaFile = javaFile;
		this.targetAnnotation = targetAnnotation;
	}

	private ArrayList<String> parseImports(CompilationUnit cu) {
		List<ImportDeclaration> imports = cu.getImports();
		if (imports != null) {
			ArrayList<String> importsList = new ArrayList<>();
			for (ImportDeclaration id : imports) {
				if (!id.isEmptyImportDeclaration()) {
					QualifiedNameExpr nameExpr = (QualifiedNameExpr) id.getName();
					importsList.add(nameExpr.toString());
				}
			}
			return importsList;
		}
		return null;
	}

	private ArrayList<FieldEntity> parseFieldEntity(ClassEntity classEntity, FieldDeclaration fd) {
		ArrayList<FieldEntity> fields = new ArrayList<>();
		if (isExport(classEntity, fd.getAnnotations())) {
			for (VariableDeclarator vd : fd.getVariables()) {
				FieldEntity field = new FieldEntity();
				field.setFieldType(TypeEntity.createTypeEntity(currentFileEntity, classEntity, null, fd.getType()));
				field.setFiledName(vd.toString());
				field.setModifiers(fd.getModifiers());
				fields.add(field);
			}
		}
		return fields;
	}

	private ArrayList<TypeParameterEntity> convertTypeParamters(List<TypeParameter> typeParmeters,
			ClassEntity classEntity) {
		if (typeParmeters != null && typeParmeters.size() > 0) {
			ArrayList<TypeParameterEntity> list = new ArrayList<>();
			for (TypeParameter parameter : typeParmeters) {
				list.add(TypeParameterEntity.createTypeParameter(currentFileEntity, classEntity, null, parameter));
			}
			return list;
		}
		return null;
	}

	private boolean isExport(ClassEntity classEntity, List<AnnotationExpr> annotations) {
		if (annotations != null && targetAnnotation != null) {
			for (AnnotationExpr ae : annotations) {
				ClassName annotationClassName = ClassNameUtils.getClassName(currentFileEntity, classEntity,
						ae.getName().getName());
				if (targetAnnotation.equals(annotationClassName.getFullName())) {
					return true;
				}
			}
		}
		return false;
	}

	private MethodEntity parseMethodEntity(ClassEntity classEntity, MethodDeclaration md) {
		if (!isExport(classEntity, md.getAnnotations())) {
			return null;
		}

		MethodEntity methodEntity = new MethodEntity();
		methodEntity.setTypeParameters(convertTypeParamters(md.getTypeParameters(), classEntity));

		methodEntity.setMethodName(md.getName());
		methodEntity
				.setReturnType(TypeEntity.createTypeEntity(currentFileEntity, classEntity, methodEntity, md.getType()));
		List<Parameter> params = md.getParameters();
		if (params != null) {
			ArrayList<MethodArgs> args = new ArrayList<>();
			for (Parameter param : params) {
				MethodArgs arg = new MethodArgs();
				arg.setArgsName(param.getId().toString());
				TypeEntity typeEntity = TypeEntity.createTypeEntity(currentFileEntity, classEntity, methodEntity,
						param.getType());
				if (param.isVarArgs()) {// 可变参数
					typeEntity.setArrayCount(1);
				}
				arg.setArgsType(typeEntity);
				args.add(arg);
			}
			methodEntity.setArgs(args);
		}

		List<ReferenceType> throwsList = md.getThrows();
		if (throwsList != null) {
			ArrayList<TypeEntity> list = new ArrayList<>();
			for (ReferenceType rt : throwsList) {
				list.add(TypeEntity.createTypeEntity(currentFileEntity, classEntity, methodEntity, rt.getType()));
			}
			methodEntity.setThrowsList(list);
		}
		methodEntity.setModifiers(md.getModifiers());
		return methodEntity;
	}

	private ConstructorEntity parseConstructorEntity(ClassEntity classEntity, ConstructorDeclaration cd) {
		if (!isExport(classEntity, cd.getAnnotations())) {
			return null;
		}
		ConstructorEntity constructorEntity = new ConstructorEntity();
		List<Parameter> params = cd.getParameters();
		if (params != null) {
			ArrayList<MethodArgs> args = new ArrayList<>();
			for (Parameter param : params) {
				MethodArgs arg = new MethodArgs();
				arg.setArgsName(param.getId().toString());
				arg.setArgsType(TypeEntity.createTypeEntity(currentFileEntity, classEntity, null, param.getType()));
				args.add(arg);
			}
			constructorEntity.setArgs(args);
		}

		List<NameExpr> throwsList = cd.getThrows();
		if (throwsList != null) {
			ArrayList<String> list = new ArrayList<>();
			for (NameExpr rt : throwsList) {
				list.add(rt.getName());
			}
			constructorEntity.setThrowsList(list);
		}
		constructorEntity.setModifiers(cd.getModifiers());
		return constructorEntity;
	}

	private void setClassName(ClassEntity classEntity, ClassEntity outerClassEntity, ClassOrInterfaceDeclaration cif) {
		String name = cif.getName();
		classEntity.setClassName(name);
		classEntity
				.setFullName(getClassFullName(name, outerClassEntity != null ? outerClassEntity.getFullName() : null));
	}

	private String getClassFullName(String name, String outerClassFullName) {
		String fullName;
		if (!StringUtils.isEmpty(outerClassFullName)) {
			fullName = outerClassFullName + "$" + name;
		} else {
			String packageName = currentFileEntity.getPackageName();
			if (!StringUtils.isEmpty(packageName)) {
				fullName = packageName + "." + name;
			} else {
				fullName = name;
			}
		}
		return fullName;
	}

	private void scanInnerClass(HashMap<String, String> map, ClassOrInterfaceDeclaration cif, String outerClassName) {
		String name = cif.getName();
		String fullName = getClassFullName(name, outerClassName);
		map.put(name, fullName);
		List<BodyDeclaration> bodyDeclarations = cif.getMembers();
		if (bodyDeclarations != null && bodyDeclarations.size() > 0) {
			for (BodyDeclaration bd : bodyDeclarations) {
				if (bd instanceof ClassOrInterfaceDeclaration) {
					scanInnerClass(map, (ClassOrInterfaceDeclaration) bd, fullName);
				}
			}
		}
	}

	private ClassEntity parseClassEntity(ClassOrInterfaceDeclaration cif, ClassEntity outerClassEntity,
			HashMap<String, String> innerClassMap) {
		ClassEntity classEntity = new ClassEntity();

		classEntity.setHasClassExportAnnotation(isExport(classEntity, cif.getAnnotations()));

		// TODO static的内部类无法访问父类的非static内部类
		classEntity.setInnerClassMap(innerClassMap);
		// className
		setClassName(classEntity, outerClassEntity, cif);

		classEntity.setTypeParameters(convertTypeParamters(cif.getTypeParameters(), classEntity));

		// extends
		List<ClassOrInterfaceType> extendsList = cif.getExtends();
		if (extendsList != null && extendsList.size() > 0) {
			classEntity.setExtendsName(
					TypeEntity.createClassTypeEntity(currentFileEntity, classEntity, extendsList.get(0).getName()));
		}
		// implements
		List<ClassOrInterfaceType> imples = cif.getImplements();
		if (imples != null) {
			ArrayList<TypeEntity> implementsList = new ArrayList<>();
			for (ClassOrInterfaceType i : imples) {
				implementsList.add(TypeEntity.createClassTypeEntity(currentFileEntity, classEntity, i.getName()));
			}
			classEntity.setImplementsList(implementsList);
		}
		// modifiers
		classEntity.setModifiers(cif.getModifiers());

		classEntity.setInterfaceClass(cif.isInterface());

		List<BodyDeclaration> bodyDeclarations = cif.getMembers();
		if (bodyDeclarations != null && bodyDeclarations.size() > 0) {
			ArrayList<FieldEntity> fields = new ArrayList<>();
			ArrayList<MethodEntity> methods = new ArrayList<>();
			ArrayList<ConstructorEntity> constructorEntities = new ArrayList<>();
			ArrayList<ClassEntity> innerClasses = new ArrayList<>();
			for (BodyDeclaration bd : bodyDeclarations) {
				if (bd instanceof FieldDeclaration) {
					FieldDeclaration fd = (FieldDeclaration) bd;
					fields.addAll(parseFieldEntity(classEntity, fd));
				} else if (bd instanceof MethodDeclaration) {
					MethodDeclaration md = (MethodDeclaration) bd;
					MethodEntity methodEntity = parseMethodEntity(classEntity, md);
					if (methodEntity != null) {
						methods.add(methodEntity);
					}
				} else if (bd instanceof ClassOrInterfaceDeclaration) {
					ClassEntity innerClass = parseClassEntity((ClassOrInterfaceDeclaration) bd, classEntity,
							innerClassMap);
					innerClass.setInnerClass(true);
					innerClass.setOuterClass(classEntity);
					innerClasses.add(innerClass);
				} else if (bd instanceof ConstructorDeclaration) {
					ConstructorEntity ce = parseConstructorEntity(classEntity, (ConstructorDeclaration) bd);
					if (ce != null) {
						constructorEntities.add(ce);
					}
				}
			}
			classEntity.setMethods(methods);
			classEntity.setFields(fields);
			classEntity.setInnerClasses(innerClasses);
			classEntity.setConstructors(constructorEntities);
		}
		return classEntity;
	}

	private ArrayList<ClassEntity> parseClassEntities(CompilationUnit cu) {
		List<TypeDeclaration> types = cu.getTypes();
		if (types != null) {
			ArrayList<ClassEntity> list = new ArrayList<>();
			for (TypeDeclaration type : types) {
				if (type instanceof ClassOrInterfaceDeclaration) {
					ClassOrInterfaceDeclaration cif = (ClassOrInterfaceDeclaration) type;
					HashMap<String, String> innerClassMap = new HashMap<>();
					scanInnerClass(innerClassMap, cif, null);
					ClassEntity classEntity = parseClassEntity(cif, null, innerClassMap);
					list.add(classEntity);
				}
			}
			return list;
		}
		return null;
	}

	public FileEntity parse() throws FileNotFoundException, ParseException {
		if (StringUtils.isEmpty(targetAnnotation)) {
			return null;
		}

		FileInputStream in = new FileInputStream(javaFile);
		CompilationUnit cu = JavaParser.parse(in);

		FileEntity fileEntity = new FileEntity();
		currentFileEntity = fileEntity;
		PackageDeclaration pd = cu.getPackage();
		if (pd != null) {
			fileEntity.setPackageName(pd.getPackageName());
		}
		fileEntity.setImports(parseImports(cu));
		fileEntity.setClassEntities(parseClassEntities(cu));
		return fileEntity;
	}

}
