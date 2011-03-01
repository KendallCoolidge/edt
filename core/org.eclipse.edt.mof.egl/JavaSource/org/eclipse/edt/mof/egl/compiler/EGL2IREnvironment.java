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
package org.eclipse.edt.mof.egl.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.edt.compiler.SystemEnvironment;
import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.IPackageBinding;
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.binding.PackageBinding;
import org.eclipse.edt.compiler.binding.PartBinding;
import org.eclipse.edt.compiler.internal.core.lookup.IBindingEnvironment;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;
import org.eclipse.edt.compiler.internal.core.utils.PartBindingCache;
import org.eclipse.edt.compiler.internal.sdk.compile.SourcePathEntry;
import org.eclipse.edt.mof.EObject;
import org.eclipse.edt.mof.MofSerializable;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.lookup.EglLookupDelegate;
import org.eclipse.edt.mof.egl.mof2binding.Mof2Binding;
import org.eclipse.edt.mof.egl.utils.IRUtils;
import org.eclipse.edt.mof.serialization.DeserializationException;
import org.eclipse.edt.mof.serialization.Environment;
import org.eclipse.edt.mof.serialization.IEnvironment;
import org.eclipse.edt.mof.serialization.MofObjectNotFoundException;
import org.eclipse.edt.mof.serialization.ObjectStore;
import org.eclipse.edt.mof.serialization.SerializationException;


public class EGL2IREnvironment implements IBindingEnvironment, IEnvironment {
    
	private static final String[] defaultPackage = InternUtil.intern(new String[0]);
	
	protected IEnvironment irEnv = Environment.INSTANCE;
	private List<File> irPathRoots = new ArrayList<File>();
	private Mof2Binding converter = new Mof2Binding(this);
	private PartBindingCache bindingCache = new PartBindingCache();
	private PackageBinding rootPackageBinding = new PackageBinding(defaultPackage, null, this);

	public EGL2IREnvironment() {
		irEnv.registerLookupDelegate(Type.EGL_KeyScheme, new EglLookupDelegate());
	}
	
	protected boolean rootsContainPackage(String[] packageName) {
		String path = IRUtils.concatWithSeparator(packageName, "/");
		for (File root : irPathRoots) {
			File folder = new File(root, path);
			if (folder.exists()) return true;
		}
		return false;
	}
	
	public void addPartBindingToCache(IPartBinding partBinding) {
		bindingCache.put(partBinding.getPackageName(), partBinding.getCaseSensitiveName(), partBinding);
	}
	
    public IPartBinding getPartBinding(String[] packageName, String partName) {
   
       IPartBinding result = null;
       
 //      result = bindingCache.get(packageName, partName);
    	
       if (result == null) {
    	   result = SourcePathEntry.getInstance().getPartBinding(packageName, partName);
       }
       
       if(result == null){
        	boolean hasSourcePart = false;
 
        	if(SourcePathEntry.getInstance().hasPart(packageName, partName) != ITypeBinding.NOT_FOUND_BINDING){
		        hasSourcePart = true;
	        }
	        String mofSignature = IRUtils.concatWithSeparator(packageName, ".") + "." + partName;
	        String eglSignature = Type.EGL_KeyScheme + ":" + mofSignature;
		
			if (hasSourcePart) {
		    	long sourceLastModified = SourcePathEntry.getInstance().lastModified(packageName, partName);
		    	long irLastModified = irEnv.lastModified(mofSignature);
		    	if (irLastModified == -1) irLastModified = irEnv.lastModified(eglSignature);
		    	if (irLastModified == -1 || sourceLastModified > irLastModified) {
		    		return SourcePathEntry.getInstance().getOrCompilePartBinding(packageName, partName);
		    	}
			}
			try {
				EObject irPart = findPart(eglSignature);
				if (irPart == null) irPart = findPart(mofSignature);
				IPartBinding partBinding;
				if (irPart == null) {
					partBinding = SystemEnvironment.getInstance().getPartBinding(packageName, partName);
				}
				else {
					partBinding = converter.convert(irPart);
				}
				if (partBinding != null) {
					bindingCache.put(packageName, partName, partBinding);
					return partBinding;
				}
				else {
					throw new RuntimeException("Part not found: " + mofSignature);
				}
			} catch (DeserializationException ex2) {
				throw new RuntimeException(ex2);
			}
       }
       return result;
        	
    }
    
