/*******************************************************************************
 * Copyright � 2005, 2010 IBM Corporation and others.
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
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.core.ast.Primitive;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;


/**
 * @author Dave Murray
 */
public class PropertyApplicableForSpecificPrimitiveOnlyAnnotationValidator extends PropertyApplicableForCertainPrimitiveTypeOnlyAnnotationValidator {
	
	Primitive requiredPrimitive;
	
	public PropertyApplicableForSpecificPrimitiveOnlyAnnotationValidator(IAnnotationTypeBinding annotationType, String canonicalAnnotationName, Primitive requiredPrimitive) {
		super(annotationType, canonicalAnnotationName);
		this.requiredPrimitive = requiredPrimitive;
	}

	protected void validatePrimitiveType(Node errorNode, IAnnotationBinding annotationBinding, IProblemRequestor problemRequestor, Primitive primitive, String canonicalItemName) {
		if(primitive != requiredPrimitive) {
			problemRequestor.acceptProblem(
				errorNode,
				IProblemRequestor.PROPERTY_CERTAIN_PRIMITIVE_REQUIRED,
				new String[] {
					canonicalAnnotationName,
					requiredPrimitive.getName(),
					primitive.getName()
				});
		}
	}
}
