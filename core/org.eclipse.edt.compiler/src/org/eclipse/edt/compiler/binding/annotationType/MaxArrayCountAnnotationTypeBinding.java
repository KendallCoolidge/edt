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
import org.eclipse.edt.compiler.binding.PrimitiveTypeBinding;
import org.eclipse.edt.compiler.core.ast.Primitive;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;


class MaxArrayCountAnnotationTypeBinding extends IntegerValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("maxArrayCount");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static MaxArrayCountAnnotationTypeBinding INSTANCE = new MaxArrayCountAnnotationTypeBinding();
	
	private MaxArrayCountAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static MaxArrayCountAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return binding.isOpenUIStatementBinding();
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
	
	public boolean takesExpressionInOpenUIStatement() {
		return true;
	}
	
	public ITypeBinding requiredTypeForOpenUIStatement() {
		return PrimitiveTypeBinding.getInstance(Primitive.INT);
	}
}
