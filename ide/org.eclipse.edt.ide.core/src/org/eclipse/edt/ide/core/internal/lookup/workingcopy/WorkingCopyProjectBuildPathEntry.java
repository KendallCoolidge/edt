/*******************************************************************************
 * Copyright © 2008, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.core.internal.lookup.workingcopy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.edt.compiler.ICompiler;
import org.eclipse.edt.compiler.binding.FileBinding;
import org.eclipse.edt.compiler.binding.IPackageBinding;
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.binding.IRPartBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.internal.core.builder.CircularBuildRequestException;
import org.eclipse.edt.compiler.internal.core.compiler.BindingCompletor;
import org.eclipse.edt.compiler.internal.core.dependency.NullDependencyRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.BindingCreator;
import org.eclipse.edt.compiler.internal.core.lookup.DefaultCompilerOptions;
import org.eclipse.edt.compiler.internal.core.lookup.EnvironmentScope;
import org.eclipse.edt.compiler.internal.core.lookup.FileASTScope;
import org.eclipse.edt.compiler.internal.core.lookup.FileScope;
import org.eclipse.edt.compiler.internal.core.lookup.IEnvironment;
import org.eclipse.edt.compiler.internal.core.lookup.Scope;
import org.eclipse.edt.compiler.internal.core.utils.PartBindingCache;
import org.eclipse.edt.compiler.internal.util.BindingUtil;
import org.eclipse.edt.compiler.internal.util.PackageAndPartName;
import org.eclipse.edt.ide.core.internal.binding.PartRestoreFailedException;
import org.eclipse.edt.ide.core.internal.builder.ASTManager;
import org.eclipse.edt.ide.core.internal.compiler.workingcopy.WorkingCopyASTManager;
import org.eclipse.edt.ide.core.internal.compiler.workingcopy.WorkingCopyProcessingQueue;
import org.eclipse.edt.ide.core.internal.lookup.ProjectBuildPathEntry;
import org.eclipse.edt.ide.core.internal.lookup.ProjectBuildPathManager;
import org.eclipse.edt.ide.core.internal.lookup.ProjectEnvironment;
import org.eclipse.edt.ide.core.internal.partinfo.IPartOrigin;
import org.eclipse.edt.ide.core.internal.utils.Util;
import org.eclipse.edt.ide.core.utils.ProjectSettingsUtility;
import org.eclipse.edt.mof.EObject;
import org.eclipse.edt.mof.egl.Part;
import org.eclipse.edt.mof.egl.PartNotFoundException;
import org.eclipse.edt.mof.serialization.CachingObjectStore;
import org.eclipse.edt.mof.serialization.DeserializationException;
import org.eclipse.edt.mof.serialization.ObjectStore;
import org.eclipse.edt.mof.utils.NameUtile;

/**
 * Project Build Path entries are caches used when binding an AST tree.  A new WorkingCopyProjectBuildPathEntry must be created each time a set of AST trees
 * is bound for a given set of IWorkingCopy elements.  
 * 
 * @author svihovec
 *
 */
public class WorkingCopyProjectBuildPathEntry implements IWorkingCopyBuildPathEntry {
	
	private class RealizingEnvironment implements IEnvironment{
		@Override
		public IPartBinding getPartBinding(String packageName, String partName) {
			return WorkingCopyProjectBuildPathEntry.this.getPartBinding(packageName, partName, true);
        }
		@Override
		public IPartBinding getNewPartBinding(PackageAndPartName ppName, int kind) {
			return WorkingCopyProjectBuildPathEntry.this.getNewPartBinding(ppName, kind);
		}
		@Override
		public boolean hasPackage(String packageName) {
			return WorkingCopyProjectBuildPathEntry.this.hasPackage(packageName);
		}
		@Override
		public IPackageBinding getRootPackage() {
			return declaringEnvironment.getRootPackage();
		}
		@Override
		public ICompiler getCompiler() {
			return WorkingCopyProjectBuildPathEntry.this.getCompiler();
		}		
	}	
	
	private PartBindingCache bindingCache;
	private WorkingCopyProjectEnvironment declaringEnvironment;
	private WorkingCopyProjectInfo projectInfo;
	private IEnvironment realizingEnvironment;
	private WorkingCopyProcessingQueue processingQueue;
	private ObjectStore[] stores;
	 
	public WorkingCopyProjectBuildPathEntry(WorkingCopyProjectInfo projectInfo){
		this.projectInfo = projectInfo;
		this.bindingCache = new PartBindingCache();
		this.stores = ProjectBuildPathEntry.EMPTY_STORES;
		this.realizingEnvironment = new RealizingEnvironment();
	}
	
