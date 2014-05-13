package org.exist.eclipse.browse.internal.create;

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
 * This wizard creates a new collection.
 * 
 * @author Pascal Schmidiger
 */

public class CreateCollectionWizard extends Wizard implements IWorkbenchWizard {
	private EnterCollectionWizardPage _enterCollectionPage;
	private ISelection _selection;
	private final IBrowseItem _item;
	private IBrowseService _itemService;

	/**
	 * Constructor for CreateCollectionWizard.
	 */
	public CreateCollectionWizard(IBrowseItem item) {
		super();
		_item = item;
		_itemService = (IBrowseService) _item.getAdapter(IBrowseService.class);
		this.setWindowTitle("Create a collection");
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		_enterCollectionPage = new EnterCollectionWizardPage(_selection, _item);
		addPage(_enterCollectionPage);
	}

	/**
	 * This method figures out whether the 'Finish' button should be enabled.
	 * The button should only be enabled on the
	 * {@link EnterCollectionWizardPage}.
	 */
	@Override
	public boolean canFinish() {
		return _enterCollectionPage.isPageComplete();
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
				IBrowseItem child = _item.getChild(_enterCollectionPage
						.getName());
				try {
					IBrowseService childService = (IBrowseService) child
							.getAdapter(IBrowseService.class);
					childService.create();
					// _itemService.refresh();
				} catch (ConnectionException e) {
					isFinished = false;
					_enterCollectionPage
							.setErrorMessage("Failure while create collection.");
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
