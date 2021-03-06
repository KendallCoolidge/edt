/*******************************************************************************
 * Copyright © 2011, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.mof.egl.impl;

import org.eclipse.edt.mof.egl.PatternType;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.utils.TimeStampAndIntervalPatternFixer;

public class PatternTypeImpl extends ParameterizedTypeImpl implements PatternType {
	private static int Slot_pattern=0;
	private static int totalSlots = 1;
	private String denormalizedPattern;
	
	public static int totalSlots() {
		return totalSlots + ParameterizedTypeImpl.totalSlots();
	}
	
	static {
		int offset = ParameterizedTypeImpl.totalSlots();
		Slot_pattern += offset;
	}
	@Override
	public String getPattern() {
		if (denormalizedPattern == null) {
			String value = (String)slotGet(Slot_pattern);
			denormalizedPattern = new TimeStampAndIntervalPatternFixer(value).toString();
		}
		return denormalizedPattern;
	}
	
	@Override
	public void setPattern(String value) {
		slotSet(Slot_pattern, value);
	}
	
	@Override
	// Assumes primitive types have the same classifier
	public boolean typeArgsEqual(Type type) {
		return this.getPattern().equals(((PatternType)type).getPattern());
	}

	@Override
	String typeArgsSignature() {
		return "(" + getPattern() + ")";
	}

}
