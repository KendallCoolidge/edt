/*******************************************************************************
 * Copyright © 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.internal;

import org.eclipse.edt.ide.ui.EDTUIPlugin;
import org.eclipse.edt.ide.ui.internal.preferences.IColorConstants;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.TextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * Preference constants used in the EDT-UI preference store. Clients should only read the
 * EDT-UI preference store using these values. Clients are not allowed to modify the 
 * preference store programmatically.
 * 
 * @since 2.0
  */
public class EGLPreferenceConstants {

	private EGLPreferenceConstants() {
	}
	
	/**
	 * A named preference that controls return type rendering of methods in the UI.
	 * <p>
	 * Value is of type <code>Boolean</code>: if <code>true</code> return types
	 * are rendered
	 * </p>
	 */
	public static final String APPEARANCE_METHOD_RETURNTYPE= "org.eclipse.jdt.ui.methodreturntype";//$NON-NLS-1$

	/**
	 * A named preference that controls if override indicators are rendered in the UI.
	 * <p>
	 * Value is of type <code>Boolean</code>: if <code>true</code> override 
	 * indicators are rendered
	 * </p>
	 */
	public static final String APPEARANCE_OVERRIDE_INDICATOR= "org.eclipse.jdt.ui.overrideindicator";//$NON-NLS-1$

	/**
	 * A named preference that defines the pattern used for package name compression.
	 * <p>
	 * Value is of type <code>String</code>. For example foe the given package name 'org.eclipse.jdt' pattern
	 * '.' will compress it to '..jdt', '1~' to 'o~.e~.jdt'.
	 * </p>
	 */	
	public static final String APPEARANCE_PKG_NAME_PATTERN_FOR_PKG_VIEW= "PackagesView.pkgNamePatternForPackagesView";//$NON-NLS-1$

	/**
	 * A named preference that controls if package name compression is turned on or off.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @see #APPEARANCE_PKG_NAME_PATTERN_FOR_PKG_VIEW
	 */	
	public static final String APPEARANCE_COMPRESS_PACKAGE_NAMES= "org.eclipse.jdt.ui.compresspackagenames";//$NON-NLS-1$

	/**
	 * A named preference that controls if empty inner packages are folded in
	 * the hierarchical mode of the package explorer.
	 * <p>
	 * Value is of type <code>Boolean</code>: if <code>true</code> empty
	 * inner packages are folded.
	 * </p>
	 * @since 2.1
	 */
	public static final String APPEARANCE_FOLD_PACKAGES_IN_PACKAGE_EXPLORER= "org.eclipse.jdt.ui.flatPackagesInPackageExplorer";//$NON-NLS-1$

	/**
	 * A named preference that defines how member elements are ordered by the
	 * Java views using the <code>JavaElementSorter</code>.
	 * <p>
	 * Value is of type <code>String</code>: A comma separated list of the
	 * following entries. Each entry must be in the list, no duplication. List
	 * order defines the sort order.
	 * <ul>
	 * <li><b>T</b>: Types</li>
	 * <li><b>C</b>: Constructors</li>
	 * <li><b>I</b>: Initializers</li>
	 * <li><b>M</b>: Methods</li>
	 * <li><b>F</b>: Fields</li>
	 * <li><b>SI</b>: Static Initializers</li>
	 * <li><b>SM</b>: Static Methods</li>
	 * <li><b>SF</b>: Static Fields</li>
	 * </ul>
	 * </p>
	 * @since 2.1
	 */
	public static final String APPEARANCE_MEMBER_SORT_ORDER= "outlinesortoption"; //$NON-NLS-1$

	/**
	 * A named preference that controls if prefix removal during setter/getter generation is turned on or off. 
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @deprecated Use JavaCore preference store (key JavaCore.
	 * CODEASSIST_FIELD_PREFIXES and CODEASSIST_STATIC_FIELD_PREFIXES)
	 */	
	public static final String CODEGEN_USE_GETTERSETTER_PREFIX= "org.eclipse.jdt.ui.gettersetter.prefix.enable";//$NON-NLS-1$

	/**
	 * A named preference that holds a list of prefixes to be removed from a local variable to compute setter 
	 * and gettter names.
	 * <p>
	 * Value is of type <code>String</code>: comma separated list of prefixed
	 * </p>
	 * 
	 * @deprecated Use JavaCore preference store (key JavaCore.
	 * CODEASSIST_FIELD_PREFIXES and CODEASSIST_STATIC_FIELD_PREFIXES)
	 */	
	public static final String CODEGEN_GETTERSETTER_PREFIX= "org.eclipse.jdt.ui.gettersetter.prefix.list";//$NON-NLS-1$

	/**
	 * A named preference that controls if suffix removal during setter/getter generation is turned on or off.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @deprecated Use JavaCore preference store (key JavaCore.
	 * CODEASSIST_FIELD_PREFIXES and CODEASSIST_STATIC_FIELD_PREFIXES)
	 */	
	public static final String CODEGEN_USE_GETTERSETTER_SUFFIX= "org.eclipse.jdt.ui.gettersetter.suffix.enable";//$NON-NLS-1$

	/**
	 * A named preference that holds a list of suffixes to be removed from a local variable to compute setter 
	 * and getter names.
	 * <p>
	 * Value is of type <code>String</code>: comma separated list of suffixes
	 * </p>
	 * @deprecated Use setting from JavaCore preference store (key JavaCore.
	 * CODEASSIST_FIELD_SUFFIXES and CODEASSIST_STATIC_FIELD_SUFFIXES)
	 */	
	public static final String CODEGEN_GETTERSETTER_SUFFIX= "org.eclipse.jdt.ui.gettersetter.suffix.list"; //$NON-NLS-1$


	/**
	 * A named preference that controls if comment stubs will be added
	 * automatically to newly created types and methods.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public static final String CODEGEN_ADD_COMMENTS= "org.eclipse.jdt.ui.javadoc"; //$NON-NLS-1$

	/**
	 * A named preference that controls if a comment stubs will be added
	 * automatocally to newly created types and methods.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @deprecated Use CODEGEN_ADD_COMMENTS instead (Name is more precice).
	 */
	public static final String CODEGEN__JAVADOC_STUBS= CODEGEN_ADD_COMMENTS;

	/**
	 * A named preference that controls if a non-javadoc comment gets added to methods generated via the 
	 * "Override Methods" operation.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @deprecated New code template story: user can
	 * specify the overriding method comment.
	 */
	public static final String CODEGEN__NON_JAVADOC_COMMENTS= "org.eclipse.jdt.ui.seecomments"; //$NON-NLS-1$

