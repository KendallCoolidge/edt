/*******************************************************************************
 * Copyright © 2011, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.mof.eglx.jtopen.validation.annotation;

import org.eclipse.edt.compiler.internal.core.validation.annotation.IAnnotationValidationRule;
import org.eclipse.edt.mof.egl.FixedPrecisionType;
import org.eclipse.edt.mof.egl.Type;

public class StructUnsignedBin8Validator extends AbstractStructParameterAnnotationValidator implements IAnnotationValidationRule {
	
	@Override
	protected String getName() {
		return "StructUnsignedBin8";
	}
	
	@Override
	protected Type getSupportedType() {
		return null;
	}

	@Override
	protected boolean isValidType(Type typeBinding) {
		if (typeBinding != null) {						
			return typeBinding instanceof FixedPrecisionType &&
					((FixedPrecisionType)typeBinding).getLength() == 20 &&
					((FixedPrecisionType)typeBinding).getDecimals() == 0;
		}
		else {
			return true;  //return true to avoid excess error messages
		}
	}


}
