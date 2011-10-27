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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.edt.javart.AnyBoxedObject;
import org.eclipse.edt.javart.Constants;
import org.eclipse.edt.javart.messages.Message;
import org.eclipse.edt.javart.util.DateTimeUtil;
import org.eclipse.edt.javart.util.JavartDateFormat;
import org.eclipse.edt.javart.util.TimestampIntervalMask;

import eglx.lang.AnyException;
import eglx.lang.InvalidArgumentException;
import eglx.lang.TypeCastException;

/**
 * A class for Timestamps. The value is a Calendar and a number of microseconds (an int) which is encapsulated in a
 * TimestampData object. Timestamps store a varying set of fields, from years down to microseconds. The first and last fields
 * are indicated by the startCode and endCode fields, which are set from the "CODE" constants.
 * @author mheitz
 */
public class ETimestamp extends AnyBoxedObject<Calendar> {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	public static final String DefaultPattern = "yyyyMMddHHmmss";
	public static final String DefaultFormatPattern = "yyyy-MM-dd HH:mm:ss.SSSSSS";

	/**
	 * Indicates that years are stored by this timestamp.
	 */
	public static final int YEAR_CODE = 0;

	/**
	 * Indicates that months are stored by this timestamp.
	 */
	public static final int MONTH_CODE = 1;

	/**
	 * Indicates that days are stored by this timestamp.
	 */
	public static final int DAY_CODE = 2;

	/**
	 * Indicates that hours are stored by this timestamp.
	 */
	public static final int HOUR_CODE = 3;

	/**
	 * Indicates that minutes are stored by this timestamp.
	 */
	public static final int MINUTE_CODE = 4;

	/**
	 * Indicates that seconds are stored by this timestamp.
	 */
	public static final int SECOND_CODE = 5;

	/**
	 * Indicates that tenths of seconds are stored by this timestamp.
	 */
	public static final int FRACTION1_CODE = 6;

	/**
	 * Indicates that hundredths of seconds are stored by this timestamp.
	 */
	public static final int FRACTION2_CODE = 7;

	/**
	 * Indicates that milliseconds are stored by this timestamp.
	 */
	public static final int FRACTION3_CODE = 8;

	/**
	 * Indicates that ten-thousandths of seconds are stored by this timestamp.
	 */
	public static final int FRACTION4_CODE = 9;

	/**
	 * Indicates that hundred-thousandths of seconds are stored by this timestamp.
	 */
	public static final int FRACTION5_CODE = 10;

	/**
	 * Indicates that microseconds are stored by this timestamp.
	 */
	public static final int FRACTION6_CODE = 11;

	private int startCode;
	private int endCode;

	public ETimestamp() {
		this(DateTimeUtil.getNewCalendar(), YEAR_CODE, SECOND_CODE);
	}

	public ETimestamp(Calendar value, int startCode, int endCode) {
		super(value);
		this.startCode = startCode;
		this.endCode = endCode;
	}

	public static ETimestamp ezeBox(Calendar value, int startCode, int endCode) {
		Calendar clone = null;
		if (value != null) {
			clone = ezeClone(value, startCode, endCode);
		}
		return new ETimestamp(clone, startCode, endCode);
	}

	public static ETimestamp ezeBox(Calendar value) {
		return ezeBox(value, YEAR_CODE, FRACTION6_CODE);
	}

	public static Object ezeCast(Object value, Object[] constraints) throws AnyException {
		Integer[] args = new Integer[constraints.length];
		java.lang.System.arraycopy(constraints, 0, args, 0, args.length);
		return ezeCast(value, args);
	}

	public static Calendar ezeCast(Object value, Integer... args) throws AnyException {
		return (Calendar) EAny.ezeCast(value, "asTimestamp", ETimestamp.class, new Class[] { Integer[].class }, args);
	}

	public static boolean ezeIsa(Object value, Integer... args) {
		boolean isa = value instanceof ETimestamp;
		if (isa) {
			if (args.length == 2)
				isa = ((ETimestamp) value).startCode == args[0] && ((ETimestamp) value).endCode == args[1];
		} else {
			isa = value instanceof Calendar;
		}
		return isa;
	}

