/*******************************************************************************
 * Copyright � 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.mof.egl;


public interface Field extends Member {
	StatementBlock getInitializerStatements();
	
	void setInitializerStatements(StatementBlock value);
	
	Boolean hasSetValuesBlock();
	
	void setHasSetValuesBlock(Boolean value);
	
	Boolean isImplicit();
	
	void setIsImplicit(Boolean value);
	
	Boolean isSystemField();
	
	void setIsSystemField(Boolean value);
	
	
	public Container getDeclarer();
}