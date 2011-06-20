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
package org.eclipse.edt.compiler.internal.egl2mof;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.edt.compiler.binding.Binding;
import org.eclipse.edt.compiler.binding.ClassFieldBinding;
import org.eclipse.edt.compiler.binding.DataItemBinding;
import org.eclipse.edt.compiler.binding.DelegateBinding;
import org.eclipse.edt.compiler.binding.FlexibleRecordBinding;
import org.eclipse.edt.compiler.binding.FlexibleRecordFieldBinding;
import org.eclipse.edt.compiler.binding.FormBinding;
import org.eclipse.edt.compiler.binding.FormGroupBinding;
import org.eclipse.edt.compiler.binding.FunctionBinding;
import org.eclipse.edt.compiler.binding.FunctionContainerBinding;
import org.eclipse.edt.compiler.binding.FunctionParameterBinding;
import org.eclipse.edt.compiler.binding.IAnnotationBinding;
import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.binding.NestedFunctionBinding;
import org.eclipse.edt.compiler.binding.TopLevelFunctionBinding;
import org.eclipse.edt.compiler.core.ast.Name;
import org.eclipse.edt.compiler.core.ast.NestedForm;
import org.eclipse.edt.compiler.core.ast.NestedFunction;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.core.ast.Program;
import org.eclipse.edt.compiler.core.ast.ProgramParameter;
import org.eclipse.edt.compiler.core.ast.UseStatement;
import org.eclipse.edt.mof.EClass;
import org.eclipse.edt.mof.EField;
import org.eclipse.edt.mof.EMember;
import org.eclipse.edt.mof.EMemberContainer;
import org.eclipse.edt.mof.EMetadataObject;
import org.eclipse.edt.mof.EMetadataType;
import org.eclipse.edt.mof.EObject;
import org.eclipse.edt.mof.MofSerializable;
import org.eclipse.edt.mof.egl.AccessKind;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.AnnotationType;
import org.eclipse.edt.mof.egl.Container;
import org.eclipse.edt.mof.egl.DataItem;
import org.eclipse.edt.mof.egl.Delegate;
import org.eclipse.edt.mof.egl.EGLClass;
import org.eclipse.edt.mof.egl.Field;
import org.eclipse.edt.mof.egl.Form;
import org.eclipse.edt.mof.egl.FormField;
import org.eclipse.edt.mof.egl.FormGroup;
import org.eclipse.edt.mof.egl.Function;
import org.eclipse.edt.mof.egl.FunctionMember;
import org.eclipse.edt.mof.egl.FunctionParameter;
import org.eclipse.edt.mof.egl.FunctionPart;
import org.eclipse.edt.mof.egl.Interface;
import org.eclipse.edt.mof.egl.LogicAndDataPart;
import org.eclipse.edt.mof.egl.Member;
import org.eclipse.edt.mof.egl.ParameterKind;
import org.eclipse.edt.mof.egl.Part;
import org.eclipse.edt.mof.egl.Record;
import org.eclipse.edt.mof.egl.Service;
import org.eclipse.edt.mof.egl.Statement;
import org.eclipse.edt.mof.egl.StatementBlock;
import org.eclipse.edt.mof.egl.Stereotype;
import org.eclipse.edt.mof.egl.StereotypeType;
import org.eclipse.edt.mof.egl.StructPart;
import org.eclipse.edt.mof.egl.StructuredRecord;
import org.eclipse.edt.mof.egl.Type;
import org.eclipse.edt.mof.egl.utils.IRUtils;
import org.eclipse.edt.mof.serialization.IEnvironment;
import org.eclipse.edt.mof.serialization.ProxyEObject;


abstract class Egl2MofPart extends Egl2MofBase {
	public MofSerializable currentPart;
	protected FunctionMember currentFunction;
	public List<NestedFunction> functionsToProcess = new ArrayList<NestedFunction>();
	 

