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
package org.eclipse.edt.gen.javascriptdev.ide;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

public class VEJavaScriptDevGenerator extends JavaScriptDevGenerator {
	private IPath outputLocation;
	
	public VEJavaScriptDevGenerator() {
		super();
	}
	
	public void setOutputDirectory(IPath outputLocation){
		this.outputLocation = outputLocation;
	}

	@Override
	public String getOutputDirectory(IResource resource) {
		return outputLocation.toOSString();
	}

	@Override
	public boolean getEditMode() {
		return true;
	}

	@Override
	public String getId() {
		return "org.eclipse.edt.ide.gen.VEJavaScriptDevGenProvider";
	}
}