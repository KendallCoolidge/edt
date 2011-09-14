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
package org.eclipse.edt.gen.javascript.templates;

import java.util.List;

import org.eclipse.edt.gen.javascript.CommonUtilities;
import org.eclipse.edt.gen.javascript.Constants;
import org.eclipse.edt.gen.javascript.Context;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.ConstantField;
import org.eclipse.edt.mof.egl.EGLClass;
import org.eclipse.edt.mof.egl.Field;
import org.eclipse.edt.mof.egl.Function;
import org.eclipse.edt.mof.egl.Library;
import org.eclipse.edt.mof.egl.Member;
import org.eclipse.edt.mof.egl.NamedElement;
import org.eclipse.edt.mof.egl.Part;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.utils.IRUtils;
import org.eclipse.edt.mof.egl.utils.TypeUtils;

public class EGLClassTemplate extends JavaScriptTemplate {

	private static final String INITIALIZER_FUNCTION = "eze$$setInitial";
	private static final String DEFAULTS_FUNCTION = "eze$$setEmpty";

	public void preGenClassBody(EGLClass part, Context ctx) {
		ctx.invoke(preGenUsedParts, part, ctx);
		ctx.invoke(preGenFields, part, ctx);
		ctx.invoke(preGenFunctions, part, ctx);
		if (part.getAnnotation(org.eclipse.edt.gen.Constants.AnnotationXMLRootElement) == null) {
			// add an xmlRootElement
			try {
				Annotation annotation = CommonUtilities.getAnnotation(ctx, Type.EGL_KeyScheme + Type.KeySchemeDelimiter + org.eclipse.edt.gen.Constants.AnnotationXMLRootElement);
				annotation.setValue("name", part.getId());
				part.addAnnotation(annotation);
			}
			catch (Exception e) {}
		}
		addNamespaceMap(part, ctx);
	}

	private void addNamespaceMap(EGLClass part, Context ctx) {
		String localName = part.getName();
		String namespace = CommonUtilities.createNamespaceFromPackage(part);
		Annotation annot = part.getAnnotation("eglx.xml.binding.annotation.xmlRootElement");
		if (annot != null) {
			if (annot.getValue("namespace") != null && ((String) annot.getValue("namespace")).length() > 0) {
				namespace = (String) annot.getValue("namespace");
			}
			if (annot.getValue("name") != null && ((String) annot.getValue("name")).length() > 0) {
				localName = (String) annot.getValue("name");
			}

		}
		ctx.addNamespace(namespace, localName, part.getFullyQualifiedName());
	}

	public void preGenUsedParts(EGLClass part, Context ctx) {
		for (Part item : IRUtils.getReferencedPartsFor(part)) {
			ctx.invoke(preGenUsedPart, part, ctx, item);
		}
	}

	public void preGenUsedPart(EGLClass part, Context ctx, Part arg) {
		ctx.invoke(preGen, arg, ctx);
	}

	public void preGenFields(EGLClass part, Context ctx) {
		for (Field field : part.getFields()) {
			ctx.invoke(preGenField, part, ctx, field);
		}
	}

	public void preGenField(EGLClass part, Context ctx, Field arg) {
		ctx.invoke(preGen, arg, ctx);
	}

	public void preGenFunctions(EGLClass part, Context ctx) {
		for (Function function : part.getFunctions()) {
			ctx.invoke(preGenFunction, part, ctx, function);
		}
	}

	public void preGenFunction(EGLClass part, Context ctx, Function arg) {
		ctx.invoke(preGen, arg, ctx);
	}

	public void genClassHeader(EGLClass part, Context ctx, TabbedWriter out) {
		out.print("egl.defineClass(");
		out.print(singleQuoted(part.getPackageName().toLowerCase()));
		out.print(", ");
		out.print(quoted(part.getName()));
		out.print(", ");
		ctx.invoke(genSuperClass, part, ctx, out);
		out.print(", ");
		out.println("{");
		out.print(quoted("eze$$fileName"));
		out.print(" : ");
		out.print(quoted(part.getFileName()));
		out.println(", ");
	}

