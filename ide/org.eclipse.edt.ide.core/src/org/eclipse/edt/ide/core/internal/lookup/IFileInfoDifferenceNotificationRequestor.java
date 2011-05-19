/*******************************************************************************
 * Copyright © 2005, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.core.internal.lookup;

/**
 * @author svihovec
 *
 */
public interface IFileInfoDifferenceNotificationRequestor {

	void partAdded(String partName);
    void partRemoved(String partName);
    void partChanged(String partName);
}
