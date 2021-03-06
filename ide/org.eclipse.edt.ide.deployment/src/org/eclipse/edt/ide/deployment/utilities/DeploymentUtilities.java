/*******************************************************************************
 * Copyright © 2008, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.deployment.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.edt.compiler.internal.IEGLBaseConstants;
import org.eclipse.edt.compiler.internal.util.EGLMessage;
import org.eclipse.edt.compiler.internal.util.IGenerationResultsMessage;
import org.eclipse.edt.ide.core.internal.generation.GenerationResultsMessage;
import org.eclipse.edt.ide.core.internal.model.BinaryPart;
import org.eclipse.edt.ide.core.internal.model.SourcePart;
import org.eclipse.edt.ide.core.internal.search.AllPartsCache;
import org.eclipse.edt.ide.core.internal.search.PartInfo;
import org.eclipse.edt.ide.core.model.EGLModelException;
import org.eclipse.edt.ide.core.model.IEGLElement;
import org.eclipse.edt.ide.core.model.IEGLProject;
import org.eclipse.edt.ide.core.model.IPart;
import org.eclipse.edt.ide.core.model.Signature;
import org.eclipse.edt.ide.core.search.IEGLSearchConstants;
import org.eclipse.edt.ide.core.search.IEGLSearchScope;
import org.eclipse.edt.ide.core.search.SearchEngine;
import org.eclipse.edt.ide.core.utils.EclipseUtilities;
import org.eclipse.edt.ide.deployment.Activator;
import org.eclipse.edt.ide.deployment.core.IDeploymentConstants;
import org.eclipse.edt.ide.deployment.core.model.DeploymentDesc;
import org.eclipse.edt.ide.deployment.core.model.DeploymentProject;
import org.eclipse.edt.ide.deployment.core.model.DeploymentTarget;
import org.eclipse.edt.ide.deployment.results.IDeploymentResultsCollector;
import org.eclipse.edt.ide.deployment.solution.DeploymentContext;
import org.eclipse.edt.mof.utils.NameUtile;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * A set of utility methods available to the deployment plugins
 *
 */
public class DeploymentUtilities {
		
	private static final String RUIHANDLER = NameUtile.getAsName( "RUIHandler" ); //$NON-NLS-1$
	public static String createExceptionMessage( Throwable t )
	{
		while( t.getCause() != null && !t.getCause().equals(t) )
		{
			t = t.getCause();
		}
		if( t.getMessage() != null && t.getMessage().length() > 0 )
		{
			return t.getClass().getName() + ":" + t.getMessage();
		}
		else if( t.toString() != null )
		{
			return t.toString();
		}
		else
		{
			return "";
		}

	}
	
	/**
	 * @param string
	 * @return
	 */
	public static IStatus createErrorStatus(String message) {
		return createErrorStatus(message, null);
	}
	
