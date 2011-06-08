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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.UserDefinedAnnotationValidationRule;
import org.eclipse.edt.compiler.internal.core.validation.annotation.GetRestValidator;
import org.eclipse.edt.mof.egl.utils.InternUtil;


public class GetRestAnnotationTypeBinding extends ComplexAnnotationTypeBinding {
	public static final String name = InternUtil.intern("GetRest");
	
	private static GetRestAnnotationTypeBinding INSTANCE = new GetRestAnnotationTypeBinding();
		
	private static final List myAnnotations = new ArrayList();
   	static{
   		myAnnotations.add(new UserDefinedAnnotationValidationRule(GetRestValidator.class));
   	}
   	
	private GetRestAnnotationTypeBinding() {
		super(name, new Object[0]);
	}
	
	public static GetRestAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return true;
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
	
	public List getAnnotations() {
		return myAnnotations;
	}

}
