/*******************************************************************************
 * Copyright Ã¦Â¼?2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.internal.contentassist.proposalcomputers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.edt.ide.core.internal.errors.ParseStack;
import org.eclipse.edt.ide.core.internal.errors.TokenStream;
import org.eclipse.edt.ide.ui.editor.EGLContentAssistInvocationContext;
import org.eclipse.edt.ide.ui.internal.contentassist.EGLCompletionProposalComputer;
import org.eclipse.edt.ide.ui.internal.contentassist.EGLDefinedKeywordCompletions;
import org.eclipse.edt.ide.ui.internal.contentassist.EGLKeywordCompletion;

public class KeywordProposalComputer extends EGLCompletionProposalComputer {


	public List computeCompletionProposals(EGLContentAssistInvocationContext context, IProgressMonitor monitor) {

		EGLKeywordCompletion[] keywordCompletions;
		ArrayList result = new ArrayList();
		// Set up the token stream
		TokenStream tokenStream = new TokenStream(getPrefix(context.getViewer(), context.getInvocationOffset()));
		tokenStream.skipPrefix();
		
		// Compute the prefix
		ArrayList prefixNodes = new ArrayList();
		
		// First recreate the parse stack
		// The parse stack is created up to the shift of the last nonextensible terminal
		// (hence the right edge may not be reduced) 
		ParseStack parseStack = getParser().parse(tokenStream);
		// Attach what the parser did not parse into the prefix
		prefixNodes.addAll(tokenStream.getPrefixNodes());
		
		if(inNativeFunction(context)){
			keywordCompletions = EGLDefinedKeywordCompletions.getNativeLibraryDefinedKeywordCompletions();
		}else{
			keywordCompletions = EGLDefinedKeywordCompletions.getDefinedKeywordCompletions();
		}
		
		for (int i = 0; i < keywordCompletions.length; i++) {
			result.addAll(keywordCompletions[i].computeCompletionProposals(parseStack, prefixNodes, context.getViewer(), context.getInvocationOffset(), false));
		}
		
		return result;
	}

	public List computeContextInformation(EGLContentAssistInvocationContext context, IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

}
