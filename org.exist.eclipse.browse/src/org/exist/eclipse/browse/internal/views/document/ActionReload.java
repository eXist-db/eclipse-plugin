/**
 * DocumentActionRefresh.java
 */
package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.jface.action.Action;

/**
 * @author Pascal Schmidiger
 * 
 */
public class ActionReload extends Action {
	private final DocumentView _view;

	public ActionReload(DocumentView view) {
		_view = view;
	}

	@Override
	public void run() {
		_view.refresh();
	}
}
