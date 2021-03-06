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
package org.eclipse.edt.ide.ui.project.templates;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardNode;

public interface IProjectTemplate {
	/**
	 * Returns the unique identifier for the template.
	 *  
	 * @return
	 */
	public String getId();
	
	/**
	 * Returns the category the template belongs to.
	 * 
	 * @return
	 */
	public String getCategory();
	
	/**
	 * Returns the short name of the template.
	 * 
	 * @return
	 */
	public String getName();
			
	/**
	 * Returns a long description of the template (to aid the user).
	 * 
	 * @return
	 */
	public String getDescription();
		
	/**
	 * Returns a small icon representing the template. 
	 * 
	 * @return
	 */
	public ImageDescriptor getIcon();
	
	/**
	 * Indicates whether the template is the default template in its category. Typically the default will be pre-selected in the wizard.
	 * 
	 * @return
	 */
	public boolean isDefault();
	
	/**
	 * Returns the project template instance defined in the extension point
	 * 
	 * @return
	 */
	public IProjectTemplateClass getProjectTemplateClass();

	/**
	 * Returns a wizard node representing the wizard (or null if no wizard is needed). 
	 * 
	 * From Eclipse: A wizard node acts a placeholder for a real wizard in a wizard selection page. It is done in such a way that the actual creation of a wizard can be deferred until the wizard is really needed.
	 * 
	 * @return
	 */

	public IWizardNode getWizardNode();
	
	/**
	 * Returns true if this template is configurable with a wizard.
	 * 
	 * @return
	 */
	public boolean hasWizard();
	
	/**
	 * Returns true if this template can be finished without configuration.
	 * 
	 * @return
	 */
	public boolean canFinish();
	
	/**
	 * Initializes the template using the supplied IConfigurationElement. 
	 * 
	 * (Templates are typically contributed via an extension point)
	 * 
	 * @param configElement
	 */
	public void init(IConfigurationElement configElement);
	

	public String getWidgetLibraryContainer();

	public void setWidgetLibraryContainer(String widgetLibraryContainer);

	
}
