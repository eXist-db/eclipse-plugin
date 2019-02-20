/**
 * IConnection.java
 */
package org.exist.eclipse;

import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.Collection;

/**
 * Interface of a XMLDB connection.
 * 
 * @author Pascal Schmidiger
 */
public interface IConnection extends AutoCloseable, ICredentials {

	/**
	 * Returns the given {@code adapter} instance if available or {@code null} if not.
	 * 
	 * @param adapter the adapter type
	 * @return the adapter implementation if available
	 */
	public <A> A getAdapter(Class<A> adapter);

	/**
	 * @return the name of the current connection.
	 */
	public String getName();

	/**
	 * @return the type of the connection.
	 */
	public ConnectionType getType();

	/**
	 * @return the path of the connection.
	 */
	public String getPath();

	/**
	 * This method gets the root-collection from the database.
	 * 
	 * @return the root-collection
	 */
	public Collection getRoot();

	/**
	 * This method opens a connection to the database.
	 * 
	 * @throws ConnectionException
	 */
	public void open() throws ConnectionException;

	/**
	 * This method closes the connection to the database.
	 * 
	 * @throws ConnectionException
	 */
	@Override
	public void close() throws ConnectionException;

	/**
	 * @return the uri of the current connection
	 */
	public String getUri();

	/**
	 * @return <code>true</code> if the connection is open, otherwise
	 *         <code>false</code>.
	 */
	public boolean isOpen();

	/**
	 * This method clones the actual connection. This connection won't be
	 * registered. This connection can be closed, but the initial connection will
	 * remain open.
	 * 
	 * @return an IConnecton
	 */
	public IConnection duplicate() throws ConnectionException;

	/**
	 * @return the exist version
	 */
	public String getVersion();
}
