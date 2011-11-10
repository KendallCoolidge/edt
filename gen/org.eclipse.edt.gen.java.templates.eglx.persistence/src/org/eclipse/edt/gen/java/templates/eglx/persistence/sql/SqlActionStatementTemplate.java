package org.eclipse.edt.gen.java.templates.eglx.persistence.sql;

import java.io.StringWriter;
import java.util.List;

import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.gen.java.templates.StatementTemplate;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.AccessKind;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.ArrayType;
import org.eclipse.edt.mof.egl.Assignment;
import org.eclipse.edt.mof.egl.Classifier;
import org.eclipse.edt.mof.egl.EGLClass;
import org.eclipse.edt.mof.egl.Expression;
import org.eclipse.edt.mof.egl.Field;
import org.eclipse.edt.mof.egl.FixedPrecisionType;
import org.eclipse.edt.mof.egl.Function;
import org.eclipse.edt.mof.egl.LHSExpr;
import org.eclipse.edt.mof.egl.Member;
import org.eclipse.edt.mof.egl.MemberAccess;
import org.eclipse.edt.mof.egl.MemberName;
import org.eclipse.edt.mof.egl.Name;
import org.eclipse.edt.mof.egl.Statement;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.utils.TypeUtils;
import org.eclipse.edt.mof.eglx.persistence.sql.DummyExpression;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlActionStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.SqlPrepareStatement;
import org.eclipse.edt.mof.eglx.persistence.sql.impl.DummyExpressionDynamicImpl;
import org.eclipse.edt.mof.eglx.persistence.sql.utils.SQL;

public abstract class SqlActionStatementTemplate extends StatementTemplate {
	public static final String sqlStmtKey = "org.eclipse.edt.gen.java.sql.stmtkey";
	public static final String genClause = "genClause";
	private static final String genStatementOptions = "genStatementOptions";
	private static final String AnnotationSQLResultSetControl = "eglx.persistence.sql.SQLResultSetControl";
	public static final String genSelectClause = "genSelectClause";
	public static final String var_connection = "ezeConn";
	public static final String var_statement = "ezeStatement";
	public static final String var_listElement = "ezeElement";
	public static final String expr_getConnection = "getConnection()";
	public static final String class_PreparedStatement = "java.sql.PreparedStatement";
	public static final String class_CallableStatement = "java.sql.CallableStatement";
	public static final String class_ResultSet = "java.sql.ResultSet";
	public static final String err_noSqlGenerated = "<no SQL generated>";
	
	public String quoted(String value) {
		StringBuilder out = new StringBuilder();
		out.append('"');
		out.append(value);
		out.append('"');
		return out.toString();
	}
	

	public void genStatementBody(Statement stmt, Context ctx, TabbedWriter out ) {
		// TODO Auto-generated method stub
		
	}

	public void genStatementEnd(TabbedWriter out) {
		// Do nothing as these statements are handled as sets of java code
		// that will manage their own blocks of code
	}
	
	public void genSqlStatementSetup(SqlActionStatement stmt, Context ctx, TabbedWriter out, String var_stmt) {
		String varName = var_statement;
		if(stmt.getPreparedStatement() != null){
			varName = getExprString(stmt.getPreparedStatement(), ctx);
		}
		genSqlStatementSetup(stmt, ctx, out, varName, false);
	}

	/**
	 * Generate code necessary to retrieve current result set row into target variables
	 * This method assumes a list of expressions can only be of non entity types, i.e. each
	 * expression maps directly to one column.  An single Entity expression would have the fields
	 * of the entity map to the given result set columns
	 * @param targets
	 * @param dataSource
	 * @param args
	 * @param ctx
	 * @param out
	 */
	protected void genGetSingleRowFromResultSet(List<Expression> targets, String resultSet, Context ctx, TabbedWriter out) {
		int i = 1;
		for (Expression target : targets) {
			genSetTargetFromResultSet(target, resultSet, i, ctx, out);
			i++;
		}
	}
	
