/**
 * BrowseListenerAction.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;

/**
 * Action which will implement for the extension point <code>collection</code>.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionBrowseListener extends Action {

	private final IBrowseListener _listener;
	private final BrowseView _view;

	public ActionBrowseListener(BrowseView view, IBrowseListener listener) {
		_view = view;
		_listener = listener;
	}

	@Override
	public void run() {
		Object[] selection = ((IStructuredSelection) _view.getViewer()
				.getSelection()).toArray();
		if (selection.length > 0) {
			IBrowseItem[] items = new IBrowseItem[selection.length];
			for (int i = 0; i < selection.length; i++) {
				Object data = selection[i];
				if (data instanceof IBrowseItem) {
					items[i] = (IBrowseItem) data;
				} else {
					items = new IBrowseItem[0];
					break;
				}
			}
			if (items.length > 0) {
				if (IManagementService.class.cast(
						items[0].getConnection().getAdapter(
								IManagementService.class)).check()) {
					_listener.init(_view.getSite().getPage());
					_listener.actionPerformed(items);
				}
			}
		}
	}
}
