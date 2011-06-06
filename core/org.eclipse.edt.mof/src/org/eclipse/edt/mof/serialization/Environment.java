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
package org.eclipse.edt.mof.serialization;

import java.util.Stack;

import org.eclipse.edt.mof.EObject;
import org.eclipse.edt.mof.MofSerializable;

public class Environment extends AbstractEnvironment {
	public static Environment INSTANCE = new Environment();
	
	private static Stack<IEnvironment> currentEnvs = new Stack<IEnvironment>();
	
	public static synchronized IEnvironment getCurrentEnv() {
		return currentEnvs.size() == 0 ? INSTANCE : currentEnvs.peek();
	}
	
	public static synchronized void pushEnv(IEnvironment env) {
		currentEnvs.push(env);
	}
	
	public static synchronized IEnvironment popEnv() {
		return currentEnvs.pop();
	}

			
	public Environment() {	
		super();
	}
			
	public void saveType(MofSerializable type) { 
		save(type);
	}
			
	public void removeType(String mofSignature) {
		remove(mofSignature);
	}	
	
	public MofSerializable findType(String mofSignature) throws TypeNotFoundException, DeserializationException {
		EObject type;
		try {
			type = find(mofSignature);
		} catch (MofObjectNotFoundException e) {
			throw new TypeNotFoundException(e);
		}
		return (MofSerializable)type;
	}

}
