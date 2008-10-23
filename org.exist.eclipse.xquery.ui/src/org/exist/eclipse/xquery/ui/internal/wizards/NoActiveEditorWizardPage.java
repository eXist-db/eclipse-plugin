package org.exist.eclipse.xquery.ui.internal.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.editor.IXQueryEditor;

/**
 * Info page if the active editor is not a {@link IXQueryEditor}.
 * 
 * @author Pascal Schmidiger
 */
public class NoActiveEditorWizardPage extends WizardPage {

	public NoActiveEditorWizardPage() {
		super("noactiveeditorwizardpage");
		setTitle(SelectContextWizard.WIZARD_TITLE);
		setErrorMessage("No active XQuery Editor available");
		setImageDescriptor(XQueryUI
				.getImageDescriptor("icons/hslu_exist_eclipse_logo.jpg"));
		setPageComplete(true);
	}

	public void createControl(Composite parent) {
		Label label = new Label(parent, SWT.NULL);
		label.setText("Please open a file in the XQuery Editor.");
		setControl(parent);
	}

}
