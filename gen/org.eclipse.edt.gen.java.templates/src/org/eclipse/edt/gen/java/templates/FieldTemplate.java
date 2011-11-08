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
package org.eclipse.edt.gen.java.templates;

import org.eclipse.edt.gen.Constants;
import org.eclipse.edt.gen.java.CommonUtilities;
import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.Assignment;
import org.eclipse.edt.mof.egl.AssignmentStatement;
import org.eclipse.edt.mof.egl.ExternalType;
import org.eclipse.edt.mof.egl.Field;
import org.eclipse.edt.mof.egl.Function;
import org.eclipse.edt.mof.egl.FunctionParameter;
import org.eclipse.edt.mof.egl.MemberName;
import org.eclipse.edt.mof.egl.ParameterKind;
import org.eclipse.edt.mof.egl.ReturnStatement;
import org.eclipse.edt.mof.egl.StatementBlock;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.utils.TypeUtils;

public class FieldTemplate extends JavaTemplate {

	public void preGen(Field field, Context ctx) {
		ctx.invoke(preGen, field.getType(), ctx);
		if (field.getContainer() instanceof Type) {
			if (field.getAnnotation(Constants.AnnotationJsonName) == null) {
				// add an xmlElement
				try {
					Annotation annotation = CommonUtilities.getAnnotation(ctx, Type.EGL_KeyScheme + Type.KeySchemeDelimiter + Constants.AnnotationJsonName);
					annotation.setValue(field.getId());
					field.addAnnotation(annotation);
				}
				catch (Exception e) {}
			}
		}
	}

	public void genDeclaration(Field field, Context ctx, TabbedWriter out) {
		// write out the debug extension data
		CommonUtilities.generateSmapExtension(field, ctx);
		// process the field
		if (field.getContainer() != null) {
			ctx.invoke(genXmlTransient, field.getContainer(), out);
			ctx.invoke(genAnnotations, field.getContainer(), ctx, out, field);
		}
		ctx.invokeSuper(this, genDeclaration, field, ctx, out);
		transientOption(field, out);
		ctx.invoke(genRuntimeTypeName, field, ctx, out, TypeNameKind.JavaPrimitive);
		out.print(" ");
		ctx.invoke(genName, field, ctx, out);
		out.println(";");
	}

	public void genAnnotations(Field field, Context ctx, TabbedWriter out) {
		for (Annotation annot : field.getAnnotations()) {
			ctx.invoke(genAnnotation, annot.getEClass(), ctx, out, annot, field);
		}
	}

	public void genInstantiation(Field field, Context ctx, TabbedWriter out) {
		ctx.invoke(genInstantiation, field.getType(), ctx, out, field);
	}

	public void genInitialization(Field field, Context ctx, TabbedWriter out) {
		// is this an inout or out temporary variable to a function. if so, then we need to default or instantiate for
		// our parms, and set to null for inout
		if ((CommonUtilities.isBoxedOutputTemp(field, ctx))) {
			// if the value associated with the temporary variable is 2, then it is to be instantiated (OUT parm)
			// otherwise it is to be defaulted to null (INOUT parm), as there is an assignment already created
			if (ctx.getAttribute(field, org.eclipse.edt.gen.Constants.SubKey_functionArgumentTemporaryVariable) == ParameterKind.PARM_OUT) {
				out.print("org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeWrap(");
				// if the field type is any, then we need to cast
				if (field.getType().getTypeSignature().equalsIgnoreCase("eglx.lang.EAny"))
					out.print("(eglx.lang.EAny) ");
				if (ctx.mapsToNativeType(field.getType()) || ctx.mapsToPrimitiveType(field.getType()))
					ctx.invoke(genDefaultValue, field.getType(), ctx, out, field);
				else
					ctx.invoke(genInstantiation, field.getType(), ctx, out, field);
				out.print(")");
			} else
				out.print("null");
		} else {
			if (field.isNullable())
				ctx.invoke(genDefaultValue, field.getType(), ctx, out, field);
			else if (TypeUtils.isReferenceType(field.getType())) {
				ctx.invoke(genDefaultValue, field.getType(), ctx, out, field);
			} else if (ctx.mapsToNativeType(field.getType()) || ctx.mapsToPrimitiveType(field.getType()))
				ctx.invoke(genDefaultValue, field.getType(), ctx, out, field);
			else
				ctx.invoke(genInstantiation, field.getType(), ctx, out, field);
		}
	}

	public void genInitializeStatement(Field field, Context ctx, TabbedWriter out) {
		processInitializeStatement(field, ctx, out, false);
	}