	public String toString() {
		return EString.asString(object);
	}

	public static Calendar ezeClone(Calendar original, int startCode, int endCode) {
		if (original == null)
			return null;
		// save the original
		boolean yearSet = original.isSet(Calendar.YEAR);
		boolean monthSet = original.isSet(Calendar.MONTH);
		boolean dateSet = original.isSet(Calendar.DATE);
		boolean hourSet = original.isSet(Calendar.HOUR_OF_DAY);
		boolean minuteSet = original.isSet(Calendar.MINUTE);
		boolean secondSet = original.isSet(Calendar.SECOND);
		boolean milliSet = original.isSet(Calendar.MILLISECOND);
		boolean zoneSet = original.isSet(Calendar.ZONE_OFFSET);
		int yearValue = original.get(Calendar.YEAR);
		int monthValue = original.get(Calendar.MONTH);
		int dateValue = original.get(Calendar.DATE);
		int hourValue = original.get(Calendar.HOUR_OF_DAY);
		int minuteValue = original.get(Calendar.MINUTE);
		int secondValue = original.get(Calendar.SECOND);
		int milliValue = original.get(Calendar.MILLISECOND);
		int zoneValue = original.get(Calendar.ZONE_OFFSET);
		// create the cloned
		Calendar cloned = defaultValue();
		cloned.clear();
		if (startCode <= YEAR_CODE && endCode >= YEAR_CODE && yearSet)
			cloned.set(Calendar.YEAR, yearValue);
		if (startCode <= MONTH_CODE && endCode >= MONTH_CODE && monthSet)
			cloned.set(Calendar.MONTH, monthValue);
		if (startCode <= DAY_CODE && endCode >= DAY_CODE && dateSet)
			cloned.set(Calendar.DATE, dateValue);
		if (startCode <= HOUR_CODE && endCode >= HOUR_CODE && hourSet)
			cloned.set(Calendar.HOUR_OF_DAY, hourValue);
		if (startCode <= MINUTE_CODE && endCode >= MINUTE_CODE && minuteSet)
			cloned.set(Calendar.MINUTE, minuteValue);
		if (startCode <= SECOND_CODE && endCode >= SECOND_CODE && secondSet)
			cloned.set(Calendar.SECOND, secondValue);
		if (startCode <= FRACTION1_CODE && endCode >= FRACTION1_CODE && milliSet)
			cloned.set(Calendar.MILLISECOND, milliValue);
		// we intentionally don't set the zone, as this is the flag we use to indicate a date object
		// process the new object
		cloned.getTimeInMillis();
		// we need to restore the original, because the .get method clobbers the flags set in the calendar object
		original.clear();
		if (yearSet)
			original.set(Calendar.YEAR, yearValue);
		if (monthSet)
			original.set(Calendar.MONTH, monthValue);
		if (dateSet)
			original.set(Calendar.DATE, dateValue);
		if (hourSet)
			original.set(Calendar.HOUR_OF_DAY, hourValue);
		if (minuteSet)
			original.set(Calendar.MINUTE, minuteValue);
		if (secondSet)
			original.set(Calendar.SECOND, secondValue);
		if (milliSet)
			original.set(Calendar.MILLISECOND, milliValue);
		// this flag is used by date objects only
		if (zoneSet)
			original.set(Calendar.ZONE_OFFSET, zoneValue);
		// process the original object
		original.getTimeInMillis();
		return cloned;
	}
	
