/**
 * MainActionGroup.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionGroup;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * Action group for the main actions.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionGroupMain extends ActionGroup {
	private final BrowseView _view;
	private Action _actionNewConnection;

	public ActionGroupMain(BrowseView view) {
		_view = view;
	}

	public void makeActions() {
		// new
		_actionNewConnection = new ActionNewConn(_view.getViewSite());
		_actionNewConnection.setText("New Connection...");
		_actionNewConnection.setToolTipText("Create a new connection");
		_actionNewConnection.setImageDescriptor(BrowsePlugin.getImageDescriptor("icons/new_connection_icon.gif"));
	}

	@Override
	public void fillActionBars(IActionBars actionBars) {
		fillLocalPullDown(actionBars.getMenuManager());
		fillLocalToolBar(actionBars.getToolBarManager());
	}

	@Override
	public void fillContextMenu(IMenuManager manager) {
		manager.add(_actionNewConnection);
	}

	//
	// private methods
	//
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(_actionNewConnection);
		manager.add(new Separator());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(_actionNewConnection);
		manager.add(new Separator());
		_view.getDrillDownAdapter().addNavigationActions(manager);
	}

}