 	Egl2MofPart(IEnvironment env) {
		super(env);
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.Record node) {
		MofSerializable part = defaultHandleVisitPart(node);
		stack.push(part);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.Enumeration node) {
		MofSerializable part = defaultHandleVisitPart(node);
		setElementInformation(node, part);
		stack.push(part);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.ExternalType node) {
		IPartBinding partBinding = (IPartBinding)node.getName().resolveBinding();
		inMofProxyContext = isMofProxy(partBinding);
		inEMetadataTypeContext = isEMetadataType(partBinding);
		inAnnotationTypeContext = isAnnotationType(partBinding) || isStereotypeType(partBinding);
		inMofContext = isMofReflectType(partBinding) || inEMetadataTypeContext;
		MofSerializable part = handleVisitPart(node);
		handleContents(node, part);
		if (part instanceof EClass) {
			// handle all super types together to allow
			// proper handling of multiple inheritance
			List<EClass> superTypes = new ArrayList<EClass>();
			for (Object name : node.getExtendedTypes()) {
				IPartBinding superType = (IPartBinding)((Name)name).resolveBinding();
				EClass superTypeClass = (EClass)mofTypeFor(superType);
				fixSuperTypes(superTypeClass);
				superTypes.add((EClass)mofTypeFor(superType));
			}
			((EClass)part).addSuperTypes(superTypes);
		}
		else {
			for (Object name : node.getExtendedTypes()) {
				IPartBinding superType = (IPartBinding)((Name)name).resolveBinding();
				if (Binding.isValidBinding(superType)) {
					((EGLClass)part).getSuperTypes().add((EGLClass)mofTypeFor(superType));
				}
			}
		}
		handleEndVisitPart(node, part);
		stack.push(part);
		return false;
	}
	
