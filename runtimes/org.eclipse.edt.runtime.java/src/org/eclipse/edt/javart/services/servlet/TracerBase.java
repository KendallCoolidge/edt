/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.edt.javart.services.servlet;

import org.eclipse.edt.javart.RunUnit;
import org.eclipse.edt.javart.resources.Trace;

public class TracerBase {
	private Trace tracer;
	final private RunUnit runUnit;
	
	protected TracerBase(RunUnit runUnit) {
		this.runUnit = runUnit;
	}
	protected Trace tracer()
	{
		if( tracer == null )
		{
			tracer = getRunUnit().getTrace();
		}
		return tracer;
	}
	
	protected boolean trace()
    {
		return tracer().traceIsOn( Trace.GENERAL_TRACE ); 
    }
	protected RunUnit getRunUnit() {
		return runUnit;
	}

}
