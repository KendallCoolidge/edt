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
package org.eclipse.edt.compiler.core.ast;

import java.util.HashMap;
import java.util.Map;

/**
 * FunctionParameter AST node type.
 *
 * @author Albert Ho
 * @author David Murray
 */
public class FunctionParameter extends Parameter {
	
	/**
 	 * Function parameter attribute (field/nullable) operators (typesafe enumeration).
 	 */
	public static class AttrType {	
		
		private String token;
		private AttrType(String token) { this.token = token; }
		public String toString() { return token; }
		
		public static final AttrType FIELD = new AttrType("field");//$NON-NLS-1$
		public static final AttrType SQLNULLABLE = new AttrType("SQLNullable");//$NON-NLS-1$
		
		private static final Map CODES;
		static {
			CODES = new HashMap(2);
			AttrType[] ops = {
					FIELD,
					SQLNULLABLE
				};
			for (int i = 0; i < ops.length; i++) {
				CODES.put(ops[i].toString(), ops[i]);
			}
		}
	}
	
	/**
 	 * Function parameter use modifiers (in/out/inout) operators (typesafe enumeration).
 	 */
	public static class UseType {	
		
		private String token;
		private UseType(String token) { this.token = token; }
		public String toString() { return token; }
		
		public static final UseType IN = new UseType("in");//$NON-NLS-1$
		public static final UseType OUT = new UseType("out");//$NON-NLS-1$
		public static final UseType INOUT = new UseType("inout");//$NON-NLS-1$
		
		private static final Map CODES;
		static {
			CODES = new HashMap(4);
			UseType[] ops = {
					IN,
					OUT,
					INOUT
				};
			for (int i = 0; i < ops.length; i++) {
				CODES.put(ops[i].toString(), ops[i]);
			}
		}
	}

	private AttrType attrTypeOpt;
	private UseType useTypeOpt;
	private boolean parmConst;

	public FunctionParameter(SimpleName name, Type type, FunctionParameter.AttrType attrTypeOpt, Boolean parmConst, FunctionParameter.UseType useTypeOpt, int startOffset, int endOffset) {
		super(name, type, startOffset, endOffset);
		
		this.attrTypeOpt = attrTypeOpt;
		this.useTypeOpt = useTypeOpt;
		this.parmConst = parmConst.booleanValue();
	}
	
	public AttrType getAttrType() {
		return attrTypeOpt;
	}
	
	public UseType getUseType() {
		return useTypeOpt;
	}
	
	public boolean isParmConst() {
		return parmConst;
	}
	
	public void accept(IASTVisitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if(visitChildren) {
			name.accept(visitor);
			type.accept(visitor);
		}
		visitor.endVisit(this);
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return new FunctionParameter((SimpleName)name.clone(), (Type)type.clone(), attrTypeOpt, Boolean.valueOf(parmConst), useTypeOpt, getOffset(), getOffset() + getLength());
	}
}
