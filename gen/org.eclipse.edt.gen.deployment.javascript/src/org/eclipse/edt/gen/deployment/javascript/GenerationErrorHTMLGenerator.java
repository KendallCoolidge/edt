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
package org.eclipse.edt.gen.deployment.javascript;

import org.eclipse.edt.compiler.ISystemEnvironment;
import org.eclipse.edt.gen.AbstractGeneratorCommand;
import org.eclipse.edt.gen.GenerationException;
import org.eclipse.edt.gen.deployment.javascript.templates.JavaScriptTemplate;
import org.eclipse.edt.gen.deployment.util.WorkingCopyGenerationResult;
import org.eclipse.edt.mof.egl.Part;

public class GenerationErrorHTMLGenerator extends ErrorHTMLGenerator {
	
	public GenerationErrorHTMLGenerator(AbstractGeneratorCommand processor, WorkingCopyGenerationResult requestor, ISystemEnvironment sysEnv) {
		super(processor, requestor, sysEnv);
	}
	
	@Override
	public void generate(Part part) throws GenerationException {
		this.generate(part, JavaScriptTemplate.genErrorHTML);
	}
}
