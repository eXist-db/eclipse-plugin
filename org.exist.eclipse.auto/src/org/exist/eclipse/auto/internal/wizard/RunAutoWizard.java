/**
 * 
 */
package org.exist.eclipse.auto.internal.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.auto.connection.IAutoContext;
import org.exist.eclipse.auto.internal.model.IAutoModel;
import org.exist.eclipse.auto.internal.run.AutomationHandler;

/**
 * This is a wizard that initializes the execution of a automation.
 * 
 * @author Markus Tanner
 */
public class RunAutoWizard extends Wizard implements IWorkbenchWizard {

	static final String WIZARD_TITLE = "Run Automation";
	static final String WIZARD_DESCRIPTION = "This wizard initializes the execution of an automation.";
	private RunAutoContextPage _contextWizardPage;
	private RunAutoCollectionPage _collectionWizardPage;
	private IAutoModel _autoModel;

	@Override
	public boolean performFinish() {
		IAutoContext autoContext = _contextWizardPage.getAutoContext();
		String target = _contextWizardPage.getTarget();
		String collection = _collectionWizardPage.getSelection();
		AutomationHandler.getInstance().run(_autoModel, autoContext,
				collection, target);
		return true;
	}

	/**
	 * Constructor
	 */
	public RunAutoWizard(IAutoModel autoModel) {
		super();
		setWindowTitle(WIZARD_TITLE);
		_autoModel = autoModel;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_contextWizardPage = new RunAutoContextPage();
		_collectionWizardPage = new RunAutoCollectionPage();
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		addPage(_contextWizardPage);
		addPage(_collectionWizardPage);
	}

}