	protected void genGetSingleRowFromResultSet(Expression target, String resultSet, Context ctx, TabbedWriter out) {
		EGLClass type = (EGLClass)target.getType().getClassifier();

		if (SQL.isMappedSQLType(type)) { 
			genSetTargetFromResultSet(target, resultSet, 1, ctx, out);
		}
		else{
			if (target.isNullable()) { 
				out.print("if (");
				ctx.invoke(genExpression, target, ctx, out);
				out.println(" == null) {");
				ctx.invoke(genExpression, target, ctx, out);
				out.print(" = ");
				ctx.invoke(genInstantiation, type, ctx, out);
				out.println(";");
				out.println("}");
			}
			int idx = 1;
			for (Field field : type.getFields()) {
				if (!field.isStatic() && 
						field.getAccessKind()!= AccessKind.ACC_PRIVATE && 
						SQL.isReadable(field) &&
						SQL.isMappedSQLType((EGLClass)field.getType().getClassifier())) {
					genSetTargetFromResultSet(target, field, resultSet, idx++, ctx, out);
				}
			}
		}
	}
	
	protected void genGetSingleRowFromResultSet(EGLClass type, String targetVar, String resultSet, Context ctx, TabbedWriter out) {
		DummyExpression dummy = DummyExpressionDynamicImpl.newInstance();
		dummy.setExpr(targetVar);
		dummy.setType(type);
		genGetSingleRowFromResultSet(dummy, resultSet, ctx, out);
	}


	
	private void genSetTargetFromResultSet(Expression target, String var_resultSet, int columnIndex, Context ctx, TabbedWriter out) {
		TabbedWriter newOut = new TabbedWriter(new StringWriter());
		genGetColumnValueByIndex(target.getType(), var_resultSet, columnIndex, ctx, newOut);
		genSetTargetFromResultSet(target, newOut.getCurrentLine(), var_resultSet, ctx, out);
	}
	
	private void genSetTargetFromResultSet(Expression target, Field field, String var_resultSet, int columnIndex, Context ctx, TabbedWriter out) {
		TabbedWriter newOut = new TabbedWriter(new StringWriter());
		ctx.invoke(genName, field, ctx, newOut);
		MemberAccess expr = ctx.getFactory().createMemberAccess();
		expr.setQualifier(target);
		expr.setId(newOut.getCurrentLine());
		genSetTargetFromResultSet(expr, var_resultSet, columnIndex, ctx, out);
	}
	
	private void genSetTargetFromResultSet(Expression target, String rhsExpr, String resultSetVar, Context ctx, TabbedWriter out) {
		Assignment assign = ctx.getFactory().createAssignment();
		assign.setLHS((LHSExpr)target);
		DummyExpression dummy = DummyExpressionDynamicImpl.newInstance();
		dummy.setExpr(rhsExpr);
		dummy.setType(target.getType());
		assign.setRHS(dummy);
		ctx.invoke(genExpression, assign, ctx, out);
		out.println(";");
		if(target.isNullable()){
			out.print("if(");
			out.print(resultSetVar);
			out.println(".wasNull()){");
			assign = ctx.getFactory().createAssignment();
			assign.setLHS((LHSExpr)target);
			dummy = DummyExpressionDynamicImpl.newInstance();
			dummy.setExpr("null");
			dummy.setType(target.getType());
			assign.setRHS(dummy);
			ctx.invoke(genExpression, assign, ctx, out);
			out.println(";");
			out.println("}");
			
		}
	}

