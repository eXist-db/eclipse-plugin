package org.exist.eclipse.browse.internal.move;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.exception.ConnectionException;

/**
 * This wizard renames and/or moves a collection.
 */

public class MoveCollectionWizard extends Wizard implements IWorkbenchWizard {
	private MoveCollectionWizardPage _moveCollectionPage;
	private ISelection _selection;
	private final IBrowseItem _item;
	private IBrowseService _itemService;

	/**
	 * Constructor for MoveCollectionWizard.
	 */
	public MoveCollectionWizard(IBrowseItem item) {
		super();
		_item = item;
		_itemService = (IBrowseService) _item.getAdapter(IBrowseService.class);
		this.setWindowTitle("Rename/Move a collection");
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		_moveCollectionPage = new MoveCollectionWizardPage(_selection, _item);
		addPage(_moveCollectionPage);
	}

	/**
	 * This method figures out whether the 'Finish' button should be enabled.
	 * The button should only be enabled on the {@link MoveCollectionWizardPage}.
	 */
	@Override
	public boolean canFinish() {
		return _moveCollectionPage.isPageComplete();
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		boolean isFinished = true;
		if (IManagementService.class.cast(
				_item.getConnection().getAdapter(IManagementService.class))
				.check()) {
			if (_itemService.check()) {
				IBrowseItem newItem = _moveCollectionPage.getNewItem();
				try {
					_itemService.move(newItem);
				} catch (ConnectionException e) {
					isFinished = false;
					_moveCollectionPage
							.setErrorMessage("Failure while move collection.");
				}
			}
		}

		return isFinished;
	}

	@Override
	public boolean performCancel() {
		return _itemService.check();
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
