/**
 * BrowseListenerAction.java
 */

package org.exist.eclipse.browse.internal.views.document;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
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

	public ActionDocumentListener(DocumentView view, IDocumentListener listener) {
		_view = view;
		_listener = listener;
	}

	@Override
	public void run() {

		Object[] sel = ((IStructuredSelection) _view.getViewer().getSelection())
				.toArray();
		if (sel.length > 0) {
			IDocumentItem first = (IDocumentItem) sel[0];

			// assume that others are also valid if first check succeed
			IBrowseService browseService = (IBrowseService) first.getParent()
					.getAdapter(IBrowseService.class);
			IDocumentService documentService = (IDocumentService) first
					.getAdapter(IDocumentService.class);
			if (IManagementService.class.cast(
					first.getParent().getConnection().getAdapter(
							IManagementService.class)).check()
					&& browseService.check() && documentService.check()) {
				_listener.init(_view.getSite().getPage());

				List<IDocumentItem> all = new ArrayList<IDocumentItem>();
				for (Object it : sel) {
					all.add((IDocumentItem) it);
				}
				_listener.actionPerformed(all.toArray(new IDocumentItem[all
						.size()]));
			}
		}
	}
}
