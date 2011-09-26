/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package eglx.lang;
import org.eclipse.edt.javart.*;
@javax.xml.bind.annotation.XmlRootElement(name="NumericOverflowException")
public class NumericOverflowException extends eglx.lang.AnyException {
	private static final long serialVersionUID = 10L;
	public NumericOverflowException() {
		super();
		ezeInitialize();
	}
	public void ezeCopy(Object source) {
		ezeCopy((NumericOverflowException) source);
	}
	public void ezeCopy(eglx.lang.AnyValue source) {
	}
	public NumericOverflowException ezeNewValue(Object... args) {
		return new NumericOverflowException();
	}
	public void ezeSetEmpty() {
	}
	public boolean isVariableDataLength() {
		return false;
	}
	public void loadFromBuffer(ByteStorage buffer, Program program) {
	}
	public int sizeInBytes() {
		return 0;
	}
	public void storeInBuffer(ByteStorage buffer) {
	}
	public void ezeInitialize() {
	}
}
