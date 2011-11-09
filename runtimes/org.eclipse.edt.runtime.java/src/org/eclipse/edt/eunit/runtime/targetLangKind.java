/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.edt.eunit.runtime;
public enum targetLangKind {
	JAVA(1),
	JAVASCRIPT(2);
	private final int value;
	private targetLangKind(int value) {
		this.value = value;
	}
	private targetLangKind() {
		value = -1;
	}
	public int getValue() {
		return value;
	}
}
