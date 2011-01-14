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

import java.util.Collections;
import java.util.List;

import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.core.IEGLConstants;


/**
 * NewExpression AST node type.
 *
 * @author Albert Ho
 * @author David Murray
 */
public class NewExpression extends Expression {

	private Type type;
	private boolean hasArgumentList;
	private List funcArgs;	// List of FunctionArguments
	private SettingsBlock settingsBlockOpt;
	
	private IDataBinding dataBindingForAnnotations;
	private IDataBinding constructorBinding;

	public NewExpression(Type type, List funcArgs, SettingsBlock settingsBlockOpt, int startOffset, int endOffset) {
		super(startOffset, endOffset);
		
		this.type = type;
		type.setParent(this);
		if(funcArgs == null) {
			this.funcArgs = Collections.EMPTY_LIST;
			hasArgumentList = false;
		}
		else {
			this.funcArgs = setParent(funcArgs);
			hasArgumentList = true;
		}
		if(settingsBlockOpt != null) {
			this.settingsBlockOpt = settingsBlockOpt;
			settingsBlockOpt.setParent(this);
		}
	}
	
	public Type getType() {
		return type;
	}
	
	public List getArguments() {
		return funcArgs;
	}
	
	public boolean hasArgumentList() {
		return hasArgumentList;
	}
	
    public String getCanonicalString() {
  		return IEGLConstants.KEYWORD_NEW + " " + type.getCanonicalName();
    }
    
	public boolean hasSettingsBlock() {
		return settingsBlockOpt != null;
	}
	
	public SettingsBlock getSettingsBlock() {
		return settingsBlockOpt;
	}
	
	public IDataBinding resolveConstructorBinding() {
		return constructorBinding;
	}
	
	public void setConstructorBinding(IDataBinding constructorBinding) {
		this.constructorBinding = constructorBinding;
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			type.accept(visitor);
			acceptChildren(visitor, funcArgs);
			if(settingsBlockOpt != null) settingsBlockOpt.accept(visitor);
		}
		visitor.endVisit(this);
	}
	
	protected Object clone() throws CloneNotSupportedException {
		SettingsBlock newSettingsBlockOpt = settingsBlockOpt != null ? (SettingsBlock)settingsBlockOpt.clone() : null;
		List newArgs = hasArgumentList ? cloneList(funcArgs) : null;
		
		return new NewExpression((Type)type.clone(), newArgs, newSettingsBlockOpt, getOffset(), getOffset() + getLength());
	}
    public IDataBinding getDataBindingForAnnotations() {
        return dataBindingForAnnotations;
    }
    public void setDataBindingForAnnotations(IDataBinding dataBindingForAnnotations) {
        this.dataBindingForAnnotations = dataBindingForAnnotations;
    }
}
