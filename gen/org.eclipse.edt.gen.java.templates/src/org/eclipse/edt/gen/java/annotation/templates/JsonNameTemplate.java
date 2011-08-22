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

public class JsonNameTemplate extends JavaTemplate {

	public void genConstructorOptions(AnnotationType annotType, Context ctx, TabbedWriter out, Annotation annot, Field field) {
		out.print("name=\"" + (String)annot.getValue() + "\"");
		out.print(", clazz=");
		ctx.invoke(genFieldTypeClassName, field.getType(), ctx, out, new Integer(0));
		out.print(".class, asOptions={");
		ctx.invoke(genJsonTypeDependentOptions, field.getType(), ctx, out);
		out.print("}");
	}
}
