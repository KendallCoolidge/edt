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
package org.eclipse.edt.compiler.internal.core.lookup;

import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.binding.IFunctionBinding;
import org.eclipse.edt.compiler.binding.IPackageBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;

/**
 * @author Dave Murray
 */
public class DefaultScope extends Scope {
	
	public DefaultScope() {
		super(null);
	}

	public ITypeBinding findType(String simpleName) {
		return IBinding.NOT_FOUND_BINDING;
	}

	public IFunctionBinding findFunction(String simpleName) {
		return IBinding.NOT_FOUND_BINDING;
	}


	public IDataBinding findData(String simpleName) {
		return IBinding.NOT_FOUND_BINDING;
	}


	public IPackageBinding findPackage(String simpleName) {
		return IBinding.NOT_FOUND_BINDING;
	}
}
