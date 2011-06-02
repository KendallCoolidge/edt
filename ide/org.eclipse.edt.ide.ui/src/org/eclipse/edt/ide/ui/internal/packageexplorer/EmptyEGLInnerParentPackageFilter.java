/*******************************************************************************
 * Copyright © 2004, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.internal.packageexplorer;

import org.eclipse.edt.ide.core.model.EGLModelException;
import org.eclipse.edt.ide.core.model.IPackageFragment;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class EmptyEGLInnerParentPackageFilter extends ViewerFilter {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.navigator.filters.INavigatorExtensionFilter#select(org.eclipse.ui.views.navigator.INavigatorExtensionSite, java.lang.Object, java.lang.Object)
	 */
    public boolean select(Viewer viewer, Object parent, Object element) {
		if (element instanceof IPackageFragment) {
			IPackageFragment pkg= (IPackageFragment)element;
			try {
				if (pkg.isDefaultPackage())
					return pkg.hasChildren();
				return (!pkg.hasSubpackages() || pkg.hasChildren() || (pkg.getNonEGLResources().length > 0));
			} catch (EGLModelException e) {
				return false;
			}
		}
	
		return true;
	}

}