	/**
	 * A named preference that controls if a file comment gets added to newly created files.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @deprecated New code template story: user can
	 * specify the new type code template.
	 */
	public static final String CODEGEN__FILE_COMMENTS= "org.eclipse.jdt.ui.filecomments"; //$NON-NLS-1$
	
	/**
	 * A named preference that holds a list of comma separated package names. The list specifies the import order used by
	 * the "Organize Imports" opeation.
	 * <p>
	 * Value is of type <code>String</code>: semicolon separated list of package
	 * names
	 * </p>
	 */
	public static final String ORGIMPORTS_IMPORTORDER= "org.eclipse.egl.ui.importorder"; //$NON-NLS-1$
	
	/**
	 * A named preference that specifies the number of imports added before a star-import declaration is used.
	 * <p>
	 * Value is of type <code>Int</code>: positive value specifing the number of non star-import is used
	 * </p>
	 */
	public static final String ORGIMPORTS_ONDEMANDTHRESHOLD= "org.eclipse.egl.ui.ondemandthreshold"; //$NON-NLS-1$

	/**
	 * A named preferences that controls if types that start with a lower case letters get added by the
	 * "Organize Import" operation.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public static final String ORGIMPORTS_IGNORELOWERCASE= "org.eclipse.jdt.ui.ignorelowercasenames"; //$NON-NLS-1$

	/**
	 * A named preference that speficies whether children of a compilation unit are shown in the package explorer.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public static final String SHOW_CU_CHILDREN= "org.eclipse.jdt.ui.packages.cuchildren"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the package explorer's selection is linked to the active editor.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public static final String LINK_PACKAGES_TO_EDITOR= "org.eclipse.jdt.ui.packages.linktoeditor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the hierarchy view's selection is linked to the active editor.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public static final String LINK_TYPEHIERARCHY_TO_EDITOR= "org.eclipse.jdt.ui.packages.linktypehierarchytoeditor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the projects view's selection is
	 * linked to the active editor.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public static final String LINK_BROWSING_PROJECTS_TO_EDITOR= "org.eclipse.jdt.ui.browsing.projectstoeditor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the packages view's selection is
	 * linked to the active editor.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public static final String LINK_BROWSING_PACKAGES_TO_EDITOR= "org.eclipse.jdt.ui.browsing.packagestoeditor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the types view's selection is
	 * linked to the active editor.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public static final String LINK_BROWSING_TYPES_TO_EDITOR= "org.eclipse.jdt.ui.browsing.typestoeditor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the members view's selection is
	 * linked to the active editor.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public static final String LINK_BROWSING_MEMBERS_TO_EDITOR= "org.eclipse.jdt.ui.browsing.memberstoeditor"; //$NON-NLS-1$
	/**
	 * A named preference that controls whether new projects are generated using source and output folder.
	 * <p>
	 * Value is of type <code>Boolean</code>. if <code>true</code> new projects are created with a source and
	 * output folder. If <code>false</code> source and output folder equals to the project.
	 * </p>
	 */
	public static final String SRCBIN_FOLDERS_IN_NEWPROJ= "org.eclipse.jdt.ui.wizards.srcBinFoldersInNewProjects"; //$NON-NLS-1$

	/**
	 * A named preference that specifies the source folder name used when creating a new Java project. Value is inactive
	 * if <code>SRCBIN_FOLDERS_IN_NEWPROJ</code> is set to <code>false</code>.
	 * <p>
	 * Value is of type <code>String</code>. 
	 * </p>
	 * 
	 * @see #SRCBIN_FOLDERS_IN_NEWPROJ
	 */
	public static final String SRCBIN_SRCNAME= "org.eclipse.jdt.ui.wizards.srcBinFoldersSrcName"; //$NON-NLS-1$

	/**
	 * A named preference that specifies the output folder name used when creating a new Java project. Value is inactive
	 * if <code>SRCBIN_FOLDERS_IN_NEWPROJ</code> is set to <code>false</code>.
	 * <p>
	 * Value is of type <code>String</code>. 
	 * </p>
	 * 
	 * @see #SRCBIN_FOLDERS_IN_NEWPROJ
	 */
	public static final String SRCBIN_BINNAME= "org.eclipse.jdt.ui.wizards.srcBinFoldersBinName"; //$NON-NLS-1$

	/**
	 * A named preference that holds a list of possible JRE libraries used by the New Java Project wizard. An library 
	 * consists of a description and an arbitrary number of <code>IClasspathEntry</code>s, that will represent the 
	 * JRE on the new project's classpath. 
	 * <p>
	 * Value is of type <code>String</code>: a semicolon separated list of encoded JRE libraries. 
	 * <code>NEWPROJECT_JRELIBRARY_INDEX</code> defines the currently used library. Clients
	 * should use the method <code>encodeJRELibrary</code> to encode a JRE library into a string
	 * and the methods <code>decodeJRELibraryDescription(String)</code> and <code>
	 * decodeJRELibraryClasspathEntries(String)</code> to decode the description and the array
	 * of classpath entries from an encoded string.
	 * </p>
	 * 
	 * @see #NEWPROJECT_JRELIBRARY_INDEX
	 * @see #encodeJRELibrary(String, IClasspathEntry[])
	 * @see #decodeJRELibraryDescription(String)
	 * @see #decodeJRELibraryClasspathEntries(String)
	 */
	public static final String NEWPROJECT_JRELIBRARY_LIST= "org.eclipse.jdt.ui.wizards.jre.list"; //$NON-NLS-1$

	/**
	 * A named preferences that specifies the current active JRE library.
	 * <p>
	 * Value is of type <code>Int</code>: an index into the list of possible JRE libraries.
	 * </p>
	 * 
	 * @see #NEWPROJECT_JRELIBRARY_LIST
	 */
	public static final String NEWPROJECT_JRELIBRARY_INDEX= "org.eclipse.jdt.ui.wizards.jre.index"; //$NON-NLS-1$

