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
package org.eclipse.edt.mof.egl;


public interface Assignment extends Expression {
	LHSExpr getLHS();
	void setLHS(LHSExpr value);
	
	Expression getRHS();	
	void setRHS(Expression value);
	
	String getOperator();
	void setOperator(String opSymbol);

}
