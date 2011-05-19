/*******************************************************************************
 * Copyright © 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.core.internal.model.search;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.edt.ide.core.model.IIndexConstants;


public class EGLSearchDocument extends SearchDocument {
	protected byte[] byteContents;
	protected char[] charContents;
	private String zipFilePath; 
//private IDocument 
	public EGLSearchDocument(java.util.zip.ZipEntry zipEntry, IPath zipFilePath, byte[] contents, SearchParticipant participant) {
		super(zipFilePath + IIndexConstants.JAR_FILE_ENTRY_SEPARATOR + zipEntry.getName(), participant);
		this.byteContents = contents;
		this.zipFilePath = zipFilePath.toOSString();
	}

	protected EGLSearchDocument(String documentPath, SearchParticipant participant) {
		super(documentPath, participant);
	}

	public byte[] getByteContent() {
		return this.byteContents;
	}

	public char[] getCharContent() {
		return charContents;
	}

	public String getEncoding() {
		return null;
	}

	public String getName() {
		return getPath();
	}

	public String getStringContent() throws IOException {
		return new String(getCharContent());
	}

	public String getType() {
		return "ir";
	}

}
