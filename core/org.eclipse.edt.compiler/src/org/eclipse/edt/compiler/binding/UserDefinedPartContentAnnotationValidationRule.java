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

import java.util.Map;

import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;
import org.eclipse.edt.compiler.internal.core.validation.annotation.IPartContentAnnotationValidationRule;


/**
 * @author svihovec
 *
 */
public class UserDefinedPartContentAnnotationValidationRule extends PartContentValidationAnnotationTypeBinding {

	private Class validatorClass;

	public UserDefinedPartContentAnnotationValidationRule(Class validatorClass) {
		super(InternUtil.internCaseSensitive("PartDefinedFieldContentAnnotationValidationRule"));
		
		this.validatorClass = validatorClass;
	}

	public void validate(Node errorNode, Node target, Map allAnnotations, IProblemRequestor problemRequestor) {
		try {
			((IPartContentAnnotationValidationRule)validatorClass.newInstance()).validate(errorNode, target, allAnnotations, problemRequestor);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
