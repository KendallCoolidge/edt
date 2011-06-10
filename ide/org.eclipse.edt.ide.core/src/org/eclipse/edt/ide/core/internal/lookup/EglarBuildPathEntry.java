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
package org.eclipse.edt.ide.core.internal.lookup;

import org.eclipse.core.runtime.IPath;
import org.eclipse.edt.compiler.ZipFileBindingBuildPathEntry;
import org.eclipse.edt.compiler.internal.core.lookup.IEnvironment;
import org.eclipse.edt.compiler.internal.mof2binding.Mof2Binding;
import org.eclipse.edt.ide.core.internal.utils.AbsolutePathUtility;

public class EglarBuildPathEntry extends ZipFileBindingBuildPathEntry {

	protected IEnvironment environment;
	
	public EglarBuildPathEntry(IEnvironment environment, IPath path, String fileExtension, Mof2Binding converter) {
		
		super(AbsolutePathUtility.getAbsolutePathString(path), fileExtension, converter);
		this.environment = environment;
	}
	
	protected IEnvironment getEnvironment() {
		return this.environment;
	}
}
