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

import org.eclipse.edt.compiler.binding.IAnnotationBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.ICompilerOptions;
import org.eclipse.edt.mof.egl.utils.InternUtil;


/**
 * @author Dave Murray
 */
public class CurrencySymbolValidator implements IAnnotationValidationRule {

	public void validate(Node errorNode, Node target, ITypeBinding targetTypeBinding, Map allAnnotations, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions){
		IAnnotationBinding annotationBinding = (IAnnotationBinding)allAnnotations.get(InternUtil.intern(IEGLConstants.PROPERTY_CURRENCYSYMBOL));
		if(annotationBinding != null && annotationBinding.getValue() != null) {
			int lengthWithoutEscapeChars = lengthWithoutEscapeChars((String) annotationBinding.getValue());
			if(lengthWithoutEscapeChars == 0) {
				problemRequestor.acceptProblem(
					errorNode,
					IProblemRequestor.INVALID_FORM_FIELD_CURRENCY_SYMBOL_PROPERTY_VALUE,
					new String[] {IEGLConstants.PROPERTY_CURRENCYSYMBOL});
			}
			else if(lengthWithoutEscapeChars > 3) {
				problemRequestor.acceptProblem(
					errorNode,
					IProblemRequestor.PROPERTY_EXCEEDS_ALLOWED_LENGTH,
					new String[] {(String) annotationBinding.getValue(), IEGLConstants.PROPERTY_CURRENCYSYMBOL, Integer.toString(3)});
			}
		}
	}
	
	/**
	 * Calculates the length of a string with excape characters removed.	 * 
	 */
	public static int lengthWithoutEscapeChars(String input) {
		String tempString = input;
		int len = input.length();
		if (tempString == "\\")
			return 1;
		// now account for escape characters if there are any
		int slashIndex = tempString.indexOf('\\');
		while (slashIndex != -1) {
			if (slashIndex == input.length()-1) {// slash is next to last character
				slashIndex = -1;
				len = len -1;
			}
			else {
				tempString = tempString.substring(slashIndex+2);
				slashIndex = tempString.indexOf('\\');
				len = len -1;
			}
		}
		return len;	
	}
}
