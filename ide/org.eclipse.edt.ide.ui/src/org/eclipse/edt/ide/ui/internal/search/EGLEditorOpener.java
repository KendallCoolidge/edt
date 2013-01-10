/*******************************************************************************
 * Copyright © 2004, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.internal.search;

import org.eclipse.core.resources.IFile;
import org.eclipse.edt.ide.ui.EDTUIPlugin;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

public class EGLEditorOpener {
	private IEditorPart fEditor;
	
	IEditorPart open(Match match) throws PartInitException {
		IWorkbenchPage wbPage= EDTUIPlugin.getActivePage();
		if (NewSearchUI.reuseEditor())
			return showWithReuse(match, wbPage);
		else
			return showWithoutReuse(match, wbPage);
	}
	
	private IEditorPart showWithoutReuse(Match match, IWorkbenchPage wbPage) throws PartInitException {
		return IDE.openEditor(wbPage, (IFile) match.getElement(), false);
	}

	private IEditorPart showWithReuse(Match match, IWorkbenchPage wbPage) throws PartInitException {
		IFile file= (IFile) match.getElement();
		String editorID= getEditorID(file);
		return showInEditor(wbPage, new FileEditorInput(file), editorID);
	}


	private String getEditorID(IFile file) {
		IEditorDescriptor desc= IDE.getDefaultEditor(file);
		if (desc == null)
			return EDTUIPlugin.getDefault().getWorkbench().getEditorRegistry().findEditor(IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID).getId();
		else
			return desc.getId();
	}


	private boolean isPinned(IEditorPart editor) {
		if (editor == null)
			return false;
		
		IEditorReference[] editorRefs= editor.getEditorSite().getPage().getEditorReferences();
		int i= 0;
		while (i < editorRefs.length) {
			if (editor.equals(editorRefs[i].getEditor(false)))
				return editorRefs[i].isPinned();
			i++;
		}
		return false;
	}
	
	private IEditorPart showInEditor(IWorkbenchPage page, IEditorInput input, String editorId) throws PartInitException {
		IEditorPart editor= page.findEditor(input);
		if (editor != null)
			page.bringToTop(editor);
		else {
			boolean isOpen= false;
			if (fEditor != null) {
				IEditorReference[] parts= page.getEditorReferences();
				int i= 0;
				while (!isOpen && i < parts.length)
					isOpen= fEditor == parts[i++].getEditor(false);
			}
				
			boolean canBeReused= isOpen && !fEditor.isDirty() && !isPinned(fEditor);
			boolean showsSameInputType= fEditor != null && fEditor.getSite().getId().equals(editorId);
			if (canBeReused && !showsSameInputType) {
				page.closeEditor(fEditor, false);
				fEditor= null;
			}
			
			if (canBeReused && showsSameInputType) {
				((IReusableEditor)fEditor).setInput(input);
				page.bringToTop(fEditor);
				editor= fEditor;
			} else {
					editor= page.openEditor(input, editorId, false);
					if (editor instanceof IReusableEditor)
						fEditor= editor;
					else
						fEditor= null;
			}
		}
		return editor;
	}

}
