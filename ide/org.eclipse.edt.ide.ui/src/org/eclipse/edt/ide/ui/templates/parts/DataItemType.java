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
package org.eclipse.edt.ide.ui.templates.parts;

public class DataItemType extends Type {
	
	public Object clone() {
		DataItemType type = new DataItemType();
		type.name = this.name;
		type.isNullable = this.isNullable;
		type.setAnnotations(this.getAnnotations());
		return type;
	}
}
