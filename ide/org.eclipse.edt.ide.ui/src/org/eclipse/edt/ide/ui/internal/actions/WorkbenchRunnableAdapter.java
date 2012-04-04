/*******************************************************************************
 * Copyright © 2000, 2012 IBM Corporation and others.
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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.edt.ide.core.model.EGLCore;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * An <code>IRunnableWithProgress</code> that adapts and  <code>IWorkspaceRunnable</code>
 * so that is can be executed inside <code>IRunnableContext</code>. <code>OperationCanceledException</code> 
 * thrown by the apapted runnabled are cought and rethrown as a <code>InterruptedException</code>.
 */
public class WorkbenchRunnableAdapter implements IRunnableWithProgress {
	
	private IWorkspaceRunnable fWorkspaceRunnable;
	private ISchedulingRule fRule;	
	
	public WorkbenchRunnableAdapter(IWorkspaceRunnable runnable) {
		this(runnable, ResourcesPlugin.getWorkspace().getRoot());		
	}

	/**
	 * Runs a workspace runnable with the given lock or <code>null</code> to run with no lock at all.
	 */
	public WorkbenchRunnableAdapter(IWorkspaceRunnable runnable, ISchedulingRule rule) {
		fWorkspaceRunnable= runnable;
		fRule= rule;
	}
	
	public ISchedulingRule getSchedulingRule() {
		return fRule;
	}
	
	/*
	 * @see IRunnableWithProgress#run(IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			EGLCore.run(fWorkspaceRunnable, fRule, monitor);
		} catch (OperationCanceledException e) {
			throw new InterruptedException(e.getMessage());
		} catch (CoreException e) {
			throw new InvocationTargetException(e);
		}
	}

}

