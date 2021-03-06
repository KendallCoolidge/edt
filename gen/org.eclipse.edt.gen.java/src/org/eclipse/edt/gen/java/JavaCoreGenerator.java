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
package org.eclipse.edt.gen.java;

import java.io.IOException;

import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.compiler.internal.interfaces.IGenerationMessageRequestor;
import org.eclipse.edt.compiler.internal.util.IGenerationResultsMessage;
import org.eclipse.edt.gen.AbstractGeneratorCommand;
import org.eclipse.edt.gen.CommonUtilities;
import org.eclipse.edt.gen.GenerationException;
import org.eclipse.edt.gen.Generator;
import org.eclipse.edt.gen.EGLMessages.EGLMessage;
import org.eclipse.edt.gen.java.templates.JavaTemplate;
import org.eclipse.edt.javart.util.JavaAliaser;
import org.eclipse.edt.mof.codegen.api.TabbedReportWriter;
import org.eclipse.edt.mof.codegen.api.TabbedWriter;
import org.eclipse.edt.mof.codegen.api.TemplateException;
import org.eclipse.edt.mof.egl.Annotation;
import org.eclipse.edt.mof.egl.Element;
import org.eclipse.edt.mof.egl.Part;

public class JavaCoreGenerator extends Generator {

	protected Context context;
	protected TabbedWriter out;
	protected AbstractGeneratorCommand generator;

	public JavaCoreGenerator(AbstractGeneratorCommand processor) {
		this(processor, null);
	}

	public JavaCoreGenerator(AbstractGeneratorCommand processor, IGenerationMessageRequestor requestor) {
		super(processor, requestor);
		generator = processor;
	}

	public String getResult() {
		if (out == null)
			return "";
		return out.getWriter().toString();
	}

	@Override
	public TabbedReportWriter getReport() {
		return (out instanceof TabbedReportWriter) ? (TabbedReportWriter) out : null;
	}

	public Context makeContext(AbstractGeneratorCommand processor) {
		context = new Context(processor);
		return context;
	}

