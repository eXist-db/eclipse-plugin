/**
 * OpenConnectionListener.java
 */
package org.exist.eclipse.browse.internal.connection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.ConnectionFactory;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.connection.IConnectionListener;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.exception.ConnectionException;

/**
 * @author Pascal Schmidiger
 * 
 */
public class CopyConnectionListener implements IConnectionListener {

	private IWorkbenchPage _page;

	@Override
	public void actionPerformed(IConnection connection) {
		try {
			ConnectionFactory.changeConnection(_page.getWorkbenchWindow()
					.getWorkbench(), connection, true);
		} catch (ConnectionException e) {
			String message = "Error while copy connection";
			Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(),
					message, e);
			BrowsePlugin.getDefault().errorDialog(message, e.getMessage(),
					status);
		}
	}

	@Override
	public void init(IWorkbenchPage page) {
		_page = page;
	}

}
