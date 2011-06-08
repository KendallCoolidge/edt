/*******************************************************************************
 * Copyright © 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.core.internal.lookup.workingcopy;

import org.eclipse.core.resources.IProject;
import org.eclipse.edt.compiler.ISystemEnvironment;
import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.IPackageBinding;
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.binding.PackageBinding;
import org.eclipse.edt.compiler.binding.PartBinding;
import org.eclipse.edt.compiler.internal.core.lookup.IEnvironment;
import org.eclipse.edt.ide.core.internal.compiler.SystemEnvironmentManager;
import org.eclipse.edt.ide.core.internal.lookup.ProjectEnvironment;
import org.eclipse.edt.ide.core.internal.partinfo.IPartOrigin;
import org.eclipse.edt.mof.egl.utils.InternUtil;

// TODO Refactor common code between this environment and project environment - level01Compile, getPartBinding, hasPackage
public class WorkingCopyProjectEnvironment implements IEnvironment {

	private PackageBinding rootPackageBinding = new PackageBinding(ProjectEnvironment.defaultPackage, null, this);
    private IProject project;
	private WorkingCopyProjectBuildPathEntry declaringProjectBuildPathEntry;
	private IWorkingCopyBuildPathEntry[] buildPathEntries;
	
	
	public WorkingCopyProjectEnvironment(IProject project) {
		super();
		this.project = project;
	}

	
	public IProject getProject() {
		return project;
	}

	public IPartOrigin getPartOrigin(String[] packageName, String partName) {
		IPartOrigin retVal = declaringProjectBuildPathEntry.getPartOrigin(packageName, partName);
        return retVal;
	}

	public void setProjectBuildPathEntries(IWorkingCopyBuildPathEntry[] projectBuildPathEntries) {
		this.buildPathEntries = projectBuildPathEntries;		
	}
	
	protected void setDeclaringProjectBuildPathEntry(WorkingCopyProjectBuildPathEntry entry) {
        this.declaringProjectBuildPathEntry = entry;
    }
	
	public IPartBinding getPartBinding(String[] packageName, String partName) {
		IPartBinding result = null;
        for(int i = 0; i < buildPathEntries.length; i++) {
	        result = buildPathEntries[i].getPartBinding(packageName, partName);
	        if(result != null) return result;
	    }
        
       return getSystemEnvironment().getPartBinding(packageName, partName);
	}

	public IPartBinding getNewPartBinding(String[] packageName,	String caseSensitiveInternedPartName, int kind) {
		IPartBinding binding = declaringProjectBuildPathEntry.getNewPartBinding(packageName, caseSensitiveInternedPartName, kind);
		if (binding != null && binding != IBinding.NOT_FOUND_BINDING){
			binding.setEnvironment(this);
		}
		return binding;
	}

	public boolean hasPackage(String[] packageName) {
		for(int i = 0; i < buildPathEntries.length; i++) {
            if(buildPathEntries[i].hasPackage(packageName)) {
                return true;
            }
        }
        
        return getSystemEnvironment().hasPackage(packageName);
	}

	public IPackageBinding getRootPackage() {
        return rootPackageBinding;
    }

	public IPartBinding level01Compile(String[] packageName, String caseSensitiveInternedPartName) {
		String caseInsensitiveInternedPartName = InternUtil.intern(caseSensitiveInternedPartName);
	   
		for(int i = 0; i < buildPathEntries.length; i++) {
	        int partType = buildPathEntries[i].hasPart(packageName, caseInsensitiveInternedPartName);
			if(partType != ITypeBinding.NOT_FOUND_BINDING) {
				IPartBinding result = PartBinding.newPartBinding(partType, packageName, caseSensitiveInternedPartName);
	            result.setEnvironment(buildPathEntries[i].getRealizingEnvironment());
	            return result;
	        }
	    }

        return getSystemEnvironment().getPartBinding(packageName, caseInsensitiveInternedPartName);
	}	
	
	public void clear() {
		buildPathEntries = null;
		rootPackageBinding = new PackageBinding(ProjectEnvironment.defaultPackage, null, this);
	}


	public WorkingCopyProjectBuildPathEntry getDeclaringProjectBuildPathEntry() {
		return declaringProjectBuildPathEntry;
	}


	@Override
	public ISystemEnvironment getSystemEnvironment() {
		return SystemEnvironmentManager.findSystemEnvironment(getProject());
	}

}
