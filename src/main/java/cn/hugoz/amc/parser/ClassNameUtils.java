package cn.hugoz.amc.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassNameUtils {
	public static final ArrayList<String> JAVA_LANG_CLASSES = new ArrayList<>();

	static {
		JAVA_LANG_CLASSES.add("AbstractMethodError");
		JAVA_LANG_CLASSES.add("AbstractStringBuilder");
		JAVA_LANG_CLASSES.add("Appendable");
		JAVA_LANG_CLASSES.add("ApplicationShutdownHooks");
		JAVA_LANG_CLASSES.add("ArithmeticException");
		JAVA_LANG_CLASSES.add("ArrayIndexOutOfBoundsException");
		JAVA_LANG_CLASSES.add("ArrayStoreException");
		JAVA_LANG_CLASSES.add("AssertionError");
		JAVA_LANG_CLASSES.add("AssertionStatusDirectives");
		JAVA_LANG_CLASSES.add("AutoCloseable");
		JAVA_LANG_CLASSES.add("Boolean");
		JAVA_LANG_CLASSES.add("BootstrapMethodError");
		JAVA_LANG_CLASSES.add("Byte");
		JAVA_LANG_CLASSES.add("Character");
		JAVA_LANG_CLASSES.add("CharacterData");
		JAVA_LANG_CLASSES.add("CharacterData00");
		JAVA_LANG_CLASSES.add("CharacterData01");
		JAVA_LANG_CLASSES.add("CharacterData02");
		JAVA_LANG_CLASSES.add("CharacterData0E");
		JAVA_LANG_CLASSES.add("CharacterDataLatin1");
		JAVA_LANG_CLASSES.add("CharacterDataPrivateUse");
		JAVA_LANG_CLASSES.add("CharacterDataUndefined");
		JAVA_LANG_CLASSES.add("CharacterName");
		JAVA_LANG_CLASSES.add("CharSequence");
		JAVA_LANG_CLASSES.add("Class");
		JAVA_LANG_CLASSES.add("ClassCastException");
		JAVA_LANG_CLASSES.add("ClassCircularityError");
		JAVA_LANG_CLASSES.add("ClassFormatError");
		JAVA_LANG_CLASSES.add("ClassLoader");
		JAVA_LANG_CLASSES.add("ClassLoaderHelper");
		JAVA_LANG_CLASSES.add("ClassNotFoundException");
		JAVA_LANG_CLASSES.add("ClassValue");
		JAVA_LANG_CLASSES.add("Cloneable");
		JAVA_LANG_CLASSES.add("CloneNotSupportedException");
		JAVA_LANG_CLASSES.add("Comparable");
		JAVA_LANG_CLASSES.add("Compiler");
		JAVA_LANG_CLASSES.add("ConditionalSpecialCasing");
		JAVA_LANG_CLASSES.add("Deprecated");
		JAVA_LANG_CLASSES.add("Double");
		JAVA_LANG_CLASSES.add("Enum");
		JAVA_LANG_CLASSES.add("EnumConstantNotPresentException");
		JAVA_LANG_CLASSES.add("Error");
		JAVA_LANG_CLASSES.add("Exception");
		JAVA_LANG_CLASSES.add("ExceptionInInitializerError");
		JAVA_LANG_CLASSES.add("Float");
		JAVA_LANG_CLASSES.add("FunctionalInterface");
		JAVA_LANG_CLASSES.add("IllegalAccessError");
		JAVA_LANG_CLASSES.add("IllegalAccessException");
		JAVA_LANG_CLASSES.add("IllegalArgumentException");
		JAVA_LANG_CLASSES.add("IllegalMonitorStateException");
		JAVA_LANG_CLASSES.add("IllegalStateException");
		JAVA_LANG_CLASSES.add("IllegalThreadStateException");
		JAVA_LANG_CLASSES.add("IncompatibleClassChangeError");
		JAVA_LANG_CLASSES.add("IndexOutOfBoundsException");
		JAVA_LANG_CLASSES.add("InheritableThreadLocal");
		JAVA_LANG_CLASSES.add("InstantiationError");
		JAVA_LANG_CLASSES.add("InstantiationException");
		JAVA_LANG_CLASSES.add("Integer");
		JAVA_LANG_CLASSES.add("InternalError");
		JAVA_LANG_CLASSES.add("InterruptedException");
		JAVA_LANG_CLASSES.add("Iterable");
		JAVA_LANG_CLASSES.add("LinkageError");
		JAVA_LANG_CLASSES.add("Long");
		JAVA_LANG_CLASSES.add("Math");
		JAVA_LANG_CLASSES.add("NegativeArraySizeException");
		JAVA_LANG_CLASSES.add("NoClassDefFoundError");
		JAVA_LANG_CLASSES.add("NoSuchFieldError");
		JAVA_LANG_CLASSES.add("NoSuchFieldException");
		JAVA_LANG_CLASSES.add("NoSuchMethodError");
		JAVA_LANG_CLASSES.add("NoSuchMethodException");
		JAVA_LANG_CLASSES.add("NullPointerException");
		JAVA_LANG_CLASSES.add("Number");
		JAVA_LANG_CLASSES.add("NumberFormatException");
		JAVA_LANG_CLASSES.add("Object");
		JAVA_LANG_CLASSES.add("OutOfMemoryError");
		JAVA_LANG_CLASSES.add("Override");
		JAVA_LANG_CLASSES.add("Package");
		JAVA_LANG_CLASSES.add("Process");
		JAVA_LANG_CLASSES.add("ProcessBuilder");
		JAVA_LANG_CLASSES.add("ProcessEnvironment");
		JAVA_LANG_CLASSES.add("ProcessImpl");
		JAVA_LANG_CLASSES.add("Readable");
		JAVA_LANG_CLASSES.add("ReflectiveOperationException");
		JAVA_LANG_CLASSES.add("Runnable");
		JAVA_LANG_CLASSES.add("Runtime");
		JAVA_LANG_CLASSES.add("RuntimeException");
		JAVA_LANG_CLASSES.add("RuntimePermission");
		JAVA_LANG_CLASSES.add("SafeVarargs");
		JAVA_LANG_CLASSES.add("SecurityException");
		JAVA_LANG_CLASSES.add("SecurityManager");
		JAVA_LANG_CLASSES.add("Short");
		JAVA_LANG_CLASSES.add("Shutdown");
		JAVA_LANG_CLASSES.add("StackOverflowError");
		JAVA_LANG_CLASSES.add("StackTraceElement");
		JAVA_LANG_CLASSES.add("StrictMath");
		JAVA_LANG_CLASSES.add("String");
		JAVA_LANG_CLASSES.add("StringBuffer");
		JAVA_LANG_CLASSES.add("StringBuilder");
		JAVA_LANG_CLASSES.add("StringCoding");
		JAVA_LANG_CLASSES.add("StringIndexOutOfBoundsException");
		JAVA_LANG_CLASSES.add("SuppressWarnings");
		JAVA_LANG_CLASSES.add("System");
		JAVA_LANG_CLASSES.add("SystemClassLoaderAction");
		JAVA_LANG_CLASSES.add("Terminator");
		JAVA_LANG_CLASSES.add("Thread");
		JAVA_LANG_CLASSES.add("ThreadDeath");
		JAVA_LANG_CLASSES.add("ThreadGroup");
		JAVA_LANG_CLASSES.add("ThreadLocal");
		JAVA_LANG_CLASSES.add("Throwable");
		JAVA_LANG_CLASSES.add("TypeNotPresentException");
		JAVA_LANG_CLASSES.add("UnknownError");
		JAVA_LANG_CLASSES.add("UnsatisfiedLinkError");
		JAVA_LANG_CLASSES.add("UnsupportedClassVersionError");
		JAVA_LANG_CLASSES.add("UnsupportedOperationException");
		JAVA_LANG_CLASSES.add("VerifyError");
		JAVA_LANG_CLASSES.add("VirtualMachineError");
		JAVA_LANG_CLASSES.add("Void");

	}

	private static String getFullName(String name, String packageName, HashMap<String, String> importsMap,
			HashMap<String, String> innerClassMap) {
		String fullName = null;
		if (innerClassMap != null) {
			fullName = innerClassMap.get(name);
		}
		if (fullName == null) {
			fullName = importsMap.get(name);
		}
		if (fullName == null) {
			if (JAVA_LANG_CLASSES.contains(name)) {
				fullName = "java.lang." + name;
			} else {
				if (packageName != null) {
					fullName = packageName + "." + name;
				} else {
					fullName = name;
				}
			}
		}
		return fullName;
	}

	// 方法级泛型>内部类>类泛型>import>同包>lang
	public static ClassName getClassName(FileEntity fileEntity, ClassEntity classEntity, String typeName) {
		ClassName className = new ClassName();
		
		String packageName = fileEntity.getPackageName();
		HashMap<String, String> importsMap = fileEntity.getClassNameMap();
		HashMap<String, String> innerClassMap = classEntity.getInnerClassMap();
		int index = typeName.indexOf(".");
		if (index != -1) {
			String[] splits = typeName.split("\\.");
			String outerClassName = null;
			int outerClassIndex = -1;
			for (int i = 0; i < (splits.length - 1); i++) {
				String s = splits[i];
				if (Character.isUpperCase(s.charAt(0))) {
					outerClassName = s;
					outerClassIndex = i;
					break;
				}
			}
			if (outerClassName != null) {
				String outerClassFullName = getFullName(outerClassName, packageName, importsMap, innerClassMap);
				StringBuilder sb = new StringBuilder(outerClassFullName);
				for (int i = outerClassIndex + 1; i < splits.length; i++) {
					sb.append("$").append(splits[i]);
				}
				String fullName = sb.toString();
				className.setFullName(fullName);
				className.setSimpleName(splits[splits.length - 1]);
			} else {
				className.setFullName(typeName);
				className.setSimpleName(splits[splits.length - 1]);
			}

		} else {
			String fullName = getFullName(typeName, packageName, importsMap, innerClassMap);
			className.setFullName(fullName);
			className.setSimpleName(typeName);
		}
		
		return className;
	}
}
