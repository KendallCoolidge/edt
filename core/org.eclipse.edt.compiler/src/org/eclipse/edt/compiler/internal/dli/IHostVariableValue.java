/*******************************************************************************
 * Copyright © 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.compiler.internal.dli;

import org.eclipse.edt.compiler.core.ast.Expression;

/**
 * @author winghong
 */
public interface IHostVariableValue extends IValue {
    
	public String getText();
    Expression getExpression();
    void setExpression(Expression expression);

}