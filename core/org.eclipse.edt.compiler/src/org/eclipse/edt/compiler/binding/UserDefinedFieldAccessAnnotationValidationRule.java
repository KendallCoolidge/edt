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
package org.eclipse.edt.compiler.binding;

import org.eclipse.edt.compiler.core.ast.Expression;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.ICompilerOptions;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;
import org.eclipse.edt.compiler.internal.core.validation.annotation.IFieldAccessAnnotationValidationRule;


/**
 * @author demurray
 */
public class UserDefinedFieldAccessAnnotationValidationRule extends FieldAccessValidationAnnotationTypeBinding {

	private Class validatorClass;

	public UserDefinedFieldAccessAnnotationValidationRule(Class validatorClass) {
		super(InternUtil.internCaseSensitive("UserDefinedFieldAccesstAnnotationValidationRule"));
		
		this.validatorClass = validatorClass;
	}
	
	public boolean validateLValue(Expression lValue, IDataBinding fieldBinding, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {
		try {
			return ((IFieldAccessAnnotationValidationRule)validatorClass.newInstance()).validateLValue(lValue, fieldBinding, problemRequestor, compilerOptions);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public boolean validateRValue(Expression rValue, IDataBinding fieldBinding, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {
		try {
			return ((IFieldAccessAnnotationValidationRule)validatorClass.newInstance()).validateRValue(rValue, fieldBinding, problemRequestor, compilerOptions);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}		
	}
}
