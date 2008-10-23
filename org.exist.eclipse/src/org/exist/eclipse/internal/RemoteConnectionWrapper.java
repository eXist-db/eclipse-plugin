/**
 * 
 */
package org.exist.eclipse.internal;

import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.Database;

/**
 * @author Markus Tanner
 * 
 */
public class RemoteConnectionWrapper extends RemoteConnection {

	/**
	 * @param name
	 * @param username
	 * @param password
	 * @param path
	 */
	public RemoteConnectionWrapper(String name, String username,
			String password, String path, Database db) {
		super(name, username, password, path);
		setDb(db);
	}

	@Override
	public void open() throws ConnectionException {
		openRoot();
	}

	@Override
	public void close() throws ConnectionException {
		closeRoot();
	}

}
