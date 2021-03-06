/*******************************************************************************
 * Copyright © 2011, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
egl.defineClass('eglx.ui.rui', "MappingKind", "eglx.lang", "Enumeration",{
		"constructor": function(valueIn) {
			this.value = valueIn;
		}
	}
);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_ALL'] = new egl.eglx.ui.rui.MappingKind(100);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_STRING'] = new egl.eglx.ui.rui.MappingKind(101);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_DATE'] = new egl.eglx.ui.rui.MappingKind(102);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_TIME'] = new egl.eglx.ui.rui.MappingKind(103);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_TIMESTAMP'] = new egl.eglx.ui.rui.MappingKind(104);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_BIGINT'] = new egl.eglx.ui.rui.MappingKind(105);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_BIN'] = new egl.eglx.ui.rui.MappingKind(106);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_DECIMAL'] = new egl.eglx.ui.rui.MappingKind(107);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_FLOAT'] = new egl.eglx.ui.rui.MappingKind(108);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_INT'] = new egl.eglx.ui.rui.MappingKind(109);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_NUM'] = new egl.eglx.ui.rui.MappingKind(110);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_MONEY'] = new egl.eglx.ui.rui.MappingKind(111);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_BOOLEAN'] = new egl.eglx.ui.rui.MappingKind(112);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_SMALLFLOAT'] = new egl.eglx.ui.rui.MappingKind(113);
egl.eglx.ui.rui.MappingKind['TYPE_PRIMITIVE_SMALLINT'] = new egl.eglx.ui.rui.MappingKind(114);
egl.eglx.ui.rui.MappingKind['TYPE_RECORD_ALL'] = new egl.eglx.ui.rui.MappingKind(200);
egl.eglx.ui.rui.MappingKind['TYPE_RECORD_SIMPLE'] = new egl.eglx.ui.rui.MappingKind(201);
egl.eglx.ui.rui.MappingKind['TYPE_RECORD_EMBED_RECORD'] = new egl.eglx.ui.rui.MappingKind(202);
egl.eglx.ui.rui.MappingKind['TYPE_RECORD_WITH_PRIMITIVE_ARRAY'] = new egl.eglx.ui.rui.MappingKind(203);
egl.eglx.ui.rui.MappingKind['TYPE_RECORD_WITH_RECORD_ARRAY'] = new egl.eglx.ui.rui.MappingKind(204);
;
