/**
 * 
 */
package org.exist.eclipse.auto.internal.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * This is a wizard that gives you the possibility to create a new automation.
 * 
 * @author Markus Tanner
 */
public class AutomationNewWizard extends Wizard implements INewWizard {

	private static final String WIZARD_TITLE = "Create new Automation";
	private AutomationNewWizardPage _page;
	private IStructuredSelection _selection;

	/**
	 * AutomationNewWizard Constructor
	 */
	public AutomationNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		_page = new AutomationNewWizardPage(_selection);
		_page.setTitle(WIZARD_TITLE);
		addPage(_page);
	}

	@Override
	public boolean performFinish() {
		return _page.finish();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_selection = selection;
		setWindowTitle(WIZARD_TITLE);
	}

}
