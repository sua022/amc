package cn.hugoz.amc.parser;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class MethodEntity {

	private TypeEntity returnType;
	private String methodName;

	private ArrayList<MethodArgs> args;
	private ArrayList<TypeEntity> throwsList;
	private ArrayList<TypeParameterEntity> typeParameters;
	private HashMap<String, TypeParameterEntity> typeParameterMap;

	private int modifiers;


	public TypeEntity getReturnType() {
		return returnType;
	}

	public void setReturnType(TypeEntity returnType) {
		this.returnType = returnType;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public ArrayList<MethodArgs> getArgs() {
		return args;
	}

	public void setArgs(ArrayList<MethodArgs> args) {
		this.args = args;
	}


	public ArrayList<TypeEntity> getThrowsList() {
		return throwsList;
	}

	public void setThrowsList(ArrayList<TypeEntity> throwsList) {
		this.throwsList = throwsList;
	}

	public boolean isStaticMethod() {
		return Modifier.isStatic(modifiers);
	}

	public boolean isAbstractMethod() {
		return Modifier.isAbstract(modifiers);
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
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
	
	public TypeParameterEntity getTypeParameter(String name) {
		if (typeParameterMap != null) {
			return typeParameterMap.get(name);
		}
		return null;
	}

}
