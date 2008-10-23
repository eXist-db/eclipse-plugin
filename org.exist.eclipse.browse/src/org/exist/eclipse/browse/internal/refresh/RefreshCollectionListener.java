/**
 * CreateBrowseListener.java
 */
package org.exist.eclipse.browse.internal.refresh;

import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.browse.IBrowseService;

/**
 * @author Pascal Schmidiger
 * 
 */
public class RefreshCollectionListener implements IBrowseListener {

	public void actionPerformed(IBrowseItem[] items) {
		for (IBrowseItem item : items) {
			IBrowseService.class.cast(item.getAdapter(IBrowseService.class))
					.refresh();
		}
	}

	public void init(IWorkbenchPage page) {
	}

}
