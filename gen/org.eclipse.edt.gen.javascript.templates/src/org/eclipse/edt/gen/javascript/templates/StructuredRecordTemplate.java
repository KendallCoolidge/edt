/*******************************************************************************
 * Copyright � 2011 IBM Corporation and others.
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
import org.eclipse.edt.mof.egl.StructuredRecord;

public class StructuredRecordTemplate extends JavascriptTemplate {

	public void validateClassBody(StructuredRecord part, Context ctx, Object... args) {}

	public void genPart(StructuredRecord part, Context ctx, TabbedWriter out, Object... args) {}

	public void genClassBody(StructuredRecord part, Context ctx, TabbedWriter out, Object... args) {}

	public void genClassHeader(StructuredRecord part, Context ctx, TabbedWriter out, Object... args) {}
}
