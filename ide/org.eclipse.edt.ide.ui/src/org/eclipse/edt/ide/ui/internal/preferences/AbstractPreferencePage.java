/*******************************************************************************
 * Copyright © 2008, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.internal.preferences;

import org.eclipse.edt.ide.ui.EDTUIPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class AbstractPreferencePage extends PreferencePage implements ModifyListener, SelectionListener, IWorkbenchPreferencePage {

	protected final static int WIDTH_VALIDATION_LOWER_LIMIT = 0; //$NON-NLS-1$
	protected final static int WIDTH_VALIDATION_UPPER_LIMIT = 999; //$NON-NLS-1$

	public static Button createCheckBox(Composite group, String label) {
		Button button = new Button(group, SWT.CHECK | SWT.LEFT);
		button.setText(label);

		//GridData
		GridData data = new GridData(GridData.FILL);
		data.verticalAlignment = GridData.CENTER;
		data.horizontalAlignment = GridData.FILL;
		button.setLayoutData(data);

		return button;
	}

	protected Composite createComposite(Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NULL);

		//GridLayout
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		composite.setLayout(layout);
		composite.setFont(parent.getFont());

		//GridData
		GridData data = new GridData(GridData.FILL);
		data.horizontalIndent = 0;
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
		
		Dialog.applyDialogFont(composite);

		return composite;
	}

	protected Control createContents(Composite parent) {
		return createScrolledComposite(parent);
	}

	protected Combo createDropDownBox(Composite parent) {
		Combo comboBox = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);

		//GridData
		GridData data = new GridData();
		data.verticalAlignment = GridData.CENTER;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		comboBox.setLayoutData(data);

		return comboBox;
	}

	public static Group createGroup(Composite parent, int numColumns) {
		Group group = new Group(parent, SWT.NULL);

		//GridLayout
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		group.setLayout(layout);

		//GridData
		GridData data = new GridData(GridData.FILL);
		data.horizontalIndent = 0;
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData(data);

		return group;
	}

	protected Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.LEFT);
		label.setText(text);


		//GridData
		GridData data = new GridData(GridData.FILL);
		data.verticalAlignment = GridData.CENTER;
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);

		return label;
	}

	protected Button createRadioButton(Composite group, String label) {
		Button button = new Button(group, SWT.RADIO);
		button.setText(label);

		//GridData
		GridData data = new GridData(GridData.FILL);
		data.verticalAlignment = GridData.CENTER;
		data.horizontalAlignment = GridData.FILL;
		button.setLayoutData(data);

		return button;
	}

	protected Composite createScrolledComposite(Composite parent) {
		// create scrollbars for this parent when needed
		final ScrolledComposite sc1 = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sc1.setLayoutData(new GridData(GridData.FILL_BOTH));
		sc1.setFont(parent.getFont());
		Composite composite = createComposite(sc1, 1);
		sc1.setContent(composite);

		// not calling setSize for composite will result in a blank composite,
		// so calling it here initially
		// setSize actually needs to be called after all controls are created,
		// so scrolledComposite
		// has correct minSize
		setSize(composite);
		return composite;
	}

	protected Label createSeparator(Composite parent, int columnSpan) {
		// Create a spacer line
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);

		//GridData
		GridData data = new GridData(GridData.FILL);
		data.verticalAlignment = GridData.CENTER;
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = columnSpan;

		separator.setLayoutData(data);
		return separator;
	}

	protected Text createTextField(Composite parent) {
		Text text = new Text(parent, SWT.SINGLE | SWT.BORDER);

		//GridData
		GridData data = new GridData();
		data.verticalAlignment = GridData.CENTER;
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		text.setLayoutData(data);

		return text;
	}

	protected void enableValues() {
	}

	public void init(IWorkbench workbench) {
	}

	protected void initializeValues() {
	}

	protected boolean loadPreferences() {
		BusyIndicator.showWhile(getControl().getDisplay(), new Runnable() {
			public void run() {
				initializeValues();
				validateValues();
				enableValues();
			}
		});
		return true;
	}

	public void modifyText(ModifyEvent e) {
		// If we are called too early, i.e. before the controls are created
		// then return
		// to avoid null pointer exceptions
		if (e.widget != null && e.widget.isDisposed())
			return;

		validateValues();
		enableValues();
	}

	protected void performDefaults() {
		super.performDefaults();
	}

	public boolean performOk() {
		savePreferences();
		return true;
	}

	protected boolean savePreferences() {
		BusyIndicator.showWhile(getControl().getDisplay(), new Runnable() {
			public void run() {
				storeValues();
			}
		});
		return true;
	}

	protected void setSize(Composite composite) {
		if (composite != null) {
			Point minSize = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			composite.setSize(minSize);
			// set scrollbar composite's min size so page is expandable but
			// has scrollbars when needed
			if (composite.getParent() instanceof ScrolledComposite) {
				ScrolledComposite sc1 = (ScrolledComposite) composite.getParent();
				sc1.setMinSize(minSize);
				sc1.setExpandHorizontal(true);
				sc1.setExpandVertical(true);
			}
		}
	}

	protected void storeValues() {
		EDTUIPlugin.getDefault().saveUIPluginPreferences();
	}

	protected void validateValues() {
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

	public void widgetSelected(SelectionEvent e) {
		// If we are called too early, i.e. before the controls are created
		// then return
		// to avoid null pointer exceptions
		if (e.widget != null && e.widget.isDisposed())
			return;

		validateValues();
		enableValues();
	}
}
