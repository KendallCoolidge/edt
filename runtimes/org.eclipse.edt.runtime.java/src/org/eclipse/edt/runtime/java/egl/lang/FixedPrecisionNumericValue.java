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

import java.math.BigDecimal;

import org.eclipse.edt.javart.AnyBoxedObject;


public class FixedPrecisionNumericValue extends AnyBoxedObject<BigDecimal> {

	int precision;
	int decimals;
	
	public FixedPrecisionNumericValue(BigDecimal value, int precision, int decimals) {
		super(value);
		this.precision = precision;
		this.decimals = decimals;
	}

	public int getPrecision() {
		return precision;
	}

	public int getDecimals() {
		return decimals;
	}
	
}
