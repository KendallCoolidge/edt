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
package org.eclipse.edt.ide.ui.internal.actions;

import org.eclipse.edt.ide.ui.internal.wizards.ProgramWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;

public class NewProgramAction extends AbstractOpenWizardWorkbenchAction {

	public NewProgramAction() {
	}
	public NewProgramAction(IWorkbench workbench, String label, Class[] acceptedTypes) {
		super(workbench, label, acceptedTypes, false);
	}
	protected Wizard createWizard() {  
		return new ProgramWizard();
	}
	protected boolean shouldAcceptElement(Object obj) { 
		return true;
	}
}
