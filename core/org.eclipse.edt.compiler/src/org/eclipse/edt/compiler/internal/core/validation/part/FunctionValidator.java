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
package org.eclipse.edt.compiler.internal.core.validation.part;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.edt.compiler.binding.ArrayTypeBinding;
import org.eclipse.edt.compiler.binding.Binding;
import org.eclipse.edt.compiler.binding.BindingUtilities;
import org.eclipse.edt.compiler.binding.DataItemBinding;
import org.eclipse.edt.compiler.binding.FixedRecordBinding;
import org.eclipse.edt.compiler.binding.FlexibleRecordBinding;
import org.eclipse.edt.compiler.binding.FunctionParameterBinding;
import org.eclipse.edt.compiler.binding.IBinding;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.binding.IPartBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.compiler.binding.PrimitiveTypeBinding;
import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.core.ast.AbstractASTVisitor;
import org.eclipse.edt.compiler.core.ast.AddStatement;
import org.eclipse.edt.compiler.core.ast.ArrayType;
import org.eclipse.edt.compiler.core.ast.AssignmentStatement;
import org.eclipse.edt.compiler.core.ast.CallStatement;
import org.eclipse.edt.compiler.core.ast.CaseStatement;
import org.eclipse.edt.compiler.core.ast.CloseStatement;
import org.eclipse.edt.compiler.core.ast.ContinueStatement;
import org.eclipse.edt.compiler.core.ast.ConverseStatement;
import org.eclipse.edt.compiler.core.ast.DeleteStatement;
import org.eclipse.edt.compiler.core.ast.DisplayStatement;
import org.eclipse.edt.compiler.core.ast.EmptyStatement;
import org.eclipse.edt.compiler.core.ast.ExecuteStatement;
import org.eclipse.edt.compiler.core.ast.ExitStatement;
import org.eclipse.edt.compiler.core.ast.ForEachStatement;
import org.eclipse.edt.compiler.core.ast.ForStatement;
import org.eclipse.edt.compiler.core.ast.ForwardStatement;
import org.eclipse.edt.compiler.core.ast.FreeSQLStatement;
import org.eclipse.edt.compiler.core.ast.FunctionDataDeclaration;
import org.eclipse.edt.compiler.core.ast.FunctionInvocationStatement;
import org.eclipse.edt.compiler.core.ast.FunctionParameter;
import org.eclipse.edt.compiler.core.ast.GetByKeyStatement;
import org.eclipse.edt.compiler.core.ast.GetByPositionStatement;
import org.eclipse.edt.compiler.core.ast.GotoStatement;
import org.eclipse.edt.compiler.core.ast.IfStatement;
import org.eclipse.edt.compiler.core.ast.LabelStatement;
import org.eclipse.edt.compiler.core.ast.MoveStatement;
import org.eclipse.edt.compiler.core.ast.Name;
import org.eclipse.edt.compiler.core.ast.NameType;
import org.eclipse.edt.compiler.core.ast.NestedFunction;
import org.eclipse.edt.compiler.core.ast.Node;
import org.eclipse.edt.compiler.core.ast.OnEventBlock;
import org.eclipse.edt.compiler.core.ast.OpenStatement;
import org.eclipse.edt.compiler.core.ast.OpenUIStatement;
import org.eclipse.edt.compiler.core.ast.PrepareStatement;
import org.eclipse.edt.compiler.core.ast.Primitive;
import org.eclipse.edt.compiler.core.ast.PrintStatement;
import org.eclipse.edt.compiler.core.ast.ReplaceStatement;
import org.eclipse.edt.compiler.core.ast.ReturnStatement;
import org.eclipse.edt.compiler.core.ast.SetStatement;
import org.eclipse.edt.compiler.core.ast.SetValuesStatement;
import org.eclipse.edt.compiler.core.ast.ShowStatement;
import org.eclipse.edt.compiler.core.ast.Statement;
import org.eclipse.edt.compiler.core.ast.ThrowStatement;
import org.eclipse.edt.compiler.core.ast.TopLevelFunction;
import org.eclipse.edt.compiler.core.ast.TransferStatement;
import org.eclipse.edt.compiler.core.ast.TryStatement;
import org.eclipse.edt.compiler.core.ast.Type;
import org.eclipse.edt.compiler.core.ast.WhileStatement;
import org.eclipse.edt.compiler.internal.core.builder.IMarker;
import org.eclipse.edt.compiler.internal.core.builder.IProblemRequestor;
import org.eclipse.edt.compiler.internal.core.lookup.ICompilerOptions;
import org.eclipse.edt.compiler.internal.core.lookup.System.SystemPartManager;
import org.eclipse.edt.compiler.internal.core.utils.InternUtil;
import org.eclipse.edt.compiler.internal.core.validation.name.EGLNameValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.AddStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.AssignmentStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.CallStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.CaseStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.CloseStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ContinueStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ConverseStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.DeleteStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.DisplayStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ExecuteStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ExitStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ForEachStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ForStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ForwardStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.FreeSQLStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.FunctionDataDeclarationValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.GetByKeyStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.GetByPositionStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.GotoStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.IfStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.LabelStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.MoveStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.OpenStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.OpenUIStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.PrepareStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.PrintStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ReplaceStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ReturnStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.SetStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ShowStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.StatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.ThrowStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.TransferStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.TryStatementValidator;
import org.eclipse.edt.compiler.internal.core.validation.statement.WhileStatementValidator;


