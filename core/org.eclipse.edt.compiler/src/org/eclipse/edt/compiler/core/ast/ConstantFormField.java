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

import org.eclipse.edt.compiler.binding.FormFieldBinding;

/**
 * ConstantFormField AST node type.
 *
 * @author Albert Ho
 * @author David Murray
 */
public class ConstantFormField extends FormField {
	
	FormFieldBinding binding;

	public ConstantFormField(SettingsBlock settingsBlockOpt, Expression initializerOpt, int startOffset, int endOffset) {
		super(settingsBlockOpt, initializerOpt, startOffset, endOffset);
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			if(settingsBlockOpt != null) settingsBlockOpt.accept(visitor);
			if(initializerOpt != null) initializerOpt.accept(visitor);
		}
		visitor.endVisit(this);
	}
	
	public FormFieldBinding resolveBinding() {
		return binding;
	}
	
	public void setBinding(FormFieldBinding binding) {
		this.binding = binding;
	}

	protected Object clone() throws CloneNotSupportedException {
		SettingsBlock newSettingsBlockOpt = settingsBlockOpt != null ? (SettingsBlock)settingsBlockOpt.clone() : null;
		
		Expression newInitializerOpt = initializerOpt != null ? (Expression)initializerOpt.clone() : null;
		
		return new ConstantFormField(newSettingsBlockOpt, newInitializerOpt, getOffset(), getOffset() + getLength());
	}
}
