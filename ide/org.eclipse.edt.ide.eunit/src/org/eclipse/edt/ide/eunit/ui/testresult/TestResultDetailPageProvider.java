package org.eclipse.edt.ide.eunit.ui.testresult;

import org.eclipse.edt.ide.eunit.ui.testresult.ResultSummaryBlock.Record_ResultSummary;
import org.eclipse.edt.ide.eunit.ui.testresult.ResultSummaryBlock.TestResultPkgNode;
import org.eclipse.edt.ide.eunit.ui.testresult.ResultSummaryBlock.TestResultRootNode;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;

public class TestResultDetailPageProvider implements IDetailsPageProvider {

	@Override
	public IDetailsPage getPage(Object key) {						
		if(key instanceof TestResultRootNode){
			TestResultRootNode rootNode = (TestResultRootNode)key;
			return (new TestResultRootNodeDetailsPage(rootNode));
		}

		if(key instanceof TestResultPkgNode){
			TestResultPkgNode pkgNode = (TestResultPkgNode)key;
			return (new TestResultPkgNodeDetailsPage(pkgNode.statisticCnts));
		}
				
				
		if(key instanceof Record_ResultSummary){
			Record_ResultSummary resultSum = (Record_ResultSummary)key;
			return (new RecordResultDetailsPage(resultSum));
		}
		
		return null;
	}

	@Override
	public Object getPageKey(Object object) {
		return object;
	}

}
