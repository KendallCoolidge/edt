/*******************************************************************************
 * Copyright © 2005, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.core.internal.builder;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.edt.compiler.binding.FileBinding;
import org.eclipse.edt.compiler.binding.IPackageBinding;
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.core.ast.File;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.core.ast.Part;
import org.eclipse.edt.compiler.internal.core.builder.CappedProblemRequestor;
import org.eclipse.edt.compiler.internal.core.builder.IBuildNotifier;
import org.eclipse.edt.compiler.internal.core.builder.IMarker;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.BindingCreator;
import org.eclipse.edt.compiler.internal.core.lookup.DefaultCompilerOptions;
import org.eclipse.edt.compiler.internal.core.lookup.EnvironmentScope;
import org.eclipse.edt.compiler.internal.core.lookup.FileScope;
import org.eclipse.edt.compiler.internal.core.lookup.Scope;
import org.eclipse.edt.compiler.internal.egl2mof.Egl2Mof;
import org.eclipse.edt.compiler.internal.util.PackageAndPartName;
import org.eclipse.edt.ide.core.EDTCoreIDEPlugin;
import org.eclipse.edt.ide.core.internal.compiler.Compiler;
import org.eclipse.edt.ide.core.internal.dependency.DependencyGraphManager;
import org.eclipse.edt.ide.core.internal.dependency.DependencyInfo;
import org.eclipse.edt.ide.core.internal.generation.IDEContext;
import org.eclipse.edt.ide.core.internal.lookup.FileInfoManager;
import org.eclipse.edt.ide.core.internal.lookup.IFileInfo;
import org.eclipse.edt.ide.core.internal.lookup.ProjectBuildPathEntryManager;
import org.eclipse.edt.ide.core.internal.lookup.ProjectEnvironment;
import org.eclipse.edt.ide.core.internal.lookup.ProjectEnvironmentManager;
import org.eclipse.edt.ide.core.internal.lookup.ProjectInfo;
import org.eclipse.edt.ide.core.internal.lookup.ProjectInfoManager;
import org.eclipse.edt.mof.EObject;
import org.eclipse.edt.mof.MofSerializable;
import org.eclipse.edt.mof.egl.utils.TypeUtils;
import org.eclipse.edt.mof.serialization.IEnvironment;
import org.eclipse.edt.mof.utils.NameUtile;
import org.eclipse.osgi.util.NLS;

/**
 * @author svihovec
 *
 */
public abstract class AbstractProcessingQueue extends org.eclipse.edt.compiler.internal.core.builder.AbstractProcessingQueue {

	protected ProjectInfo projectInfo;
	
    private static final int MAX_COMPILE_LOOP = 5;
    private IProject project;
    private ProjectEnvironment projectEnvironment;
    private IProcessorRequestor requestor;
   
    public AbstractProcessingQueue(IProject project, IBuildNotifier notifier) {
    	super(notifier, DefaultCompilerOptions.getInstance());
        this.project = project;
        this.projectInfo = ProjectInfoManager.getInstance().getProjectInfo(project);
        this.projectEnvironment = ProjectEnvironmentManager.getInstance().getProjectEnvironment(project);
    }
    
    @Override
    protected boolean hasExceededMaxLoop() {
        return compileLoop >= MAX_COMPILE_LOOP;
    }

