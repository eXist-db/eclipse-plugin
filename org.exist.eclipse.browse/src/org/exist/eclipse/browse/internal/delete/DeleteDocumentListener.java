/**
 * CreateBrowseListener.java
 */
package org.exist.eclipse.browse.internal.delete;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentListener;
import org.exist.eclipse.browse.document.IDocumentService;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.browse.internal.browse.BrowseItemInvisible;
import org.exist.eclipse.exception.ConnectionException;

/**
 * @author Pascal Schmidiger
 * 
 */
public class DeleteDocumentListener implements IDocumentListener {

	private IWorkbenchPage _page;

	public void actionPerformed(IDocumentItem item) {
		try {
			if (item.getParent() instanceof BrowseItemInvisible) {
				MessageDialog.openInformation(_page.getWorkbenchWindow()
						.getShell(), "Delete", "Could not delete '"
						+ item.getName() + "'");
			} else {
				boolean confirm = MessageDialog.openConfirm(_page
						.getWorkbenchWindow().getShell(), "Delete",
						"Delete the document '" + item.getPath() + "'");
				if (confirm) {
					IBrowseService browseService = IBrowseService.class
							.cast(item.getParent().getAdapter(
									IBrowseService.class));
					IDocumentService documentService = IDocumentService.class
							.cast(item.getAdapter(IDocumentService.class));
					if (IManagementService.class.cast(
							item.getParent().getConnection().getAdapter(
									IManagementService.class)).check()
							&& browseService.check() && documentService.check()) {
						documentService.delete();
					}
				}
			}
		} catch (ConnectionException e) {
			Status status = new Status(Status.ERROR, BrowsePlugin.getId(),
					"Error while deleting collection '" + item.getName() + "'",
					e);
			BrowsePlugin.getDefault().getLog().log(status);
		}
	}

	public void init(IWorkbenchPage page) {
		_page = page;
	}

}
