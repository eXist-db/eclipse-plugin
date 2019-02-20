
package org.exist.eclipse.query.internal.item;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;

/**
 * With this wizard you can choose a collection.
 * 
 * @author Pascal Schmidiger
 */

public class ChooseCollectionWizard extends Wizard implements IWorkbenchWizard {
	private ChooseCollectionWizardPage _chooseCollectionPage;
	private ISelection _selection;
	private final IConnection _connection;
	private final IBrowseItem _item;

	/**
	 * Constructor for NewConnectionWizard.
	 */
	public ChooseCollectionWizard(IConnection connection, IBrowseItem item) {
		super();
		_connection = connection;
		_item = item;
		this.setWindowTitle("Choose a Collection");
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		_chooseCollectionPage = new ChooseCollectionWizardPage(_selection, _connection, _item);
		addPage(_chooseCollectionPage);
	}

	/**
	 * This method figures out whether the 'Finish' button should be enabled. The
	 * button should only be enabled on the NewConnectionWizardPage.
	 */
	@Override
	public boolean canFinish() {
		return _chooseCollectionPage.isPageComplete();
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will
	 * create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		boolean isFinished = true;
		if (_connection.getAdapter(IManagementService.class).check()) {
			IBrowseItem item = _chooseCollectionPage.getSelection();
			if (IBrowseService.class.cast(item.getAdapter(IBrowseService.class)).check()) {
				ChangeItemNotifier.getInstance().change(item);
			} else {
				isFinished = false;
			}
		}

		return isFinished;
	}

	@Override
	public boolean performCancel() {
		return true;
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_selection = selection;
	}

}
