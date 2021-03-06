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
package org.eclipse.edt.runtime.java.eglx.lang;

import org.eclipse.edt.javart.BoxedValue;

import eglx.lang.AnyException;

public interface SequenceType extends BoxedValue {

	int getLength();
	
	<T extends SequenceType> T substring(int start, int end) throws AnyException;
}
