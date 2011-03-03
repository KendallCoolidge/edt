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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.edt.compiler.internal.sql.SQLInfo;


/**
 * OpenStatement AST node type.
 * 
 * For result set identifier, use:
 *  - getResultSetID()
 * 
 * For hold/scroll options, use:
 *  - hasHold(); 
 *  - hasScroll()
 * 
 * For clauses, invoke accept() with an IASTVisitor that overrides 
 * visit() for the following types: 
 *  - IntoClause 
 *  - ForExpressionClause 
 *  - ForUpdateClause  
 *  - UsingClause
 *  - UsingKeysClause
 *  - WithInlineSQLClause
 *  - WithIDClause    
 *
 * @author Albert Ho
 * @author David Murray
 */
public class OpenStatement extends Statement {

	private String resultSetID;
	private boolean isHold;
	private boolean isScroll;
	private List openTargets;	// List of Nodes
	private SQLInfo sqlInfo;
	
	private List ioObjects;

	public OpenStatement(String resultSetID, Boolean[] openModifierOpt, List openTargets, int startOffset, int endOffset) {
		super(startOffset, endOffset);
		
		this.resultSetID = resultSetID;
		
		// openModifierOpt is guaranteed by the action code in the parser
		// to be a two-element array
		isHold = openModifierOpt[0].booleanValue();
		isScroll = openModifierOpt[1].booleanValue();
		
		this.openTargets = setParent(openTargets);
	}
	
	public String getResultSetID() {
		return resultSetID;
	}

	public boolean hasHold() {
		return isHold;
	}
	
	public boolean hasScroll() {
		return isScroll;
	}
	
	public List getOpenTargets() {
		return openTargets;
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			acceptChildren(visitor, openTargets);
		}
		visitor.endVisit(this);
	}
	
	public List getIOObjects() {
		if(ioObjects == null) {
			ioObjects = Collections.EMPTY_LIST;
			acceptChildren(new DefaultASTVisitor() {
				public boolean visit(ForExpressionClause forExpressionClause) {
					if(ioObjects == Collections.EMPTY_LIST) {
						ioObjects = new ArrayList();
					}
					ioObjects.add(forExpressionClause.getExpression());
					return false;
				}
			}, openTargets);
		}
		return ioObjects;
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return new OpenStatement(new String(resultSetID), new Boolean[]{new Boolean(isHold), new Boolean(isScroll)}, cloneList(openTargets), getOffset(), getOffset() + getLength());
	}
    public SQLInfo getSqlInfo() {
        return sqlInfo;
    }
    public void setSqlInfo(SQLInfo sqlInfo) {
        this.sqlInfo = sqlInfo;
    }
}