	 public boolean hasPackage(String packageName) {
    	return projectInfo.hasPackage(packageName);
    }

	 public IPartBinding getPartBinding(String packageName, String partName) {
        return getPartBinding(packageName, partName, false);
    }
    
    public IPartBinding getPartBinding(String packageName, String partName, boolean force) {
        IPartBinding result = null;
        if(processingQueue != null) {
            result = processingQueue.requestCompilationFor(packageName, partName, force);
		}
        if(result == null){
        	// Conceptually should check whether it has that part or not, but for performance reason we will try to grab it from
        	// the cache first.
        	// The existance of a part in the cache implies that the part does physically exist
            result = bindingCache.get(packageName, partName);
            if(result != null) {
                return result;
            }
            else {
            	
        		if (WorkingCopyProjectBuildPathManager.getInstance().getProjectBuildPath(projectInfo.getProject()).isBinary()) {
        			//IF this is a binary project, and we are requesting a binding for the file, we need to compile it at level2. 
        			//All other requests for part bindings from a  binary project should return null
        			IPartOrigin origin = projectInfo.getPartOrigin(packageName, partName);
        			if (origin != null && origin.getEGLFile() != null) {
        				// We are in a binary project with source. If the request is for the file binding, do not try to read it from disk (it wont be there)
        				if (Util.getFilePartName(origin.getEGLFile())== partName) {
        					try{
        						return compileLevel2Binding(projectInfo.getPackageAndPartName(packageName, partName));
        					}catch(CircularBuildRequestException e){
                    			// Remove this part from the cache, so that it is not used incorrectly in the future
                    			removePartBindingInvalid(packageName, partName);
                    			throw e;
                    		}
        				}
        			}
        			return null;
        		}

            	//RMERUI
            	if(WorkingCopyProjectBuildPathManager.getInstance().getProjectBuildPath(projectInfo.getProject()).isReadOnly()){          		
            		// It is a project with no source, read the IRs
            		return readPartBinding(packageName, partName);
            	}else{
            		
            		if (projectInfo.hasPart(packageName, partName) != ITypeBinding.NOT_FOUND_BINDING) {
	
	            		// This project has source, compile from the source
	            		IPartOrigin partOrigin = projectInfo.getPartOrigin(packageName, partName);
	            		boolean shouldCompiled = true;
	            		if (partOrigin == null || partOrigin.getEGLFile() == null) {
	            			shouldCompiled = false;
	            		}
	            		else {
		            		if(partOrigin.getEGLFile().isReadOnly()) {
		            			int index = partName.lastIndexOf(".");
		    					if(index > -1) {//if the part name represents FileName, will continue Compilation	
		    						String fileExtension = partName.substring(index+1);
		    						if(!"egl".equalsIgnoreCase(fileExtension)) {
		    							shouldCompiled = false;
		    						}
		    					}
		    					else {
		    						shouldCompiled = false;
		    					}
		            		} //shouldCompiled &&
	            		}
	            		
	            		if( shouldCompiled) {
	            			try{
	            				return compileLevel2Binding(projectInfo.getPackageAndPartName(packageName, partName));
	            			}catch(CircularBuildRequestException e){
	                			// Remove this part from the cache, so that it is not used incorrectly in the future
	                			removePartBindingInvalid(packageName, partName);
	                			throw e;
	                		}
	                    }
	                    else {
	                        return null;
	                    }
	            	}
	            		
	            	else {return null;}
            	}
            }
        }
        
        return result;
    }
        
    public IPartBinding getNewPartBinding(PackageAndPartName ppName, int kind) {
        IPartBinding partBinding = bindingCache.get(ppName.getPackageName(), ppName.getPartName());
        if(partBinding == null || partBinding.getKind() != kind) {
            partBinding = BindingUtil.createPartBinding(kind, ppName);
            bindingCache.put(ppName.getPackageName(), ppName.getPartName(), partBinding);
        }
        else {
        	partBinding.clear();
        	partBinding.setValid(false);
        }
        return partBinding;
    }

	public void setDeclaringEnvironment(WorkingCopyProjectEnvironment projectEnvironment) {
		this.declaringEnvironment = projectEnvironment;		
	}

	public IPartBinding getPartBindingFromCache(String packageName, String partName) {
		return bindingCache.get(packageName, partName);
	}
	