	/**
	 * @param string
	 * @return
	 */
	public static IStatus createErrorStatus(String message, Throwable exception) {
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1, message, exception);
	}
    
	/**
	 * Convenience method for creating a deployment message. These are the messages that will be displayed
	 * at the end of the deployment operation
	 * 
	 * @param severity The severity of the message <code>IStatus.ERROR IStatus.WARNING IStatus.INFO</code>
	 * @param message The message to display
	 * @return
	 */
	public static IStatus createDeployMessage(int severity, String message) {
		return new Status(severity, "Deployment", message); //$NON-NLS-1$
	}
	
	/**
	 * Generates the directory path that the HTML file needs to be deployed into
	 * 
	 * @param deploySource The HTML file generated by the model and currently in the source project
	 * @param targetDirectory The target directory to deploy into
	 * @param fileName The name of the HTML file as specified by the user. This name by default is
	 * the same as the handler name but does not have to be the same
	 * @param localeCode The language locale code
	 * 
	 * @return
	 * @throws CoreException
	 */
	public static String deriveHTMLFilePath(String targetDirectory, String fileName, String localeCode, boolean filenameWithLocale, String fileExtension) 
			throws CoreException{
		/**
         * deploy the HTML file directly into the context root and do not honor
         * its path within the source project.
         */
		String filename = buildFileNameWithLocale(fileName, localeCode, filenameWithLocale, fileExtension);
		
		return targetDirectory + "/" + filename; //$NON-NLS-1$
	}
	
	public static String buildFileNameWithLocale(String fileName, String localeCode, boolean filenameWithLocale, String fileExtension) {
		return fileName + (filenameWithLocale ?  "-" + localeCode : "" ) + "." + fileExtension; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Returns a directory path string. This is derived by finding the a directory type within the project of the passed 
	 * IFolder. The directory type should be either <code>IConstants.WEB_CONTENT_FOLDER_NAME</code> or
	 * <code>IConstants.EGL_SOURCE_FOLDER_NAME</code>. Removing this prefix path from the path of the passed IFolder
	 * and adding the resulting partial part to the end of the passed targetDirecory 
	 * 
	 * @param sourceFolder
	 * @param targetDirectory
	 * @param targetDirectoryType
	 * @return
	 * @throws CoreException
	 */
	public static String generateTargetDirectory(IFolder sourceFolder, String targetDirectory, String targetDirectoryType)
			throws CoreException {
		String result = ""; //$NON-NLS-1$
		String sourceDirectory = sourceFolder.getFullPath().toString();
		/**
		 * now build the target directory path
		 */
//		IProject project = sourceFolder.getProject();
		List results = new ArrayList();
//		DeploymentUtilities.findFolder(project, results, targetDirectoryType);
		if (results.size() > 0) {
			String rootPath = ((IResource)results.get(0)).getFullPath().toString();
			if (sourceDirectory.startsWith(rootPath)) {
				result =  targetDirectory + sourceDirectory.substring(rootPath.length());
			}
		}
		return result;
	}
	
	public static void copyFile(IFile file, String fullTargetPath)
	throws CoreException {
		copyFile(file.getRawLocation().toOSString(), fullTargetPath);
	}

	public static void copyFile(File file, String fullTargetPath)
	throws CoreException {
		copyFile(file.getAbsolutePath(), fullTargetPath);
	}

	private static void copyFile(String filePath, String fullTargetPath)
	throws CoreException {
		try{
			InputStream reader = new BufferedInputStream(new FileInputStream(filePath));
			copyFile(reader, fullTargetPath);
		} catch (IOException e) {
			e.printStackTrace();
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1,
					e.getMessage(), e);
			throw new CoreException(status);
		}
	}
	
	public static void copyFile(InputStream is, String fullTargetPath)
	throws CoreException {
		try {
			OutputStream writer = new BufferedOutputStream(
					new FileOutputStream(fullTargetPath));
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = is.read(buf)) != -1) {
				writer.write(buf, 0, i);
			}
			is.close();
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1,
					e.getMessage(), e);
			throw new CoreException(status);
		} catch (IOException e) {
			e.printStackTrace();
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1,
					e.getMessage(), e);
			throw new CoreException(status);
		}
	}
	
	public static String getDeploymentTargetId(DeploymentTarget target, String resultsCollectorsSubName, String resultsCollectorName)
	{
		if(target instanceof DeploymentProject){
			return ((DeploymentProject)target).getName();
		}
		
		return null;
	}
	
	public static String getDeploymentTargetType(DeploymentTarget target)
	{
		if( target != null )
		{
			switch(target.getTargetType())
			{
				case DeploymentTarget.TARGET_PROJECT:
					return IDeploymentConstants.TARGET_PROJECT;
			}
		}
		return "";
	}
	
	public static void finalize(final IDeploymentResultsCollector collector, final boolean displayErrorDialog, final String modelName)
	{
		try
		{
			if( collector.hasError() )
			{
				if(PlatformUI.isWorkbenchRunning()) {
/*	TODO - EDT	  		  	
 					Util.getDisplay().syncExec(new Runnable() {
		
						public void run() {
							IPreferenceStore preferenceStore = EGLUIPlugin.getDefault().getPreferenceStore();
							boolean errorDialog = !preferenceStore.getBoolean(IEGLPreferenceConstants.DEPLOY_DISPLAY_DIALOG_ON_ERROR);
							if( errorDialog && displayErrorDialog )
							{
								MessageDialogWithToggle dialog = MessageDialogWithToggle.openError(Util.getShell(), 
												Messages.deployment_error_title, 
												Messages.bind(Messages.deployment_error_description, new String[]{modelName}),
												Messages.deployment_error_skip,
												!errorDialog,
												null,
												null);
								preferenceStore.setValue(IEGLPreferenceConstants.DEPLOY_DISPLAY_DIALOG_ON_ERROR, dialog.getToggleState());
							}
						};
		  		  });
*/
				}
			}
		}
		finally
		{
			collector.done();
		}
	}
	
	public static String[] convertPackage( String pkg )
	{
		StringTokenizer toks = new StringTokenizer(pkg,".");
		List pkgAry = new ArrayList();
		while( toks.hasMoreElements() )
		{
			pkgAry.add(toks.nextToken());
		}
		return (String[])pkgAry.toArray(new String[pkgAry.size()]);
	}
	
	public static IStatus convert( IGenerationResultsMessage message )
	{
		int statusint = IStatus.INFO;
		switch( message.getSeverity())
		{
			case EGLMessage.EGL_ERROR_MESSAGE:
				statusint = IStatus.ERROR;
				break;
			case EGLMessage.EGL_INFORMATIONAL_MESSAGE:
				statusint = IStatus.INFO;
				break;
			case EGLMessage.EGL_WARNING_MESSAGE:
				statusint = IStatus.WARNING;
				break;
			default:
				statusint = IStatus.CANCEL;
				break;
		}
		return createStatus(statusint, message.getBuiltMessage());
	}
	public static IStatus createStatus( int severity, String message )
	{
		return new Status(severity, Activator.PLUGIN_ID, message);
	}
	
	public static final Shell getShell() {

		Shell shell = null;

		Display display = getDisplay();
		if (display != null)
			shell = display.getActiveShell();

		if (shell == null) {
			IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (workbenchWindow != null)
				shell = workbenchWindow.getShell();
		}

		return shell;
	}
	
	/**
	 * Try desperately to return a valid Display. Return null if we fail.
	 * 
	 * @return Display
	 */
	public static final Display getDisplay() {
		Display display = Display.getCurrent();
		if (display == null)
			display = Display.getDefault();
		return display;
	}
	
	public static List<DeploymentDesc> getDependentModels( IProject project, DeploymentDesc mainModel ) throws Exception {
		List<String> egldds = EclipseUtilities.getDependentDescriptors(project);
		
		final List<DeploymentDesc> models = new ArrayList<DeploymentDesc>();
		final IDEDeploymentDescFileLocator fileLocator = new IDEDeploymentDescFileLocator();
		for (String eglddFile : egldds ) {
			IFile eglddResource = (IFile)ResourcesPlugin.getWorkspace().getRoot().findMember(eglddFile);

			String ddName = eglddResource.getName();
    		ddName = ddName.substring(0, ddName.indexOf( eglddResource.getFileExtension() ) - 1);
    		if ( !ddName.equalsIgnoreCase( mainModel.getName() ) ) {
    			DeploymentDesc model = DeploymentDesc.createDeploymentDescriptor(ddName, eglddResource.getContents());
    			models.add( model );
    		}
//								resolveIncludes( model, fileLocator );
		}
	
		return models;
	}
	
	public static List getAllEglddsName( DeploymentContext context ) {
		List egldds = new ArrayList();
		egldds.add( context.getDeploymentDesc().getName() );
		for ( DeploymentDesc egldd : context.getDependentModels() ) {
			egldds.add( egldd.getName() );
		}
		return egldds;
	}
	
	public static Map getAllRUIHandlersInProject( IEGLProject project) throws EGLModelException{
		return getAllRUIHandlersInProject(project, false);
	}
	
	public static Map getAllRUIHandlersInProject( IEGLProject project , boolean searchReferencedProjects) throws EGLModelException
	{
		List handlerList = new ArrayList();
		final IEGLSearchScope projSearchScope = SearchEngine.createEGLSearchScope( new IEGLElement[]{ project }, searchReferencedProjects );
		AllPartsCache.getParts( projSearchScope, IEGLSearchConstants.HANDLER, new NullProgressMonitor(), handlerList );
		
		Map ruiMap = new HashMap();
		for ( Iterator it = handlerList.iterator(); it.hasNext(); )
		{
			PartInfo partinfo = (PartInfo)it.next();
			IPart part = partinfo.resolvePart( projSearchScope );
			if(part instanceof SourcePart){
			SourcePart sourcePart = (SourcePart)partinfo.resolvePart( projSearchScope );
			if ( sourcePart.isHandler() && sourcePart.getSubTypeSignature() != null && NameUtile.equals( RUIHANDLER, NameUtile.getAsName( Signature.toString( sourcePart.getSubTypeSignature() ) ) ) )
			{
				String impl = partinfo.getFullyQualifiedName();
				String htmlDefault = impl;
				
				int idx = htmlDefault.lastIndexOf( '.' );
				if ( idx != -1 )
				{
					htmlDefault = htmlDefault.substring( idx + 1 );
				}
				
				ruiMap.put( impl, htmlDefault );
			}
		}
			else if(part instanceof BinaryPart){
				BinaryPart binaryPart = (BinaryPart)partinfo.resolvePart( projSearchScope );
				if ( binaryPart.isHandler() && binaryPart.getSubTypeSignature() != null && NameUtile.equals( RUIHANDLER, NameUtile.getAsName( Signature.toString( binaryPart.getSubTypeSignature() ) ) ) )
				{
					String impl = partinfo.getFullyQualifiedName();
					String htmlDefault = impl;
					
					int idx = htmlDefault.lastIndexOf( '.' );
					if ( idx != -1 )
					{
						htmlDefault = htmlDefault.substring( idx + 1 );
					}
					
					ruiMap.put( impl, htmlDefault );
				}
			}
		}
		
		return ruiMap;
	}
	
	public static IGenerationResultsMessage createEGLDeploymentErrorMessage(String messageID, Object messageContributor, String[] inserts) {
		return new GenerationResultsMessage( new EGLMessage(
				getValidationResourceBundleName(),
				EGLMessage.EGL_ERROR_MESSAGE,
				messageID,
				EGLMessage.EGLMESSAGE_GROUP_VALIDATION,
				messageContributor,
				inserts,
				-1,
				-1,
				-1,
				-1) );
	}
	public static IGenerationResultsMessage createEGLDeploymentInformationalMessage(String messageID, Object messageContributor, String[] inserts) {
		return new GenerationResultsMessage( new EGLMessage(
				getValidationResourceBundleName(),
				EGLMessage.EGL_INFORMATIONAL_MESSAGE,
				messageID,
				EGLMessage.EGLMESSAGE_GROUP_DEPLOYMENT,
				messageContributor,
				inserts,
				-1,
				-1,
				-1,
				-1) );
	}
	
	public static String getValidationResourceBundleName() {

		return IEGLBaseConstants.EGL_VALIDATION_RESOURCE_BUNDLE_NAME;
	}
