/*******************************************************************************
 * Copyright © 2011, 2012 IBM Corporation and others.
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
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.ICompilerOptions;
import org.eclipse.edt.compiler.internal.core.validation.name.EGLNameValidator;


/**
 * @author demurray
 */
public class AliasForVGUIRecordValueValidator implements IValueValidationRule{
	
	public void validate(Node errorNode, Node target, IAnnotationBinding annotationBinding, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {
		String value = annotationBinding.getValue().toString();
		if( value.startsWith("#") || value.startsWith("@") ) {
			value = value.substring(1);
		}
		EGLNameValidator.validate(value, EGLNameValidator.PROPERTY_ALIAS, problemRequestor, errorNode, compilerOptions);
	}
}