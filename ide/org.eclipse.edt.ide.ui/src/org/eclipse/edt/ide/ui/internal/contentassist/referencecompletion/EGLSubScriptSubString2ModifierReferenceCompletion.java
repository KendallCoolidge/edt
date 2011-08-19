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
package org.eclipse.edt.ide.ui.internal.contentassist.referencecompletion;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.ide.core.internal.errors.ParseStack;
import org.eclipse.edt.ide.ui.internal.contentassist.proposalhandlers.EGLDeclarationProposalHandler;
import org.eclipse.jface.text.ITextViewer;

public class EGLSubScriptSubString2ModifierReferenceCompletion
		extends
			EGLAbstractReferenceCompletion {

	/**
	 * 
	 */
	public EGLSubScriptSubString2ModifierReferenceCompletion() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.edt.ide.ui.internal.contentassist.referencecompletion.EGLAbstractReferenceCompletion#precompileContexts()
	 */
	protected void precompileContexts() {
		addContext("package a; function a() a=b[c:"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.edt.ide.ui.internal.contentassist.referencecompletion.EGLAbstractReferenceCompletion#returnCompletionProposals(org.eclipse.edt.ide.core.internal.errors.ParseStack, java.lang.String, org.eclipse.jface.text.ITextViewer, int)
	 */
	protected List returnCompletionProposals(ParseStack parseStack, final String prefix, final ITextViewer viewer, final int documentOffset) {
		final List proposals = new ArrayList();
		//Get all integer data item variable proposals
		getBoundASTNodeForOffsetInStatement(viewer, documentOffset, new IBoundNodeProcessor() {public void processBoundNode(Node boundNode) {
			//Get all integer data item variable proposals
			proposals.addAll(
				new EGLDeclarationProposalHandler(viewer,
					documentOffset,
					prefix,
					boundNode)
						.getDataItemProposals(EGLDeclarationProposalHandler.INTEGER_DATAITEM));
		
			//Get all record variable proposals
			proposals.addAll(
				new EGLDeclarationProposalHandler(viewer,
					documentOffset,
					prefix,
					boundNode)
						.getRecordProposals(EGLDeclarationProposalHandler.ALL_RECORDS));
		}});		
		return proposals;
	}
}
