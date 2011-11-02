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
package org.eclipse.edt.gen.java.templates.eglx.lang;

import java.util.List;

import org.eclipse.edt.gen.java.CommonUtilities;
import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.gen.java.templates.JavaTemplate;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.ArrayType;
import org.eclipse.edt.mof.egl.Expression;
import org.eclipse.edt.mof.egl.InvocationExpression;
import org.eclipse.edt.mof.egl.NewExpression;
import org.eclipse.edt.mof.egl.TimestampType;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.utils.TypeUtils;

public class ListTypeTemplate extends JavaTemplate 
{
	public void genContainerBasedNewExpression( Type type, Context ctx, TabbedWriter out, NewExpression expr )
	{
		if ( expr.getArguments() == null || expr.getArguments().size() == 0 )
		{
			genInstantiation( type, ctx, out );
		}
		else
		{
			ctx.invoke( genRuntimeTypeName, type.getClassifier(), ctx, out, TypeNameKind.EGLImplementation );
			out.print( ".ezeNew(" );
			ctx.invoke( genExpression, expr.getArguments().get( 0 ), ctx, out );
			out.print( ',' );
			factory( (ArrayType)expr.getType(), ctx, out, expr.getArguments(), 1 );
			out.print( ')' );
		}
	}

	public void genInstantiation( Type type, Context ctx, TabbedWriter out )
	{
		Type elementType = ((ArrayType)type).getElementType();		
		if ( elementType instanceof ArrayType )
		{
			out.print( "((" );
			ctx.invoke( genRuntimeTypeName, type.getClassifier(), ctx, out, TypeNameKind.JavaObject );
			out.print( ")new java.util.ArrayList())" );
		}
		else
		{
			ctx.invoke( genRuntimeTypeName, type.getClassifier(), ctx, out, TypeNameKind.EGLImplementation );
			out.print( ".ezeNew(" );
			ctx.invoke( genRuntimeTypeName, elementType.getClassifier(), ctx, out, TypeNameKind.JavaObject );
			out.print( ".class)" );
		}
	}

	public void genContainerBasedInvocation( Type type, Context ctx, TabbedWriter out, InvocationExpression expr )
	{
		ctx.invoke( genRuntimeTypeName, type, ctx, out, TypeNameKind.EGLImplementation );
		out.print( '.' );
		ctx.invoke( genName, expr.getTarget(), ctx, out );
		out.print( '(' );
		ctx.invoke( genExpression, expr.getQualifier(), ctx, out );

		// Special cases: 1. Pass a factory to resize so it can create new elements.
		// 2. We may need to copy the first argument to appendElement, insertElement,
		// and setElement.
		if ( expr.getId().equalsIgnoreCase( "resize" ) )
		{
			out.print( ", " );
			ctx.invoke( genExpression, expr.getArguments().get( 0 ), ctx, out );
			out.print( ", " );
			factory( (ArrayType)expr.getQualifier().getType(), ctx, out, null, 0 );
			out.print( ')' );
		}
		else if ( expr.getId().equalsIgnoreCase( "appendElement" ) )
		{
			out.print( ", " );
			Expression arg0 = expr.getArguments().get( 0 );
			if ( arg0.getType() == null || TypeUtils.isReferenceType( arg0.getType() ) 
					|| ctx.mapsToPrimitiveType( arg0.getType() ) )
			{
				ctx.invoke( genExpression, arg0, ctx, out );
			}
			else
			{
				CommonUtilities.genEzeCopyTo(arg0, ctx, out);
				ctx.invoke( genExpression, arg0, ctx, out );
				out.print( ", " );
				ctx.invoke( genInstantiation, arg0.getType(), ctx, out );
				out.print( ')' );
			}
			out.print( ')' );
		}
		else if ( expr.getId().equalsIgnoreCase( "insertElement" )
					|| expr.getId().equalsIgnoreCase( "setElement" ) )
		{
			out.print( ", " );
			Expression arg0 = expr.getArguments().get( 0 );
			if ( arg0.getType() == null || TypeUtils.isReferenceType( arg0.getType() ) 
					|| ctx.mapsToPrimitiveType( arg0.getType() ) )
			{
				ctx.invoke( genExpression, arg0, ctx, out );
			}
			else
			{
				CommonUtilities.genEzeCopyTo(arg0, ctx, out);
				ctx.invoke( genExpression, arg0, ctx, out );
				out.print( ", " );
				ctx.invoke( genInstantiation, arg0.getType(), ctx, out );
				out.print( ')' );
			}
			out.print( ", " );
			ctx.invoke( genExpression, expr.getArguments().get( 1 ), ctx, out );
			out.print( ')' );
		}
		else
		{
			if ( expr.getArguments() != null && expr.getArguments().size() > 0 )
			{
				out.print( ", " );
				ctx.foreach( expr.getArguments(), ',', genExpression, ctx, out );
			}
			out.print( ')' );
		}
	}
	
