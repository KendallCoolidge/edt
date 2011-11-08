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
import org.eclipse.edt.mof.egl.FunctionParameter;
import org.eclipse.edt.mof.egl.ParameterKind;

public class FunctionParameterTemplate extends JavaTemplate {

	public void genDeclaration(FunctionParameter decl, Context ctx, TabbedWriter out) {
		// write out the debug extension data
		CommonUtilities.generateSmapExtension(decl, ctx);
		if (org.eclipse.edt.gen.CommonUtilities.isBoxedParameterType(decl, ctx) && !decl.isConst()) {
			out.print("AnyBoxedObject<");
			ctx.invoke(genRuntimeTypeName, decl.getType(), ctx, out, TypeNameKind.JavaObject);
			out.print(">");
		} else
			ctx.invoke(genRuntimeTypeName, decl, ctx, out, TypeNameKind.JavaPrimitive);
		out.print(" ");
		ctx.invoke(genName, decl, ctx, out);
	}
	public void genFunctionParametersSignature(FunctionParameter parameter, Context ctx, TabbedWriter out) {
		out.print("@FunctionParameter(name=\"");
		ctx.invoke(genName, parameter, ctx, out);
		out.print("\", kind=FunctionParameterKind.");
		out.print(convert(parameter.getParameterKind()));
		out.print(", parameterType=");
		Integer arrayDimension = (Integer)ctx.invoke(genFieldTypeClassName, parameter.getType(), ctx, out, new Integer(0));
		out.print(".class, asOptions={");
		ctx.invoke(genJsonTypeDependentOptions, parameter.getType(), ctx, out);
		out.print("}");
		if(arrayDimension > 0 ){
			out.print(", arrayDimensions=");
			out.print(arrayDimension.toString());
		}
		out.print(")");
	}
	private String convert(ParameterKind parameterKind){
		if(ParameterKind.PARM_IN.equals(parameterKind)){
			return "IN";
		}
		else if(ParameterKind.PARM_OUT.equals(parameterKind)){
			return "OUT";
		}
		else if(ParameterKind.PARM_INOUT.equals(parameterKind)){
			return "INOUT";
		}
		else{
			return "RETURN";
		}
	}
	
	public void genRuntimeClassTypeName( FunctionParameter parameter, Context ctx, TabbedWriter out, TypeNameKind kind )
	{
		ctx.invoke( genRuntimeClassTypeName, parameter.getType(), ctx, out, kind );
	}
}
