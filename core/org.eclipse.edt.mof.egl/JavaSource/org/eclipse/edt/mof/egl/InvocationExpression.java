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
package org.eclipse.edt.mof.egl;

import java.util.List;

public interface InvocationExpression extends Expression {
	String getId();
	void setId(String id);
	
	List<Expression> getArguments();
	
	Type getParameterTypeForArg(int index);
	
	InvocableElement getTarget();
	
	QualifiedFunctionInvocation addQualifier(Expression expr);
}