/**
 * @author Dave Murray
 */
public class FunctionValidator extends AbstractASTVisitor {
	
	IProblemRequestor problemRequestor;
	private IPartBinding enclosingPart;
	private String functionName;
	private HashMap labelMap = new HashMap();
	private ArrayList gotoList = new ArrayList();
    ICompilerOptions compilerOptions;
    private boolean nextStatementIsUnreachable;
	private IStatementValidInContainerInfo statementValidInContainerInfo;
	private LabelStatement labelPrecedingStatement;
	private Map labeledLoops = new HashMap();
	
	public FunctionValidator(IProblemRequestor problemRequestor, IPartBinding enclosingPart, ICompilerOptions compilerOptions) {
		this.problemRequestor = problemRequestor;
		this.enclosingPart = enclosingPart;
		if(enclosingPart != null) {
			this.statementValidInContainerInfo = StatementValidInContainerInfoFactory.INSTANCE.create(enclosingPart);
		}
		this.compilerOptions = compilerOptions;
	}
	
	public FunctionValidator(IProblemRequestor problemRequestor, ICompilerOptions compilerOptions) {
		this(problemRequestor, null, compilerOptions);
	}
	
	public boolean visit(TopLevelFunction topLevelFunction) {
		functionName = topLevelFunction.getName().getCanonicalName();
		if(topLevelFunction.getIdentifier() == InternUtil.intern(IEGLConstants.MNEMONIC_MAIN)) {
			problemRequestor.acceptProblem(
				topLevelFunction.getName(),
				IProblemRequestor.FUNCTION_NO_MAIN_FUNCTION_ALLOWED,
				new String[] {IEGLConstants.MNEMONIC_MAIN});
		}
		
		checkFunctionName(topLevelFunction.getName(), false);
		checkNumberOfParms(topLevelFunction.getFunctionParameters(), topLevelFunction.getName());
		
		return true;
	}
	
	public boolean visit(NestedFunction nestedFunction) {
		functionName = nestedFunction.getName().getCanonicalName();
		
		checkFunctionName(nestedFunction.getName(), true);
		checkNumberOfParms(nestedFunction.getFunctionParameters(), nestedFunction.getName());
		
		return true;
	}
	
	void checkNumberOfParms(List parms, Name name) {
		if (parms.size() > 255) {
        	problemRequestor.acceptProblem(
            		name,
    				IProblemRequestor.FUNCTION_TOO_MANY_PARMS,
    				new String[] {name.getCanonicalName(), Integer.toString(parms.size())});        	
		}
	}
	
	public boolean visit(FunctionParameter functionParameter) {
	
       	checkConstParmNotField(functionParameter);
	
        Type parmType = functionParameter.getType();
        FunctionParameterBinding fParameterBinding = (FunctionParameterBinding) functionParameter.getName().resolveBinding();
        
        if(fParameterBinding != null) {			
	        checkParmTypeNotStaticArray(functionParameter, parmType);
	        
	        ITypeBinding typeBinding = parmType.resolveTypeBinding();
	        if(typeBinding != null && typeBinding != IBinding.NOT_FOUND_BINDING) {
	        	checkParmNotEmptyRecord(functionParameter, typeBinding);
	        	checkRecordParmNotFieldOrNullable(functionParameter, typeBinding);
	        	checkParmNotPSBRecord(functionParameter, typeBinding);
	        	checkTypeNotServiceOrInterfaceArray(functionParameter, typeBinding);
	        	
	        	if(fParameterBinding.isInput()) {
	        		checkInputParm(functionParameter, typeBinding);         		
	            }
	        	else if(fParameterBinding.isOutput()) {
	        		checkOutputParm(functionParameter, typeBinding);
	        	}
	        	else if(functionParameter.getUseType() != null) {
	        		checkExplicitInputOutputParm(functionParameter, typeBinding);
	        	}
	        	else {
	        		checkImplicitInputOutputParm(functionParameter, typeBinding);
	        	}
	        	
	        	if(enclosingPart != null) {
	        		checkPartSpecificConditions(functionParameter, fParameterBinding, enclosingPart);
	        	}
	        	
	        	if(enclosingPart == null ||
	        	   (ITypeBinding.SERVICE_BINDING != enclosingPart.getKind() &&
	 			    ITypeBinding.INTERFACE_BINDING != enclosingPart.getKind())) {
	 				if(fParameterBinding.isSQLNullable()) {
	 					checkNullableParm(functionParameter, typeBinding);
	 				}
	 			}
	        	
	        	StatementValidator.validateDeclarationForStereotypeContext(fParameterBinding, problemRequestor, functionParameter.getType().getBaseType());
	        }
	        
	        if(fParameterBinding.isInput()) {
	    		checkInputParm(functionParameter);
	        }
	    	else if(fParameterBinding.isOutput()) {
	    		checkOutputParm(functionParameter);
	    	}
	    	
	    	EGLNameValidator.validate(functionParameter.getName(), EGLNameValidator.PART, problemRequestor, compilerOptions);
        }
        return true;
	}
	