	/**
	 * A named preference that controls if a new type hierarchy gets opened in a 
	 * new type hierarchy perspective or inside the type hierarchy view part.
	 * <p>
	 * Value is of type <code>String</code>: possible values are <code>
	 * OPEN_TYPE_HIERARCHY_IN_PERSPECTIVE</code> or <code>
	 * OPEN_TYPE_HIERARCHY_IN_VIEW_PART</code>.
	 * </p>
	 * 
	 * @see #OPEN_TYPE_HIERARCHY_IN_PERSPECTIVE
	 * @see #OPEN_TYPE_HIERARCHY_IN_VIEW_PART
	 */
	public static final String OPEN_TYPE_HIERARCHY= "org.eclipse.jdt.ui.openTypeHierarchy"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>OPEN_TYPE_HIERARCHY</code>.
	 * 
	 * @see #OPEN_TYPE_HIERARCHY
	 */
	public static final String OPEN_TYPE_HIERARCHY_IN_PERSPECTIVE= "perspective"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>OPEN_TYPE_HIERARCHY</code>.
	 * 
	 * @see #OPEN_TYPE_HIERARCHY
	 */
	public static final String OPEN_TYPE_HIERARCHY_IN_VIEW_PART= "viewPart"; //$NON-NLS-1$
	
	/**
	 * A named preference that controls the behaviour when double clicking on a container in the packages view. 
	 * <p>
	 * Value is of type <code>String</code>: possible values are <code>
	 * DOUBLE_CLICK_GOES_INTO</code> or <code>
	 * DOUBLE_CLICK_EXPANDS</code>.
	 * </p>
	 * 
	 * @see #DOUBLE_CLICK_EXPANDS
	 * @see #DOUBLE_CLICK_GOES_INTO
	 */
	public static final String DOUBLE_CLICK= "packageview.doubleclick"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>DOUBLE_CLICK</code>.
	 * 
	 * @see #DOUBLE_CLICK
	 */
	public static final String DOUBLE_CLICK_GOES_INTO= "packageview.gointo"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>DOUBLE_CLICK</code>.
	 * 
	 * @see #DOUBLE_CLICK
	 */
	public static final String DOUBLE_CLICK_EXPANDS= "packageview.doubleclick.expands"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether Java views update their presentation while editing or when saving the
	 * content of an editor. 
	 * <p>
	 * Value is of type <code>String</code>: possible values are <code>
	 * UPDATE_ON_SAVE</code> or <code>
	 * UPDATE_WHILE_EDITING</code>.
	 * </p>
	 * 
	 * @see #UPDATE_ON_SAVE
	 * @see #UPDATE_WHILE_EDITING
	 */
	public static final String UPDATE_JAVA_VIEWS= "JavaUI.update"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>UPDATE_JAVA_VIEWS</code>
	 * 
	 * @see #UPDATE_JAVA_VIEWS
	 */
	public static final String UPDATE_ON_SAVE= "JavaUI.update.onSave"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>UPDATE_JAVA_VIEWS</code>
	 * 
	 * @see #UPDATE_JAVA_VIEWS
	 */
	public static final String UPDATE_WHILE_EDITING= "JavaUI.update.whileEditing"; //$NON-NLS-1$

	/**
	 * A named preference that holds the path of the Javadoc command used by the Javadoc creation wizard.
	 * <p>
	 * Value is of type <code>String</code>.
	 * </p>
	 */
	public static final String JAVADOC_COMMAND= "command"; //$NON-NLS-1$

	/**
	 * A named preference that defines the hover shown when no control key is
	 * pressed.
	 *
	 * @see JavaUI
	 * @since 2.1
	 */
	public static final String EDITOR_TEXT_HOVER_MODIFIERS= "hoverModifiers"; //$NON-NLS-1$

	/**
	 * The id of the best match hover contributed for extension point
	 * <code>javaEditorTextHovers</code>.
	 *
	 * @since 2.1
	 */
	public static String ID_BESTMATCH_HOVER= "org.eclipse.jdt.ui.BestMatchHover"; //$NON-NLS-1$

	/**
	 * The id of the source code hover contributed for extension point
	 * <code>javaEditorTextHovers</code>.
	 *
	 * @since 2.1
	 */
	public static String ID_SOURCE_HOVER= "org.eclipse.jdt.ui.JavaSourceHover"; //$NON-NLS-1$

	/**
	 * The id of the javadoc hover contributed for extension point
	 * <code>javaEditorTextHovers</code>.
	 *
	 * @since 2.1
	 */
	public static String ID_JAVADOC_HOVER= "org.eclipse.jdt.ui.JavadocHover"; //$NON-NLS-1$

	/**
	 * The id of the problem hover contributed for extension point
	 * <code>javaEditorTextHovers</code>.
	 *
	 * @since 2.1
	 */
	public static String ID_PROBLEM_HOVER= "org.eclipse.jdt.ui.ProblemHover"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether bracket matching highlighting is turned on or off.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_MATCHING_BRACKETS= "matchingBrackets"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used to highlight matching brackets.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string 
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_MATCHING_BRACKETS_COLOR=  "matchingBracketsColor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the current line highlighting is turned on or off.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_CURRENT_LINE= "currentLine"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used to highlight the current line.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_CURRENT_LINE_COLOR= "currentLineColor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the print margin is turned on or off.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_PRINT_MARGIN= "printMargin"; //$NON-NLS-1$
	
	/**
	 * A named preference that holds the color used to render the print margin.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_PRINT_MARGIN_COLOR= "printMarginColor"; //$NON-NLS-1$

	/**
	 * Print margin column. Int value.
	 */
	public final static String EDITOR_PRINT_MARGIN_COLUMN= "printMarginColumn"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used for the find/replace scope.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_FIND_SCOPE_COLOR= AbstractTextEditor.PREFERENCE_COLOR_FIND_SCOPE;

	/**
	 * A named preference that specifies if the editor uses spaces for tabs.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code>spaces instead of tabs are used
	 * in the editor. If <code>false</code> the editor inserts a tab character when pressing the tab
	 * key.
	 * </p>
	 */
	public final static String EDITOR_SPACES_FOR_TABS= "spacesForTabs"; //$NON-NLS-1$

	/**
	 * A named preference that holds the number of spaces used per tab in the editor.
	 * <p>
	 * Value is of type <code>Int</code>: positive int value specifying the number of
	 * spaces per tab.
	 * </p>
	 */
	public final static String EDITOR_TAB_WIDTH= "org.eclipse.jdt.ui.editor.tab.width"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the outline view selection
	 * should stay in sync with with the element at the current cursor position.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_SYNC_OUTLINE_ON_CURSOR_MOVE= "JavaEditor.SyncOutlineOnCursorMove"; //$NON-NLS-1$

