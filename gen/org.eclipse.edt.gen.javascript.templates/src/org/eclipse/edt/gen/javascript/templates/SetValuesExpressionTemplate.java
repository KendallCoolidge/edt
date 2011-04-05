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

import org.eclipse.edt.gen.javascript.Context;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.SetValuesExpression;

public class SetValuesExpressionTemplate extends JavascriptTemplate {

	public void genExpression(SetValuesExpression expr, Context ctx, TabbedWriter out, Object... args) {
		if (expr.getTarget() != null)
			ctx.gen(genExpression, expr.getTarget(), ctx, out, args);
		if (expr.getSettings() != null)
			ctx.gen(genStatement, expr.getSettings(), ctx, out, args);
	}
}
