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

import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.ExpressionStatement;
import org.eclipse.edt.mof.egl.utils.IRUtils;

public class ExpressionStatementTemplate extends JavaTemplate {
	private boolean processEnd = true;

	public void genStatementBody(ExpressionStatement stmt, Context ctx, TabbedWriter out) {
		// an expression statement with an expression that simply points at a member name is not valid and needs to be
		// ignored. normally, this won't happen in the IRs, but can occur when our statementblock processing logic alters the
		// set values expression statements, when resetting the slot for the temporary variable
		if (IRUtils.hasSideEffects(stmt.getExpr()))
			ctx.invoke(genExpression, stmt.getExpr(), ctx, out);
		else
			processEnd = false;
	}

	public void genStatementEnd(ExpressionStatement stmt, Context ctx, TabbedWriter out) {
		if (processEnd)
			ctx.invokeSuper(this, genStatementEnd, stmt, ctx, out);
	}
}
