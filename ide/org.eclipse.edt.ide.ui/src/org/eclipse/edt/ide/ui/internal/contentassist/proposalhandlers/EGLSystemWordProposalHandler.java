/*******************************************************************************
 * Copyright Ã¦Â¼?2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.internal.contentassist.proposalhandlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.edt.compiler.ISystemEnvironment;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.binding.IFunctionBinding;
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.binding.LibraryBinding;
import org.eclipse.edt.compiler.core.ast.DefaultASTVisitor;
import org.eclipse.edt.compiler.core.ast.File;
import org.eclipse.edt.compiler.core.ast.Handler;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.core.ast.Part;
import org.eclipse.edt.compiler.internal.IEGLConstants;
import org.eclipse.edt.compiler.internal.core.lookup.AbstractBinder;
import org.eclipse.edt.compiler.internal.core.lookup.System.SystemLibraryManager;
import org.eclipse.edt.ide.core.internal.compiler.SystemEnvironmentManager;
import org.eclipse.edt.ide.ui.internal.UINlsStrings;
import org.eclipse.edt.ide.ui.internal.contentassist.EGLCompletionProposal;
import org.eclipse.edt.ide.ui.internal.util.CapabilityFilterUtility;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

public class EGLSystemWordProposalHandler extends EGLAbstractProposalHandler {
	
	public static int RETURNS = 0;
	public static int NORETURNS = 1;
	
	private boolean isWithinPageHandler;

	public EGLSystemWordProposalHandler(ITextViewer viewer, int documentOffset, String prefix, IEditorPart editor, Node boundNode) {
		super(viewer, documentOffset, prefix, editor);
		
		while(!(boundNode instanceof File)) {
			if(boundNode instanceof Part) {
				boundNode.accept(new DefaultASTVisitor() {
					public boolean visit(Handler handler) {
						isWithinPageHandler = handler.getName().resolveBinding().getAnnotation(EGLUIJSF, IEGLConstants.HANDLER_SUBTYPE_JSF) != null;
						return false;
					}
				});
			}
			boundNode = boundNode.getParent();
		}
	}

	public List getProposals(int options, boolean addPrefix) {
		List proposals = new ArrayList();
		
		IFileEditorInput editorInput = (IFileEditorInput) editor.getEditorInput();
		ISystemEnvironment env = SystemEnvironmentManager.findSystemEnvironment(editorInput.getFile().getProject(), null); 
		SystemLibraryManager slm = env.getSystemLibraryManager();
		Collection systemLibaries = CapabilityFilterUtility.filterParts(
			slm.getLibraries().values(),
			new CapabilityFilterUtility.IPartBindingFilter[] {
				new CapabilityFilterUtility.IPartBindingFilter() {
					public boolean partBindingPasses(IPartBinding partBinding) {
						if(AbstractBinder.typeIs(partBinding, EGLJAVA, IEGLConstants.KEYWORD_J2EELIB))
							return isWithinPageHandler;
						return true;
					}
				}
			});
		
		for (Iterator iter = systemLibaries.iterator(); iter.hasNext();) {
			LibraryBinding systemLibrary = (LibraryBinding) iter.next();
			for(Iterator iter2 = systemLibrary.getDeclaredFunctions().iterator(); iter2.hasNext();) {
				IDataBinding nestedFunctionBinding = (IDataBinding) iter2.next();
				IFunctionBinding functionBinding = (IFunctionBinding) nestedFunctionBinding.getType();
				if(RETURNS == options && functionBinding.getReturnType() == null) {
					continue;
				}
				if(NORETURNS == options && functionBinding.getReturnType() != null) {
					continue;
				}
				if (nestedFunctionBinding.getName().toUpperCase().startsWith(getPrefix().toUpperCase())) {
					proposals.addAll(createFunctionInvocationProposals(functionBinding, UINlsStrings.bind(UINlsStrings.CAProposal_LibraryFunction, systemLibrary.getCaseSensitiveName()), EGLCompletionProposal.RELEVANCE_SYSTEM_WORD, addPrefix));
				}
			}
			for(Iterator iter2 = systemLibrary.getDeclaredData().iterator(); iter2.hasNext();) {
				IDataBinding variableBinding = (IDataBinding) iter2.next();
				if (variableBinding.getName().toUpperCase().startsWith(getPrefix().toUpperCase())) {
					proposals.add(createVariableProposal(variableBinding, EGLCompletionProposal.RELEVANCE_SYSTEM_WORD, systemLibrary.getCaseSensitiveName(), addPrefix));
				}
			}
		}
		return proposals;
	}
	
	/**
	 * @param dataBinding
	 * @return
	 */
	protected EGLCompletionProposal createVariableProposal(IDataBinding dataBinding, int relevance, String libraryName, boolean addPrefix) {
		String proposalString = dataBinding.getCaseSensitiveName();
		String prefix;
		if (addPrefix) {
			prefix = dataBinding.getDeclaringPart().getCaseSensitiveName() + "."; //$NON-NLS-1$
		}
		else {
			prefix = ""; //$NON-NLS-1$
		}

		return new EGLCompletionProposal(
			viewer,
			proposalString + " (" + getPartTypeString(dataBinding.getType()) + ")", //$NON-NLS-1$ //$NON-NLS-2$
			prefix + proposalString,
			UINlsStrings.bind(UINlsStrings.CAProposal_LibraryVariable, libraryName),
			getDocumentOffset() - getPrefix().length(),			
			getPrefix().length(),			
			prefix.length() + proposalString.length(),
			relevance);
	}
}