	public boolean genSqlStatementSetup(SqlActionStatement stmt, Context ctx, TabbedWriter out, String var_stmt, boolean stmtDeclared) {
		boolean isCall = SQL.isCallStatement(stmt.getSqlString());
		out.println("try {");
		if (stmt.getPreparedStatement() == null || stmt instanceof SqlPrepareStatement) {
			Integer stmtNumber = getNextStatementKey(ctx);
			String typeSignature = quoted(((Type)((Function)stmt.getContainer()).getContainer()).getTypeSignature());
			if (!stmtDeclared)
				if (isCall) {
					out.print(class_CallableStatement + " ");
					out.print(var_stmt + " = (" + class_CallableStatement + ")");
				}
				else {
					out.print(class_PreparedStatement + " ");
					out.print(var_stmt + " = (" + class_PreparedStatement + ")");
				}
			ctx.invoke(genExpression, stmt.getDataSource(), ctx, out);
			out.println(".getStatement(" + typeSignature + ", " + stmtNumber + ");");
			out.println("if (" + var_stmt + "== null) {");
			out.print("String stmtStr = ");
			if (stmt instanceof SqlPrepareStatement) {
				ctx.invoke(genExpression, ((SqlPrepareStatement)stmt).getSqlStringExpr(), ctx, out);
			}
			else {
				out.print(quoted(SQL.removeCRLFs(stmt.getSqlString())));
			}
			out.println(';');
			out.print(var_stmt + " = ");
			ctx.invoke(genExpression, stmt.getDataSource(), ctx, out);
			out.print('.' + expr_getConnection);
			if (isCall) {
				out.print(".prepareCall(stmtStr");
			}
			else {
				out.print(".prepareStatement(stmtStr");
			}
			if(getResultSet(stmt) != null){
				ctx.invoke(genStatementOptions, stmt, ctx, out, getResultSet(stmt));
			}
			out.println(");");
			ctx.invoke(genExpression, stmt.getDataSource(), ctx, out);
			out.println(".registerStatement(" + typeSignature + ", " + stmtNumber + ", " + var_stmt + ");");
			out.println("}");
		}
		if (stmt.getUsingExpressions()!= null) {
			if (isCall) {
				out.print("java.sql.ParameterMetaData ezeParmData = ");
				out.println(var_stmt + ".getParameterMetaData();");
				int i = 1;
				for (Expression uexpr : stmt.getUsingExpressions()) {
					// For each parameter find out if it is an OUT parameter.
					// If so register the type of the parameter based on the
					// mapping of the input argument expression type to SQL types
					out.println("if (ezeParmData.getParameterMode(" + i + ") == java.sql.ParameterMetaData.parameterModeOut) {");
					out.print(var_stmt + ".registerOutParameter(");
					out.print(i);
					out.print(", ");
					out.print(SQL.getSQLTypeConstant(uexpr.getType().getClassifier()));
					if (uexpr.getType() instanceof FixedPrecisionType) {
						out.print(", ");
						out.print(((FixedPrecisionType)uexpr.getType()).getDecimals());
					}
					out.println(");");
					out.println("}");
					i++;
				}
			}
			int i = 1;
			if(stmt.getUsingExpressions() != null && stmt.getUsingExpressions().size() > 0 ){
				for (Expression uexpr : stmt.getUsingExpressions()) {
					genSetColumnValue(stmt, uexpr, var_stmt, i, ctx, out);
					i++;
				}
			}
			else{
				genUsingForClause(stmt, var_stmt, ctx, out);
			}
		}
		return isCall;
	}
	
	protected void genUsingForClause(SqlActionStatement stmt, String var_stmt, Context ctx, TabbedWriter out){
		
	}
	public void genSqlStatementSetup(SqlActionStatement stmt, Context ctx, TabbedWriter out) {
		genSqlStatementSetup(stmt, ctx, out, var_statement);
	}

	public void genStatementOptions(SqlActionStatement stmt, Context ctx, TabbedWriter out, MemberAccess member) {
		genStatementOptions(member.getMember().getAnnotation(AnnotationSQLResultSetControl), ctx, out);
	}

	public void genStatementOptions(SqlActionStatement stmt, Context ctx, TabbedWriter out, MemberName member) {
		genStatementOptions(member.getMember().getAnnotation(AnnotationSQLResultSetControl), ctx, out);
	}

