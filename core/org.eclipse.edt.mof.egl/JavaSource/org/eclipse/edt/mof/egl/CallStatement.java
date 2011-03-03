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

import java.util.List;

public interface CallStatement extends Statement {
	Expression getInvocationTarget();
	
	void setInvocationTarget(Expression value);
	
	List<Expression> getArguments();
	
	Expression getCallback();
	
	void setCallback(Expression value);
	
	Expression getErrorCallback();
	
	void setErrorCallback(Expression value);
	
	String getLinkageKey(); 
	public Boolean isExternal();

}
