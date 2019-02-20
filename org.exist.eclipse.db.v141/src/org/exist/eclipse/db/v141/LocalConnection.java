/**
 * LocalConnection.java
 */
package org.exist.eclipse.db.v141;

import java.nio.file.Paths;

import org.exist.eclipse.ConnectionType;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.spi.AbstractConnection;
import org.exist.xmldb.DatabaseImpl;
import org.exist.xmldb.DatabaseInstanceManager;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

/**
 * With this class you can connect to a local database and start it in the same
 * JVM.
 * 
 * @author Pascal Schmidiger
 */
public class LocalConnection extends AbstractConnection {
	private static int _counter = 0;

	private final String uriName;
	private Database db;
	private Collection root;

	/**
	 * Create a new connection. Post you have to call {@link #open()}.
	 * 
	 * @param name     of the connection, mandatory field.
	 * @param username with which you connect to the database, mandatory field.
	 * @param password of the username, optional field.
	 * @param path     the uri to the configuration field
	 */
	public LocalConnection(String name, String username, String password, String path) {
		this(name, username, password, path, getNextUri());
	}

	public LocalConnection(String name, String username, String password, String path, String uri) {
		super(ExistConnectionProvider.VERSION, ConnectionType.LOCAL, name, username, password, Paths.get(path).toAbsolutePath().toString());
		uriName = uri;
	}

	private String getUriName() {
		return uriName;
	}

	private static synchronized String getNextUri() {
		return "local" + _counter++;
	}

	@Override
	public void open() throws ConnectionException {
		openDb();
		openRoot();
		registerConnection();
	}

	/**
	 * @throws ConnectionException
	 */
	protected void openRoot() throws ConnectionException {
		if (root == null) {
			try {
				root = DatabaseManager.getCollection(getRootUri(), getUsername(), getPassword());
				// check whether a connection was established successfully.
				root.getChildCollectionCount();
			} catch (Exception e) {
				root = null;
				close();
				throw new ConnectionException("Failure while getting db collection: " + e.getMessage(), e);

			}
		}
	}

	/**
	 * @throws ConnectionException
	 */
	protected void openDb() throws ConnectionException {
		if (db == null) {
			try {
				db = new DatabaseImpl();
				db.setProperty("create-database", "true");
				db.setProperty("configuration", getPath());
				db.setProperty("database-id", getUriName());
				DatabaseManager.registerDatabase(db);
			} catch (Exception e) {
				db = null;
				throw new ConnectionException("Failure while setting up db: " + e.getMessage(), e);
			}
		}
	}

	@Override
	public void close() throws ConnectionException {
		closeRoot();
		closeDb();
		// close it from the box
		deregisterConnection();
	}

	/**
	 * @throws ConnectionException
	 */
	protected void closeDb() throws ConnectionException {
		if (db != null) {
			try {
				DatabaseManager.deregisterDatabase(db);
			} catch (XMLDBException e) {
				throw new ConnectionException("Failure while shutting down db: " + e.getMessage(), e);
			} finally {
				db = null;
			}
		}
	}

	/**
	 * @throws ConnectionException
	 */
	protected void closeRoot() throws ConnectionException {
		if (isOpen()) {
			try {
				DatabaseInstanceManager manager = (DatabaseInstanceManager) root.getService("DatabaseInstanceManager", "1.0");
				if (root != null) {
					root.close();
				}
				manager.shutdown();
			} catch (XMLDBException e1) {
				throw new ConnectionException("Failure while shutting down db: " + e1.getMessage(), e1);
			} finally {
				root = null;
			}
		}
	}

	protected void setDb(Database db) {
		this.db = db;
	}

	@Override
	public Collection getRoot() {
		return root;
	}

	@Override
	public String getUri() {
		return "xmldb:" + getUriName() + "://";
	}

	@Override
	public boolean isOpen() {
		if (db == null && root == null) {
			return false;
		} else {
			try {
				root.getChildCollectionCount();
				return true;
			} catch (Exception e) {
				// ignored
				return false;
			}
		}
	}

	@Override
	public IConnection duplicate() throws ConnectionException {
		LocalConnectionWrapper wrapper = new LocalConnectionWrapper(getName(), getUsername(),getPassword(),getPath(), uriName, db);
		wrapper.open();
		return wrapper;
	}

	// //////////////////////////////////////////////////////////////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	private String getRootUri() {
		return getUri() + "/db";
	}
}