	protected Expression getResultSet(SqlActionStatement stmt){
		return stmt.getTarget();
	}
	private void genStatementOptions(Annotation annot, Context ctx, TabbedWriter out) {
		if(annot != null){
			ctx.invoke(genAnnotation, annot.getEClass(), ctx, out, annot);
		}
	}

	public void genDefaultWhereClauseParameterSettings(EGLClass targetType, Expression targetExpr, String var_statement, int startParmIndex, Context ctx, TabbedWriter out) {
		int i = startParmIndex;
		for (Field f : targetType.getFields()) {
			if (SQL.isKeyField(f)) {
				out.print(var_statement);
				out.print(".setObject(" + i + ", ");
				if (targetExpr.getType() instanceof ArrayType) {
					out.print(var_listElement);
				}
				else {
					ctx.invoke(genExpression, targetExpr, ctx, out);
				}
				out.print('.');
				ctx.invoke(genName, f, ctx, out);
				out.println(");");
				i++;
			}
		}

	}

	
	public void genSqlStatementEnd(SqlActionStatement stmt, Context ctx, TabbedWriter out) {
		out.println("}");
		out.println("catch(java.sql.SQLException ezeEx) {");
		out.println("throw org.eclipse.edt.javart.util.JavartUtil.makeEglException(ezeEx);");
		out.println('}');
	}
	
	// Handles conversion of EGL values to SQL values in places where the default mapping
	// is not sufficient, i.e. dates and timestamps need to become SQL dates and timestamps
	public void genConvertValueStart(EGLClass type, Context ctx, TabbedWriter out) {
		if (SQL.isWrappedSQLType(type)) {
			out.print("new ");
			out.print(SQL.getSQLTypeName(type));
			out.print('(');	
		}
	}
	
	public void genConvertValueEnd(EGLClass type, Context ctx, TabbedWriter out) {
		if (SQL.isWrappedSQLType(type)) {
			out.print('.');
			out.print(SQL.getConvertToSQLConstructorOptions(type));
			out.print(')');
		}
	}
	
	protected void genGetColumnValueByIndex(Type type, String resultSetName, int columnIndex, Context ctx, TabbedWriter out) {
		Classifier typeClassifier = type.getClassifier();
		if (typeClassifier.equals(TypeUtils.Type_TIMESTAMP)) {
			out.print("org.eclipse.edt.runtime.java.eglx.lang.ETimestamp.asTimestamp(org.eclipse.edt.javart.util.DateTimeUtil.getNewCalendar(");
			out.print(resultSetName);
			out.print('.');
			genSqlGetValueMethodName(typeClassifier, ctx, out);
			out.print("(" + columnIndex + "))");
			ctx.invoke(genTypeDependentOptions, type, ctx, out);
			out.print(")");
		}
		else if(typeClassifier.equals(TypeUtils.Type_DATE)){
			out.print("org.eclipse.edt.runtime.java.eglx.lang.EDate.asDate(org.eclipse.edt.javart.util.DateTimeUtil.getNewCalendar(");
			out.print(resultSetName);
			out.print('.');
			genSqlGetValueMethodName(typeClassifier, ctx, out);
			out.print("(" + columnIndex + ")))");
		}
		else {
			out.print(resultSetName);
			out.print('.');
			genSqlGetValueMethodName(typeClassifier, ctx, out);
			out.print("(" + columnIndex + ")");
		}
	}
	public boolean isNullable(SqlActionStatement stmt, Context ctx, MemberAccess expr) {
		return expr.getMember().isNullable();
	}
	public Boolean isNullable(SqlActionStatement stmt, Context ctx, MemberName expr) {
		return expr.getMember().isNullable();
	}
	public Boolean isNullable(SqlActionStatement stmt, Context ctx, Expression expr) {
		return Boolean.FALSE;
	}
	protected void genSetColumnValue(SqlActionStatement stmt, Expression expr, String stmt_or_resultSet_var, int columnIndex, Context ctx, TabbedWriter out) {
		EGLClass type = (EGLClass)expr.getType().getClassifier();
		Boolean isNullable = (Boolean)ctx.invoke("isNullable", stmt, ctx, expr);
		if(isNullable != null && isNullable){
			out.print("if(null == ");
			ctx.invoke(genExpression, expr, ctx, out);
			out.println("){");
			out.print(stmt_or_resultSet_var);
			out.print(".setNull(");
			out.print(columnIndex);
			out.print(", ");
			out.print(SQL.getSQLTypeConstant(type));
			out.println(");");
			out.println("}");
			out.println("else{");
		}
		out.print(stmt_or_resultSet_var);
		out.print('.');
		genSqlSetValueMethodName(type, ctx, out);
		out.print('(');
		out.print(columnIndex);
		out.print(", ");
		genConvertValueStart(type, ctx, out);
		ctx.invoke(genExpression, expr, ctx, out);
		genConvertValueEnd(type, ctx, out);
		out.println(");");
		if(isNullable != null && isNullable){
			out.println("}");
		}
	}
	
