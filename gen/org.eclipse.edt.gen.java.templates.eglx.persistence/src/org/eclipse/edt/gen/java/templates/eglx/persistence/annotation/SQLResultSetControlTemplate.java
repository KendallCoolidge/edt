/*******************************************************************************
 * Copyright © 2012, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.gen.java.templates.eglx.persistence.annotation;

import org.eclipse.edt.gen.java.CommonUtilities;
import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.gen.java.templates.JavaTemplate;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.AnnotationType;

public class SQLResultSetControlTemplate extends JavaTemplate {
	public void genSQLAnnotation(AnnotationType aType, Context ctx, TabbedWriter out, Annotation annot) {
		Object scrollablity = annot.getValue("scrollablity"); 
		Object concurrency = annot.getValue("concurrency"); 
		Object holdability = annot.getValue("holdability"); 
		if(concurrency != null || scrollablity != null || holdability != null){
			//	TYPE_FORWARD_ONLY, TYPE_SCROLL_INSENSITIVE, TYPE_SCROLL_SENSITIVE
			String id = CommonUtilities.getEnumerationName(scrollablity);
			if("TYPE_SCROLL_INSENSITIVE".equalsIgnoreCase(id)){
				out.print(", java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE");
			}
			else if("TYPE_SCROLL_SENSITIVE".equalsIgnoreCase(id)){
				out.print(", java.sql.ResultSet.TYPE_SCROLL_SENSITIVE");
				
			}
			else{
				out.print(", java.sql.ResultSet.TYPE_FORWARD_ONLY");
			}
			id = CommonUtilities.getEnumerationName(concurrency);
			if("CONCUR_UPDATABLE".equalsIgnoreCase(id)){
				out.print(", java.sql.ResultSet.CONCUR_UPDATABLE");
			}
			else{
				out.print(", java.sql.ResultSet.CONCUR_READ_ONLY");
			}
			if(holdability != null){
				id = CommonUtilities.getEnumerationName(holdability);
				if("CLOSE_CURSORS_AT_COMMIT".equalsIgnoreCase(id)){
					out.print(", java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT");
				}
				else{
					out.print(", java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT");
				}
			}
		}
	}

}