	private void checkFunctionName(Name name, boolean nested) {
		if (nested) {
			EGLNameValidator.validate(name, EGLNameValidator.INTERNALFIELD, problemRequestor, compilerOptions);
		}
		else {
			EGLNameValidator.validate(name, EGLNameValidator.FUNCTION, problemRequestor, compilerOptions);
		}
	}
	
	private void checkParmTypeNotStaticArray(FunctionParameter functionParameter, Type parmType) {
		if(parmType.isArrayType() && ((ArrayType) parmType).hasInitialSize()) {
        	problemRequestor.acceptProblem(
        		parmType,
				IProblemRequestor.STATIC_ARRAY_PARAMETER_DEFINITION,
				new String[] {functionParameter.getName().getCanonicalName(), functionName});        	
        }
	}
	
	private void checkParmNotEmptyRecord(FunctionParameter functionParameter, ITypeBinding parmType) {
		boolean isEmptyRecord = false;
    	if(ITypeBinding.FIXED_RECORD_BINDING == parmType.getKind()) {
    		isEmptyRecord = ((FixedRecordBinding) parmType).getStructureItems().isEmpty();
    	}
    	else if(ITypeBinding.FLEXIBLE_RECORD_BINDING == parmType.getKind()) {
    		isEmptyRecord = ((FlexibleRecordBinding) parmType).getDeclaredFields().isEmpty();
    	}
    	
    	if(isEmptyRecord) {
    		problemRequestor.acceptProblem(
    			functionParameter.getType(),
				IProblemRequestor.RECORD_PARAMETER_WITH_NO_CONTENTS,
				new String[] {functionParameter.getName().getCanonicalName(), functionParameter.getType().getCanonicalName()});
    	}
	}
	
	private void checkConstParmNotField(FunctionParameter functionParameter) {
		if (FunctionParameter.AttrType.FIELD == functionParameter.getAttrType()
				&& functionParameter.isParmConst()) {
			
			problemRequestor.acceptProblem(
					functionParameter,
					IProblemRequestor.FUNCTION_PARM_CONST_AND_FIELD_MUTEX,
					new String[] {
						functionParameter.getName().getCanonicalName(),
						functionName});
		}
	}
		
	private void checkRecordParmNotFieldOrNullable(FunctionParameter functionParameter, ITypeBinding parmType) {
		if(FunctionParameter.AttrType.FIELD == functionParameter.getAttrType() ||
		   FunctionParameter.AttrType.SQLNULLABLE == functionParameter.getAttrType()) {
			if(ITypeBinding.FIXED_RECORD_BINDING == parmType.getKind() ||
			   ITypeBinding.FLEXIBLE_RECORD_BINDING == parmType.getKind()) {
				problemRequestor.acceptProblem(
					functionParameter,
					functionParameter.getAttrType() == FunctionParameter.AttrType.FIELD ?
						IProblemRequestor.FUNCTION_PARAMETER_TYPE_CANNOT_BE_FIELD :
						IProblemRequestor.FUNCTION_PARAMETER_TYPE_CANNOT_BE_NULLABLE,
					new String[] {
						functionParameter.getName().getCanonicalName(),
						IEGLConstants.KEYWORD_RECORD,
						functionName});
			}
    	}
	}
	
	private void checkParmNotPSBRecord(FunctionParameter functionParameter, ITypeBinding parmType) {
		if(parmType.getAnnotation(new String[] {"egl", "io", "dli"}, "PSBRecord") != null) {
			problemRequestor.acceptProblem(
				functionParameter.getType(),
				IProblemRequestor.DLI_PSBRECORD_NOT_VALID_AS_PARAMETER);
		}
	}
	
