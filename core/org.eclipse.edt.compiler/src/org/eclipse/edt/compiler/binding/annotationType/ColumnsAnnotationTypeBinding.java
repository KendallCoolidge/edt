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


class ColumnsAnnotationTypeBinding extends IntegerValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("columns");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static ColumnsAnnotationTypeBinding INSTANCE = new ColumnsAnnotationTypeBinding();
	
	private ColumnsAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static ColumnsAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return (takesVariableFormFieldAnnotations(binding) ||
		       takesConsoleArrayFieldAnnotations(binding)) &&
			   !takesDataItemBinding(binding);
	}
	
	public Object getDefaultValue() {
		return new Integer(1);
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
}
