/**
 * IConnectionListener.java
 */
package org.exist.eclipse.listener;

import org.exist.eclipse.IConnection;

/**
 * If you implement this interface, you will inform about a new or a removed
 * connection. Thus you can react on a added or removed connection. It is very
 * important that you registered your implementation on
 * {@link ConnectionRegistration#addListener(IConnectionListener)} and removed
 * it on {@link ConnectionRegistration#removeListener(IConnectionListener)} if
 * you does not use it.
 * 
 * @see ConnectionRegistration
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IConnectionListener {
	/**
	 * The method will call if the given <code>connection</code> was added. If
	 * this method call, the connection must not be opened. If the connection is
	 * opened, then the method {@link #opened(IConnection)} will call.
	 * 
	 * @param connection
	 *            the added connection.
	 */
	public void added(IConnection connection);

	/**
	 * The method will call if the given <code>connection </code> was removed.
	 * 
	 * @param connection
	 *            the removed invalid connection.
	 */
	public void removed(IConnection connection);

	/**
	 * The method will call if the given <code>connection </code> was opened.
	 * 
	 * @param connection
	 *            the opened valid connection.
	 */
	public void opened(IConnection connection);

	/**
	 * The method will call if the given <code>connection </code> was closed.
	 * 
	 * @param connection
	 *            the closed valid connection.
	 */
	public void closed(IConnection connection);
}
