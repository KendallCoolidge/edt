/*******************************************************************************
 * Copyright © 2008, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.core.internal.partinfo;

import org.eclipse.core.resources.IFile;

public class EGLSourcelessProjectOrigin implements IPartOrigin {
	
	public IFile getEGLFile() {
		return null; 
	}

	public boolean isOriginEGLFile() {
		return false;
	}

	public boolean isSourceCodeAvailable() {
		return false;
	}
}
