package org.eclipse.edt.ide.core.internal.builder;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.edt.compiler.internal.io.IRFileNameUtility;
import org.eclipse.edt.compiler.tools.EGL2IR;
import org.eclipse.edt.ide.core.CoreIDEPluginStrings;
import org.eclipse.edt.ide.core.EDTCoreIDEPlugin;
import org.eclipse.edt.ide.core.internal.lookup.ProjectBuildPathManager;
import org.eclipse.edt.ide.core.model.EGLCore;
import org.eclipse.edt.ide.core.model.EGLModelException;
import org.eclipse.edt.ide.core.model.IPackageFragmentRoot;
import org.eclipse.edt.ide.core.utils.ProjectSettingsUtility;
import org.eclipse.edt.mof.serialization.ZipFileObjectStore;
import org.osgi.service.prefs.Preferences;

/**
 * Manages listeners for project-specific settings.
 */
public class ProjectSettingsListenerManager {
	
	private static final ProjectSettingsListenerManager instance = new ProjectSettingsListenerManager();
	
	public static final Object FAMILY_PROCESS_GENERATOR_CHANGES = new Object();
	
	private Map<IProject, IPreferenceChangeListener> listeners;
	
	private ProjectSettingsListenerManager() {
		listeners = new HashMap<IProject, IPreferenceChangeListener>();
		
		// Initialize the currently open projects.
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			if (project.isAccessible()) {
				addProject(project);
			}
		}
	}
	
	public static ProjectSettingsListenerManager getInstance() {
		return instance;
	}
	
	public void addProject(IProject project) {
		if (!listeners.containsKey(project)) {
			GeneratorPreferenceListener listener = new GeneratorPreferenceListener(project);
			Preferences prefs = new ProjectScope(project).getNode(EDTCoreIDEPlugin.PLUGIN_ID).node(ProjectSettingsUtility.PROPERTY_GENERATOR_IDS);
			if (prefs instanceof IEclipsePreferences) {
				((IEclipsePreferences)prefs).addPreferenceChangeListener(listener);
			}
			listeners.put(project, listener);
		}
	}
	
	public void removeProject(IProject project) {
		IPreferenceChangeListener listener = listeners.remove(project);
		if (listener != null) {
			Preferences prefs = new ProjectScope(project).getNode(EDTCoreIDEPlugin.PLUGIN_ID).node(ProjectSettingsUtility.PROPERTY_GENERATOR_IDS);
			if (prefs instanceof IEclipsePreferences) {
				((IEclipsePreferences)prefs).removePreferenceChangeListener(listener);
			}
		}
	}
	
	/**
	 * When the generator(s) for a resource are modified, this causes all affected resources to be regenerated.
	 */
	private class GeneratorPreferenceListener implements IPreferenceChangeListener {
		
		final IProject project;
		final Preferences prefs;
		
		public GeneratorPreferenceListener(IProject project) {
			this.project = project;
			this.prefs = new ProjectScope(project).getNode(EDTCoreIDEPlugin.PLUGIN_ID).node(ProjectSettingsUtility.PROPERTY_GENERATOR_IDS);
		}
		
		@Override
		public void preferenceChange(PreferenceChangeEvent event) {
			// Regenerate the entire resource tree that's affected.
			
			final IResource source;
			String key = event.getKey();
			if (ProjectSettingsUtility.PROJECT_KEY.equals(key)) {
				source = project;
			}
			else {
				source = project.findMember(key);
			}
			
			if (source != null) {
				final IResource[] sourceDirs;
				final IResource[] toTraverse;
				try {
					IPackageFragmentRoot[] roots = EGLCore.create(project).getPackageFragmentRoots();
					sourceDirs = new IResource[roots.length];
					for (int i = 0; i < roots.length; i++) {
						if (roots[i].isArchive()) {
							continue;
						}
						sourceDirs[i] = roots[i].getResource();
					}
				}
				catch (EGLModelException e) {
					EDTCoreIDEPlugin.log(e);
					return;
				}
				
				// For projects we just want to traverse the source folders
				if (source.getType() == IResource.PROJECT) {
					toTraverse = sourceDirs;
				}
				else {
					toTraverse = new IResource[]{source};
				}
				
				WorkspaceJob job = new WorkspaceJob(CoreIDEPluginStrings.calculatingGeneratorChanges) {
					public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
						final IContainer outputFolder = ProjectBuildPathManager.getInstance().getProjectBuildPath(project).getOutputLocation();
						for (final IResource nextResource : toTraverse) {
							if (nextResource == null) { // Eglar entry
								continue;
							}
							
							// We're checking the .egl files, but we need to be able to strip off the package fragment root from its path.
							// This will allow us to find the IR in the output directory.
							IPath sourceDir = null;
							for (IResource nextSrcDir : sourceDirs) {
								if (nextSrcDir == null) {
									continue;
								}
								
								if (nextSrcDir.getFullPath().isPrefixOf(nextResource.getFullPath())) {
									sourceDir = nextSrcDir.getFullPath();
									break;
								}
							}
							
							if (sourceDir == null) {
								continue;
							}
							
							final int sourceDirSegments = sourceDir.segmentCount();
							nextResource.accept(new IResourceProxyVisitor() {
								@Override
								public boolean visit(IResourceProxy proxy) throws CoreException {
									IResource resource = proxy.requestResource();
									
									// If it's the originating resource, or its a child resource that inherits settings from the originating resource,
									// then it should be regenerated if a file, otherwise check its kids.
									if (resource.equals(source) || ProjectSettingsUtility.findSetting(resource.getFullPath(), prefs, false) == null) {
										if (resource.getType() == IResource.FILE) {
											// We really don't know which extension the IR will have, so check for all known extensions.
											String relativePath = IRFileNameUtility.toIRFileName(resource.getFullPath().removeFirstSegments(sourceDirSegments).removeFileExtension().toString());
											IResource file = outputFolder.findMember(relativePath + EGL2IR.EGLXML);
											if (file != null) {
												file.touch(null);
											}
											file = outputFolder.findMember(relativePath + EGL2IR.EGLBIN);
											if (file != null) {
												file.touch(null);
											}
											file = outputFolder.findMember(relativePath + ZipFileObjectStore.MOFXML);
											if (file != null) {
												file.touch(null);
											}
											file = outputFolder.findMember(relativePath + ZipFileObjectStore.MOFBIN);
											if (file != null) {
												file.touch(null);
											}
											return false; // files don't have kids
										}
										return true;
									}
									return false;
								}
							}, 0);
						}
						return Status.OK_STATUS;
					}
					
					/* (non-Javadoc)
					 *  @see org.eclipse.core.runtime.jobs.Job#belongsTo(Object)
					 */
					public boolean belongsTo(Object family) {
						return family == FAMILY_PROCESS_GENERATOR_CHANGES;
					}
				};
				
				job.schedule();
			}
		}
	}
}
