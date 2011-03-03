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
package org.eclipse.edt.mof.egl.impl;

import org.eclipse.edt.mof.egl.ArrayType;
import org.eclipse.edt.mof.egl.Expression;
import org.eclipse.edt.mof.egl.Type;

public class ArrayTypeImpl extends GenericTypeImpl implements ArrayType {
	private static int Slot_initialSize=0;
	private static int Slot_elementsNullable = 1;
	private static int totalSlots = 2;
	
	public static int totalSlots() {
		return totalSlots + GenericTypeImpl.totalSlots();
	}
	
	static {
		int offset = GenericTypeImpl.totalSlots();
		Slot_initialSize += offset;
		Slot_elementsNullable += offset;
	}
	@Override
	public Expression getInitialSize() {
		return (Expression)slotGet(Slot_initialSize);
	}
	
	@Override
	public void setInitialSize(Expression value) {
		slotSet(Slot_initialSize, value);
	}
	
	
	@Override
	public boolean hasInitialSize() {
		return getInitialSize() != null;
	}
	
	@Override
	public boolean elementsNullable() {
		return (Boolean)slotGet(Slot_elementsNullable);
	}
	
	@Override
	public void setElementsNullable(boolean value) {
		slotSet(Slot_elementsNullable, value);
	}
	
	@Override
	public Type getElementType() {
		return getTypeArguments().isEmpty() ? null : getTypeArguments().get(0);
	}
	
	@Override
	public void setElementType(Type elementType) {
		getTypeArguments().add(0, elementType);
	}
	
	@Override
	public String modifySignature(String typeSignature) {
		if (elementsNullable()) {
			return typeSignature + Type.NullableIndicator;
		}
		return typeSignature;
	}

}
