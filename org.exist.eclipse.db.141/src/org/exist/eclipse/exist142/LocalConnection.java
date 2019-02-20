/**
 * LocalConnection.java
 */
package org.exist.eclipse.exist142;

import java.io.File;
import java.util.Objects;

import org.exist.eclipse.IConnection;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.internal.ConnectionBox;
import org.exist.eclipse.internal.ConnectionEnum;
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

	private final String _name;
	private final String _username;
	private final String _password;
	private final String _path;
	private final String _uriName;
	private Database _db;
	private Collection _root;

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
		if (name == null || name.length() < 1) {
			throw new IllegalArgumentException("name must be set.");
		}
		if (username == null || username.length() < 1) {
			throw new IllegalArgumentException("username must be set.");
		}
		if (path == null || path.length() < 1) {
			throw new IllegalArgumentException("path must be set.");
		}
		_name = name;
		_username = username;
		_password = password;
		_path = new File(path).getAbsolutePath();
		_uriName = uri;
	}

	private static synchronized String getNextUri() {
		return "local" + _counter++;
	}

	@Override
	public final String getName() {
		return _name;
	}

	@Override
	public String getPassword() {
		return _password;
	}

	@Override
	public String getPath() {
		return _path;
	}

	@Override
	public ConnectionEnum getType() {
		return ConnectionEnum.local;
	}

	@Override
	public String getUsername() {
		return _username;
	}

	@Override
	public void open() throws ConnectionException {
		openDb();
		openRoot();
		registerConnection();
	}

	/**
	 * Registers the connection in the connection box
	 */
	protected void registerConnection() {
		ConnectionBox.getInstance().openConnection(this);
	}

	/**
	 * @throws ConnectionException
	 */
	protected void openRoot() throws ConnectionException {
		if (_root == null) {
			try {
				_root = DatabaseManager.getCollection(getRootUri(), _username, _password);
				// check whether a connection was established successfully.
				_root.getChildCollectionCount();
			} catch (Exception e) {
				_root = null;
				close();
				throw new ConnectionException("Failure while getting db collection: " + e.getMessage(), e);

			}
		}
	}

	/**
	 * @throws ConnectionException
	 */
	protected void openDb() throws ConnectionException {
		if (_db == null) {
			try {
				_db = new DatabaseImpl();
				_db.setProperty("create-database", "true");
				_db.setProperty("configuration", getPath());
				_db.setProperty("database-id", getUriName());
				DatabaseManager.registerDatabase(_db);
			} catch (Exception e) {
				_db = null;
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
	 * De-registers the connections instance from the connection box
	 */
	protected void deregisterConnection() {
		ConnectionBox.getInstance().closeConnection(this);
	}

	/**
	 * @throws XMLDBException
	 */
	protected void closeDb() throws ConnectionException {
		if (_db != null) {
			try {
				DatabaseManager.deregisterDatabase(_db);
			} catch (XMLDBException e) {
				throw new ConnectionException("Failure while shutting down db: " + e.getMessage(), e);
			} finally {
				_db = null;
			}
		}
	}

	/**
	 * @throws XMLDBException
	 * @throws XMLDBException
	 */
	protected void closeRoot() throws ConnectionException {
		if (isOpen()) {
			DatabaseInstanceManager manager;
			try {
				manager = (DatabaseInstanceManager) _root.getService("DatabaseInstanceManager", "1.0");
				if (_root != null) {
					_root.close();
				}
				manager.shutdown();
			} catch (XMLDBException e1) {
				throw new ConnectionException("Failure while shutting down db: " + e1.getMessage(), e1);
			} finally {
				_root = null;
			}
		}
	}

	@Override
	public Collection getRoot() {
		return _root;
	}

	@Override
	public String getUri() {
		return "xmldb:" + getUriName() + "://";
	}

	@Override
	public boolean isOpen() {
		if (_db == null && _root == null) {
			return false;
		} else {
			try {
				_root.getChildCollectionCount();
				return true;
			} catch (Exception e) {
				// ignored
				return false;
			}
		}
	}

	@Override
	public IConnection duplicate() throws ConnectionException {
		LocalConnectionWrapper wrapper = new LocalConnectionWrapper(_name, _username, _password, _path, _uriName, _db);
		wrapper.open();
		return wrapper;
	}

	@Override
	public String toString() {
		return getName() + " (" + getPath() + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof LocalConnection)) {
			return false;
		}
		final LocalConnection other = (LocalConnection) obj;
		return Objects.equals(_name, other._name);
	}

	protected void setDb(Database db) {
		_db = db;
	}

	// //////////////////////////////////////////////////////////////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	private String getRootUri() {
		return getUri() + "/db";
	}

	private String getUriName() {
		return _uriName;
	}
}
