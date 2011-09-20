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
package org.eclipse.edt.ide.ui.preferences;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.edt.ide.core.EDTCoreIDEPlugin;
import org.eclipse.edt.ide.core.Logger;
import org.eclipse.edt.ide.core.internal.model.EGLProject;
import org.eclipse.edt.ide.core.utils.EclipseUtilities;
import org.eclipse.edt.ide.core.utils.ProjectSettingsUtility;
import org.eclipse.edt.ide.ui.internal.UINlsStrings;
import org.eclipse.edt.ide.ui.internal.dialogs.StatusInfo;
import org.eclipse.edt.ide.ui.internal.wizards.FolderSelectionDialog;
import org.eclipse.edt.ide.ui.internal.wizards.IStatusChangeListener;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class GenerationSettingsComposite extends Composite {
	
	protected final IResource resource;
	protected final IPreferenceStore prefStore;
	protected final IEclipsePreferences projectPrefs;
	protected final String dirPropertyID;
	protected final String argPropertyID;
	protected final String preferenceID;
	protected final IStatusChangeListener statusListener;
	
	protected Button browseInside;
	protected Text genInsideDirectory;
	protected Text genArguments;
	protected String originalGenDir;
	
	
	private StatusInfo latestStatus = new StatusInfo();
	
	public GenerationSettingsComposite(Composite parent, int style, IResource resource, IPreferenceStore prefStore,
			IEclipsePreferences projectPrefs, String dirPropertyID, String argPropertyID, String preferenceID, IStatusChangeListener statusListener) {
		super(parent, style);
		
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
		
		this.resource = resource;
		this.prefStore = prefStore;
		this.projectPrefs = projectPrefs;
		this.dirPropertyID = dirPropertyID;
		this.argPropertyID = argPropertyID;
		this.preferenceID = preferenceID;
		this.statusListener = statusListener;
		
		createContents();
		init();
	}
	
	private void createContents() {
		Group group = new Group(this, SWT.NONE);
		group.setText(UINlsStrings.genSettingsGroupLabel);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		group.setLayout(layout);
		group.setFont(getFont());

		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalIndent = 0;
		group.setLayoutData(data);
		
		if (resource == null) {
			Composite c = new Composite(group, SWT.NULL);
			layout = new GridLayout();
			layout.numColumns = 2;
			c.setLayout(layout);
			c.setFont(group.getFont());
			data = new GridData(GridData.FILL_BOTH);
			data.horizontalIndent = 0;
			c.setLayoutData(data);
			
			new Label(c, SWT.LEFT).setText(UINlsStrings.genDefaultGenDir);
			
			genInsideDirectory = new Text(c, SWT.BORDER);
			data = new GridData(GridData.FILL_HORIZONTAL);
			genInsideDirectory.setLayoutData(data);
			
			if (statusListener != null) {
				genInsideDirectory.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						String text = genInsideDirectory.getText().trim();
						if (text.length() == 0) {
							latestStatus.setError(UINlsStrings.genSettingsValidationDefaultBlank);
							statusListener.statusChanged(latestStatus);
						}
						else if (!new Path(text).isValidPath(text)) {
							latestStatus.setError(UINlsStrings.genSettingsValidationDefaultInvalid);
							statusListener.statusChanged(latestStatus);
						}
						else if (text.charAt(0) == '/') {
							latestStatus.setError(UINlsStrings.genSettingsValidationDefaultRetlative);
							statusListener.statusChanged(latestStatus);
						}
						else if (latestStatus != null && !latestStatus.isOK()){
							latestStatus.setOK();
							statusListener.statusChanged(latestStatus);
						}
					}
				});
			}
		}
		else {
			
			Composite cIn = new Composite(group, SWT.NULL);
			layout = new GridLayout();
			layout.numColumns = 2;
			cIn.setLayout(layout);
			cIn.setFont(group.getFont());
			data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalIndent = 0;
			cIn.setLayoutData(data);
			
			genInsideDirectory = new Text(cIn, SWT.BORDER);
			data = new GridData(GridData.FILL_HORIZONTAL);
			genInsideDirectory.setLayoutData(data);
			
			browseInside = new Button(cIn, SWT.PUSH);
			browseInside.setText("..."); //$NON-NLS-1$
			
			browseInside.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					FolderSelectionDialog dialog = new FolderSelectionDialog(browseInside.getShell(), new WorkbenchLabelProvider(), new WorkbenchContentProvider());
					dialog.setAllowMultiple(false);
					
					String currentValue = genInsideDirectory.getText();
					if (currentValue.length() > 0) {
						IResource initialSelection;
						if (currentValue.charAt(0) == '/') {
							initialSelection = ResourcesPlugin.getWorkspace().getRoot().findMember(currentValue);
						}
						else {
							initialSelection = resource.getProject().findMember(currentValue);
						}
						if (initialSelection != null) {
							dialog.setInitialSelection(initialSelection);
						}
					}
					
					dialog.setTitle(UINlsStrings.genDirSelectionTitle);
					dialog.setMessage(UINlsStrings.genDirSelectionMsg);
					dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
					if (dialog.open() == Window.OK) {
						Object result = dialog.getResult()[0];
						if (result instanceof IContainer) {
							IContainer container = (IContainer)result;
							if (!(container instanceof IProject) && container.getProject().equals(resource.getProject())) {
								genInsideDirectory.setText(container.getProjectRelativePath().toString());
							}
							else {
								genInsideDirectory.setText(container.getFullPath().toString());
							}
						}
					}
				}
			});
			
			if (statusListener != null) {
				genInsideDirectory.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
							String text = genInsideDirectory.getText().trim();
							if (text.length() == 0) {
								latestStatus.setError(UINlsStrings.genSettingsValidationBlank);
								statusListener.statusChanged(latestStatus);
							}
							else {
								IPath path = new Path(text);
								if (!path.isValidPath(text)) {
									latestStatus.setError(UINlsStrings.genSettingsValidationInvalid);
									statusListener.statusChanged(latestStatus);
								}
								else if (text.charAt(0) == '/' && (path.segmentCount() < 1 || !ResourcesPlugin.getWorkspace().getRoot().getProject(path.segment(0)).exists())) {
									latestStatus.setError(UINlsStrings.genSettingsValidationProject);
									statusListener.statusChanged(latestStatus);
								}
								else if (latestStatus != null && !latestStatus.isOK()){
									latestStatus.setOK();
									statusListener.statusChanged(latestStatus);
								}
							}
						}
				});
			}
			
			Group argGroup = new Group(this, SWT.NONE);
			argGroup.setText(UINlsStrings.genArguments);

			GridLayout argLayout = new GridLayout();
			argLayout.numColumns = 1;
			argGroup.setLayout(argLayout);
			argGroup.setFont(getFont());

			GridData argData = new GridData(GridData.FILL_BOTH);
			argData.horizontalIndent = 0;
			argGroup.setLayoutData(argData);

			genArguments = new Text(argGroup, SWT.BORDER);
			data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalIndent = 5;
			genArguments.setLayoutData(data);
		}
	}
	
	private void init() {
		
		if (resource == null) {
			genInsideDirectory.setText(prefStore.getString(preferenceID));
		}
		else {
			String genDir = ProjectSettingsUtility.getGenerationDirectory(resource, prefStore, projectPrefs, dirPropertyID, preferenceID);
			genDir = EclipseUtilities.convertFromInternalPath(genDir);
			if (genDir.length() == 0) {
				genDir = resource.getProject().getFullPath().toString();
			}
			genInsideDirectory.setText(genDir);

			String genArgument = ProjectSettingsUtility.getGenerationArgument(resource, prefStore, projectPrefs, argPropertyID);
			genArguments.setText(genArgument);
		}
		this.originalGenDir = genInsideDirectory.getText();
	}
	
	public void performDefaults() {
		latestStatus.setOK();
		statusListener.statusChanged(latestStatus);
		init();
	}
	
	public boolean performOK() {
		if (resource == null) {
			prefStore.setValue(preferenceID, genInsideDirectory.getText());
			if(!genInsideDirectory.getText().equalsIgnoreCase(originalGenDir)){
				try {
					ProjectSettingsUtility.setBuildFlag(null);
				} catch (BackingStoreException e) {
					Logger.log("GenerationSettingsComposite.performOk", NLS.bind(UINlsStrings.genSettingsSaveError, resource.getFullPath().toString()), e); //$NON-NLS-1$
					return false;
				}
			}
		}
		else {
			try {
				String value;
				
				value = genInsideDirectory.getText();
				if (value.equals(resource.getProject().getFullPath().toString())) {
					// just use an indicator for 'this project'
					value = ""; //$NON-NLS-1$
				}

				//rebuild project if generation directory is changed
				if(!value.equalsIgnoreCase(originalGenDir)){
					ProjectSettingsUtility.setBuildFlag(resource);
				}

				value = EclipseUtilities.convertToInternalPath(value);
				ProjectSettingsUtility.setGenerationDirectory(resource, value, projectPrefs, dirPropertyID);
				
				String argValue = genArguments.getText().trim();
				ProjectSettingsUtility.setGenerationArgument(resource, argValue, projectPrefs, argPropertyID);

			}
			catch (BackingStoreException e) {
				Logger.log("GenerationSettingsComposite.performOk", NLS.bind(UINlsStrings.genSettingsSaveError, resource.getFullPath().toString()), e); //$NON-NLS-1$
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Remove any preferences that this tab previously stored in a 
	 * resource's preference store.
	 */
	public void removePreferencesForAResource() {
		if (projectPrefs != null) {
			try {
				ProjectSettingsUtility.setGenerationDirectory(resource, null, projectPrefs, dirPropertyID);
				ProjectSettingsUtility.setGenerationArgument(resource, null, projectPrefs, argPropertyID);
			}
			catch (BackingStoreException e) {
				Logger.log("GenerationSettingsComposite.removePreferencesForAResource", NLS.bind(UINlsStrings.CompilerPropertyPage_errorCleaningUpPrefStore, resource.getFullPath().toString()), e); //$NON-NLS-1$
			}
		}
	}
	
	/**
	 * Remove ALL preferences that this composite previously stored in a 
	 * resource's preference store.
	 */
	public void removePreferencesForAllResources() {
		if (projectPrefs != null) {
			try {
				Preferences propertyPrefs = projectPrefs.node( dirPropertyID );
				propertyPrefs.clear();
				propertyPrefs.flush();

				propertyPrefs = projectPrefs.node( argPropertyID );
				propertyPrefs.clear();
				propertyPrefs.flush();

			} catch( BackingStoreException e ) {
				Logger.log("GenerationSettingsComposite.removePreferencesForAllResources", NLS.bind(UINlsStrings.CompilerPropertyPage_errorCleaningUpPrefStore, resource.getFullPath().toString()), e); //$NON-NLS-1$
			}
		}
	}
	
	public void performRemoval() {
		if(statusListener!=null){
			latestStatus.setOK();
			statusListener.statusChanged(latestStatus);
		}
	}

	public void performAddition(){
		genInsideDirectory.setText(genInsideDirectory.getText());
	}
}
