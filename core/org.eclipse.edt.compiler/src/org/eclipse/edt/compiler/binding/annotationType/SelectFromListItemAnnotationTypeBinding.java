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
import java.util.List;

import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;
import org.eclipse.edt.compiler.internal.core.validation.annotation.SelectFromListItemAnnotationValidator;


class SelectFromListItemAnnotationTypeBinding extends ResolvableNameAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("selectFromListItem");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static SelectFromListItemAnnotationTypeBinding INSTANCE = new SelectFromListItemAnnotationTypeBinding();
	
	private static final List annotations = new ArrayList();
	static{
		annotations.add(new SelectFromListItemAnnotationValidator());
	}
	
	private SelectFromListItemAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static SelectFromListItemAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return takesPageItemAnnotations(binding);
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
	
	public List getAnnotations(){
		return annotations;
	}
}
