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

import java.util.Collections;
import java.util.List;

import org.eclipse.edt.compiler.core.IEGLConstants;


/**
 * Library AST node type.
 *
 * @author Albert Ho
 * @author David Murray
 */
public class Library extends Part {

	private Name partSubTypeOpt;

	public Library(Boolean privateAccessModifierOpt, SimpleName name, Name partSubTypeOpt, List classContents, int startOffset, int endOffset) {
		super(privateAccessModifierOpt, name, classContents, startOffset, endOffset);
		
		if(partSubTypeOpt != null) {
			this.partSubTypeOpt = partSubTypeOpt;
			partSubTypeOpt.setParent(this);
		}
	}
	
	public boolean hasSubType() {
		return partSubTypeOpt != null;
	}
	
	public Name getSubType() {
		return partSubTypeOpt;
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			name.accept(visitor);
			if(partSubTypeOpt != null) partSubTypeOpt.accept(visitor);
			acceptChildren(visitor, contents);
		}
		visitor.endVisit(this);
	}
	
	protected Object clone() throws CloneNotSupportedException {
		Name newPartSubTypeOpt = partSubTypeOpt != null ? (Name)partSubTypeOpt.clone() : null;
		
		return new Library(new Boolean(isPrivate), (SimpleName)name.clone(), newPartSubTypeOpt, cloneContents(), getOffset(), getOffset() + getLength());
	}
	
	public String getPartTypeName() {
		return  IEGLConstants.KEYWORD_LIBRARY;
	}
	
	public int getPartType() {
		return LIBRARY;
	}
	
	/**
	 * @deprecated There is no serviceReferences syntax in language anymore. This returns empty list
	 */
	public List getServiceReferences() {
		return Collections.EMPTY_LIST;
	}

}