	/**
	 * A named preference that controls if correction indicators are shown in the UI.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_CORRECTION_INDICATION= "JavaEditor.ShowTemporaryProblem"; //$NON-NLS-1$
	
	/**
	 * A named preference that controls whether the editor shows problem indicators in text (squiggly lines). 
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_HANDLE_DYNAMIC_PROBLEMS= "handleDynamicProblems"; //$NON-NLS-1$
	
	/**
	 * A named preference that controls whether the editor shows problem indicators in text (squiggly lines). 
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_ERROR_INDICATION= "errorIndication"; //$NON-NLS-1$
	
	/**
	 * A named preference that holds the color used to render problem indicators.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_ERROR_INDICATION_COLOR= "errorIndicationColor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the editor shows warning indicators in text (squiggly lines). 
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_WARNING_INDICATION= "warningIndication"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used to render warning indicators.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see #EDITOR_WARNING_INDICATION
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @since 2.1
	 */
	public final static String EDITOR_WARNING_INDICATION_COLOR= "warningIndicationColor"; //$NON-NLS-1$
	
	/**
	 * A named preference that controls whether the editor shows task indicators in text (squiggly lines). 
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_TASK_INDICATION= "taskIndication"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used to render task indicators.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see #EDITOR_TASK_INDICATION
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @since 2.1
	 */
	public final static String EDITOR_TASK_INDICATION_COLOR= "taskIndicationColor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the editor shows bookmark
	 * indicators in text (squiggly lines).
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_BOOKMARK_INDICATION= "bookmarkIndication"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used to render bookmark indicators.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 *
	 * @see #EDITOR_BOOKMARK_INDICATION
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @since 2.1
	 */
	public final static String EDITOR_BOOKMARK_INDICATION_COLOR= "bookmarkIndicationColor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the editor shows search
	 * indicators in text (squiggly lines).
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_SEARCH_RESULT_INDICATION= "searchResultIndication"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used to render search indicators.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 *
	 * @see #EDITOR_SEARCH_RESULT_INDICATION
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @since 2.1
	 */
	public final static String EDITOR_SEARCH_RESULT_INDICATION_COLOR= "searchResultIndicationColor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the editor shows unknown
	 * indicators in text (squiggly lines).
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_UNKNOWN_INDICATION= "othersIndication"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used to render unknown
	 * indicators.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 *
	 * @see #EDITOR_UNKNOWN_INDICATION
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @since 2.1
	 */
	public final static String EDITOR_UNKNOWN_INDICATION_COLOR= "othersIndicationColor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the overview ruler shows error
	 * indicators.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_ERROR_INDICATION_IN_OVERVIEW_RULER= TextEditorPreferenceConstants.EDITOR_ERROR_INDICATION_IN_OVERVIEW_RULER;
	
	/**
	 * A named preference that controls whether the overview ruler shows warning
	 * indicators.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_WARNING_INDICATION_IN_OVERVIEW_RULER= "warningIndicationInOverviewRuler"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the overview ruler shows task
	 * indicators.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_TASK_INDICATION_IN_OVERVIEW_RULER= "taskIndicationInOverviewRuler"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the overview ruler shows
	 * bookmark indicators.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_BOOKMARK_INDICATION_IN_OVERVIEW_RULER= "bookmarkIndicationInOverviewRuler"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the overview ruler shows
	 * search result indicators.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_SEARCH_RESULT_INDICATION_IN_OVERVIEW_RULER= "searchResultIndicationInOverviewRuler"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the overview ruler shows
	 * unknown indicators.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_UNKNOWN_INDICATION_IN_OVERVIEW_RULER= "othersIndicationInOverviewRuler"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the 'close strings' feature
	 *  is   enabled.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_CLOSE_STRINGS= "closeStrings"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the 'wrap strings' feature is
	 * enabled.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_WRAP_STRINGS= "wrapStrings"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the 'close brackets' feature is
	 * enabled.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_CLOSE_BRACKETS= "closeBrackets"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the 'close braces' feature is
	 * enabled.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_CLOSE_BRACES= "closeBraces"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the 'close java docs' feature is
	 * enabled.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_CLOSE_JAVADOCS= "closeJavaDocs"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the 'add JavaDoc tags' feature
	 * is enabled.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_ADD_JAVADOC_TAGS= "addJavaDocTags"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the 'format Javadoc tags'
	 * feature is enabled.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_FORMAT_JAVADOCS= "autoFormatJavaDocs"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the 'smart paste' feature is
	 * enabled.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_SMART_PASTE= "smartPaste"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether the 'smart home-end' feature is
	 * enabled.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String EDITOR_SMART_HOME_END= AbstractTextEditor.PREFERENCE_NAVIGATION_SMART_HOME_END;

	/**
	 * A named preference that controls if temporary problems are evaluated and shown in the UI.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_EVALUTE_TEMPORARY_PROBLEMS= "handleTemporaryProblems"; //$NON-NLS-1$

	/**
	 * A named preference that controls if the overview ruler is shown in the UI.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_OVERVIEW_RULER= "overviewRuler"; //$NON-NLS-1$

	/**
	 * A named preference that controls if the line number ruler is shown in the UI.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_LINE_NUMBER_RULER= AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER;

	/**
	 * A named preference that holds the color used to render line numbers inside the line number ruler.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @see #EDITOR_LINE_NUMBER_RULER
	 */
	public final static String EDITOR_LINE_NUMBER_RULER_COLOR= "lineNumberColor"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used to render linked positions inside code templates.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_LINKED_POSITION_COLOR= "linkedPositionColor"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used as the text foreground.
	 * This value has not effect if the system default color is used.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_FOREGROUND_COLOR= AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND;

	/**
	 * A named preference that describes if the system default foreground color
	 * is used as the text foreground.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_FOREGROUND_DEFAULT_COLOR= AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT;

	/**
	 * A named preference that holds the color used as the text background.
	 * This value has not effect if the system default color is used.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_BACKGROUND_COLOR= AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND;

	/**
	 * A named preference that describes if the system default background color
	 * is used as the text background.
	 * <p>
	 * Value is of type <code>Boolean</code>. 
	 * </p>
	 */
	public final static String EDITOR_BACKGROUND_DEFAULT_COLOR= AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT;

	/**
	 * Preference key suffix for bold text style preference keys.
	 * 
	 * @since 2.1
	 */
	public static final String EDITOR_BOLD_SUFFIX= "_bold"; //$NON-NLS-1$

