/*******************************************************************************
 * Copyright � 2008, 2010 IBM Corporation and others.
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

public class CallbackTarget extends Node {
	private Expression expr;

	public CallbackTarget(Expression expr, int startOffset, int endOffset) {
		super(startOffset, endOffset);
		
		this.expr = expr;
		expr.setParent(this);
	}
	
	public Expression getExpression() {
		return expr;
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			expr.accept(visitor);
		}
		visitor.endVisit(this);
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return new CallbackTarget((Expression)expr.clone(), getOffset(), getOffset() + getLength());
	}

}