    private EObject findPart(String mofSignature) throws DeserializationException {
    	try {
    		return irEnv.find(mofSignature);
    	}
    	catch(MofObjectNotFoundException e1) {
    		return null;
    	}
    }
    
    public IPartBinding getNewPartBinding(String[] packageName, String caseSensitiveInternedPartName, int kind) {
    	return SourcePathEntry.getInstance().getNewPartBinding(packageName, caseSensitiveInternedPartName, kind);
    }
    
    public boolean hasPackage(String[] packageName) {
        return SourcePathEntry.getInstance().hasPackage(packageName)
	      	|| rootsContainPackage(packageName)
	      	|| SystemEnvironment.getInstance().hasPackage(packageName);
    }
    
    public IPackageBinding getRootPackage() {
        return rootPackageBinding;
    }
    
    public void addRoot(File file) {
    	irPathRoots.add(file);
    }
    
    
    public IPartBinding level01Compile(String[] packageName, String caseSensitiveInternedPartName) {
    	String caseInsensitiveInternedPartName = InternUtil.intern(caseSensitiveInternedPartName);
	    int partType = SourcePathEntry.getInstance().hasPart(packageName, caseInsensitiveInternedPartName);
			
	    if(partType != ITypeBinding.NOT_FOUND_BINDING){
	        IPartBinding result = PartBinding.newPartBinding(partType, packageName, caseSensitiveInternedPartName);
            result.setEnvironment(EGL2IREnvironment.this);
            return result;
	    }
	    return IBinding.NOT_FOUND_BINDING;
	}

	@Override
	public EObject find(String key) throws MofObjectNotFoundException, DeserializationException {
		return irEnv.find(key);
	}

	@Override
	public EObject find(String key, boolean useProxies) throws MofObjectNotFoundException, DeserializationException {
		return irEnv.find(key, useProxies);
	}

	@Override
	public ObjectStore getDefaultSerializeStore(String keyScheme) {
		return irEnv.getDefaultSerializeStore(keyScheme);
	}

	@Override
	public EObject lookup(String key) throws DeserializationException {
		return irEnv.lookup(key);
	}

	@Override
	public void registerLookupDelegate(String scheme, LookupDelegate delegate) {
		irEnv.registerLookupDelegate(scheme, delegate);
	}

	@Override
	public void registerObjectStore(String scheme, ObjectStore store) {
		irEnv.registerObjectStore(scheme, store);
	}

	@Override
	public void remove(String key) {
		irEnv.remove(key);
	}
	
	@Override
	public void save(MofSerializable object) throws SerializationException {
		irEnv.save(object);
	}

	@Override
	public void save(MofSerializable object, boolean serialize)
			throws SerializationException {
		irEnv.save(object, serialize);
	}

	@Override
	public void save(String key, EObject object) throws SerializationException {
		irEnv.save(key, object);
	}

	@Override
	public void save(String key, EObject object, boolean serialize)
			throws SerializationException {
		irEnv.save(key, object, serialize);
	}

	@Override
	public void setDefaultSerializeStore(String keyScheme, ObjectStore store) {
		irEnv.setDefaultSerializeStore(keyScheme, store);
	}

	@Override
	public long lastModified(String key) {
		return irEnv.lastModified(key);
	}

	@Override
	public EObject get(String key) {
		return irEnv.get(key);
	}

	@Override
	public LookupDelegate getLookupDelegateForKey(String key) {
		return irEnv.getLookupDelegateForKey(key);
	}
	
	public Mof2Binding getConverter() {
		return converter;
	}
}