    @Override
    protected IPartBinding level03Compile(PackageAndPartName ppName) {
        String qualifiedName = getQualifiedName(ppName.getPackageName(), ppName.getPartName());
		
		if(Builder.DEBUG){
		    System.out.println("\nProcessing: " + qualifiedName); //$NON-NLS-1$
		}
		
		notifier.subTask(NLS.bind(BuilderResources.buildCompiling, qualifiedName));
		
		IFile declaringFile = projectInfo.getPartOrigin(ppName.getPackageName(), ppName.getPartName()).getEGLFile();
		Node partAST = ASTManager.getInstance().getAST(declaringFile, ppName.getPartName());
		
		IPartBinding binding = new BindingCreator(projectEnvironment, ppName, partAST).getPartBinding();
		binding.setEnvironment(projectEnvironment);
      
		DependencyInfo dependencyInfo = new DependencyInfo();
		Scope scope = createScope(ppName.getPackageName(), declaringFile, binding, dependencyInfo);
		CappedProblemRequestor cappedProblemRequestor = new CappedProblemRequestor();
		cappedProblemRequestor.setRequestor(new MarkerProblemRequestor(declaringFile, ppName.getPartName()));
		
		Compiler.getInstance().compilePart(partAST, binding, scope, dependencyInfo, cappedProblemRequestor, compilerOptions);
		
		if(binding.getKind() == ITypeBinding.FILE_BINDING){
			validatePackageDeclaration(ppName.getPackageName(), declaringFile, partAST, ((FileBinding)binding), cappedProblemRequestor);
		}
		
		if(binding.getKind() != ITypeBinding.FILE_BINDING){
	        processCompiledPart(ppName.getPackageName(), ppName.getPartName(), qualifiedName, (Part)partAST, binding, declaringFile, dependencyInfo, cappedProblemRequestor);
		}else{
			processCompiledFilePart(ppName.getPackageName(), ppName.getPartName(), declaringFile);
		}
		
		// record dependency info
		DependencyGraphManager.getInstance().getDependencyGraph(project).putPart(ppName.getPackageName(), ppName.getPartName(), org.eclipse.edt.ide.core.internal.utils.Util.getFilePartName(declaringFile), dependencyInfo);
		
		if(Builder.DEBUG){
			numErrorsReported += cappedProblemRequestor.getNumberOfProblemsReported();
		    totalUnitsCompiled++;
		    System.out.println("Finished Processing: " + qualifiedName + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
			
		return binding;
    }

	private void processCompiledFilePart(String packageName, String caseInsensitiveInternedString, IFile declaringFile) {
		IFileInfo fileInfo = FileInfoManager.getInstance().getFileInfo(project, declaringFile.getProjectRelativePath());
		
		// For a file part, always add all other parts that are in the file
		// Do not broadcast the file part change to other projects
		for (Iterator iter = fileInfo.getPartNames().iterator(); iter.hasNext();) {
			String nextName = (String) iter.next();
			
			// don't add the file part again
			if(!NameUtile.equals(nextName, caseInsensitiveInternedString)){
				addPartFromCompiledFile(new PackageAndPartName(fileInfo.getCaseSensitivePackageName(), fileInfo.getCaseSensitivePartName(nextName), packageName));
			}
		}
	}
    
	private void processCompiledPart(String packageName, String caseInsensitiveInternedString, String qualifiedName, Part partAST, IPartBinding binding, IFile declaringFile, DependencyInfo dependencyInfo, CappedProblemRequestor cappedProblemRequestor) {
		notifier.subTask(NLS.bind(BuilderResources.buildCreatingIR, qualifiedName));
		File fileAST = ASTManager.getInstance().getFileAST(declaringFile);

		MofSerializable previousPart;
		try {
			EObject eobj = ProjectEnvironmentManager.getInstance().getProjectEnvironment(project).findPart(packageName, caseInsensitiveInternedString);
			if (eobj instanceof MofSerializable) {
				previousPart = (MofSerializable)eobj;
			}
			else {
				previousPart = null;
			}
		}
		catch (Exception e) {
			// if we couldn't load the part, just assume it's structurally different
			previousPart = null;
		}
		
		try {
			MofSerializable part = createIRFromBoundAST(partAST, declaringFile, fileAST.getImportDeclarations(), cappedProblemRequestor);
			if(previousPart == null || !TypeUtils.areStructurallyEquivalent(previousPart, part)){
				notifier.subTask(NLS.bind(BuilderResources.buildAddingDependentsOf, qualifiedName));
				
				// If a part binding does not previously exist, do not add dependents of this part, because we have added dependents already before we processed the part.
				if(previousPart != null){
					addDependents(packageName, caseInsensitiveInternedString);
				}
				
				if(requestor != null){
					requestor.recordStructuralChange(packageName, caseInsensitiveInternedString, binding.getKind());
				}
			}
			
			notifier.subTask(NLS.bind(BuilderResources.buildSavingIR, qualifiedName));
			
			if (canSave(caseInsensitiveInternedString)) {
				ProjectEnvironmentManager.getInstance().getProjectEnvironment(project).getIREnvironment().save(part, true);
			}
		} catch (RuntimeException e) {
			cappedProblemRequestor.acceptProblem(partAST.getName(), IProblemRequestor.COMPILATION_EXCEPTION, new String[]{partAST.getName().getCanonicalName()});
			Compiler.getInstance().logIRCreationException(e);
		}
	}
	
    private MofSerializable createIRFromBoundAST(Part partAST, IFile declaringFile, List imports, IProblemRequestor problemRequestor) {
    	IEnvironment env = ProjectEnvironmentManager.getInstance().getProjectEnvironment(project).getIREnvironment();
        Egl2Mof generator = new Egl2Mof(env);
        return (MofSerializable)generator.convert(partAST, new IDEContext(declaringFile, projectEnvironment.getCompiler()), problemRequestor);
    }

	protected void addPartFromCompiledFile(PackageAndPartName ppName){}

	@Override
    protected IPartBinding level02Compile(PackageAndPartName ppName) {
        return ProjectBuildPathEntryManager.getInstance().getProjectBuildPathEntry(project).compileLevel2Binding(ppName);
    }

	@Override
    protected IPartBinding level01Compile(PackageAndPartName ppName) {
        return projectEnvironment.level01Compile(ppName);
    }

	@Override
    protected IPartBinding getPartBindingFromCache(String packageName, String partName) {
        return ProjectBuildPathEntryManager.getInstance().getProjectBuildPathEntry(project).getPartBindingFromCache(packageName, partName);
    }
    
    protected String getQualifiedName(String packageName, String partName){
		if (packageName.length() == 0 || new Path(partName).segmentCount() > 1){
			//partName is a file part or no packageName
			return partName;
		}		
		
		String retVal = new Path(packageName).addTrailingSeparator().toString() + partName;
		return retVal.replace(IPath.SEPARATOR,'.');
	}
    
    private Scope createScope(String packageName, IFile declaringFile, IPartBinding binding, DependencyInfo dependencyInfo) {
		Scope scope;
		if(binding.getKind() == ITypeBinding.FILE_BINDING){
			scope = new EnvironmentScope(projectEnvironment, dependencyInfo);
		}else{
			String fileName = org.eclipse.edt.ide.core.internal.utils.Util.getFilePartName(declaringFile);
			IPartBinding fileBinding = projectEnvironment.getPartBinding(packageName, fileName);
			scope = new FileScope(new EnvironmentScope(projectEnvironment, dependencyInfo), (FileBinding)fileBinding, dependencyInfo);
		}
		return scope;
	}
    
	private void validatePackageDeclaration(String packageName, IFile declaringFile, Node partAST, FileBinding binding, IProblemRequestor problemRequestor) {
		try{
		    IPackageBinding declaringPackage = binding.getDeclaringPackage();
		
			if(declaringPackage != null && !NameUtile.equals(declaringPackage.getPackageName(), packageName)){
				if(packageName.length() == 0){
					// package name specified in default package
					problemRequestor.acceptProblem(((File)partAST).getPackageDeclaration(), IProblemRequestor.PACKAGE_NAME_DOESNT_MATCH_DIRECTORY_STRUCTURE, new String[0]);
				}else if(((File)partAST).hasPackageDeclaration()){
					// incorrect package declaration
					problemRequestor.acceptProblem(((File)partAST).getPackageDeclaration(), IProblemRequestor.PACKAGE_NAME_DOESNT_MATCH_DIRECTORY_STRUCTURE, new String[0]);
				}else{
					// missing package declaration
					IPath packagePath = declaringFile.getProjectRelativePath().removeFileExtension().removeLastSegments(1);
					packagePath = packagePath.removeFirstSegments(packagePath.segmentCount() - org.eclipse.edt.ide.core.internal.utils.Util.qualifiedNameToStringArray(packageName).length);
					problemRequestor.acceptProblem(0, 0, IMarker.SEVERITY_ERROR, IProblemRequestor.PACKAGE_NAME_NOT_PROVIDED, new String[]{packagePath.toString().replace(IPath.SEPARATOR, '.')});
				}
			}else{
				// package declaration must match package name exactly (case sensitive)
				if(((File)partAST).hasPackageDeclaration()){
					String packageDeclName = ((File)partAST).getPackageDeclaration().getName().getCanonicalName();
					// get package path, minus source folder
					IPath packagePath = declaringFile.getProjectRelativePath().removeFileExtension().removeLastSegments(1);
					packagePath = packagePath.removeFirstSegments(packagePath.segmentCount() - org.eclipse.edt.ide.core.internal.utils.Util.qualifiedNameToStringArray(packageName).length);
					if(!packageDeclName.equals(packagePath.toString().replace(IPath.SEPARATOR, IEGLConstants.PACKAGE_SEPARATOR.charAt(0)))){
						//package name does not match case of package on file system
						problemRequestor.acceptProblem(((File)partAST).getPackageDeclaration(), IProblemRequestor.PACKAGE_NAME_DOESNT_MATCH_DIRECTORY_STRUCTURE, IMarker.SEVERITY_ERROR, new String[0]);
					}
				}
			}
		}catch(RuntimeException e){
		    problemRequestor.acceptProblem(0, 0, IMarker.SEVERITY_ERROR, IProblemRequestor.COMPILATION_EXCEPTION, new String[]{binding.getName()});
		    EDTCoreIDEPlugin.getPlugin().log("Part Validation Failure", e);  //$NON-NLS-1$
		}
	}

	protected abstract void addDependents(String packageName, String partName);
	
	protected abstract void addDependents(String qualifiedName);

	public void setProcessorRequestor(IProcessorRequestor requestor){
    	this.requestor = requestor;
    }    
	
	public void removePart(String packageName, String partName) {
		pendingUnits.remove(new ProcessingUnitKey(packageName, partName));	
	}
	
	@Override
	protected void doAddPart(String packageName, String caseInsensitivePartName) {
		PackageAndPartName ppName = projectInfo.getPackageAndPartName(packageName, caseInsensitivePartName);
		addPart(new PackageAndPartName(ppName.getCaseSensitivePackageName(), ppName.getCaseSensitivePartName(), packageName));		
	}
}
