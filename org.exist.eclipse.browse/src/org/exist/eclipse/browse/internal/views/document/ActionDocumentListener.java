/**
 * BrowseListenerAction.java
 */
package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.jface.action.Action;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentListener;
import org.exist.eclipse.browse.document.IDocumentService;

/**
 * Action which will implement for the extension point <code>document</code>.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionDocumentListener extends Action {

	private final IDocumentListener _listener;
	private final DocumentView _view;
	private final IDocumentItem _item;

	public ActionDocumentListener(DocumentView view,
			IDocumentListener listener, IDocumentItem item) {
		_view = view;
		_listener = listener;
		_item = item;
	}

	@Override
	public void run() {
		IBrowseService browseService = (IBrowseService) _item.getParent()
				.getAdapter(IBrowseService.class);
		IDocumentService documentService = (IDocumentService) _item
				.getAdapter(IDocumentService.class);
		if (IManagementService.class.cast(
				_item.getParent().getConnection().getAdapter(
						IManagementService.class)).check()
				&& browseService.check() && documentService.check()) {
			_listener.init(_view.getSite().getPage());
			_listener.actionPerformed(_item);
		}
	}
}
