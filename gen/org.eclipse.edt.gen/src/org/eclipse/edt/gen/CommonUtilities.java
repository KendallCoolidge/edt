package org.eclipse.edt.gen;

import org.eclipse.edt.gen.EGLMessages.EGLMessage;
import org.eclipse.edt.mof.EObject;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.DelegateInvocation;
import org.eclipse.edt.mof.egl.Expression;
import org.eclipse.edt.mof.egl.FunctionInvocation;
import org.eclipse.edt.mof.egl.FunctionParameter;
import org.eclipse.edt.mof.egl.InvocationExpression;
import org.eclipse.edt.mof.egl.ParameterKind;
import org.eclipse.edt.mof.egl.QualifiedFunctionInvocation;
import org.eclipse.edt.mof.egl.utils.TypeUtils;
import org.eclipse.edt.mof.impl.AbstractVisitor;

public class CommonUtilities {

	public static boolean hasLocalVariableSideEffects(Expression expr, EglContext ctx) {
		return (new CheckLocalVariableSideEffects()).checkLocalVariableSideEffect(expr, ctx);
	}

	public static class CheckLocalVariableSideEffects extends AbstractVisitor {
		boolean has = false;
		EglContext ctx;

		public boolean checkLocalVariableSideEffect(Expression expr, EglContext ctx) {
			this.ctx = ctx;
			disallowRevisit();
			setReturnData(false);
			expr.accept(this);
			return (Boolean) getReturnData();
		}

		public boolean visit(EObject obj) {
			return false;
		}

		public boolean visit(Expression expr) {
			if (has)
				return false;
			return true;
		}

		public boolean visit(FunctionInvocation expr) {
			processInvocation(expr);
			return false;
		}

		public boolean visit(DelegateInvocation expr) {
			processInvocation(expr);
			return false;
		}

		public boolean visit(QualifiedFunctionInvocation expr) {
			processInvocation(expr);
			return false;
		}

		private void processInvocation(InvocationExpression object) {
			// we need to scan the function arguments for any conditions that require temporary variables to be set
			// up. Things like IN args, INOUT args with java primitives, OUT arg initialization, etc.
			for (int i = 0; i < object.getTarget().getParameters().size(); i++) {
				if (isArgumentToBeAltered(object.getTarget().getParameters().get(i), object.getArguments().get(i), ctx)) {
					has = true;
					setReturnData(has);
					break;
				}
			}
			return;
		}
	}

	public static boolean isArgumentToBeAltered(FunctionParameter parameter, Expression expression, EglContext ctx) {
		if (parameter.getParameterKind() == ParameterKind.PARM_IN) {
			// if the parameter is reference then do not make a temporary
			if (TypeUtils.isReferenceType(parameter.getType()))
				return false;
			// if the argument and parameter types mismatch, or if nullable, or not java primitive, then create a
			// temporary
			if (!parameter.getType().equals(expression.getType()) || parameter.isNullable() || expression.isNullable()
				|| !ctx.mapsToPrimitiveType(parameter.getType()))
				// if the parameter is a const then we should not make a copy
				return !parameter.isConst();
			return false;
		} else {
			// if the parameter is a const we should not make a copy
			return isBoxedParameterType(parameter, ctx) && !parameter.isConst();
		}
	}

	public static boolean isBoxedParameterType(FunctionParameter parameter, EglContext ctx) {
		if (parameter.getParameterKind() == ParameterKind.PARM_INOUT) {
			if (TypeUtils.isReferenceType(parameter.getType()))
				return true;
			else if (ctx.mapsToPrimitiveType(parameter.getType()))
				return true;
			else if (parameter.isNullable())
				return true;
		} else if (parameter.getParameterKind() == ParameterKind.PARM_OUT)
			return true;
		return false;
	}

	public static Annotation includeEndOffset(Annotation annotation, EglContext ctx) {
		if (annotation != null) {
			annotation.addAnnotation(ctx.getFactory().createAnnotation(EGLMessage.IncludeEndOffset));
		}
		return annotation;
	}
}
