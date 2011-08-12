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

import org.eclipse.edt.gen.EGLMessages.EGLMessage;
import org.eclipse.edt.gen.java.Constants;
import org.eclipse.edt.gen.java.Context;
import org.eclipse.edt.mof.EObject;

public class EObjectTemplate extends JavaTemplate {

	public void preGen(EObject object, Context ctx) {
		String[] details = new String[] { object.getEClass().getETypeSignature() };
		EGLMessage message = EGLMessage.createEGLMessage(ctx.getMessageMapping(), EGLMessage.EGL_ERROR_MESSAGE, Constants.EGLMESSAGE_UNSUPPORTED_ELEMENT,
			object, details, ctx.getLastStatementLocation());
		ctx.getMessageRequestor().addMessage(message);
	}
}
