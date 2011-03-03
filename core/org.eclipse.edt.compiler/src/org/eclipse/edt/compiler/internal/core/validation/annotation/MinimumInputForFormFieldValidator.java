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

import java.util.Map;

import org.eclipse.edt.compiler.binding.FormFieldBinding;
import org.eclipse.edt.compiler.binding.IAnnotationBinding;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.ICompilerOptions;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;


/**
 * @author demurray
 */
public class MinimumInputForFormFieldValidator extends DefaultFieldContentAnnotationValidationRule {

	public void validate(Node errorNode, Node container, IDataBinding containerBinding, String canonicalContainerName, Map allAnnotations, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {
		IAnnotationBinding annotationBinding = (IAnnotationBinding) allAnnotations.get(InternUtil.intern(IEGLConstants.PROPERTY_MINIMUMINPUT));
		if(annotationBinding != null && annotationBinding.getValue() != null) {
			
			if(containerBinding != null && IDataBinding.FORM_FIELD == containerBinding.getKind()) {
				FormFieldBinding fieldBinding = (FormFieldBinding) containerBinding;
				if(!fieldBinding.isConstant()) {
					IAnnotationBinding fieldLenABinding = (IAnnotationBinding) allAnnotations.get(InternUtil.intern(IEGLConstants.PROPERTY_FIELDLEN));				
					if(fieldLenABinding != null && fieldLenABinding.getValue() != null) {
						try {
							int definedLength = Integer.parseInt(annotationBinding.getValue().toString());
							int maxLengthAllowed = Integer.parseInt(fieldLenABinding.getValue().toString());
							if(definedLength > maxLengthAllowed) {
								problemRequestor.acceptProblem(errorNode, 
									IProblemRequestor.PROPERTY_MINIMUM_INPUT_MUST_BE_LESS_THAN_PRIMITIVE_LENGTH,
									new String[]{
										String.valueOf(definedLength),
										String.valueOf(maxLengthAllowed),
										fieldBinding.getType().getCaseSensitiveName()});
							}
						}
						catch(NumberFormatException e) {							
						}
					}
				}
			}			
		}
	}
}
