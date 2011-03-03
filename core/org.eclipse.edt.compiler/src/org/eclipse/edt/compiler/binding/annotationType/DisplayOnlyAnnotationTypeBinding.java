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


class DisplayOnlyAnnotationTypeBinding extends BooleanValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("displayOnly");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static DisplayOnlyAnnotationTypeBinding INSTANCE = new DisplayOnlyAnnotationTypeBinding();
	
	private DisplayOnlyAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static DisplayOnlyAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return binding.isOpenUIStatementBinding();
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
}