	/**
	 * The symbolic font name for the EGL editor text font 
	 * (value <code>"org.eclipse.jdt.ui.editors.textfont"</code>).
	 * 
	 * @since 2.1
	 */
	public final static String EDITOR_TEXT_FONT= "org.eclipse.edt.ide.ui.editors.textfont"; //$NON-NLS-1$

	/**
	 * A named preference that holds the color used to render multi line comments.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_MULTI_LINE_COMMENT_COLOR= IColorConstants.EGL_MULTI_LINE_COMMENT;

	
	/**
	 * A named preference that controls whether multi line comments are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> multi line comments are rendered
	 * in bold. If <code>false</code> the are rendered using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_MULTI_LINE_COMMENT_BOLD= IColorConstants.EGL_MULTI_LINE_COMMENT + EDITOR_BOLD_SUFFIX; 

	/**
	 * A named preference that holds the color used to render single line comments.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_SINGLE_LINE_COMMENT_COLOR= IColorConstants.EGL_SINGLE_LINE_COMMENT;

	/**
	 * A named preference that controls whether sinle line comments are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered
	 * in bold. If <code>false</code> the are rendered using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_SINGLE_LINE_COMMENT_BOLD= IColorConstants.EGL_SINGLE_LINE_COMMENT + EDITOR_BOLD_SUFFIX; 

	/**
	 * A named preference that holds the color used to render egl keywords.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_KEYWORD_COLOR= IColorConstants.EGL_KEYWORD;

	/**
	 * A named preference that controls whether keywords are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_BOLD= IColorConstants.EGL_KEYWORD + EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that holds the color used to render string constants.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_STRING_COLOR= IColorConstants.EGL_STRING;

	/**
	 * A named preference that controls whether string constants are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_STRING_BOLD= IColorConstants.EGL_STRING + EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that holds the color used to render egl default text.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_DEFAULT_COLOR= IColorConstants.EGL_DEFAULT;

	/**
	 * A named preference that controls whether egl default text is rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String EDITOR_DEFAULT_BOLD= IColorConstants.EGL_DEFAULT + EDITOR_BOLD_SUFFIX;
	
	/**
	 * A named preference that holds the color used for 'linked-mode' underline.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 *
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @since 2.1
	 */
	public final static String EDITOR_LINK_COLOR= "linkColor"; //$NON-NLS-1$

	/**
	 * A named preference that controls whether hover tooltips in the editor are turned on or off.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public static final String EDITOR_SHOW_HOVER= "org.eclipse.jdt.ui.editor.showHover"; //$NON-NLS-1$

	/**
	 * A named preference that defines the hover shown when no control key is
	 * pressed.
	 * <p>Value is of type <code>String</code>: possible values are <code>
	 * EDITOR_NO_HOVER_CONFIGURED_ID</code> or
	 * <code>EDITOR_DEFAULT_HOVER_CONFIGURED_ID</code> or the hover id of a hover
	 * contributed as <code>javaEditorTextHovers</code>.
	 * </p>
	 * @see #EDITOR_NO_HOVER_CONFIGURED_ID
	 * @see #EDITOR_DEFAULT_HOVER_CONFIGURED_ID
	 * @see JavaUI
	 * @since 2.1
	 * @deprecated Will soon be removed - replaced by {@link #EDITOR_TEXT_HOVER_MODIFIERS}
	 */
	public static final String EDITOR_NONE_HOVER= "noneHover"; //$NON-NLS-1$

	/**
	 * A named preference that defines the hover shown when the
	 * <code>CTRL</code> modifier key is pressed.
	 * <p>Value is of type <code>String</code>: possible values are <code>
	 * EDITOR_NO_HOVER_CONFIGURED_ID</code> or
	 * <code>EDITOR_DEFAULT_HOVER_CONFIGURED_ID</code> or the hover id of a
	 * hover contributed as <code>javaEditorTextHovers</code>.
	 * </p>
	 * @see #EDITOR_NO_HOVER_CONFIGURED_ID
	 * @see #EDITOR_DEFAULT_HOVER_CONFIGURED_ID
	 * @see JavaUI
	 * @since 2.1
	 * @deprecated Will soon be removed - replaced by {@link #EDITOR_TEXT_HOVER_MODIFIERS}
	 */
	public static final String EDITOR_CTRL_HOVER= "ctrlHover"; //$NON-NLS-1$
	
	/**
	 * A named preference that defines the hover shown when the
	 * <code>SHIFT</code> modifier key is pressed.
	 * <p>Value is of type <code>String</code>: possible values are <code>
	 * EDITOR_NO_HOVER_CONFIGURED_ID</code> or
	 * <code>EDITOR_DEFAULT_HOVER_CONFIGURED_ID</code> or the hover id of a
	 * hover contributed as <code>javaEditorTextHovers</code>.
	 * </p>
	 * @see #EDITOR_NO_HOVER_CONFIGURED_ID
	 * @see #EDITOR_DEFAULT_HOVER_CONFIGURED_ID
	 * @see JavaUI ID_*_HOVER
	 * @since 2.1
	 * @deprecated Will soon be removed - replaced by {@link #EDITOR_TEXT_HOVER_MODIFIERS}
	 */
	public static final String EDITOR_SHIFT_HOVER= "shiftHover"; //$NON-NLS-1$

	/**
	 * A named preference that defines the hover shown when the
	 * <code>CTRL + ALT</code> modifier keys is pressed.
	 * <p>Value is of type <code>String</code>: possible values are <code>
	 * EDITOR_NO_HOVER_CONFIGURED_ID</code> or
	 * <code>EDITOR_DEFAULT_HOVER_CONFIGURED_ID</code> or the hover id of a
	 * hover contributed as <code>javaEditorTextHovers</code>.
	 * </p>
	 * @see #EDITOR_NO_HOVER_CONFIGURED_ID
	 * @see #EDITOR_DEFAULT_HOVER_CONFIGURED_ID
	 * @see JavaUI ID_*_HOVER
	 * @since 2.1
	 */
	public static final String EDITOR_CTRL_ALT_HOVER= "ctrlAltHover"; //$NON-NLS-1$

	/**
	 * A named preference that defines the hover shown when the
	 * <code>CTRL + ALT + SHIFT</code> modifier keys is pressed.
	 * <p>Value is of type <code>String</code>: possible values are <code>
	 * EDITOR_NO_HOVER_CONFIGURED_ID</code> or
	 * <code>EDITOR_DEFAULT_HOVER_CONFIGURED_ID</code> or the hover id of a
	 * hover contributed as <code>javaEditorTextHovers</code>.
	 * </p>
	 * @see #EDITOR_NO_HOVER_CONFIGURED_ID
	 * @see #EDITOR_DEFAULT_HOVER_CONFIGURED_ID
	 * @see JavaUI ID_*_HOVER
	 * @since 2.1
	 */
	public static final String EDITOR_CTRL_ALT_SHIFT_HOVER= "ctrlAltShiftHover"; //$NON-NLS-1$

