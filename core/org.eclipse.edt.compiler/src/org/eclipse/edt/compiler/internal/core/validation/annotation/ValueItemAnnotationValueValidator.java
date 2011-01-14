/*******************************************************************************
 * Copyright � 2005, 2010 IBM Corporation and others.
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
import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.binding.StructureItemBinding;
import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.core.ast.Name;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.ICompilerOptions;


/**
 * @author demurray
 */
public class ValueItemAnnotationValueValidator implements IValueValidationRule {

	public void validate(Node errorNode, Node target, IAnnotationBinding annotationBinding, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {
		if (annotationBinding.getValue() != null) {
			if (annotationBinding.getValue() instanceof Name){
				IDataBinding value = ((Name) annotationBinding.getValue()).resolveDataBinding();
				
				if(IDataBinding.STRUCTURE_ITEM_BINDING == value.getKind()) {
					StructureItemBinding sItemBinding = (StructureItemBinding) value;
					if(sItemBinding.getParentItem() != null || !sItemBinding.getChildren().isEmpty()) {
						problemRequestor.acceptProblem(
							errorNode,
							IProblemRequestor.SELECTION_ITEM_MUST_BE_TOP_LEVEL_AND_LEAF,
							new String[] {
								value.getCaseSensitiveName(),
								IEGLConstants.PROPERTY_VALUEITEM
							}
						);
					}
				}
				
				ITypeBinding tBinding = value.getType();
				if(tBinding != null && IBinding.NOT_FOUND_BINDING != tBinding) {
					if(ITypeBinding.ARRAY_TYPE_BINDING == tBinding.getKind()) {
						problemRequestor.acceptProblem(
							errorNode,
							IProblemRequestor.VALUEITEM_OR_LABELITEM_CANNOT_BE_ARRAY,
							new String[] {
								value.getCaseSensitiveName()
							});
					}
				}
			}	
		}
	}
}
