/*******************************************************************************
 * Copyright � 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.compiler.internal.dli.stmtFactory;

import java.util.List;

import org.eclipse.edt.compiler.binding.ITypeBinding;


/**
 * @author Harmon
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IHierarchyEntry {
    IRelationship getRelationship();
    int getLevel();
    IHierarchyEntry getParent();
    List getChildren();
    String getSegmentName();
    IDLISegmentRecord getSegmentRecord();
}
