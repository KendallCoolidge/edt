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
package org.eclipse.edt.javart.services.servlet.rest.rpc;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.edt.javart.Constants;
import org.eclipse.edt.javart.messages.Message;
import org.eclipse.edt.javart.resources.Trace;
import org.eclipse.edt.javart.services.servlet.JsonRpcInvoker;
import org.eclipse.edt.javart.services.servlet.Servlet;

import eglx.http.HttpMethod;
import eglx.http.HttpRequest;
import eglx.http.HttpResponse;
import eglx.http.HttpUtilities;
import eglx.json.JsonLib;
import eglx.json.JsonUtilities;
import eglx.services.ServiceInvocationException;
import eglx.services.ServiceKind;
import eglx.services.ServiceUtilities;



/**
 * Servlet implementation class for Servlet: AjaxProxyServlet
 *
 */
 public class ServiceServlet extends Servlet {
	private static final long serialVersionUID = Constants.SERIAL_VERSION_UID;
	static final String SERVICE_SERVLET = "EGL REST Service servlet";
	protected String contextRoot;

	protected RestServiceProjectInfo restServiceProjectInfo;

	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);
		contextRoot = config.getServletContext().getContextPath();
		while(contextRoot.charAt(0) == '/'){
			contextRoot = contextRoot.substring(1);
		}
		traceInfos();
	}
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public ServiceServlet() 
	{
		super();
	}   	 	
	
	@Override
	protected String servletName() {
		return SERVICE_SERVLET;
	}

	protected RestServiceProjectInfo restServiceProjectInfo(){
		if(restServiceProjectInfo == null){
			restServiceProjectInfo = RestRpcUtilities.getRestServiceInfo( contextRoot + "-uri.xml" );
		}
		return restServiceProjectInfo;
	}

	@Override
	protected HttpResponse processRequest(String urlString, HttpRequest request, HttpServletRequest httpServletReq) throws Exception {
		HttpResponse response = null;
		try
		{
			if( HttpMethod.POST.equals(request.getMethod()))
			{
				if ( tracer().traceIsOn( Trace.GENERAL_TRACE ) ){
					tracer().put( "this is an EGL REST RPC service" );
				}
				String pathInfo = null;
				pathInfo = httpServletReq.getPathInfo();
				
				if( pathInfo == null || pathInfo.length() == 0)
				{
					URL url;
					String path;
					try
					{
						url = new URL(urlString);
						path = url.getPath();
					}
					catch( MalformedURLException mfurl )
					{
						path = urlString;
					}
					int contextRootStart = path.indexOf( '/' );
					int contextRootEnd = path.indexOf( '/', contextRootStart + 1 );
					int serviceIdx = path.indexOf( '/', contextRootEnd + 1 );
					if( serviceIdx == -1 )
					{
						serviceIdx = path.length();
					}
					pathInfo = path.substring(serviceIdx);
				}
				
				if( restServiceProjectInfo() != null )
				{
					RestServiceProjectInfo.ServiceFunctionInfo serviceFunctionInfo = restServiceProjectInfo().getServiceFunctionInfo(pathInfo, request.getMethod() );
					if ( tracer().traceIsOn( Trace.GENERAL_TRACE ) && serviceFunctionInfo != null ){
						tracer().put( "invoking service " + serviceFunctionInfo.getClassName() );
						tracer().put( "    request encoding:" + String.valueOf(serviceFunctionInfo.getInEncoding()) );
						tracer().put( "    response encoding:" + String.valueOf(serviceFunctionInfo.getOutEncoding()) );
						tracer().put( "    hostProgramService?:" + String.valueOf(serviceFunctionInfo.isHostProgramService()) );
						tracer().put( "    body:" + request.getBody() == null ? "null" : request.getBody() );
					}
					response = new HttpResponse();
					if( serviceFunctionInfo != null )
					{
						JsonRpcInvoker invoker = new JsonRpcInvoker(serviceFunctionInfo.getClassName(), ServiceKind.REST);
						response = invoker.invoke(request);
						if ( tracer().traceIsOn( Trace.GENERAL_TRACE ) ){
							tracer().put( "returned from service" + response == null ? "null" : JsonLib.convertToJSON(response) );
						}
					}
					else
					{
						throw ServiceUtilities.buildServiceInvocationException(Message.SOA_E_WS_REST_NO_SERVICE, new String[] {urlString}, null, ServiceKind.WEB );
					}
				}
			}
			else
			{
				throw ServiceUtilities.buildServiceInvocationException(Message.SOA_E_WS_REST_WRONG_HTTP_FUNCTION, new String[] {HttpUtilities.httpMethodToString(request.getMethod())}, null, ServiceKind.WEB );
			}
		}
		catch(ServiceInvocationException sie )
		{
			if( response == null ){
				response = new HttpResponse();
			}
			response.setBody( JsonUtilities.createJsonAnyException(sie) );
			response.setStatus( HttpUtilities.HTTP_STATUS_FAILED );
			response.setStatusMessage( "FAILED" );
		}
		finally
		{
		}
		return response;
	}
	protected boolean resultContainsError( String result )
	{
		return result.indexOf( "{\"error\" : {" ) != -1;
	}
	private void traceInfos()
	{
		if( tracer().traceIsOn( Trace.GENERAL_TRACE ) )
		{
			StringBuilder buf = new StringBuilder();
			buf.append( "EGL REST service servlet " + contextRoot + " starting" + '\n' );
			buf.append( restServiceProjectInfo().toString() );
			trace( buf.toString() );
		}
	}
}
