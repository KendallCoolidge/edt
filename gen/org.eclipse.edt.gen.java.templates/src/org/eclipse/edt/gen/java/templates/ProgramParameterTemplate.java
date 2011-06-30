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
import org.eclipse.edt.mof.egl.AccessKind;
import org.eclipse.edt.mof.egl.ProgramParameter;
import org.eclipse.edt.mof.egl.utils.TypeUtils;

public class ProgramParameterTemplate extends JavaTemplate {

	public void genDeclaration(ProgramParameter field, Context ctx, TabbedWriter out) {
		AccessKind access = field.getAccessKind();
		if (access == AccessKind.ACC_PRIVATE)
			out.print("private ");
		else
			out.print("public ");
	}

	public void genRuntimeTypeName(ProgramParameter mbr, Context ctx, TabbedWriter out, TypeNameKind arg) {
		if (mbr.getType() == null)
			out.print("void");
		else if (ctx.mapsToPrimitiveType(mbr.getType().getClassifier()) && !mbr.isNullable() && TypeUtils.isValueType(mbr.getType()))
			ctx.invoke(genRuntimeTypeName, mbr.getType(), ctx, out, TypeNameKind.JavaPrimitive, mbr);
		else if (ctx.mapsToPrimitiveType(mbr.getType().getClassifier()))
			ctx.invoke(genRuntimeTypeName, mbr.getType(), ctx, out, TypeNameKind.JavaObject, mbr);
		else if (ctx.mapsToNativeType(mbr.getType().getClassifier()))
			ctx.invoke(genRuntimeTypeName, mbr.getType(), ctx, out, TypeNameKind.EGLInterface, mbr);
		else
			ctx.invoke(genRuntimeTypeName, mbr.getType(), ctx, out, TypeNameKind.JavaObject, mbr);
	}
}
