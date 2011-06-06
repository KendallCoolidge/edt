/*******************************************************************************
 * Copyright © 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.internal.codemanipulation;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.edt.compiler.SystemEnvironment;
import org.eclipse.edt.compiler.binding.AmbiguousTypeBinding;
import org.eclipse.edt.compiler.binding.Binding;
import org.eclipse.edt.compiler.binding.IAnnotationBinding;
import org.eclipse.edt.compiler.binding.IAnnotationTypeBinding;
import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.core.Boolean;
import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.core.ast.AbstractASTExpressionVisitor;
import org.eclipse.edt.compiler.core.ast.AbstractASTVisitor;
import org.eclipse.edt.compiler.core.ast.AnnotationExpression;
import org.eclipse.edt.compiler.core.ast.Assignment;
import org.eclipse.edt.compiler.core.ast.CallStatement;
import org.eclipse.edt.compiler.core.ast.DataTable;
import org.eclipse.edt.compiler.core.ast.Expression;
import org.eclipse.edt.compiler.core.ast.ExternalType;
import org.eclipse.edt.compiler.core.ast.Handler;
import org.eclipse.edt.compiler.core.ast.ImportDeclaration;
import org.eclipse.edt.compiler.core.ast.Interface;
import org.eclipse.edt.compiler.core.ast.Library;
import org.eclipse.edt.compiler.core.ast.Name;
import org.eclipse.edt.compiler.core.ast.NameType;
import org.eclipse.edt.compiler.core.ast.NestedForm;
import org.eclipse.edt.compiler.core.ast.Part;
import org.eclipse.edt.compiler.core.ast.Program;
import org.eclipse.edt.compiler.core.ast.QualifiedName;
import org.eclipse.edt.compiler.core.ast.Record;
import org.eclipse.edt.compiler.core.ast.ReturningToNameClause;
import org.eclipse.edt.compiler.core.ast.Service;
import org.eclipse.edt.compiler.core.ast.SettingsBlock;
import org.eclipse.edt.compiler.core.ast.TopLevelForm;
import org.eclipse.edt.compiler.core.ast.TopLevelFunction;
import org.eclipse.edt.compiler.core.ast.TransferStatement;
import org.eclipse.edt.compiler.core.ast.UseStatement;
import org.eclipse.edt.ide.core.internal.builder.IDEEnvironment;
import org.eclipse.edt.ide.core.internal.utils.Util;
import org.eclipse.edt.ide.ui.internal.codemanipulation.OrganizeImportsOperation.OrganizedImportSection;

public class OrganizeImportsVisitor extends AbstractASTExpressionVisitor{
	private Name currentPartName = null;
	private OrganizedImportSection resolvedTypes;
	private Map /*<String>, <Name>*/unresolvedTypes;
	Set /*<ImportDeclaration>*/ originalImports;
	private Boolean fIsIncludeRefFunc;
	private IProject project;

	public OrganizeImportsVisitor(OrganizedImportSection resolvedTypes, Map unresolvedTypes, Set /*<ImportDeclaration>*/ oldImports, Boolean isIncludeRefFunc, IProject project) {
		super(); 
		this.resolvedTypes = resolvedTypes;
		this.unresolvedTypes = unresolvedTypes;
		this.originalImports = oldImports;
		this.fIsIncludeRefFunc = isIncludeRefFunc;
		this.project = project;
	}

	public void setCurrentPartName(Name partName)
	{
		currentPartName = partName;
	}
	
	public boolean visitExpression(Expression expression) {
		if (expression.isName())
			addUnresolvedName((Name)expression);
		return true;
	}

	public boolean visit(TopLevelFunction topLevelFunction) {
		//the bound part is a top level function
		//check for containerContextDependent annotation value
		//if yes
			//if called from the current file generable part(program)
				//try to resolve all the imports needed for this top level function, add to the same file
			//else
				//do nothing
		
		//if no
			//if from a program(generatable) file, or otherwise 
				//try to resolve all the imports needed for this top level function, add to its file
		return true;
	};

	public boolean visit(CallStatement callStatement) {
		Expression invocationTarget = callStatement.getInvocationTarget();
		if(!invocationTarget.isName()) {
			IBinding binding = invocationTarget.resolveTypeBinding();
			if(isResolvedBinding(binding)) {
				addToResovledTypes(binding);
			}
		}
		else
			addUnresolvedName((Name)invocationTarget);
			
		return true;
	}
	
	public boolean visit(AnnotationExpression annotationExpression) {
		Name annotationName = annotationExpression.getName();
		// Defect 55539 - Don't add system types to unresolved list
		// Defect 61502 - Add user-defined annotations like MVC to unresolved list
		// sysPartBinding is a FlexibleRecordBinding for system annotations
		// sysPartBinding is a NotFoundBinding for user-defined annotations
		IPartBinding sysPartBinding = IDEEnvironment.findSystemEnvironment(project).getPartBinding(null, annotationName.getIdentifier());
		if( !Binding.isValidBinding(sysPartBinding) ) {
			addUnresolvedName(annotationName);
		}
		return true;
	}
	
	public boolean visit(SettingsBlock settingsBlock) {
		settingsBlock.accept(new AbstractASTVisitor(){
			public boolean visit(Assignment assignment) {
				IAnnotationBinding annotationBinding = assignment.resolveBinding();
				if(annotationBinding != null && annotationBinding != IBinding.NOT_FOUND_BINDING){
					if(annotationBinding.getName().equalsIgnoreCase(IEGLConstants.PROPERTY_MSGTABLEPREFIX)){
						String prefixValue = (String)annotationBinding.getValue();
						if(prefixValue != null && prefixValue.length() > 0){
							check4MsgTablePrefix(prefixValue);
						}
					}
				}
				Expression lhsExp = assignment.getLeftHandSide();
				visitExpression(lhsExp);
				return true;
			}
		});
		return true;
	}
	
	/**
	 * if there is msgTablePrefix annotation
	 * if the oldImports has single type imports that starts with the prefixValue, need to keep it
	 * or if the oldImports has any onDemand imports, need to keep it as well							
	 * 
	 * @param msgTablePrefixValue
	 */
	private void check4MsgTablePrefix(String msgTablePrefixValue){
		if(msgTablePrefixValue != null && msgTablePrefixValue.length() > 0){
			boolean fndSingleTypeMatch = false;
			//check to see if this part is already in the existing import section
			Iterator oldIt = originalImports.iterator();						
			while(oldIt.hasNext() && !fndSingleTypeMatch)
			{
				ImportDeclaration oldImportDecl = (ImportDeclaration)(oldIt.next());
				Name importNameNode = oldImportDecl.getName();
				String oldPackageName = "";							 //$NON-NLS-1$
				
				if(!oldImportDecl.isOnDemand())
				{
					String oldPartName = importNameNode.getIdentifier();
					if(oldPartName.startsWith(msgTablePrefixValue))
					{					
						fndSingleTypeMatch = true;
						if(importNameNode instanceof QualifiedName)
						{
							oldPackageName = ((QualifiedName)importNameNode).getQualifier().getCanonicalName();
						}//else if SimpleName, packagename is empty string  
						
						resolvedTypes.addImport(oldPackageName, oldPartName);					
					}
				}
				else  //on demand
				{		
					oldPackageName = importNameNode.getCanonicalName();					
					//we need to keep the onDemand					
					resolvedTypes.addImport(oldPackageName, "*");
				}
			}		
		}
		
	}

	public boolean visit(Service service) {
		handlePart(service);
        for(Iterator iter = service.getImplementedInterfaces().iterator(); iter.hasNext();) {
    		Name nextName = (Name) iter.next();
    		addUnresolvedName(nextName);
        }
        return true;
	}
	
	public boolean visit(ExternalType externalType) {
		handlePart(externalType);
		for(Iterator iter = externalType.getExtendedTypes().iterator(); iter.hasNext();) {			
			Name nextName = (Name) iter.next();
    		addUnresolvedName(nextName);
    	}
		return true;
	}
	
	public boolean visit(Record record) {
		handlePart(record);
		return true;
	}
	
	public boolean visit(DataTable dataTable) {
		handlePart(dataTable);
		return true;
	}
	
	public boolean visit(TopLevelForm topLevelForm) {
		handlePart(topLevelForm);
		return true;
	}
	
	public boolean visit(NestedForm nestedForm) {
		Name subType = nestedForm.getSubType();
		if(subType != null) {
			addUnresolvedName(subType);
		}
		return true;
	}
	
	public boolean visit(Handler handler) {
		handlePart(handler);
		return true;
	}
	
	public boolean visit(Interface interfaceNode) {
		handlePart(interfaceNode);
		return true;
	}
	
	public boolean visit(Library library) {
		handlePart(library);
		return true;
	}
	
	public boolean visit(Program program) {
		handlePart(program);
		return true;
	}
	
	private void handlePart(Part part) {
		Name subType = part.getSubType();
		if(subType != null) {
			addUnresolvedName(subType);
		}
	}

	public boolean visit(UseStatement useStatement) {
        for (Iterator iter = useStatement.getNames().iterator(); iter.hasNext();) 
        {
            Name nextName = (Name) iter.next();
            addUnresolvedName(nextName);
        }		
		return true;
	}

	public boolean visit(ReturningToNameClause returningToNameClause) {
		addUnresolvedName(returningToNameClause.getName());
		return true;
	}
	
	public boolean visit(TransferStatement transferStatement) {
		Expression invocationTarget = transferStatement.getInvocationTarget();
		if(!invocationTarget.isName()) {
			IBinding binding = invocationTarget.resolveTypeBinding();
			if(isResolvedBinding(binding)) {
				addToResovledTypes(binding);
			}
		}		
		else
			addUnresolvedName((Name)invocationTarget);
		return true;
	}
	
	public boolean visit(org.eclipse.edt.compiler.core.ast.FunctionInvocation functionInvocation) {
		Expression expr = functionInvocation.getTarget();
		expr.accept(this);
		// Defect RATLC01531980/49410 - Add imports for top-level functions
		expr.accept(new AbstractASTExpressionVisitor(){
			public boolean visitExpression(Expression expr) {
				if (expr.isName() && ((Name)expr).isSimpleName())
					addUnresolvedName((Name)expr);
				return false;
			}
		});
		return true;
	}
	
	private boolean isResolvedBinding(IBinding binding)
	{
		return ((binding != null) && binding != IBinding.NOT_FOUND_BINDING && !(binding instanceof AmbiguousTypeBinding));
	}
	
	private void addUnresolvedName(Name nameNode)
	{
		IBinding binding = nameNode.resolveBinding();
		addToUnresolvedTypes(nameNode, binding);
	}

	private void addToUnresolvedTypes(Name nameNode, IBinding binding) {
		if(!isResolvedBinding(binding)){
			addToUnresolvedTypes(nameNode);
		}
	}
	
	public boolean visit(QualifiedName qualifiedName) {
		Name nameNode = qualifiedName;
		while (nameNode.isQualifiedName())
			nameNode = ((QualifiedName)nameNode).getQualifier();		

		addUnresolvedName(nameNode);
		return true;		
	}
		
	
	public boolean visit(NameType nameType) {		
		ITypeBinding typeBinding = nameType.resolveTypeBinding();
		addToUnresolvedTypes(nameType.getName(), typeBinding);		
		return true;
	}

	public boolean visitName(Name name) {
		if(name != currentPartName)			//skip the current bound part
		{
			IBinding binding = name.resolveBinding();		
			if(isResolvedBinding(binding)) 
			{
				addToResovledTypes(binding);
			}
			else		//unrevolved binding
			{
				//addToUnresolvedTypes(name);
			}
		}		
			
		//return super.visitName(name);
		return true;
	}

	private void addToResovledTypes(IBinding binding) {
		ITypeBinding typeBinding = null;		
		if(binding.isTypeBinding())
		{
			//cast to type binding
			typeBinding = (ITypeBinding)binding;
		}
		else if(binding.isAnnotationBinding()){
			IAnnotationBinding annotationBinding = (IAnnotationBinding)binding;
			if(!annotationBinding.isAnnotationField())
				typeBinding = annotationBinding.getType();
		}
		else if(binding.isDataBinding())
		{
			IDataBinding dataBinding = (IDataBinding)binding;
			switch(dataBinding.getKind())
			{
			case IDataBinding.LIBRARY_BINDING:
			case IDataBinding.DATATABLE_BINDING:
			case IDataBinding.EXTERNALTYPE_BINDING:
			case IDataBinding.FORMGROUP_BINDING:
			case IDataBinding.PROGRAM_BINDING:
				typeBinding = dataBinding.getType();
				break;
			case IDataBinding.TOP_LEVEL_FUNCTION_BINDING:
				//if(fIsIncludeRefFunc == Boolean.YES) what if calling a topLevelFunc inside another topLevelFunc
					typeBinding = dataBinding.getType();
					break;
			default:
				break;
			}
		}
		
		if(typeBinding != null && typeBinding.isPartBinding())			//non primitive part binding
		{
			IPartBinding partBinding = (IPartBinding) typeBinding;
			String partName = typeBinding.getCaseSensitiveName();
			
			String[] pkgName = typeBinding.getPackageName();
			IPath pkgPath = Util.stringArrayToPath(pkgName);
			String packageName = pkgPath.toString().replace(IPath.SEPARATOR, '.');
			boolean isSysPart = partBinding.isSystemPart();
			if(!isSysPart || (isSysPart && isInOriginalImports(packageName, partName))) {	
				resolvedTypes.addImport(packageName, partName);
			}
		}
		else if(typeBinding != null && typeBinding instanceof IAnnotationTypeBinding){
			IAnnotationTypeBinding annotationTypeBinding = (IAnnotationTypeBinding)typeBinding;
			String partName = annotationTypeBinding.getCaseSensitiveName();			
			String[] pkgName = annotationTypeBinding.getPackageName();
			if(pkgName != null) {
				IPath pkgPath = Util.stringArrayToPath(pkgName);
				String packageName = pkgPath.toString().replace(IPath.SEPARATOR, '.');
				
				boolean isSysAnnotation = annotationTypeBinding.isSystemAnnotation();
				//if it is the system annotation, but it was already in the original imports
				//we want to keep it
				if(!isSysAnnotation || (isSysAnnotation && isInOriginalImports(packageName, partName))){
					resolvedTypes.addImport(packageName, partName);
				}
			}
		}
	}
	
	private boolean isInOriginalImports(String pkgName2Test, String partName2Test){
		boolean fndInOriginal = false;
		//check to see if this part is already in the existing import section
		Iterator oldIt = originalImports.iterator();						
		while(oldIt.hasNext() && !fndInOriginal)
		{
			ImportDeclaration oldImportDecl = (ImportDeclaration)(oldIt.next());
			Name importNameNode = oldImportDecl.getName();
			String oldPackageName = "";							 //$NON-NLS-1$
			if(!oldImportDecl.isOnDemand())
			{
				String oldPartName = importNameNode.getIdentifier();
				if(partName2Test.equalsIgnoreCase(oldPartName))
				{					
					if(importNameNode instanceof QualifiedName)
					{
						oldPackageName = ((QualifiedName)importNameNode).getQualifier().getCanonicalName();
					}
					//else if SimpleName, packagename is empty string  
					if(oldPackageName.equalsIgnoreCase(pkgName2Test))
						fndInOriginal = true;
				}
			}
			else  //on demand
			{								
				oldPackageName = importNameNode.getCanonicalName();
				if(pkgName2Test.equalsIgnoreCase(oldPackageName))
				{
					fndInOriginal = true;
				}
			}
		}						
		return fndInOriginal;
	}

	private void addToUnresolvedTypes(Name name) {
		//add to the unresolved type list
		addToMapTypes(name, unresolvedTypes);
	}	
		
	private void addToMapTypes(Name name, Map types){
		String strname = name.getIdentifier();
		if(!types.containsKey(strname))
			types.put(strname, name);
	}
}
