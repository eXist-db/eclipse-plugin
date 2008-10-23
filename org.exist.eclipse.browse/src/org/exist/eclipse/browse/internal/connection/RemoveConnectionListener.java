/**
 * RemoveConnectionListener.java
 */
package org.exist.eclipse.browse.internal.connection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.ConnectionFactory;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.connection.IConnectionListener;

/**
 * @author Pascal Schmidiger
 * 
 */
public class RemoveConnectionListener implements IConnectionListener {

	private IWorkbenchPage _page;

	public void actionPerformed(IConnection connection) {
		boolean confirm = MessageDialog.openConfirm(_page.getWorkbenchWindow()
				.getShell(), "Delete", "Delete the connection '"
				+ connection.getName() + "'");
		if (confirm) {
			ConnectionFactory.getConnectionBox().removeConnection(connection);
		}
	}

	public void init(IWorkbenchPage page) {
		_page = page;
	}

}
