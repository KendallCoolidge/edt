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
package org.eclipse.edt.compiler.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.eclipse.edt.compiler.EDTCompiler;
import org.eclipse.edt.compiler.SystemEnvironment;
import org.eclipse.edt.compiler.SystemEnvironmentUtil;
import org.eclipse.edt.compiler.SystemPackageBuildPathEntryFactory;
import org.eclipse.edt.compiler.Util;
import org.eclipse.edt.compiler.internal.core.lookup.BindingCreator;
import org.eclipse.edt.compiler.internal.mof2binding.Mof2Binding;
import org.eclipse.edt.mof.EObject;
import org.eclipse.edt.mof.MofFactory;
import org.eclipse.edt.mof.egl.BinaryExpression;
import org.eclipse.edt.mof.egl.Field;
import org.eclipse.edt.mof.egl.FunctionMember;
import org.eclipse.edt.mof.egl.Operation;
import org.eclipse.edt.mof.egl.Part;
import org.eclipse.edt.mof.egl.QualifiedFunctionInvocation;
import org.eclipse.edt.mof.egl.Statement;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.lookup.EglLookupDelegate;
import org.eclipse.edt.mof.egl.lookup.PartEnvironment;
import org.eclipse.edt.mof.egl.utils.IRUtils;
import org.eclipse.edt.mof.impl.AbstractVisitor;
import org.eclipse.edt.mof.serialization.DeserializationException;
import org.eclipse.edt.mof.serialization.Environment;
import org.eclipse.edt.mof.serialization.FileSystemObjectStore;
import org.eclipse.edt.mof.serialization.IEnvironment;
import org.eclipse.edt.mof.serialization.MofObjectNotFoundException;
import org.eclipse.edt.mof.serialization.ObjectStore;


public class TestLoadPart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		(new TestLoadPart()).doIt(args);
	}
	
	public void doIt(String[] args) {
		File root = null;
		String partName = null;
		if (args.length > 0) {
			root = new File(args[0]);
		}
		if (args.length > 1) {
			partName = args[1];
		}
		else {
			root = new File("d:/workspaces/EGL_CE/org.eclipse.edt.mof.egl/EGL_MOF");
			partName = "egl:lc.eksportremburs.datatables.common.lcget01";
		}
		// Register MOF model object store
		ObjectStore typeStore = new FileSystemObjectStore(root, PartEnvironment.getCurrentEnv(), "XML");
		PartEnvironment.getCurrentEnv().registerObjectStore(IEnvironment.DefaultScheme, typeStore);
		// Register EGL parts object store
		typeStore = new FileSystemObjectStore(root, PartEnvironment.getCurrentEnv(), "XML", ".eglxml");
		PartEnvironment.getCurrentEnv().registerObjectStore(Type.EGL_KeyScheme, typeStore);
		
		//initialize the system parts
		new EDTCompiler().getSystemEnvironment(null);

		try {
			MofFactory mof = MofFactory.INSTANCE;
			EObject eClass = null;
			for (int i=0; i<2; i++) {
				long start = System.currentTimeMillis();
				eClass = Environment.INSTANCE.find(partName);
				System.out.println("Time to load: " + (System.currentTimeMillis()-start));
			}
			if (eClass instanceof Part) {
				visitStuff((Part)eClass);
				Set<Part> types = IRUtils.getReferencedPartsFor((Part)eClass);
				System.out.println("Referenced parts for: " + ((Part)eClass).getFullyQualifiedName());
				for (Part part : types) {
					System.out.println(part.getFullyQualifiedName());
				}
			}
//			Serializer xml = new XMLSerializer();
//			xml.serialize(eClass);
//			System.out.print((String)xml.getContents());
		} catch (MofObjectNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DeserializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void visitStuff(Part part) {
		AbstractVisitor visitor = new MyVisitor();
		visitor.disallowRevisit();
		part.accept(visitor);
	}
	
	
	public static class MyVisitor extends AbstractVisitor {
//		public boolean visit(QualifiedFunctionInvocation inv) {
//			FunctionMember function = inv.getTarget();
//			return false;
//		}
		
//		public boolean visit(BinaryExpression expr) {
//			Operation op = expr.getOperation();
//			return false;
//		}
		
		public boolean visit(Field field) {
			
			String name = IRUtils.getFileName(field);
			System.out.println("Field: " + field.getId() + " = " + name);
			
			return true;
		}
//		
//		public boolean visit(Statement stmt) {
//
//			String name = IRUtils.getFileName(stmt);
//			System.out.println("Statement: " + stmt.getClass().getName() + " = " + name);
//			
//			return true;
//		}
	}

}