	protected void factory( ArrayType arrayType, Context ctx, TabbedWriter out, 
			List<Expression> dimensionSizes, int whichDimension )
	{
		if ( arrayType.elementsNullable() )
		{
			out.print( "null" );
		}
		else
		{
			Type elementType = arrayType.getElementType();
			if ( elementType instanceof ArrayType )
			{
				ArrayType elementArrayType = (ArrayType)elementType;
				out.print( "new org.eclipse.edt.runtime.java.eglx.lang.EList.ListFactory<" );
				ctx.invoke( genRuntimeTypeName, elementArrayType.getElementType(), ctx, out, TypeNameKind.JavaObject );
				out.print( ">(" );
				if ( dimensionSizes == null )
				{
					out.print( 0 );
				}
				else
				{
					ctx.invoke( genExpression, dimensionSizes.get( whichDimension ), ctx, out );
				}
				out.print( ',' );
				factory( elementArrayType, ctx, out, dimensionSizes, whichDimension + 1 );
				out.print( ')' );
			}
			else
			{
				switch ( TypeUtils.getTypeKind( elementType ) )
				{
					case TypeUtils.TypeKind_INT:
						out.print( "org.eclipse.edt.runtime.java.eglx.lang.EList.IntFactory" );
						break;
					case TypeUtils.TypeKind_SMALLINT:
						out.print( "org.eclipse.edt.runtime.java.eglx.lang.EList.SmallintFactory" );
						break;
					case TypeUtils.TypeKind_BIGINT:
						out.print( "org.eclipse.edt.runtime.java.eglx.lang.EList.BigintFactory" );
						break;
					case TypeUtils.TypeKind_FLOAT:
						out.print( "org.eclipse.edt.runtime.java.eglx.lang.EList.FloatFactory" );
						break;
					case TypeUtils.TypeKind_SMALLFLOAT:
						out.print( "org.eclipse.edt.runtime.java.eglx.lang.EList.SmallfloatFactory" );
						break;
					case TypeUtils.TypeKind_DECIMAL:
						out.print( "org.eclipse.edt.runtime.java.eglx.lang.EList.DecimalFactory" );
						break;
					case TypeUtils.TypeKind_BOOLEAN:
						out.print( "org.eclipse.edt.runtime.java.eglx.lang.EList.BooleanFactory" );
						break;
					case TypeUtils.TypeKind_STRING:
						out.print( "org.eclipse.edt.runtime.java.eglx.lang.EList.StringFactory" );
						break;
					case TypeUtils.TypeKind_DATE:
						out.print( "org.eclipse.edt.runtime.java.eglx.lang.EList.DateFactory" );
						break;
					case TypeUtils.TypeKind_TIMESTAMP:
						TimestampType tsType = (TimestampType)elementType;
						String pattern = "yyyyMMddhhmmss";
						if ( tsType.getPattern() != null && !tsType.getPattern().equalsIgnoreCase( "null" ) )
						{
							pattern = tsType.getPattern();
						}
						out.print( "new org.eclipse.edt.runtime.java.eglx.lang.EList.TimestampFactory(" );
						ctx.invoke( genRuntimeTypeName, tsType, ctx, out, TypeNameKind.EGLImplementation );
						out.print( '.' );
						out.print( TimestampTypeTemplate.getStartPattern( pattern ) );
						out.print( ',' );
						ctx.invoke( genRuntimeTypeName, tsType, ctx, out, TypeNameKind.EGLImplementation );
						out.print( '.' );
						out.print( TimestampTypeTemplate.getEndPattern( pattern ) );
						out.print( ')' );
						break;
					default:
						out.print( "new org.eclipse.edt.runtime.java.eglx.lang.EList.ElementFactory<" );
						ctx.invoke( genRuntimeTypeName, elementType, ctx, out, TypeNameKind.EGLInterface );
						out.print( ">(" );
						ctx.invoke( genRuntimeClassTypeName, elementType, ctx, out, TypeNameKind.EGLImplementation );
						out.print( ")" );
				}
			}
		}
	}
}
