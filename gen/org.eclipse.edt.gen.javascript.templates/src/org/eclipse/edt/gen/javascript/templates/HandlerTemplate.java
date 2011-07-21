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

import org.eclipse.edt.gen.javascript.Context;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Handler;
import org.eclipse.edt.mof.egl.NamedElement;
import org.eclipse.edt.mof.egl.Part;

public class HandlerTemplate extends JavaScriptTemplate {

	public void genSuperClass(Handler type, Context ctx, TabbedWriter out) {
		out.print("ExecutableBase");
	}

	public void genClassHeader(Handler library, Context ctx, TabbedWriter out) {
		// TODO sbg Not all handlers are RUIWidgets, so this may need to be refactored
		out.print("egl.defineRUIWidget(");
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

	public void genConstructor(Handler handler, Context ctx, TabbedWriter out) {
		out.print(quoted("constructor"));
		out.println(": function() {");
		ctx.invoke(genInitializeMethodBody, handler, ctx, out);

		out.println("this.jsrt$SysVar = new egl.egl.core.SysVar();");
		// instantiate each user part
		List<Part> usedParts = handler.getUsedParts();
		for (Part part : usedParts) {
			ctx.invoke(genInstantiation, part, ctx, out);
			out.println(";");
		}
		// instantiate each library
		ctx.invoke(genLibraries, handler, ctx, out);
		out.println("}");
	}

	public void genConstructorOptions(Handler type, Context ctx, TabbedWriter out) {}

	public void genRuntimeTypeName(Handler type, Context ctx, TabbedWriter out, TypeNameKind arg) {
		ctx.invoke(genPartName, type, ctx, out);
	}

	public void genQualifier(Handler type, Context ctx, TabbedWriter out, NamedElement arg) {
		out.print("this.");
	}
}
