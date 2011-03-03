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
	package org.eclipse.edt.compiler.internal.core.validation.statement;
	
	import java.util.Iterator;

import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.core.ast.ClassDataDeclaration;
import org.eclipse.edt.compiler.core.ast.DefaultASTVisitor;
import org.eclipse.edt.compiler.core.ast.Expression;
import org.eclipse.edt.compiler.core.ast.Name;
import org.eclipse.edt.compiler.core.ast.Part;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.ICompilerOptions;
import org.eclipse.edt.compiler.internal.core.validation.annotation.AnnotationValidator;
import org.eclipse.edt.compiler.internal.core.validation.name.EGLNameValidator;

	
	/**
	 * @author Craig Duval
	 */
	public class ClassDataDeclarationValidator extends DefaultASTVisitor {
		
		private IProblemRequestor problemRequestor;
        private ICompilerOptions compilerOptions;
		
		public ClassDataDeclarationValidator(IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {
			this.problemRequestor = problemRequestor;
			this.compilerOptions = compilerOptions;
		}
		
		public boolean visit(final ClassDataDeclaration classDataDeclaration) {
			for(Iterator iter = classDataDeclaration.getNames().iterator(); iter.hasNext();) {
				EGLNameValidator.validate((Name) iter.next(), EGLNameValidator.PART, problemRequestor, compilerOptions);
			}
			
			StatementValidator.validateDataDeclarationInitializer(classDataDeclaration,problemRequestor, compilerOptions);
			if (classDataDeclaration.isConstant()){
				StatementValidator.validatePrimitiveConstant(classDataDeclaration.getType(),problemRequestor);
			}
			
			StatementValidator.validateDataDeclarationType(classDataDeclaration.getType(),((Part)classDataDeclaration.getParent()).getName().getCanonicalName(),((Expression)classDataDeclaration.getNames().get(0)).getCanonicalString(),  problemRequestor);
			
			new AnnotationValidator(problemRequestor, compilerOptions).validateAnnotationTarget(classDataDeclaration);
			
			IBinding binding = ((Name) classDataDeclaration.getNames().get(0)).resolveBinding();
			if(binding != null && IBinding.NOT_FOUND_BINDING != binding && binding.isDataBinding()) {
				StatementValidator.validateDeclarationForStereotypeContext((IDataBinding) binding, problemRequestor, classDataDeclaration.getType().getBaseType());
			}
			
			classDataDeclaration.accept(new FieldValidator(problemRequestor, compilerOptions));
			
			
			return false;
		}

		
	}
	
	


