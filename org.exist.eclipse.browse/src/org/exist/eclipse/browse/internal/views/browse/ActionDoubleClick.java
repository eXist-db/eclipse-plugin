/**
 * ActionDoubleClick.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.TreeItem;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.internal.connection.OpenConnectionListener;
import org.exist.eclipse.browse.internal.views.document.DocumentBrowseListener;

/**
 * @author Pascal Schmidiger
 * 
 */
public class ActionDoubleClick extends Action {

	private final BrowseView _view;

	ActionDoubleClick(BrowseView view) {
		_view = view;
	}

	@Override
	public void run() {
		TreeItem[] selection = _view.getViewer().getTree().getSelection();
		if (selection.length > 0) {
			Object data = selection[0].getData();
			if (data instanceof IConnection) {
				new ActionConnectionListener(_view,
						new OpenConnectionListener()).run();
			} else if (data instanceof IBrowseItem) {
				new ActionBrowseListener(_view, new DocumentBrowseListener())
						.run();
			}
		}
	}
}
