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
package org.eclipse.edt.mof.eglx.services;

import org.eclipse.edt.compiler.ICompilerExtension;
import org.eclipse.edt.compiler.PartValidator;
import org.eclipse.edt.compiler.StatementValidator;
import org.eclipse.edt.compiler.SystemEnvironmentUtil;
import org.eclipse.edt.compiler.TypeValidator;
import org.eclipse.edt.compiler.core.ast.CallStatement;
import org.eclipse.edt.compiler.core.ast.Expression;
import org.eclipse.edt.compiler.core.ast.FieldAccess;
import org.eclipse.edt.compiler.core.ast.NestedFunction;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.core.ast.Part;
import org.eclipse.edt.compiler.core.ast.Statement;
import org.eclipse.edt.compiler.core.ast.ThisExpression;
import org.eclipse.edt.compiler.internal.egl2mof.ElementGenerator;
import org.eclipse.edt.mof.egl.Member;
import org.eclipse.edt.mof.egl.Service;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.eglx.services.gen.ServiceElementGenerator;
import org.eclipse.edt.mof.eglx.services.gen.ServicesCallStatement;
import org.eclipse.edt.mof.eglx.services.validation.ServicesCallStatementValidator;

public class ServicesExtension implements ICompilerExtension {
	
	@Override
	public String[] getSystemEnvironmentPaths() {
		return new String[]{SystemEnvironmentUtil.getSystemLibraryPath(ServicesCallStatement.class, "egllib")};
	}
	
	@Override
	public Class<?>[] getExtendedTypes() {
		return new Class[]{CallStatement.class, NestedFunction.class};
	}
	
	@Override
	public ElementGenerator getElementGeneratorFor(Node node) {
		if (shouldExtend(node)) {
			return new ServiceElementGenerator();
		}
		return null;
	}
	
	@Override
	public PartValidator getValidatorFor(Part part) {
		// No special validators.
		return null;
	}
	
	@Override
	public StatementValidator getValidatorFor(Statement stmt) {
		// Call statement can be extended.
		if (shouldExtend(stmt)) {
			return new ServicesCallStatementValidator();
		}
		return null;
	}
	
	@Override
	public TypeValidator getValidatorFor(Type type) {
		// No special validators.
		return null;
	}
	
	private boolean shouldExtend(Node node) {
		if(node instanceof CallStatement){
			if(((CallStatement)node).getUsing() != null){
				return Utils.isIHTTP(((CallStatement)node).getUsing().resolveType());
			}
			else{
				Expression exp = ((CallStatement)node).getInvocationTarget();
				Member binding = exp.resolveMember();
				return binding != null &&
						(binding.getAnnotation("eglx.rest.Rest") != null ||
						binding.getAnnotation("eglx.rest.EGLService") != null ||
						(!(exp instanceof FieldAccess && ((FieldAccess)exp).getPrimary() instanceof ThisExpression) &&
								binding.getContainer() instanceof Service));
			}
		}
		return false;
	}
}