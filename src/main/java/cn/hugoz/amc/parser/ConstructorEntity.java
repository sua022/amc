package cn.hugoz.amc.parser;

import java.util.ArrayList;

public class ConstructorEntity {

	private ArrayList<MethodArgs> args;
	private ArrayList<String> throwsList;

	private int modifiers;

	public ArrayList<MethodArgs> getArgs() {
		return args;
	}

	public void setArgs(ArrayList<MethodArgs> args) {
		this.args = args;
	}

	public ArrayList<String> getThrowsList() {
		return throwsList;
	}

	public void setThrowsList(ArrayList<String> throwsList) {
		this.throwsList = throwsList;
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
}
