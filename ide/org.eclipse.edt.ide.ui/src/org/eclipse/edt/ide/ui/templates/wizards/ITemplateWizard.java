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
package org.eclipse.edt.ide.ui.templates.wizards;

import org.eclipse.edt.ide.ui.templates.ITemplate;
import org.eclipse.jface.wizard.IWizard;

public interface ITemplateWizard extends IWizard {
	/**
	 * Returns the parent wizard this template wizard is sitting in.
	 * 
	 * @return
	 */
	public IWizard getParentWizard();
	
	/**
	 * Sets the parent wizard.
	 * 
	 * @param parent
	 */
	public void setParentWizard(IWizard parent);
	
	/**
	 * Sets the template associated with this wizard.
	 * 
	 * @param template
	 */
	public void setTemplate(ITemplate template);
	
	/**
	 * Returns the template associated with this wizard.
	 * 
	 * @return
	 */
	public ITemplate getTemplate();
}