	public IPartBinding getCachedPartBinding(String packageName, String partName) {
		return getPartBindingFromCache(packageName, partName);
	}


// TODO - When we add support to avoid copying nested records into the parent record's IR, we can use this 
// method to get better performance out of the WCC - this method will load the IR if the source is older than the 
// IR, instead of recompiling everything all of the time
//	private IPartBinding doGetPartBinding(String[] packageName, String partName) {
//		// First, see if there is a working copy of this part that we need to use
//		// If there is no working copy of the part, attempt to find the IR file for this binding
//		// If the IR file exists, check to see if the modification date on this file is older than the modification date on the source file
//		
//		// Check to see if a source part exists for this part
//		int partType = projectInfo.hasPart(packageName, partName);
//		if(partType != ITypeBinding.NOT_FOUND_BINDING) {
//			// We found a source part
//			String caseSensitiveInternedPartName = projectInfo.getCaseSensitivePartName(packageName, partName);
//			if(projectInfo.isWorkingCopy(packageName, partName)){
//				// The part is a working copy
//				return compileLevel2Binding(packageName, caseSensitiveInternedPartName);
//			}else{
//				// The part is not a working copy
//				if(partType == ITypeBinding.FILE_BINDING){
//					// we can't load file bindings from IRs, so always recompile them
//					return compileLevel2Binding(packageName, caseSensitiveInternedPartName);
//				}else{
//					File irFile = getIRFile(packageName, partName.toLowerCase());
//					if(irFile.exists()){
//						long irLastModified = irFile.lastModified();
//						long sourceLastModified = projectInfo.getPartOrigin(packageName, NameUtile.getAsName(partName)).getEGLFile().getModificationStamp();
//						
//						if(sourceLastModified > irLastModified){
//			        		return compileLevel2Binding(packageName, caseSensitiveInternedPartName);
//			        	}else{
//			        		IPartBinding partBinding = readPartBinding(irFile);
//			        		if(partBinding.isValid()){
//								partBinding.setEnvironment(declaringEnvironment);
//						        bindingCache.put(packageName, NameUtile.getAsName(partName), partBinding);
//						        //? WorkingCopyASTManager.getInstance().reportNestedFunctions(partAST,declaringFile);
//						        return partBinding;
//							}
//			        	}				
//					}
//				}
//			}
//		}else{
//			File irFile = getIRFile(packageName, partName);
//			if(irFile.exists()){
//				IPartBinding partBinding = readPartBinding(irFile);
//				if(partBinding.isValid()){
//					partBinding.setEnvironment(declaringEnvironment);
//			        bindingCache.put(packageName, NameUtile.getAsName(partName), partBinding);
//			        //? WorkingCopyASTManager.getInstance().reportNestedFunctions(partAST,declaringFile);
//			        return partBinding;
//				}
//			}
//		}
//		return null;
//	}
   
	public IPartBinding compileLevel2Binding(PackageAndPartName ppName) {
		IFile declaringFile = projectInfo.getPartOrigin(ppName.getPackageName(), ppName.getPartName()).getEGLFile();
        
        Node partAST = WorkingCopyASTManager.getInstance().getAST(declaringFile, ppName.getPartName());
        IPartBinding partBinding = new BindingCreator(declaringEnvironment, ppName, partAST).getPartBinding();
        
        Scope scope;
        if(partBinding.getKind() == ITypeBinding.FILE_BINDING){
            scope = new EnvironmentScope(declaringEnvironment, NullDependencyRequestor.getInstance());
        }else{
        	String fileName = Util.getFilePartName(declaringFile);
			IPartBinding fileBinding = getPartBinding(ppName.getPackageName(), fileName, true);
			if(!fileBinding.isValid()){
				scope = new FileASTScope(new EnvironmentScope(declaringEnvironment, NullDependencyRequestor.getInstance()), (FileBinding)fileBinding, ASTManager.getInstance().getFileAST(declaringFile));
			}else{
				scope = new FileScope(new EnvironmentScope(declaringEnvironment, NullDependencyRequestor.getInstance()), (FileBinding)fileBinding, NullDependencyRequestor.getInstance());
			}
        }
        BindingCompletor.getInstance().completeBinding(partAST, partBinding, scope, DefaultCompilerOptions.getInstance());
        partBinding.setEnvironment(declaringEnvironment);
        if (partBinding instanceof IRPartBinding) {
			BindingUtil.setEnvironment(((IRPartBinding)partBinding).getIrPart(), declaringEnvironment);
		}
       
        bindingCache.put(ppName.getPackageName(), ppName.getPartName(), partBinding);
		WorkingCopyASTManager.getInstance().reportNestedFunctions(partAST,declaringFile);
		
        return partBinding;
	}

	/**
	 * Called by a level_01 compile to create a binding for a part when the processing queue is too long.
	 * This should only be called on 'Source' projects, as a read only project would result in the part being loaded
	 * directly from an IR instead of being compiled.
	 */
	@Override
	public int hasPart(String packageName, String partName) {
		return projectInfo.hasPart(packageName, partName);
	}

