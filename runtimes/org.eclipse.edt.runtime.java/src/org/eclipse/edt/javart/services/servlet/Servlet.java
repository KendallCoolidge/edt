/*******************************************************************************
 * Copyright © 2006, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.javart.services.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.edt.javart.AnyBoxedObject;
import org.eclipse.edt.javart.Constants;
import org.eclipse.edt.javart.JEERunUnit;
import org.eclipse.edt.javart.RunUnit;
import org.eclipse.edt.javart.json.ParseException;
import org.eclipse.edt.javart.messages.Message;
import org.eclipse.edt.javart.resources.StartupInfo;
import org.eclipse.edt.javart.resources.Trace;

import eglx.http.Request;
import eglx.http.Response;
import eglx.http.HttpUtilities;
import eglx.json.JsonUtilities;
import eglx.lang.AnyException;
import eglx.lang.EDictionary;
import eglx.services.ServiceKind;
import eglx.services.ServiceUtilities;



/**
 * Servlet implementation class for Servlet: AjaxProxyServlet
 *
 */
 public abstract class Servlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;

	private Trace tracer;
	
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
	}
	
	protected Trace tracer()
	{
		if( tracer == null )
		{
			tracer = getRunUnit().getTrace();
		}
		return tracer;
	}
	
	protected abstract String servletName();
	
	protected RunUnit getRunUnit()
	{
		RunUnit ru = org.eclipse.edt.javart.Runtime.getRunUnit();
		if ( ru == null )
		{
			ru = new JEERunUnit( new StartupInfo( servletName(), "", null ) );
			org.eclipse.edt.javart.Runtime.setThreadRunUnit(ru);
		}
		return ru;
	}
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public Servlet() 
	{
		super();
	}   	 	
	
	protected void trace( String msg )
	{
		if ( tracer().traceIsOn( Trace.GENERAL_TRACE ) ) 
		{
			tracer().put( msg );
		}
	}
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest httpServletReq, HttpServletResponse httpServletRes) throws ServletException, IOException 
	{
		doHttp(httpServletReq, httpServletRes);
	}

//	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		doHttp(req, resp);
	}
	
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		doHttp(req, resp);
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException 
	{
		doHttp(req, resp);
	}

	private void doHttp(HttpServletRequest httpServletReq, HttpServletResponse httpServletRes)
	{
		getRunUnit();
		HttpSession session = httpServletReq.getSession();
		if ( tracer().traceIsOn( Trace.GENERAL_TRACE ) ){
			trace("HttpServletRequest.getContextPath():" + httpServletReq.getContextPath());
			trace("HttpServletRequest.getPathInfo():" + httpServletReq.getPathInfo());
		}
		String url = null;
		ServiceKind serviceKind = null;
		Request request = null;
		Response response = null;
		try
		{
			if ( tracer().traceIsOn( Trace.GENERAL_TRACE ) ){
				trace( servletName() + " sessionId:" + (session == null ? "null" : session.getId()) );
			}
			request = ServletUtilities.createNewRequest( httpServletReq );
			if(request != null)
			{
				url = request.uri; 
				//debug("server@"+portNumber+": url="+url);
				if ( tracer().traceIsOn( Trace.GENERAL_TRACE ) ) 
				{
					tracer().put( "REQUEST:" );
					tracer().put( "    URL:" + url != null ? url : "null");
					tracer().put( "    content:" + request.body != null ? request.body : "null");
					tracer().put( "    httpMethod:" + request.method != null ? HttpUtilities.httpMethodToString(request.method) : "null" );
				}
				response = processRequest( url, request, httpServletReq );
			}
		}
		catch(Throwable t)
		{
			response = buildResponse( getRunUnit(), httpServletReq.getRequestURL().toString(), t, 
					request != null && request.body != null ? request.body : "", serviceKind);
		}
		String content = response.body;
		log(content, response);
		write( httpServletRes, content, response.headers, response.status );
	}   	
	
	protected abstract Response processRequest(String url, Request request, HttpServletRequest httpServletReq) throws Exception;

	private Response buildResponse( RunUnit runUnit, String url,  Throwable t, String requestContent, ServiceKind serviceKind )
	{
		
		Response outerResponse = new Response();
		//handle as inner exception
		AnyException jrte;

		if( t instanceof ParseException )
		{
			jrte = ServiceUtilities.buildServiceInvocationException(Message.SOA_E_WS_REST_BAD_CONTENT, new String[]{requestContent}, t, serviceKind );
		}
		else if( t instanceof IOException )
		{
			//handle as outer exception
			jrte = ServiceUtilities.buildServiceInvocationException(Message.SOA_E_WS_PROXY_COMMUNICATION, new String[]{url}, t, serviceKind );
		}
		else
		{
			jrte = ServiceUtilities.buildServiceInvocationException(Message.SOA_E_WS_PROXY_UNIDENTIFIED, new Object[0], t, serviceKind );
		}
		Response innerResponse = new Response();
		innerResponse.body = eglx.json.JsonUtilities.createJsonAnyException(jrte);
		innerResponse.status = HttpUtilities.HTTP_STATUS_FAILED;
		innerResponse.statusMessage = HttpUtilities.HTTP_STATUS_MSG_FAILED;
		ServletUtilities.setBody(outerResponse, innerResponse);
		outerResponse.status = HttpUtilities.HTTP_STATUS_OK;
		outerResponse.statusMessage = HttpUtilities.HTTP_STATUS_MSG_OK;
		return outerResponse;
	}
	private void log( String content, Response response )
	{
/*		String str;
		if( response.getBody() instanceof Response )
		{
			str = "Multi-Level";
		}
		else
		{
			str = "Single-Level";
		}
		System.out.println( str + "\n      " + content );
		*/
	}
	private void write( HttpServletResponse httpServletRes, String content, EDictionary headers, int status )
	{
		try
		{
			boolean addContentType = true;
			if(headers != null){
				for ( Iterator<Map.Entry<String, Object>> iter = headers.entrySet().iterator(); iter.hasNext(); )
				{
					Map.Entry<String, Object> entry = iter.next();
					Object entryValue = entry.getValue();
					if(entryValue instanceof AnyBoxedObject<?>){
						entryValue = ((AnyBoxedObject<?>)entryValue).ezeUnbox();
					}
					if(entry.getKey().equalsIgnoreCase(HttpUtilities.CONTENT_TYPE_KEY)){
						addContentType = false;
					}
					if(entryValue != null){
						httpServletRes.setHeader( entry.getKey(), entryValue.toString() );
					}
				}
			}
			if(addContentType){
				httpServletRes.setContentType( HttpUtilities.getContentType(HttpUtilities.CONTENT_TEXT_KEY) );
			}
			if( status == HttpUtilities.HTTP_STATUS_OK )
			{
				PrintWriter pw = httpServletRes.getWriter();
				pw.write( content );
				pw.flush();
				httpServletRes.setStatus( status );
				httpServletRes.flushBuffer();
			}
			else
			{
				try
				{
					httpServletRes.setHeader( JsonUtilities.JSON_RPC_ERROR_NAME_VALUE, URLEncoder.encode( content, ServiceUtilities.UTF8 ) );
				}
				catch( Exception e ){}
				httpServletRes.sendError( status );
			}
		}
		catch( Throwable t2 ){t2.printStackTrace();}

	}

}
