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
package org.eclipse.edt.ide.core.internal.errors;

/**
 * @author winghong
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ParseStackEntry {
	public int state;
	public ParseNode node;
	
	public ParseStackEntry(int state, ParseNode node) {
		this.state = state;
		this.node = node;
	}
	
	public String toString() {
		return ParseTreePrinter.getLabel(node) + " " + state;
	}
}
