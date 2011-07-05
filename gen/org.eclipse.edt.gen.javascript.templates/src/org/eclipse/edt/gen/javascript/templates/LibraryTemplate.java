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

import org.eclipse.edt.gen.javascript.Constants;
import org.eclipse.edt.gen.javascript.Context;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Field;
import org.eclipse.edt.mof.egl.Function;
import org.eclipse.edt.mof.egl.Library;
import org.eclipse.edt.mof.egl.NamedElement;
import org.eclipse.edt.mof.egl.Part;
import org.eclipse.edt.mof.egl.Type;

public class LibraryTemplate extends JavaScriptTemplate {

	@SuppressWarnings("unchecked")
	public void preGen(Library library, Context ctx) {
		// process anything else the superclass needs to do
		ctx.invokeSuper(this, preGen, library, ctx);
		// ignore adding this entry to the list, if it is the part we are currently generating
		if (((Part) ctx.getAttribute(ctx.getClass(), Constants.Annotation_partBeingGenerated)).getFullyQualifiedName().equalsIgnoreCase(
			library.getFullyQualifiedName()))
			return;
		// when we get here, it is because a part is being referenced by the original part being generated. Add it to the
		// parts used table if it doesn't already exist
		boolean found = false;
		List<Library> libraries = (List<Library>) ctx.getAttribute(ctx.getClass(), Constants.Annotation_partLibrariesUsed);
		for (Library lib : libraries) {
			if (library.getTypeSignature().equalsIgnoreCase(lib.getTypeSignature())) {
				found = true;
				break;
			}
		}
		if (!found)
			libraries.add(library);
	}

	public void genSuperClass(Library library, Context ctx, TabbedWriter out) {
		out.print("ExecutableBase");
	}

	public void genClassBody(Library library, Context ctx, TabbedWriter out) {
		ctx.invoke(genConstructors, library, ctx, out);
		ctx.invoke(genFunctions, library, ctx, out);
		ctx.invoke(genFields, library, ctx, out);
		ctx.invoke(genGetterSetters, library, ctx, out);
		out.println(",");
		ctx.invoke(genXmlAnnotations, library, ctx, out);
		out.println(",");
		ctx.invoke(genNamespaceMap, library, ctx, out);
	}

	public void genClassHeader(Library library, Context ctx, TabbedWriter out) {
		// TODO sbg consider refactoring into a separate extension
		out.print("egl.defineRUILibrary(");
		out.print(singleQuoted(library.getPackageName().toLowerCase()));
		out.print(", ");
		out.print(singleQuoted(library.getName()));
		out.println(", ");
		out.println("{");
		out.print("'eze$$fileName': ");
		out.print(singleQuoted(library.getFileName()));
		out.println(", ");
		out.print("'eze$$runtimePropertiesFile': ");
		out.print(singleQuoted(library.getFullyQualifiedName()));
		out.println(", ");
	}

	public void genName(Library library, Context ctx, TabbedWriter out) {
		// TODO Use Aliaser stuff from RBD
		out.print(eglnamespace + library.getPackageName().toLowerCase() + "." + library.getName());
	}

	public void genAccessor(Library library, Context ctx, TabbedWriter out) {
		ctx.invoke(genName, library, ctx, out);
		out.print("['$inst']");
	}

	public void genConstructor(Library library, Context ctx, TabbedWriter out) {
		out.print(quoted("constructor"));
		out.println(": function() {");
		out.print("if(");
		ctx.invoke(genAccessor, library, ctx, out);
		out.print(") return ");
		ctx.invoke(genAccessor, library, ctx, out);
		out.println(";");
		ctx.invoke(genAccessor, library, ctx, out);
		out.println("=this;");
		ctx.invoke(genInitializeMethodBody, library, ctx, out);

		out.println("this.jsrt$SysVar = new egl.egl.core.SysVar();");
		// instantiate each user part
		List<Part> usedParts = library.getUsedParts();
		for (Part part : usedParts) {
			ctx.invoke(genInstantiation, part, ctx, out);
			out.println(";");
		}
		// instantiate each library
		ctx.invoke(genLibraries, library, ctx, out);
		out.println("}");
	}

	public void genSetEmptyMethods(Library library, Context ctx, TabbedWriter out) {}

	public void genSetEmptyMethod(Library library, Context ctx, TabbedWriter out) {}

	public void genInitializeMethods(Library library, Context ctx, TabbedWriter out) {}

	public void genInstantiation(Type type, Context ctx, TabbedWriter out) {
		out.print("new ");
		ctx.invoke(genName, type, ctx, out);
		out.print("()");
	}

	public void genGetterSetter(Library library, Context ctx, TabbedWriter out, Field arg) {
		ctx.invoke(genGetter, arg, ctx, out);
		out.println(",");
		ctx.invoke(genSetter, arg, ctx, out);
	}

	public void genRuntimeTypeName(Library library, Context ctx, TabbedWriter out, TypeNameKind arg) {
		ctx.invoke(genPartName, library, ctx, out);
	}

	public void genContainerBasedAccessor(Library library, Context ctx, TabbedWriter out, Function arg) {
		out.print("new egl.egl.jsrt.Delegate(");
		ctx.invoke(genAccessor, library, ctx, out);
		out.print(",");
		ctx.invoke(genName, library, ctx, out);
		out.print(".prototype.");
		ctx.invoke(genName, arg, ctx, out);
		out.print(")");
	}

	public void genQualifier(Library library, Context ctx, TabbedWriter out) {}

	public void genQualifier(Library library, Context ctx, TabbedWriter out, NamedElement arg) {
//		out.print("this.");
	}
}
