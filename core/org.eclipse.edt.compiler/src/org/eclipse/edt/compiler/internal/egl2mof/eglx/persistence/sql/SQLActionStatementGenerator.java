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
package org.eclipse.edt.compiler.internal.egl2mof.eglx.persistence.sql;

import java.util.Iterator;
import java.util.List;

import org.eclipse.edt.compiler.core.ast.AbstractASTExpressionVisitor;
import org.eclipse.edt.compiler.core.ast.FromOrToExpressionClause;
import org.eclipse.edt.compiler.core.ast.InlineSQLStatement;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.egl2mof.AbstractIOStatementGenerator;
import org.eclipse.edt.mof.EClass;
import org.eclipse.edt.mof.egl.Expression;
import org.eclipse.edt.mof.egl.Statement;
import org.eclipse.edt.mof.egl.StatementBlock;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlActionStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlAddStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlDeleteStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlExecuteStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlFactory;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlForEachStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlGetByKeyStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlOpenStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlPrepareStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlReplaceStatement;
import org.eclipse.edt.mof.serialization.IEnvironment;

public class SQLActionStatementGenerator extends AbstractIOStatementGenerator {
	
	SqlFactory factory = SqlFactory.INSTANCE;
	final SQLActionStatementGenerator generator = this;

	
	public SQLActionStatementGenerator() {
		super(null);
	}
	
	public SQLActionStatementGenerator(IEnvironment env) {
		super(env);
	}

	private void doCommonVisit(org.eclipse.edt.compiler.core.ast.Statement node, final SqlActionStatement stmt) {
		for (Node expr : (List<Node>)node.getIOObjects()) {
			expr.accept(this);
			stmt.getTargets().add((Expression)stack.pop());
		}
		node.accept(new AbstractASTExpressionVisitor() {
			public boolean visit(FromOrToExpressionClause clause) {
				clause.getExpression().accept(generator);
				stmt.setDataSource((Expression) stack.pop());
				return false;
			}
			public boolean visit(InlineSQLStatement sqlStmt) {
				stmt.setSqlString(sqlStmt.getValue());
				return false;
			}
			public boolean visit(org.eclipse.edt.compiler.core.ast.UsingClause clause) {
				Iterator i = clause.getExpressions().iterator();
				while (i.hasNext()) {
					org.eclipse.edt.compiler.core.ast.Expression expr = (org.eclipse.edt.compiler.core.ast.Expression) i.next();
					expr.accept(generator);
					stmt.getUsingExpressions().add((Expression)stack.pop());
				}
				return false;
			};

			public boolean visit(org.eclipse.edt.compiler.core.ast.UsingKeysClause clause) {
				Iterator i = clause.getExpressions().iterator();
				while (i.hasNext()) {
					org.eclipse.edt.compiler.core.ast.Expression expr = (org.eclipse.edt.compiler.core.ast.Expression) i.next();
					expr.accept(generator);
					stmt.getUsingKeyExpressions().add((Expression)stack.pop());
				}
				return false;
			};
		});

	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(org.eclipse.edt.compiler.core.ast.AddStatement node) {
		SqlAddStatement stmt = factory.createSqlAddStatement();
		stack.push(stmt);
		doCommonVisit(node, stmt);
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(org.eclipse.edt.compiler.core.ast.DeleteStatement node) {
		SqlDeleteStatement stmt = factory.createSqlDeleteStatement();
		stack.push(stmt);
		doCommonVisit(node, stmt);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.ExecuteStatement node) {
		SqlExecuteStatement stmt = factory.createSqlExecuteStatement();
		stack.push(stmt);
		doCommonVisit(node, stmt);
		return false;		
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.ForEachStatement forEachStatement) {
		SqlForEachStatement forEachStmt = factory.createSqlForEachStatement();
		stack.push(forEachStmt);
		doCommonVisit(forEachStatement, forEachStmt);
		StatementBlock block = irFactory.createStatementBlock();
		// TODO: set source info
//		setSourceInfoOn(block, forEachStatement);
		forEachStmt.setBody(block);

		for (Node node : (List<Node>)forEachStatement.getStmts()) {
			node.accept(this);
			block.getStatements().add((Statement)stack.pop());
		}

		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.GetByKeyStatement node) {
		SqlGetByKeyStatement stmt = factory.createSqlGetByKeyStatement();
		stack.push(stmt);
		doCommonVisit(node, stmt);

		return false;
	}


	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.OpenStatement node) {
		SqlOpenStatement stmt = factory.createSqlOpenStatement();
		stack.push(stmt);
		doCommonVisit(node, stmt);
		node.getResultSet().accept(this);
		stmt.getTargets().add((Expression)stack.pop());
		
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.PrepareStatement node) {
		SqlPrepareStatement stmt = factory.createSqlPrepareStatement();
		stack.push(stmt);
//		stmt.setPreparedStatementId(node.getPreparedStatementID());

		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.ReplaceStatement node) {
		SqlReplaceStatement stmt = factory.createSqlReplaceStatement();
		stack.push(stmt);
		doCommonVisit(node, stmt);

		return false;
	}
	
	public EClass getStatementEClass(org.eclipse.edt.compiler.core.ast.AddStatement stmt) {
		return factory.getSqlAddStatementEClass();
	}

	public EClass getStatementEClass(org.eclipse.edt.compiler.core.ast.CloseStatement stmt) {
		return factory.getSqlCloseStatementEClass();
	}

	public EClass getStatementEClass(org.eclipse.edt.compiler.core.ast.DeleteStatement stmt) {
		return factory.getSqlDeleteStatementEClass();
	}

	public EClass getStatementEClass(org.eclipse.edt.compiler.core.ast.ExecuteStatement stmt) {
		return factory.getSqlExecuteStatementEClass();
	}

	public EClass getStatementEClass(org.eclipse.edt.compiler.core.ast.ForEachStatement stmt) {
		return factory.getSqlForEachStatementEClass();
	}

	public EClass getStatementEClass(org.eclipse.edt.compiler.core.ast.GetByKeyStatement stmt) {
		return factory.getSqlGetByKeyStatementEClass();
	}

	public EClass getStatementEClass(org.eclipse.edt.compiler.core.ast.OpenStatement stmt) {
		return factory.getSqlOpenStatementEClass();
	}

	public EClass getStatementEClass(org.eclipse.edt.compiler.core.ast.PrepareStatement stmt) {
		return factory.getSqlPrepareStatementEClass();
	}

	public EClass getStatementEClass(org.eclipse.edt.compiler.core.ast.ReplaceStatement stmt) {
		return factory.getSqlReplaceStatementEClass();
	}



}
