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
import org.eclipse.edt.javart.resources.*;
import org.eclipse.edt.javart.*;
import org.eclipse.edt.runtime.java.eglx.lang.EDate;
import java.util.Calendar;
import eglx.lang.MathLib;
import org.eclipse.edt.eunit.runtime.Status;
import org.eclipse.edt.runtime.java.eglx.lang.EInt;
import java.lang.Integer;
import org.eclipse.edt.runtime.java.eglx.lang.EFloat;
import java.lang.Double;
import org.eclipse.edt.eunit.runtime.ConstantsLib;
import org.eclipse.edt.runtime.java.eglx.lang.EBoolean;
import java.lang.Boolean;
import org.eclipse.edt.runtime.java.eglx.lang.EAny;
import org.eclipse.edt.runtime.java.eglx.lang.ETimestamp;
import org.eclipse.edt.eunit.runtime.AssertionFailedException;
import org.eclipse.edt.runtime.java.eglx.lang.EList;
import java.util.List;
import org.eclipse.edt.runtime.java.eglx.lang.EString;
import java.lang.String;
import org.eclipse.edt.runtime.java.eglx.lang.EDecimal;
import java.math.BigDecimal;
import org.eclipse.edt.eunit.runtime.Log;
import org.eclipse.edt.runtime.java.eglx.lang.EBigint;
import java.lang.Long;
@SuppressWarnings("unused")
@javax.xml.bind.annotation.XmlRootElement(name="LogResult")
public class LogResult extends ExecutableBase {
	private static final long serialVersionUID = 10L;
	@javax.xml.bind.annotation.XmlTransient
	private Log outR;
	@javax.xml.bind.annotation.XmlTransient
	private Status s;
	private static final String ezeConst_ACTUALHEADER = "Actual value = ";
	public final String ACTUALHEADER = ezeConst_ACTUALHEADER;
	private static final String ezeConst_EXPECTEDHEADER = "Expected value = ";
	public final String EXPECTEDHEADER = ezeConst_EXPECTEDHEADER;
	private static final String ezeConst_ACTUALSIZEHEADER = "Actual array size = ";
	public final String ACTUALSIZEHEADER = ezeConst_ACTUALSIZEHEADER;
	private static final String ezeConst_EXPECTEDSIZEHEADER = "Exepcted array size = ";
	public final String EXPECTEDSIZEHEADER = ezeConst_EXPECTEDSIZEHEADER;
	public ConstantsLib eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib;
	public LogResult() {
		super();
		ezeInitialize();
	}
	public void ezeInitialize() {
		outR = new Log();
		s = new Status();
	}
	@org.eclipse.edt.javart.json.Json(name="outR", clazz=Log.class, asOptions={})
	public Log getOutR() {
		Log eze$Temp1 = null;
		return (org.eclipse.edt.runtime.java.eglx.lang.AnyValue.ezeCopyTo(outR, eze$Temp1));
	}
	public void setOutR(Log ezeValue) {
		org.eclipse.edt.runtime.java.eglx.lang.AnyValue.ezeCopyTo(ezeValue, outR);
	}
	@org.eclipse.edt.javart.json.Json(name="s", clazz=Status.class, asOptions={})
	public Status getS() {
		Status eze$Temp2 = null;
		return (org.eclipse.edt.runtime.java.eglx.lang.AnyValue.ezeCopyTo(s, eze$Temp2));
	}
	public void setS(Status ezeValue) {
		org.eclipse.edt.runtime.java.eglx.lang.AnyValue.ezeCopyTo(ezeValue, s);
	}
	@org.eclipse.edt.javart.json.Json(name="ACTUALHEADER", clazz=EString.class, asOptions={})
	public String getACTUALHEADER() {
		return ACTUALHEADER;
	}
	@org.eclipse.edt.javart.json.Json(name="EXPECTEDHEADER", clazz=EString.class, asOptions={})
	public String getEXPECTEDHEADER() {
		return EXPECTEDHEADER;
	}
	@org.eclipse.edt.javart.json.Json(name="ACTUALSIZEHEADER", clazz=EString.class, asOptions={})
	public String getACTUALSIZEHEADER() {
		return ACTUALSIZEHEADER;
	}
	@org.eclipse.edt.javart.json.Json(name="EXPECTEDSIZEHEADER", clazz=EString.class, asOptions={})
	public String getEXPECTEDSIZEHEADER() {
		return EXPECTEDSIZEHEADER;
	}
	public ConstantsLib eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib() {
		if (eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib == null) {
			eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib = (ConstantsLib)org.eclipse.edt.javart.Runtime.getRunUnit().loadLibrary("org.eclipse.edt.eunit.runtime.ConstantsLib");
		}
		return eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib;
	}
	public void clearResults() {
		outR.msg = "";
		s.code = (int)(short)((short) -1);
		s.reason = "";
	}
	public Status getStatus() {
		Status eze$Temp3 = null;
		return (org.eclipse.edt.runtime.java.eglx.lang.AnyValue.ezeCopyTo(s, eze$Temp3));
	}
	public Log getLog() {
		Log eze$Temp4 = null;
		return (org.eclipse.edt.runtime.java.eglx.lang.AnyValue.ezeCopyTo(outR, eze$Temp4));
	}
	public void logStdOut(String logmsg) {
		outR.msg += eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().NEWLINE;
		outR.msg += logmsg;
	}
	public void passed(String str) {
		s.code = eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().SPASSED;
		if (((org.eclipse.edt.runtime.java.eglx.lang.NullType.equals(str, null)) || ((str).equals("")))) {
			str = "OK";
		}
		s.reason = str;
	}
	public void failed(String str) {
		s.code = eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().SFAILED;
		str = (("FAILED - ") + str);
		s.reason = str;
	}
	public void error(String str) {
		s.code = eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().SERROR;
		str = (("ERROR - ") + str);
		s.reason = str;
	}
	public void skipped(String str) {
		s.code = eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().SNOT_RUN;
		str = (("SKIPPED - ") + str);
		s.reason = str;
	}
	private void assertTrueException(String failedReason, Boolean testCondition, Boolean throwsFailException) {
		if (testCondition) {
			passed("OK");
		}
		else {
			failed(failedReason);
			if (throwsFailException) {
				AssertionFailedException eze$Temp8 = null;
				{
					AssertionFailedException eze$SettingTarget1;
					eze$SettingTarget1 = new AssertionFailedException();
					eze$SettingTarget1.setMessage(s.reason);
					eze$Temp8 = eze$SettingTarget1;
				}
				throw eze$Temp8;
			}
		}
	}
	public void assertTrue(String failedReason, Boolean testCondition) {
		assertTrueException(failedReason, testCondition, true);
	}
	public void assertTrue1(Boolean testCondition) {
		assertTrue("", testCondition);
	}
	public void assertBigIntEqual(String message, Long expected, Long actual) {
		boolean isEqual;
		isEqual = (expected == actual);
		expectAssertTrue(message, EBigint.ezeBox(expected), EBigint.ezeBox(actual), isEqual);
	}
	public void assertBigIntEqual1(Long expected, Long actual) {
		assertBigIntEqual("", expected, actual);
	}
	public void assertStringEqual(String message, String expected, String actual) {
		boolean isEqual;
		isEqual = ((expected).equals(actual));
		expectAssertTrue(message, EString.ezeBox(expected), EString.ezeBox(actual), isEqual);
	}
	public void assertStringEqual1(String expected, String actual) {
		assertStringEqual("", expected, actual);
	}
	public void assertStringArrayEqual(String message, List<String> expected, List<String> actual) {
		boolean isArrayEqual;
		isArrayEqual = true;
		int expectedSize;
		expectedSize = EList.getSize(expected);
		int actualSize;
		actualSize = EList.getSize(actual);
		String failedReason = "";
		if ((expectedSize == actualSize)) {
			String failedHeader;
			failedHeader = "Array element No.[";
			String expectedValues;
			expectedValues = ((EXPECTEDHEADER) + "[");
			String actualValues;
			actualValues = ((ACTUALHEADER) + "[");
			{
				int i = 0;
				for (i = (int)(short)((short) 1); (i <= expectedSize); i = (i + (int)(short)((short) 1))) {
					if ((!(expected.get(org.eclipse.edt.javart.util.JavartUtil.checkIndex(i - 1, expected))).equals(actual.get(org.eclipse.edt.javart.util.JavartUtil.checkIndex(i - 1, actual))))) {
						if (!(isArrayEqual)) {
							failedHeader += ", ";
						}
						isArrayEqual = false;
						failedHeader += EString.asString(i);
					}
					expectedValues += expected.get(org.eclipse.edt.javart.util.JavartUtil.checkIndex(i - 1, expected));
					actualValues += actual.get(org.eclipse.edt.javart.util.JavartUtil.checkIndex(i - 1, actual));
					if ((i != expectedSize)) {
						expectedValues += ", ";
						actualValues += ", ";
					}
				}
			}
			failedHeader += "] differs; ";
			expectedValues += "]; ";
			actualValues += "] ";
			failedReason = ((((failedHeader) + expectedValues)) + actualValues);
		}
		else {
			isArrayEqual = false;
			failedReason = (((((((((((((((("Failed: ") + EXPECTEDSIZEHEADER)) + "'")) + EString.asString(expectedSize))) + "' ")) + ACTUALSIZEHEADER)) + "'")) + EString.asString(actualSize))) + "' ");
		}
		if (((org.eclipse.edt.runtime.java.eglx.lang.NullType.notEquals(message, null)) && (!(message).equals("")))) {
			;
			failedReason = ((((message) + " - ")) + failedReason);
		}
		assertTrue(failedReason, isArrayEqual);
	}
	public void assertStringArrayEqual1(List<String> expected, List<String> actual) {
		assertStringArrayEqual("", expected, actual);
	}
	public void assertDateEqual(String message, Calendar expected, Calendar actual) {
		boolean isEqual;
		isEqual = (EDate.equals(expected, actual));
		expectAssertTrue(message, EDate.ezeBox(expected), EDate.ezeBox(actual), isEqual);
	}
	public void assertDateEqual1(Calendar expected, Calendar actual) {
		assertDateEqual("", expected, actual);
	}
	public void assertTimestampEqual(String message, Calendar expected, Calendar actual) {
		boolean isEqual;
		isEqual = (ETimestamp.equals(expected, actual));
		expectAssertTrue(message, ETimestamp.ezeBox(expected), ETimestamp.ezeBox(actual), isEqual);
	}
	public void assertTimestampEqual1(Calendar expected, Calendar actual) {
		assertTimestampEqual("", expected, actual);
	}
	public void assertDecimalEqual(String message, BigDecimal expected, BigDecimal actual) {
		boolean isEqual;
		isEqual = (EDecimal.equals(expected, actual));
		expectAssertTrue(message, EDecimal.ezeBox(expected), EDecimal.ezeBox(actual), isEqual);
	}
	public void assertDecimalEqual1(BigDecimal expected, BigDecimal actual) {
		assertDecimalEqual("", expected, actual);
	}
	public void assertFloatEqual(String message, Double expected, Double actual) {
		boolean isEqual;
		isEqual = isFloatEqual(expected, actual);
		expectAssertTrue(message, EFloat.ezeBox(expected), EFloat.ezeBox(actual), isEqual);
	}
	public void assertFloatEqual1(Double expected, Double actual) {
		assertFloatEqual("", expected, actual);
	}
	private boolean isFloatEqual(Double expected, Double actual) {
		double normalExpected = 0.0;
		double normalActual = 0.0;
		double delta = 0.0;
		int mantissaExpected = 0;
		int mantissaActual = 0;
		String signExpected = "";
		String signActual = "";
		double deltaLimit;
		deltaLimit = 1E-14;
		int eze$Temp15 = 0;
		AnyBoxedObject<Integer> eze$Temp16;
		eze$Temp16 = org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeWrap(eze$Temp15);
		String eze$Temp17 = "";
		AnyBoxedObject<String> eze$Temp18;
		eze$Temp18 = org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeWrap(eze$Temp17);
		normalExpected = normalFloat(expected, eze$Temp16, eze$Temp18);
		mantissaExpected = org.eclipse.edt.javart.util.JavartUtil.checkNullable(eze$Temp16.ezeUnbox());
		signExpected = org.eclipse.edt.javart.util.JavartUtil.checkNullable(eze$Temp18.ezeUnbox());
		int eze$Temp19 = 0;
		AnyBoxedObject<Integer> eze$Temp20;
		eze$Temp20 = org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeWrap(eze$Temp19);
		String eze$Temp21 = "";
		AnyBoxedObject<String> eze$Temp22;
		eze$Temp22 = org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeWrap(eze$Temp21);
		normalActual = normalFloat(actual, eze$Temp20, eze$Temp22);
		mantissaActual = org.eclipse.edt.javart.util.JavartUtil.checkNullable(eze$Temp20.ezeUnbox());
		signActual = org.eclipse.edt.javart.util.JavartUtil.checkNullable(eze$Temp22.ezeUnbox());
		delta = (normalExpected - normalActual);
		delta = MathLib.abs(delta);
		boolean isEqual;
		isEqual = ((((signExpected).equals(signActual)) && (mantissaExpected == mantissaActual)) && (delta < deltaLimit));
		return isEqual;
	}
	private void expectAssertTrue(String message, eglx.lang.EAny expected, eglx.lang.EAny actual, Boolean isEqual) {
		String failedReason;
		failedReason = buildFailedReason(message, expected, actual);
		assertTrue(failedReason, isEqual);
	}
	private String buildFailedReason(String message, eglx.lang.EAny expected, eglx.lang.EAny actual) {
		String failedReason;
		failedReason = expect(expected, actual);
		if (((org.eclipse.edt.runtime.java.eglx.lang.NullType.notEquals(message, null)) && (!(message).equals("")))) {
			failedReason = ((((message) + " - ")) + failedReason);
		}
		return failedReason;
	}
	private String expect(eglx.lang.EAny expected, eglx.lang.EAny actual) {
		String standardMsg;
		standardMsg = (((((((((((((((("Failed: ") + EXPECTEDHEADER)) + "'")) + EString.ezeCast(expected))) + "' ")) + ACTUALHEADER)) + "'")) + EString.ezeCast(actual))) + "' ");
		return standardMsg;
	}
	private double normalFloat(Double afloat, AnyBoxedObject<Integer> mantissa, AnyBoxedObject<String> sign) {
		mantissa.ezeCopy((int)(short)((short) 0));
		if ((afloat >= (double)(short)((short) 0))) {
			sign.ezeCopy("+");
		}
		else {
			sign.ezeCopy("-");
			afloat = (afloat * (double)(short)((short) -1));
		}
		if ((afloat != (double)(short)((short) 0))) {
			while ((afloat < (double)(short)((short) 1))) {
				afloat = (afloat * (double)(short)((short) 10));
				mantissa.ezeCopy((mantissa.ezeUnbox() - (int)(short)((short) 1)));
			}
			while ((afloat >= (double)(short)((short) 10))) {
				afloat = (EFloat.divide(afloat, (double)(short)((short) 10)));
				mantissa.ezeCopy((mantissa.ezeUnbox() + (int)(short)((short) 1)));
			}
		}
		return afloat;
	}
}
