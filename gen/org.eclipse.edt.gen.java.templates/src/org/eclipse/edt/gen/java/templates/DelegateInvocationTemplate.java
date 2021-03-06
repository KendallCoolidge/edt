/*******************************************************************************
 * Copyright © 2011, 2013 IBM Corporation and others.
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

import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.DelegateInvocation;
import org.eclipse.edt.mof.egl.utils.IRUtils;

public class DelegateInvocationTemplate extends JavaTemplate {

	public void genExpression(DelegateInvocation expr, Context ctx, TabbedWriter out) {
		// first, make this expression's arguments compatible
		IRUtils.makeCompatible(expr);
		// cast the return value, if present
		if (expr.getType() != null) {
			out.print("(");
			ctx.invoke(genRuntimeTypeName, expr.getType(), ctx, out, TypeNameKind.JavaObject);
			out.print(")");
		}
		// then process the function invocation
		if (expr.getQualifier() != null) {
			ctx.invoke(genExpression, expr.getQualifier(), ctx, out, expr.getQualifier());
			out.print(".");
		}
		ctx.invoke(genExpression, expr.getExpression(), ctx, out);
		out.print(".invoke");
		out.print("(");
		ctx.foreach(expr.getArguments(), ',', genExpression, ctx, out);
		out.print(")");
	}
}
