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
package org.eclipse.edt.compiler.internal.core.validation;

import org.eclipse.edt.compiler.core.ast.Handler;
import org.eclipse.edt.compiler.core.ast.Interface;
import org.eclipse.edt.compiler.core.ast.Library;
import org.eclipse.edt.compiler.core.ast.Program;
import org.eclipse.edt.compiler.core.ast.Service;
import org.eclipse.edt.compiler.core.ast.TopLevelFunction;
import org.eclipse.edt.compiler.internal.core.validation.part.FunctionValidator;
import org.eclipse.edt.compiler.internal.core.validation.part.HandlerValidator;
import org.eclipse.edt.compiler.internal.core.validation.part.LibraryValidator;
import org.eclipse.edt.compiler.internal.core.validation.part.ProgramValidator;
import org.eclipse.edt.compiler.internal.core.validation.part.ServiceValidator;

public class DefaultPartValidator extends AbstractPartValidator {
	@Override
	public boolean visit(Program program) {
		program.accept(new ProgramValidator(problemRequestor, irBinding, compilerOptions));
		return false;
	}
	
	public boolean visit(Handler handler) {
		handler.accept(new HandlerValidator(problemRequestor, irBinding, compilerOptions));
		return false;
	};
	
	public boolean visit(Library library) {
		library.accept(new LibraryValidator(problemRequestor, irBinding, compilerOptions));
		return false;
	}
	
	public boolean visit(Service service) {
		service.accept(new ServiceValidator(problemRequestor, irBinding, compilerOptions));
		return false;
	}
	
	public boolean visit(Interface iface) {
//		iface.accept(new InterfaceValidator(problemRequestor, irBinding, compilerOptions));
		return false;
	}
	
	public boolean visit(TopLevelFunction topLevelFunction) {
		topLevelFunction.accept(new FunctionValidator(problemRequestor, compilerOptions));
		return false;
	}
}