/*******************************************************************************
 * Copyright © 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.templates.parts;

public class RecordType extends Type {
	
	public boolean isReferenceType() {
		return true;
	}
	
	public Object clone() {
		RecordType type = new RecordType();
		type.name = this.name;
		type.isNullable = this.isNullable;
		type.setAnnotations(this.getAnnotations());
		return type;
	}
}
