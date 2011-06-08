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
package org.eclipse.edt.mof.egl.compiler;

import java.util.Map;

import org.eclipse.edt.compiler.ISystemPackageBuildPathEntry;
import org.eclipse.edt.compiler.ISystemPartBindingLoadedRequestor;
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.internal.core.lookup.IEnvironment;
import org.eclipse.edt.compiler.internal.io.ZipFileBuildPathEntry;
import org.eclipse.edt.mof.EObject;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.mof2binding.Mof2Binding;
import org.eclipse.edt.mof.egl.utils.InternUtil;
import org.eclipse.edt.mof.serialization.IZipFileEntryManager;


public class SystemPackageMOFPathEntry extends SystemPackageBuildPathEntry implements ISystemPackageBuildPathEntry, IZipFileEntryManager{

	private String fileExtension;

	public SystemPackageMOFPathEntry(IEnvironment env, String path, ISystemPartBindingLoadedRequestor req, String fileExtension, Mof2Binding converter) {
		super(env, path, req, fileExtension, converter);
	}

	
	protected String convertToStoreKey(String entry) {
		//entries are in the form: "pkg1/pkg2/partName.mofxml". Need to convert this to:
		//"pkg1.pkg2.partName"
		
		//strip off the filename extension
		String value = entry.substring(0, entry.indexOf("."));
		
		return value.replaceAll("/", ".");
		
	}


}
