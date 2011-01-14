/*******************************************************************************
 * Copyright � 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.compiler.internal.core.builder;


/**
 * @author cduval
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BuildException extends RuntimeException {

	/**
	 * 
	 */
	public BuildException() {
		super();
	}

	/**
	 * @param message
	 */
	public BuildException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BuildException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public BuildException(Throwable cause) {
		super(cause);
	}
	
	static public String getPartName (String[] packageName, String partName){
		StringBuffer buffer = new StringBuffer();
		if (packageName.length > 0){
			for (int i = 0; i < packageName.length; i++){
				buffer.append(packageName[i]);
				buffer.append('.');
			}
		}
		
		buffer.append(partName);
		
		return buffer.toString();
	}

}
