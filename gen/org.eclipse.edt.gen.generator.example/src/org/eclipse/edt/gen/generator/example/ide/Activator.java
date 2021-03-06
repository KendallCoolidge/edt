/*******************************************************************************
 * Copyright © 2011, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.gen.generator.example.ide;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String PROPERTY_EXAMPLEGEN_DIR = "exampleGenDirectory"; //$NON-NLS-1$
	public static final String PROPERTY_EXAMPLEGEN_ARGUMENTS = "exampleGenArguments"; //$NON-NLS-1$
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.edt.gen.generator.example"; //$NON-NLS-1$

	public static final String PREFERENCE_DEFAULT_EXAMPLEGEN_DIRECTORY = PLUGIN_ID + ".defaultExampleGenDirectory"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	protected void initializeDefaultPreferences(IPreferenceStore store) {
		store.setDefault(PREFERENCE_DEFAULT_EXAMPLEGEN_DIRECTORY, "generatedOutput"); //$NON-NLS-1$
	}
}
