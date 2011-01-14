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
import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.core.ast.Primitive;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;
import org.eclipse.edt.compiler.internal.core.validation.annotation.PropertyApplicableForSpecificPrimitiveOnlyAnnotationValidator;


class AsBytesAnnotationTypeBinding extends BooleanValueAnnotationTypeBinding {
	public static final String caseSensitiveName = InternUtil.internCaseSensitive("asBytes");
	public static final String name = InternUtil.intern(caseSensitiveName);
	
	private static AsBytesAnnotationTypeBinding INSTANCE = new AsBytesAnnotationTypeBinding();
	
	private static final List annotations = new ArrayList();
	static {
		annotations.add(new PropertyApplicableForSpecificPrimitiveOnlyAnnotationValidator(
			INSTANCE, IEGLConstants.PROPERTY_ASBYTES, Primitive.CHAR
		));
	}
	
	private AsBytesAnnotationTypeBinding() {
		super(caseSensitiveName);
	}
	
	public static AsBytesAnnotationTypeBinding getInstance() {
		return INSTANCE;
	}
	
	public boolean isApplicableFor(IBinding binding) {
		return takesSQLItemAnnotations(binding);
	}
	
	private Object readResolve() {
		return INSTANCE;
	}
	
	public List getAnnotations() {
		return annotations;
	}
}
