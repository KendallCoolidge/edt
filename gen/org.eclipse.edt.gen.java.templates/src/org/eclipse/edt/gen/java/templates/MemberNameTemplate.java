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

import org.eclipse.edt.gen.java.CommonUtilities;
import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Expression;
import org.eclipse.edt.mof.egl.FunctionParameter;
import org.eclipse.edt.mof.egl.Member;
import org.eclipse.edt.mof.egl.MemberName;
import org.eclipse.edt.mof.egl.Type;

public class MemberNameTemplate extends JavaTemplate {

	public void genAssignment(MemberName expr, Context ctx, TabbedWriter out, Expression arg1, String arg2) {
		// check to see if we are copying boxed function parameters
		if (expr.getMember() instanceof FunctionParameter
			&& org.eclipse.edt.gen.CommonUtilities.isBoxedParameterType((FunctionParameter) expr.getMember(), ctx)) {
			ctx.invoke(genAccessor, expr.getMember(), ctx, out);
			out.print(".ezeCopy(");
			// if we are doing some type of complex assignment, we need to place that in the argument
			if (arg2.length() > 3 && arg2.indexOf("=") > 1) {
				out.print("(");
				ctx.invoke(genRuntimeTypeName, expr.getMember().getType(), ctx, out, TypeNameKind.JavaObject);
				out.print(")");
				ctx.invoke(genAccessor, expr.getMember(), ctx, out);
				out.print(".ezeUnbox()");
				out.print(arg2.substring(0, arg2.indexOf("=")) + arg2.substring(arg2.indexOf("=") + 1));
			}
			// check to see if we are unboxing RHS temporary variables (inout and out types only)
			if (CommonUtilities.isBoxedOutputTemp(arg1, ctx)) {
				out.print("((");
				ctx.invoke(genRuntimeTypeName, arg1.getType(), ctx, out, TypeNameKind.JavaObject);
				out.print(")");
				ctx.invoke(genExpression, arg1, ctx, out);
				out.print(".ezeUnbox()");
				out.print(")");
			} else
				ctx.invoke(genExpression, arg1, ctx, out);
			out.print(")");
			// check to see if we are copying LHS boxed temporary variables (inout and out types only)
		} else if (CommonUtilities.isBoxedOutputTemp(expr, ctx)) {
			ctx.invoke(genExpression, (Expression) expr, ctx, out);
			out.print(arg2);
			out.print("org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeWrap(");
			// check to see if we are unboxing RHS temporary variables (inout and out types only)
			if (CommonUtilities.isBoxedOutputTemp(arg1, ctx)) {
				out.print("((");
				ctx.invoke(genRuntimeTypeName, arg1.getType(), ctx, out, TypeNameKind.JavaObject);
				out.print(")");
				ctx.invoke(genExpression, arg1, ctx, out);
				out.print(".ezeUnbox()");
				out.print(")");
			} else
				ctx.invoke(genExpression, arg1, ctx, out);
			out.print(")");
			// check to see if we are unboxing RHS temporary variables (inout and out types only)
		} else if (CommonUtilities.isBoxedOutputTemp(arg1, ctx)) {
			ctx.invoke(genExpression, (Expression) expr, ctx, out);
			out.print(arg2);
			out.print("((");
			ctx.invoke(genRuntimeTypeName, arg1.getType(), ctx, out, TypeNameKind.JavaObject);
			out.print(")");
			ctx.invoke(genExpression, arg1, ctx, out);
			out.print(".ezeUnbox()");
			out.print(")");
		} else
			ctx.invokeSuper(this, genAssignment, expr, ctx, out, arg1, arg2);
	}

	public void genExpression(MemberName expr, Context ctx, TabbedWriter out) {
		Member member = expr.getMember();
		if (member != null && member.getContainer() != null && member.getContainer() instanceof Type)
			ctx.invoke(genContainerBasedMemberName, (Type) member.getContainer(), ctx, out, expr, member);
		else {
			genMemberName(expr, ctx, out);
		}
	}

	public void genMemberName(MemberName expr, Context ctx, TabbedWriter out) {
		// check to see if we are copying boxed function parameters
		if (expr.getMember() instanceof FunctionParameter
			&& org.eclipse.edt.gen.CommonUtilities.isBoxedParameterType((FunctionParameter) expr.getMember(), ctx)
			&& !((FunctionParameter) expr.getMember()).isConst()) {
			out.print("((");
			ctx.invoke(genRuntimeTypeName, expr.getType(), ctx, out, TypeNameKind.JavaObject);
			out.print(")");
			ctx.invoke(genAccessor, expr.getMember(), ctx, out);
			out.print(".ezeUnbox()");
			out.print(")");
		} else
			ctx.invoke(genAccessor, expr.getMember(), ctx, out);
	}
}
