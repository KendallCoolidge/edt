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
package org.eclipse.edt.gen.javascriptdev.templates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.edt.gen.javascript.Context;
import org.eclipse.edt.gen.javascriptdev.Constants;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.EGLClass;
import org.eclipse.edt.mof.egl.Field;
import org.eclipse.edt.mof.egl.Library;
import org.eclipse.edt.mof.egl.LogicAndDataPart;
import org.eclipse.edt.mof.egl.StructPart;

public class EGLClassTemplate extends org.eclipse.edt.gen.javascript.templates.EGLClassTemplate {
	
	@Override
	public void genClassBody(EGLClass part, Context ctx, TabbedWriter out) {
		super.genClassBody(part, ctx, out);
		ctx.invoke(Constants.genDebugFunctions, part, ctx, out);
	}
	
	public void genGetName(EGLClass part, Context ctx, TabbedWriter out) {
		out.println(",");
		out.print(quoted("eze$$getName"));
		out.println(": function() {");
		out.println("return \"" + part.getCaseSensitiveName()+ "\";");
		out.println("}");
	}
	
	public void genDebugFunctions(EGLClass part, Context ctx, TabbedWriter out) {
		ctx.invoke(Constants.genGetName, part, ctx, out);
		ctx.invoke(Constants.genGetVariables, part, ctx, out);
	}
	
	public void genGetVariables(EGLClass part, Context ctx, TabbedWriter out) {
		out.println(",");
		out.print(quoted("eze$$getChildVariables"));
		out.println(": function() {");
		out.println("var eze$$parent = this;");
		out.print("return [");
		
		boolean first = true;
		List<Library> libraries = (List<Library>) ctx.getAttribute(ctx.getClass(), org.eclipse.edt.gen.javascript.Constants.SubKey_partLibrariesUsed);
		for (Library lib : libraries) {
			if (first) {
				first = false;
				out.print("\n");
			}
			else {
				out.print(",\n");
			}
			ctx.invoke(Constants.genGetVariablesEntry, lib, ctx, out);
		}
		
		List<Field> fields = new ArrayList<Field>();
		gatherFields(part, fields, new HashSet<LogicAndDataPart>());
		for (Field field : fields) {
			if (first) {
				first = false;
				out.print("\n");
			}
			else {
				out.print(",\n");
			}
			ctx.invoke(Constants.genGetVariablesEntry, field, ctx, out);
		}
		
		out.println("\n];");
		out.println("}");
	}
	
	private void gatherFields(LogicAndDataPart part, List<Field> fields, Set<LogicAndDataPart> seenParts) {
		if (seenParts.contains(part)) {
			return;
		}
		
		seenParts.add(part);
		fields.addAll(part.getFields());
		
		for (StructPart parent : part.getSuperTypes()) {
			if (parent instanceof LogicAndDataPart) {
				gatherFields((LogicAndDataPart)parent, fields, seenParts);
			}
		}
	}
	
	@Override
	public void genInitializeMethodBody(EGLClass part, Context ctx, TabbedWriter out) {
		out.println("try { egl.enter(\"<init>\",this,arguments);");
		
		super.genInitializeMethodBody(part, ctx, out);
		
		out.println("if (!egl.debugg) egl.leave();");
		out.println("} finally { ");
		out.println("if (!egl.debugg){ ");
		List widgetsAccess = (List)ctx.get( Constants.REFERENCES_WIDGETS );
		if( widgetsAccess != null && widgetsAccess.size() > 0 ) {
			for ( int i = 0; i < widgetsAccess.size(); i ++ ) {
				ctx.invoke("genReferencedWidgets", widgetsAccess.get(i), ctx, out);
			}
			widgetsAccess.clear();
		}
		
		out.println("} else { egl.leave(); } ");
		out.println("}");
	}
	
	public void genInitializerStatements(Field field, Context ctx, TabbedWriter out) {
		super.genInitializerStatements(field, ctx, out);
		ctx.invoke(Constants.genSetWidgetLocation, field, Boolean.FALSE, ctx, out);
	}
	
	@SuppressWarnings("unchecked")
	public void genLibraries(EGLClass part, Context ctx, TabbedWriter out) {

		super.genLibraries(part, ctx, out);
	}
}