	public static Calendar asTimestamp(EDate timestamp) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox());
	}

	public static Calendar asTimestamp(ETimestamp timestamp) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox());
	}

	public static Calendar asTimestamp(GregorianCalendar timestamp) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp((Calendar) timestamp);
	}

	public static Calendar asTimestamp(Calendar timestamp) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp, DefaultPattern);
	}

	public static Calendar asTimestamp(EDate timestamp, String timespanMask) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox(), timespanMask);
	}

	public static Calendar asTimestamp(ETimestamp timestamp, String timespanMask) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox(), timespanMask);
	}

	public static Calendar asTimestamp(GregorianCalendar timestamp, String timespanMask) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp((Calendar) timestamp, timespanMask);
	}

	public static Calendar asTimestamp(Calendar timestamp, String timespanMask) throws AnyException {
		if (timestamp == null)
			return null;
		if (timespanMask == null || timespanMask.length() == 0)
			timespanMask = DefaultPattern;
		// Default values in case the pattern doesn't specify things.
		int startCode = YEAR_CODE;
		int endCode = SECOND_CODE;
		TimestampIntervalMask mask = new TimestampIntervalMask(timespanMask);
		if (mask.getStartCode() != -1 && mask.getStartCode() <= mask.getEndCode()) {
			startCode = mask.getStartCode();
			endCode = mask.getEndCode();
		}
		return asTimestamp(timestamp, startCode, endCode);
	}

	public static Calendar asTimestamp(EString timestamp, Integer... args) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox(), args);
	}

	public static Calendar asTimestamp(String timestamp, Integer... args) throws AnyException {
		if (timestamp == null)
			return null;
		if (args != null && args.length == 2)
			return convert(timestamp, args[0], args[1]);
		else
			return asTimestamp(timestamp, DefaultPattern);
	}

	public static Calendar asTimestamp(EString timestamp, String timespanMask) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox(), timespanMask);
	}

	public static Calendar asTimestamp(String timestamp, String timespanMask) throws AnyException {
		if (timestamp == null || timespanMask == null)
			return null;
		// Default values in case the pattern doesn't specify things.
		int startCode = YEAR_CODE;
		int endCode = SECOND_CODE;
		TimestampIntervalMask mask = new TimestampIntervalMask(timespanMask);
		if (mask.getStartCode() != -1 && mask.getStartCode() <= mask.getEndCode()) {
			startCode = mask.getStartCode();
			endCode = mask.getEndCode();
		}
		return convert(timestamp, startCode, endCode);
	}

	public static Calendar asTimestamp(EDate timestamp, Integer... args) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox(), args);
	}

	public static Calendar asTimestamp(ETimestamp timestamp, Integer... args) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox(), args);
	}

	public static Calendar asTimestamp(GregorianCalendar timestamp, Integer... args) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp((Calendar) timestamp, args);
	}

	public static Calendar asTimestamp(Calendar date, Integer... args) {
		if (date == null)
			return null;
		if (args == null)
			return asTimestamp((Calendar) date, YEAR_CODE, SECOND_CODE);
		else
			return asTimestamp((Calendar) date, args[0], args[1]);
	}

	public static Calendar asTimestamp(EDate timestamp, int startCode, int endCode) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox(), startCode, endCode);
	}

	public static Calendar asTimestamp(ETimestamp timestamp, int startCode, int endCode) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox(), startCode, endCode);
	}

	public static Calendar asTimestamp(GregorianCalendar timestamp, int startCode, int endCode) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp((Calendar) timestamp, startCode, endCode);
	}

	public static Calendar asTimestamp(Calendar original, int startCode, int endCode) {
		if (original == null)
			return null;
		Calendar date = ezeClone(original, YEAR_CODE, FRACTION1_CODE);
		Calendar result = DateTimeUtil.getBaseCalendar();
		// Get values for the full set of fields. Fields that we need will be
		// set from the calendar. The others will be set to reasonable defaults.
		Calendar forDefaults = null;
		if (startCode <= YEAR_CODE && endCode >= YEAR_CODE) {
			if (date.isSet(Calendar.YEAR))
				result.set(Calendar.YEAR, date.get(Calendar.YEAR));
			else {
				if (forDefaults == null)
					forDefaults = DateTimeUtil.getNewCalendar();
				result.set(Calendar.YEAR, forDefaults.get(Calendar.YEAR));
			}
		}
		if (startCode <= MONTH_CODE && endCode >= MONTH_CODE) {
			if (date.isSet(Calendar.MONTH))
				result.set(Calendar.MONTH, date.get(Calendar.MONTH));
			else {
				if (forDefaults == null)
					forDefaults = DateTimeUtil.getNewCalendar();
				result.set(Calendar.MONTH, forDefaults.get(Calendar.MONTH));
			}
		}
		if (startCode <= DAY_CODE && endCode >= DAY_CODE) {
			if (date.isSet(Calendar.DATE))
				result.set(Calendar.DATE, date.get(Calendar.DATE));
			else {
				if (forDefaults == null)
					forDefaults = DateTimeUtil.getNewCalendar();
				result.set(Calendar.DATE, forDefaults.get(Calendar.DATE));
			}
		}
		if (startCode <= HOUR_CODE && endCode >= HOUR_CODE) {
			if (date.isSet(Calendar.HOUR_OF_DAY))
				result.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
			else {
				if (forDefaults == null)
					forDefaults = DateTimeUtil.getNewCalendar();
				result.set(Calendar.HOUR_OF_DAY, forDefaults.get(Calendar.HOUR_OF_DAY));
			}
		}
		if (startCode <= MINUTE_CODE && endCode >= MINUTE_CODE) {
			if (date.isSet(Calendar.MINUTE))
				result.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
			else {
				if (forDefaults == null)
					forDefaults = DateTimeUtil.getNewCalendar();
				result.set(Calendar.MINUTE, forDefaults.get(Calendar.MINUTE));
			}
		}
		if (startCode <= SECOND_CODE && endCode >= SECOND_CODE) {
			if (date.isSet(Calendar.SECOND))
				result.set(Calendar.SECOND, date.get(Calendar.SECOND));
			else {
				if (forDefaults == null)
					forDefaults = DateTimeUtil.getNewCalendar();
				result.set(Calendar.SECOND, forDefaults.get(Calendar.SECOND));
			}
		}
		if (startCode <= FRACTION1_CODE && endCode >= FRACTION1_CODE) {
			if (date.isSet(Calendar.MILLISECOND))
				result.set(Calendar.MILLISECOND, date.get(Calendar.MILLISECOND));
			else {
				if (forDefaults == null)
					forDefaults = DateTimeUtil.getNewCalendar();
				result.set(Calendar.MILLISECOND, forDefaults.get(Calendar.MILLISECOND));
			}
		}
		return result;
	}

	/**
	 * Converts a formatted string into a Calendar that's suitable for assigning or comparing with this item. Strings are
	 * parsed using defaultTimeStampFormat if it's not blank, otherwise we use the pattern field of this object. If the
	 * string can't be parsed using a pattern then we do it by hand, pulling out digits and ignoring non-digit characters.
	 * <P> When we parse using defaultTimeStampFormat, only the fields from the format string are set. The rest default to
	 * zeros so you get Jan 1 1970, etc. When we parse by hand, unspecified fields are set to the current time. This behavior
	 * is OK for assignments, but it must be accounted for when comparing two timestamps.
	 */
	public static Calendar asTimestamp(EString timestamp, String format, int startCode, int endCode) throws AnyException {
		if (timestamp == null)
			return null;
		return asTimestamp(timestamp.ezeUnbox(), format, startCode, endCode);
	}

	public static Calendar asTimestamp(String timestamp, String format, int startCode, int endCode) throws AnyException {
		if (timestamp == null)
			return null;
		Calendar result;
		timestamp = timestamp.trim();
		try {
			result = convert(timestamp, format);
		}
		catch (ParseException pe) {
			// OK. Do it ourselves
			result = convert(timestamp, startCode, endCode);
		}
		return result;
	}

	public static int compareTo(Calendar op1, Calendar op2) throws AnyException {
		if (op1 == null && op2 == null)
			return 0;
		return op1.compareTo(op2);
	}

	public static boolean equals(Calendar op1, Calendar op2) {
		if (op1 == null && op2 == null)
			return true;
		if (op1 == null || op2 == null)
			return false;
		return op1.equals(op2);
	}

	public static boolean notEquals(Calendar op1, Calendar op2) {
		if (op1 == null && op2 == null)
			return false;
		if (op1 == null || op2 == null)
			return true;
		return !op1.equals(op2);
	}

	public static Calendar convert(String timestamp, int startCode, int endCode) {
		// Try to parse the string by hand, looking for each field (years, months,
		// etc.) that this ETimestamp stores.
		int years = -1;
		int months = -1;
		int days = -1;
		int hours = -1;
		int minutes = -1;
		int seconds = -1;
		int microseconds = -1;
		int length = timestamp.length();
		PARSE: if (length > 0) {
			// ch is the character we're currently looking at. i is the index of
			// the
			// next character after ch.
			char ch;
			int i = 0;
			// Locate the first digit.
			do {
				ch = timestamp.charAt(i);
				i++;
			}
			while (i < length && !('0' <= ch && ch <= '9'));
			// Read in the number of years.
			if (i <= length && startCode == YEAR_CODE) {
				years = 0;
				for (int j = 0; '0' <= ch && ch <= '9' && j < 4; j++) {
					years = years * 10 + ch - '0';
					if (i < length) {
						ch = timestamp.charAt(i);
						i++;
					} else {
						// make sure there were 4 digits (when we get here it points exactly)
						if (i != 4)
							years = -1;
						break PARSE;
					}
				}
				// make sure there were 4 digits (when we get here it points past)
				if (i != 5)
					years = -1;
			}
			// Skip ahead to the next digit.
			while (i < length && !('0' <= ch && ch <= '9')) {
				ch = timestamp.charAt(i);
				i++;
			}
			// Read in the number of months.
			if (i <= length && startCode <= MONTH_CODE && endCode >= MONTH_CODE) {
				months = ch - '0';
				if (i < length) {
					ch = timestamp.charAt(i);
					i++;
					if ('0' <= ch && ch <= '9') {
						months = months * 10 + ch - '0';
						if (i < length) {
							ch = timestamp.charAt(i);
							i++;
						} else {
							break PARSE;
						}
					}
				} else {
					break PARSE;
				}
			}
			// Skip ahead to the next digit.
			while (i < length && !('0' <= ch && ch <= '9')) {
				ch = timestamp.charAt(i);
				i++;
			}
			// Read in the number of days.
			if (i <= length && startCode <= DAY_CODE && endCode >= DAY_CODE) {
				days = ch - '0';
				if (i < length) {
					ch = timestamp.charAt(i);
					i++;
					if ('0' <= ch && ch <= '9') {
						days = days * 10 + ch - '0';
						if (i < length) {
							ch = timestamp.charAt(i);
							i++;
						} else {
							break PARSE;
						}
					}
				} else {
					break PARSE;
				}
			}
			// Skip ahead to the next digit.
			while (i < length && !('0' <= ch && ch <= '9')) {
				ch = timestamp.charAt(i);
				i++;
			}
			// Read in the number of hours.
			if (i <= length && startCode <= HOUR_CODE && endCode >= HOUR_CODE) {
				hours = ch - '0';
				if (i < length) {
					ch = timestamp.charAt(i);
					i++;
					if ('0' <= ch && ch <= '9') {
						hours = hours * 10 + ch - '0';
						if (i < length) {
							ch = timestamp.charAt(i);
							i++;
						} else {
							break PARSE;
						}
					}
				} else {
					break PARSE;
				}
			}
			// Skip ahead to the next digit.
			while (i < length && !('0' <= ch && ch <= '9')) {
				ch = timestamp.charAt(i);
				i++;
			}
			// Read in the number of minutes.
			if (i <= length && startCode <= MINUTE_CODE && endCode >= MINUTE_CODE) {
				minutes = ch - '0';
				if (i < length) {
					ch = timestamp.charAt(i);
					i++;
					if ('0' <= ch && ch <= '9') {
						minutes = minutes * 10 + ch - '0';
						if (i < length) {
							ch = timestamp.charAt(i);
							i++;
						} else {
							break PARSE;
						}
					}
				} else {
					break PARSE;
				}
			}
			// Skip ahead to the next digit.
			while (i < length && !('0' <= ch && ch <= '9')) {
				ch = timestamp.charAt(i);
				i++;
			}
			// Read in the number of seconds.
			if (i <= length && startCode <= SECOND_CODE && endCode >= SECOND_CODE) {
				seconds = ch - '0';
				if (i < length) {
					ch = timestamp.charAt(i);
					i++;
					if ('0' <= ch && ch <= '9') {
						seconds = seconds * 10 + ch - '0';
						if (i < length) {
							ch = timestamp.charAt(i);
							i++;
						} else {
							break PARSE;
						}
					}
				} else {
					break PARSE;
				}
			}
			// Skip ahead to the next digit.
			while (i < length && !('0' <= ch && ch <= '9')) {
				ch = timestamp.charAt(i);
				i++;
			}
			// Read in the number of microseconds.
			if (i <= length && endCode >= FRACTION1_CODE) {
				microseconds = 0;
				int microsecondsFound = 0;
				int fractionDigits = endCode - FRACTION1_CODE + 1;
				while (microsecondsFound < fractionDigits && '0' <= ch && ch <= '9') {
					microseconds *= 10;
					microseconds += ch - '0';
					microsecondsFound++;
					if (i < length) {
						ch = timestamp.charAt(i);
						i++;
					} else {
						break;
					}
				}
				if (microsecondsFound < 6) {
					// If we didn't read in the full number of fraction digits,
					// then
					// adjust what we saw. If we read "650" then microseconds
					// should
					// be 65000 not 650.
					for (int j = microsecondsFound; j < 6; j++) {
						microseconds *= 10;
					}
				}
			}
		}
		// Make sure all required fields were found.
		if ((years == -1 && startCode == YEAR_CODE) || (months == -1 && startCode <= MONTH_CODE && endCode >= MONTH_CODE)
			|| (days == -1 && startCode <= DAY_CODE && endCode >= DAY_CODE) || (hours == -1 && startCode <= HOUR_CODE && endCode >= HOUR_CODE)
			|| (minutes == -1 && startCode <= MINUTE_CODE && endCode >= MINUTE_CODE) || (seconds == -1 && startCode <= SECOND_CODE && endCode >= SECOND_CODE)
			|| (microseconds == -1 && endCode >= FRACTION1_CODE))
		{
			TypeCastException tcx = new TypeCastException();
			tcx.actualTypeName = "string";
			tcx.castToName = "timestamp";
			throw tcx.fillInMessage( Message.CONVERSION_ERROR, timestamp, tcx.actualTypeName, tcx.castToName );
		}
		// The last thing to do is put the values into a calendar and TimestampData.
		Calendar cal = DateTimeUtil.getBaseCalendar();
		if (years != -1)
			cal.set(Calendar.YEAR, years);
		if (months != -1)
			cal.set(Calendar.MONTH, months - 1);
		if (days != -1)
			cal.set(Calendar.DATE, days);
//		else if (months != -1)
//			// No day was specified, but a month was specified. Set the day to
//			// one. Without this we'd default to the current day, which might
//			// be invalid for the month that was specified. For example, this
//			// change was put in on May 30 after setting a timestamp to
//			// Feb 2007: Feb 30 is not a valid date.
//			cal.set(Calendar.DATE, 1);
		if (hours != -1)
			cal.set(Calendar.HOUR_OF_DAY, hours);
		if (minutes != -1)
			cal.set(Calendar.MINUTE, minutes);
		if (seconds != -1)
			cal.set(Calendar.SECOND, seconds);
		if (microseconds != -1)
			//Calendar only supports miliseconds, so drop the last 3 digits of precision from microseconds
			cal.set(Calendar.MILLISECOND, microseconds / 1000);
		// Verify that the values are valid. We only do this if year month and date are at least there
		try {
			if (years != -1 && months != -1 && days != -1)
				cal.getTimeInMillis();
		}
		catch (Exception ex) {
			TypeCastException tcx = new TypeCastException();
			tcx.actualTypeName = "string";
			tcx.castToName = "timestamp";
			tcx.initCause( ex );
			throw tcx.fillInMessage( Message.CONVERSION_ERROR, timestamp, tcx.actualTypeName, tcx.castToName );
		}
		return cal;
	}

	public static Calendar convert(String timestamp, String format) throws ParseException {
		synchronized (DateTimeUtil.LOCK) {
			JavartDateFormat formatter = DateTimeUtil.getDateFormat(format);
			formatter.setMicrosecond(0);
			Date date = formatter.parse(timestamp);
			Calendar cal = DateTimeUtil.getBaseCalendar();
			cal.setTime(date);
			return cal;
		}
	}

	/**
	 * Create a Timestamp pattern string based on a startcode and endcode value
	 */
	public static String createPattern(int startCode, int endCode) {
		if (startCode > endCode) {
			return null;
		}
		String[] code = { "yyyy", "MM", "dd", "HH", "mm", "ss", "SSSSSS" };
		String[] delimiters = { "-", "-", " ", ":", ":", "." };
		int sindex = (startCode > 6) ? 6 : startCode;
		int eindex = (endCode > 6) ? 6 : endCode;
		StringBuilder patternString = new StringBuilder(26);
		for (int i = sindex, j = 0; i <= eindex; i++, j++) {
			if (j > 0)
				patternString.append(delimiters[i - 1]);
			patternString.append(code[i]);
		}
		return patternString.toString();
	}

	public static Calendar defaultValue() {
		long now = java.lang.System.currentTimeMillis();
		Calendar cal = DateTimeUtil.getBaseCalendar();
		cal.setTimeInMillis(now);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		cal.clear();
		cal.set(year, month, date, hour, minute, second);
		return cal;
	}

	public static Calendar defaultValue(int startCode, int endCode) {
		long now = java.lang.System.currentTimeMillis();
		Calendar cal = DateTimeUtil.getBaseCalendar();
		cal.setTimeInMillis(now);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int date = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		int fraction = cal.get(Calendar.MILLISECOND);
		cal.clear();
		if (startCode <= YEAR_CODE && endCode >= YEAR_CODE)
			cal.set(Calendar.YEAR, year);
		if (startCode <= MONTH_CODE && endCode >= MONTH_CODE)
			cal.set(Calendar.MONTH, month);
		if (startCode <= DAY_CODE && endCode >= DAY_CODE)
			cal.set(Calendar.DATE, date);
		if (startCode <= HOUR_CODE && endCode >= HOUR_CODE)
			cal.set(Calendar.HOUR_OF_DAY, hour);
		if (startCode <= MINUTE_CODE && endCode >= MINUTE_CODE)
			cal.set(Calendar.MINUTE, minute);
		if (startCode <= SECOND_CODE && endCode >= SECOND_CODE)
			cal.set(Calendar.SECOND, second);
		if (startCode <= FRACTION1_CODE && endCode >= FRACTION1_CODE)
			cal.set(Calendar.MILLISECOND, fraction);
		return asTimestamp(cal, startCode, endCode);
	}

	/**
	 * Returns the day of a timestamp
	 */
	public static int dayOf(Calendar original) throws AnyException {
		Calendar aTimestamp = ezeClone(original, YEAR_CODE, FRACTION1_CODE);
		if (!aTimestamp.isSet(Calendar.DATE))
		{
			InvalidArgumentException ex = new InvalidArgumentException();
			throw ex.fillInMessage( Message.NO_FIELD_IN_TIMESTAMP, "dayOf", "dd" );
		}
		return aTimestamp.get(Calendar.DATE);
	}

	/**
	 * Returns the month of a timestamp
	 */
	public static int monthOf(Calendar original) throws AnyException {
		Calendar aTimestamp = ezeClone(original, YEAR_CODE, FRACTION1_CODE);
		if (!aTimestamp.isSet(Calendar.MONTH))
		{
			InvalidArgumentException ex = new InvalidArgumentException();
			throw ex.fillInMessage( Message.NO_FIELD_IN_TIMESTAMP, "monthOf", "MM" );
		}
		return aTimestamp.get(Calendar.MONTH) + 1;
	}

	/**
	 * Returns the year of a timestamp
	 */
	public static int yearOf(Calendar original) throws AnyException {
		Calendar aTimestamp = ezeClone(original, YEAR_CODE, FRACTION1_CODE);
		if (!aTimestamp.isSet(Calendar.YEAR))
		{
			InvalidArgumentException ex = new InvalidArgumentException();
			throw ex.fillInMessage( Message.NO_FIELD_IN_TIMESTAMP, "yearOf", "yyyy" );
		}
		return aTimestamp.get(Calendar.YEAR);
	}

	/**
	 * Returns the weekday of a timestamp
	 */
	public static int weekdayOf(Calendar original) throws AnyException {
		Calendar aTimestamp = ezeClone(original, YEAR_CODE, FRACTION1_CODE);
		if (!aTimestamp.isSet(DAY_CODE))
		{
			InvalidArgumentException ex = new InvalidArgumentException();
			throw ex.fillInMessage( Message.NO_FIELD_IN_TIMESTAMP, "weekdayOf", "dd" );
		}
		return aTimestamp.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * Returns the date of a timestamp
	 */
	public static Calendar dateOf(Calendar original) throws AnyException {
		Calendar aTimestamp = ezeClone(original, YEAR_CODE, FRACTION1_CODE);
		if (!aTimestamp.isSet(Calendar.YEAR) && !aTimestamp.isSet(Calendar.MONTH) && !aTimestamp.isSet(Calendar.DATE))
		{
			InvalidArgumentException ex = new InvalidArgumentException();
			throw ex.fillInMessage( Message.NO_FIELD_IN_TIMESTAMP, "dateOf", "yyyyMMdd" );
		}
		int start;
		if (aTimestamp.isSet(Calendar.YEAR))
			start = YEAR_CODE;
		else if (aTimestamp.isSet(Calendar.MONTH))
			start = MONTH_CODE;
		else
			start = DAY_CODE;
		int end;
		if (aTimestamp.isSet(Calendar.DATE))
			end = DAY_CODE;
		else if (aTimestamp.isSet(Calendar.MONTH))
			end = MONTH_CODE;
		else
			end = YEAR_CODE;
		return new ETimestamp(ezeClone(aTimestamp, start, end), start, end).ezeUnbox();
	}

	/**
	 * Returns the time of a timestamp
	 */
	public static Calendar timeOf(Calendar original) throws AnyException {
		Calendar aTimestamp = ezeClone(original, YEAR_CODE, FRACTION1_CODE);
		if (!aTimestamp.isSet(Calendar.HOUR_OF_DAY) && !aTimestamp.isSet(Calendar.MINUTE) && !aTimestamp.isSet(Calendar.SECOND)) {
			InvalidArgumentException ex = new InvalidArgumentException();
			throw ex.fillInMessage( Message.NO_FIELD_IN_TIMESTAMP, "timeOf", "HHmmss" );
		}
		int start;
		if (aTimestamp.isSet(Calendar.HOUR_OF_DAY))
			start = HOUR_CODE;
		else if (aTimestamp.isSet(Calendar.MINUTE))
			start = MINUTE_CODE;
		else
			start = SECOND_CODE;
		int end;
		if (aTimestamp.isSet(Calendar.SECOND))
			end = SECOND_CODE;
		else if (aTimestamp.isSet(Calendar.MINUTE))
			end = MINUTE_CODE;
		else
			end = HOUR_CODE;
		return new ETimestamp(ezeClone(aTimestamp, start, end), start, end).ezeUnbox();
	}

	/**
	 * Returns the extension of a timestamp
	 */
	public static Calendar extend(Calendar aTimestamp, String timeSpanPattern) throws AnyException {
		// Default values in case the pattern doesn't specify things.
		int startCode = YEAR_CODE;
		int endCode = SECOND_CODE;
		TimestampIntervalMask mask = new TimestampIntervalMask(timeSpanPattern);
		if (mask.getStartCode() != -1 && mask.getStartCode() <= mask.getEndCode()) {
			startCode = mask.getStartCode();
			endCode = mask.getEndCode();
		}
		return new ETimestamp(ezeClone(aTimestamp, startCode, endCode), startCode, endCode).ezeUnbox();
	}
}
