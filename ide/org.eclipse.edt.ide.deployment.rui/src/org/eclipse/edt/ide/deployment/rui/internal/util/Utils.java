/*******************************************************************************
 * Copyright © 2009, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.deployment.rui.internal.util;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.edt.compiler.internal.interfaces.IGenerationMessageRequestor;
import org.eclipse.edt.compiler.internal.util.EGLMessage;
import org.eclipse.edt.ide.core.generation.IGenerationUnit;
import org.eclipse.edt.ide.core.internal.generation.GeneratePartsOperation;
import org.eclipse.edt.ide.deployment.results.DeploymentResultMessageRequestor;
import org.eclipse.edt.ide.deployment.results.IDeploymentResultsCollector;
import org.eclipse.edt.ide.deployment.rui.Activator;
import org.eclipse.edt.ide.deployment.rui.internal.nls.Messages;
import org.eclipse.edt.ide.deployment.utilities.DeploymentUtilities;
import org.eclipse.edt.ide.ui.internal.util.CoreUtility;
import org.eclipse.edt.mof.egl.Part;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class Utils 
{
	public static final String SKIP_BIND_FILE_GENERATION = "SKIP_BIND_FILE_GENERATION";
//	private static ElementFactory elementFactory;
	
	//TODO - EDT
//	public static void addAnnotation( Part part, String key, Object value)
//	{
//		Annotation annotation = part.getAnnotation(key);
//		if( annotation == null )
//		{
//			annotation = getFactory().createAnnotation(key, false, true);
//		}
//		annotation.setValue(value);
//		part.addAnnotation(annotation);
//	}
//
//	static ElementFactory getFactory()
//	{
//		if( elementFactory == null )
//		{
//			elementFactory = new ElementFactoryImpl();
//		}
//		return elementFactory;
//		
//	}
	public static void buildStackTraceMessages(IDeploymentResultsCollector resultsCollector, Throwable t)
	{
		GeneratePartsOperation.buildStackTraceMessages(t, new DeploymentResultMessageRequestor(resultsCollector));
	}
	public static void addMessage( IDeploymentResultsCollector results, boolean error, String msgId, String[] inserts )
	{
		if( results != null )
		{
			EGLMessage message;
			if( error )
			{
				message = EGLMessage.createEGLDeploymentErrorMessage( msgId, null, inserts );
			}
			else
			{
				message = EGLMessage.createEGLDeploymentInformationalMessage( msgId, null, inserts );
			}
			results.addMessage(DeploymentUtilities.convert(message));
		}
	}

	public static void addMessage( IGenerationMessageRequestor results, boolean error, String msgId, String[] inserts )
	{
		if( results != null )
		{
			EGLMessage message;
			if( error )
			{
				message = EGLMessage.createEGLDeploymentErrorMessage( msgId, null, inserts );
			}
			else
			{
				message = EGLMessage.createEGLDeploymentInformationalMessage( msgId, null, inserts );
			}
			results.addMessage(message);
		}
	}

	public static IStatus createEmptyStatus()
	{
		return new Status(IStatus.OK, Activator.PLUGIN_ID, ""){
			public int getSeverity() {
				return -1;
			}
		};
	}
	
	public static String getBuildDescriptorID(IGenerationUnit genunit)
	{
		String id = "Invalid BD (NULL)";
		if(genunit.getBuildDescriptor() != null )
		{
			if( genunit.getBuildDescriptor().getPartName() != null &&
					genunit.getBuildDescriptor().getPartName().length() > 0 )
			{
				id = genunit.getBuildDescriptor().getPartName() + " - ";
			}
			else
			{
				id = "Invalid BD name " + (genunit.getBuildDescriptor().getPartName() == null ? "(NULL)" : "") + " - ";
			}
			if( genunit.getBuildDescriptor().getPartPath() != null &&
					genunit.getBuildDescriptor().getPartPath().length() > 0 )
			{
				id += genunit.getBuildDescriptor().getPartPath();
			}
			else
			{
				id += "Invalid BD file " + (genunit.getBuildDescriptor().getPartPath() == null ? "(NULL)" : "");
			}
		}
		return id;
	}

	public static String getPartID(Part part)
	{
		String id = "Invalid part (NULL)";
		if( part != null )
		{
			if( part.getFileName() != null &&
					part.getFileName().length() > 0 )
			{
				id = part.getFileName();
			}
			else
			{
				id = "Invalid part name " + (part.getFileName() == null ? "(NULL)" : "");
			}
		}
		return id;
	}
	public static String getPartID(IGenerationUnit genunit)
	{
		String id = "Invalid part (NULL)";
		if( genunit.getPart() != null )
		{
			if( genunit.getPart().getPartName() != null &&
					genunit.getPart().getPartName().length() > 0 )
			{
				id = genunit.getPart().getPartName();
			}
			else
			{
				id = "Invalid part name " + (genunit.getPart().getPartName() == null ? "(NULL)" : "");
			}
			if( genunit.getPart().getPartPath() == null ||
					genunit.getPart().getPartPath().length() == 0)
			{
				id += " Invalid part path" + (genunit.getPart().getPartPath() == null ? "(NULL)" : "");
			}
		}
		return id;
	}
	public static IFolder createDirectory(String targetDirectory) throws CoreException {
		try {
			IFolder folder= ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(targetDirectory));
			CoreUtility.createFolder( folder, true, true, new NullProgressMonitor() );
			return folder;
		}
		catch (CoreException ce) {
			throw new CoreException(DeploymentUtilities.createDeployMessage(IStatus.ERROR,
					NLS.bind( Messages.J2EEDeploymentSolution_31, new Object[]{targetDirectory.replaceAll( "\\\\", "/" ), ce.getMessage()} )));
		}
	}
	public static IFolder getContextDirectory(IProject project) {
		IVirtualComponent component = ComponentCore.createComponent(project);
		return (IFolder)component.getRootFolder().getUnderlyingResource();
	}
	
	//TODO - EDT
//	public static String generateFullTargetPathForWebContent(IResource deploySource, String targetDirectory) 
//		throws CoreException{
//		String result = ""; //$NON-NLS-1$
//		IProject project = deploySource.getProject();
//		List results = new ArrayList();
//		DeploymentUtilities.findFolder(project, results, IConstants.WEB_CONTENT_FOLDER_NAME);
//		if (results.size() > 0) {
//			String rootPath = ((IResource)results.get(0)).getFullPath().toString();
//			String sourcePath = deploySource.getFullPath().toString();
//			if (sourcePath.startsWith(rootPath)) {
//				result =  targetDirectory + sourcePath.substring(rootPath.length());
//			}
//		}
//		return result;
//	}

}
