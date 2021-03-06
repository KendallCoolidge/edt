/*******************************************************************************
 * Copyright © 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.compiler;

import org.eclipse.edt.ide.core.IDEBaseCompiler;
import org.eclipse.edt.ide.core.IDEBaseCompilerExtension;
import org.eclipse.edt.mof.eglx.jtopen.IBMiFactory;
import org.eclipse.edt.mof.eglx.jtopen.ext.IBMiExtension;

public class IDEIBMiExtension extends IDEBaseCompilerExtension {
	
	public IDEIBMiExtension() {
		this.baseExtension = new IBMiExtension();
	}
	
	@Override
	public String[] getSystemEnvironmentPaths() {
		return new String[]{IDEBaseCompiler.getPathToPluginDirectory(IBMiFactory.packageName, "egllib")};
	}
}
