/*******************************************************************************
 * Copyright © 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.core.internal.model.index.impl;

/**
 * This interface provides constants used by the search engine.
 */
public interface IIndexConstants {
	/**
	 * The signature of the index file.
	 */
	public static final String SIGNATURE= "INDEX FILE 0.012"; //$NON-NLS-1$
	/**
	 * The separator for files in the index file.
	 */
	public static final char FILE_SEPARATOR= '/';
	/**
	 * The size of a block for a <code>Block</code>.
	 */
	public static final int BLOCK_SIZE= 8192;
}
