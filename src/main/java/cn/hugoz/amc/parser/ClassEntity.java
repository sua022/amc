package cn.hugoz.amc.parser;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.Opcodes;

public class ClassEntity {

	private String className;
	private String fullName;
	private TypeEntity extendsName;
	private ArrayList<TypeEntity> implementsList;

	private ArrayList<FieldEntity> fields;
	private ArrayList<MethodEntity> methods;
	private ArrayList<ConstructorEntity> constructors;
	private ArrayList<TypeParameterEntity> typeParameters;
	private HashMap<String, TypeParameterEntity> typeParameterMap;

	private boolean hasClassExportAnnotation;// Class是否被标识了导出的Annotation

	// simpleName -> fullName
	private HashMap<String, String> innerClassMap;

	private boolean interfaceClass;

	private boolean innerClass;
	private ArrayList<ClassEntity> innerClasses;

	private int modifiers;

	private ClassEntity outerClass;
	private String innerName;

	public boolean isNeedExport() {
		if (hasClassExportAnnotation) {
			return true;
		} else if (fields != null && fields.size() > 0) {
			return true;
		} else if (methods != null && methods.size() > 0) {
			return true;
		} else if (constructors != null && constructors.size() > 0) {
			return true;
		}
		return false;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public ClassEntity getOuterClass() {
		return outerClass;
	}

	public void setOuterClass(ClassEntity outerClass) {
		this.outerClass = outerClass;
	}

	public String getInnerName() {
		return innerName;
	}

	public void setInnerName(String innerName) {
		this.innerName = innerName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public TypeEntity getExtendsName() {
		return extendsName;
	}

	public void setExtendsName(TypeEntity extendsName) {
		this.extendsName = extendsName;
	}

	public ArrayList<TypeEntity> getImplementsList() {
		return implementsList;
	}

	public void setImplementsList(ArrayList<TypeEntity> implementsList) {
		this.implementsList = implementsList;
	}

	public ArrayList<FieldEntity> getFields() {
		return fields;
	}

	public void setFields(ArrayList<FieldEntity> fields) {
		this.fields = fields;
	}

	public ArrayList<MethodEntity> getMethods() {
		return methods;
	}

	public void setMethods(ArrayList<MethodEntity> methods) {
		this.methods = methods;
	}

	public boolean isInnerClass() {
		return innerClass;
	}

	public void setInnerClass(boolean innerClass) {
		this.innerClass = innerClass;
	}

	public boolean isStaticClass() {
		return Modifier.isStatic(modifiers);
	}

	public ArrayList<ClassEntity> getInnerClasses() {
		return innerClasses;
	}

	public void setInnerClasses(ArrayList<ClassEntity> innerClasses) {
		this.innerClasses = innerClasses;
	}

	public boolean isInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(boolean interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public boolean isAbstractClass() {
		return Modifier.isAbstract(modifiers);
	}

	public ArrayList<TypeParameterEntity> getTypeParameters() {
		return typeParameters;
	}

	public void setTypeParameters(ArrayList<TypeParameterEntity> typeParameters) {
		this.typeParameters = typeParameters;
		if (typeParameters != null) {
			typeParameterMap = new HashMap<>();
			for (TypeParameterEntity entity : typeParameters) {
				typeParameterMap.put(entity.getSimpleName(), entity);
			}
		}
	}

	public int getModifiers() {
		if (isInterfaceClass()) {
			return modifiers + Opcodes.ACC_INTERFACE;
		}
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public ArrayList<ConstructorEntity> getConstructors() {
		return constructors;
	}

	public void setConstructors(ArrayList<ConstructorEntity> constructors) {
		this.constructors = constructors;
	}

	public HashMap<String, String> getInnerClassMap() {
		return innerClassMap;
	}

	public void setInnerClassMap(HashMap<String, String> innerClassMap) {
		this.innerClassMap = innerClassMap;
	}

	public TypeParameterEntity getTypeParameter(String name) {
		if (typeParameterMap != null) {
			return typeParameterMap.get(name);
		}
		return null;
	}

	public boolean isHasClassExportAnnotation() {
		return hasClassExportAnnotation;
	}

	public void setHasClassExportAnnotation(boolean hasClassExportAnnotation) {
		this.hasClassExportAnnotation = hasClassExportAnnotation;
	}

}
