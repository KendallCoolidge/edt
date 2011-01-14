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


class DisplayNameAnnotationTypeBinding extends StringValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("displayName");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static DisplayNameAnnotationTypeBinding INSTANCE = new DisplayNameAnnotationTypeBinding();
	
	private DisplayNameAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static DisplayNameAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return takesPageItemAnnotations(binding) || takesUIItemAnnotations(binding);
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
}
