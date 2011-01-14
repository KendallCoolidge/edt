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
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;


public class EGLValueCanBeExpressionForOpenUIAnnotationTypeBinding extends BooleanValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("eglValueCanBeExpressionForOpenUI");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static EGLValueCanBeExpressionForOpenUIAnnotationTypeBinding INSTANCE = new EGLValueCanBeExpressionForOpenUIAnnotationTypeBinding();
	
	private EGLValueCanBeExpressionForOpenUIAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static EGLValueCanBeExpressionForOpenUIAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return binding.isTypeBinding() && ITypeBinding.FLEXIBLE_RECORD_BINDING == ((ITypeBinding) binding).getKind(); 
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
}