	private void checkTypeNotServiceOrInterfaceArray(FunctionParameter functionParameter, ITypeBinding parmType) {
		if(ITypeBinding.ARRAY_TYPE_BINDING == parmType.getKind()) {
			ITypeBinding elementType = ((ArrayTypeBinding) parmType).getBaseType();
			if(ITypeBinding.SERVICE_BINDING == elementType.getKind() ||
			   (ITypeBinding.INTERFACE_BINDING == elementType.getKind() &&
			   	SystemPartManager.getInstance().findType(elementType.getName()) != elementType)) {
				problemRequestor.acceptProblem(
					functionParameter.getType(),
					IProblemRequestor.SERVICE_OR_INTERFACE_ARRAYS_NOT_SUPPORTED);
			}
		}
	}
	
	private void checkNullableParm(FunctionParameter functionParameter, ITypeBinding parmType) {
		if(ITypeBinding.PRIMITIVE_TYPE_BINDING == parmType.getKind()) {
			PrimitiveTypeBinding primTypeBinding = (PrimitiveTypeBinding) parmType;
			Primitive prim = primTypeBinding.getPrimitive();
			
			if(Primitive.BIGINT == prim	|| Primitive.MBCHAR == prim	||
			   Primitive.NUM == prim	|| Primitive.NUMC == prim	||
			   Primitive.PACF == prim) {
				problemRequestor.acceptProblem(
					functionParameter,
					IProblemRequestor.FUNCTION_PARAMETER_TYPE_CANNOT_BE_NULLABLE,
					new String[] {
						functionParameter.getName().getCanonicalName(),
						prim.getName(),
						functionName
					});
			}
			else if(Primitive.BIN == prim) {
				if(primTypeBinding.getLength() == 18 || primTypeBinding.getDecimals() != 0) {
					problemRequestor.acceptProblem(
						functionParameter,
						IProblemRequestor.FUNCTION_PARAMETER_BIN_CANNOT_BE_NULLABLE,
						new String[] {
							functionParameter.getName().getCanonicalName(),
							prim.getName(),
							functionName
						});
				}
			}
		}
	}
	
	private void checkInputOrOutputOrInputOutputParm(FunctionParameter functionParameter, ITypeBinding parmType) {

	}
	
	private void checkInputOrOutputParm(FunctionParameter functionParameter, ITypeBinding parmType) {
		checkInputOrOutputOrInputOutputParm(functionParameter, parmType);		
	}
	
	private void checkInputParm(FunctionParameter functionParameter, ITypeBinding parmType) {
		checkInputOrOutputParm(functionParameter, parmType);
		if(BindingUtilities.isLooseType(parmType)) {
        	problemRequestor.acceptProblem(
        		functionParameter,
				IProblemRequestor.IN_OR_OUT_MODIFIER_NOT_ALLOWED_WITH_ANY_TYPE,
				new String[] {IEGLConstants.KEYWORD_IN.toUpperCase()});
		}
		if (functionParameter.isParmConst()) {
			validatePrimitiveConst(functionParameter.getType());
		}
	}
	
	private void checkForExternalTypeParm(FunctionParameter functionParameter, ITypeBinding parmType) {
		if (!Binding.isValidBinding(parmType)) {
			return;
		}
		
		if (parmType.getKind() == ITypeBinding.EXTERNALTYPE_BINDING ) {
			problemRequestor.acceptProblem(
            		functionParameter,
					IProblemRequestor.IN_MODIFIER_REQUIRED_FOR_EXTERNALTYPE,
					new String[] {functionParameter.getName().getCanonicalString()});
		}
		
		
	}

	private void checkOutputParm(FunctionParameter functionParameter, ITypeBinding parmType) {
		checkInputOrOutputParm(functionParameter, parmType);
		checkForExternalTypeParm(functionParameter, parmType);
		if(ITypeBinding.PRIMITIVE_TYPE_BINDING == parmType.getKind()) {
			switch(((PrimitiveTypeBinding) parmType).getPrimitive().getType()) {
			case Primitive.NUMBER_PRIMITIVE:
				problemRequestor.acceptProblem(
            		functionParameter,
					IProblemRequestor.FUNCTION_PARAMETER_MODIFIER_NOT_ALLOWED_WITH_LOOSE_TYPE,
					new String[] {IEGLConstants.KEYWORD_OUT.toUpperCase(), IEGLConstants.KEYWORD_NUMBER.toUpperCase()});
				break;
			case Primitive.INTERVAL_PRIMITIVE:
				if(((PrimitiveTypeBinding) parmType).getTimeStampOrIntervalPattern() == null) {
					problemRequestor.acceptProblem(
	            		functionParameter,
						IProblemRequestor.FUNCTION_PARAMETER_MODIFIER_NOT_ALLOWED_WITH_LOOSE_TYPE,
						new String[] {IEGLConstants.KEYWORD_OUT.toUpperCase(), IEGLConstants.KEYWORD_INTERVAL.toUpperCase()});
				}
				break;
			}
		}
		if (functionParameter.isParmConst()) {
			problemRequestor.acceptProblem(
            		functionParameter,
					IProblemRequestor.CONST_MODIFIER_NOT_ALLOWED_WITH_IN_MODIFIER,
					new String[] {IEGLConstants.KEYWORD_OUT.toUpperCase(), IEGLConstants.KEYWORD_INTERVAL.toUpperCase()});
		}
	}
	
