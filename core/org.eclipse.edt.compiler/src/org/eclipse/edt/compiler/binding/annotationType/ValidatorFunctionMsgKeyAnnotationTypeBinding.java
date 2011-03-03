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
package org.eclipse.edt.compiler.binding.annotationType;

import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;


class ValidatorFunctionMsgKeyAnnotationTypeBinding extends StringValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("validatorFunctionMsgKey");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static ValidatorFunctionMsgKeyAnnotationTypeBinding INSTANCE = new ValidatorFunctionMsgKeyAnnotationTypeBinding();
	
	private ValidatorFunctionMsgKeyAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static ValidatorFunctionMsgKeyAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return takesUIItemAnnotations(binding) || takesValidationAnnotations(binding);
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
}
