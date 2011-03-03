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

import java.util.List;

/**
 * FunctionDataDeclaration AST node type.
 *
 * @author Albert Ho
 * @author David Murray
 */
public class FunctionDataDeclaration extends Statement {

	private List ID_plus;	// List of SimpleNames
	private Type type;
	private SettingsBlock settingsBlockOpt;
	private Expression initializerOpt;
	private boolean isConstant;

	public FunctionDataDeclaration(List ID_plus, Type type, SettingsBlock settingsBlockOpt, Expression initializerOpt, boolean isConstant, int startOffset, int endOffset) {
		super(startOffset, endOffset);
		
		this.ID_plus = setParent( ID_plus );
		this.type = type;
		type.setParent(this);
		if(settingsBlockOpt != null) {
			this.settingsBlockOpt = settingsBlockOpt;
			settingsBlockOpt.setParent(this);
		}
		if(initializerOpt != null) {
			this.initializerOpt = initializerOpt;
			initializerOpt.setParent(this);
		}
		this.isConstant = isConstant;
	}
	
	public List getNames() {
		return ID_plus;
	}
	
	public Type getType() {
		return type;
	}
	
	public SettingsBlock getSettingsBlockOpt() {
		return settingsBlockOpt;
	}
	
	public boolean hasInitializer() {
		return initializerOpt != null;
	}

	public boolean hasSettingsBlock() {
		return settingsBlockOpt != null;
	}

	public Expression getInitializer() {
		return initializerOpt;
	}
	
	public boolean isConstant() {
		return isConstant;
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			acceptChildren(visitor, ID_plus);
			type.accept(visitor);
			if(settingsBlockOpt != null) settingsBlockOpt.accept(visitor);
			if(initializerOpt != null) initializerOpt.accept(visitor);
		}
		visitor.endVisit(this);
	}
	
	protected Object clone() throws CloneNotSupportedException {
		SettingsBlock newSettingsBlockOpt = settingsBlockOpt != null ? (SettingsBlock)settingsBlockOpt.clone() : null;
		
		Expression newInitializerOpt = initializerOpt != null ? (Expression)initializerOpt.clone() : null;
		
		return new FunctionDataDeclaration(cloneList(ID_plus), (Type)type.clone(), newSettingsBlockOpt, newInitializerOpt, isConstant, getOffset(), getOffset() + getLength());
		
	}
}
