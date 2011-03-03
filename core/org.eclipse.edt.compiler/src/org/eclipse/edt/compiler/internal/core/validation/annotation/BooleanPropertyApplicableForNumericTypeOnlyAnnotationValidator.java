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
package org.eclipse.edt.compiler.internal.core.validation.annotation;

import org.eclipse.edt.compiler.binding.IAnnotationBinding;
import org.eclipse.edt.compiler.binding.IAnnotationTypeBinding;
import org.eclipse.edt.compiler.core.Boolean;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.core.ast.Primitive;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;


/**
 * @author svihovec
 */
public class BooleanPropertyApplicableForNumericTypeOnlyAnnotationValidator extends PropertyApplicableForNumericTypeOnlyAnnotationValidator {
	
	public BooleanPropertyApplicableForNumericTypeOnlyAnnotationValidator(IAnnotationTypeBinding annotationType, String canonicalAnnotationName) {
		super(annotationType, canonicalAnnotationName);
	}
	
	protected void validatePrimitiveType(final Node errorNode, final IAnnotationBinding annotationBinding, final IProblemRequestor problemRequestor, Primitive primitive, String canonicalItemName) {
		if(annotationBinding.getValue() == Boolean.YES){
			super.validatePrimitiveType(errorNode, annotationBinding, problemRequestor, primitive, canonicalItemName);
		}
	}
}