	/**
	 * A named preference that defines the hover shown when the
	 * <code>CTRL + SHIFT</code> modifier keys is pressed.
	 * <p>Value is of type <code>String</code>: possible values are <code>
	 * EDITOR_NO_HOVER_CONFIGURED_ID</code> or
	 * <code>EDITOR_DEFAULT_HOVER_CONFIGURED_ID</code> or the hover id of a
	 * hover contributed as <code>javaEditorTextHovers</code>.
	 * </p>
	 * @see #EDITOR_NO_HOVER_CONFIGURED_ID
	 * @see #EDITOR_DEFAULT_HOVER_CONFIGURED_ID
	 * @see JavaUI ID_*_HOVER
	 * @since 2.1
	 * @deprecated Will be removed in one of the next builds.
	 */
	public static final String EDITOR_CTRL_SHIFT_HOVER= "ctrlShiftHover"; //$NON-NLS-1$

	/**
	 * A named preference that defines the hover shown when the
	 * <code>ALT</code> modifier key is pressed.
	 * <p>Value is of type <code>String</code>: possible values are <code>
	 * EDITOR_NO_HOVER_CONFIGURED_ID</code>,
	 * <code>EDITOR_DEFAULT_HOVER_CONFIGURED_ID</code>  or the hover id of a
	 * hover contributed as <code>javaEditorTextHovers</code>.
	 * </p>
	 * @see #EDITOR_NO_HOVER_CONFIGURED_ID
	 * @see #EDITOR_DEFAULT_HOVER_CONFIGURED_ID
	 * @see JavaUI ID_*_HOVER
	 * @deprecated Will soon be removed - replaced by {@link #EDITOR_TEXT_HOVER_MODIFIERS}
	 * @since 2.1
	 */
	public static final String EDITOR_ALT_SHIFT_HOVER= "altShiftHover"; //$NON-NLS-1$

	/**
	 * A string value used by the named preferences for hover configuration to
	 * descibe that no hover should be shown for the given key modifiers.
	 * @deprecated Will soon be removed - replaced by {@link #EDITOR_TEXT_HOVER_MODIFIERS}
	 * @since 2.1
	 */
	public static final String EDITOR_NO_HOVER_CONFIGURED_ID= "noHoverConfiguredId"; //$NON-NLS-1$
	
	/**
	 * A string value used by the named preferences for hover configuration to
	 * descibe that the default hover should be shown for the given key
	 * modifiers. The default hover is described by the
	 * <code>EDITOR_DEFAULT_HOVER</code> property.
	 * @since 2.1
	 * @deprecated Will soon be removed - replaced by {@link #EDITOR_TEXT_HOVER_MODIFIERS}
	 */
	public static final String EDITOR_DEFAULT_HOVER_CONFIGURED_ID= "defaultHoverConfiguredId"; //$NON-NLS-1$

	/**
	 * A named preference that defines the hover named the 'default hover'.
	 * Value is of type <code>String</code>: possible values are <code>
	 * EDITOR_NO_HOVER_CONFIGURED_ID</code> or <code> the hover id of a hover
	 * contributed as <code>javaEditorTextHovers</code>.
	 * </p>
	 * @since 2.1
	 * @deprecated Will soon be removed - replaced by {@link #EDITOR_TEXT_HOVER_MODIFIERS}
	 */
	public static final String EDITOR_DEFAULT_HOVER= "defaultHover"; //$NON-NLS-1$

	/**
	 * A named preference that controls if segmented view (show selected element only) is turned on or off.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public static final String EDITOR_SHOW_SEGMENTS= "org.eclipse.jdt.ui.editor.showSegments"; //$NON-NLS-1$
	
	/**
	 * A named preference that controls if browser like links are turned on or off.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @since 2.1
	 */
	public static final String EDITOR_BROWSER_LIKE_LINKS= "browserLikeLinks"; //$NON-NLS-1$

	/**
	 * A named preference that controls the key modifier for browser like links.
	 * <p>
	 * Value is of type <code>String</code>.
	 * </p>
	 * 
	 * @since 2.1
	 */
	public static final String EDITOR_BROWSER_LIKE_LINKS_KEY_MODIFIER= "browserLikeLinksKeyModifier"; //$NON-NLS-1$

	/**
	 * A named preference that controls the key modifier mask for browser like links.
	 * The value is only used if the value of <code>EDITOR_BROWSER_LIKE_LINKS</code>
	 * cannot be resolved to valid SWT modifier bits.
	 * <p>
	 * Value is of type <code>String</code>.
	 * </p>
	 * 
	 * @see #EDITOR_BROWSER_LIKE_LINKS_KEY_MODIFIER
	 * @since 2.1.1
	 */
	public static final String EDITOR_BROWSER_LIKE_LINKS_KEY_MODIFIER_MASK= "browserLikeLinksKeyModifierMask"; //$NON-NLS-1$
	
	/**
	 * A named preference that controls whether folding is enabled in the Ant editor.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 */
	public static final String EDITOR_FOLDING_ENABLED= "editor_folding_enabled"; //$NON-NLS-1$
	
	/**
	 * A named preference that stores the configured folding provider.
	 * <p>
	 * Value is of type <code>String</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */
	public static final String EDITOR_FOLDING_PROVIDER= "editor_folding_provider"; //$NON-NLS-1$
	
	/**
	 * A named preference that stores the value for comments folding for the default folding provider.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */	
	public static final String EDITOR_FOLDING_COMMENTS= "editor_folding_comments"; //$NON-NLS-1$
	
	/**
	 * A named preference that stores the value for parts(generatible) folding for the default folding provider.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */		
	public static final String EDITOR_FOLDING_PARTS= "editor_folding_parts"; //$NON-NLS-1$
	
	/**
	 * A named preference that stores the value for sub parts folding for the default folding provider.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */		
	//public static final String EDITOR_FOLDING_SUBPARTS= "editor_folding_subparts"; //$NON-NLS-1$
	
	/**
	 * A named preference that stores the value for functions folding for the default folding provider.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */		
	public static final String EDITOR_FOLDING_FUNCTIONS= "editor_folding_functions"; //$NON-NLS-1$

