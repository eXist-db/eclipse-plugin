/**
 * BrowseListenerAction.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.TreeItem;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.connection.IConnectionListener;

/**
 * Action which will implement for the extension point <code>collection</code>.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionConnectionListener extends Action {

	private final IConnectionListener _listener;
	private final BrowseView _view;

	public ActionConnectionListener(BrowseView view,
			IConnectionListener listener) {
		_view = view;
		_listener = listener;
	}

	@Override
	public void run() {
		TreeItem[] selection = _view.getViewer().getTree().getSelection();
		if (selection.length > 0) {
			_listener.init(_view.getSite().getPage());
			for (int i = 0; i < selection.length; i++) {
				Object data = selection[i].getData();
				if (data instanceof IConnection) {
					_listener.actionPerformed((IConnection) data);
				}
			}
		}
	}
}
