/*******************************************************************************
 * Copyright © 2010, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.rui.visualeditor.internal.widget.gen.engine;

import org.eclipse.edt.ide.rui.visualeditor.internal.widget.gen.GenNode;

public class ShortBindingNameResolver implements IGenVariableResolver {

	public void resolve(GenNode genNode) {
		String template = genNode.getTemplate();
		String bindingName = genNode.getInsertDataNode().getBindingName();
		String shortBindingName = bindingName.substring(bindingName.lastIndexOf(".")+1, bindingName.length());
		String newTemplate = template.replace(IGenVariableResolver.ShortBindingName, shortBindingName);
		genNode.setTemplate(newTemplate); 
	}

}
