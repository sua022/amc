package cn.hugoz.amc.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class FileEntity {

	private String packageName;
	private ArrayList<String> imports;
	private HashMap<String, String> classNameMap = new HashMap<>();

	private ArrayList<ClassEntity> classEntities;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public ArrayList<String> getImports() {
		return imports;
	}

	public void setImports(ArrayList<String> imports) {
		this.imports = imports;
		if (imports != null) {
			for (String className : imports) {
				int index = className.indexOf(".");
				String simpleName;
				if (index != -1) {
					String[] splits = className.split("\\.");
					boolean hasOuterClass = false;
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < (splits.length - 1); i++) {
						String s = splits[i];
						if (Character.isUpperCase(s.charAt(0))) {
							hasOuterClass = true;
							if (i > 0) {
								sb.append(".");
							}
							sb.append(s);
						} else {
							if (hasOuterClass) {
								sb.append("$").append(s);
							} else {
								if (i > 0) {
									sb.append(".");
								}
								sb.append(s);
							}
						}
					}
					if (hasOuterClass) {
						sb.append("$");
					} else {
						sb.append(".");
					}
					simpleName = splits[splits.length - 1];
					sb.append(simpleName);
					classNameMap.put(simpleName, sb.toString());
				} else {
					simpleName = className;
					classNameMap.put(simpleName, className);
				}
			}
		}
	}

	public HashMap<String, String> getClassNameMap() {
		return classNameMap;
	}

	public ArrayList<ClassEntity> getClassEntities() {
		return classEntities;
	}

	public void setClassEntities(ArrayList<ClassEntity> classEntities) {
		this.classEntities = classEntities;
	}

}
