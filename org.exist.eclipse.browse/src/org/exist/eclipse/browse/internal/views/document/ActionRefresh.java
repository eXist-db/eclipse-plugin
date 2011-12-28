/**
 * DocumentActionRefresh.java
 */
package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.jface.action.Action;

/**
 * @author Pascal Schmidiger
 * 
 */
public class ActionRefresh extends Action {
	private final DocumentView _view;

	public ActionRefresh(DocumentView view) {
		_view = view;
	}

	@Override
	public void run() {
		_view.refresh();
	}
}
