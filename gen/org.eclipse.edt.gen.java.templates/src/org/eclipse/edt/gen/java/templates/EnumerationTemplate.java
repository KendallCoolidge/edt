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
package org.eclipse.edt.gen.java.templates;

import java.util.List;

import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.mof.EEnumLiteral;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Enumeration;
import org.eclipse.edt.mof.egl.Type;

public class EnumerationTemplate extends JavaTemplate {

	public void validateClassBody(Enumeration part, Context ctx) {}

	public void genClassBody(Enumeration part, Context ctx, TabbedWriter out) {
		// generate enumerated fields
		List<EEnumLiteral> enums = part.getEntries();
		if (enums != null && enums.size() != 0) {
			boolean needsSeparator = false;
			for (EEnumLiteral literal : enums) {
				if (needsSeparator)
					out.println(",");
				needsSeparator = true;
				ctx.invoke(genName, literal, ctx, out);
				out.print("(" + literal.getValue() + ")");
			}
			out.println(";");
		}
		out.println("private final int value;");
		ctx.invoke(genClassName, (Type) part, ctx, out);
		out.println("(int value) {");
		out.println("\tthis.value = value;");
		out.println("}");
		out.println("public int getValue() {");
		out.println("\treturn value;");
		out.println("}");
	}

	public void genClassHeader(Enumeration part, Context ctx, TabbedWriter out) {
		out.print("public enum ");
		ctx.invoke(genClassName, (Type) part, ctx, out);
		out.println(" {");
	}

	public void genAccessor(Enumeration part, Context ctx, TabbedWriter out) {
		ctx.invoke(genClassName, (Type) part, ctx, out);
	}

	public void genRuntimeTypeName(Enumeration part, Context ctx, TabbedWriter out, TypeNameKind arg) {
		ctx.invoke(genClassName, (Type) part, ctx, out);
	}
}
