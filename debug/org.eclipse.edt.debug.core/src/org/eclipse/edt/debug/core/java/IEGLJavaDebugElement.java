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
package org.eclipse.edt.debug.core.java;

import org.eclipse.edt.debug.core.IEGLDebugElement;

public interface IEGLJavaDebugElement extends IEGLDebugElement
{
	/**
	 * @return the underlying Java debug element.
	 */
	public Object getJavaDebugElement();
}
