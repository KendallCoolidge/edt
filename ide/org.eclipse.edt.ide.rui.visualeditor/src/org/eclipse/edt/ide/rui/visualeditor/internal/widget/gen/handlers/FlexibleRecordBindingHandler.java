/*******************************************************************************
 * Copyright © 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.rui.visualeditor.internal.widget.gen.handlers;

import org.eclipse.edt.compiler.binding.FlexibleRecordBinding;
import org.eclipse.edt.compiler.binding.IAnnotationBinding;
import org.eclipse.edt.compiler.binding.IDataBinding;
import org.eclipse.edt.compiler.binding.ITypeBinding;
import org.eclipse.edt.ide.core.internal.lookup.workingcopy.WorkingCopyProjectEnvironmentManager;
import org.eclipse.edt.ide.rui.visualeditor.internal.wizards.insertwidget.InsertDataModel;
import org.eclipse.edt.ide.rui.visualeditor.internal.wizards.insertwidget.InsertDataNode;
import org.eclipse.edt.ide.rui.visualeditor.internal.wizards.insertwidget.InsertDataNodeFactory;
import org.eclipse.edt.ide.rui.visualeditor.internal.wizards.insertwidget.InsertDataModel.Context;

public class FlexibleRecordBindingHandler extends DataTypeBindingHandler {
	
	public void handle(IDataBinding dataBinding, ITypeBinding typeBinding, InsertDataModel insertDataModel){
		//create self
		FlexibleRecordBinding flexibleRecordBinding = (FlexibleRecordBinding)typeBinding;
		String bindingName = (String)insertDataModel.getContext().get(Context.BINDING_NAME);
		InsertDataNode insertDataNode = InsertDataNodeFactory.newInsertDataNode(insertDataModel, bindingName, dataBinding.getType().getName());
		insertDataNode.setArray(false);
		insertDataNode.setContainer(true);
		insertDataNode.setNodeType(InsertDataNode.NodeType.TYPE_RECORD_ALL);
		insertDataNode.addNodeTypeDetail(InsertDataNode.NodeTypeDetail.TYPE_RECORD_SIMPLE);
		//set annotation
		IAnnotationBinding displayNameAnnotationBinding = dataBinding.getAnnotation(BindingHandlerHelper.EGLUI, BindingHandlerHelper.ANNOTATION_DISPLAYNAME);
		if(displayNameAnnotationBinding != null){
			insertDataNode.setLabelText((String)displayNameAnnotationBinding.getValue());
		}
//		IAnnotationBinding displayUseAnnotationBinding = dataBinding.getAnnotation(BindingHandlerHelper.EGLUIRUI, BindingHandlerHelper.ANNOTATION_DISPLAYUSE);
//		if(displayUseAnnotationBinding != null){
//			insertDataNode.setDefaultWidgetType((String)displayUseAnnotationBinding.getValue());
//		}
		
		//add to model tree
		Object parent = insertDataModel.getContext().get(Context.PARENT_INSERT_DATA_NODE);
		//simple record
		if(parent == null){
			insertDataModel.addRootDataNode(insertDataNode);
		}
		//complex record
		else{
			InsertDataNode parentInsertDataNode = (InsertDataNode)parent;
			//record array
			if(parentInsertDataNode.isArray()){
				if(parentInsertDataNode.getBindingName().equals(insertDataNode.getBindingName())){
					insertDataNode = parentInsertDataNode;
					insertDataNode.addNodeTypeDetail(InsertDataNode.NodeTypeDetail.TYPE_RECORD_SIMPLE);
				}
				else{
					parentInsertDataNode.addChild(insertDataNode);
					parentInsertDataNode.removeNodeTypeDetail(InsertDataNode.NodeTypeDetail.TYPE_RECORD_SIMPLE);
					parentInsertDataNode.addNodeTypeDetail(InsertDataNode.NodeTypeDetail.TYPE_RECORD_EMBED_RECORD);
				}
			}
			//record embed record
			else{
				parentInsertDataNode.addChild(insertDataNode);
				parentInsertDataNode.removeNodeTypeDetail(InsertDataNode.NodeTypeDetail.TYPE_RECORD_SIMPLE);
				parentInsertDataNode.addNodeTypeDetail(InsertDataNode.NodeTypeDetail.TYPE_RECORD_EMBED_RECORD);
			}
		}		
		WorkingCopyProjectEnvironmentManager.getInstance().initialize();
		//process children
		IDataBinding[] dataBindings = flexibleRecordBinding.getFields();
		for(int i=0; i<dataBindings.length; i++){
			IDataBinding cDataBinding = dataBindings[i];
			StringBuffer sbBindName = new StringBuffer(bindingName).append(".").append(cDataBinding.getCaseSensitiveName());
			insertDataModel.getContext().set(Context.BINDING_NAME, sbBindName.toString());
			//set self as parent
			insertDataModel.getContext().set(Context.PARENT_INSERT_DATA_NODE, insertDataNode);
			BindingHandlerManager.getInstance().handle(cDataBinding, cDataBinding.getType(), insertDataModel);			
		}
	}
}
