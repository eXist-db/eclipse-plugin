/**
 * IConnection.java
 */
package org.exist.eclipse;

import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.internal.ConnectionEnum;
import org.exist.eclipse.internal.ManagementService;
import org.xmldb.api.base.Collection;

/**
 * Interface of a XMLDB connection.
 * 
 * @author Pascal Schmidiger
 */
public interface IConnection extends AutoCloseable {

	public default <A> A getAdapter(Class<A> adapter) {
		if (adapter.isAssignableFrom(ManagementService.class)) {
			return adapter.cast(new ManagementService(this));
		}
		return null;
	}

	/**
	 * @return the name of the current connection.
	 */
	public String getName();

	/**
	 * @returns the username of the current connection.
	 */
	public String getUsername();

	/**
	 * @returns the password of the current connection.
	 */
	public String getPassword();

	/**
	 * @return the type of the connection.
	 */
	public ConnectionEnum getType();

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
