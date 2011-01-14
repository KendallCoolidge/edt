/*******************************************************************************
 * Copyright � 2008, 2010 IBM Corporation and others.
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

import org.eclipse.edt.compiler.internal.dli.DLIInfo;


/**
 * @author Harmon
 */
public interface IDliIOStatement{
    List getTargets();
    DLIInfo getDliInfo();
    void setDliInfo(DLIInfo dliInfo);
    void accept(IASTVisitor visitor);
}
