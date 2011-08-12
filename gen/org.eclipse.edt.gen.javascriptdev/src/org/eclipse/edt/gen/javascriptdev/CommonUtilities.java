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
package org.eclipse.edt.gen.javascriptdev;

import org.eclipse.edt.mof.egl.ConstantField;
import org.eclipse.edt.mof.egl.Container;
import org.eclipse.edt.mof.egl.FunctionMember;
import org.eclipse.edt.mof.egl.LogicAndDataPart;
import org.eclipse.edt.mof.egl.Member;

public class CommonUtilities {
	
	private CommonUtilities() {
		// No instances.
	}
	
	public static boolean shouldDebug(Member member) {
		if (member instanceof ConstantField || member.getId().startsWith(org.eclipse.edt.gen.Constants.temporaryVariablePrefix) || member.getId().startsWith("eze")) {
			return false;
		}
		
		Container container = member.getContainer();
		return container == null || container instanceof FunctionMember || container instanceof LogicAndDataPart;
	}
}
