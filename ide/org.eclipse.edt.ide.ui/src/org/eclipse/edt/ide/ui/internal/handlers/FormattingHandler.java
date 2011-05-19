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
package org.eclipse.edt.ide.ui.internal.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.edt.ide.core.model.EGLCore;
import org.eclipse.edt.ide.core.model.EGLModelException;
import org.eclipse.edt.ide.core.model.IBuffer;
import org.eclipse.edt.ide.core.model.IEGLElement;
import org.eclipse.edt.ide.core.model.IEGLFile;
import org.eclipse.edt.ide.core.model.IEGLProject;
import org.eclipse.edt.ide.core.model.IPackageFragment;
import org.eclipse.edt.ide.core.model.IPackageFragmentRoot;
import org.eclipse.edt.ide.core.model.IRegion;
import org.eclipse.edt.ide.ui.EDTUIPlugin;
import org.eclipse.edt.ide.ui.internal.CodeFormatterUtil;
import org.eclipse.edt.ide.ui.internal.EGLUI;
import org.eclipse.edt.ide.ui.internal.UINlsStrings;
import org.eclipse.edt.ide.ui.internal.actions.WorkbenchRunnableAdapter;
import org.eclipse.edt.ide.ui.internal.codemanipulation.OrganizeImportsOperation.SyntaxErrorHelper;
import org.eclipse.edt.ide.ui.internal.dialogs.OptionalMessageDialog;
import org.eclipse.edt.ide.ui.internal.dialogs.ProblemDialog;
import org.eclipse.edt.ide.ui.internal.editor.DocumentAdapter;
import org.eclipse.edt.ide.ui.internal.editor.EGLEditor;
import org.eclipse.edt.ide.ui.internal.formatting.FormattingStrategy;
import org.eclipse.edt.ide.ui.internal.formatting.ui.ProfileManager;
import org.eclipse.edt.ide.ui.internal.util.EditorUtility;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.DocumentRewriteSessionType;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.formatter.FormattingContext;
import org.eclipse.jface.text.formatter.FormattingContextProperties;
import org.eclipse.jface.text.formatter.IFormattingContext;
import org.eclipse.jface.text.formatter.MultiPassContentFormatter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.texteditor.ITextEditor;

public class FormattingHandler extends AbstractHandler implements IHandler {

	protected IStructuredSelection fSelection;
	protected IWorkbenchSite fSite;	
	protected EGLEditor fEditor;
	
