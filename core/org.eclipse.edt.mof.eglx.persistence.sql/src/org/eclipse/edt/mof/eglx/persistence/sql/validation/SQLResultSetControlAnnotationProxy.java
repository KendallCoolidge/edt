/*******************************************************************************
 * Copyright © 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.mof.eglx.persistence.sql.validation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.edt.compiler.binding.AbstractValidationProxy;
import org.eclipse.edt.compiler.binding.AnnotationValidationRule;
import org.eclipse.edt.compiler.binding.UserDefinedAnnotationValidationRule;
import org.eclipse.edt.mof.utils.NameUtile;

public class SQLResultSetControlAnnotationProxy extends AbstractValidationProxy {
	public static final String name = NameUtile.getAsName("SQLResultSetControl");
	
	private static SQLResultSetControlAnnotationProxy INSTANCE = new SQLResultSetControlAnnotationProxy();
		
	private static final List<AnnotationValidationRule> myAnnotations = new ArrayList();
   	static {
   		myAnnotations.add(new UserDefinedAnnotationValidationRule(SQLResultSetControlValidator.class));
   	}
   	
	private SQLResultSetControlAnnotationProxy() {
	}
	
	public static SQLResultSetControlAnnotationProxy getInstance() {
		return INSTANCE;
	}
	
	@Override
	public List<AnnotationValidationRule> getAnnotationValidators() {
		return myAnnotations;
	}
}