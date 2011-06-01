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
package org.eclipse.edt.ide.compiler.gen;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.edt.gen.Constants;
import org.eclipse.edt.gen.Generator;
import org.eclipse.edt.gen.generator.javascript.EGL2JavaScript;
import org.eclipse.edt.ide.core.IGenerator;
import org.eclipse.edt.ide.core.utils.EclipseUtilities;
import org.eclipse.edt.mof.codegen.api.TabbedReportWriter;
import org.eclipse.edt.mof.egl.Part;
import org.eclipse.edt.mof.egl.utils.LoadPartException;

/**
 * Subclass of the base Javascript generator command to do certain things in the Eclipse way.
 */
public class EclipseEGL2JavaScript extends EGL2JavaScript {

	private final IFile eglFile;
	private final Part part;
	private final IGenerator generatorProvider;

	public EclipseEGL2JavaScript(IFile eglFile, Part part, IGenerator generator) {
		super();
		this.eglFile = eglFile;
		this.part = part;
		this.generatorProvider = generator;
	}

	protected List<Part> loadEGLParts() throws LoadPartException {
		List<Part> parts = new ArrayList<Part>();
		parts.add(part);
		return parts;
	}

	protected void writeFile(Part part, Generator generator) throws Exception {
		String outputFolder = (String) parameterMapping.get(Constants.parameter_output).getValue();
		if (EclipseUtilities.shouldWriteFileInEclipse(outputFolder)) {
			IFile outputFile = EclipseUtilities.writeFileInEclipse(part, outputFolder, eglFile, generator.getResult(), generator.getRelativeFileName(part));

			// write out generation report if there is one
			TabbedReportWriter report = generator.getReport();
			if (report != null) {
				{
					String fn = generator.getRelativeFileName(part);
					fn = fn.substring(0, fn.lastIndexOf('.')) + Constants.report_fileExtension;
					String rpt = report.rpt.getWriter().toString();
					IFile reportFile = EclipseUtilities.writeFileInEclipse(part, outputFolder, eglFile, rpt, fn);
				}
			}

			// make sure it's a source folder
			EclipseUtilities.addToJavaBuildPathIfNecessary(outputFile.getProject(), outputFolder);

			// Add required runtimes.
			EclipseUtilities.addRuntimesToProject(outputFile.getProject(), generatorProvider.getRuntimeContainers());

			// call back to the generator, to see if it wants to do any supplementary tasks
			generator.processFile(outputFile.getFullPath().toString());
		} else {
			// super's method handles writing to an absolute file system path.
			super.writeFile(part, generator);
		}
	}
}
