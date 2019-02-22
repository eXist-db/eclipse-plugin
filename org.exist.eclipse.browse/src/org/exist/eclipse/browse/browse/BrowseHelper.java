/**
 * BrowseHelper.java
 */
package org.exist.eclipse.browse.browse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.browse.internal.browse.BrowseItem;
import org.xmldb.api.base.XMLDBException;

/**
 * Helper class to get {@link IBrowseItem} directly.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class BrowseHelper {
	/**
	 * Return the root item for the given <code>connection</code>.
	 * 
	 * @param connection the connection object
	 * @return the actual browse item
	 */
	public static IBrowseItem getRootBrowseItem(IConnection connection) {
		IBrowseItem item = null;

		try {
			item = new BrowseItem(connection, connection.getRoot().getName());
		} catch (XMLDBException e) {
			Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(), "Error while getting root collection", e);
			BrowsePlugin.getDefault().getLog().log(status);
		}
		return item;
	}

	/**
	 * Return the {@link IBrowseItem} for the given <code>path</code> on the given
	 * <code>connection</code>.
	 * 
	 * @param connection the connection object
	 * @param path       the actual collection path
	 * @return the actual browse item
	 */
	public static IBrowseItem getBrowseItem(IConnection connection, String path) {
		return new BrowseItem(connection, path);
	}
}
