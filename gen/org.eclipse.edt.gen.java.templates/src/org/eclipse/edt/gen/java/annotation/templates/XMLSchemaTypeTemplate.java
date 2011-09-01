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
package org.eclipse.edt.gen.java.annotation.templates;

import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.gen.java.templates.JavaTemplate;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.AnnotationType;
import org.eclipse.edt.mof.egl.Field;
import org.eclipse.edt.mof.egl.Type;

public class XMLSchemaTypeTemplate extends JavaTemplate {

	public void genAnnotation(AnnotationType aType, Context ctx, TabbedWriter out, Annotation annot, Field field) {
		ctx.invokeSuper(this, genAnnotation, (Type)aType, ctx, out, Boolean.TRUE, annot, field);
	}
	public void genConstructorOptions(AnnotationType annotType, Context ctx, TabbedWriter out, Annotation annot, Field field) {
		out.print("name=\"" + (String)annot.getValue() + "\"");
		
		if(annot.getValue("namespace") instanceof String && !"http://www.w3.org/2001/XMLSchema".equals(annot.getValue("namespace"))){
			out.print(", ");
			out.print("namespace=\"" + (String)annot.getValue("namespace") + "\"");
		}
	}
}
