/*******************************************************************************
 * Copyright © 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.rui.visualeditor.internal.widget;

/**
 * Each descriptor group has a list of widget descriptors. 
 */
public interface IWidgetDescriptorRegistryListener {

	/**
	 * Called when the widget descriptor registry has changed.
	 */
	public void widgetDescriptorRegistryChanged();
}
