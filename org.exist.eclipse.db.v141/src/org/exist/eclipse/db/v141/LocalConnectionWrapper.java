/**
 * 
 */
package org.exist.eclipse.db.v141;

import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.Database;

/**
 * @author Markus Tanner
 * 
 */
public class LocalConnectionWrapper extends LocalConnection {

	/**
	 * @param name
	 * @param username
	 * @param password
	 * @param path
	 */
	public LocalConnectionWrapper(String name, String username, String password, String path, String uriName,
			Database db) {
		super(name, username, password, path, uriName);
		setDb(db);
	}

	@Override
	public void open() throws ConnectionException {
		openRoot();
	}

	@Override
	public void close() throws ConnectionException {
		// closeRoot();
	}

}
