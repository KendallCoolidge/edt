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
package org.eclipse.edt.gen.javascriptdev.templates;

import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.gen.javascript.CommonUtilities;
import org.eclipse.edt.gen.javascript.Context;
import org.eclipse.edt.gen.javascriptdev.Constants;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.Field;

public class FieldTemplate extends org.eclipse.edt.gen.javascript.templates.FieldTemplate {
	
	public void genGetVariablesEntry(Field field, Context ctx, TabbedWriter out) {
		out.print("{name: " + quoted(field.getId()) + ", value : ");
		
		Annotation property = CommonUtilities.getPropertyAnnotation(field);
		if (property != null && property.getValue("getMethod") != null) {
			// Catch errors so that we don't throw an exception when we're just trying to display values
			out.print("function(){try{return eze$$parent.");
			ctx.invoke(genGetter, field, ctx, out);
			out.print( ";}catch(e){return e;}}()" );
		}
		else {
			out.print("eze$$parent.");
			ctx.invoke(genName, field, ctx, out);
		}
		
		out.print(", type : \"");
		ctx.invoke(Constants.genDebugTypeInfo, field, ctx, out);
		out.print("\", jsName : \"");
		ctx.invoke(Constants.genDebugJSName, field, ctx, out);
		out.print( "\"}" );
	}
	
	@Override
	public void genDeclaration(Field field, Context ctx, TabbedWriter out) {
		super.genDeclaration(field, ctx, out);
		
		ctx.invoke(Constants.genAddLocalFunctionVariable, field, ctx, out);
	}
	
	public void genSetWidgetLocation(Field field, Boolean isLocalField, Context ctx, TabbedWriter out){
		if(CommonUtilities.isRUIWidget(field.getType())){
			Annotation annotation = field.getAnnotation(IEGLConstants.EGL_LOCATION);
			if (annotation != null){
				Integer offset = (Integer)annotation.getValue(IEGLConstants.EGL_PARTOFFSET);
				Integer length = (Integer)annotation.getValue(IEGLConstants.EGL_PARTLENGTH);
				
				out.print("egl.setWidgetLocation(");
				if(!isLocalField){
					out.print("this.");
				}
				ctx.invoke(genName, field, ctx, out);
				out.print(", '");
				ctx.invoke(genName, field, ctx, out);
				out.print("', ");
				out.print(offset.toString());
				out.print(", ");
				out.print(length.toString());
				out.print(", ");
				out.print(true);
				out.println(");");
			}
		}
	}
	
}
