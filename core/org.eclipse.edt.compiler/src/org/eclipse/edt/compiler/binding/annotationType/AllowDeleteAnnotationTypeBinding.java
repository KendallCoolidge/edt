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
import org.eclipse.edt.compiler.core.Boolean;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;


class AllowDeleteAnnotationTypeBinding extends BooleanValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("allowDelete");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static AllowDeleteAnnotationTypeBinding INSTANCE = new AllowDeleteAnnotationTypeBinding();
	
	private AllowDeleteAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static AllowDeleteAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return binding.isOpenUIStatementBinding();
	}
	
	public Object getDefaultValue() {
		return Boolean.YES;
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
}
