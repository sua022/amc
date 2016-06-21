package cn.hugoz.amc.asm;

import java.util.ArrayList;

import cn.hugoz.amc.parser.ConstructorEntity;
import cn.hugoz.amc.parser.MethodArgs;
import cn.hugoz.amc.parser.MethodEntity;
import cn.hugoz.amc.parser.TypeEntity;
import cn.hugoz.amc.parser.TypeParameterEntity;

public class SignatureUtil {

	public static String getConstructorSignature(ConstructorEntity constructorEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (constructorEntity.getArgs() != null) {
			for (MethodArgs arg : constructorEntity.getArgs()) {
				sb.append(getTypeSignature(arg.getArgsType()));
			}
		}
		sb.append(")V");
		return sb.toString();
	}

	public static String getMethodSignature(MethodEntity methodEntity) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (methodEntity.getArgs() != null) {
			for (MethodArgs arg : methodEntity.getArgs()) {
				sb.append(getTypeSignature(arg.getArgsType()));
			}
		}
		sb.append(")");
		sb.append(getTypeSignature(methodEntity.getReturnType()));
		return sb.toString();
	}

	public static String getTypeSignature(TypeEntity typeEntity) {
		if (typeEntity.isVoidType()) {
			return "V";
		} else if (typeEntity.isPrimitiveType()) {
			return getPrimitiveTypeSignature(typeEntity);
		} else {
			return getClassTypeSignature(typeEntity);
		}
	}

	private static String getClassTypeSignature(TypeEntity typeEntity) {
		StringBuilder signatureSb = new StringBuilder();
		int arrayCount = typeEntity.getArrayCount();
		if (arrayCount > 0) {
			for (int i = 0; i < arrayCount; i++) {
				signatureSb.append("[");
			}
		}
		signatureSb.append("L");
		if (typeEntity instanceof TypeParameterEntity) {// 如果是泛型则单独处理
			ArrayList<TypeEntity> bounds = ((TypeParameterEntity) typeEntity).getTypeBounds();
			if (bounds != null && bounds.size() > 0) {//声明了bounds则使用bounds做类型，否则使用Object
				String name = bounds.get(0).getFullName();
				signatureSb.append(name.replace(".", "/"));
				signatureSb.append(";");
			} else {
				signatureSb.append("java/lang/Object;");
			}
		} else {
			String name = typeEntity.getFullName();
			signatureSb.append(name.replace(".", "/"));
			signatureSb.append(";");
		}
		return signatureSb.toString();
	}

	private static String getPrimitiveTypeSignature(TypeEntity typeEntity) {
		String name = typeEntity.getSimpleName();
		StringBuilder signatureSb = new StringBuilder();
		int arrayCount = typeEntity.getArrayCount();
		if (arrayCount > 0) {
			for (int i = 0; i < arrayCount; i++) {
				signatureSb.append("[");
			}
		}
		if (name.equals("boolean")) {
			signatureSb.append("Z");
		} else if (name.equals("byte")) {
			signatureSb.append("B");
		} else if (name.equals("char")) {
			signatureSb.append("C");
		} else if (name.equals("short")) {
			signatureSb.append("S");
		} else if (name.equals("int")) {
			signatureSb.append("I");
		} else if (name.equals("long")) {
			signatureSb.append("J");
		} else if (name.equals("float")) {
			signatureSb.append("F");
		} else if (name.equals("double")) {
			signatureSb.append("D");
		}
		return signatureSb.toString();
	}

}
