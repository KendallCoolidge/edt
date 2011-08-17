/*******************************************************************************
 * Copyright © 2005, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.wizards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.edt.compiler.binding.ArrayTypeBinding;
import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.INullableTypeBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.core.ast.DefaultASTVisitor;
import org.eclipse.edt.compiler.core.ast.ExternalType;
import org.eclipse.edt.compiler.core.ast.FunctionParameter;
import org.eclipse.edt.compiler.core.ast.NestedFunction;
import org.eclipse.edt.compiler.core.ast.Parameter;
import org.eclipse.edt.compiler.core.ast.Part;
import org.eclipse.edt.compiler.core.ast.Service;
import org.eclipse.edt.compiler.core.ast.Type;
import org.eclipse.edt.ide.core.internal.model.SourcePart;
import org.eclipse.edt.ide.core.internal.utils.Util;
import org.eclipse.edt.ide.core.model.EGLCore;
import org.eclipse.edt.ide.core.model.EGLModelException;
import org.eclipse.edt.ide.core.model.IEGLElement;
import org.eclipse.edt.ide.core.model.IEGLFile;
import org.eclipse.edt.ide.core.model.IFunction;
import org.eclipse.edt.ide.core.model.IPart;
import org.eclipse.edt.ide.core.model.Signature;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

public class ExtractInterfaceConfiguration extends InterfaceConfiguration {
    
	private Part fTheBoundPart;
	
	private SourcePart fTheSourcePart = null;
	/**
	 * save this value, when extracting interface from the service, set interface's wsdl namespace property to be this service pacakge
	 * which matches the portType in the wsdl file(which is generated from the same service)
	 */
	private String originalServicePackage=""; //$NON-NLS-1$
	
	/**
	 * each element is NestedFunction
	 */
	private List fFunctions = new ArrayList();
	
	/**
	 * each element is Boolean to represent if user has chosen them, init all true
	 */
	private List fFunctionsSelectionState = new ArrayList();
    
	public ExtractInterfaceConfiguration(){
		super();
		
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		IEGLFile serviceFile = getSelectedIEGLFile(selection);
		if(serviceFile != null) {
			
			String fileName = initFile_Package(serviceFile);
			
			String partSimpleName = fileName;		//service is generatable part, so the part name and file name are the same
			
            //try to get the service part name
			if(serviceFile.exists() && fTheSourcePart == null) {
	           IPart sourcepart = serviceFile.getPart(partSimpleName);
            	if(sourcepart instanceof SourcePart)
        			fTheSourcePart = (SourcePart)sourcepart;		           
			}			            
    		if(fTheSourcePart != null && (fTheSourcePart.isService()))
    			partSimpleName = fTheSourcePart.getElementName();
                         
            //init the interface name to be the same as the file name
			setInterface(getFileName());
    		
            Part boundPart = getBoundPart(serviceFile, partSimpleName);
            if(boundPart instanceof Service) {
            	initBoundPart_FunctionList(boundPart);
            	return;
            }
		}
	}

	//return the eglFile name w/o extension
	protected String initFile_Package(IEGLFile eglFile) {
		//init the package name to be the same as the service package
		IEGLElement parentElem = eglFile.getParent();
		originalServicePackage = parentElem.getElementName();
		setFPackage(originalServicePackage);
		
		//init the file name
		//get the filename without the extension
		String fileName = eglFile.getElementName();
		int dot = fileName.indexOf('.');
		fileName = fileName.substring(0, dot);            
		setFileName("I" + fileName);		 //$NON-NLS-1$
		return fileName;
	}
	
	public void initBoundPart_FunctionList(Part boundPart){
    	fTheBoundPart = boundPart;
    	initFunctionList(fTheBoundPart);		
	}
	
	protected void setInterface(String name){
		setInterfaceName(name);
	}

	protected org.eclipse.edt.ide.core.model.IEGLFile getSelectedIEGLFile(
			IStructuredSelection selection) {
		org.eclipse.edt.ide.core.model.IEGLFile serviceFile = null;				
		Object selectedElement= selection.getFirstElement();
		if(selectedElement instanceof IFile)
		{
		    IEGLElement eglElem = EGLCore.create((IFile)selectedElement);	
		    if(eglElem instanceof org.eclipse.edt.ide.core.model.IEGLFile)
		        serviceFile = (org.eclipse.edt.ide.core.model.IEGLFile)eglElem;
		}
		else if(selectedElement instanceof org.eclipse.edt.ide.core.model.IEGLFile)
		{
		    serviceFile = (org.eclipse.edt.ide.core.model.IEGLFile)selectedElement;
		}
		else if(selectedElement instanceof SourcePart)
		{
			fTheSourcePart = (SourcePart)selectedElement;
			serviceFile = fTheSourcePart.getEGLFile();
		}
		return serviceFile;
	}
    
	private void initFunctionList(Part part)
	{
		//clear the list first
		fFunctions.clear();
		fFunctionsSelectionState.clear();
		
		part.accept(new DefaultASTVisitor(){
			public boolean visit(Service service) {return true;}
			
			public boolean visit(ExternalType externaltype) {return true;}
			
			public boolean visit(NestedFunction nestedFunction) {
				if(!nestedFunction.isPrivate()) //only extract the public functions into the interface
				{
					fFunctions.add(nestedFunction);
					fFunctionsSelectionState.add(new Boolean(true));  		//init all true, all selected
				}
				return false;
			};
		});
	}
	

	/**
	 * return the function simple signature, name and parameter types
	 * i.e. method1(int, int)
	 * this is used to be displayed in the UI list to let user choose
	 * 
	 * @param eglfunc
	 * @param resolveQualifier	- if false, the parameter type name is not qualified
	 * 							- if true, the parameter type name is fully qualified if it differs from the currFilePkg name
	 * @param currFilePkg - the file that contains the functions' package name
	 * @return
	 */
	static public String getFunctionSimpleSignature(NestedFunction eglfunc, boolean resolveQualifier, String currFilePkg)
	{
	    StringBuffer strbuf = new StringBuffer();
	    strbuf.append(eglfunc.getName().getCanonicalName());
	    strbuf.append("("); //$NON-NLS-1$
	    
	    List params = eglfunc.getFunctionParameters();
	    Iterator it = params.iterator();
	    boolean bFirst = true;
	    while(it.hasNext())
	    {	           
	        Object obj = it.next();
	        if(obj instanceof FunctionParameter)
	        {
	            if(!bFirst)
	                strbuf.append(", "); //$NON-NLS-1$
	            
	            FunctionParameter eglfuncparam = (FunctionParameter)obj;
	            strbuf.append(eglfuncparam.getName().getCanonicalName());
	            strbuf.append(" "); //$NON-NLS-1$
	            String strParamType = getQualifiedTypeString(eglfuncparam, currFilePkg, resolveQualifier);
	            strbuf.append(strParamType);	                     
//	            if(eglfuncparam.isArrayParameter())		//getTypeString is already returning [] if it's an array
//	                strbuf.append("[]");
	            	            
	            strbuf.append(" "); //$NON-NLS-1$
	            if(eglfuncparam.getUseType() == FunctionParameter.UseType.IN)
	                strbuf.append(IEGLConstants.KEYWORD_IN);
	            else if(eglfuncparam.getUseType() == FunctionParameter.UseType.OUT)
	                strbuf.append(IEGLConstants.KEYWORD_OUT);
	            else if(eglfuncparam.getUseType() == FunctionParameter.UseType.INOUT)
	                strbuf.append(IEGLConstants.KEYWORD_INOUT);	            
	            	            	            
	            bFirst = false;
	        }
	    }
	    strbuf.append(")"); //$NON-NLS-1$
	    
	    return strbuf.toString();
	}
	
	static public String getFunctionSimpleSignature(IFunction func, boolean resolveQualifier, String currFilePkg) throws EGLModelException {
		StringBuffer strbuf = new StringBuffer();
		
		strbuf.append(func.getElementName());

		strbuf.append("("); 
		String[] paramNames = func.getParameterNames();
		String[] paramTypes = func.getParameterTypes();		
		String[] useTypes = func.getUseTypes();
		String[] paramPkgs = func.getParameterPackages();
		boolean isFirst = true;
		for(int i=0; i<func.getNumberOfParameters(); i++){
			if(!isFirst)
				strbuf.append(", "); 
			else
				isFirst = false;		
			strbuf.append(paramNames[i]);
			strbuf.append(" ");
			strbuf.append(getQualifiedTypeString(Signature.toString(paramTypes[i]), paramPkgs[i], currFilePkg));
			if(useTypes.length > i && useTypes[i].trim().length() > 0){
				strbuf.append(" ");
				strbuf.append(useTypes[i]);
			}
			
		}
		strbuf.append(")"); //$NON-NLS-1$
		
		return strbuf.toString();
	}

	/**
	 * get the fully qualified type string if the qualifier differs from the currFilePkg name
	 * 
	 * @param egltypedElem
	 * @param currFilePkg
	 * @return
	 */
	static public String getQualifiedTypeString(Parameter egltypedElem, String currFilePkg, boolean resolveQualifier)
	{
        //get parameter's fully qualifed type name
        Type paramType = egltypedElem.getType();
        return getQualifiedTypeString(paramType, currFilePkg, resolveQualifier);
	}
	
	/**
	 * get the fully qualified type string if the qualifier differs from the currFilePkg name
	 * 
     * @param currFilePkg
     * @param elemType
     * @return
     */
    static public String getQualifiedTypeString(Type elemType, String currFilePkg, boolean resolveQualifier)
    {
    	ITypeBinding elemTypeBinding = elemType.resolveTypeBinding();
    	StringBuffer qualifiedTypeString = new StringBuffer();        
        String typeName = elemType.getCanonicalName();
        
        if(resolveQualifier)
        {
	        String qualifier = getReferenceTypeParamQualifier(elemTypeBinding, currFilePkg);
	        if(qualifier.length()>0)
	        {
	        	qualifiedTypeString.append(qualifier);
	        	qualifiedTypeString.append("."); //$NON-NLS-1$
	        }
        }
        
        if(elemTypeBinding != null && elemTypeBinding != IBinding.NOT_FOUND_BINDING)
        	getSimpleTypeString(elemTypeBinding, qualifiedTypeString);
        else
        	qualifiedTypeString.append(typeName);
                
        return qualifiedTypeString.toString();
    }
    
	
	//for binary function
	static public String getQualifiedTypeString(String elemTypeName, String elemTypePkg, String currFilePkg){
		if(elemTypePkg == null || elemTypePkg.trim().length() == 0)
			return elemTypeName;
		if(elemTypePkg.equalsIgnoreCase(currFilePkg))
			return elemTypeName;
		return elemTypePkg + "." + elemTypeName;
	}
    
    /**
     * 
     * @param elemTypeBinding - assume this is not null, nor NOT_FOUND_BINDING
     * @param simpleTypeString
     */
    static public void getSimpleTypeString(ITypeBinding elemTypeBinding, StringBuffer simpleTypeString){
    	int bindingtypeKind = elemTypeBinding.getKind();
    	if(elemTypeBinding.isNullable()){
    		INullableTypeBinding nullableTypeBinding = (INullableTypeBinding)elemTypeBinding;
    		getSimpleTypeString(nullableTypeBinding.getValueType(), simpleTypeString);
    		
    		simpleTypeString.append('?');
    	}
    	else if(bindingtypeKind == ITypeBinding.ARRAY_TYPE_BINDING){
    		ArrayTypeBinding arrayTypeBinding = (ArrayTypeBinding)elemTypeBinding;
    		getSimpleTypeString(arrayTypeBinding.getElementType(), simpleTypeString);
    		simpleTypeString.append("[]"); //$NON-NLS-1$
    	}
    	else
    		simpleTypeString.append(elemTypeBinding.getName());
    }

    /**
     * if the typeBinding's qualifier differs from the currFilePkg, then return the qualifier string
	 * otherwise, return ""
	 * @param refType
	 * @return
	 */
	static public String getReferenceTypeParamQualifier(ITypeBinding typeBinding, String currFilePkg)
	{
	    String qualifier = ""; //$NON-NLS-1$
	    
	    if(typeBinding != null && typeBinding != IBinding.NOT_FOUND_BINDING)
	    {
	    	//get the element type till it's no longer an array type binding 
	    	while (typeBinding instanceof ArrayTypeBinding) {
				ArrayTypeBinding arrayTypeBinding = (ArrayTypeBinding) typeBinding;
				typeBinding = arrayTypeBinding.getElementType();
			}
	    	String[] pkgs = typeBinding.getPackageName();
	    	if(pkgs != null) {
	    		if(pkgs != null) {
			    	IPath pkgsPath = Util.stringArrayToPath(pkgs);
			    	qualifier = pkgsPath.toString().replace(IPath.SEPARATOR, '.');
		    	}
	    	}
	    }
	    	    
	    //if the parameter's qualifier is the same as the file qualifier, no need to qualify
	    if(currFilePkg.equalsIgnoreCase(qualifier))
	        return ""; //$NON-NLS-1$
	    return qualifier;
	}
	
    /**
     * @return Returns the fFunctions.
     */
    public List getFFunctions() {
        return fFunctions;
    }
    
    public boolean getFunctionSelectionState(int i)
    {
        Boolean B = (Boolean)(fFunctionsSelectionState.get(i));
        return B.booleanValue();
    }
    
    public void setFunctionsSelectionState(int i, boolean bState)
    {
        fFunctionsSelectionState.set(i, new Boolean(bState));
    }
    
    /**
     * @return Returns the fFunctionsSelectionState.
     */
    public List getFFunctionsSelectionState() {
        return fFunctionsSelectionState;
    }
    /**
     * @return Returns the fService.
     */
    public Part getTheBoundPart() {
        return fTheBoundPart;
    }
    /**
     * @return Returns the originalServicePackage.
     */
    public String getOriginalServicePackage() {
        return originalServicePackage;
    }
    
    public void setTheBoundPart(Part boundPart){
    	fTheBoundPart = boundPart;
    }
}
