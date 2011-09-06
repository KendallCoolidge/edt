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
package org.eclipse.edt.ide.rui.preferences;

import org.eclipse.edt.ide.rui.internal.Activator;

public interface IRUIPreferenceConstants {

	public static final String PLUGIN_ID = Activator.PLUGIN_ID;
	public static final String PREFIX = PLUGIN_ID+".";	//$NON-NLS-1$

	//	RUI Preference Constants
	public static final String RUI_DEFAULT_LOCALES = PREFIX + "eglRUIDefaultLocales"; //$NON-NLS-1$
	
	// Language
	//---------
	public static final String 	PREFERENCE_RUNTIME_MESSAGES_LOCALE					= PREFIX + "RuntimeLocale"; //$NON-NLS-1$
	public static final String 	PREFERENCE_HANDLER_LOCALE							= PREFIX + "HandlerLocale"; //$NON-NLS-1$
	
	// Test server
	public static final String PREFERENCE_TESTSERVER_OBSOLETE_METHODS = PREFIX + "ObsoleteMethods"; //$NON-NLS-1$
	public static final String PREFERENCE_TESTSERVER_HCR_FAILED = PREFIX + "HCRFailed"; //$NON-NLS-1$
	public static final String PREFERENCE_TESTSERVER_HCR_UNSUPPORTED = PREFIX + "HCRUnsupported"; //$NON-NLS-1$
	// the following are the values for the above preferences, but not preferences themselves
	public static final int TESTSERVER_PROMPT = 0;
	public static final int TESTSERVER_TERMINATE = 1;
	public static final int TESTSERVER_IGNORE = 2;
}
