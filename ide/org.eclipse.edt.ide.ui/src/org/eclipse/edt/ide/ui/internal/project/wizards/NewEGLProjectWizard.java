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
package org.eclipse.edt.ide.ui.internal.project.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.edt.ide.ui.EDTUIPlugin;
import org.eclipse.edt.ide.ui.EDTUIPreferenceConstants;
import org.eclipse.edt.ide.ui.internal.PluginImages;
import org.eclipse.edt.ide.ui.internal.project.wizard.pages.ProjectTemplateSelectionPage;
import org.eclipse.edt.ide.ui.internal.project.wizard.pages.ProjectWizardTypePage;
import org.eclipse.edt.ide.ui.internal.project.wizard.pages.SourceProjectWizardCapabilityPage;
import org.eclipse.edt.ide.ui.internal.wizards.NewWizardMessages;
import org.eclipse.edt.ide.ui.project.templates.IProjectTemplateClass;
import org.eclipse.edt.ide.ui.project.templates.ProjectTemplateWizardNode;
import org.eclipse.edt.ide.ui.wizards.ProjectConfiguration;
import org.eclipse.edt.ide.ui.wizards.ProjectFinishUtility;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

public class NewEGLProjectWizard extends Wizard 
	implements IWorkbenchWizard, INewWizard {
	
	private ProjectWizardTypePage typePage;		
	private ProjectTemplateSelectionPage templatePage;
	
	private ProjectConfiguration model;	
	private IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	private IConfigurationElement configElement;

	public NewEGLProjectWizard() {
		model = new ProjectConfiguration();
		model.setDefaultAttributes();
		IPreferenceStore store = EDTUIPlugin.getDefault().getPreferenceStore();
		model.setBasePackageName(store.getString(EDTUIPreferenceConstants.NEWPROJECTWIZARD_BASEPACKAGE));
		setDefaultPageImageDescriptor(PluginImages.DESC_WIZBAN_NEWEGLPROJECT);
		setDialogSettings(EDTUIPlugin.getDefault().getDialogSettings());
		setWindowTitle(NewWizardMessages.EGLNewProjectWizard_0);		
	}

	public boolean performFinish() {
		try{
			ISchedulingRule rule = getCurrentSchedulingRule();
			model.setProjectName(typePage.getModel().getProjectName());
			
			IWizardNode node = templatePage.getSelectedNode();
			ProjectTemplateWizardNode twn = (ProjectTemplateWizardNode) node;		
			List ops = ProjectFinishUtility.getCreateProjectFinishOperations((IProjectTemplateClass) twn.getTemplate().getProjectTemplateClass(), model, 0, rule);
			for(Iterator it = ops.iterator(); it.hasNext();)
			{
				Object obj = it.next();
				if(obj instanceof WorkspaceModifyOperation)
				{
					WorkspaceModifyOperation op = (WorkspaceModifyOperation)obj;
					getContainer().run(true, true, op);
				}
			}
			// Remember base package name
			IPreferenceStore store = EDTUIPlugin.getDefault().getPreferenceStore();
			store.putValue(EDTUIPreferenceConstants.NEWPROJECTWIZARD_BASEPACKAGE, model.getBasePackageName());
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			return false;
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
				e.printStackTrace();
				EDTUIPlugin.log(e);
			}
			return false;
		}
		finally{
			postPerformFinish();
		}
		return true;
	}

	protected ISchedulingRule getCurrentSchedulingRule() {
		ISchedulingRule rule= null;
		Job job= Job.getJobManager().currentJob();
		if (job != null)
			rule= job.getRule();

		if (rule == null)
			rule = getSchedulingRule();
		return rule;
	}
	
	/**
	 * Returns the scheduling rule for creating the element.
	 */
	protected ISchedulingRule getSchedulingRule() {
		return ResourcesPlugin.getWorkspace().getRoot(); // look all by default
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle(NewWizardMessages.EGLNewProjectWizard_0);
	}
	
	public void addPages() {
		this.typePage = new ProjectWizardTypePage(NewWizardMessages.EGLNewProjectWizard_1, model);
		addPage(typePage);
		this.templatePage = new ProjectTemplateSelectionPage(NewWizardMessages.ProjectTemplateSelectionPage);
		addPage(templatePage);
	}
	
	public boolean needsPreviousAndNextButtons() {
		return true;
	}
	
	public ProjectConfiguration getModel() {
		return model;
	}
	
	protected void postPerformFinish() {
		DummyConfigurationElement dummy = new DummyConfigurationElement();
		ProjectWizardUtils.updatePerspective(dummy);
		BasicNewResourceWizard.selectAndReveal(workspaceRoot.getProject(model.getProjectName()), PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	}
	
	/*
	 * Stores the configuration element for the wizard.  The config element will be used
	 * in <code>performFinish</code> to set the result perspective.
	 */
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
		configElement= cfig;
	}
	
	/**
	 * This class is here purely so that we have the ability to auto switch perspectives
	 */
	public class DummyConfigurationElement implements IConfigurationElement {
		
	    /**
	     * Extension attribute name for final perspective.
	     * This needs to be kept in sync with BasicNewProjectResourceWizard.FINAL_PERSPECTIVE
	     */
	    private static final String FINAL_PERSPECTIVE = "finalPerspective"; //$NON-NLS-1$

		public Object createExecutableExtension(String propertyName) throws CoreException {
			return null;
		}

		public String getAttribute(String name) throws InvalidRegistryObjectException {
			if (name.equals(FINAL_PERSPECTIVE)) {
				return "org.eclipse.edt.ide.ui.EGLPerspective"; //$NON-NLS-1$
			}
			return null;
		}
		
		public String getAttribute(String attrName, String locale) throws InvalidRegistryObjectException {
			return getAttribute( attrName );
		}

		public String getAttributeAsIs(String name) throws InvalidRegistryObjectException {
			return null;
		}

		public String[] getAttributeNames() throws InvalidRegistryObjectException {
			return null;
		}

		public IConfigurationElement[] getChildren() throws InvalidRegistryObjectException {
			return null;
		}

		public IConfigurationElement[] getChildren(String name) throws InvalidRegistryObjectException {
			return null;
		}

		public IContributor getContributor() throws InvalidRegistryObjectException {
			return null;
		}

		public IExtension getDeclaringExtension() throws InvalidRegistryObjectException {
			return null;
		}

		public String getName() throws InvalidRegistryObjectException {
			return null;
		}

		public String getNamespace() throws InvalidRegistryObjectException {
			return null;
		}

		public String getNamespaceIdentifier() throws InvalidRegistryObjectException {
			return null;
		}

		public Object getParent() throws InvalidRegistryObjectException {
			return null;
		}

		public String getValue() throws InvalidRegistryObjectException {
			return null;
		}
		
		public String getValue(String locale) throws InvalidRegistryObjectException {
			return null;
		}

		public String getValueAsIs() throws InvalidRegistryObjectException {
			return null;
		}

		public boolean isValid() {
			return false;
		}
		
	}
	
}
