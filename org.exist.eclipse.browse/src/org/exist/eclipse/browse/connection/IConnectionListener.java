/**
 * IConnectionListener.java
 */
package org.exist.eclipse.browse.connection;

import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.IConnection;

/**
 * This is the interface for the extension point 'connection'.
 * 
 * The time flow of the method is:
 * <ol>
 * <li>call {@link #init(IWorkbenchPage)}</li>
 * <li>call {@link #actionPerformed(IConnection)}</li>
 * </ol>
 * 
 * @author Pascal Schmidiger
 */
public interface IConnectionListener {
	/**
	 * You have the possibility to start a view or something else.
	 * 
	 * @param page
	 */
	public void init(IWorkbenchPage page);

	/**
	 * You gets a {@link IConnection}, which was selected.
	 * 
	 * @param connection
	 *            on which you will perform your action.
	 */
	public void actionPerformed(IConnection connection);
}
