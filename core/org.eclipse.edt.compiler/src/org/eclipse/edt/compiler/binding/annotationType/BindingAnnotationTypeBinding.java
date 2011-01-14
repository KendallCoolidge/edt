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
package org.eclipse.edt.compiler.binding.annotationType;

import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;


class BindingAnnotationTypeBinding extends StringValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("binding");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static BindingAnnotationTypeBinding INSTANCE = new BindingAnnotationTypeBinding();
	
	private BindingAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static BindingAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return takesConsoleFieldAnnotations(binding);
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
}
