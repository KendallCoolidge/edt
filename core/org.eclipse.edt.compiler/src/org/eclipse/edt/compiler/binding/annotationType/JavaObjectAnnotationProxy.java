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
package org.eclipse.edt.compiler.binding.annotationType;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.edt.compiler.binding.AbstractValidationProxy;
import org.eclipse.edt.compiler.binding.FieldContentValidationRule;
import org.eclipse.edt.compiler.binding.UserDefinedFieldContentAnnotationValidationRule;
import org.eclipse.edt.compiler.internal.core.validation.annotation.JavaObjectFieldTypeValidator;
import org.eclipse.edt.mof.utils.NameUtile;


/**
 * @author Harmon
 */
public class JavaObjectAnnotationProxy extends AbstractValidationProxy {
    public static final String name = NameUtile.getAsName("JavaObject");

    private static JavaObjectAnnotationProxy INSTANCE = new JavaObjectAnnotationProxy();
    
    private static final List<FieldContentValidationRule> subPartTypeAnnotations = new ArrayList();
    static {
    	subPartTypeAnnotations.add(new UserDefinedFieldContentAnnotationValidationRule(JavaObjectFieldTypeValidator.class));
    }
    
    private JavaObjectAnnotationProxy() {
    }
    
    public static JavaObjectAnnotationProxy getInstance() {
        return INSTANCE;
    }
    
    @Override
    public List<FieldContentValidationRule> getPartSubTypeValidators() {
    	return subPartTypeAnnotations;
    }
}
