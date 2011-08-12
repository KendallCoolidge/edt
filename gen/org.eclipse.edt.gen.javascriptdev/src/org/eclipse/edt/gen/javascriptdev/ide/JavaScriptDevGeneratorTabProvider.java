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

import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.edt.ide.ui.preferences.AbstractGeneratorTabProvider;
import org.eclipse.edt.ide.ui.preferences.GenerationSettingsComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This is a sample generator tab provider class.
 */
public class JavaScriptDevGeneratorTabProvider extends AbstractGeneratorTabProvider {

	private GenerationSettingsComposite genSettings;
	private IEclipsePreferences projectPreferenceStore;

	/**
	 * Define the tab contents within the parent composite.
	 * @param parent
	 * @return
	 */
	@Override
	public Control getTabContent(Composite parent) {
		if (getComposite() == null) {
			setComposite(new Composite(parent, SWT.NULL));

			GridLayout layout = new GridLayout();
			layout.numColumns = 1;
			getComposite().setLayout(layout);
			getComposite().setLayoutData(new GridData(GridData.FILL_BOTH));
			getComposite().setFont(parent.getFont());

			if (getResource() != null) {
				this.projectPreferenceStore = new ProjectScope(getResource().getProject()).getNode(Activator.PLUGIN_ID);
			}
			genSettings = new GenerationSettingsComposite(getComposite(), SWT.NULL, getResource(), Activator.getDefault().getPreferenceStore(),
				this.projectPreferenceStore, Activator.PROPERTY_EXAMPLEGEN_DIR, Activator.PREFERENCE_DEFAULT_EXAMPLEGEN_DIRECTORY, getStatusChangeListener());
			genSettings.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		return this.getComposite();
	}

	@Override
	public void performApply() {
		performOk();
	}

	@Override
	public boolean performOk() {
		return genSettings.performOK();
	}

	@Override
	public void performDefaults() {
		genSettings.performDefaults();
	}

	@Override
	public IEclipsePreferences getProjectPreferenceStore() {
		return this.projectPreferenceStore;
	}

	@Override
	public void removePreferencesForAResource() {
		if (genSettings != null) {
			genSettings.removePreferencesForAResource();
		}
	}

	@Override
	public void removePreferencesForAllResources() {
		if (genSettings != null) {
			genSettings.removePreferencesForAllResources();
		}
	}
}
