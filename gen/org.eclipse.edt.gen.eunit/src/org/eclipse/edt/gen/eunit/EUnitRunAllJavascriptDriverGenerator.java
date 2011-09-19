package org.eclipse.edt.gen.eunit;

import java.util.List;

import org.eclipse.edt.compiler.internal.interfaces.IGenerationMessageRequestor;
import org.eclipse.edt.gen.AbstractGeneratorCommand;

public class EUnitRunAllJavascriptDriverGenerator extends
		EUnitRunAllDriverGenerator {

	public EUnitRunAllJavascriptDriverGenerator(
			AbstractGeneratorCommand processor,
			IGenerationMessageRequestor msgReq, String driverPartNameAppend, IEUnitGenerationNotifier eckGenerationNotifier) {
		super(processor, msgReq, driverPartNameAppend, eckGenerationNotifier);
	}

	public void generateRunAllDriver(List<String> listOfGenedLibs, TestCounter totalCnts){	
		genPackageDeclaration();
		
		genImports();
		
		String genedPartName = RunAllTest + fDriverPartNameAppend;		
		out.println("Handler " + genedPartName + " type RUIhandler {initialUI = [], includefile = \"rununit.html\", onConstructionFunction = start, title=\"" + genedPartName + "\"} ");
		out.pushIndent();
		out.println("function start()");
		out.pushIndent();
		for(String genLibName : listOfGenedLibs){
			out.println(genLibName + "." + CommonUtilities.exeTestMethodName + "();");				
		}
		out.println("TestExecutionLib.writeResultSummary(" + totalCnts.getCount() + ");");
		out.popIndent();
		out.println("end");
		out.popIndent();
		out.println("end");		
		out.close();
	}	
}
