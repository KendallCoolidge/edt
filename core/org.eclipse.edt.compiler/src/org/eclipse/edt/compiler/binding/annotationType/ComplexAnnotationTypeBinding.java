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
package org.eclipse.edt.compiler.binding.annotationType;

import java.util.Collections;
import java.util.Set;

import org.eclipse.edt.compiler.binding.AnnotationTypeBinding;



/**
 * @author Harmon
 */
public abstract class ComplexAnnotationTypeBinding extends AnnotationTypeBinding{
    
	public ComplexAnnotationTypeBinding(String caseSensitiveInternedName, Object[] fields) {
        super(caseSensitiveInternedName, fields);
    }
    
    public ComplexAnnotationTypeBinding(String caseSensitiveInternedName, Object[] fields, Object[] defaultValues) {
    	super(caseSensitiveInternedName, fields, defaultValues);
    }

    public boolean isComplex() {
        return true;
    }
    
	public Set getFields(){
    	return Collections.EMPTY_SET;
    }
    
	public boolean hasSingleValue() {
		return false;
	}
}