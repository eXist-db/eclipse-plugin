package org.exist.eclipse.util.internal.restore;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.util.internal.backup.BackupTargetWizardPage;

/**
 * This is the Wizard that handles the restore process.
 * 
 * @author Markus Tanner
 * 
 */
public class RestoreWizard extends Wizard implements IWorkbenchWizard {

	private BackupLocationWizardPage _backupLocationPage;
	private ISelection _selection;
	private final IBrowseItem _item;
	private IBrowseService _itemService;
	private RestoreJob _job;

	public RestoreWizard(IBrowseItem item) {
		super();
		_item = item;
		_itemService = (IBrowseService) _item.getAdapter(IBrowseService.class);
		this.setWindowTitle("Restore from backup");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_selection = selection;
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		_backupLocationPage = new BackupLocationWizardPage(_selection);
		addPage(_backupLocationPage);
	}

	/**
	 * This method figures out whether the 'Finish' button should be enabled.
	 * The button should only be enabled on the {@link BackupTargetWizardPage}.
	 */
	@Override
	public boolean canFinish() {
		return _backupLocationPage.isAbleToFinish();
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
			if (IBrowseService.class.cast(
					_item.getAdapter(IBrowseService.class)).check()) {
				_job = new RestoreJob(_item, _backupLocationPage
						.getBackupLocation());
				_job.setUser(true);
				_job.schedule();
			}
		}

		return isFinished;
	}

	@Override
	public boolean performCancel() {
		return _itemService.check();
	}

}
