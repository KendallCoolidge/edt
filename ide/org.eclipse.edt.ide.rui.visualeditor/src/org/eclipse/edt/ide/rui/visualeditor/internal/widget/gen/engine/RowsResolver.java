/*******************************************************************************
 * Copyright © 2010, 2011 IBM Corporation and others.
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

import org.eclipse.edt.ide.rui.visualeditor.internal.widget.gen.ComposeGenNode;
import org.eclipse.edt.ide.rui.visualeditor.internal.widget.gen.GenNode;

public class RowsResolver implements IGenVariableResolver {

	public void resolve(GenNode genNode) {
		if(genNode instanceof ComposeGenNode){
			ComposeGenNode composeGenNode = (ComposeGenNode)genNode;
			String template = composeGenNode.getTemplate();
			
			String newTemplate = template.replace(IGenVariableResolver.Rows, Integer.toString(composeGenNode.getChildren().size()));
			genNode.setTemplate(newTemplate);  
		}
	}

}
