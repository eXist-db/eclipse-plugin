package org.exist.eclipse.browse.internal.move;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentService;
import org.exist.eclipse.exception.ConnectionException;

/**
 * This wizard renames a document.
 */

public class RenameDocumentWizard extends Wizard implements IWorkbenchWizard {
	private RenameDocumentWizardPage _renameDocumentPage;
	private ISelection _selection;
	private final IDocumentItem _item;
	private IDocumentService _itemService;

	/**
	 * Constructor for MoveCollectionWizard.
	 */
	public RenameDocumentWizard(IDocumentItem item) {
		super();
		_item = item;
		_itemService = (IDocumentService) _item
				.getAdapter(IDocumentService.class);
		this.setWindowTitle("Rename a document");
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		_renameDocumentPage = new RenameDocumentWizardPage(_selection, _item);
		addPage(_renameDocumentPage);
	}

	/**
	 * This method figures out whether the 'Finish' button should be enabled.
	 * The button should only be enabled on the {@link MoveCollectionWizardPage}.
	 */
	public boolean canFinish() {
		return _renameDocumentPage.isPageComplete();
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		boolean isFinished = true;
		if (IManagementService.class.cast(
				_item.getParent().getConnection().getAdapter(
						IManagementService.class)).check()) {

			if (IBrowseService.class.cast(
					_item.getParent().getAdapter(IBrowseService.class)).check()) {

				if (_itemService.check()) {
					IDocumentItem newItem = _renameDocumentPage.getNewItem();
					try {
						_itemService.move(newItem);
					} catch (ConnectionException e) {
						isFinished = false;
						_renameDocumentPage
								.setErrorMessage("Failure while move document.");
					}
				}
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
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_selection = selection;
	}

}
