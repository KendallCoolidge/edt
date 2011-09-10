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
package org.eclipse.edt.gen.javascript.templates.egl.lang;

import org.eclipse.edt.gen.javascript.CommonUtilities;
import org.eclipse.edt.gen.javascript.Context;
import org.eclipse.edt.gen.javascript.templates.JavaScriptTemplate;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.AsExpression;
import org.eclipse.edt.mof.egl.BinaryExpression;
import org.eclipse.edt.mof.egl.FixedPrecisionType;
import org.eclipse.edt.mof.egl.IntegerLiteral;
import org.eclipse.edt.mof.egl.ParameterizableType;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.utils.TypeUtils;

public class NumTypeTemplate extends JavaScriptTemplate {

	// this method gets invoked when there is a specific fixed precision needed
	public void genDefaultValue(FixedPrecisionType type, Context ctx, TabbedWriter out) {
		processDefaultValue(type, ctx, out);
	}

	// this method gets invoked when there is a generic (unknown) fixed precision needed
	public void genDefaultValue(ParameterizableType type, Context ctx, TabbedWriter out) {
		processDefaultValue(type, ctx, out);
	}

	public void processDefaultValue(Type type, Context ctx, TabbedWriter out) {
		out.print("egl.javascript.BigDecimal.prototype.ZERO");
	}

	// this method gets invoked when there is a specific fixed precision needed
	public void genSignature(FixedPrecisionType type, Context ctx, TabbedWriter out) {
		String signature = "N" + type.getLength() + ":" + type.getDecimals() + ";";
		out.print(signature);
	}

	// this method gets invoked when there is a generic (unknown) fixed precision needed
	public void genSignature(ParameterizableType type, Context ctx, TabbedWriter out) {
		String signature = "N;";
		out.print(signature);
	}

	protected boolean needsConversion(Type fromType, Type toType) {
		boolean result = true;
		if (TypeUtils.isNumericType(fromType) && !CommonUtilities.needsConversion(fromType, toType))
			result = CommonUtilities.isJavaScriptBigDecimal(toType);
		return result;
	}

	public void genConversionOperation(FixedPrecisionType type, Context ctx, TabbedWriter out, AsExpression arg) {
		Type toType = arg.getEType();
		Type fromType = arg.getObjectExpr().getType();
		if ((arg.getConversionOperation() == null) && TypeUtils.isNumericType(fromType)) {
			if (needsConversion(fromType, toType)) {
				out.print(ctx.getNativeImplementationMapping(toType) + '.');
				out.print("from");
				out.print(ctx.getNativeTypeName(fromType));
				out.print("(");
				ctx.invoke(genExpression, arg.getObjectExpr(), ctx, out);
				ctx.invoke(genTypeDependentOptions, arg.getEType(), ctx, out, arg);
				out.print(")");
			} else {
				ctx.invoke(genExpression, arg.getObjectExpr(), ctx, out);
			}
		} else {
			// we need to invoke the logic in type template to call back to the other conversion situations
			ctx.invokeSuper(this, genConversionOperation, type, ctx, out, arg);
		}
	}

	public void genTypeDependentOptions(ParameterizableType type, Context ctx, TabbedWriter out, AsExpression arg) {
		out.print(", ");
		// if we get here, then we have been given an integer literal, to be represented as a FixedPrecisionType. So, we must
		// set the dependend options to be a list of nines
		if (arg.getObjectExpr() instanceof IntegerLiteral) {
			String value = ((IntegerLiteral) arg.getObjectExpr()).getValue();
			if (value.startsWith("-"))
				value = value.substring(1);
			if (value.length() > 4)
				out.print("egl.javascript.BigDecimal.prototype.NINES[8]");
			else
				out.print("egl.javascript.BigDecimal.prototype.NINES[3]");
		} else
			out.print("egl.javascript.BigDecimal.prototype.NINES[8]");
	}

	public void genTypeDependentOptions(ParameterizableType type, Context ctx, TabbedWriter out) {
		out.print(", ");
		out.print("egl.javascript.BigDecimal.prototype.NINES[8]");
	}

	public void genBinaryExpression(Type type, Context ctx, TabbedWriter out, BinaryExpression arg) {
		if (false) { // TODO sbg other impls of genBinaryExpression consider nullables
		} else {
			out.print(getNativeStringPrefixOperation(arg));
			out.print("(");
			ctx.invoke(genExpression, arg.getLHS(), ctx, out);
			out.print(getNativeStringOperation(arg));
			ctx.invoke(genExpression, arg.getRHS(), ctx, out);
			out.print(getNativeStringComparisionOperation(arg));
			out.print(")");
		}
	}

	@SuppressWarnings("static-access")
	protected String getNativeStringPrefixOperation(BinaryExpression expr) {
		String op = expr.getOperator();
		if (op.equals(expr.Op_NE))
			return "!";
		if (op.equals(expr.Op_MODULO))
			return "egl.remainder";
		return "";
	}

	@SuppressWarnings("static-access")
	protected String getNativeStringOperation(BinaryExpression expr) {
		String op = expr.getOperator();
		// these are the defaults for what can be handled by the java string class
		if (op.equals(expr.Op_PLUS))
			return " + ";
		if (op.equals(expr.Op_MINUS))
			return " - ";
		if (op.equals(expr.Op_MULTIPLY))
			return " * ";
		if (op.equals(expr.Op_DIVIDE))
			return " / ";
		if (op.equals(expr.Op_EQ))
			return ".compareTo(";
		if (op.equals(expr.Op_NE))
			return ".compareTo(";
		if (op.equals(expr.Op_LT))
			return ".compareTo(";
		if (op.equals(expr.Op_GT))
			return ".compareTo(";
		if (op.equals(expr.Op_LE))
			return ".compareTo(";
		if (op.equals(expr.Op_GE))
			return ".compareTo(";
		if (op.equals(expr.Op_AND))
			return " && ";
		if (op.equals(expr.Op_OR))
			return " || ";
		if (op.equals(expr.Op_CONCAT))
			return " + ";
		if (op.equals(expr.Op_MODULO))
			return ",";
		return "";
	}

	@SuppressWarnings("static-access")
	protected String getNativeStringComparisionOperation(BinaryExpression expr) {
		String op = expr.getOperator();
		if (op.equals(expr.Op_EQ))
			return ") == 0";
		if (op.equals(expr.Op_NE))
			return ")";
		if (op.equals(expr.Op_LT))
			return ") < 0";
		if (op.equals(expr.Op_GT))
			return ") > 0";
		if (op.equals(expr.Op_LE))
			return ") <= 0";
		if (op.equals(expr.Op_GE))
			return ") >= 0";
		return "";
	}
}
