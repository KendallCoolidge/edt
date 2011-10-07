package org.eclipse.edt.gen.eunit;

import java.util.List;

import org.eclipse.edt.compiler.internal.interfaces.IGenerationMessageRequestor;
import org.eclipse.edt.gen.AbstractGeneratorCommand;

public class EUnitRunAllJavascriptAsyncDriverGenerator extends
		EUnitRunAllDriverGenerator {

	public EUnitRunAllJavascriptAsyncDriverGenerator(
			AbstractGeneratorCommand processor,
			IGenerationMessageRequestor msgReq, String driverPartNameAppend,
			IEUnitGenerationNotifier eckGenerationNotifier) {
		super(processor, msgReq, driverPartNameAppend, eckGenerationNotifier);
	}
	
	@Override
	protected void genImports() {
		out.println("import " + CommonUtilities.EUNITRUNTIME_PACKAGENAME +".TestListMgr;");
		out.println("import " + CommonUtilities.EUNITRUNTIME_PACKAGENAME + ".runTestMethod;");
		super.genImports();
	}

	@Override
	public void generateRunAllDriver(List<String> listOfGenedLibs,
			TestCounter totalCnts) {
		genPackageDeclaration();
		
		genImports();

		String genedPartName = RunAllTest + fDriverPartNameAppend;		
		out.println("Handler " + genedPartName + " type RUIhandler {initialUI = [], includefile = \"rununit.html\", onConstructionFunction = start, title=\"" + genedPartName + "\"} ");
		out.pushIndent();
		out.println("function start()");
		out.pushIndent();
		out.println("TestListMgr.LibraryStartTests = new runTestMethod[];");
		for(String genLibName : listOfGenedLibs){
			out.print("TestListMgr.LibraryStartTests ::= ");
			out.println(genLibName + "." + CommonUtilities.exeTestMethodName + ";");				
		}
		out.println("TestListMgr.LibraryStartTests ::= " + CommonUtilities.endTestMethodName + ";");
		out.println("TestListMgr.LibraryStartTests[1]();");
		out.popIndent();
		out.println("end");
		
		out.println();
		out.println("function " + CommonUtilities.endTestMethodName + "()");
		out.pushIndent();		
		out.println("TestExecutionLib.writeResultSummary(" + totalCnts.getCount() + ");");
		out.popIndent();
		out.println("end");
		out.popIndent();
		out.println("end");		
		out.close();
		
	}	

}
