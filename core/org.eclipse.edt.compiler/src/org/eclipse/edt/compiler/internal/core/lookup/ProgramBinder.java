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
package org.eclipse.edt.compiler.internal.core.lookup;

import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.ProgramBinding;
import org.eclipse.edt.compiler.binding.ProgramBindingCompletor;
import org.eclipse.edt.compiler.core.ast.Program;
import org.eclipse.edt.compiler.core.ast.ProgramParameter;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.dependency.IDependencyRequestor;


/**
 * @author winghong
 */

public class ProgramBinder extends FunctionContainerBinder {

    private ProgramBinding programBinding;
    private Scope scope;

    public ProgramBinder(ProgramBinding programBinding, Scope scope, IDependencyRequestor dependencyRequestor, IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {
        super(programBinding, scope, dependencyRequestor, problemRequestor, compilerOptions);
        this.programBinding = programBinding;
        this.scope = scope;
    }

    public boolean visit(Program program) {
        // First we have to complete the program binding (as a side effect some of the AST nodes are bound)
        program.accept(new ProgramBindingCompletor(scope, programBinding, dependencyRequestor, problemRequestor, compilerOptions));

        // The current scope only changes once the initial program binding is complete
        currentScope = new ProgramScope(currentScope, programBinding);
        
        preprocessPart(program);

        // We will bind the rest of the program now
        return true;
    }
    
    public void endVisit(Program program) {
		doneVisitingPart();
	}
    
    public boolean visit(ProgramParameter programParameter) {
        if (programParameter.getName().resolveBinding() == IBinding.NOT_FOUND_BINDING) {
            return false;
        }
        processResolvableProperties(programParameter.getName());
        return false;
    }

}
