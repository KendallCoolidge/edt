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
package org.eclipse.edt.compiler.internal.egl2mof.eglx.services;

import java.util.List;

import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.egl2mof.AbstractIOStatementGenerator;
import org.eclipse.edt.mof.egl.Expression;
import org.eclipse.edt.mof.eglx.services.ServicesCallStatement;
import org.eclipse.edt.mof.eglx.services.ServicesFactory;
import org.eclipse.edt.mof.serialization.IEnvironment;

public class ServicesActionStatementGenerator extends AbstractIOStatementGenerator {
	
	ServicesFactory factory = ServicesFactory.INSTANCE;
	final ServicesActionStatementGenerator generator = this;

	
	public ServicesActionStatementGenerator() {
		super(null);
	}
	
	public ServicesActionStatementGenerator(IEnvironment env) {
		super(env);
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.CallStatement callStatement) {
		ServicesCallStatement stmt = factory.createServicesCallStatement();
		callStatement.getInvocationTarget().accept(this);
		stmt.setInvocationTarget((Expression)stack.pop());
		if (callStatement.hasArguments()) {
			for (Node node : (List<Node>)callStatement.getArguments()) {
				node.accept(this);
				stmt.getArguments().add((Expression)stack.pop());
			}
		}
		if (callStatement.getCallbackTarget() != null) {
			callStatement.getCallbackTarget().accept(this);
			stmt.setCallback((Expression)stack.pop());
		}
		if (callStatement.getErrorCallbackTarget() != null) {
			callStatement.getErrorCallbackTarget().accept(this);
			stmt.setErrorCallback((Expression)stack.pop());
		}
		setElementInformation(callStatement, stmt);
		stack.push(stmt);
		if (callStatement.hasSettingsBlock())
			processSettings(stmt, callStatement.getSettingsBlock());
		return false;
	}
	
}
