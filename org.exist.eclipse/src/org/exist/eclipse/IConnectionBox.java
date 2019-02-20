/**
 * IConnectionBox.java
 */
package org.exist.eclipse;

import java.util.Collection;

import org.exist.eclipse.listener.IConnectionListener;

/**
 * Here you can registered some {@link IConnectionListener} implementations and
 * gets all available connections.
 * 
 * @author Pascal Schmidiger
 */
public interface IConnectionBox {
	/**
	 * Add a new listener. <br />
	 * If there exists some connections in the box, then it will call the methods
	 * <ul>
	 * <li>{@link IConnectionListener#added(IConnection)}</li>
	 * <li>{@link IConnectionListener#opened(IConnection)}</li>
	 * </ul>
	 * for all connections.
	 * 
	 * @param listener the new listener
	 */
	public void addListener(IConnectionListener listener);

	/**
	 * Remove an unused listener.
	 * 
	 * @param listener
	 */
	public void removeListener(IConnectionListener listener);

	/**
	 * Remove the given <code>connection</code> from the connection box.
	 * 
	 * @param connection
	 */
	public void removeConnection(IConnection connection);

	/**
	 * @return all available connections.
	 */
	public Collection<IConnection> getConnections();
}
