/**
 * CreateBrowseListener.java
 */

package org.exist.eclipse.browse.internal.delete;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.UiUtil;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentListener;
import org.exist.eclipse.browse.document.IDocumentService;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * @author Pascal Schmidiger
 * 
 */
public class DeleteDocumentListener implements IDocumentListener {

	private IWorkbenchPage _page;

	@Override
	public void actionPerformed(IDocumentItem[] items) {
		if (items.length == 0) {
			return;
		}

		String msg;
		if (items.length == 1) {
			msg = "Delete document '" + items[0].getPath() + "'?";
		} else {
			msg = "Delete " + items.length + " documents?";
		}

		Shell shell = _page.getWorkbenchWindow().getShell();
		boolean confirm = UiUtil.openConfirm(shell, "Delete", msg, "Delete");
		if (confirm) {

			boolean hadErrors = false;

			for (IDocumentItem item : items) {
				try {
					IBrowseItem coll = item.getParent();
					IBrowseService browseService = IBrowseService.class
							.cast(coll.getAdapter(IBrowseService.class));
					IDocumentService documentService = IDocumentService.class
							.cast(item.getAdapter(IDocumentService.class));
					if (IManagementService.class.cast(
							coll.getConnection().getAdapter(
									IManagementService.class)).check()
							&& browseService.check() && documentService.check()) {
						documentService.delete();
					}
				} catch (Exception e) {
					hadErrors = true;
					Status status = new Status(IStatus.ERROR, BrowsePlugin
							.getId(), "Error while deleting document '"
							+ item.getName() + "': " + e, e);
					BrowsePlugin.getDefault().getLog().log(status);
				}
			}

			if (hadErrors) {
				MessageDialog
						.openError(shell, "Delete",
								"Errors occured while deleting. See the Eclipse Error Log for details.");
			}
		}
	}

	@Override
	public void init(IWorkbenchPage page) {
		_page = page;
	}

}
