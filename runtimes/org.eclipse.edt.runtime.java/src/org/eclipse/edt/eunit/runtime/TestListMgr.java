package org.eclipse.edt.eunit.runtime;
import org.eclipse.edt.javart.resources.*;
import org.eclipse.edt.javart.*;
import org.eclipse.edt.runtime.java.eglx.lang.EInt;
import java.lang.Integer;
import eglx.lang.AnyException;
import org.eclipse.edt.eunit.runtime.ServiceBindingType;
import org.eclipse.edt.eunit.runtime.LogResult;
import eglx.services.ServiceInvocationException;
import org.eclipse.edt.runtime.java.eglx.lang.EString;
import java.lang.String;
import org.eclipse.edt.runtime.java.eglx.lang.EList;
import java.util.List;
import org.eclipse.edt.eunit.runtime.AssertionFailedException;
import eglx.lang.SysLib;
import org.eclipse.edt.eunit.runtime.MultiStatus;
import org.eclipse.edt.eunit.runtime.ConstantsLib;
import org.eclipse.edt.runtime.java.eglx.lang.EAny;
@SuppressWarnings("unused")
@javax.xml.bind.annotation.XmlRootElement(name="TestListMgr")
public class TestListMgr extends ExecutableBase {
	private static final long serialVersionUID = 10L;
	@javax.xml.bind.annotation.XmlTransient
	public ServiceBindingType bindingType;
	@javax.xml.bind.annotation.XmlTransient
	public MultiStatus ms;
	@javax.xml.bind.annotation.XmlTransient
	public int testIndex;
	@javax.xml.bind.annotation.XmlTransient
	public List<String> testMethodNames;
	@javax.xml.bind.annotation.XmlTransient
	public List<org.eclipse.edt.javart.Delegate> runTestMtds;
	@javax.xml.bind.annotation.XmlTransient
	public String testLibName;
	@javax.xml.bind.annotation.XmlTransient
	public List<org.eclipse.edt.javart.Delegate> LibraryStartTests;
	@javax.xml.bind.annotation.XmlTransient
	private int libIndex;
	public LogResult eze_Lib_org_eclipse_edt_eunit_runtime_LogResult;
	public ConstantsLib eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib;
	public TestListMgr() {
		super();
		ezeInitialize();
	}
	public void ezeInitialize() {
		ms = new MultiStatus();
		testMethodNames = EList.ezeNew(String.class);
		runTestMtds = EList.ezeNew(org.eclipse.edt.javart.Delegate.class);
		testLibName = "";
		LibraryStartTests = EList.ezeNew(org.eclipse.edt.javart.Delegate.class);
		bindingType = ServiceBindingType.DEDICATED;
		testIndex = (int)(short)((short) 1);
		libIndex = (int)(short)((short) 1);
	}
	@org.eclipse.edt.javart.json.Json(name="bindingType", clazz=ServiceBindingType.class, asOptions={})
	public ServiceBindingType getBindingType() {
		return bindingType;
	}
	public void setBindingType(ServiceBindingType ezeValue) {
		bindingType = ezeValue;
	}
	@org.eclipse.edt.javart.json.Json(name="ms", clazz=MultiStatus.class, asOptions={})
	public MultiStatus getMs() {
		return ms;
	}
	public void setMs(MultiStatus ezeValue) {
		ms = ezeValue;
	}
	@org.eclipse.edt.javart.json.Json(name="testIndex", clazz=EInt.class, asOptions={})
	public int getTestIndex() {
		return testIndex;
	}
	public void setTestIndex(Integer ezeValue) {
		testIndex = ezeValue;
	}
	@org.eclipse.edt.javart.json.Json(name="testMethodNames", clazz=EString.class, asOptions={})
	public List<String> getTestMethodNames() {
		return testMethodNames;
	}
	public void setTestMethodNames(List<String> ezeValue) {
		testMethodNames = ezeValue;
	}
	@org.eclipse.edt.javart.json.Json(name="runTestMtds", clazz=org.eclipse.edt.javart.Delegate.class, asOptions={})
	public List<org.eclipse.edt.javart.Delegate> getRunTestMtds() {
		return runTestMtds;
	}
	public void setRunTestMtds(List<org.eclipse.edt.javart.Delegate> ezeValue) {
		runTestMtds = ezeValue;
	}
	@org.eclipse.edt.javart.json.Json(name="testLibName", clazz=EString.class, asOptions={})
	public String getTestLibName() {
		return testLibName;
	}
	public void setTestLibName(String ezeValue) {
		testLibName = ezeValue;
	}
	@org.eclipse.edt.javart.json.Json(name="LibraryStartTests", clazz=org.eclipse.edt.javart.Delegate.class, asOptions={})
	public List<org.eclipse.edt.javart.Delegate> getLibraryStartTests() {
		return LibraryStartTests;
	}
	public void setLibraryStartTests(List<org.eclipse.edt.javart.Delegate> ezeValue) {
		LibraryStartTests = ezeValue;
	}
	@org.eclipse.edt.javart.json.Json(name="libIndex", clazz=EInt.class, asOptions={})
	public int getLibIndex() {
		return libIndex;
	}
	public void setLibIndex(Integer ezeValue) {
		libIndex = ezeValue;
	}
	public LogResult eze_Lib_org_eclipse_edt_eunit_runtime_LogResult() {
		if (eze_Lib_org_eclipse_edt_eunit_runtime_LogResult == null) {
			eze_Lib_org_eclipse_edt_eunit_runtime_LogResult = (LogResult)org.eclipse.edt.javart.Runtime.getRunUnit().loadLibrary("org.eclipse.edt.eunit.runtime.LogResult");
		}
		return eze_Lib_org_eclipse_edt_eunit_runtime_LogResult;
	}
	public ConstantsLib eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib() {
		if (eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib == null) {
			eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib = (ConstantsLib)org.eclipse.edt.javart.Runtime.getRunUnit().loadLibrary("org.eclipse.edt.eunit.runtime.ConstantsLib");
		}
		return eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib;
	}
	public void nextTest() {
		String testId;
		testId = getTestIdString();
		SysLib.writeStdout((("Running test: ") + testId));
		ms.addStatus(testId);
		boolean eze$Temp1;
		eze$Temp1 = (testIndex < EList.getSize(runTestMtds));
		if (eze$Temp1) {
			testIndex += (int)(short)((short) 1);
			runTestMtds.get(org.eclipse.edt.javart.util.JavartUtil.checkIndex(testIndex - 1, runTestMtds)).invoke();
		}
	}
	private String getTestIdString() {
		int testMethodNamesSize;
		testMethodNamesSize = EList.getSize(testMethodNames);
		String testId;
		testId = ((testLibName) + "::");
		if ((testIndex <= testMethodNamesSize)) {
			testId += testMethodNames.get(org.eclipse.edt.javart.util.JavartUtil.checkIndex(testIndex - 1, testMethodNames));
		}
		else {
			if ((EInt.equals(testIndex, (testMethodNamesSize + (int)(short)((short) 1))))) {
				testId += "endTest";
			}
			else {
				testId += "INVALIDINDEXFOUND!!!";
			}
		}
		return testId;
	}
	public void nextTestLibrary() {
		boolean eze$Temp5;
		eze$Temp5 = (libIndex < EList.getSize(LibraryStartTests));
		if (eze$Temp5) {
			libIndex += (int)(short)((short) 1);
			LibraryStartTests.get(org.eclipse.edt.javart.util.JavartUtil.checkIndex(libIndex - 1, LibraryStartTests)).invoke();
		}
	}
	public void handleCallBackException(AnyException exp, eglx.http.IHttp http) {
		String str;
		str = (((((("Caught service exception: ") + exp.getMessageID())) + ": ")) + exp.getMessage());
		if (org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeIsa(exp, ServiceInvocationException.class)) {
			ServiceInvocationException sexp;
			sexp = org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeCast(exp, ServiceInvocationException.class);
			String s1;
			s1 = (("detail1:") + sexp.detail1);
			String s2;
			s2 = (("detail2:") + sexp.detail2);
			String s3;
			s3 = (("detail3:") + sexp.detail3);
			str = ((str) + eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().NEWLINE);
			str = ((((str) + s1)) + eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().NEWLINE);
			str = ((((str) + s2)) + eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().NEWLINE);
			str = ((((str) + s3)) + eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().NEWLINE);
			str = (((EString.plus(((str) + "Original request body: "), http.getRequest().body))) + eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().NEWLINE);
			str = (((EString.plus(((str) + "Raw response body: "), http.getResponse().body))) + eze_Lib_org_eclipse_edt_eunit_runtime_ConstantsLib().NEWLINE);
		}
		eze_Lib_org_eclipse_edt_eunit_runtime_LogResult().error(str);
		String testId;
		testId = this.testMethodNames.get(org.eclipse.edt.javart.util.JavartUtil.checkIndex(this.testIndex - 1, this.testMethodNames));
		nextTest();
	}
	public void caughtFailedAssertion(AssertionFailedException exp) {
		SysLib.writeStdout((("AssertionFail - ") + exp.getMessage()));
	}
	public void caughtAnyException(AnyException exp) {
		String expMsg;
		expMsg = (("uncaught exception for: ") + getTestIdString());
		eze_Lib_org_eclipse_edt_eunit_runtime_LogResult().error(expMsg);
	}
	public String getBindingTypeString(ServiceBindingType bType) {
		{
			EzeLabel_eze_CaseLabel_0: if ((org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeCast(bType, java.lang.Enum.class) == org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeCast(ServiceBindingType.DEDICATED, java.lang.Enum.class))) {
				return "DEDICATED_BINDING";
			}
			else {
				if ((org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeCast(bType, java.lang.Enum.class) == org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeCast(ServiceBindingType.DEVELOP, java.lang.Enum.class))) {
					return "DEVELOP_BINDING";
				}
				else {
					if ((org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeCast(bType, java.lang.Enum.class) == org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeCast(ServiceBindingType.DEPLOYED, java.lang.Enum.class))) {
						return "DEPLOYED_BINDING";
					}
					else {
						return "UNKNOWN Binding Type - NOT supported";
					}
				}
			}
		}
	}
}
