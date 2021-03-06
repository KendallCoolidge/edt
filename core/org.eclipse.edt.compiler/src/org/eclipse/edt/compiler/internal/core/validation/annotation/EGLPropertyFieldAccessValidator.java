/*******************************************************************************
 * Copyright © 2012, 2013 IBM Corporation and others.
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

import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.Member;

public class EGLPropertyFieldAccessValidator extends PropertyFieldAccessValidator {
	
	@Override
	protected Annotation getAnnotation(Member binding) {
		return binding.getAnnotation("eglx.lang.EGLProperty");
	}
	
	@Override
	protected boolean hasValue(Object obj) {
		return (obj != null);
	}

}
