/*******************************************************************************
 * Copyright © 2011, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.compiler.gen;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.edt.compiler.internal.interfaces.IGenerationMessageRequestor;
import org.eclipse.edt.compiler.internal.util.ExpressionParenter;
import org.eclipse.edt.ide.compiler.EDTCompilerIDEPlugin;
import org.eclipse.edt.ide.core.AbstractGenerator;
import org.eclipse.edt.ide.core.EDTRuntimeContainer;
import org.eclipse.edt.mof.egl.Part;
import org.eclipse.edt.mof.egl.utils.CompoundConditionExpander;
import org.eclipse.edt.mof.serialization.IEnvironment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Provides know-how for hooking the Java Generator into the IDE.
 */
public class JavaGenerator extends AbstractGenerator {
	
	public JavaGenerator() {
		super();
	}
	
	public EDTRuntimeContainer[] resolveBaseRuntimeContainers() {
		return new EDTRuntimeContainer[] {EDTCompilerIDEPlugin.JAVA_RUNTIME_CONTAINER};
	}
	
	@Override
	public void generate(String filePath, Part part, IEnvironment env, IGenerationMessageRequestor msgRequestor) throws Exception {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filePath));
		preprocess(part);
		EclipseEGL2Java cmd = new EclipseEGL2Java(file, part, this);
		cmd.generate(buildArgs(file, part), new EclipseJavaGenerator(cmd, msgRequestor), env, null);
	}
	
	private void preprocess(Part part) {
		//expand complex conditional expressions that contain function invocations
		new CompoundConditionExpander(part);
		
		//add statament information to expressions
		new ExpressionParenter(part);
	}
	
	@Override
	public boolean supportsProject(IProject project) {
		try {
			return project.hasNature(JavaCore.NATURE_ID);
		}
		catch (CoreException ce) {
			return false;
		}
	}
	
	@Override
	protected String getGenerationDirectoryPropertyKey() {
		return EDTCompilerIDEPlugin.PROPERTY_JAVAGEN_DIR;
	}

	@Override
	public String getProjectSettingsPluginId() {
		return EDTCompilerIDEPlugin.PLUGIN_ID;
	}

	@Override
	protected String getGenerationDirectoryPreferenceKey() {
		return EDTCompilerIDEPlugin.PREFERENCE_DEFAULT_JAVAGEN_DIRECTORY;
	}

	@Override
	protected IPreferenceStore getPreferenceStore() {
		return EDTCompilerIDEPlugin.getDefault().getPreferenceStore();
	}

	@Override
	protected String getRelativeFilePath(IFile eglFile, Part part) {
		return new EclipseJavaGenerator(new EclipseEGL2Java(eglFile, part, this), null).getRelativeFileName(part);
	}

	@Override
	protected String getGenerationArgumentsPropertyKey() {
		return EDTCompilerIDEPlugin.PROPERTY_JAVAGEN_ARGUMENTS;
	}
}