    //key is the IEGLFile, value is IEditorPart, use HashMap, so it allows null as value
    //if file is opened in an editor, then it does not need to be saved
    //otherwise the file needs to be saved
    private HashMap fileNeedsSave = new HashMap();    
    private DocumentRewriteSession fRewriteSession;
    private IRegion fPartialRegion = null;
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		// Initialize selection	if called from Part Reference or Part List
	    ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			fSelection = (IStructuredSelection) selection;
			fSite = HandlerUtil.getActiveSite( event );			
		}
		
		// Initialize editor if called from EGL Editor
		IEditorPart editor = HandlerUtil.getActiveEditor( event );
		if( editor instanceof EGLEditor ) {
			fEditor = (EGLEditor)editor;
			if(fSelection == null && editor != null)
			{	
				IEditorInput editorInput = editor.getEditorInput();
				// Could be a VirtualEditorInput if coming from PageDesigners QEV
				if (editorInput instanceof IFileEditorInput) {
					IResource resource = ((IFileEditorInput) editorInput).getFile();
					IEGLElement element = EGLCore.create(resource);
					fSite = editor.getSite();
					fSelection = new StructuredSelection( element );
				}			
		    }			
		}	
		
		if( fSelection != null )
		{
			List eglFiles = getEGLFiles( fSelection );
			int selCnt = eglFiles.size();
			if( selCnt == 0 )
				return null;
			
			if( selCnt > 1 && fSite != null ){
				int returnCode = OptionalMessageDialog.open("EGLFormat",  //$NON-NLS-1$
						fSite.getShell(), 
						UINlsStrings.FormatTitle, null, 
						UINlsStrings.UndoNotSupportedMsg, 
						MessageDialog.WARNING, 
						new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, 0);
				if( returnCode != OptionalMessageDialog.NOT_SHOWN &&
						returnCode != Window.OK ) {
					return null;		
				}
			}			
			if( selCnt > 1 ) {
				runOnMultiple( eglFiles, null );
			} else {
				runOnSingle((IEGLFile)eglFiles.get(0));
			}
		}
		return null;
	}
	
	private List getEGLFiles(IStructuredSelection selection)
	{
	    List result = new ArrayList();
	    if(selection != null)
	    {
	        Iterator it = selection.iterator();
	        while(it.hasNext())
	        {
	        	try
				{
		            Object element = it.next();
		            if(element instanceof IEGLElement)
		            {
		                IEGLElement eglElem = (IEGLElement)element;
		                getEGLElements( eglElem, result );
		            }
		            else if(element instanceof IProject)
		            {
		            	 IEGLProject eglproj = EGLCore.create((IProject)element);
		            	 getEGLElements( eglproj, result );
		            }
		            else if(element instanceof IResource)
		            {
		            	IEGLElement eglResourceElem = EGLCore.create((IResource)element);
		            	getEGLElements( eglResourceElem, result );
		            }
				}
	        	catch(EGLModelException e)
				{
	        		EDTUIPlugin.log(e);
				}
	        }
	    }
	    return result;
	}	
	
	private void runOnMultiple(final List eglfiles, final String formatProfileName){
		String message = UINlsStrings.FormatComplete;
		final MultiStatus status = new MultiStatus( EDTUIPlugin.PLUGIN_ID, IStatus.OK, message, null );
		
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();

		try{
			if(eglfiles.size() == 1){
				Object eglFile = eglfiles.get(0);
				EditorUtility.openInEditor(eglFile);
				fileNeedsSave.put(eglFile, EditorUtility.isOpenInEditor(eglFile));
			}
			
			progressService.run(true, true, new WorkbenchRunnableAdapter(new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor) throws CoreException {
					doRunOnMultiple(eglfiles, formatProfileName, status, monitor);					
				}			
			}));
			
			if(!status.isOK() && fSite != null){
				String title = UINlsStrings.EGLFormatting;
				ProblemDialog.open(fSite.getShell(), title, null, status);
			}
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}		
		catch (InterruptedException e) {
			//cancel by user
		}		
		catch(CoreException ex){
			ex.printStackTrace();
		}
	}
	
	private void doRunOnMultiple(List eglfiles, String formatProfileName, MultiStatus status, IProgressMonitor monitor) throws OperationCanceledException{
		if( monitor == null ) {
			monitor = new NullProgressMonitor();
		}
		monitor.setTaskName( UINlsStrings.FormattingProgress );
		
		int size = eglfiles.size();
		monitor.beginTask("", size*4); //$NON-NLS-1$
			
		try{			
			for(int i=0; i<size; i++){
				if(monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				IEGLFile eglfile = (IEGLFile)(eglfiles.get(i));
				try {
					SyntaxErrorHelper synErr = runFormat(eglfile, formatProfileName, monitor);
					if(synErr != null){
						String msg = UINlsStrings.bind( UINlsStrings.SyntaxError, new String[]{eglfile.getPath().makeRelative().toString()} );
						status.add(new Status(IStatus.INFO, EDTUIPlugin.PLUGIN_ID, IStatus.ERROR, msg, null));
					}
				} catch (CoreException e) {
					status.add(e.getStatus());
				}
			}
		}
		finally{
			monitor.done();
		}
	}
	
	private SyntaxErrorHelper runFormat(IEGLFile eglfile, String formatProfileName, IProgressMonitor monitor) throws CoreException{
		IPath path = eglfile.getPath();
		
		SyntaxErrorHelper synErr = null;
		ITextFileBufferManager manager = FileBuffers.getTextFileBufferManager();
		boolean connectedTextBuffer = false;
		IEGLFile workingCopy = null;
		try{
			Object editorpart = fileNeedsSave.get(eglfile);
			boolean needSave = editorpart == null;
			
			IDocument doc = null;
			if(!needSave){//means the file is opened in an editor							
				if(editorpart instanceof EGLEditor ||
						editorpart instanceof MultiPageEditorPart){
					synchronized( eglfile ){
						workingCopy = (IEGLFile)(eglfile.getSharedWorkingCopy(null, EGLUI.getBufferFactory(), null));
					}
					IBuffer buf = workingCopy.getBuffer();
					if( buf instanceof DocumentAdapter ){
						doc = ((DocumentAdapter)buf).getDocument();
					}								
				}
			}
			
			ITextFileBuffer filebuffer = null;
			if(doc == null){
				manager.connect(path, new SubProgressMonitor(monitor, 1));	
				connectedTextBuffer = true;
				filebuffer = manager.getTextFileBuffer(path);
				doc = filebuffer.getDocument();
			}
			String filePathLocation = path.makeRelative().toString();
			monitor.subTask(filePathLocation);
			synErr = formatEGLFile(doc, formatProfileName, needSave);
			//synErr = doFormat(doc, formatProfileName);
			
			if(needSave && filebuffer != null && filebuffer.isDirty()){
				filebuffer.commit(new SubProgressMonitor(monitor, 2), false);
			}
			else
				monitor.worked(2);
		}
		finally{
			if(workingCopy != null)
				workingCopy.destroy();
			
			if(connectedTextBuffer)
				manager.disconnect(path, new SubProgressMonitor(monitor, 1));
		}					
		return synErr;
	}
	
	private SyntaxErrorHelper formatEGLFile(final IDocument document, final String formatProfileName, final boolean needSave){
		final SyntaxErrorHelper[] synErr = new SyntaxErrorHelper[1];
		if(!needSave && fSite != null){
			fSite.getShell().getDisplay().syncExec(new Runnable(){
				public void run() {
					synErr[0] = doFormat(document, formatProfileName);					
				}				
			});
		}
		else{
			synErr[0] = doFormat(document, formatProfileName);	//run in context thread
		}
		return synErr[0];
	}
	
	private SyntaxErrorHelper doFormat(IDocument document, String formatProfileName){
		ProfileManager profileMgr = ProfileManager.getInstance();
		//save the original selected profile
		EObject originalSelProfile = profileMgr.getSelectedProfile();			
		
		Map options = CodeFormatterUtil.getFormattingOptionMapByProfileName(formatProfileName, profileMgr);			
		final IFormattingContext context = new FormattingContext();
		FormattingStrategy eglFormattingStrategy = new FormattingStrategy();
		try{
			context.setProperty(FormattingContextProperties.CONTEXT_PREFERENCES, options);
			
			if(fPartialRegion != null){
				context.setProperty(FormattingContextProperties.CONTEXT_REGION, fPartialRegion);
				context.setProperty(FormattingContextProperties.CONTEXT_DOCUMENT, Boolean.FALSE);
			}
			else
				context.setProperty(FormattingContextProperties.CONTEXT_DOCUMENT, Boolean.TRUE);
			final MultiPassContentFormatter formatter= new MultiPassContentFormatter(IDocumentExtension3.DEFAULT_PARTITIONING, IDocument.DEFAULT_CONTENT_TYPE); 
			formatter.setMasterStrategy(eglFormattingStrategy);;
			
			try{
				startSequentialRewriteMode(document);
				formatter.format(document, context);
			}
			finally {
				stopSequentialRewriteMode(document);
			}			
		}
		finally{
			context.dispose();
			//reset the current format profile selection back to its original
			profileMgr.setSelectedProfile(originalSelProfile);
		}
		
		SyntaxErrorHelper synErr = eglFormattingStrategy.get1stSyntaxErrorMsg();
		return synErr;
	}
	
	private void startSequentialRewriteMode(IDocument document) {
		if (document instanceof IDocumentExtension4) {
			IDocumentExtension4 extension= (IDocumentExtension4) document;
			fRewriteSession= extension.startRewriteSession(DocumentRewriteSessionType.SEQUENTIAL);
		} else if (document instanceof IDocumentExtension) {
			IDocumentExtension extension= (IDocumentExtension) document;
			extension.startSequentialRewrite(false);
		}
	}
	
	private void stopSequentialRewriteMode(IDocument document) {
		if (document instanceof IDocumentExtension4) {
			IDocumentExtension4 extension= (IDocumentExtension4) document;
			extension.stopRewriteSession(fRewriteSession);
		} else if (document instanceof IDocumentExtension) {
			IDocumentExtension extension= (IDocumentExtension)document;
			extension.stopSequentialRewrite();
		}
	}
		
	private void runOnSingle(final IEGLFile eglFile){
		try
		{
			IEditorPart editor = EditorUtility.openInEditor(eglFile);
			fileNeedsSave.put(eglFile, EditorUtility.isOpenInEditor(eglFile));
			
			IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
			IRunnableContext context= fSite != null ? fSite.getWorkbenchWindow() : null;
			if (context == null) {
				context= progressService;
			}

			final SyntaxErrorHelper[] synErr = new SyntaxErrorHelper[1];
			progressService.run(true, true,
					new WorkbenchRunnableAdapter(new IWorkspaceRunnable(){
						public void run(final IProgressMonitor monitor) throws CoreException {
							synErr[0] = runFormat(eglFile, null, monitor);							
						}				
					}));
			
			postRun(synErr[0], (editor instanceof ITextEditor) ? (ITextEditor)editor : null);
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		}
		catch(CoreException ex){
			ex.printStackTrace();
		}		
	}
	
	protected void postRun(SyntaxErrorHelper syntaxErr, ITextEditor editor) {
		if(syntaxErr != null)
		{
			MessageDialog.openInformation(fSite.getShell(), UINlsStrings.EGLFormatting, syntaxErr.fErrMsg);
			
			//highlight the syntax error location
			if(editor != null && syntaxErr.fSynErr.startOffset > 0)
				editor.selectAndReveal(syntaxErr.fSynErr.startOffset, syntaxErr.fSynErr.endOffset-syntaxErr.fSynErr.startOffset+1);
		}
	}
	
	private void getEGLElements(IEGLElement eglElem, List result) throws EGLModelException
	{
		if(eglElem != null)
		{
	        switch(eglElem.getElementType())
	        {
	        case IEGLElement.EGL_PROJECT:
	        	IPackageFragmentRoot[] pkgRoots = ((IEGLProject)eglElem).getPackageFragmentRoots();
	        	for(int i=0; i<pkgRoots.length; i++)
	        	{
	        		collectEGLFiles(pkgRoots[i], result);
	        	}		                	
	            break;
	        case IEGLElement.PACKAGE_FRAGMENT_ROOT:
	        	collectEGLFiles((IPackageFragmentRoot)eglElem, result);
	            break;
	        case IEGLElement.PACKAGE_FRAGMENT:
	        	collectEGLFiles((IPackageFragment)eglElem, result);
	            break;
	        case IEGLElement.EGL_FILE:
	            result.add(eglElem);	
	            fileNeedsSave.put(eglElem, EditorUtility.isOpenInEditor(eglElem));
	            break;
	        }
		}
	}
	
	private void collectEGLFiles(IPackageFragment pkg, List result) throws EGLModelException
	{
    	IEGLFile[] eglfiles = pkg.getEGLFiles();
    	for(int i=0; i<eglfiles.length; i++)
    	{
    		result.add(eglfiles[i]);
    		fileNeedsSave.put(eglfiles[i], EditorUtility.isOpenInEditor(eglfiles[i]));
    	}
	}
	
	private void collectEGLFiles(IPackageFragmentRoot pkgRoot, List result) throws EGLModelException
	{
		if(pkgRoot.getKind() == IPackageFragmentRoot.K_SOURCE)
		{
			IEGLElement[] children = pkgRoot.getChildren();
			for(int i=0; i<children.length; i++)
			{
				collectEGLFiles((IPackageFragment)children[i], result);
			}
		}
	}
	
}
