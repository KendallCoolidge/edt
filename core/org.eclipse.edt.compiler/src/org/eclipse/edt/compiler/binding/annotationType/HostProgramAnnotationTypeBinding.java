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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.binding.UserDefinedAnnotationValidationRule;
import org.eclipse.edt.compiler.binding.UserDefinedValueValidationRule;
import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;
import org.eclipse.edt.compiler.internal.core.validation.annotation.EGLPropertyValidator;
import org.eclipse.edt.compiler.internal.core.validation.annotation.GetMethodAnnotationValueValidator;
import org.eclipse.edt.compiler.internal.core.validation.annotation.HostProgramValidator;
import org.eclipse.edt.compiler.internal.core.validation.annotation.SetMethodAnnotationValueValidator;


public class HostProgramAnnotationTypeBinding extends ComplexAnnotationTypeBinding {
	public static final String name = InternUtil.intern("HostProgram");
	
	private static HostProgramAnnotationTypeBinding INSTANCE = new HostProgramAnnotationTypeBinding();
	
	private static final List myAnnotations = new ArrayList();
   	static{
   		myAnnotations.add(new UserDefinedAnnotationValidationRule(HostProgramValidator.class));
   	}
   	
    private static final HashMap fieldAnnotations = new HashMap();
   	
	private HostProgramAnnotationTypeBinding() {
		super(name, new Object[0]);
	}
	
	public static HostProgramAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
	      return binding.isTypeBinding() && (((ITypeBinding) binding).getKind() == ITypeBinding.INTERFACE_BINDING);
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
		
	public List getAnnotations() {
		return myAnnotations;
	}

}
