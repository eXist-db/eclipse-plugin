/**
 * BrowseActionNewConn.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IViewSite;
import org.exist.eclipse.ConnectionFactory;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.exception.ConnectionException;

/**
 * Action to open a connection.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionNewConn extends Action {
	private final IViewSite _site;

	public ActionNewConn(IViewSite site) {
		_site = site;
	}

	@Override
	public void run() {
		try {
			ConnectionFactory.createConnection(_site.getWorkbenchWindow()
					.getWorkbench());
		} catch (ConnectionException e) {
			String message = "Error while create connection";
			Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(),
					message, e);
			BrowsePlugin.getDefault().errorDialog(message, e.getMessage(),
					status);
		}
	}
}
