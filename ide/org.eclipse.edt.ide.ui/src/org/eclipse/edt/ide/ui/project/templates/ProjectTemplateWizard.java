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
package org.eclipse.edt.ide.ui.project.templates;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;

public abstract class ProjectTemplateWizard extends Wizard implements IProjectTemplateWizard {
	protected IProjectTemplate template;
	protected IWizard parentWizard;
	
	public boolean performFinish() {
		return false;
	}

	public IWizard getParentWizard() {
		return parentWizard;
	}

	public void setParentWizard(IWizard parent) {
		this.parentWizard = parent;
	}

	public void setTemplate(IProjectTemplate template) {
		this.template = template;
		
		setWindowTitle(template.getName());
	}

	public IProjectTemplate getTemplate() {
		return template;
	}

	

}