	private void checkExplicitInputOutputParm(FunctionParameter functionParameter, ITypeBinding parmType) {
		checkInputOrOutputOrInputOutputParm(functionParameter, parmType);
		checkForExternalTypeParm(functionParameter, parmType);
		
		if (functionParameter.isParmConst()) {
			validatePrimitiveConst(functionParameter.getType());
		}

	}
	
	private void checkImplicitInputOutputParm(FunctionParameter functionParameter, ITypeBinding parmType) {
		checkForExternalTypeParm(functionParameter, parmType);
		
		if (functionParameter.isParmConst()) {
			validatePrimitiveConst(functionParameter.getType());
		}

	}


	private void checkInputOrOutputParm(FunctionParameter functionParameter) {
		if(functionParameter.getAttrType() == FunctionParameter.AttrType.FIELD) {
			problemRequestor.acceptProblem(
				functionParameter,
				IProblemRequestor.FUNCTION_PARAMETER_OUT_NOT_ALLOWED_WITH_FIELD,
				new String[] {functionParameter.getUseType().toString().toUpperCase()});
		}
	}
	
	private void checkInputParm(FunctionParameter functionParameter) {
		checkInputOrOutputParm(functionParameter);
	}
	
	private void checkOutputParm(FunctionParameter functionParameter) {
		checkInputOrOutputParm(functionParameter);
	}
	
	private void checkPartSpecificConditions(FunctionParameter functionParameter, FunctionParameterBinding parmBinding, IPartBinding enclosingPart) {
		ITypeBinding parmType = parmBinding.getType();
		if(parmType != null && parmType != IBinding.NOT_FOUND_BINDING) {
			if(enclosingPart.getAnnotation(new String[] {"egl", "ui", "jasper"}, "JasperReport") != null) {
				if(ITypeBinding.PRIMITIVE_TYPE_BINDING != parmType.getKind()) {
					problemRequestor.acceptProblem(
						functionParameter.getType(),
						IProblemRequestor.ONLY_DATAITEMS_ALLOWED_AS_PARAMETER_OR_RETURN_IN_REPORTHANDLER);
				}
			}
		}
	}
	
	private void preVisitStatement(Statement statement) {
		preVisitStatement(statement, true);
	}
	
	private void preVisitStatement(Statement statement, boolean issueWarningIfUnreachable) {
		if(issueWarningIfUnreachable && nextStatementIsUnreachable) {
			problemRequestor.acceptProblem(
				statement,
				IProblemRequestor.UNREACHABLE_CODE,
				IMarker.SEVERITY_WARNING);
			nextStatementIsUnreachable = false;
		}
		
		labelPrecedingStatement = null;
	}
	
	private void postVisitStatement(Statement statement) {
		if(statement.canIncludeOtherStatements()) {
			for(Iterator iter = statement.getStatementBlocks().iterator(); iter.hasNext();) {
				for(Iterator stmtIter = ((List) iter.next()).iterator(); stmtIter.hasNext();) {
					((Node) stmtIter.next()).accept(this);
				}
				nextStatementIsUnreachable = false;
			}
		}
	}
	
	private boolean checkStatementAllowedInContainer(Statement statement) {
		if(statementValidInContainerInfo != null && !statementValidInContainerInfo.statementIsValidInContainer(statement)) {
			problemRequestor.acceptProblem(statement, statementValidInContainerInfo.getProblemKind(), new String[] {StatementValidator.getName(statement)});
			return false;
		}
		return true;
	}
	
	public boolean visit(AddStatement addStatement) {
		preVisitStatement(addStatement);
		if (checkStatementAllowedInContainer(addStatement)) {
			addStatement.accept(new AddStatementValidator(problemRequestor));
		}
		postVisitStatement(addStatement);
		return false;
	}

	public boolean visit(AssignmentStatement assignmentStatement) {
		preVisitStatement(assignmentStatement);
		if (checkStatementAllowedInContainer(assignmentStatement)) {
			assignmentStatement.accept(new AssignmentStatementValidator(problemRequestor, compilerOptions, enclosingPart));
		}
		postVisitStatement(assignmentStatement);
		return false;
	}