	public void genInitializeStatement(Field field, Context ctx, TabbedWriter out, Boolean adjustSmap) {
		processInitializeStatement(field, ctx, out, adjustSmap);
	}

	public void processInitializeStatement(Field field, Context ctx, TabbedWriter out, boolean adjustSmap) {
		if (field.getInitializerStatements() == null || field.getInitializerStatements().getStatements().isEmpty()) {
			// there are no initializer statements, so just initialize the field
			ctx.invoke(genName, field, ctx, out);
			out.print(" = ");
			ctx.invoke(genInitialization, field, ctx, out);
			out.println(";");
		} else {
			// if the initializer statements are not against the currect field, then we need to do the initialization in
			// addition to the statements
			if (!(field.getInitializerStatements().getStatements().size() > 0
				&& field.getInitializerStatements().getStatements().get(0) instanceof AssignmentStatement
				&& ((AssignmentStatement) field.getInitializerStatements().getStatements().get(0)).getAssignment().getLHS() instanceof MemberName 
				&& ((MemberName) ((AssignmentStatement) field.getInitializerStatements().getStatements().get(0)).getAssignment().getLHS()).getMember().equals(field))) {
				// we need to initialize the field, before applying the initializer statements
				ctx.invoke(genName, field, ctx, out);
				out.print(" = ");
				ctx.invoke(genInitialization, field, ctx, out);
				out.println(";");
				if (adjustSmap) {
					// as this is an expression that also creates a new line with the above println method, it throws off the
					// smap ending line number by 1. We need to issue a call to correct this
					ctx.setSmapLastJavaLineNumber(out.getLineNumber() - 1);
				}
			}
			// now process the initializer statements
			ctx.invoke(genStatementNoBraces, field.getInitializerStatements(), ctx, out);
		}
	}

	public void genGetter(Field field, Context ctx, TabbedWriter out) {
		ctx.invoke(genAnnotations, field, ctx, out);
		Function function = factory.createFunction();
		StatementBlock statementBlock = factory.createStatementBlock();
		MemberName nameExpression = factory.createMemberName();
		nameExpression.setMember(field);
		nameExpression.setId(field.getName());
		ReturnStatement returnStatement = factory.createReturnStatement();
		returnStatement.setContainer(function);
		returnStatement.setExpression(nameExpression);
		statementBlock.setContainer(function);
		statementBlock.getStatements().add(returnStatement);
		function.setType(field.getType());
		function.setStatementBlock(statementBlock);
		function.setReturnField(field);
		function.setName("get" + genMethodName(field));
		// write out the function
		ctx.invoke(genDeclaration, function, ctx, out);
	}

	public void genSetter(Field field, Context ctx, TabbedWriter out) {
		Function function = factory.createFunction();
		StatementBlock statementBlock = factory.createStatementBlock();
		FunctionParameter functionParameter = factory.createFunctionParameter();
		functionParameter.setContainer(function);
		functionParameter.setName("ezeValue");
		functionParameter.setParameterKind(ParameterKind.PARM_IN);
		functionParameter.setType(field.getType());
		AssignmentStatement assignmentStatement = factory.createAssignmentStatement();
		assignmentStatement.setContainer(function);
		Assignment assignment = factory.createAssignment();
		assignmentStatement.setAssignment(assignment);
		MemberName nameExpression1 = factory.createMemberName();
		nameExpression1.setMember(field);
		nameExpression1.setId(field.getName());
		MemberName nameExpression2 = factory.createMemberName();
		nameExpression2.setMember(functionParameter);
		nameExpression2.setId(functionParameter.getName());
		assignment.setLHS(nameExpression1);
		assignment.setRHS(nameExpression2);
		statementBlock.setContainer(function);
		statementBlock.getStatements().add(assignmentStatement);
		function.setStatementBlock(statementBlock);
		function.getParameters().add(functionParameter);
		function.setName("set" + genMethodName(field));
		// write out the function
		ctx.invoke(genDeclaration, function, ctx, out);
	}

	protected String genMethodName(Field field) {
		String ret = field.getName().substring(0, 1).toUpperCase();
		if (field.getName().length() > 1)
			ret = ret + field.getName().substring(1);
		return ret;
	}

	protected void transientOption(Field field, TabbedWriter out) {
		ExternalType et = CommonUtilities.getJavaExternalType(field.getType());
		if (et != null && !CommonUtilities.isSerializable(et))
			out.print("transient ");
	}
}
