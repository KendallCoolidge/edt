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
package org.eclipse.edt.gen.java.templates.jee;

import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.gen.java.jee.Constants;
import org.eclipse.edt.gen.java.templates.JavaTemplate;
import org.eclipse.edt.mof.EType;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.AnnotationType;
import org.eclipse.edt.mof.egl.EGLClass;
import org.eclipse.edt.mof.egl.Member;

public class ETypeTemplate extends JavaTemplate implements Constants {

	public void preGen(EType aType, Context ctx, Annotation annot, EGLClass part) {}

	public void genAnnotation(EType type, Context ctx, TabbedWriter out, Annotation annot) {}

	public void genAnnotation(EType type, Context ctx, TabbedWriter out, Annotation annot, Member member) {}

	public void genAnnotation(AnnotationType aType, Context ctx, TabbedWriter out, Annotation annot) {}
}