/*	
	public static void resolveIncludes(DeploymentDesc root, DeploymentDescFileLocator locator) throws Exception {
		resolveDeploymentDescriptors( root, root, locator );
	}
	
	private static void resolveDeploymentDescriptors( DeploymentDesc root, DeploymentDesc deploymentDesc, DeploymentDescFileLocator locator ) throws Exception
	{
		List includes = deploymentDesc.getIncludes();
		for ( int i = 0; i < includes.size(); i ++ ) {
			String include = (String)includes.get( i );
			try {
				DeploymentDesc includedDeploymentDesc = DeploymentDesc.createDeploymentDescriptor( include, locator.getInputStream( include ) );
				root.addRestBindingsAll(includedDeploymentDesc.getRestBindings());
				root.addSqlDatabaseBindingsAll(includedDeploymentDesc.getSqlDatabaseBindings());
				resolveDeploymentDescriptors( root, includedDeploymentDesc, locator );
			} catch ( Exception e ) {
				DeploymentResultsCollectorManager.getInstance().getCollector(DeploymentUtilities.getDeploymentTargetId(root.getDeploymentTarget(), null, root.getName()), root.getName(), false, false).addMessage(
						DeploymentUtilities.createDeployMessage(IStatus.WARNING, Messages.bind(Messages.deployment_action_no_dd_files_found, include)));
			}
		}
	}
*/

}
