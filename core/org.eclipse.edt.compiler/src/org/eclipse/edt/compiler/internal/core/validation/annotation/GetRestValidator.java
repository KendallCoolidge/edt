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
package org.eclipse.edt.compiler.internal.core.validation.annotation;

import org.eclipse.edt.compiler.core.IEGLConstants;

public class GetRestValidator extends XXXrestValidator {
	protected String getName() {
		return IEGLConstants.PROPERTY_GETREST;
	}

	protected boolean supportsResourceParm() {
		return false;
	}

}
