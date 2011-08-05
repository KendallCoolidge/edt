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
package org.eclipse.edt.runtime.java.egl.lang;

import org.eclipse.edt.javart.AnyBoxedObject;
import org.eclipse.edt.javart.Constants;

public class NullType extends EglAny {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

	private NullType() {
		super();
	}

	public static <R extends Object> Boolean equals(AnyBoxedObject<R> s1, AnyBoxedObject<R> s2) {
		if ((s1 == null || s1.ezeUnbox() == null) && (s2 == null || s2.ezeUnbox() == null))
			return true;
		if (s1 == null || s2 == null)
			return false;
		return s1.equals(s2);
	}

	public static <R extends Object> Boolean notEquals(AnyBoxedObject<R> s1, AnyBoxedObject<R> s2) {
		if ((s1 == null || s1.ezeUnbox() == null) && (s2 == null || s2.ezeUnbox() == null))
			return false;
		if (s1 == null || s2 == null)
			return true;
		return !s1.equals(s2);
	}

	public static egl.lang.NullType asNullType(egl.lang.EglAny source) {
		return null;
	}
}
