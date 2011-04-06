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

import org.eclipse.edt.gen.Label;
import org.eclipse.edt.gen.javascript.Context;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.ContinueStatement;

public class ContinueStatementTemplate extends JavaScriptTemplate {

	public void genStatementBody(ContinueStatement stmt, Context ctx, TabbedWriter out, Object... args) {
		// see if they specified a label
		if (stmt.getLabel() != null && stmt.getLabel().length() > 0) {
			out.print("continue " + stmt.getLabel());
		} else {
			Label label = null;
			if (stmt.getContinueType() == ContinueStatement.CONTINUE_FOR)
				label = ctx.searchLabelStack(Label.LABEL_TYPE_FOR);
			else if (stmt.getContinueType() == ContinueStatement.CONTINUE_FOREACH)
				label = ctx.searchLabelStack(Label.LABEL_TYPE_FOREACH);
			else if (stmt.getContinueType() == ContinueStatement.CONTINUE_OPENUI)
				label = ctx.searchLabelStack(Label.LABEL_TYPE_OPENUI);
			else if (stmt.getContinueType() == ContinueStatement.CONTINUE_WHILE)
				label = ctx.searchLabelStack(Label.LABEL_TYPE_WHILE);
			// if we did not find a label, search for an eligible generic one
			if (label == null)
				label = ctx.searchLabelStack(Label.LABEL_TYPE_GENERIC);
			// if we still don't have one, then ignore it
			if (label != null)
				out.print("continue " + label.getName());
		}
	}
}