	// fix object identity problems that can occur when compiling the mof model
	private void fixSuperTypes(EClass eclass) {
		List<EClass> superTypes = eclass.getSuperTypes();
		for (int i = 0; i < superTypes.size(); i++) {
			EObject obj = getMofSerializable(superTypes.get(i).getMofSerializationKey());
			if (obj instanceof EClass) {
				if (obj == superTypes.get(i)) {
					fixSuperTypes((EClass) obj);
				}
				else {
					superTypes.set(i, ((EClass)obj));
				}
			}
		}
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.Program node) {
		MofSerializable part = defaultHandleVisitPart(node);
		stack.push(part);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.Interface node) {
		MofSerializable part = defaultHandleVisitPart(node);
		stack.push(part);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.Handler node) {
		MofSerializable part = defaultHandleVisitPart(node);
		stack.push(part);
		return false;
	}
	
	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.Library node) {
		MofSerializable part = defaultHandleVisitPart(node);
		stack.push(part);
		return false;
	}
	
	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.DataItem dataItem) {
		DataItem part = factory.createDataItem();
		part.setName(dataItem.getName().getCaseSensitiveIdentifier());
		DataItemBinding type = (DataItemBinding)dataItem.getName().resolveBinding();
		part.setBaseType((Type)mofTypeFor(type.getPrimitiveTypeBinding()));
		part.setPackageName(concatWithSeparator(type.getPackageName(), "."));
		createAnnotations(type, part);
		stack.push(part);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.DataTable node) {
		MofSerializable part = defaultHandleVisitPart(node);
		stack.push(part);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.Delegate delegate) {
		Delegate part = factory.createDelegate();
		DelegateBinding binding = (DelegateBinding)delegate.getName().resolveBinding();
		part.setName(binding.getCaseSensitiveName());
		part.setPackageName(concatWithSeparator(binding.getPackageName(), "."));
		
		if (binding.getReturnType() != null) {
			part.setReturnType((Type)mofTypeFor(binding.getReturnType()));		
			part.setIsNullable(binding.getReturnType().isNullable());
		}
		else {
			part.setIsNullable(false);
		}

		partProcessingStack.push(part);
	
		for (org.eclipse.edt.compiler.core.ast.Parameter parm : (List<org.eclipse.edt.compiler.core.ast.Parameter>)delegate.getParameters()) {
			parm.accept(this);
			part.addMember((FunctionParameter)stack.pop());
		}
		setElementInformation(delegate, part);
		partProcessingStack.pop();
		stack.push(part);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.FormGroup formGroup) {
		FormGroup group = factory.createFormGroup();
		group.setName(formGroup.getName().getCaseSensitiveIdentifier());
		FormGroupBinding groupBinding = (FormGroupBinding)formGroup.getName().resolveBinding();
		group.setPackageName(concatWithSeparator(groupBinding.getPackageName(), "."));
		stack.push(group);
		for (Node node : (List<Node>)formGroup.getContents()) {
			//TODO handle USE statment for top level forms
			if (node instanceof NestedForm) {
				NestedForm form = (NestedForm) node;
				form.accept(this);
				Form eForm = (Form)stack.pop();
				eForm.setContainer(group);
				group.getForms().add(eForm);
			}
		}
		createAnnotations(groupBinding, group);
		setElementInformation(formGroup, group);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.NestedForm nestedForm) {
		Form part = factory.createForm();
		part.setName(nestedForm.getName().getCaseSensitiveIdentifier());
		FormBinding binding = (FormBinding)nestedForm.getName().resolveBinding();
		stack.push(part);
		for (Node field : (List<Node>)nestedForm.getContents()) {
			field.accept(this);
			FormField f = (FormField)stack.pop();
			if (f != null)
				part.addMember(f);
		}
		createAnnotations(binding, part);
		setElementInformation(nestedForm, part);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.TopLevelForm topLevelForm) {
		MofSerializable part = defaultHandleVisitPart(topLevelForm);
		stack.push(part);
		return false;
	}

	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.Service service) {
		Service part = (Service)defaultHandleVisitPart(service);
		for (Node node : (List<Node>)service.getImplementedInterfaces()) {
			Interface iface = (Interface)mofTypeFor((ITypeBinding)((org.eclipse.edt.compiler.core.ast.Name)node).resolveBinding());
			part.getInterfaces().add(iface);
		}
		stack.push(part);
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(org.eclipse.edt.compiler.core.ast.TopLevelFunction node) {
		TopLevelFunctionBinding binding = (TopLevelFunctionBinding) node.getName().resolveBinding();
		FunctionPart part = (FunctionPart)handleVisitPart(node);
		Function function = factory.createFunction();
		setElementInformation(node, function);
		part.setFunction(function);
		function.setName(part.getName());
		for (Node parm : (List<Node>)node.getFunctionParameters()) {
			parm.accept(this);
			function.addMember((FunctionParameter)stack.pop());
		}
		if (node.getReturnDeclaration() != null) {
			function.setType((Type)mofTypeFor(node.getReturnType().resolveTypeBinding()));
		}
		StatementBlock block = factory.createStatementBlock();
		block.setContainer(function);
		function.setStatementBlock(block);
		setCurrentFunctionMember(function);
		for (Node stmt : (List<Node>)node.getStmts()) {
			stmt.accept(this);
			block.getStatements().add((Statement)stack.pop());
		}
		setCurrentFunctionMember(null);
		setElementInformation(node, part);
		stack.push(part);
		return false;
	}
	
	public boolean visit(UseStatement stmt) {
		for ( org.eclipse.edt.compiler.core.ast.Name name : (List<org.eclipse.edt.compiler.core.ast.Name>)stmt.getNames()) {
			ITypeBinding partBinding = (ITypeBinding)name.resolveBinding();
			if (!IBinding.NOT_FOUND_BINDING.equals(partBinding)) {
				Part part = (Part)mofTypeFor(partBinding);
				// Assume current part is a LogicAndDataPart as only they have UseStatements
				((LogicAndDataPart)currentPart).getUsedParts().add(part);
				if (partBinding instanceof FormBinding) {
					Field formField = factory.createField();
					formField.setName(partBinding.getCaseSensitiveName());
					formField.setType(part);
					((LogicAndDataPart)currentPart).addMember(formField);
					eObjects.put(partBinding, formField);
				}
			}
		}
		stack.push(null);
		return false;
	}
	
	protected MofSerializable defaultHandleVisitPart(org.eclipse.edt.compiler.core.ast.Part node) {
		IBinding binding = node.getName().resolveBinding();
		MofSerializable part;
		part = handleVisitPart(node);
		handleContents(node, part);
		handleParms(node, part);
		handleEndVisitPart(node, part);
		
		return part;
	}
	
	private void handleParms(org.eclipse.edt.compiler.core.ast.Part part, EObject container) {
		if (part instanceof Program) {
			Program pgmAst = (Program) part;
			org.eclipse.edt.mof.egl.Program program = (org.eclipse.edt.mof.egl.Program) container;
			for (ProgramParameter parmAst : (List<ProgramParameter>)pgmAst.getParameters()) {
				parmAst.accept(this);
				org.eclipse.edt.mof.egl.ProgramParameter parm = (org.eclipse.edt.mof.egl.ProgramParameter) stack.pop();
				parm.setContainer(program);
				program.getParameters().add(parm);
			}
			program.setIsCallable(pgmAst.isCallable());
		}
	}

	protected MofSerializable handleVisitPart(org.eclipse.edt.compiler.core.ast.Part part) {
		IPartBinding partBinding = (IPartBinding)part.getName().resolveBinding();

		// EGL Enumerations are treated as straight Mof EEnum types
		inMofProxyContext = isMofProxy(partBinding);
		inEMetadataTypeContext = isEMetadataType(partBinding);
		inAnnotationTypeContext = isAnnotationType(partBinding) || isStereotypeType(partBinding);
		inMofContext = isMofReflectType(partBinding) || inEMetadataTypeContext;
		EClass typeClass = (EClass)getMofSerializable(mofPartTypeSignatureFor(part));
		MofSerializable eObj = (MofSerializable)typeClass.newInstance();
		partProcessingStack.push(eObj);
		// Use dynamic interface to handle both MOF or EGL types
		eObj.eSet("name", partBinding.getCaseSensitiveName());
		eObj.eSet("packageName", concatWithSeparator(partBinding.getPackageName(), "."));
		IAnnotationBinding subtype = partBinding.getSubTypeAnnotationBinding();
		if (!inMofProxyContext) setReflectTypeValues(eObj, subtype);
		
		env.save(mofSerializationKeyFor(partBinding), eObj, false);
		if (!mofSerializationKeyFor(partBinding).equals(eObj.getMofSerializationKey())) {
			env.save(((MofSerializable)eObj).getMofSerializationKey(), eObj, false);
		}
		if (!inMofContext) { 
			eObjects.put(partBinding, eObj);
			currentPart = eObj;
		}
		return eObj;
	}

	@SuppressWarnings("unchecked")
	protected void handleEndVisitPart(org.eclipse.edt.compiler.core.ast.Part astPart, MofSerializable mofPart) {
		MofSerializable part = partProcessingStack.pop();
		
		// Set the stereotype value if necessary
		IPartBinding partBinding = (IPartBinding)astPart.getName().resolveBinding();
		IAnnotationBinding subtype = partBinding.getSubTypeAnnotationBinding();

		if (mofPart instanceof EClass) {
			createAnnotations(partBinding, (EClass)mofPart);
//			setPartInformation(astPart, (EClass)mofPart);
		}
		else if (mofPart instanceof Part) {
			createAnnotations(partBinding, (Part)mofPart);
			if (mofPart instanceof StructPart)
				setDefaultSuperType((StructPart)mofPart);
			setElementInformation(astPart,  (Part)mofPart);
		}
		
		// Process statements of function members now since
		// All members that expressions may reference have been
		// created and resolved
		for (NestedFunction astFunc : functionsToProcess) {
			IBinding binding = astFunc.getName().resolveDataBinding().getType();
			FunctionMember irFunc = (FunctionMember)getEObjectFor(binding);
			setCurrentFunctionMember(irFunc);
			for (org.eclipse.edt.compiler.core.ast.Node node : (List<org.eclipse.edt.compiler.core.ast.Node>)astFunc.getStmts()) {
				if (node instanceof org.eclipse.edt.compiler.core.ast.Statement) {
					node.accept(this);
					Statement irStmt = (Statement)stack.pop();
					irFunc.getStatements().add(irStmt);
				}
			}
			setCurrentFunctionMember(null);
		}
		if (part instanceof LogicAndDataPart)
			IRUtils.markOverloadedFunctions((LogicAndDataPart)part);
						
		if (inMofProxyContext) {
			EMetadataObject metadata = (EMetadataObject)mofValueFrom(subtype);
			((EClass)mofPart).getMetadataList().add(metadata);
		}
		
		
		for (Entry<IBinding, ProxyEObject> entry : proxies.entrySet()) {
			EObject real = eObjects.get(entry.getKey());
			updateProxyReferences(entry.getKey(), real);
		}

	}
	
	private void handleImplicits(org.eclipse.edt.compiler.core.ast.Part part, EObject container){
		IDataBinding[] impFields = getImplicitFields(part.getName().resolveBinding());
		if (impFields != null) {
			for (int i = 0; i < impFields.length; i++) {
				((Container)container).addMember(createImplicitField(impFields[i]));
			}
		}
		
		NestedFunctionBinding[] implFunctions = getImplicitFunctions(part.getName().resolveBinding());
		if (implFunctions != null) {
			for (int i = 0; i < implFunctions.length; i++) {
				((Container)container).addMember(createImplicitFunction(implFunctions[i]));
			}	
		}
	}
	
	private Member createImplicitField(IDataBinding data) {
		EClass fieldClass = mofMemberTypeFor(data);
		Field f = (Field)fieldClass.newInstance();
		setUpEglTypedElement(f, data);
		if (data instanceof ClassFieldBinding) {
			ClassFieldBinding cfb = (ClassFieldBinding)data;
			f.setIsStatic(cfb.isStatic());
			if (cfb.isPrivate()) {
				f.setAccessKind(AccessKind.ACC_PRIVATE);
			}			
		}

		for (IAnnotationBinding ann : (List<IAnnotationBinding>)data.getAnnotations()) {
			Annotation value = (Annotation)mofValueFrom(ann);
			f.getAnnotations().add(value);
		}
		
		eObjects.put(data, f);

		return f;
	}
	
	private Member createImplicitFunction(NestedFunctionBinding function) {

		FunctionBinding functionBinding = (FunctionBinding) function.getType();
		EClass funcClass = mofMemberTypeFor(function);
		Function func = (Function)funcClass.newInstance();
		setUpEglTypedElement(func, function);
				
		StatementBlock stmts = factory.createStatementBlock();
		stmts.setContainer(func);
		func.setStatementBlock(stmts);

		for (FunctionParameterBinding parmBinding : (List<FunctionParameterBinding>)functionBinding.getParameters()) {

			FunctionParameter parm = factory.createFunctionParameter();
			ParameterKind parmKind;
			if (parmBinding.isInput())
				parmKind=ParameterKind.PARM_IN;
			else if (parmBinding.isInputOutput())
				parmKind=ParameterKind.PARM_INOUT;
			else if (parmBinding.isOutput()) 
				parmKind=ParameterKind.PARM_OUT;
			else
				parmKind=ParameterKind.PARM_INOUT;
			parm.setParameterKind(parmKind);
			setUpEglTypedElement(parm, parmBinding);
			eObjects.put(parmBinding, parm);
			func.addMember(parm);
		}

		if (functionBinding.isPrivate()) {
			func.setAccessKind(AccessKind.ACC_PRIVATE);
		}
		
		if (functionBinding.isStatic()) {
			func.setIsStatic(true);
		}
		eObjects.put(function.getType(), func);	
		return func;
	}
	
	private IDataBinding[] getImplicitFields(IBinding binding) {
		if (binding instanceof FunctionContainerBinding) {
			List<ClassFieldBinding> list =  new ArrayList<ClassFieldBinding>();
			FunctionContainerBinding fc = (FunctionContainerBinding) binding;			
			for (ClassFieldBinding field : (List<ClassFieldBinding>)fc.getClassFields()) {
				if (field.isImplicit()) {
					list.add(field);
				}
			}
			if (list.size() > 0) {
				return (IDataBinding[])list.toArray(new IDataBinding[list.size()]);
			}
		}
		
		if (binding instanceof FlexibleRecordBinding) {
			List<FlexibleRecordFieldBinding> list =  new ArrayList<FlexibleRecordFieldBinding>();
			FlexibleRecordBinding rec = (FlexibleRecordBinding) binding;			
			for (FlexibleRecordFieldBinding field : (List<FlexibleRecordFieldBinding>)rec.getDeclaredFields()) {
				if (field.isImplicit()) {
					list.add(field);
				}
			}
			if (list.size() > 0) {
				return (IDataBinding[])list.toArray(new IDataBinding[list.size()]);
			}
		}
		
		return null;
	}
	
	private NestedFunctionBinding[] getImplicitFunctions(IBinding binding) {
		if (binding instanceof FunctionContainerBinding) {
			List<NestedFunctionBinding> list =  new ArrayList<NestedFunctionBinding>();
			FunctionContainerBinding fc = (FunctionContainerBinding) binding;			
			for (NestedFunctionBinding function : (List<NestedFunctionBinding>)fc.getDeclaredFunctions()) {
				if (((FunctionBinding)function.getType()).isImplicit()) {
					list.add(function);
				}
			}
			if (list.size() > 0) {
				return (NestedFunctionBinding[])list.toArray(new NestedFunctionBinding[list.size()]);
			}
		}
		
		return null;
	}

	

	@SuppressWarnings("unchecked")
	private void handleContents(org.eclipse.edt.compiler.core.ast.Part part, EObject container) {
		
		if (!inMofContext) {
			handleImplicits(part, container);
		}
		
		for (Node n : (List<Node>)part.getContents()) {
			n.accept(this);
			EObject mofObj = stack.pop();
			if (mofObj != null) {
				if (inMofContext) {
					((EMemberContainer)container).addMember((EMember)mofObj);
					if (mofObj instanceof EField && (container instanceof AnnotationType || container instanceof EMetadataType)) {
						((EField)mofObj).setContainment(true);
					}
				}
				else {
					((Container)container).addMember((Member)mofObj);
				}
			}
		}
	}
	private void setDefaultSuperType(StructPart part) {
		if (part.getSuperTypes().isEmpty()) {
			Stereotype stereotype = part.getStereotype();
			if (stereotype != null) {
				StructPart superType = (StructPart)((StereotypeType)stereotype.getEClass()).getDefaultSuperType();
				if (superType == null) {
					String typeSignature = Type_AnyObject;
					if (part instanceof Record)
						typeSignature = Type_AnyRecord;
					else if (part instanceof StructuredRecord) {
						typeSignature = Type_AnyStruct;
					}
					else if (part instanceof AnnotationType) {
						typeSignature = Type_Annotation;
					}
					else if (part instanceof StereotypeType) {
						typeSignature = Type_Stereotype;
					}
					superType = (StructPart)getMofSerializable(typeSignature);
				}
				if (superType != null)
					part.getSuperTypes().add(superType);
				
			}
		}
	}
		
	protected void setCurrentFunctionMember(FunctionMember function) {
		currentFunction = function;
	}
	
	protected FunctionMember getCurrentFunctionMember() {
		return currentFunction;
	}
	
	protected void setElementInformation(org.eclipse.edt.compiler.core.ast.Node node, EObject obj) {
		
		if (obj instanceof Statement) {
			Statement stmt = (Statement) obj;
			if (stmt.getContainer() == null && (currentPart instanceof Container)) {
				stmt.setContainer((Container)currentPart);
			}
		}
		
		super.setElementInformation(node, obj);
	}

}
