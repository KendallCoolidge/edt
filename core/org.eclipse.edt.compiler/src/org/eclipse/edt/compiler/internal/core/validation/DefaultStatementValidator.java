package org.eclipse.edt.compiler.internal.core.validation;

import org.eclipse.edt.compiler.internal.core.validation.statement.AddStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.CloseStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.DeleteStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ExecuteStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ForEachStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.GetByKeyStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.OpenStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.PrepareStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ReplaceStatementValidator;

public class DefaultStatementValidator extends AbstractStatementValidator {

	public boolean visit(org.eclipse.edt.compiler.core.ast.AddStatement addStatement) {
		addStatement.accept(new AddStatementValidator(problemRequestor));
		return false;
	}
	
	public boolean visit(org.eclipse.edt.compiler.core.ast.CloseStatement closeStatement) {
		closeStatement.accept(new CloseStatementValidator(problemRequestor));
		return false;
	}
	
	public boolean visit(org.eclipse.edt.compiler.core.ast.DeleteStatement deleteStatement) {
		deleteStatement.accept(new DeleteStatementValidator(problemRequestor, compilerOptions));
		return false;
	}

	public boolean visit(org.eclipse.edt.compiler.core.ast.ExecuteStatement executeStatement) {
		executeStatement.accept(new ExecuteStatementValidator(problemRequestor, compilerOptions));
		return false;
	}

	public boolean visit(org.eclipse.edt.compiler.core.ast.ForEachStatement foreachStatement) {
		foreachStatement.accept(new ForEachStatementValidator(problemRequestor));
		return false;
	}
	
	public boolean visit(org.eclipse.edt.compiler.core.ast.GetByKeyStatement getStatement) {
		getStatement.accept(new GetByKeyStatementValidator(problemRequestor, compilerOptions));
		return false;
	}

	public boolean visit(org.eclipse.edt.compiler.core.ast.OpenStatement openStatement) {
		openStatement.accept(new OpenStatementValidator(problemRequestor, compilerOptions));
		return false;
	}

	public boolean visit(org.eclipse.edt.compiler.core.ast.PrepareStatement prepareStatement) {
		prepareStatement.accept(new PrepareStatementValidator(problemRequestor, compilerOptions));
		return false;
	}
	
	public boolean visit(org.eclipse.edt.compiler.core.ast.ReplaceStatement replaceStatement) {
		replaceStatement.accept(new ReplaceStatementValidator(problemRequestor, compilerOptions));
		return false;
	}

}
