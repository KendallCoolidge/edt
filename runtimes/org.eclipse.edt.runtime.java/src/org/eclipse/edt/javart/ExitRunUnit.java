/*******************************************************************************
 * Copyright © 2006, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.javart;

/**
 * Exception to be thrown about when an EGL RunUnit terminates.
 */
public class ExitRunUnit extends EglExit
{
	/**
	 * The version ID used in serialization.
	 */
	private static final long serialVersionUID = 70L;

	/** Only one instance of this exception may be created - this is it */
	private static ExitRunUnit theinstance = null;
	
	/**
	 * Private constructor - not just anybody can create one of these.
	 */
	private ExitRunUnit()
	{
		//
	}
	
	/**
	 * Return the single instance of this exception. Create it if necessary.
	 * 
	 * @return The exception object
	 */
	public static ExitRunUnit getSingleton()
	{
		if (theinstance == null)
		{
			theinstance = new ExitRunUnit();
		}
		
		return theinstance;
	}
}
