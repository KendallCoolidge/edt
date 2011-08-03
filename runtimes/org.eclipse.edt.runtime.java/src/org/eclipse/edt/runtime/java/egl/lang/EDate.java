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
import java.util.Calendar;

import org.eclipse.edt.javart.AnyBoxedObject;
import org.eclipse.edt.javart.Constants;
import org.eclipse.edt.javart.JavartException;
import org.eclipse.edt.javart.util.DateTimeUtil;
import org.eclipse.edt.javart.util.TimestampIntervalMask;

/**
 * A class for Timestamps. The value is a Calendar and a number of microseconds (an int) which is encapsulated in a
 * TimestampData object. Timestamps store a varying set of fields, from years down to microseconds. The first and last fields
 * are indicated by the startCode and endCode fields, which are set from the "CODE" constants.
 * @author mheitz
 */
public class EDate extends AnyBoxedObject<Calendar> {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

	public EDate() {
		this(DateTimeUtil.getNewCalendar());
	}

	public EDate(Calendar value) {
		super(value);
	}

	public static EDate ezeBox(Calendar value) {
		return new EDate((Calendar) value.clone());
	}

	public static Object ezeCast(Object value, Object[] constraints) throws JavartException {
		Integer[] args = new Integer[constraints.length];
		java.lang.System.arraycopy(constraints, 0, args, 0, args.length);
		return ezeCast(value, args);
	}

	public static BigDecimal ezeCast(Object value, Integer... args) throws JavartException {
		return (BigDecimal) EglAny.ezeCast(value, "asDate", EDate.class, new Class[] { Integer[].class }, args);
	}

	public static boolean ezeIsa(Object value) {
		return value instanceof EDate;
	}

	public static Calendar asDate(String date, Integer... length) throws JavartException {
		int startCode = ETimestamp.YEAR_CODE;
		int endCode = ETimestamp.DAY_CODE;
		return ETimestamp.convert(EString.asString(date, length), startCode, endCode);
	}

	/**
	 * Returns the difference between 2 dates
	 */
	public static Integer daysDifferent(Calendar aDate, Calendar bDate) throws JavartException {
		return (int) ((aDate.getTimeInMillis() - bDate.getTimeInMillis()) / (1000 * DateTimeUtil.SECONDS_PER_DAY));
	}

	/**
	 * Returns the adds days to a date
	 */
	public static Calendar addDays(Calendar aDate, Integer amount) throws JavartException {
		Calendar newDate = (Calendar) aDate.clone();
		newDate.roll(Calendar.DAY_OF_YEAR, amount);
		return newDate;
	}

	/**
	 * Returns the extension of a date
	 */
	public static Calendar extend(Calendar aDate, String timeSpanPattern) throws JavartException {
		// Default values in case the pattern doesn't specify things.
		int startCode = ETimestamp.YEAR_CODE;
		int endCode = ETimestamp.SECOND_CODE;
		TimestampIntervalMask mask = new TimestampIntervalMask(timeSpanPattern);
		if (mask.getStartCode() != -1 && mask.getStartCode() <= mask.getEndCode()) {
			startCode = mask.getStartCode();
			endCode = mask.getEndCode();
		}
		return new ETimestamp((Calendar) aDate.clone(), startCode, endCode).ezeUnbox();
	}
}
