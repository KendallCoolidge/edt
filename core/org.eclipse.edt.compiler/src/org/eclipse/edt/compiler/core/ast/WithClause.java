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
package org.eclipse.edt.compiler.core.ast;

public abstract class WithClause extends Node {

	public WithClause(int startOffset, int endOffset) {
		super(startOffset, endOffset);
	}
	
	public boolean isWithExpression() {
		return false;
	}
	
	public boolean isWithInline() {
		return false;
	}

	public boolean isWithInlineSQL() {
		return false;
	}

	public boolean isWithInlineDLI() {
		return false;
	}
}
