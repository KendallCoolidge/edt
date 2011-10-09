/*******************************************************************************
 * Copyright © 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.internal.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.edt.ide.ui.internal.EGLLogger;
import org.eclipse.edt.ide.ui.internal.PluginImages;
import org.eclipse.edt.ide.ui.wizards.EGLFileConfiguration;
import org.eclipse.edt.ide.ui.wizards.EGLPackageConfiguration;
import org.eclipse.edt.ide.ui.wizards.LibraryConfiguration;
import org.eclipse.edt.ide.ui.wizards.LibraryOperation;
import org.eclipse.edt.ide.ui.wizards.PartTemplateException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class LibraryWizard extends EGLPartWizard implements INewWizard {

	private static final String WIZPAGENAME_LibraryWizardPage = "WIZPAGENAME_LibraryWizardPage"; //$NON-NLS-1$
	LibraryConfiguration configuration;
	
	public LibraryWizard() {
		super();
		setDefaultPageImageDescriptor(PluginImages.DESC_WIZBAN_NEWLIBRARY);
	}	

	protected IRunnableWithProgress getOperation()
	{
		ISchedulingRule rule= getCurrentSchedulingRule();
		LibraryOperation operation = null;
		if (rule != null){
			operation = new LibraryOperation((LibraryConfiguration)getConfiguration(), rule);
		}else{
			operation = new LibraryOperation((LibraryConfiguration)getConfiguration());
		}
		
	    return operation;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	public boolean performFinish() {
		if (!super.performFinish())
			return false;
		
		IRunnableWithProgress operation = getOperation();
	
		try{
			getContainer().run(canRunForked(), true, operation);
		}
		catch (InterruptedException e) {
			boolean dialogResult = false;
			if(e.getMessage().indexOf(':')!=-1){
				PartTemplateException pe = new PartTemplateException(e.getMessage());
				if(pe.getTemplateExcpetion().compareTo(EGLFileConfiguration.TEMPLATE_NOT_FOUND)==0){
					dialogResult = ((EGLPartWizardPage)this.getPage(WIZPAGENAME_LibraryWizardPage)).handleTemplateError(pe.getPartType(), pe.getPartDescription()); //$NON-NLS-1$
				}
				else if(pe.getTemplateExcpetion().compareTo(EGLFileConfiguration.TEMPLATE_DISABLED)==0){
					//is there a way to tell this?
				}
				else if(pe.getTemplateExcpetion().compareTo(EGLFileConfiguration.TEMPLATE_CORRUPTED)==0){
					dialogResult = ((EGLPartWizardPage)this.getPage(WIZPAGENAME_LibraryWizardPage)).handleTemplateError(pe.getPartType(), pe.getPartDescription()); //$NON-NLS-1$
				}
				
				if(dialogResult)
					return performFinish();
				else
					return false;
			}
			else{
				EGLLogger.log(this, e);
				return false;
			}
		}
		catch (InvocationTargetException e) {
			if(e.getTargetException() instanceof CoreException) {
				ErrorDialog.openError(
					getContainer().getShell(),
					null,
					null,
					((CoreException) e.getTargetException()).getStatus());
			}
			else {
				EGLLogger.log(this, e);
			}
			return false;
		}
		
		//update the dialog settings
		((LibraryWizardPage)getPage(WIZPAGENAME_LibraryWizardPage)).finishPage();
		
		//open the file
		openResource(configuration.getFile());		
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		getConfiguration().init(workbench, selection);
		setWindowTitle(NewWizardMessages.NewEGLLibraryWizardPageTitle);
	}

	public EGLPackageConfiguration getConfiguration() {
		if (configuration == null)
			configuration = new LibraryConfiguration();
		return configuration;
	}

	public void addPages() {
		addPage(new LibraryWizardPage(WIZPAGENAME_LibraryWizardPage));
	}

}
