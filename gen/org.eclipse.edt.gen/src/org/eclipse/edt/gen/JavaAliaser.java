/*******************************************************************************
 * Copyright © 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.gen;

import java.util.Properties;

public class JavaAliaser {
	/**
	 * A registry for the aliases that we have to use for Java's keywords.
	 */
	private static final Properties keywordCache = new Properties();

	static {
		// To prevent compilation errors, insert aliases for Java's keywords.
		keywordCache.put("abstract", "eze_abstract");
		keywordCache.put("assert", "eze_assert");
		keywordCache.put("boolean", "eze_boolean");
		keywordCache.put("break", "eze_break");
		keywordCache.put("byte", "eze_byte");
		keywordCache.put("case", "eze_case");
		keywordCache.put("catch", "eze_catch");
		keywordCache.put("char", "eze_char");
		keywordCache.put("class", "eze_class");
		keywordCache.put("const", "eze_const");
		keywordCache.put("continue", "eze_continue");
		keywordCache.put("default", "eze_default");
		keywordCache.put("do", "eze_do");
		keywordCache.put("double", "eze_double");
		keywordCache.put("else", "eze_else");
		keywordCache.put("extends", "eze_extends");
		keywordCache.put("false", "eze_false");
		keywordCache.put("final", "eze_final");
		keywordCache.put("finally", "eze_finally");
		keywordCache.put("float", "eze_float");
		keywordCache.put("for", "eze_for");
		keywordCache.put("goto", "eze_goto");
		keywordCache.put("if", "eze_if");
		keywordCache.put("implements", "eze_implements");
		keywordCache.put("import", "eze_import");
		keywordCache.put("instanceof", "eze_instanceof");
		keywordCache.put("int", "eze_int");
		keywordCache.put("interface", "eze_interface");
		keywordCache.put("long", "eze_long");
		keywordCache.put("native", "eze_native");
		keywordCache.put("new", "eze_new");
		keywordCache.put("null", "eze_null");
		keywordCache.put("package", "eze_package");
		keywordCache.put("private", "eze_private");
		keywordCache.put("protected", "eze_protected");
		keywordCache.put("public", "eze_public");
		keywordCache.put("return", "eze_return");
		keywordCache.put("short", "eze_short");
		keywordCache.put("static", "eze_static");
		keywordCache.put("strictfp", "eze_strictfp");
		keywordCache.put("super", "eze_super");
		keywordCache.put("switch", "eze_switch");
		keywordCache.put("synchronized", "eze_synchronized");
		keywordCache.put("this", "eze_this");
		keywordCache.put("throw", "eze_throw");
		keywordCache.put("throws", "eze_throws");
		keywordCache.put("transient", "eze_transient");
		keywordCache.put("true", "eze_true");
		keywordCache.put("try", "eze_try");
		keywordCache.put("void", "eze_void");
		keywordCache.put("volatile", "eze_volatile");
		keywordCache.put("while", "eze_while");
	}

	/**
	 * Maps names of classes in the java.lang package to their aliases.
	 */
	private static final Properties javaLangNames = new Properties();

	static {
		// Initialize javaLangNames.
		javaLangNames.put("AbstractMethodError", "eze_AbstractMethodError");
		javaLangNames.put("ArithmeticException", "eze_ArithmeticException");
		javaLangNames.put("ArrayIndexOutOfBoundsException", "eze_ArrayIndexOutOfBoundsException");
		javaLangNames.put("ArrayStoreException", "eze_ArrayStoreException");
		javaLangNames.put("AssertionError", "eze_AssertionError");
		javaLangNames.put("Boolean", "eze_Boolean");
		javaLangNames.put("Byte", "eze_Byte");
		javaLangNames.put("CharSequence", "eze_CharSequence");
		javaLangNames.put("Character", "eze_Character");
		javaLangNames.put("Class", "eze_Class");
		javaLangNames.put("ClassCastException", "eze_ClassCastException");
		javaLangNames.put("ClassCircularityError", "eze_ClassCircularityError");
		javaLangNames.put("ClassFormatError", "eze_ClassFormatError");
		javaLangNames.put("ClassLoader", "eze_ClassLoader");
		javaLangNames.put("ClassNotFoundException", "eze_ClassNotFoundException");
		javaLangNames.put("CloneNotSupportedException", "eze_CloneNotSupportedException");
		javaLangNames.put("Cloneable", "eze_Cloneable");
		javaLangNames.put("Comparable", "eze_Comparable");
		javaLangNames.put("Compiler", "eze_Compiler");
		javaLangNames.put("Double", "eze_Double");
		javaLangNames.put("Error", "eze_Error");
		javaLangNames.put("Exception", "eze_Exception");
		javaLangNames.put("ExceptionInInitializerError", "eze_ExceptionInInitializerError");
		javaLangNames.put("Float", "eze_Float");
		javaLangNames.put("IllegalAccessError", "eze_IllegalAccessError");
		javaLangNames.put("IllegalAccessException", "eze_IllegalAccessException");
		javaLangNames.put("IllegalArgumentException", "eze_IllegalArgumentException");
		javaLangNames.put("IllegalMonitorStateException", "eze_IllegalMonitorStateException");
		javaLangNames.put("IllegalStateException", "eze_IllegalStateException");
		javaLangNames.put("IllegalThreadStateException", "eze_IllegalThreadStateException");
		javaLangNames.put("IncompatibleClassChangeError", "eze_IncompatibleClassChangeError");
		javaLangNames.put("IndexOutOfBoundsException", "eze_IndexOutOfBoundsException");
		javaLangNames.put("InheritableThreadLocal", "eze_InheritableThreadLocal");
		javaLangNames.put("InstantiationError", "eze_InstantiationError");
		javaLangNames.put("InstantiationException", "eze_InstantiationException");
		javaLangNames.put("Integer", "eze_Integer");
		javaLangNames.put("InternalError", "eze_InternalError");
		javaLangNames.put("InterruptedException", "eze_InterruptedException");
		javaLangNames.put("LinkageError", "eze_LinkageError");
		javaLangNames.put("Long", "eze_Long");
		javaLangNames.put("Math", "eze_Math");
		javaLangNames.put("NegativeArraySizeException", "eze_NegativeArraySizeException");
		javaLangNames.put("NoClassDefFoundError", "eze_NoClassDefFoundError");
		javaLangNames.put("NoSuchFieldError", "eze_NoSuchFieldError");
		javaLangNames.put("NoSuchFieldException", "eze_NoSuchFieldException");
		javaLangNames.put("NoSuchMethodError", "eze_NoSuchMethodError");
		javaLangNames.put("NoSuchMethodException", "eze_NoSuchMethodException");
		javaLangNames.put("NullPointerException", "eze_NullPointerException");
		javaLangNames.put("Number", "eze_Number");
		javaLangNames.put("NumberFormatException", "eze_NumberFormatException");
		javaLangNames.put("Object", "eze_Object");
		javaLangNames.put("OutOfMemoryError", "eze_OutOfMemoryError");
		javaLangNames.put("Package", "eze_Package");
		javaLangNames.put("Process", "eze_Process");
		javaLangNames.put("Runnable", "eze_Runnable");
		javaLangNames.put("Runtime", "eze_Runtime");
		javaLangNames.put("RuntimeException", "eze_RuntimeException");
		javaLangNames.put("RuntimePermission", "eze_RuntimePermission");
		javaLangNames.put("SecurityException", "eze_SecurityException");
		javaLangNames.put("SecurityManager", "eze_SecurityManager");
		javaLangNames.put("Short", "eze_Short");
		javaLangNames.put("StackOverflowError", "eze_StackOverflowError");
		javaLangNames.put("StackTraceElement", "eze_StackTraceElement");
		javaLangNames.put("StrictMath", "eze_StrictMath");
		javaLangNames.put("String", "eze_String");
		javaLangNames.put("StringBuffer", "eze_StringBuffer");
		javaLangNames.put("StringIndexOutOfBoundsException", "eze_StringIndexOutOfBoundsException");
		javaLangNames.put("System", "eze_System");
		javaLangNames.put("Thread", "eze_Thread");
		javaLangNames.put("ThreadDeath", "eze_ThreadDeath");
		javaLangNames.put("ThreadGroup", "eze_ThreadGroup");
		javaLangNames.put("ThreadLocal", "eze_ThreadLocal");
		javaLangNames.put("Throwable", "eze_Throwable");
		javaLangNames.put("UnknownError", "eze_UnknownError");
		javaLangNames.put("UnsatisfiedLinkError", "eze_UnsatisfiedLinkError");
		javaLangNames.put("UnsupportedClassVersionError", "eze_UnsupportedClassVersionError");
		javaLangNames.put("UnsupportedOperationException", "eze_UnsupportedOperationException");
		javaLangNames.put("VerifyError", "eze_VerifyError");
		javaLangNames.put("VirtualMachineError", "eze_VirtualMachineError");
		javaLangNames.put("Void", "eze_Void");
	}

	/**
	 * Returns true if name is a Java keyword that we should use an alias for.
	 * @param name the name to check.
	 * @return true if name is a Java keyword that we should use an alias for.
	 */
	public static boolean isJavaKeyword(String name) {
		return JavaAliaser.keywordCache.containsKey(name);
	}

	/**
	 * Returns an alias for the part name. Aliases are only different from the name when the name is a Java keyword. Java
	 * keywords are aliased by prefixing the keyword with eze_.
	 * @param partName the name of the part.
	 * @return either an alias for the part name, or the part name if it doesn't need an alias.
	 */
	public static String getAlias(String partName) {
		// check our cache of names so we don't have to examine each character in the part name every time.
		String alias = JavaAliaser.keywordCache.getProperty(partName);
		if (alias == null)
			return partName;
		else
			return alias;
	}

	/**
	 * This is the same as getAlias, with one more step. The value it returns will not be the name of any class in the
	 * java.lang package.
	 * @param partName the name of the part.
	 * @return either an alias for the part name, or the part name if it doesn't need an alias.
	 */
	public static String getJavaSafeAlias(String partName) {
		String alias = JavaAliaser.getAlias(partName);
		return JavaAliaser.javaLangNames.getProperty(alias, alias);
	}

	/**
	 * Return the package name in lowercase with folders separated by a separator character.
	 * @param pkg String array of folder names
	 * @param separator Character separator
	 * @return Legal Java gen package name
	 */
	public static String packageNameAlias(String[] pkg, char separator) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < pkg.length; i++) {
			if (i > 0)
				buff.append(separator);
			buff.append(getJavaSafeAlias(pkg[i]).toLowerCase());
		}
		return buff.toString();
	}

	/**
	 * @return a String made from packageName, lowercased, with each part aliased.
	 */
	public static String packageNameAlias(String packageName) {
		String unAliased = packageName.toLowerCase();
		String aliased = "";
		int dotIndex = unAliased.indexOf('.');
		if (dotIndex == -1)
			aliased = getJavaSafeAlias(unAliased);
		else {
			while (dotIndex != -1) {
				String piece = unAliased.substring(0, dotIndex);
				aliased = (aliased.length() == 0 ? aliased : aliased + '.') + getJavaSafeAlias(piece);
				unAliased = unAliased.substring(dotIndex + 1);
				dotIndex = unAliased.indexOf('.');
			}
			aliased = aliased + '.' + getJavaSafeAlias(unAliased);
		}
		return aliased;
	}

	public static boolean isValidJavaIdentifier(String str, boolean validateNotKeyword) {
		if (str == null)
			return false;
		if (str == "")
			return true;
		if (validateNotKeyword) {
			if (isJavaKeyword(str))
				return false;
		}
		if (!Character.isJavaIdentifierStart(str.charAt(0)))
			return false;
		for (int i = 1; i < str.length(); i++) {
			if (!Character.isJavaIdentifierPart(str.charAt(i)))
				return false;
		}
		return true;
	}
}
