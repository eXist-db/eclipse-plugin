package org.exist.eclipse.internal.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

/**
 * This is a sample new wizard. Its role is to create a new remote or local
 * connection.
 * 
 * @author Markus Tanner
 */
public class NewConnectionWizard extends Wizard implements INewWizard {
	static final String WIZARD_TITLE = "New eXist Connection";
	private IWorkbench _workbench;
	private ConnectionTypeWizardPage _page;
	private IStructuredSelection _selection;

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		_page = new ConnectionTypeWizardPage(_workbench, _selection);
		addPage(_page);
	}

	/**
	 * This method figures out whether the 'Finish' button should be enabled.
	 * The button should only be enabled on the NewConnectionWizardPage.
	 */
	public boolean canFinish() {
		return false;
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_workbench = workbench;
		_selection = selection;
		setWindowTitle(WIZARD_TITLE);
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
