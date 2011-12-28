/**
 * MainActionGroup.java
 */

package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * @author Pascal Schmidiger
 * 
 */
public class ActionGroupMain extends ActionGroup {
	private final DocumentView _view;
	private Action _actionRefresh;

	public ActionGroupMain(DocumentView view) {
		_view = view;
	}

	public void makeActions() {
		// refresh
		_actionRefresh = new ActionRefresh(_view);
		_actionRefresh.setText("Refresh");
		_actionRefresh.setToolTipText("Refresh");
		_actionRefresh.setImageDescriptor(BrowsePlugin
				.getImageDescriptor("icons/refresh_icon.png"));
	}

	@Override
	public void fillActionBars(IActionBars actionBars) {
		fillLocalPullDown(actionBars.getMenuManager());
		fillLocalToolBar(actionBars.getToolBarManager());
	}

	@Override
	public void fillContextMenu(IMenuManager manager) {
		manager.add(_actionRefresh);
	}

	//
	// private methods
	//
	private void fillLocalPullDown(IMenuManager manager) {
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(_actionRefresh);
	}

}
