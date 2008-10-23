package org.exist.eclipse.browse.internal.delete;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;

/**
 * This wizards deletes a chose of collections.
 */

public class DeleteCollectionWizard extends Wizard implements IWorkbenchWizard {
	private ListCollectionWizardPage _enterCollectionPage;
	private ISelection _selection;
	private final IBrowseItem[] _items;

	/**
	 * Constructor for CreateCollectionWizard.
	 */
	public DeleteCollectionWizard(IBrowseItem[] items) {
		super();
		_items = items;
		this.setWindowTitle("Delete");
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		_enterCollectionPage = new ListCollectionWizardPage(_selection, _items);
		addPage(_enterCollectionPage);
	}

	/**
	 * This method figures out whether the 'Finish' button should be enabled.
	 * The button should only be enabled on the {@link ListCollectionWizardPage}.
	 */
	public boolean canFinish() {
		return _enterCollectionPage.isPageComplete();
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {
		boolean isFinished = true;

		if (IManagementService.class.cast(
				_items[0].getConnection().getAdapter(IManagementService.class))
				.check()) {
			Collection<IBrowseItem> deleteItems = new ArrayList<IBrowseItem>();
			for (IBrowseItem item : _items) {
				IBrowseService service = IBrowseService.class.cast(item
						.getAdapter(IBrowseService.class));
				if (service.check()) {
					deleteItems.add(item);
				}
			}

			if (deleteItems.size() > 0) {
				DeleteCollectionJob job = new DeleteCollectionJob(deleteItems
						.toArray(new IBrowseItem[deleteItems.size()]));
				job.setUser(true);
				job.schedule();
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
