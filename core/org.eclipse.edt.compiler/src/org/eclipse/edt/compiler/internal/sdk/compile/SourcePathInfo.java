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
package org.eclipse.edt.compiler.internal.sdk.compile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.core.ast.Part;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;
import org.eclipse.edt.compiler.internal.sdk.utils.Util;
import org.eclipse.edt.compiler.sdk.compile.BuildPathException;


/**
 * @author svihovec
 *
 */
public class SourcePathInfo {

    private static final SourcePathInfo INSTANCE = new SourcePathInfo();
    
	private class PartEntry {
		private int partType;
		private File file;
		private String caseSensitivePartName;
		
		public PartEntry(int partType, File file, String caseSensitivePartName){
			this.partType = partType;
			this.file = file;
			this.caseSensitivePartName = caseSensitivePartName;
		}
		public File getFile() {
			return file;
		}
		public int getPartType() {
			return partType;
		}
		public String getCaseSensitivePartName(){
			return caseSensitivePartName;
		}
	}
	
	/**
	 * This class is used to maintain information about the packages and parts in a source path entry.
	 * @author svihovec
	 */
	private class ResourceInfo {
		private HashMap packages = null;
		private HashMap parts = null;
		
		public ResourceInfo addPackage(String packageName){
			if(packages == null){
				packages = new HashMap(5);
			}
			
			ResourceInfo pkgInfo = (ResourceInfo)packages.get(packageName);
			if(pkgInfo == null){
				pkgInfo = new ResourceInfo();
				packages.put(packageName, pkgInfo);
			}
			return pkgInfo;
		}
		
		public ResourceInfo getPackage(String packageName){
			if(packages != null){
				return (ResourceInfo)packages.get(packageName);
			}
			return null;
		}
		
		public void addPart(String partName, int partType, File file, String caseSensitivePartName){
			// map parts to files
			if(parts == null){
				parts = new HashMap();
			}
			if(!parts.containsKey(partName)){
			    parts.put(partName, new PartEntry(partType, file, caseSensitivePartName));		
			}
		}
		
		public File getFile(String partName){
			PartEntry part = getPart(partName);
			if(part != null){
				return part.getFile();
			}
			return null;
		}
		
		public int getPartType(String partName){
			PartEntry part = getPart(partName);
			if(part != null){
				return part.getPartType();
			}
			return ITypeBinding.NOT_FOUND_BINDING;
		}
	
		public String getCaseSensitivePartName(String partName){
			PartEntry part = getPart(partName);
			if(part != null){
				return part.getCaseSensitivePartName();
			}
			return null;
		}
		
		private PartEntry getPart(String partName){
			if(parts != null){
				return (PartEntry)parts.get(partName);
			}
			return null;
		}
		
		public String toString(){
			StringBuffer result = new StringBuffer();
			
			result.append("Packages: \n");
			if(packages == null){
				result.append("\tNone");
			}else{
				Set set = packages.keySet();
				for (Iterator iter = set.iterator(); iter.hasNext();) {
					String pkgName = (String) iter.next();
					result.append("\t" + pkgName + "\n");
				}
			}
			result.append("\n");
			
			result.append("Parts: \n");
			if(parts == null){
				result.append("\tNone");
			}else{
				Set set = parts.keySet();
				for (Iterator iter = set.iterator(); iter.hasNext();) {
					String partName = (String) iter.next();
					result.append("\t" + partName + "\n");
				}
			}
			result.append("\n");			
			
			return result.toString();
		}

		public boolean containsPart(String identifier) {
			return parts != null && parts.containsKey(identifier);
		}
	}
	
	private ResourceInfo rootResourceInfo = new ResourceInfo();
    
	private SourcePathInfo(){ }
	
	public static SourcePathInfo getInstance(){
	    return INSTANCE;
	}
	
	public void reset() {
		rootResourceInfo = new ResourceInfo();
	}
	
    public void addPart(String[] packageName, String partName, int partType, File declaringFile, String caseSensitivePartName) {
        ResourceInfo root = rootResourceInfo;
        for (int i = 0; i < packageName.length; i++) {
            root = root.addPackage(InternUtil.intern(packageName[i]));
        }
          
        root.addPart(partName, partType, declaringFile, caseSensitivePartName);
    }

    private void initializeEGLPackageHelper(final File parent, final ResourceInfo parentMap){
    	
    	File[] resources = parent.listFiles();
		
    	if (resources == null) {
    		return;
    	}
		
		for (int i = 0; i < resources.length; i++) {
			
			if(resources[i].isDirectory()){
				
				ResourceInfo info = parentMap.addPackage(InternUtil.intern(resources[i].getName()));
				initializeEGLPackageHelper(resources[i], info);
			
			}else{
				if (Util.isEGLFileName(resources[i].getName())) {
					initializeEGLFileHelper(resources[i],parentMap);
				}
			}
		}		
    }
    
    private void initializeEGLFileHelper(File file, ResourceInfo parentResourceInfo) {
    	
    	org.eclipse.edt.compiler.core.ast.File parsedFile = ASTManager.getInstance().getFileAST(file);
    	
    	List parts = parsedFile.getParts();
    	for (Iterator iter = parts.iterator(); iter.hasNext();) {
    		Part part = (Part)iter.next();
    		
    		parentResourceInfo.addPart(part.getIdentifier(), Util.getPartType(part), file, part.getName().getCaseSensitiveIdentifier());			
		}
    	
    	// add the file part
		parentResourceInfo.addPart(Util.getFilePartName(file), Util.getPartType(parsedFile), file, Util.getCaseSensitiveFilePartName(file));
	}
    
    public boolean hasPackage(String[] packageName) {
    	ResourceInfo info = rootResourceInfo;
    	for (int i = 0; i < packageName.length; i++) {
			info = info.getPackage(packageName[i]);
			if(info == null){
				break;
			}
		}
    	return info != null;
    }
    
    public int hasPart(String[] packageName, String partName) {
    	ResourceInfo info = getPackageInfo(packageName);
    	if(info != null){
    		return info.getPartType(partName);
    	}
    	return ITypeBinding.NOT_FOUND_BINDING;
    }
    
    private ResourceInfo getPackageInfo(String[] packageName) {
		ResourceInfo info = rootResourceInfo;
    	for (int i = 0; i < packageName.length; i++) {
			info = info.getPackage(packageName[i]);
			if(info == null){
				break;
			}
		}
		return info;
	}

	public File getDeclaringFile(String[] packageName, String partName) {
    	ResourceInfo info = getPackageInfo(packageName);
    	if(info != null){
    		return info.getFile(partName);
    	}
    	return null;
    }

    public void setSourceLocations(File[] sourceLocations) {
        for (int i = 0; i < sourceLocations.length; i++) {
        	if (!sourceLocations[i].exists()){
        		try {
					throw new BuildPathException("Invalid path: " + sourceLocations[i].getCanonicalPath());
				} catch (IOException e) {
					throw new BuildPathException("Invalid path: ", e);
				}
        	}
        	initializeEGLPackageHelper(sourceLocations[i], rootResourceInfo);
        }        
    }   
    
    public String getCaseSensitivePartName(String[] packageName, String partName){
    	ResourceInfo info = getPackageInfo(packageName);
    	if(info != null){
    		return info.getCaseSensitivePartName(partName);
    	}
    	return null;
    }
}
