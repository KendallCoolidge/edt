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
package org.eclipse.edt.compiler.internal;

/**
 * @author yunw
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IEGLBaseConstants {

	public static final String EGL_VALIDATION_RESOURCE_BUNDLE_NAME = "org.eclipse.edt.compiler.internal.core.builder.EGLValidationResources"; //$NON-NLS-1$
	public static final String EGL_BASE_RESOURCE_BUNDLE_NAME = "org.eclipse.edt.compiler.internal.EGLBaseResources"; //$NON-NLS-1$
	// The following constant does not really make sense here as this project does not require the egl.core project.
	// however, the constant must be defined here to avoid a circular dependency in the required projects
	public static final String EGL_NATURE_ID = "com.ibm.etools.egl.core.EGLNature"; //$NON-NLS-1$

}
