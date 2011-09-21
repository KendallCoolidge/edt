/*******************************************************************************
 * Copyright ©2000, 2011 IBM Corporation and others.
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

import org.eclipse.edt.compiler.core.ast.DefaultASTVisitor;
import org.eclipse.edt.compiler.core.ast.Interface;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.core.ast.Service;
import org.eclipse.edt.ide.core.internal.errors.ParseStack;
import org.eclipse.edt.ide.core.search.IEGLSearchConstants;
import org.eclipse.edt.ide.ui.internal.contentassist.proposalhandlers.EGLPartSearchProposalHandler;
import org.eclipse.edt.ide.ui.internal.contentassist.proposalhandlers.EGLPredefinedDataTypeProposalHandler;
import org.eclipse.edt.ide.ui.internal.contentassist.proposalhandlers.EGLPrimitiveProposalHandler;
import org.eclipse.jface.text.ITextViewer;

public class EGLAbstractFunctionReturnsReferenceCompletion extends EGLAbstractReferenceCompletion {
	protected boolean parens;

	/* (non-Javadoc)
	 * @see org.eclipse.edt.ide.ui.internal.contentassist.EGLAbstractReferenceCompletion#precompileContexts()
	 */
	protected void precompileContexts() {
		addContext("package a; function a() returns("); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.edt.ide.ui.internal.contentassist.EGLAbstractReferenceCompletion#returnCompletionProposals(com.ibm.etools.egl.pgm.errors.ParseStack, java.util.List, org.eclipse.jface.text.ITextViewer, int)
	 */
	protected List returnCompletionProposals(ParseStack parseStack, final String prefix, final ITextViewer viewer, final int documentOffset) {
		final List proposals = new ArrayList();
		final int partTypes[] = new int[] {IEGLSearchConstants.ITEM | IEGLSearchConstants.SERVICE | IEGLSearchConstants.INTERFACE | IEGLSearchConstants.DELEGATE};
		Node partNode = getPart(viewer, documentOffset);
		partNode.accept(new DefaultASTVisitor() {
			public boolean visit(Service service) {
				partTypes[0] = IEGLSearchConstants.ITEM | IEGLSearchConstants.SERVICE | IEGLSearchConstants.INTERFACE;
				return false;
			}
			public boolean visit(Interface interfacex) {
				partTypes[0] = IEGLSearchConstants.ITEM | IEGLSearchConstants.SERVICE | IEGLSearchConstants.INTERFACE;
				return false;
			}
		});
		getBoundASTNode(viewer, documentOffset, new String[] {""}, new CompletedNodeVerifier() { //$NON-NLS-1$
			public boolean nodeIsValid(Node astNode) {
				return astNode != null;
			}
		}, new IBoundNodeProcessor() {
			public void processBoundNode(Node boundNode) {
				proposals.addAll(new EGLPrimitiveProposalHandler(viewer, documentOffset, prefix, boundNode).getProposals());
				proposals.addAll(new EGLPredefinedDataTypeProposalHandler(viewer, documentOffset, prefix, boundNode).getProposals());				
			}
		});
		proposals.addAll(new EGLPartSearchProposalHandler(viewer, documentOffset, prefix, editor).getProposals(partTypes[0]));
		return proposals;
	}
}
