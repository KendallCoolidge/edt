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

/**
 * SQLLiteral AST node type.
 *
 * @author Albert Ho
 * @author David Murray
 */
public class SQLLiteral extends LiteralExpression {

	private String SQLCONDITION;

	public SQLLiteral(String SQLCONDITION, int startOffset, int endOffset) {
		super(startOffset, endOffset);
		
		this.SQLCONDITION = SQLCONDITION;		
	}
	
	public String getValue() {
		return SQLCONDITION;
	}
	
	public String getCanonicalString() {
  		return SQLCONDITION;
    }
	
	public void accept(IASTVisitor visitor) {
		visitor.visit(this);		
		visitor.endVisit(this);
	}
	
	public int getLiteralKind() {
		return SQLSTMT_LITERAL;
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return new SQLLiteral(new String(SQLCONDITION), getOffset(), getOffset() + getLength());
	}
}
