package cn.hugoz.amc.parser;

import java.lang.reflect.Modifier;

public class FieldEntity {

	// int/long/Object
	private TypeEntity fieldType;
	private String filedName;

	private int modifiers;


	public TypeEntity getFieldType() {
		return fieldType;
	}

	public void setFieldType(TypeEntity fieldType) {
		this.fieldType = fieldType;
	}

	public String getFiledName() {
		return filedName;
	}

	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}

	public boolean isStaticField() {
		return Modifier.isStatic(modifiers);
	}

	public boolean isFinalField() {
		return Modifier.isFinal(modifiers);
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
}
