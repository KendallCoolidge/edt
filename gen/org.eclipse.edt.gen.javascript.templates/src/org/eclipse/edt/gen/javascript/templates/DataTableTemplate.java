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
import org.eclipse.edt.mof.egl.DataTable;
import org.eclipse.edt.mof.egl.Part;

public class DataTableTemplate extends JavaScriptTemplate {

	@SuppressWarnings("unchecked")
	public void preGen(DataTable dataTable, Context ctx) {
		// process anything else the superclass needs to do
		ctx.invokeSuper(this, preGen, dataTable, ctx);
		// ignore adding this entry to the list, if it is the part we are currently generating
		if (((Part) ctx.getAttribute(ctx.getClass(), Constants.SubKey_partBeingGenerated)).getFullyQualifiedName().equalsIgnoreCase(
			dataTable.getFullyQualifiedName()))
			return;
		// when we get here, it is because a part is being referenced by the original part being generated. Add it to the
		// parts used table if it doesn't already exist
		boolean found = false;
		List<DataTable> dataTables = (List<DataTable>) ctx.getAttribute(ctx.getClass(), Constants.SubKey_partDataTablesUsed);
		for (DataTable table : dataTables) {
			if (dataTable.getTypeSignature().equalsIgnoreCase(table.getTypeSignature())) {
				found = true;
				break;
			}
		}
		if (!found)
			dataTables.add(dataTable);
	}

	public void genClassBody(DataTable dataTable, Context ctx, TabbedWriter out) {}

	public void genSuperClass(DataTable dataTable, Context ctx, TabbedWriter out) {
		out.print("ExecutableBase");
	}

	public void genAccessor(DataTable dataTable, Context ctx, TabbedWriter out) {}

	public void genConstructor(DataTable dataTable, Context ctx, TabbedWriter out) {}
}
