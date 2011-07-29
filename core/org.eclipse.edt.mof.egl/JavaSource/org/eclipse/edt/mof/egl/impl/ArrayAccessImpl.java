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

import org.eclipse.edt.mof.egl.ArrayAccess;
import org.eclipse.edt.mof.egl.ArrayType;
import org.eclipse.edt.mof.egl.Expression;
import org.eclipse.edt.mof.egl.GenericType;
import org.eclipse.edt.mof.egl.NoSuchFunctionError;
import org.eclipse.edt.mof.egl.Operation;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.utils.IRUtils;
import org.eclipse.edt.mof.egl.utils.TypeUtils;


public class ArrayAccessImpl extends ExpressionImpl implements ArrayAccess {
	private static int Slot_array=0;
	private static int Slot_index=1;
	private static int Slot_operation=2;
	private static int totalSlots = 3;
	
	public static int totalSlots() {
		return totalSlots + ExpressionImpl.totalSlots();
	}
	
	static {
		int offset = ExpressionImpl.totalSlots();
		Slot_array += offset;
		Slot_index += offset;
		Slot_operation += offset;
	}
	@Override
	public Expression getArray() {
		return (Expression)slotGet(Slot_array);
	}
	
	@Override
	public void setArray(Expression value) {
		slotSet(Slot_array, value);
	}
	
	@Override
	public Expression getIndex() {
		return (Expression)slotGet(Slot_index);
	}
	
	@Override
	public void setIndex(Expression value) {
		slotSet(Slot_index, value);
	}
	
	@Override
	public Type getType() {
		Type arrayType = getArray().getType();
		if (arrayType instanceof GenericType && !((GenericType)arrayType).getTypeArguments().isEmpty())
			return ((GenericType)arrayType).getTypeArguments().get(0);
		else 
			return TypeUtils.Type_ANY;
	}

	@Override
	public boolean isNullable() {
		if (getArray().getType() instanceof ArrayType) {
			return ((ArrayType)getArray().getType()).elementsNullable() 
				|| TypeUtils.isReferenceType(getType()) ;
		}
		else {
			return TypeUtils.isReferenceType(getType());
		}
	}

	@Override
	public Operation getOperation() {
		if (slotGet(Slot_operation) == null) {
			try {
				setOperation(resolveOperation());
			} catch (NoSuchFunctionError e) {
				throw new RuntimeException(e);
			}
		}
		return (Operation)slotGet(Slot_operation);
	}
	
	@Override
	public void setOperation(Operation value) {
		slotSet(Slot_operation, value);
	}
	
	private Operation resolveOperation() {
		Operation op = IRUtils.getMyOperation(getArray().getType().getClassifier(), "[]");
		if (op == null) throw new NoSuchFunctionError();
		return op;
	}
}
