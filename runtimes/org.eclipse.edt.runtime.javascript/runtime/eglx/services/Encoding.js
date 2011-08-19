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
egl.defineClass('eglx.services', "Encoding", "egl.lang", "Enumeration",{
		"constructor": function(valueIn) {
			this.value = valueIn;
		}
	}
);
egl.eglx.services.Encoding['NONE'] = new egl.eglx.services.Encoding(1);
egl.eglx.services.Encoding['JSON'] = new egl.eglx.services.Encoding(2);
egl.eglx.services.Encoding['XML'] = new egl.eglx.services.Encoding(3);
egl.eglx.services.Encoding['_FORM'] = new egl.eglx.services.Encoding(4);
egl.eglx.services.Encoding['USE_CONTENTTYPE'] = new egl.eglx.services.Encoding(5);
;
