package cn.hugoz.amc.asm;

import java.util.ArrayList;

import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;

import cn.hugoz.amc.parser.ClassEntity;
import cn.hugoz.amc.parser.MethodArgs;
import cn.hugoz.amc.parser.MethodEntity;
import cn.hugoz.amc.parser.TypeEntity;
import cn.hugoz.amc.parser.TypeParameterEntity;
import cn.hugoz.amc.parser.WildcardTypeEntity;

public class GenericSignatureUtil {
	private static void buildTypeSignature(SignatureWriter signature, TypeEntity typeEntity) {
		for (int i = 0, size = typeEntity.getArrayCount(); i < size; i++) {
			signature.visitArrayType();
		}

		if (typeEntity.isVoidType()) {
			signature.visitBaseType('V');
		} else if (typeEntity.isPrimitiveType()) {
			signature.visitBaseType(getPrimitiveTypeSignature(typeEntity));
		} else if (typeEntity instanceof TypeParameterEntity) {
			signature.visitTypeVariable(typeEntity.getSimpleName());
		} else {
			signature.visitClassType(typeEntity.getFullName().replace('.', '/'));
			ArrayList<TypeEntity> typeArgs = typeEntity.getTypeArguments();
			// 内部泛型的数据
			if (typeArgs != null && typeArgs.size() > 0) {
				for (TypeEntity arg : typeArgs) {
					if (arg instanceof WildcardTypeEntity) {
						WildcardTypeEntity wt = (WildcardTypeEntity) arg;
						if (wt.getSup() != null) {
							signature.visitTypeArgument(SignatureVisitor.SUPER);
							buildTypeSignature(signature, wt.getSup());
						} else if (wt.getExt() != null) {
							signature.visitTypeArgument(SignatureVisitor.EXTENDS);
							buildTypeSignature(signature, wt.getExt());
						} else {
							continue;
						}
					} else if (typeEntity instanceof TypeParameterEntity) {
						signature.visitTypeArgument(SignatureVisitor.INSTANCEOF);
						buildTypeSignature(signature, arg);
					} else {
						signature.visitTypeArgument(SignatureVisitor.INSTANCEOF);
						buildTypeSignature(signature, arg);
					}
				}
			}
			signature.visitEnd();
		}

	}

	public static String buildClassSignature(SignatureWriter signature, ClassEntity classEntity) {
		// step1.泛型声明
		ArrayList<TypeParameterEntity> typeParameters = classEntity.getTypeParameters();
		buildAnnoucement(signature, typeParameters);

		// step2.基类
		signature.visitSuperclass();
		TypeEntity extendsType = classEntity.getExtendsName();
		if (extendsType != null) {
			buildTypeSignature(signature, extendsType);
		} else {
			signature.visitClassType("java/lang/Object");
			signature.visitEnd();
		}

		// step3.接口
		ArrayList<TypeEntity> implementsList = classEntity.getImplementsList();
		if (implementsList != null && implementsList.size() > 0) {
			for (TypeEntity typeEntity : implementsList) {
				buildTypeSignature(signature, typeEntity);
			}
		}

		return signature.toString();

	}

	// <T:Ljava/lang/Class;>
	private static void buildAnnoucement(SignatureWriter signature, ArrayList<TypeParameterEntity> typeParameters) {
		if (typeParameters != null && typeParameters.size() > 0) {
			for (TypeParameterEntity tpe : typeParameters) {
				signature.visitFormalTypeParameter(tpe.getSimpleName());
				ArrayList<TypeEntity> bounds = tpe.getTypeBounds();
				if (bounds != null && bounds.size() > 0) {
					// TODO 这里没有办法判断bounds是接口还是类
					for (int i = 0; i < bounds.size(); i++) {
						if (i == 0) {
							signature.visitClassBound();
						} else {
							signature.visitClassBound();
						}
						signature.visitClassType(bounds.get(i).getFullName().replace('.', '/'));
						signature.visitEnd();
					}
				} else {
					signature.visitClassType("java/lang/Object");
					signature.visitEnd();
				}

			}
		}
	}

	//<T:Ljava/lang/Object;>(Ljava/util/ArrayList<TT;>;[Ljava/lang/String;)Ljava/lang/Object;
	//<T:Ljava/lang/Object;>(Ljava/util/ArrayList<Ljava/lang/String;>;[Ljava/lang/String;)TT;
	public static String buildMethodSignature(SignatureWriter signature, MethodEntity methodEntity) {
		// step1.泛型声明
		ArrayList<TypeParameterEntity> typeParameters = methodEntity.getTypeParameters();
		buildAnnoucement(signature, typeParameters);

		// step2.参数
		signature.visitParameterType();
		ArrayList<MethodArgs> methodArgList = methodEntity.getArgs();
		if (methodArgList != null && methodArgList.size() > 0) {
			for (MethodArgs ma : methodArgList) {
				buildTypeSignature(signature, ma.getArgsType());
			}
		}

		// step3.返回类型
		signature.visitReturnType();
		buildTypeSignature(signature, methodEntity.getReturnType());

		return signature.toString();
	}

	private static char getPrimitiveTypeSignature(TypeEntity typeEntity) {
		String name = typeEntity.getSimpleName();
		if (name.equals("boolean")) {
			return 'Z';
		} else if (name.equals("byte")) {
			return 'B';
		} else if (name.equals("char")) {
			return 'C';
		} else if (name.equals("short")) {
			return 'S';
		} else if (name.equals("int")) {
			return 'I';
		} else if (name.equals("long")) {
			return 'J';
		} else if (name.equals("float")) {
			return 'F';
		} else if (name.equals("double")) {
			return 'D';
		}
		return 'I';
	}

}
