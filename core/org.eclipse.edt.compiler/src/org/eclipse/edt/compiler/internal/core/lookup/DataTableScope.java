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

import org.eclipse.edt.compiler.binding.DataTableBinding;
import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.binding.IFunctionBinding;
import org.eclipse.edt.compiler.binding.IPackageBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;

/**
 * @author winghong
 */
public class DataTableScope extends Scope {
    
    private DataTableBinding tableBinding;
    
    public DataTableScope(Scope parentScope, DataTableBinding tableBinding) {
        super(parentScope);
        this.tableBinding = tableBinding;
    }

    public IDataBinding findData(String simpleName) {
        IDataBinding result = tableBinding.findData(simpleName);
        if(result != IBinding.NOT_FOUND_BINDING) return result;
        
        return parentScope.findData(simpleName);
    }

    public IFunctionBinding findFunction(String simpleName) {
        return parentScope.findFunction(simpleName);
    }

    public IPackageBinding findPackage(String simpleName) {
        return parentScope.findPackage(simpleName);
    }

    public ITypeBinding findType(String simpleName) {
        return parentScope.findType(simpleName);
    }

}
