/*******************************************************************************
 * Copyright � 2005, 2010 IBM Corporation and others.
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

import org.eclipse.edt.compiler.core.IEGLConstants;


/**
 * WithInlineDLIClause AST node type.
 *
 * @author Albert Ho
 * @author David Murray
 */
public class WithInlineDLIClause extends Node {
	
	public static IOStatementClauseInfo INFO = new IOStatementClauseInfo() {
		public String getClauseKeyword() {
			return IEGLConstants.KEYWORD_WITH;
		}

		public String getContentPrefix() {
			return "#SQL{";
		}

		public String getContentSuffix() {
			return "}";
		}

		public int getContentType() {
			return IOStatementClauseInfo.INLINE_STMT_VALUE;
		}		
	};

	private InlineDLIStatement dliStmt;

	public WithInlineDLIClause(InlineDLIStatement dliStmt, int startOffset, int endOffset) {
		super(startOffset, endOffset);
		
		this.dliStmt = dliStmt;
		dliStmt.setParent(this);
	}
	
	public InlineDLIStatement getDliStmt() {
		return dliStmt;
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			dliStmt.accept(visitor);
		}
		visitor.endVisit(this);
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return new WithInlineDLIClause((InlineDLIStatement) dliStmt.clone(), getOffset(), getOffset() + getLength());
	}
}
