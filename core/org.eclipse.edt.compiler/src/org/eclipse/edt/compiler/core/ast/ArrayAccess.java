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
package org.eclipse.edt.compiler.core.ast;

import java.util.Iterator;
import java.util.List;

import org.eclipse.edt.mof.egl.Member;


/**
 * ArrayAccess AST node type.
 *
 * @author Albert Ho
 * @author David Murray
 */
public class ArrayAccess extends Expression {

	private Expression array;
	private List<Expression> subscripts;
	private Object element;

	public ArrayAccess(Expression primary, List<Expression> subscripts, int startOffset, int endOffset) {
		super(startOffset, endOffset);
		
		this.array = primary;
		array.setParent(this);
		this.subscripts = subscripts;
		setParent(subscripts);
	}
	
	public Expression getArray() {
		return array;
	}
	
	public List<Expression> getIndices() {
		return subscripts;
	}
	
	@Override
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			array.accept(visitor);
			acceptChildren(visitor, subscripts);
		}
		visitor.endVisit(this);
	}
	
	@Override
	public String getCanonicalString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getArray().getCanonicalString());
		sb.append("[");
		for(Iterator iter = subscripts.iterator(); iter.hasNext();) {
			sb.append(((Expression) iter.next()).getCanonicalString());
			if(iter.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public void setAttributeOnName(int attr, Object value) {
		array.setAttributeOnName(attr, value);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ArrayAccess((Expression)array.clone(), cloneList(subscripts), getOffset(), getOffset() + getLength());
	}
	
	@Override
	public Member resolveMember() {
		Member mbr = super.resolveMember();
		if (mbr != null) {
			return mbr;
		}
		return getArray().resolveMember();
	}
	
	@Override
	public void setElement(Object elem) {
        this.element = elem;
        super.setElement(elem);
    }
	
	@Override
    public Object resolveElement() {
    	return element;
    }
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(array.toString());
		buffer.append("[");
		Iterator i = subscripts.iterator();
		boolean first = true;
		while (i.hasNext()) {
			Expression ex = (Expression) i.next();
			if (!first) {
				buffer.append(",");
			}
			first = false;
			buffer.append(ex.toString());
		}
		buffer.append("]");
		return buffer.toString();
	}
}