	public IProject getProject() {
		return projectInfo.getProject();
	}

	@Override
	public IPartOrigin getPartOrigin(String packageName, String partName) {
		return projectInfo.getPartOrigin(packageName,partName);
	}
	
	@Override
	public IEnvironment getRealizingEnvironment() {
		return realizingEnvironment;
	}
	
	public void setProcessingQueue(WorkingCopyProcessingQueue processingQueue) {
		this.processingQueue = processingQueue;	
	}

	public void clear() {
		bindingCache = new PartBindingCache();
		
		for (ObjectStore store : stores) {
			if (store instanceof CachingObjectStore) {
				((CachingObjectStore)store).clearCache();
			}
		}
	}
	
	public boolean isZipFile(){
		return false;
	}
	
	public boolean isProject(){
		return true;
	}
	
	public String getID(){
		return getProject().getName();
	}
	
	private EObject readPart(String packageName, String name) throws DeserializationException {
		String key;
		if (packageName != null && packageName.length() > 0) {
    		key = packageName + "." + name;
    	}
    	else {
    		key = name;
    	}
    	
    	for (int i = 0; i < stores.length; i++) {
    		EObject ir = stores[i].get(key);
    		if (ir != null) {
    			return ir;
    		}
    	}
    	return null;
	}
	
	private IPartBinding readPartBinding(String packageName, String partName) {
		try {
	    	EObject ir = readPart(packageName, partName);
	    	if (ir != null) {
	    		IPartBinding partBinding = BindingUtil.createPartBinding(ir);
	    		if (partBinding != null) {
	    			bindingCache.put(packageName, partName, partBinding);
	    			return partBinding;
	    		}
	    	}
	    	return null;
    	}
    	catch(Exception e) {
    		throw new PartRestoreFailedException(packageName, partName, e);
    	}		
	}
	
    public FileBinding getFileBinding(String packageName, String fileName, org.eclipse.edt.compiler.core.ast.File fileAST) {
       	
    	String caseInsensitiveInternedFileName = NameUtile.getAsName(fileName);
    	FileBinding fileBinding = getFileBindingFromCache(packageName, caseInsensitiveInternedFileName);
    	if (fileBinding != null) {
    		return fileBinding;
    	}
    	
    	PackageAndPartName ppName = new PackageAndPartName(org.eclipse.edt.compiler.Util.createCaseSensitivePackageName(fileAST), caseInsensitiveInternedFileName);
    	
        fileBinding = (FileBinding)new BindingCreator(declaringEnvironment, ppName, fileAST).getPartBinding();
 
        fileBinding.setEnvironment(declaringEnvironment);

        
        Scope scope = new EnvironmentScope(declaringEnvironment, NullDependencyRequestor.getInstance());
        
        BindingCompletor.getInstance().completeBinding(fileAST, fileBinding, scope, DefaultCompilerOptions.getInstance());
               
        bindingCache.put(packageName, caseInsensitiveInternedFileName, fileBinding);
        
        return fileBinding;
    }

    public FileBinding getFileBindingFromCache(String packageName, String partName){
        return (FileBinding)bindingCache.get(packageName, partName);
    }
    
	private ICompiler getCompiler() {
		return ProjectSettingsUtility.getCompiler(getProject());
	}
	
	protected void setObjectStores(ObjectStore[] stores) {
    	if (stores == null) {
    		this.stores = ProjectBuildPathEntry.EMPTY_STORES;
    	}
    	else {
    		this.stores = stores;
    	}
    }

	@Override
	public ObjectStore[] getObjectStores() {
		return stores;
	}

	@Override
	public org.eclipse.edt.mof.egl.Part findPart(String packageName, String name) throws PartNotFoundException {
		if(ProjectBuildPathManager.getInstance().getProjectBuildPath(projectInfo.getProject()).isReadOnly()
				|| projectInfo.hasPart(packageName, name) != ITypeBinding.NOT_FOUND_BINDING){
			try {
				EObject ir = readPart(packageName, name);
				if (ir instanceof Part) {
					return (Part)ir;
				}
			}
			catch (DeserializationException e) {
				throw new PartNotFoundException(e);
			}
		}
		return null;
	}		
	
	/**
	 * Remove this part binding from the cache since it has been removed from the workspace
	 */
	public void removePartBindingInvalid(String packageName, String partName) {
		bindingCache.remove(packageName, partName);		
	}
	
    protected WorkingCopyProjectEnvironment getDeclaringEnvironment() {
		return this.declaringEnvironment;
	}

	
}
