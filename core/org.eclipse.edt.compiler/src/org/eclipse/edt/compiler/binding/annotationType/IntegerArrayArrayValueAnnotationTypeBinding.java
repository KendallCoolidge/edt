/*******************************************************************************
 * Copyright � 2008, 2010 IBM Corporation and others.
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

import java.util.Iterator;

import org.eclipse.edt.compiler.binding.AnnotationTypeBinding;
import org.eclipse.edt.compiler.binding.ArrayTypeBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.binding.PrimitiveTypeBinding;
import org.eclipse.edt.compiler.core.ast.ArrayLiteral;
import org.eclipse.edt.compiler.core.ast.DefaultASTVisitor;
import org.eclipse.edt.compiler.core.ast.Expression;
import org.eclipse.edt.compiler.core.ast.Primitive;


/**
 * @author Harmon
 */
abstract class IntegerArrayArrayValueAnnotationTypeBinding extends AnnotationTypeBinding {

    public IntegerArrayArrayValueAnnotationTypeBinding(String caseSensitiveInternedName) {
        super(caseSensitiveInternedName, ArrayTypeBinding.getInstance(ArrayTypeBinding.getInstance(PrimitiveTypeBinding.getInstance(Primitive.INT))));
    }
    
    public ITypeBinding getSingleValueType() {
    	return ArrayTypeBinding.getInstance(ArrayTypeBinding.getInstance(PrimitiveTypeBinding.getInstance(Primitive.INT)));
    }
    
	private static class IsTwoDimensionalArrayOfIntegerLiteralsChecker extends DefaultASTVisitor {
		boolean isTwoDimensionalArrayOfIntegerLiterals = false;
		
		boolean isTwoDimensionalArrayOfIntegerLiterals(Expression expr) {
			expr.accept(this);
			return isTwoDimensionalArrayOfIntegerLiterals;
		}
		
		public boolean visit(ArrayLiteral arrayLiteral) {
			for(Iterator iter = arrayLiteral.getExpressions().iterator(); iter.hasNext();) {
				if(!new IsIntegerArrayLiteralChecker().isIntegerArrayLiteral((Expression) iter.next())){
					return false;
				}
			}
			isTwoDimensionalArrayOfIntegerLiterals = true;
			return false;
		}
	}
}
