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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.edt.compiler.EDTCompiler;
import org.eclipse.edt.compiler.ICompiler;
import org.eclipse.edt.compiler.SystemEnvironment;
import org.eclipse.edt.compiler.SystemEnvironmentUtil;
import org.eclipse.edt.compiler.SystemPackageBuildPathEntryFactory;
import org.eclipse.edt.compiler.internal.core.lookup.BindingCreator;
import org.eclipse.edt.compiler.internal.mof2binding.Mof2Binding;
import org.eclipse.edt.mof.EObject;
import org.eclipse.edt.mof.egl.Part;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.lookup.EglLookupDelegate;
import org.eclipse.edt.mof.egl.lookup.PartEnvironment;
import org.eclipse.edt.mof.egl.utils.LoadPartException;
import org.eclipse.edt.mof.serialization.DeserializationException;
import org.eclipse.edt.mof.serialization.Environment;
import org.eclipse.edt.mof.serialization.FileSystemObjectStore;
import org.eclipse.edt.mof.serialization.IEnvironment;
import org.eclipse.edt.mof.serialization.MofObjectNotFoundException;
import org.eclipse.edt.mof.serialization.ObjectStore;


/**
 * 
 * This is a utility class to load IR files into model objects. 
 */

public class IRLoader {

	public static Part loadEGLPart(String rootDir, String partName, ICompiler compiler) throws LoadPartException {
		
		String EGL_SCHEMA = Type.EGL_KeyScheme + Type.KeySchemeDelimiter;
		
		if (partName != null && !partName.startsWith(EGL_SCHEMA)) {
			partName = EGL_SCHEMA + partName;
		}

		try {
			EObject eClass = loadEObject(rootDir, partName, compiler);

			if (eClass instanceof Part) {
				return (Part) eClass;
			} else {
				throw new LoadPartException(partName + " Not a part.");
			}
		} catch (MofObjectNotFoundException e) {
			throw new LoadPartException("Fail to load part:" + partName, e);
		} catch (DeserializationException e) {
			throw new LoadPartException("Fail to load part:" + partName, e);
		}
	}

	public static EObject loadEObject(String rootDir, String key, ICompiler compiler) throws MofObjectNotFoundException,
			DeserializationException {
		if (rootDir == null || key == null) {
			throw new DeserializationException("rootDir and key must be specified");
		}

		File root = new File(rootDir);

		// Register MOF model object store
		ObjectStore typeStore = new FileSystemObjectStore(root, PartEnvironment.INSTANCE, "XML");
		PartEnvironment.INSTANCE.registerObjectStore(IEnvironment.DefaultScheme, typeStore);

		// Register EGL parts object store
		typeStore = new FileSystemObjectStore(root, PartEnvironment.INSTANCE, "XML", ".eglxml");
		PartEnvironment.INSTANCE.registerObjectStore(Type.EGL_KeyScheme, typeStore);

		if (compiler == null) {
			compiler = new EDTCompiler();
		}
		// make sure the system parts are initialized
		compiler.getSystemEnvironment();
		
		EObject eClass = null;
		eClass = Environment.INSTANCE.find(key);
		return eClass;
	}
	
	/**
	 * Browse the root directory and its sub-directory, look for any EGL Parts. 
	 * 
	 * @param root Root directory to started browsing
	 * @param packageName A package name to be added as prefix to all the part names
	 * @return A vector contains all part names
	 */

	public static Vector<String> getPartNamesFromDirectory(File root, String packageName){
		Vector<String> partNames = new Vector<String>();
		IRLoader.getIRsFromDirectory(partNames, root, new IRLoader.EGLIRFileFilter(), packageName);
		return partNames;
		
	}

	/**
	 * Browse the root directory and its sub-directory, look for any Mof objects. 
	 * 
	 * @param root Root directory to started browsing
	 * @param packageName A package name to be added as prefix to all the names
	 * @return A vector contains all the names
	 */
	public static Vector<String> getMofIRNamesFromDirectory(File root, String packageName){
		Vector<String> partNames = new Vector<String>();
		IRLoader.getIRsFromDirectory(partNames, root, new IRLoader.MofIRFileFilter(), packageName);
		return partNames;
		
	}

	
	private static void getIRsFromDirectory(Vector<String> fileList, File root,
			FileFilter filter, String packageName) {
		if (root.isDirectory()) {
			for (File file : root.listFiles(filter)) {
				if (file.isDirectory()) {
					getIRsFromDirectory(fileList, file, filter, packageName
							+ "." + file.getName());
				} else {
					String partName = packageName
							+ "."
							+ file.getName().substring(0,
									file.getName().indexOf("."));
					if (partName.startsWith("."))
						partName = partName.substring(1);
					fileList.add(partName);
				}
			}
		}
	}

	private static class EGLIRFileFilter implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			if (pathname.isDirectory())
				return true;
			else if (pathname.getName().endsWith(".eglxml"))
				return true;
			else
				return false;
		}
	}
	
	private static class MofIRFileFilter implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			if (pathname.isDirectory())
				return true;
			else if (pathname.getName().endsWith(".mofbin") || pathname.getName().endsWith(".mofxml"))
				return true;
			else
				return false;
		}
	}
}
