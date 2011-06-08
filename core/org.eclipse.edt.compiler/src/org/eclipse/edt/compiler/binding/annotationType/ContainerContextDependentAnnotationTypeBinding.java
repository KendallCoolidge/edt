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
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.mof.egl.utils.InternUtil;


class ContainerContextDependentAnnotationTypeBinding extends BooleanValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("containerContextDependent");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static ContainerContextDependentAnnotationTypeBinding INSTANCE = new ContainerContextDependentAnnotationTypeBinding();
	
	private ContainerContextDependentAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static ContainerContextDependentAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		if(binding.isTypeBinding()) {
			return ((ITypeBinding) binding).getKind() == ITypeBinding.FIXED_RECORD_BINDING;
		}
		if(binding.isFunctionBinding()) {
            return binding instanceof IPartBinding; 
        }
    	return false;
    }
	
	private Object readResolve() {
		return INSTANCE;
	}
}