	public void generate(Object part) throws GenerationException {
		makeWriter();
		if (!verifyPartSupported(part, "Java")) {
			return;
		}
		try {
			context.putAttribute(context.getClass(), Constants.SubKey_partBeingGenerated, part);
			context.invoke(JavaTemplate.preGenPart, part, context);
			if (!context.getMessageRequestor().isError()) {
				out.getWriter().flush();
				
				// Add the header before anything else so that SMAP lines are not thrown off by adding it later.
				boolean autoIndent = out.getAutoIndent();
				out.setAutoIndent(false);
				if (getHeader() != null && getHeader().length() > 0) {
					out.println(getHeader());
				}
				out.setAutoIndent(autoIndent);
				
				// get the egl file being processed
				String eglFileName = ((Part) part).getFileName();
				if (eglFileName.indexOf('\\') >= 0)
					eglFileName = eglFileName.substring(eglFileName.lastIndexOf('\\') + 1);
				if (eglFileName.indexOf('/') >= 0)
					eglFileName = eglFileName.substring(eglFileName.lastIndexOf('/') + 1);
				// now we have a file name, without the path
				String fileName = eglFileName;
				if (fileName.indexOf('.') >= 0)
					fileName = fileName.substring(0, fileName.lastIndexOf('.'));
				context.getSmapData().append(JavaAliaser.getAlias(fileName) + getFileExtension() + Constants.smap_stratum);
				// we need to insert the file list here, but cannot do this until the part generation finished
				context.getSmapData().append(Constants.smap_lines);
				context.invoke(JavaTemplate.genPart, part, context, out);
				context.writeSmapLine();
				// time to insert the list of files
				int index = 0;
				String fileList = "";
				for (String eglFile : context.getSmapFiles())
					fileList += "+ " + (++index) + " " + unqualifyFileName(eglFile) + "\n" + eglFile + "\n";
				context.getSmapData().insert(context.getSmapData().indexOf(Constants.smap_stratum) + Constants.smap_stratum.length(), fileList);
				// finish up the smap data
				context.getSmapData().append(Constants.smap_trailer);
				// add our special egl extension
				context.getSmapData().append(context.getSmapExtension());
				context.getSmapData().append(Constants.smap_extensionTrailer);
				out.flush();
			}
		}
		catch (IOException e) {
			throw new GenerationException(e);
		}
		catch (TemplateException e) {
			Annotation locAnn = context.getLastStatementLocation();
			String partName = "";
			if (part instanceof Part) {
				partName = ((Part)part).getCaseSensitiveName();
				if (locAnn == null) {
					locAnn = ((Part)part).getAnnotation(IEGLConstants.EGL_LOCATION);
				}
			}
			String[] details1 = new String[] { partName, e.getLocalizedMessage() };
			EGLMessage message1 = EGLMessage.createEGLMessage(context.getMessageMapping(), EGLMessage.EGL_ERROR_MESSAGE,
				Constants.EGLMESSAGE_EXCEPTION_OCCURED, e, details1, CommonUtilities.includeEndOffset(locAnn, context));
			context.getMessageRequestor().addMessage(message1);
			if (e.getCause() != null) {
				String[] details2 = new String[] { e.getCause().toString() };
				EGLMessage message2 = EGLMessage.createEGLStackTraceMessage(context.getMessageMapping(), EGLMessage.EGL_ERROR_MESSAGE, Constants.EGLMESSAGE_STACK_TRACE,
					e, details2, CommonUtilities.includeEndOffset(locAnn, context));
				context.getMessageRequestor().addMessage(message2);
			}
			// print out the whole stack trace
			int startLine = 0;
			Annotation annotation = locAnn;
			if (annotation != null) {
				if (annotation.getValue(IEGLConstants.EGL_PARTLINE) != null)
					startLine = ((Integer) annotation.getValue(IEGLConstants.EGL_PARTLINE)).intValue();
			}
			System.err.println("generating:" + ((Part) part).getFullyQualifiedName() + "[" + ((Part) part).getFileName() + "]:(" + startLine + ")");
			e.printStackTrace();
		}
		// close the output
		out.close();
	}

	private void makeWriter() {
		if (out == null)
			out = context.getTabbedWriter();
	}

	private String unqualifyFileName(String fileName) {
		int lastSlash = fileName.lastIndexOf('/');
		if (lastSlash != -1) {
			return fileName.substring(lastSlash + 1);
		}
		return fileName;
	}

	public void dumpErrorMessages() {
		// dump out all validation and generation messages
		for (IGenerationResultsMessage message : context.getMessageRequestor().getMessages()) {
			System.out.println(message.getBuiltMessage());
		}
	}

	public void processFile(String fileName) {
		// do any post processing once the file has been written
		writeReport(context, fileName, getReport(), Constants.EGLMESSAGE_ENCODING_ERROR, Constants.EGLMESSAGE_GENERATION_REPORT_FAILED);
	}

	@Override
	public String getRelativeFileName(Part part) {
		// This can be called outside of a generation - make sure we have a context.
		if (context == null) {
			makeContext(generator);
		}
		
		StringBuilder buf = new StringBuilder(50);
		if (context.mapsToNativeType(part)) {
			String name = context.getRawNativeImplementationMapping(part);
			
			int lastDot = name.lastIndexOf('.');
			if (lastDot != -1) {
				buf.append(JavaAliaser.packageNameAlias(name.substring(0, lastDot).split("[.]"), '/'));
				buf.append('/');
				buf.append(JavaAliaser.getAlias(name.substring(lastDot + 1)));
			}
			else {
				buf.append(JavaAliaser.getAlias(name));
			}
		}
		else {
			String pkg = part.getCaseSensitivePackageName();
			if (pkg.length() > 0) {
				buf.append(JavaAliaser.packageNameAlias(pkg.split("[.]"), '/'));
				buf.append('/');
			}
			buf.append(JavaAliaser.getAlias(part.getCaseSensitiveName()));
		}
		buf.append(getFileExtension());
		return buf.toString();
	}

	@Override
	public String getFileExtension() {
		return ".java";
	}
}
