/*******************************************************************************
 * Copyright © 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
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
package org.eclipse.edt.ide.rui.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

import org.eclipse.edt.compiler.internal.interfaces.IGenerationMessageRequestor;
import org.eclipse.edt.compiler.core.ast.ISyntaxErrorRequestor;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.core.builder.IMarker;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.ide.core.internal.compiler.workingcopy.IProblemRequestorFactory;
//TODO EDT generation
//import com.ibm.etools.egl.genresults.GenerationResultsServer;
//import com.ibm.etools.egl.internal.genresults.impl.MessageEventImpl;
import org.eclipse.edt.compiler.internal.util.EGLMessage;
import org.eclipse.edt.compiler.internal.util.IGenerationResultsMessage;

public class WorkingCopyGenerationResult implements IProblemRequestor, ISyntaxErrorRequestor, IGenerationMessageRequestor, IProblemRequestorFactory {

	private boolean hasError = false;
	private boolean sendMessagesToGenerationResultsServer;
	
	private List genMessages = Collections.EMPTY_LIST;
	private int numGenErrors;
	private int numGenWarnings;
	
	private static final Set EGLMESSAGESTOIGNORE = new HashSet(Arrays.asList(new String[] {
		EGLMessage.EGLMESSAGE_GENERATION_PROGRAM_RESULTS_VIEW_MESSAGE
	}));
	
	public void acceptProblem(int startOffset, int endOffset, int severity, int problemKind, String[] inserts) {
		if(severity == IMarker.SEVERITY_ERROR){
			hasError = true;
		}
	}
	public void acceptProblem(Node astNode, int problemKind) {
		hasError = true;
	}
	public void acceptProblem(Node astNode, int problemKind, int severity) {
		if(severity == IMarker.SEVERITY_ERROR){
			hasError = true;
		}
	}
	public void acceptProblem(Node astNode, int problemKind, String[] inserts) {
		hasError = true;
	}
	public void acceptProblem(Node astNode, int problemKind, int severity, String[] inserts) {
		if(severity == IMarker.SEVERITY_ERROR){
			hasError = true;
		}
	}
	public void acceptProblem(int startOffset, int endOffset, int severity, int problemKind) {
		if(severity == IMarker.SEVERITY_ERROR){
			hasError = true;
		}
	}
	public void acceptProblem(int startOffset, int endOffset, int problemKind, String[] inserts) {
		hasError = true;
	}
	public void acceptProblem(int startOffset, int endOffset, int problemKind, boolean isError, String[] inserts) {
		if(isError){
			hasError = true;
		}
	}
	public boolean hasError() {
		return hasError;
	}
	public boolean shouldReportProblem(int problemKind) {
		return false;
	}
	public void incorrectNonTerminal(int nonTerminalType, int startOffset, int endOffset) {
		hasError = true;
	}
	public void incorrectPhrase(int nonTerminalType, int startOffset, int endOffset) {
		hasError = true;			
	}
	public void incorrectPreviousNonTerminal(int nonTerminalType, int startOffset, int endOffset) {
		hasError = true;			
	}
	public void incorrectPreviousTerminal(int terminalType, int startOffset, int endOffset) {
		hasError = true;			
	}
	public void incorrectTerminal(int terminalType, int startOffset, int endOffset) {
		hasError = true;			
	}
	public void invalidCharacterInHexLiteral(int startOffset, int endOffset) {
		hasError = true;			
	}
	public void invalidEscapeSequence(int startOffset, int endOffset) {
		hasError = true;			
	}
	public void keywordAsName(int terminalType, int startOffset, int endOffset) {
		hasError = true;
	}
	public void missingEndForPart(int startOffset, int endOffset) {
		hasError = true;
	}
	public void missingNonTerminal(int nonTerminalType, int startOffset, int endOffset) {
		hasError = true;			
	}
	public void missingPreviousNonTerminal(int nonTerminalType, int startOffset, int endOffset) {
		hasError = true;
	}
	public void missingPreviousTerminal(int terminalType, int startOffset, int endOffset) {
		hasError = true;			
	}
	public void missingScopeCloser(int terminalType, int startOffset, int endOffset) {
		hasError = true;			
	}
	public void missingTerminal(int terminalType, int startOffset, int endOffset) {
		hasError = true;
	}
	public void panicPhrase(int startOffset, int endOffset) {
		hasError = true;
	}
	public void tooManyErrors() {
		hasError = true;
	}
	public void unclosedBlockComment(int startOffset, int endOffset) {
		hasError = true;
	}
	public void unclosedDLI(int startOffset, int endOffset) {
		hasError = true;
	}
	public void unclosedSQL(int startOffset, int endOffset) {
		hasError = true;
	}
	public void unclosedSQLCondition(int startOffset, int endOffset) {
		hasError = true;
	}
	public void unclosedString(int startOffset, int endOffset) {
		hasError = true;
	}
	public void unexpectedPhrase(int startOffset, int endOffset) {
		hasError = true;
	}
	public void unexpectedPreviousTerminal(int startOffset, int endOffset) {
		hasError = true;
	}
	public void unexpectedTerminal(int startOffset, int endOffset) {
		hasError = true;	
	}
	public void whitespaceInDLI(int startOffset, int endOffset) {
		hasError = true;
	}
	public void whitespaceInSQL(int startOffset, int endOffset) {
		hasError = true;
	}
	public void whitespaceInSQLCondition(int startOffset, int endOffset) {
		hasError = true;	
	}
	
	public void addMessage(IGenerationResultsMessage message) {
		if(!EGLMESSAGESTOIGNORE.contains(message.getId())) {
			if (message.isError()) {
				hasError = true;
			}
			
			if(genMessages == Collections.EMPTY_LIST) {
				genMessages = new ArrayList();
			}
//TODO EDT generation			
//			if (sendMessagesToGenerationResultsServer) {
//				GenerationResultsServer.getInstance().message(new MessageEventImpl(message.getSeverity(), message.getId(), message.getBuiltMessageWithLineAndColumn()));
//			}
			
			genMessages.add(message);
			
			if(message.isError() && !EGLMessage.EGLMESSAGE_GENERATION_FAILED.equals(message.getId())) {
				numGenErrors += 1;
			}
			else if(message.isWarning()) {
				numGenWarnings += 1;
			}	
		}
	}

	public void addMessages(List<IGenerationResultsMessage> newmsgs) {
		Iterator i = newmsgs.iterator();
		while (i.hasNext()) {
			EGLMessage msg = (EGLMessage) i.next();
			addMessage(msg);
		}
	}

	public List getMessages() {
		return genMessages;
	}
	
	public int getNumGenErrors() {
		return numGenErrors;
	}
	
	public int getNumGenWarnings() {
		return numGenWarnings;
	}

	public boolean isError() {
		return hasError;
	}
	
	public void clear() {
		hasError = false;
		genMessages = Collections.EMPTY_LIST;
		numGenWarnings = 0;
		numGenErrors = 0;
	}
	
	public boolean hasGenerationError(){
		return genMessages.size() > 0;
	}
	
	public void setHasError(boolean error){
		hasError = error;
	}
	
	public IProblemRequestor getContainerContextTopLevelProblemRequestor(IFile file, String functionPartName, String containerContextName, IPath containerContextPath, boolean containerContextDependent) {
		return this;
	}
	public IProblemRequestor getFileProblemRequestor(IFile file) {
		return this;
	}
	public IProblemRequestor getGenericTopLevelFunctionProblemRequestor(IFile file, String partName, boolean containerContextDependent) {
		return this;
	}
	public IProblemRequestor getProblemRequestor(IFile file, String partName) {
		return this;
	}
	public ISyntaxErrorRequestor getSyntaxErrorRequestor(IFile file) {
		return this;
	}
	public void sendMessagesToGenerationResultsServer(boolean bool) {
		sendMessagesToGenerationResultsServer = bool; 		
	}

}
