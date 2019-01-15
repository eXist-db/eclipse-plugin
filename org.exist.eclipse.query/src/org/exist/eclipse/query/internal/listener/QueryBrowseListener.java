/**
 * QueryBrowseListener.java
 */
package org.exist.eclipse.query.internal.listener;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.query.internal.QueryPlugin;
import org.exist.eclipse.query.internal.item.ChangeItemNotifier;
import org.exist.eclipse.query.internal.views.QueryView;

/**
 * Open the query view and set the context.
 * 
 * @author Markus Tanner
 * 
 */
public class QueryBrowseListener implements IBrowseListener {

	@Override
	public void actionPerformed(IBrowseItem[] items) {
		IBrowseService service = (IBrowseService) items[0]
				.getAdapter(IBrowseService.class);
		if (service.check()) {
			ChangeItemNotifier.getInstance().change(items[0]);
		}
	}

	@Override
	public void init(IWorkbenchPage page) {
		if (page != null) {
			try {
				page.showView(QueryView.ID);
			} catch (PartInitException e) {
				String message = "Error while show query view";
				Status status = new Status(IStatus.ERROR, QueryPlugin.getId(),
						message, e);
				QueryPlugin.getDefault().errorDialog(message, e.getMessage(),
						status);
			}
		}
	}
}
