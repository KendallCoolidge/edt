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
package org.eclipse.edt.compiler.binding;

import java.util.Map;

import org.eclipse.edt.compiler.core.ast.FunctionParameter;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.core.ast.Type;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.ICompilerOptions;


/**
 * @author svihovec
 *
 */
public abstract class FieldContentValidationAnnotationTypeBinding extends ContentValidationAnnotationTypeBinding {

	public FieldContentValidationAnnotationTypeBinding(String caseSensitiveInternedName) {
		super(caseSensitiveInternedName);
	}
	
	public abstract void validate(Node errorNode, Node target, IDataBinding containerBinding, String canonicalContainerName, Map allAnnotations, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions);
	
	public void validateFunctionParameter(FunctionParameter fParameter, IDataBinding parameterBinding, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {		
	}
	
	public void validateFunctionReturnType(Type typeNode, ITypeBinding typeBinding, IPartBinding declaringPart, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {		
	}
}