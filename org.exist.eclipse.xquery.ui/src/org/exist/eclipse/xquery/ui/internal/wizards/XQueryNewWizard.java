/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard for creating a new XQuery file.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryNewWizard extends Wizard implements INewWizard {
	private static final String WIZARD_TITLE = "Create new XQuery";
	private XQueryNewWizardPage _page;
	private IStructuredSelection _selection;

	public XQueryNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		_page = new XQueryNewWizardPage(_selection);
		_page.setTitle(WIZARD_TITLE);
		addPage(_page);
	}

	public boolean performFinish() {
		return _page.finish();
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_selection = selection;
		setWindowTitle(WIZARD_TITLE);
	}

}