	protected void genSetColumnValue(Field field, String stmt_or_resultSet_var, String varName, int columnIndex, Context ctx, TabbedWriter out) {
		EGLClass type = (EGLClass)field.getType().getClassifier();
		if(field.isNullable()){
			out.print("if(null == ");
			out.print(varName);
			out.print('.');
			ctx.invoke(genName, field, ctx, out);
			out.println("){");
			out.print(stmt_or_resultSet_var);
			out.print(".setNull(");
			out.print(columnIndex);
			out.print(", ");
			out.print(SQL.getSQLTypeConstant(field.getType().getClassifier()));
			out.println(");");
			out.println("}");
			out.println("else{");
		}
		out.print(stmt_or_resultSet_var);
		out.print('.');
		genSqlSetValueMethodName(type, ctx, out);
		out.print('(');
		out.print(columnIndex);
		out.print(", ");
		genConvertValueStart(type, ctx, out);
		out.print(varName);
		out.print('.');
		ctx.invoke(genName, field, ctx, out);
		genConvertValueEnd(type, ctx, out);
		out.println(");");
		if(field.isNullable()){
			out.println("}");
		}
	}

	
	public void genSqlGetValueMethodName(Classifier type, Context ctx, TabbedWriter out) {
		String name = "get";
		name += SQL.getSqlSimpleTypeName(type);
		out.print(name);
	}
	
	public void genSqlSetValueMethodName(Classifier type, Context ctx, TabbedWriter out) {
		String name = "set";
		name += SQL.getSqlSimpleTypeName(type);
		out.print(name);
	}

	public Integer getNextStatementKey(Context ctx) {
		Integer key = (Integer)ctx.get(sqlStmtKey);
		if (key == null) {
			key = 0;
		}
		else {
			key++;
		}
		ctx.put(sqlStmtKey, key);
		return key;
	}
	
	public String getCursorNameFromDataSource(Expression datasource, Context ctx) {
		Member ds = (Member)((Name)datasource).getNamedElement();
		return (String)ctx.getAttribute(ds, "eglx.persistence.sql.CursorName");
	}

	public EGLClass getTargetType(SqlActionStatement stmt) {
		Type type = stmt.getTarget().getType();
		return getBaseType(type);
	}
	
	public EGLClass getBaseType(Type type) {
		if (type instanceof ArrayType) {
			return (EGLClass)((ArrayType)type).getElementType();
		}
		else {
			return (EGLClass)type;
		}
	}

	
	public String getExprString(Expression expr, Context ctx) {
		if (expr == null) return "<invalid expression>";
		
		TabbedWriter out = new TabbedWriter(new StringWriter());
		ctx.invoke(genExpression, expr, ctx, out);
		return out.getCurrentLine();
	}
	

}
