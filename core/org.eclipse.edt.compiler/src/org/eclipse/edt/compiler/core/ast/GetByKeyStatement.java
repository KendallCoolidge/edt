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

import java.util.List;

import org.eclipse.edt.compiler.internal.dli.DLIInfo;
import org.eclipse.edt.compiler.internal.sql.SQLInfo;


/**
 * GetByKeyStatement AST node type.
 * 
 * The targets of this statement are accessible though getTargets(). For
 * clauses and options (using pcb ..., singlerow, etc...), invoke the
 * accept() method with an IASTVisitor that overrides visit() for the
 * following types:
 * 
 * - ForUpdateWithIDClause
 * - SingleRowClause
 * - WithIDClause
 * - WithInlineSQLClause
 * - WithInlineDLIClause
 * - IntoClause
 * - UsingClause
 * - UsingKeysClause
 * - UsingPCBClause
 *
 * @author Albert Ho
 * @author David Murray
 */
public class GetByKeyStatement extends Statement implements IDliIOStatement {

	private List exprs;	// List of Expressions
	private List getByKeyOptions;	// List of Nodes
	private DLIInfo dliInfo;
	private SQLInfo sqlInfo;

	public GetByKeyStatement(List exprs, List getByKeyOptions, int startOffset, int endOffset) {
		super(startOffset, endOffset);
		
		this.exprs = setParent(exprs);
		this.getByKeyOptions = setParent(getByKeyOptions);
	}
	
	public List getTargets() {
		return exprs;
	}
	
	public List getGetByKeyOptions() {
		return getByKeyOptions;
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			acceptChildren(visitor, exprs);
			acceptChildren(visitor, getByKeyOptions);
		}
		visitor.endVisit(this);
	}
	
	public List getIOObjects() {
		return getTargets();
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return new GetByKeyStatement(cloneList(exprs), cloneList(getByKeyOptions), getOffset(), getOffset() + getLength());
	}
    public DLIInfo getDliInfo() {
        return dliInfo;
    }
    public void setDliInfo(DLIInfo dliInfo) {
        this.dliInfo = dliInfo;
    }
    public SQLInfo getSqlInfo() {
        return sqlInfo;
    }
    public void setSqlInfo(SQLInfo sqlInfo) {
        this.sqlInfo = sqlInfo;
    }
}