	public boolean visit(CallStatement callStatement) {
		preVisitStatement(callStatement);
		if (checkStatementAllowedInContainer(callStatement)) {
			callStatement.accept(new CallStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(callStatement);
		return false;
	}

	public boolean visit(CaseStatement caseStatement) {
		if(labelPrecedingStatement != null) {
			labeledLoops.put(caseStatement, labelPrecedingStatement);
		}
		preVisitStatement(caseStatement);
		if (checkStatementAllowedInContainer(caseStatement)) {
			caseStatement.accept(new CaseStatementValidator(problemRequestor, compilerOptions));
		}		
		postVisitStatement(caseStatement);
		return false;
	}

	public boolean visit(CloseStatement closeStatement) {
		preVisitStatement(closeStatement);
		if (checkStatementAllowedInContainer(closeStatement)) {
			closeStatement.accept(new CloseStatementValidator(problemRequestor));
		}
		postVisitStatement(closeStatement);
		return false;
	}

	public boolean visit(ContinueStatement continueStatement) {
		preVisitStatement(continueStatement);
		if (checkStatementAllowedInContainer(continueStatement)) {
			continueStatement.accept(new ContinueStatementValidator(problemRequestor, labeledLoops));
			nextStatementIsUnreachable = true;
		}
		postVisitStatement(continueStatement);
		return false;
	}

	public boolean visit(ConverseStatement converseStatement) {
		preVisitStatement(converseStatement);
		if (checkStatementAllowedInContainer(converseStatement)) {
			converseStatement.accept(new ConverseStatementValidator(problemRequestor, enclosingPart));
		}
		postVisitStatement(converseStatement);
		return false;
	}

	public boolean visit(DeleteStatement deleteStatement) {
		preVisitStatement(deleteStatement);
		if (checkStatementAllowedInContainer(deleteStatement)) {
			deleteStatement.accept(new DeleteStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(deleteStatement);
		return false;
	}

	public boolean visit(DisplayStatement displayStatement) {
		preVisitStatement(displayStatement);
		if (checkStatementAllowedInContainer(displayStatement)) {
			displayStatement.accept(new DisplayStatementValidator(problemRequestor, enclosingPart, compilerOptions));
		}
		postVisitStatement(displayStatement);
		return false;
	}

	public boolean visit(ExecuteStatement executeStatement) {
		preVisitStatement(executeStatement);
		if (checkStatementAllowedInContainer(executeStatement)) {
			executeStatement.accept(new ExecuteStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(executeStatement);
		return false;
	}

	public boolean visit(EmptyStatement emptyStatement) {
		preVisitStatement(emptyStatement, false);
		if (checkStatementAllowedInContainer(emptyStatement)) {
		}
		postVisitStatement(emptyStatement);
		return false;
	}

	public boolean visit(ExitStatement exitStatement) {
		preVisitStatement(exitStatement);
		if (checkStatementAllowedInContainer(exitStatement)) {
			exitStatement.accept(new ExitStatementValidator(problemRequestor, labeledLoops, enclosingPart, compilerOptions));
			nextStatementIsUnreachable = true;
		}
		postVisitStatement(exitStatement);
		return false;
	}

	public boolean visit(ForEachStatement forEachStatement) {
		if(labelPrecedingStatement != null) {
			labeledLoops.put(forEachStatement, labelPrecedingStatement);
		}
		preVisitStatement(forEachStatement);
		if (checkStatementAllowedInContainer(forEachStatement)) {
			forEachStatement.accept(new ForEachStatementValidator(problemRequestor));
		}		
		postVisitStatement(forEachStatement);
		return false;
	}

	public boolean visit(ForStatement forStatement) {
		if(labelPrecedingStatement != null) {
			labeledLoops.put(forStatement, labelPrecedingStatement);
		}
		preVisitStatement(forStatement);
		if (checkStatementAllowedInContainer(forStatement)) {
			forStatement.accept(new ForStatementValidator(problemRequestor, compilerOptions));
		}		
		postVisitStatement(forStatement);
		return false;
	}

	public boolean visit(ForwardStatement forwardStatement) {
		preVisitStatement(forwardStatement);
		if (checkStatementAllowedInContainer(forwardStatement)) {
			forwardStatement.accept(new ForwardStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(forwardStatement);
		return false;
	}

	public boolean visit(FreeSQLStatement freeSQLStatement) {
		preVisitStatement(freeSQLStatement);
		if (checkStatementAllowedInContainer(freeSQLStatement)) {
			freeSQLStatement.accept(new FreeSQLStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(freeSQLStatement);
		return false;
	}

	public boolean visit(FunctionInvocationStatement functionInvocationStatement) {
		preVisitStatement(functionInvocationStatement);
		if (checkStatementAllowedInContainer(functionInvocationStatement)) {
		}
		postVisitStatement(functionInvocationStatement);
		return false;
	}

	public boolean visit(FunctionDataDeclaration functionDataDeclaration) {
		preVisitStatement(functionDataDeclaration);
		if (checkStatementAllowedInContainer(functionDataDeclaration)) {
			functionDataDeclaration.accept(new FunctionDataDeclarationValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(functionDataDeclaration);
		return false;
	}

	public boolean visit(GetByKeyStatement getByKeyStatement) {
		preVisitStatement(getByKeyStatement);
		if (checkStatementAllowedInContainer(getByKeyStatement)) {
			getByKeyStatement.accept(new GetByKeyStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(getByKeyStatement);
		return false;
	}

	public boolean visit(GetByPositionStatement getByPositionStatement) {
		preVisitStatement(getByPositionStatement);
		if (checkStatementAllowedInContainer(getByPositionStatement)) {
			getByPositionStatement.accept(new GetByPositionStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(getByPositionStatement);
		return false;
	}

	public boolean visit(GotoStatement gotoStatement) {
		preVisitStatement(gotoStatement);
		if (checkStatementAllowedInContainer(gotoStatement)) {
			gotoStatement.accept(new GotoStatementValidator(problemRequestor, compilerOptions));
			gotoList.add(gotoStatement);
		}
		postVisitStatement(gotoStatement);
		return false;
	}

	public boolean visit(IfStatement ifStatement) {
		if(labelPrecedingStatement != null) {
			labeledLoops.put(ifStatement, labelPrecedingStatement);
		}
		preVisitStatement(ifStatement);
		if (checkStatementAllowedInContainer(ifStatement)) {
			ifStatement.accept(new IfStatementValidator(problemRequestor));
		}
		postVisitStatement(ifStatement);
		return false;
	}

	public boolean visit(LabelStatement labelStatement) {
		nextStatementIsUnreachable = false;
		preVisitStatement(labelStatement);
		labelPrecedingStatement = labelStatement;
		if (checkStatementAllowedInContainer(labelStatement)) {
			labelStatement.accept(new LabelStatementValidator(problemRequestor, compilerOptions));
			if (labelMap.containsKey(labelStatement.getLabel())) {
				problemRequestor.acceptProblem(labelStatement, IProblemRequestor.DUPLICATE_LABEL, new String[] {
						labelStatement.getLabel(), this.functionName });
			} else {
				labelMap.put(labelStatement.getLabel(), labelStatement);
			}
		}
		postVisitStatement(labelStatement);
		return false;
	}

	public boolean visit(MoveStatement moveStatement) {
		preVisitStatement(moveStatement);
		if (checkStatementAllowedInContainer(moveStatement)) {
			moveStatement.accept(new MoveStatementValidator(problemRequestor, compilerOptions, enclosingPart));
		}
		postVisitStatement(moveStatement);
		return false;
	}

	public boolean visit(OpenStatement openStatement) {
		preVisitStatement(openStatement);
		if (checkStatementAllowedInContainer(openStatement)) {
			openStatement.accept(new OpenStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(openStatement);
		return false;
	}

	public boolean visit(OpenUIStatement openUIStatement) {
		preVisitStatement(openUIStatement);
		if (checkStatementAllowedInContainer(openUIStatement)) {
			openUIStatement.accept(new OpenUIStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(openUIStatement);
		return false;
	}

	public boolean visit(PrepareStatement prepareStatement) {
		preVisitStatement(prepareStatement);
		if (checkStatementAllowedInContainer(prepareStatement)) {
			prepareStatement.accept(new PrepareStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(prepareStatement);
		return false;
	}

	public boolean visit(PrintStatement printStatement) {
		preVisitStatement(printStatement);
		if (checkStatementAllowedInContainer(printStatement)) {
			printStatement.accept(new PrintStatementValidator(problemRequestor, enclosingPart));
		}
		postVisitStatement(printStatement);
		return false;
	}

	public boolean visit(ReplaceStatement replaceStatement) {
		preVisitStatement(replaceStatement);
		if (checkStatementAllowedInContainer(replaceStatement)) {
			replaceStatement.accept(new ReplaceStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(replaceStatement);
		return false;
	}

	public boolean visit(ReturnStatement returnStatement) {
		preVisitStatement(returnStatement);
		if (checkStatementAllowedInContainer(returnStatement)) {
			returnStatement.accept(new ReturnStatementValidator(problemRequestor, compilerOptions));
		}
		postVisitStatement(returnStatement);
		return false;
	}

	public boolean visit(SetStatement setStatement) {
		preVisitStatement(setStatement);
		if (checkStatementAllowedInContainer(setStatement)) {
			setStatement.accept(new SetStatementValidator(problemRequestor, enclosingPart, compilerOptions));
		}
		postVisitStatement(setStatement);
		return false;
	}

	public boolean visit(SetValuesStatement setValuesStatement) {
		preVisitStatement(setValuesStatement);
		if (checkStatementAllowedInContainer(setValuesStatement)) {
		}
		postVisitStatement(setValuesStatement);
		return false;
	}

	public boolean visit(ShowStatement showStatement) {
		preVisitStatement(showStatement);
		if (checkStatementAllowedInContainer(showStatement)) {
			showStatement.accept(new ShowStatementValidator(problemRequestor, enclosingPart, compilerOptions));
		}
		postVisitStatement(showStatement);
		return false;
	}

	public boolean visit(ThrowStatement throwStatement) {
		preVisitStatement(throwStatement);
		if (checkStatementAllowedInContainer(throwStatement)) {
			throwStatement.accept(new ThrowStatementValidator(problemRequestor, enclosingPart));
		}
		postVisitStatement(throwStatement);
		return false;
	}

	public boolean visit(TransferStatement transferStatement) {
		preVisitStatement(transferStatement);
		if (checkStatementAllowedInContainer(transferStatement)) {
			transferStatement.accept(new TransferStatementValidator(problemRequestor, enclosingPart, compilerOptions));
		}
		postVisitStatement(transferStatement);
		return false;
	}

	public boolean visit(TryStatement tryStatement) {
		preVisitStatement(tryStatement);
		if (checkStatementAllowedInContainer(tryStatement)) {
			tryStatement.accept(new TryStatementValidator(problemRequestor, enclosingPart));
		}
		postVisitStatement(tryStatement);
		return false;
	}

	public boolean visit(WhileStatement whileStatement) {
		if(labelPrecedingStatement != null) {
			labeledLoops.put(whileStatement, labelPrecedingStatement);
		}
		preVisitStatement(whileStatement);
		if (checkStatementAllowedInContainer(whileStatement)) {
			whileStatement.accept(new WhileStatementValidator(problemRequestor));
		}		
		postVisitStatement(whileStatement);
		return false;
	}
	
	public void endVisit(TopLevelFunction topLevelFunction) {
		validateGotoLabels();
	}
	
	public void endVisit(NestedFunction nestedFunction) {
		validateGotoLabels();
	}
	
	private void validateGotoLabels(){
		Iterator iter = gotoList.iterator();
		while(iter.hasNext()){
			GotoStatement gotoStatement = (GotoStatement)iter.next();
			if (!labelMap.containsKey(gotoStatement.getLabel())){
				problemRequestor.acceptProblem(gotoStatement,
						IProblemRequestor.GOTO_LABEL_IS_UNDEFINED,
						new String[]{gotoStatement.getLabel(),functionName});
			}
			else {
				if(!inParents(gotoStatement, ((Node) labelMap.get(gotoStatement.getLabel())).getParent())){
					problemRequestor.acceptProblem(gotoStatement,
						IProblemRequestor.GOTO_LABEL_NOT_ACCESSIBLE,
						new String[]{gotoStatement.getLabel(),functionName});
				}
//				Node gotoOnEventBlock = getEnclosingOnEventBlock(gotoStatement);
//				Node labelOnEventBlock = getEnclosingOnEventBlock((Node) labelMap.get(gotoStatement.getLabel()));
//				if(gotoOnEventBlock == null) {
//					if(labelOnEventBlock != null) {
//						problemRequestor.acceptProblem(gotoStatement, IProblemRequestor.GOTO_LABEL_INSIDE_ONEVENT_BLOCK);
//					}
//				}
//				else {					
//					if(labelOnEventBlock != gotoOnEventBlock) {
//						problemRequestor.acceptProblem(gotoStatement, IProblemRequestor.GOTO_LABEL_OUTSIDE_ONEVENT_BLOCK);
//					}
//				}
			}
		}
	}
	

	private boolean inParents(Node child, Node node) {
		while(child != null) {
			if(child == node) {
				return true;
			}
			child = child.getParent();
		}
		return false;
	}

	private Node getEnclosingOnEventBlock(Node node) {
		Node parent = node.getParent();
		while(parent != null && !(parent instanceof OnEventBlock)) {
			parent = parent.getParent();
		}
		return parent instanceof OnEventBlock ? parent : null;
	}
	
	private void validatePrimitiveConst(Type type){
		StatementValidator.validatePrimitiveConstant(type, problemRequestor);
	}

	
}
