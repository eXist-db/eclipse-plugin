/**
 * IConnection.java
 */
package org.exist.eclipse;

import org.eclipse.core.runtime.IAdaptable;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.internal.ConnectionEnum;
import org.xmldb.api.base.Collection;

/**
 * Interface of a XMLDB connection.
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IConnection extends IAdaptable {
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
	 * This method returns the collection for the given path
	 * 
	 * @param path
	 *            the collection path name
	 * @return the collection object
	 */
	public Collection getCollection(String path);

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
	 * registered. This connection can be closed, but the initial connection
	 * will remain open.
	 * 
	 * @return an IConnecton
	 */
	public IConnection duplicate() throws ConnectionException;
}
