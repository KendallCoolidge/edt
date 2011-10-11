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
import org.eclipse.edt.runtime.java.eglx.lang.EInt;
import java.lang.Integer;
import org.eclipse.edt.eunit.runtime.ConstantsLib;
import org.eclipse.edt.eunit.runtime.LogResult;
import org.eclipse.edt.runtime.java.eglx.lang.EString;
import java.lang.String;
import org.eclipse.edt.eunit.runtime.Status;
import eglx.rbd.StrLib;
import org.eclipse.edt.runtime.java.eglx.lang.EAny;
@javax.xml.bind.annotation.XmlRootElement(name="MultiStatus")
public class MultiStatus extends ExecutableBase {
	private static final long serialVersionUID = 10L;
	@org.eclipse.edt.javart.json.Json(name="testCnt", clazz=EInt.class, asOptions={})
	public int testCnt;
	@org.eclipse.edt.javart.json.Json(name="expectedCnt", clazz=EInt.class, asOptions={})
	public int expectedCnt;
	@org.eclipse.edt.javart.json.Json(name="passedCnt", clazz=EInt.class, asOptions={})
	public int passedCnt;
	@org.eclipse.edt.javart.json.Json(name="failedCnt", clazz=EInt.class, asOptions={})
	public int failedCnt;
	@org.eclipse.edt.javart.json.Json(name="errCnt", clazz=EInt.class, asOptions={})
	public int errCnt;
	@org.eclipse.edt.javart.json.Json(name="badCnt", clazz=EInt.class, asOptions={})
	public int badCnt;
	@org.eclipse.edt.javart.json.Json(name="notRunCnt", clazz=EInt.class, asOptions={})
	public int notRunCnt;
	@org.eclipse.edt.javart.json.Json(name="firstFailedTestName", clazz=EString.class, asOptions={})
	public String firstFailedTestName;
	public ConstantsLib eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib;
	public LogResult eze_Lib_org_eclipse_edt_eunit_runtime_LogResult;
	public StrLib eze_Lib_eglx_rbd_StrLib;
	
	public MultiStatus() {
		super();
		ezeInitialize();
	}
	public void ezeInitialize() {
		testCnt = 0;
		expectedCnt = 0;
		passedCnt = 0;
		failedCnt = 0;
		errCnt = 0;
		badCnt = 0;
		notRunCnt = 0;
		firstFailedTestName = "";
	}
	public ConstantsLib eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib() {
		if (eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib == null) {
			eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib = (ConstantsLib)org.eclipse.edt.javart.Runtime.getRunUnit().loadLibrary("org.eclipse.edt.eunit.runtime.ConstantsLib");
		}
		return eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib;
	}
	public LogResult eze_Lib_org_eclipse_edt_eunit_runtime_LogResult() {
		if (eze_Lib_org_eclipse_edt_eunit_runtime_LogResult == null) {
			eze_Lib_org_eclipse_edt_eunit_runtime_LogResult = (LogResult)org.eclipse.edt.javart.Runtime.getRunUnit().loadLibrary("org.eclipse.edt.eunit.runtime.LogResult");
		}
		return eze_Lib_org_eclipse_edt_eunit_runtime_LogResult;
	}
	public StrLib eze_Lib_eglx_rbd_StrLib() {
		if (eze_Lib_eglx_rbd_StrLib == null) {
			eze_Lib_eglx_rbd_StrLib = (StrLib)org.eclipse.edt.javart.Runtime.getRunUnit().loadLibrary("eglx.rbd.StrLib");
		}
		return eze_Lib_eglx_rbd_StrLib;
	}
	public void addStatus(String testId) {
		Status s = null;
		s = org.eclipse.edt.runtime.java.eglx.lang.AnyValue.ezeCopyTo(eze_Lib_org_eclipse_edt_eunit_runtime_LogResult().getStatus(), s);
		String msg;
		msg = ((((testId) + ": ")) + s.reason);
		eze_Lib_org_eclipse_edt_eunit_runtime_LogResult().logStdOut(msg);
		testCnt += (int)((short) 1);
		if ((s.code == eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().SPASSED)) {
			passedCnt += (int)((short) 1);
		}
		else {
			if ((s.code == eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().SFAILED)) {
				failedCnt += (int)((short) 1);
			}
			else {
				if ((s.code == eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().SERROR)) {
					errCnt += (int)((short) 1);
				}
				else {
					if ((s.code == eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().SNOT_RUN)) {
						notRunCnt += (int)((short) 1);
					}
					else {
						badCnt += (int)((short) 1);
					}
				}
			}
		}
		boolean eze$Temp5;
		eze$Temp5 = ((s.code != eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().SPASSED) && (eze_Lib_eglx_rbd_StrLib().characterLen(firstFailedTestName) == (int)((short) 0)));
		if (eze$Temp5) {
			firstFailedTestName = testId;
		}
	}
}
