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
package org.eclipse.edt.runtime.java.eglx.lang;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.edt.javart.AnyBoxedObject;
import org.eclipse.edt.javart.Constants;
import org.eclipse.edt.javart.messages.Message;

import eglx.lang.AnyException;
import eglx.lang.TypeCastException;

public class ESmallfloat extends AnyBoxedObject<Float> implements eglx.lang.ENumber {
	/**
	 * The version ID used in serialization.
	 */
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

	private ESmallfloat(Float value) {
		super(value);
	}

	public static ESmallfloat ezeBox(Float value) {
		return new ESmallfloat(value);
	}

	public static Float ezeCast(Object value) throws AnyException {
		return (Float) EAny.ezeCast(value, "asSmallfloat", ESmallfloat.class, null, null);
	}

	public static boolean ezeIsa(Object value) {
		if (value instanceof ENumber && ((ENumber) value).ezeUnbox() instanceof Float)
			return true;
		return value instanceof ESmallfloat || value instanceof Float;
	}

	public String toString() {
		return EString.asString(object);
	}

	public static Float asSmallfloat(Short value) {
		if (value == null)
			return null;
		return value.floatValue();
	}

	public static Float asSmallfloat(ESmallint value) {
		if (value == null)
			return null;
		return value.ezeUnbox().floatValue();
	}

	public static Float asSmallfloat(Integer value) {
		if (value == null)
			return null;
		return value.floatValue();
	}

	public static Float asSmallfloat(EInt value) {
		if (value == null)
			return null;
		return value.ezeUnbox().floatValue();
	}

	public static Float asSmallfloat(Long value) {
		if (value == null)
			return null;
		return value.floatValue();
	}

	public static Float asSmallfloat(EBigint value) {
		if (value == null)
			return null;
		return value.ezeUnbox().floatValue();
	}

	public static Float asSmallfloat(Float value) throws AnyException {
		if (value == null)
			return null;
		return value.floatValue();
	}

	public static Float asSmallfloat(ESmallfloat value) throws AnyException {
		if (value == null)
			return null;
		return value.ezeUnbox().floatValue();
	}

	public static Float asSmallfloat(Double value) throws AnyException {
		if (value == null)
			return null;
		return value.floatValue();
	}

	public static Float asSmallfloat(EFloat value) throws AnyException {
		if (value == null)
			return null;
		return value.ezeUnbox().floatValue();
	}

	public static Float asSmallfloat(BigDecimal value) {
		if (value == null)
			return null;
		return value.floatValue();
	}

	public static Float asSmallfloat(EDecimal value) {
		if (value == null)
			return null;
		return value.ezeUnbox().floatValue();
	}

	public static Float asSmallfloat(BigInteger value) {
		if (value == null)
			return null;
		return value.floatValue();
	}

	public static Float asSmallfloat(Number value) {
		if (value == null)
			return null;
		return value.floatValue();
	}

	public static Float asSmallfloat(ENumber value) {
		if (value == null)
			return null;
		return value.ezeUnbox().floatValue();
	}

	public static Float asSmallfloat(String value) throws AnyException {
		if (value == null)
			return null;
		try
		{
			//TODO this is too permissive.  See the doc for the method.  We should
			// only allow a sign, digits, decimal point, digits, and exponent.  The
			// sign and everything after the first set of digits is optional.
			return Float.valueOf( value );
		}
		catch ( Exception ex )
		{
			// It's invalid.
			TypeCastException tcx = new TypeCastException();
			tcx.actualTypeName = "string";
			tcx.castToName = "smallfloat";
			throw tcx.fillInMessage( Message.CONVERSION_ERROR, value, tcx.actualTypeName, tcx.castToName );
		}
	}

	public static Float asSmallfloat(EString value) throws AnyException {
		if (value == null)
			return null;
		return asSmallfloat(value.ezeUnbox());
	}

	/**
	 * this is different. Normally we need to place the "as" methods in the corresponding class, but asNumber methods need to
	 * go into the class related to the argument instead
	 */
	public static ENumber asNumber(Float value) throws AnyException {
		if (value == null)
			return null;
		return ENumber.asNumber(value);
	}

	public static ENumber asNumber(ESmallfloat value) throws AnyException {
		if (value == null)
			return null;
		return ENumber.asNumber(value.ezeUnbox());
	}

	public static float plus(float op1, float op2) throws AnyException {
		return op1 + op2;
	}

	public static float minus(float op1, float op2) throws AnyException {
		return op1 - op2;
	}

	public static float divide(float op1, float op2) throws AnyException {
		return op1 / op2;
	}

	public static float multiply(float op1, float op2) throws AnyException {
		return op1 * op2;
	}

	public static float remainder(float op1, float op2) throws AnyException {
		return op1 % op2;
	}

	public static float power(float op1, float op2) throws AnyException {
		return (float) StrictMath.pow(op1, op2);
	}

	public static int compareTo(Float op1, Float op2) throws AnyException {
		if (op1 == null && op2 == null)
			return 0;
		return op1.compareTo(op2);
	}

	public static boolean equals(Float op1, Float op2) {
		if (op1 == null || op2 == null)
			return false;
		return op1.compareTo(op2) == 0;
	}

	public static boolean notEquals(Float op1, Float op2) {
		if (op1 == null || op2 == null)
			return false;
		return op1.compareTo(op2) != 0;
	}
}