	public void genClassBody(EGLClass part, Context ctx, TabbedWriter out) {
		ctx.invoke(genConstructors, part, ctx, out);
		ctx.invoke(genSetEmptyMethods, part, ctx, out);
		ctx.invoke(genInitializeMethods, part, ctx, out);
		ctx.invoke(genCloneMethods, part, ctx, out);
		// genGetFieldSignaturesMethod(part, ctx, out, args);
		ctx.invoke(genAnnotations, part, ctx, out);
		ctx.invoke(genFieldAnnotations, part, ctx, out);
		ctx.invoke(genFunctions, part, ctx, out);
		ctx.invoke(genFields, part, ctx, out);
		ctx.invoke(genGetterSetters, part, ctx, out);
		ctx.invoke(genToString, part, ctx, out);
	}

	public void genToString(EGLClass part, Context ctx, TabbedWriter out) {
		out.println(",");
		out.print(quoted("toString"));
		out.println(": function() {");
		out.println("return \"[" + part.getId() + "]\";");
		out.println("}");
	}

	public void genFields(EGLClass part, Context ctx, TabbedWriter out) {
		for (Field field : part.getFields()) {
			ctx.invoke(genField, part, ctx, out, field);
		}
	}

	public void genField(EGLClass part, Context ctx, TabbedWriter out, Field arg) {}

	public void genConstructors(EGLClass part, Context ctx, TabbedWriter out) {
		ctx.invoke(genConstructor, part, ctx, out);
	}

	public void genConstructor(EGLClass part, Context ctx, TabbedWriter out) {
		// Generate default constructor
		out.print(quoted("constructor"));
		out.println(": function() {");
		ctx.invoke(genLibraries, part, ctx, out);
		out.println("this."+ INITIALIZER_FUNCTION + "();");
		out.println("}");
	}

	@SuppressWarnings("unchecked")
	public void genLibraries(EGLClass part, Context ctx, TabbedWriter out) {
		List<Library> libraries = (List<Library>) ctx.getAttribute(ctx.getClass(), Constants.SubKey_partLibrariesUsed);
		for (Library library : libraries) {
			ctx.invoke(genLibrary, part, ctx, out, library);
		}
	}

	public void genLibrary(EGLClass part, Context ctx, TabbedWriter out, Library arg) {
		ctx.invoke(genInstantiation, arg, ctx, out);
		out.println(";");
	}

	public void genSetEmptyMethods(EGLClass part, Context ctx, TabbedWriter out) {
		out.println(",");
		out.print(quoted(DEFAULTS_FUNCTION));
		out.println(": function() {");
		ctx.invoke(genSetEmptyMethodBody, part, ctx, out);
		out.println("}");
	}

	public void genSetEmptyMethodBody(EGLClass part, Context ctx, TabbedWriter out) {
		for (Field field : part.getFields()) {
			ctx.invoke(genSetEmptyMethod, part, ctx, out, field);
		}
	}

	public void genSetEmptyMethod(EGLClass part, Context ctx, TabbedWriter out, Field arg) {
		out.print("this.");
		ctx.invoke(genName, arg, ctx, out);
		out.print(" = ");
		ctx.invoke(genInitialization, arg, ctx, out);
		out.println(";");
	}

	public void genInitializeMethods(EGLClass part, Context ctx, TabbedWriter out) {
		out.println(",");
		out.print(quoted(INITIALIZER_FUNCTION));
		out.println(": function() {");
		ctx.invoke(genInitializeMethodBody, part, ctx, out);
		out.println("}");
	}

	public void genInitializeMethodBody(EGLClass part, Context ctx, TabbedWriter out) {
		out.println("this."+DEFAULTS_FUNCTION + "();");
		for (Field field : part.getFields()) {
			ctx.invoke(genInitializeMethod, part, ctx, out, field);
		}

		/*
		 * If there are part-level initializer statements, process them AFTER the field-level initializers so that they take
		 * precedence. TODO confirm this with Tim et al.
		 */
		if (part.getInitializerStatements() != null) {
			ctx.invoke(genStatementNoBraces, part.getInitializerStatements(), ctx, out);
		}
	}
	
	public void genInitializeMethod(EGLClass part, Context ctx, TabbedWriter out, Field arg) {
		if (!(arg instanceof ConstantField)) {
			ctx.invoke(genInitializeStatement, arg.getType(), ctx, out, arg);
		}
	}

	public void genInitializerStatements(Field field, Context ctx, TabbedWriter out) {
		ctx.invoke(genStatementNoBraces, field.getInitializerStatements(), ctx, out);
	}

	public void genCloneMethods(EGLClass part, Context ctx, TabbedWriter out) {
		out.println(",");
		out.print(quoted("eze$$clone"));
		out.println(": function() {");
		ctx.invoke(genCloneMethodBody, part, ctx, out);
		out.println("}");
	}

