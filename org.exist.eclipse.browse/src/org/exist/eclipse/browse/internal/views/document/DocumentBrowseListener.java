/**
 * DocumentBrowseListener.java
 */
package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * Implementation, which is needed for browse extension. Have a look at
 * plugin.xml > extension.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class DocumentBrowseListener implements IBrowseListener {

	private DocumentView _view;

	@Override
	public void actionPerformed(IBrowseItem[] items) {
		if (_view != null) {
			IBrowseService service = items[0].getAdapter(IBrowseService.class);
			if (service.check()) {
				_view.setItem(items[0]);
			}
		}
	}

	@Override
	public void init(IWorkbenchPage page) {
		if (page != null) {
			try {
				_view = (DocumentView) page.showView(DocumentView.ID);
			} catch (PartInitException e) {
				String message = "Error while open document view";
				Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(), message, e);
				BrowsePlugin.getDefault().errorDialog(message, e.getMessage(), status);
			}
		}
	}

}