	/**
	 * A named preference that stores the value for imports folding for the default folding provider.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */	
	public static final String EDITOR_FOLDING_IMPORTS= "editor_folding_imports"; //$NON-NLS-1$
	
	/**
	 * A named preference that stores the value for partitions(sql, dli) folding for the default folding provider.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */		
	public static final String EDITOR_FOLDING_PARTITIONS= "editor_folding_partitions"; //$NON-NLS-1$
	
	/**
	 * A named preference that stores the value for statement blocks(if/else, while, case, etc.) folding for the default folding provider.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */	
	public static final String EDITOR_FOLDING_STATEMENTBLOCKS= "editor_folding_statementblocks"; //$NON-NLS-1$

	/**
	 * A named preference that stores the value for properties blocks folding for the default folding provider.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */	
	public static final String EDITOR_FOLDING_PROPERTIESBLOCKS= "editor_folding_propertiesblocks"; //$NON-NLS-1$
		
	/**
	 * A named preference that stores the value for properties blocks threshold number of lines
	 * <p>
	 * Value is of type <code>Int</code>.
	 * </p>
	 * 
	 * @since 7.0
	 */		
	public static final String EDITOR_FOLDING_PROPERTIESBLOCKS_THRESHOLD = "editor_folding_propertiesblocks_threshold"; //$NON-NLS-1$
	
	/**
	 * A named preference that controls if the Java code assist gets auto activated.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String CODEASSIST_AUTOACTIVATION= "content_assist_autoactivation"; //$NON-NLS-1$

	/**
	 * A name preference that holds the auto activation delay time in milli seconds.
	 * <p>
	 * Value is of type <code>Int</code>.
	 * </p>
	 */
	public final static String CODEASSIST_AUTOACTIVATION_DELAY= "content_assist_autoactivation_delay"; //$NON-NLS-1$

	/**
	 * A named preference that controls if code assist contains only visible proposals.
	 * <p>
	 * Value is of type <code>Boolean</code>. if <code>true<code> code assist only contains visible members. If 
	 * <code>false</code> all members are included.
	 * </p>
	 */
	public final static String CODEASSIST_SHOW_VISIBLE_PROPOSALS= "content_assist_show_visible_proposals"; //$NON-NLS-1$

	/**
	 * A named preference that controls if the Java code assist inserts a
	 * proposal automatically if only one proposal is available.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String CODEASSIST_AUTOINSERT= "content_assist_autoinsert"; //$NON-NLS-1$

	/**
	 * A named preference that controls if the Java code assist adds import
	 * statements.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String CODEASSIST_ADDIMPORT= "content_assist_add_import"; //$NON-NLS-1$
	
	/**
	 * A named preference that controls if the Java code assist only inserts
	 * completions. If set to false the proposals can also _replace_ code.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String CODEASSIST_INSERT_COMPLETION= "content_assist_insert_completion"; //$NON-NLS-1$	

	/**
	 * A named preference that controls whether code assist proposals filtering is case sensitive or not.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String CODEASSIST_CASE_SENSITIVITY= "content_assist_case_sensitivity"; //$NON-NLS-1$
	
	/**
	 * A named preference that defines if code assist proposals are sorted in alphabetical order.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> that are sorted in alphabetical 
	 * order. If <code>false</code> that are unsorted.
	 * </p>
	 */
	public final static String CODEASSIST_ORDER_PROPOSALS= "content_assist_order_proposals"; //$NON-NLS-1$

	/**
	 * A named preference that controls if argument names are filled in when a method is selected from as list
	 * of code assist proposal.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String CODEASSIST_FILL_ARGUMENT_NAMES= "content_assist_fill_method_arguments"; //$NON-NLS-1$

	/**
	 * A named preference that controls if method arguments are guessed when a
	 * method is selected from as list of code assist proposal.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String CODEASSIST_GUESS_METHOD_ARGUMENTS= "content_assist_guess_method_arguments"; //$NON-NLS-1$

	/**
	 * A named preference that holds the characters that auto activate code assist in Java code.
	 * <p>
	 * Value is of type <code>Sring</code>. All characters that trigger auto code assist in Java code.
	 * </p>
	 */
	public final static String CODEASSIST_AUTOACTIVATION_TRIGGERS_JAVA= "content_assist_autoactivation_triggers_java"; //$NON-NLS-1$

	/**
	 * A named preference that holds the characters that auto activate code assist in Javadoc.
	 * <p>
	 * Value is of type <code>Sring</code>. All characters that trigger auto code assist in Javadoc.
	 * </p>
	 */
	public final static String CODEASSIST_AUTOACTIVATION_TRIGGERS_JAVADOC= "content_assist_autoactivation_triggers_javadoc"; //$NON-NLS-1$

	/**
	 * A named preference that holds the background color used in the code assist selection dialog.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String CODEASSIST_PROPOSALS_BACKGROUND= "content_assist_proposals_background"; //$NON-NLS-1$

	/**
	 * A named preference that holds the foreground color used in the code assist selection dialog.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String CODEASSIST_PROPOSALS_FOREGROUND= "content_assist_proposals_foreground"; //$NON-NLS-1$
	
	/**
	 * A named preference that holds the background color used for parameter hints.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String CODEASSIST_PARAMETERS_BACKGROUND= "content_assist_parameters_background"; //$NON-NLS-1$

	/**
	 * A named preference that holds the foreground color used in the code assist selection dialog.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String CODEASSIST_PARAMETERS_FOREGROUND= "content_assist_parameters_foreground"; //$NON-NLS-1$

	/**
	 * A named preference that holds the background color used in the code
	 * assist selection dialog to mark replaced code.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 *
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @since 2.1
	 */
	public final static String CODEASSIST_REPLACEMENT_BACKGROUND= "content_assist_completion_replacement_background"; //$NON-NLS-1$

	/**
	 * A named preference that holds the foreground color used in the code
	 * assist selection dialog to mark replaced code.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string
	 * using class <code>PreferenceConverter</code>
	 * </p>
	 *
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 * @since 2.1
	 */
	public final static String CODEASSIST_REPLACEMENT_FOREGROUND= "content_assist_completion_replacement_foreground"; //$NON-NLS-1$


