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
 * PackageDeclaration AST node type.
 *
 * @author Albert Ho
 * @author David Murray
 */
public class PackageDeclaration extends Node {

	private Name name;

	public PackageDeclaration(Name name, int startOffset, int endOffset) {
		super(startOffset, endOffset);
		
		this.name = name;
		name.setParent(this);
	}
	
	public Name getName() {
		return name;
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			name.accept(visitor);
		}
		visitor.endVisit(this);
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return new PackageDeclaration((Name)name.clone(), getOffset(), getOffset() + getLength());
	}
}
