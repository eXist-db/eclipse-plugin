package org.exist.eclipse.xquery.ui.internal.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * Info page if no context is registered.
 * 
 * @author Pascal Schmidiger
 */
public class NoContextWizardPage extends WizardPage {

	public NoContextWizardPage() {
		super("noactiveeditorwizardpage");
		setTitle(SelectContextWizard.WIZARD_TITLE);
		setErrorMessage("No context available.");
		setImageDescriptor(XQueryUI
				.getImageDescriptor("icons/hslu_exist_eclipse_logo.jpg"));
		setPageComplete(true);
	}

	public void createControl(Composite parent) {
		Label label = new Label(parent, SWT.NULL);
		label.setText("Please open a context in order to run a query.");
		setControl(parent);
	}

}
