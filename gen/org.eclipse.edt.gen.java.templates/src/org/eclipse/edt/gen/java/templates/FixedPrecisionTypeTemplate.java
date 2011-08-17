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
import org.eclipse.edt.mof.egl.FixedPrecisionType;

public class FixedPrecisionTypeTemplate extends JavaTemplate {

	public void genTypeDependentOptions(FixedPrecisionType type, Context ctx, TabbedWriter out) {
		out.print(", ");
		out.print(type.getLength());
		out.print(", ");
		out.print(type.getDecimals());
	}
	
	public void genJsonTypeDependentOptions(FixedPrecisionType type, Context ctx, TabbedWriter out) {
		out.print("\"");
		out.print(type.getLength());
		out.print("\", \"");
		out.print(type.getDecimals());
		out.print("\"");
	}
	
}
