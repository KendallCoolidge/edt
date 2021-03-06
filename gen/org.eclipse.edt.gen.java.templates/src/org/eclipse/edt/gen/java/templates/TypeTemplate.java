/*******************************************************************************
 * Copyright © 2011, 2013 IBM Corporation and others.
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
import org.eclipse.edt.gen.GenerationException;
import org.eclipse.edt.gen.java.CommonUtilities;
import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.javart.util.JavaAliaser;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.*;
import org.eclipse.edt.mof.egl.utils.TypeUtils;

public class TypeTemplate extends JavaTemplate {

	public void preGen(Type type, Context ctx) {
		// when we get here, it is because a type is being referenced by the original part being generated. Add it to the
		// types used table if it doesn't already exist. The first thing we want to check for, is to make sure the
		// unqualified type name (the last node in the name) is not already taken. If it is already taken, we can't do an
		// import for it and when used throughout the program, will have to be fully qualified at all times.
		CommonUtilities.processImport(aliasPackageName(ctx.getNativeImplementationMapping(type.getClassifier())), ctx);
		// in the case where the model name doesn't match the interface name, then we also need to process the interface
		// name as it will exist
		if (ctx.getNativeMapping(ctx.getNativeMapping(type.getClassifier().getTypeSignature())) != null)
			CommonUtilities.processImport(aliasPackageName(ctx.getNativeMapping(type.getClassifier().getTypeSignature())), ctx);
		if (ctx.mapsToPrimitiveType(type.getClassifier())) {
			// if this primitive type is really a primitive, it will map back to the java object. We want to use that java
			// object instead of the primitive mapped simply to a java object. For example, eglx.lang.ESmallint maps to short
			// (the primitive) and then short maps to the java object java.lang.Short. We want to always use the object for
			// the imports.
			if (ctx.getPrimitiveMapping(ctx.getPrimitiveMapping(type.getClassifier().getTypeSignature())) != null)
				CommonUtilities.processImport(aliasPackageName(ctx.getPrimitiveMapping(ctx.getPrimitiveMapping(type.getClassifier().getTypeSignature()))), ctx);
			else
				CommonUtilities.processImport(aliasPackageName(ctx.getPrimitiveMapping(type.getClassifier().getTypeSignature())), ctx);
		}
	}

	private String aliasPackageName(String imported) {
		String type;
		int lastDot = imported.lastIndexOf('.');
		if (lastDot == -1)
			type = imported;
		else
			type = CommonUtilities.packageName(imported.substring(0, lastDot)) + '.' + JavaAliaser.getAlias(imported.substring(lastDot + 1));
		return type;
	}

	public Boolean isAssignmentArrayMatchingWanted(Type type, Context ctx) {
		// types can override this to cause/prevent type matching of array literals to be ignored.
		return true;
	}

	public Boolean isAssignmentBreakupWanted(Type type, Context ctx, Assignment expr) {
		// types can override this to cause/prevent an compound assignment expression to be broken up
		// the arg contains the operation being asked about. we always want certain ones broken up
		if (expr.getOperator().equals("**=") || expr.getOperator().equals("?:=") || expr.getOperator().equals("::=")
			|| (expr.getLHS() instanceof MemberName && CommonUtilities.getPropertyFunction(((MemberName) expr.getLHS()).getNamedElement(), true, ctx) != null))
			return true;
		else
			return false;
	}

	public Boolean isListReorganizationWanted(Type type, Context ctx, Expression arg) {
		// types can override this to cause/prevent list reorganization to be done
		return true;
	}

	public Boolean isMathLibDecimalBoxingWanted(Type type, Context ctx) {
		// types can override this to cause/prevent mathlib decimals/precision boxing to be done
		return false;
	}

	public Boolean isStringLibFormatBoxingWanted(Type type, Context ctx) {
		return false;
	}

	public void genInstantiation(Type type, Context ctx, TabbedWriter out) {
		out.print("new ");
		ctx.invoke(genRuntimeTypeName, type, ctx, out, TypeNameKind.JavaImplementation);
		out.print("(");
		ctx.invoke(genConstructorOptions, type, ctx, out);
		out.print(")");
	}

	public void genInstantiation(Type type, Context ctx, TabbedWriter out, Field arg) {
		ctx.invoke(genInstantiation, type, ctx, out);
	}

	public void genInvocation(Type type, Context ctx, TabbedWriter out, InvocationExpression arg) {
		ctx.invoke(genInvocation, arg, ctx, out);
	}

	public void genDefaultValue(Type type, Context ctx, TabbedWriter out, TypedElement arg) {
		if (arg.isNullable())
			out.print("null");
		else
			ctx.invoke(genDefaultValue, type, ctx, out);
	}

	public void genDefaultValue(Type type, Context ctx, TabbedWriter out, Expression arg) {
		if (arg.isNullable())
			out.print("null");
		else
			ctx.invoke(genDefaultValue, type, ctx, out);
	}

	public void genDefaultValue(Type type, Context ctx, TabbedWriter out) {
		if (TypeUtils.isReferenceType(type))
			ctx.invoke(genInstantiation, type, ctx, out);
		else
			out.print("\"Invalid default value\"");
	}

	public void genRuntimeConstraint(Type type, Context ctx, TabbedWriter out) {
		ctx.invoke(genRuntimeClassTypeName, type, ctx, out, TypeNameKind.EGLImplementation);
	}

	public void genRuntimeTypeName(Type type, Context ctx, TabbedWriter out) {
		ctx.invoke(genRuntimeTypeName, type, ctx, out, TypeNameKind.JavaPrimitive);
	}

	public void genRuntimeTypeExtension(Type type, Context ctx, TabbedWriter out) {
		// no default
	}

	public void genRuntimeClassTypeName(Type type, Context ctx, TabbedWriter out, TypeNameKind arg) {
		ctx.invoke(genRuntimeTypeName, type, ctx, out, arg);
		out.print(".class");
	}

	public void genRuntimeTypeName(Type type, Context ctx, TabbedWriter out, TypeNameKind arg) {
		// are we looking for the default (java primitive) or specifically java primitive, if it exists
		if (arg == TypeNameKind.JavaPrimitive) {
			if (ctx.mapsToPrimitiveType(type.getClassifier())) {
				out.print(ctx.getPrimitiveMapping(type.getClassifier()));
				return;
			}
		}
		// are we looking for the java object
		if (arg == TypeNameKind.JavaObject) {
			if (ctx.mapsToPrimitiveType(type.getClassifier())) {
				String item = ctx.getPrimitiveMapping(type.getClassifier());
				if (ctx.getPrimitiveMapping(item) == null)
					out.print(item);
				else
					out.print(ctx.getPrimitiveMapping(item));
				return;
			}
		}
		// we couldn't resolve the java types, so we have to check for the java implementation name
		if (arg == TypeNameKind.JavaImplementation) {
			if (ctx.mapsToPrimitiveType(type.getClassifier())) {
				String item = ctx.getPrimitiveMapping(type.getClassifier());
				if (ctx.getPrimitiveMapping(item) == null)
					out.print(item);
				else
					out.print(ctx.getPrimitiveMapping(item));
				return;
			}
		}
		// type an egl implementation name
		if (arg == TypeNameKind.EGLImplementation) {
			if (ctx.mapsToNativeType(type.getClassifier())) {
				out.print(ctx.getNativeImplementationMapping(type.getClassifier()));
				return;
			}
		}
		// select the proper default to use. we have run out of options
		if (arg == TypeNameKind.JavaImplementation)
			out.print(ctx.getNativeImplementationMapping(type.getClassifier()));
		else
			// must be an egl interface name we want
			out.print(ctx.getNativeInterfaceMapping(type.getClassifier()));
	}

	public void genConstructorOptions(Type type, Context ctx, TabbedWriter out) {
		// no default
	}

	public void genTypeDependentOptions(Type type, Context ctx, TabbedWriter out) {
		// no default
	}

	public void genConversionOperationOptions(Type type, Context ctx, TabbedWriter out) {
		// Default to the type-dependent options.
		ctx.invoke(genTypeDependentOptions, type, ctx, out);
	}

	public Integer genFieldTypeClassName(Type type, Context ctx, TabbedWriter out, Integer arrayDimensions) {
		ctx.invoke(genRuntimeTypeName, type, ctx, out, TypeNameKind.EGLImplementation);
		return arrayDimensions;
	}

	public void genAssignment(Type type, Context ctx, TabbedWriter out, Expression arg1, Expression arg2, String arg3) {
		// if the lhs is non-nullable but the rhs is nullable, we have a special case
		if (!arg1.isNullable() && arg2.isNullable()) {
			if (TypeUtils.isReferenceType(arg1.getType())) {
				ctx.invoke(genExpression, arg1, ctx, out);
				out.print(arg3 + " ");
				if (!(arg2 instanceof BoxingExpression)) {
					out.print("(");
					ctx.invoke(genRuntimeTypeName, arg2.getType(), ctx, out, TypeNameKind.JavaObject);
					ctx.invoke(genRuntimeTypeExtension, arg2.getType(), ctx, out);
					out.print(") ");
				}
				// we don't want to do a checkNullable on the temporary variables that are logically not nullable
				if (arg2 instanceof MemberName && ((MemberName) arg2).getId().startsWith(Constants.temporaryVariableLogicallyNotNullablePrefix))
					assignmentSource(arg1, arg2, ctx, out);
				else {
					out.print("org.eclipse.edt.javart.util.JavartUtil.checkNullable(");
					assignmentSource(arg1, arg2, ctx, out);
					out.print(")");
				}
				// check to see if we are unboxing RHS temporary variables (inout and out types only)
				if (CommonUtilities.isBoxedOutputTemp(arg2, ctx))
					out.print(".ezeUnbox()");
			} else {
				// if this is a well-behaved assignment, we can avoid the temporary
				if (org.eclipse.edt.gen.CommonUtilities.hasSideEffects(arg2, ctx)) {
					String temporary = ctx.nextTempName();
					ctx.invoke(genRuntimeTypeName, arg1.getType(), ctx, out, TypeNameKind.JavaObject);
					out.print(" " + temporary + " = ");
					assignmentSource(arg1, arg2, ctx, out);
					out.println(";");
					ctx.invoke(genExpression, arg1, ctx, out);
					out.print(arg3 + " ");
					if (!(arg2 instanceof BoxingExpression)) {
						out.print("(");
						ctx.invoke(genRuntimeTypeName, arg2.getType(), ctx, out, TypeNameKind.JavaObject);
						ctx.invoke(genRuntimeTypeExtension, arg2.getType(), ctx, out);
						out.print(") ");
					}
					out.print("org.eclipse.edt.javart.util.JavartUtil.checkNullable(" + temporary + ")");
				} else if (TypeUtils.isReferenceType(arg2.getType())) {
					ctx.invoke(genExpression, arg1, ctx, out);
					out.print(arg3);
					out.print("org.eclipse.edt.javart.util.JavartUtil.checkNullable(");
					assignmentSource(arg1, arg2, ctx, out);
					out.print(")");
				} else {
					ctx.invoke(genExpression, arg1, ctx, out);
					out.print(arg3 + " ");
					if (!(arg2 instanceof BoxingExpression)) {
						out.print("(");
						ctx.invoke(genRuntimeTypeName, arg2.getType(), ctx, out, TypeNameKind.JavaObject);
						ctx.invoke(genRuntimeTypeExtension, arg2.getType(), ctx, out);
						out.print(") ");
					}
					out.print("org.eclipse.edt.javart.util.JavartUtil.checkNullable(");
					assignmentSource(arg1, arg2, ctx, out);
					out.print(")");
				}
			}
		} else {
			ctx.invoke(genExpression, arg1, ctx, out);
			out.print(arg3);
			assignmentSource(arg1, arg2, ctx, out);
			// check to see if we are unboxing RHS temporary variables (inout and out types only)
			if (CommonUtilities.isBoxedOutputTemp(arg2, ctx))
				out.print(".ezeUnbox()");
		}
	}
	
	public static void assignmentSource( Expression lhs, Expression rhs, Context ctx, TabbedWriter out )
	{
		// Generate something special for the RHS of an assignment when:
		//  1) assigning bytes(x) to bytes(y), and x < y
		//  2) assigning bytes to bytes(x)
		//  3) assigning 'any as bytes(x)' to bytes(x)
		//  4) assigning 'any as bytes' to bytes
		//  5) assigning a function to a delegate
		Type lhsType = lhs.getType();
		Type rhsType = rhs.getType();
		if ( rhsType != null && TypeUtils.getTypeKind( rhsType ) == TypeUtils.TypeKind_BYTES
				&& lhsType != null && TypeUtils.getTypeKind( lhsType ) == TypeUtils.TypeKind_BYTES )
		{
			if ( ( lhsType instanceof SequenceType && rhsType instanceof SequenceType
						&& ((SequenceType)rhsType).getLength() < ((SequenceType)lhsType).getLength() )
					|| ( rhs instanceof AsExpression && ((AsExpression)rhs).getObjectExpr().getType() instanceof ParameterizableType
							&& TypeUtils.getTypeKind( ((AsExpression)rhs).getObjectExpr().getType() ) == TypeUtils.TypeKind_BYTES ) )
			{
				assignShorterBytesToLongerBytes( rhs, rhsType, lhs, lhsType, ctx, out );
			}
			else if ( rhs instanceof AsExpression 
					&& TypeUtils.getTypeKind( ((AsExpression)rhs).getObjectExpr().getType() ) == TypeUtils.TypeKind_ANY )
			{
				int lhsLength = lhsType instanceof SequenceType ? ((SequenceType)lhsType).getLength() : 0;

				ctx.invoke( genRuntimeTypeName, rhsType, ctx, out, TypeNameKind.EGLImplementation );
				out.print( ".ezeAssignFromAny(" );
				if ( lhsLength > 0 )
				{
					if ( lhs instanceof MemberName 
							&& ctx.get( "generating declaration of " + ((MemberName)lhs).getMember() + ((MemberName)lhs).getMember().hashCode() ) != null )
					{
						out.print( "new byte[" + lhsLength + ']' );
					}
					else
					{
						ctx.invoke( genExpression, lhs, ctx, out );
					}
					out.print( ", " );
					out.print( lhsLength );
				}
				else
				{
					out.print( "null" );
				}
				out.print( ", " );
				ctx.invoke( genExpression, rhs, ctx, out );
				out.print( ')' );
			}
			else
			{
				ctx.invoke( genExpression, rhs, ctx, out );
			}
		}
		else if ( lhsType instanceof Delegate 
				&& (rhs instanceof MemberName || rhs instanceof MemberAccess) 
				&& CommonUtilities.getMember( rhs ) instanceof Function )
		{
			String functionSig = ((Function)CommonUtilities.getMember( rhs )).getSignature();
			ctx.put( "Delegate_signature_for_function_" + functionSig, ((Delegate)lhsType).getTypeSignature() );
			ctx.invoke( genExpression, rhs, ctx, out );
			ctx.remove( "Delegate_signature_for_function_" + functionSig );
		}
		else
		{
			ctx.invoke( genExpression, rhs, ctx, out );
		}
	}

	static private void assignShorterBytesToLongerBytes( Expression rhs, Type rhsType, Expression lhs, Type lhsType, Context ctx, TabbedWriter out )
	{
		int lhsLength = ((SequenceType)lhsType).getLength();
		if ( rhs instanceof BytesLiteral )
		{
			StringBuilder value = new StringBuilder( ((BytesLiteral)rhs).getValue() );
			while ( value.length() / 2 < lhsLength )
			{
				value.append( "00" );
			}
			BytesLiteral newRHS = ctx.getFactory().createBytesLiteral();
			newRHS.setValue( value.toString() );
			ctx.invoke( genExpression, newRHS, ctx, out );
		}
		else
		{
			ctx.invoke( genRuntimeTypeName, rhsType, ctx, out, TypeNameKind.EGLImplementation );
			out.print( ".ezeAssignToLonger(" );
			if ( lhs instanceof MemberName 
					&& ctx.get( "generating declaration of " + ((MemberName)lhs).getMember() + ((MemberName)lhs).getMember().hashCode() ) != null )
			{
				out.print( "new byte[" + lhsLength + ']' );
			}
			else
			{
				ctx.invoke( genExpression, lhs, ctx, out );
			}
			out.print( ", " );
			out.print( lhsLength );
			out.print( ", " );
			ctx.invoke( genExpression, rhs, ctx, out );
			out.print( ')' );
		}
	}

	public void genIsaExpression(Type type, Context ctx, TabbedWriter out, IsAExpression arg) {
		if (arg.getObjectExpr().getType().getTypeSignature().equalsIgnoreCase(arg.getEType().getTypeSignature())) {
			if (arg.getObjectExpr().isNullable()) {
				out.print("(");
				ctx.invoke(genExpression, arg.getObjectExpr(), ctx, out);
				out.print(" == null ? false : true)");
			} else
				out.print("true");
		} else if (arg.getObjectExpr().getType().getClassifier() != null
			&& arg.getObjectExpr().getType().getClassifier().getTypeSignature().equalsIgnoreCase(arg.getEType().getTypeSignature())) {
			if (arg.getObjectExpr().isNullable()) {
				out.print("(");
				ctx.invoke(genExpression, arg.getObjectExpr(), ctx, out);
				out.print(" == null ? false : true)");
			} else
				out.print("true");
		} else if (arg.getObjectExpr().getType().getClassifier() != null && arg.getEType().getClassifier() != null
			&& arg.getObjectExpr().getType().getClassifier().getTypeSignature().equalsIgnoreCase(arg.getEType().getClassifier().getTypeSignature())) {
			out.print("false");
		} else if (ctx.mapsToPrimitiveType(arg.getEType())) {
			ctx.invoke(genRuntimeTypeName, arg.getEType(), ctx, out, TypeNameKind.EGLImplementation);
			out.print(".ezeIsa(");
			// do a callout to allow certain source types to decide to create a boxing expression
			ctx.invoke(genIsaExpressionBoxing, arg.getObjectExpr().getType(), ctx, out, arg);
			// then process the isa operation
			ctx.invoke(genExpression, arg.getObjectExpr(), ctx, out);
			ctx.invoke(genTypeDependentOptions, arg.getEType(), ctx, out);
			out.print(")");
		} else {
			out.print("org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeIsa(");
			ctx.invoke(genExpression, arg.getObjectExpr(), ctx, out);
			out.print(", ");
			ctx.invoke(genRuntimeClassTypeName, arg.getEType(), ctx, out, TypeNameKind.EGLImplementation);
			out.print(")");
		}
	}

	public void genConversionOperation(Type type, Context ctx, TabbedWriter out, AsExpression arg) {
		// check to see if a conversion is required
		if (arg.getConversionOperation() != null) {
			out.print(ctx.getNativeImplementationMapping((Classifier) arg.getConversionOperation().getContainer()) + ".");
			out.print(arg.getConversionOperation().getCaseSensitiveName());
			out.print("(");
			// do a callout to allow certain source types to decide to create a boxing expression
			ctx.invoke(genAsExpressionBoxing, arg.getObjectExpr().getType(), ctx, out, arg);
			// then process the conversion operation
			ctx.invoke(genExpression, arg.getObjectExpr(), ctx, out);
			ctx.invoke(genTypeDependentOptions, arg.getEType(), ctx, out);
			out.print(")");
		} else if (ctx.mapsToPrimitiveType(arg.getEType())) {
			ctx.invoke(genRuntimeTypeName, arg.getEType(), ctx, out, TypeNameKind.EGLImplementation);
			out.print(".ezeCast(");
			// do a callout to allow certain source types to decide to create a boxing expression
			ctx.invoke(genAsExpressionBoxing, arg.getObjectExpr().getType(), ctx, out, arg);
			// then process the conversion operation
			ctx.invoke(genExpression, arg.getObjectExpr(), ctx, out);
			ctx.invoke(genTypeDependentOptions, arg.getEType(), ctx, out);
			out.print(")");
		} else {
			out.print("org.eclipse.edt.runtime.java.eglx.lang.EAny.ezeCast(");
			ctx.invoke(genExpression, arg.getObjectExpr(), ctx, out);
			out.print(", ");
			ctx.invoke(genRuntimeClassTypeName, arg.getEType(), ctx, out, TypeNameKind.JavaImplementation);
			out.print(")");
		}
	}

	public void genAsExpressionBoxing(Type type, Context ctx, TabbedWriter out, AsExpression arg) {
		// do nothing
	}

	public void genIsaExpressionBoxing(Type type, Context ctx, TabbedWriter out, IsAExpression arg) {
		// do nothing
	}

	public void genContainerBasedInvocationBoxing(Type type, Context ctx, TabbedWriter out, InvocationExpression arg) {
		// do nothing
	}

	public void genInvocationArgumentBoxing(Type type, Context ctx, TabbedWriter out, InvocationExpression arg1, Integer arg2) {
		// do nothing
	}

	public void genReturnStatement(Type type, Context ctx, TabbedWriter out, ReturnStatement arg) {
		ctx.invoke(genReturnStatement, arg, ctx, out);
	}

	public void genBinaryExpression(Type type, Context ctx, TabbedWriter out, BinaryExpression arg) throws GenerationException {
		// if either side of this expression is nullable, or if either side is an array access,
		// or if there is no direct java operation, we need to use the runtime
		if ((arg.getLHS().isNullable() || arg.getRHS().isNullable()) || (arg.getLHS() instanceof ArrayAccess || arg.getRHS() instanceof ArrayAccess)
			|| CommonUtilities.getNativeJavaOperation(arg, ctx).length() == 0) {
			out.print(ctx.getNativeImplementationMapping((Type) arg.getOperation().getContainer()) + ".");
			out.print(CommonUtilities.getNativeRuntimeOperationName(arg));
			out.print("(");
			ctx.invoke(genExpression, arg.getLHS(), ctx, out, arg.getOperation().getParameters().get(0));
			out.print(", ");
			ctx.invoke(genExpression, arg.getRHS(), ctx, out, arg.getOperation().getParameters().get(1));
			out.print(")" + CommonUtilities.getNativeRuntimeComparisionOperation(arg));
		} else {
			ctx.invoke(genExpression, arg.getLHS(), ctx, out);
			out.print(CommonUtilities.getNativeJavaOperation(arg, ctx));
			ctx.invoke(genExpression, arg.getRHS(), ctx, out);
		}
	}

	public void genUnaryExpression(Type type, Context ctx, TabbedWriter out, UnaryExpression arg) throws GenerationException {
		// if either side of this expression is nullable
		if (arg.getExpression().isNullable()) {
			out.print(ctx.getNativeImplementationMapping((Type) arg.getOperation().getContainer()) + ".");
			out.print(CommonUtilities.getNativeRuntimeOperationName(arg));
			out.print("(");
			ctx.invoke(genExpression, arg.getExpression(), ctx, out, arg.getOperation().getParameters().get(0));
			out.print(")");
		} else {
			// we only need to check for minus sign and if found, we need to change it to -()
			if (arg.getOperator().equals(UnaryExpression.Op_NEGATE) || arg.getOperator().equals(UnaryExpression.Op_NOT) || arg.getOperator().equals(UnaryExpression.Op_BITWISENOT)) {
				out.print(arg.getOperator() + "(");
				ctx.invoke(genExpression, arg.getExpression(), ctx, out);
				out.print(")");
			} else
				ctx.invoke(genExpression, arg.getExpression(), ctx, out);
		}
	}

	public void genContainerBasedAssignment(Type type, Context ctx, TabbedWriter out, Assignment arg1, Field arg2) {
		ctx.invoke(genAssignment, arg1, ctx, out);
	}

	public void genContainerBasedArrayAccess(Type type, Context ctx, TabbedWriter out, ArrayAccess arg1, Field arg2) {
		ctx.invoke(genArrayAccess, arg1, ctx, out);
	}

	public void genContainerBasedMemberAccess(Type type, Context ctx, TabbedWriter out, MemberAccess arg1, Member arg2) {
		ctx.invoke(genMemberAccess, arg1, ctx, out);
	}

	public void genContainerBasedMemberName(Type type, Context ctx, TabbedWriter out, MemberName arg1, Member arg2) {
		ctx.invoke(genMemberName, arg1, ctx, out);
	}

	public void genContainerBasedNewExpression(Type type, Context ctx, TabbedWriter out, Expression arg) {
		ctx.invoke(genNewExpression, arg, ctx, out);
	}

	public void genContainerBasedInvocation(Type type, Context ctx, TabbedWriter out, Expression arg) {
		ctx.invoke(genInvocation, arg, ctx, out);
	}

	public void genTypeBasedAssignment(Type type, Context ctx, TabbedWriter out, Assignment arg) {
		String operator = "=";
		if (arg.getOperator() != null && arg.getOperator().length() > 0)
			operator = arg.getOperator();
		ctx.invoke(genAssignment, arg.getLHS(), ctx, out, arg.getRHS(), " " + CommonUtilities.getNativeJavaAssignment(operator) + " ");
	}
}
