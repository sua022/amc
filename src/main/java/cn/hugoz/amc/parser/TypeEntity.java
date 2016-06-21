package cn.hugoz.amc.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;

public class TypeEntity {


	private String simpleName;
	private String fullName;
	private boolean voidType;
	private boolean primitiveType;

	private ArrayList<TypeEntity> typeArguments;// 相关的泛型类型，如ArrayList<String>里的String

	private int arrayCount;

	public static TypeEntity createClassTypeEntity(FileEntity fileEntity, ClassEntity classEntity, String typeName) {
		TypeEntity typeEntity = new TypeEntity();
		setClassTypeEntityName(typeEntity, fileEntity, classEntity, typeName);
		return typeEntity;
	}

	// 方法级泛型>内部类>类泛型>import>同包>lang
	private static void setClassTypeEntityName(TypeEntity typeEntity, FileEntity fileEntity, ClassEntity classEntity,
											   String typeName) {

		ClassName className = ClassNameUtils.getClassName(fileEntity, classEntity, typeName);

		typeEntity.setFullName(className.getFullName());
		typeEntity.setSimpleName(className.getSimpleName());
	}

	public static TypeEntity createTypeEntity(FileEntity fileEntity, ClassEntity classEntity, MethodEntity methodEntity,
											  Type type) {

		TypeEntity typeEntity;
		String name;
		if (type instanceof VoidType) {
			typeEntity = new TypeEntity();
			typeEntity.setVoidType(true);
			name = type.toString();
		} else if (type instanceof PrimitiveType) {
			typeEntity = new TypeEntity();
			typeEntity.setPrimitiveType(true);
			name = type.toString();
		} else if (type instanceof WildcardType) {
			typeEntity = new WildcardTypeEntity();
			WildcardTypeEntity wct = (WildcardTypeEntity) typeEntity;
			Type extendsType = ((WildcardType) type).getExtends();
			if (extendsType != null) {
				wct.setExt(createTypeEntity(fileEntity, classEntity, methodEntity, extendsType));
			}
			Type superType = ((WildcardType) type).getSuper();
			if (superType != null) {
				wct.setSup(TypeEntity.createTypeEntity(fileEntity, classEntity, methodEntity, superType));
			}
			name = type.toString();
		} else if (type instanceof ReferenceType) {
			ReferenceType rt = (ReferenceType) type;
			Type rtType = rt.getType();
			if (rtType instanceof ClassOrInterfaceType) {
				ClassOrInterfaceType cif = (ClassOrInterfaceType)rtType;
				if (((ClassOrInterfaceType) rtType).getScope() != null) {//说明是内部类，直接toString把outerClass带上
					name = rtType.toString();
				} else {
					name = cif.getName();
				}
			} else {
				name = rtType.toString();
			}
			// 如果是方法或类定义的泛型则直接返回
			TypeParameterEntity typeParameterEntity = null;
			if (methodEntity != null) {
				typeParameterEntity = methodEntity.getTypeParameter(name);
			}
			if (typeParameterEntity == null) {
				HashMap<String, String> innerClassMap = classEntity.getInnerClassMap();
				if (innerClassMap == null || !innerClassMap.containsKey(name)) {// 如果内部类里面包含此类型则认为用的是内部类
					typeParameterEntity = classEntity.getTypeParameter(name);
				}
			}
			if (typeParameterEntity != null) {
				TypeParameterEntity copy = typeParameterEntity.copy();
				copy.setArrayCount(rt.getArrayCount());
				return typeParameterEntity;
			}

			typeEntity = new TypeEntity();
			typeEntity.setArrayCount(rt.getArrayCount());
			if (rtType instanceof PrimitiveType) {
				typeEntity.setPrimitiveType(true);
			} else if (rtType instanceof ClassOrInterfaceType) {
				ArrayList<TypeEntity> argList = new ArrayList<>();
				List<Type> typeArgs = ((ClassOrInterfaceType) rtType).getTypeArgs();
				if (typeArgs != null && typeArgs.size() > 0) {
					for (Type arg : typeArgs) {
						argList.add(TypeEntity.createTypeEntity(fileEntity, classEntity, methodEntity, arg));
					}
				}
				typeEntity.setTypeArguments(argList);
			}
		} else {
			typeEntity = new TypeEntity();
			name = type.toString();
		}
		if (typeEntity.isPrimitiveType() || typeEntity.isVoidType()) {
			typeEntity.setSimpleName(name);
			typeEntity.setFullName(name);
		} else {
			setClassTypeEntityName(typeEntity, fileEntity, classEntity, name);
		}
		return typeEntity;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isVoidType() {
		return voidType;
	}

	public void setVoidType(boolean voidType) {
		this.voidType = voidType;
	}

	public boolean isPrimitiveType() {
		return primitiveType;
	}

	public void setPrimitiveType(boolean primitiveType) {
		this.primitiveType = primitiveType;
	}

	public int getArrayCount() {
		return arrayCount;
	}

	public void setArrayCount(int arrayCount) {
		this.arrayCount = arrayCount;
	}

	public ArrayList<TypeEntity> getTypeArguments() {
		return typeArguments;
	}

	public void setTypeArguments(ArrayList<TypeEntity> typeArguments) {
		this.typeArguments = typeArguments;
	}

}