	/**
	 * A named preference that controls the behaviour of the refactoring wizard for showing the error page. 
	 * <p>
	 * Value is of type <code>String</code>. Valid values are: 
	 * <code>REFACTOR_FATAL_SEVERITY</code>,
	 * <code>REFACTOR_ERROR_SEVERITY</code>,
	 * <code>REFACTOR_WARNING_SEVERITY</code>
	 * <code>REFACTOR_INFO_SEVERITY</code>,
	 * <code>REFACTOR_OK_SEVERITY</code>.
	 * </p>
	 * 
	 * @see #REFACTOR_FATAL_SEVERITY
	 * @see #REFACTOR_ERROR_SEVERITY
	 * @see #REFACTOR_WARNING_SEVERITY
	 * @see #REFACTOR_INFO_SEVERITY
	 * @see #REFACTOR_OK_SEVERITY
	 */
	public static final String REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD= "Refactoring.ErrorPage.severityThreshold"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD</code>.
	 * 
	 * @see #REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD
	 */
	public static final String REFACTOR_FATAL_SEVERITY= "4"; //$NON-NLS-1$
	
	/**
	 * A string value used by the named preference <code>REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD</code>.
	 * 
	 * @see #REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD
	 */	
	public static final String REFACTOR_ERROR_SEVERITY= "3"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD</code>.
	 * 
	 * @see #REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD
	 */
	public static final String REFACTOR_WARNING_SEVERITY= "2"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD</code>.
	 * 
	 * @see #REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD
	 */
	public static final String REFACTOR_INFO_SEVERITY= "1"; //$NON-NLS-1$

	/**
	 * A string value used by the named preference <code>REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD</code>.
	 * 
	 * @see #REFACTOR_ERROR_PAGE_SEVERITY_THRESHOLD
	 */
	public static final String REFACTOR_OK_SEVERITY= "0"; //$NON-NLS-1$

	/**
	 * A named preference thet controls whether all dirty editors are automatically saved before a refactoring is
	 * executed.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public static final String REFACTOR_SAVE_ALL_EDITORS= "Refactoring.savealleditors"; //$NON-NLS-1$

	/**
	 * A named preference that controls if the Java Browsing views are linked to the active editor.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * 
	 * @see #LINK_PACKAGES_TO_EDITOR
	 */
	public static final String BROWSING_LINK_VIEW_TO_EDITOR= "org.eclipse.jdt.ui.browsing.linktoeditor"; //$NON-NLS-1$

	/**
	 * A named preference that controls the layout of the Java Browsing views vertically. Boolean value.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true<code> the views are stacked vertical.
	 * If <code>false</code> they are stacked horizontal.
	 * </p>
	 */
	public static final String BROWSING_STACK_VERTICALLY= "org.eclipse.jdt.ui.browsing.stackVertically"; //$NON-NLS-1$
	
	
	/**
	 * A named preference that controls if templates are formatted when applied.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 *
	 * @since 2.1
	 */	
	public static final String TEMPLATES_USE_CODEFORMATTER= "org.eclipse.jdt.ui.template.format"; //$NON-NLS-1$

	/**
	 * Initializes the given preference store with the default values the Preferences used by the EGL Editor
	 * 
	 * @param store the preference store to be initialized
	 * 
	 * @since 2.1
	 */
	public static void initializeDefaultEGLEditorPreferences(IPreferenceStore store) {
		
		//TextEditorPreferenceConstants.initializeDefaultValues(store);
		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_ENABLED, true);

		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_PROPERTIESBLOCKS_THRESHOLD, 5);		
		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_PROVIDER, "org.eclipse.edt.ide.ui.internal.editor.folding.EGLFoldingStructureProvider"); //$NON-NLS-1$
		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_COMMENTS, false);
		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_PARTS, false);
		//store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_SUBPARTS, false);
		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_FUNCTIONS, false);
		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_IMPORTS, true);
		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_PARTITIONS, true);
		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_STATEMENTBLOCKS, false);
		store.setDefault(EGLPreferenceConstants.EDITOR_FOLDING_PROPERTIESBLOCKS, false);
		store.setDefault(EGLPreferenceConstants.EDITOR_HANDLE_DYNAMIC_PROBLEMS, true);
		store.setDefault(EGLPreferenceConstants.ORGIMPORTS_IMPORTORDER, "com;egl"); //$NON-NLS-1$
		store.setDefault(EGLPreferenceConstants.ORGIMPORTS_ONDEMANDTHRESHOLD, 99);
		store.setDefault(EGLPreferenceConstants.CODEASSIST_AUTOACTIVATION, true);
		
		PreferenceConverter.setDefault(store, CODEASSIST_PROPOSALS_BACKGROUND, new RGB(254, 241, 233));
		PreferenceConverter.setDefault(store, CODEASSIST_PROPOSALS_FOREGROUND, new RGB(0, 0, 0));
		
		initializeDefaultEGLColorPreferences(store);		
	}
	
	/**
	 * Initializes the given preference store with the default values for the EGL Color Preferences.
	 * 
	 * @param store the preference store to be initialized
	 * 
	 * @since 2.1
	 */
	public static void initializeDefaultEGLColorPreferences(IPreferenceStore store) {
		
		store.setDefault(EDITOR_FOREGROUND_DEFAULT_COLOR, true);
		store.setDefault(EDITOR_BACKGROUND_DEFAULT_COLOR, true);

		PreferenceConverter.setDefault(store, EDITOR_MULTI_LINE_COMMENT_COLOR, new RGB(63, 127, 95));
		store.setDefault(EDITOR_MULTI_LINE_COMMENT_BOLD, false);

		PreferenceConverter.setDefault(store, EDITOR_SINGLE_LINE_COMMENT_COLOR, new RGB(63, 127, 95));
		store.setDefault(EDITOR_SINGLE_LINE_COMMENT_BOLD, false);

		PreferenceConverter.setDefault(store, EDITOR_KEYWORD_COLOR, new RGB(127, 0, 85));
		store.setDefault(EDITOR_KEYWORD_BOLD, true);

		PreferenceConverter.setDefault(store, EDITOR_STRING_COLOR, new RGB(42, 0, 255));
		store.setDefault(EDITOR_STRING_BOLD, false);

		PreferenceConverter.setDefault(store, EDITOR_DEFAULT_COLOR, new RGB(0, 0, 0));
		store.setDefault(EDITOR_DEFAULT_BOLD, false);

	}

	/**
	 * Returns the EDT-UI preference store.
	 * 
	 * @return the EDT-UI preference store
	 */
	public static IPreferenceStore getPreferenceStore() {
		return EDTUIPlugin.getDefault().getPreferenceStore();
	}

}