	public void genCloneMethodBody(EGLClass part, Context ctx, TabbedWriter out) {
		String temp1 = "ezert$$1";
		String temp2 = "ezert$$2";
		out.print("var ");
		out.print(temp1);
		out.println(" = this;");
		out.print("var ");
		out.print(temp2);
		out.print(" = ");
		ctx.invoke(genInstantiation, part, ctx, out);
		out.println(";");

//		out.print(temp2);
//		out.println(".eze$$isNull = this.eze$$isNull;");

//		out.print(temp2);
//		out.println(".eze$$isNullable = this.eze$$isNullable;");

		// clone fields
		for (Field field : part.getFields()) {
			ctx.invoke(genCloneMethod, part, ctx, out, field);
		}

//		out.print(temp2);
//		out.print(".setNull(");
//		out.print(temp1);
//		out.println("eze$$isNull);");

		out.print("return ");
		out.print(temp2);
		out.println(";");
	}

	public void genCloneMethod(EGLClass part, Context ctx, TabbedWriter out, Field arg) {
		String temp1 = "ezert$$1";
		String temp2 = "ezert$$2";
		out.print(temp2);
		out.print(".");
		ctx.invoke(genName, arg, ctx, out);
		out.print(" = ");
		out.print(temp1);
		out.print(".");
		ctx.invoke(genName, arg, ctx, out);
		if (arg.isNullable() || TypeUtils.isReferenceType(arg.getType())) {
			out.print(" === null ? null : ");
			out.print(temp1);
			out.print(".");
			ctx.invoke(genName, arg, ctx, out);
			ctx.invoke(genCloneMethod, arg.getType(), ctx, out);
		}
		out.println(";");
	}

	public void genGetterSetters(EGLClass part, Context ctx, TabbedWriter out) {
		for (Field field : part.getFields()) {
			ctx.invoke(genGetterSetter, part, ctx, out, field);
		}
	}

	// we only generate getter and setters for fields within records or libraries, so do nothing if it gets back here
	public void genGetterSetter(EGLClass part, Context ctx, TabbedWriter out, Field arg) {}

	public void genFunctions(EGLClass part, Context ctx, TabbedWriter out) {
		for (Function function : part.getFunctions()) {
			ctx.invoke(genFunction, part, ctx, out, function);
		}
	}

	public void genFunction(EGLClass part, Context ctx, TabbedWriter out, Function arg) {
		out.println(",");
		ctx.invoke(genDeclaration, arg, ctx, out);
	}

	public void genAdditionalConstructorParams(EGLClass part, Context ctx, TabbedWriter out) {}

	public void genAdditionalSuperConstructorArgs(EGLClass part, Context ctx, TabbedWriter out) {}

	public void genDeclaration(EGLClass part, Context ctx, TabbedWriter out) {}

	public void genSuperClass(EGLClass part, Context ctx, TabbedWriter out) {}

	public void genAnnotations(EGLClass part, Context ctx, TabbedWriter out) {
		out.println(",");
		out.print(quoted("eze$$getAnnotations"));
		out.println(": function() {");
		out.println("if(this.annotations === undefined){");
		out.println("this.annotations = {};");
		for (Annotation annot : part.getAnnotations()) {
			ctx.invoke(genConversionControlAnnotation, annot.getEClass(), ctx, out, annot, part);
		}
		out.println("}");
		out.println("return this.annotations;");
		out.println("}");
	}

	public void genFieldAnnotations(EGLClass part, Context ctx, TabbedWriter out) {
		out.println(",");
		out.print(quoted("eze$$getFieldInfos"));
		out.println(": function() {");
		out.println("if(this.fieldInfos === undefined){");
		out.println("var annotations;");
		out.println("this.fieldInfos = new Array();");
		int idx = 0;
		for (Field field : part.getFields()) {
			if (field instanceof ConstantField || field.isStatic()) {
				continue;
			}
			ctx.invoke(genAnnotations, field, ctx, out, Integer.valueOf(idx));
			idx++;
		}
		out.println("}");
		out.println("return this.fieldInfos;");
		out.println("}");
	}

	public void genQualifier(EGLClass part, Context ctx, TabbedWriter out, NamedElement arg) {
		for (Member mbr : part.getAllMembers()) {
			if (mbr.getId().equalsIgnoreCase(arg.getId())) {
				out.print("this.");
				break;
			}
		}
	}
}
