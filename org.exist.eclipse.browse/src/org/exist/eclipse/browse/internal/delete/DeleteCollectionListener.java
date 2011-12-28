/**
 * CreateBrowseListener.java
 */

package org.exist.eclipse.browse.internal.delete;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.UiUtil;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.browse.IBrowseService;

/**
 * @author Pascal Schmidiger
 * 
 */
public class DeleteCollectionListener implements IBrowseListener {

	private IWorkbenchPage _page;

	public void actionPerformed(IBrowseItem[] items) {
		if (IManagementService.class.cast(
				items[0].getConnection().getAdapter(IManagementService.class))
				.check()) {
			String msg;
			if (items.length == 1) {
				msg = "Delete collection '" + items[0].getPath() + "'?";
			} else {
				msg = "Delete " + items.length + " collections?";
			}
			Shell shell = _page.getWorkbenchWindow().getShell();
			boolean confirm = UiUtil
					.openConfirm(shell, "Delete", msg, "Delete");
			if (confirm) {
				Collection<IBrowseItem> deleteItems = new ArrayList<IBrowseItem>();
				for (IBrowseItem item : items) {
					IBrowseService service = IBrowseService.class.cast(item
							.getAdapter(IBrowseService.class));
					if (service.check()) {
						deleteItems.add(item);
					}
				}

				if (deleteItems.size() > 0) {
					DeleteCollectionJob job = new DeleteCollectionJob(
							deleteItems.toArray(new IBrowseItem[deleteItems
									.size()]));
					job.setUser(true);
					job.schedule();
				}
			}
		}
	}

	public void init(IWorkbenchPage page) {
		_page = page;
	}

}
