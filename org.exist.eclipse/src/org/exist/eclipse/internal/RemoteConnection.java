/**
 * RemoteConnection.java
 */
package org.exist.eclipse.internal;

import java.util.Objects;

import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

/**
 * With this class you can connect to a remote database. The database must run.
 * 
 * @author Pascal Schmidiger
 */
public class RemoteConnection extends AbstractConnection implements Cloneable {
	private final String _name;
	private final String _username;
	private final String _password;
	private final String _path;
	private Database _db;
	private Collection _root;

	/**
	 * Create a new connection. Post you have to call {@link #open()}.
	 * 
	 * @param name
	 *            of the connection, mandatory field.
	 * @param username
	 *            with which you connect to the database, mandatory field.
	 * @param password
	 *            of the username, optional field.
	 * @param path
	 *            the uri to the remote database, mandatory field.
	 */
	public RemoteConnection(String name, String username, String password,
			String path) {
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
		_path = path;
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
		return ConnectionEnum.remote;
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
	 * 
	 */
	protected void registerConnection() {
		// open it from the box
		ConnectionBox.getInstance().openConnection(this);
	}

	/**
	 * @throws ConnectionException
	 */
	protected void openDb() throws ConnectionException {
		if (_db == null) {
			try {
				String driver = "org.exist.xmldb.DatabaseImpl";
				Class<?> c1 = Class.forName(driver);
				_db = (Database) c1.newInstance();
				DatabaseManager.registerDatabase(_db);
			} catch (Exception e) {
				_db = null;
				throw new ConnectionException("Failure while setting up db: "
						+ e.getMessage(), e);
			}
		}
	}

	/**
	 * @throws ConnectionException
	 */
	protected void openRoot() throws ConnectionException {
		if (_root == null) {
			try {
				_root = DatabaseManager.getCollection(getRootUri(), _username,
						_password);

				// check whether a connection was established successfully.
				_root.getChildCollectionCount();
			} catch (Exception e) {
				_root = null;
				close();
				throw new ConnectionException(
						"Failure while getting db collection: "
								+ e.getMessage(), e);

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
		if (_db != null) {
			try {
				DatabaseManager.deregisterDatabase(_db);
			} catch (XMLDBException e) {
				throw new ConnectionException(
						"Failure while shutting down db: " + e.getMessage(), e);
			} finally {
				_db = null;
			}
		}
	}

	/**
	 * @throws ConnectionException
	 */
	protected void closeRoot() throws ConnectionException {
		if (isOpen()) {
			if (_root != null) {
				try {
					_root.close();
				} catch (XMLDBException e) {
					throw new ConnectionException(
							"Failure while shutting down db: " + e.getMessage(),
							e);
				} finally {
					_root = null;
				}
			}
		}
	}

	/**
	 * De-registers the connections instance from the connection box
	 */
	protected void deregisterConnection() {
		ConnectionBox.getInstance().closeConnection(this);
	}

	protected void setDb(Database db) {
		_db = db;
	}

	@Override
	public Collection getRoot() {
		return _root;
	}

	@Override
	public String getUri() {
		return getPath();
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
		RemoteConnectionWrapper wrapper = new RemoteConnectionWrapper(_name,
				_username, _password, _path, _db);
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
		} else if (!(obj instanceof RemoteConnection)) {
			return false;
		}
		final RemoteConnection other = (RemoteConnection) obj;
		return Objects.equals(_name, other._name);
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (IManagementService.class.equals(adapter)) {
			return new ManagementService(this);
		}
		return null;
	}

	// //////////////////////////////////////////////////////////////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	private String getRootUri() {
		return getUri() + "/db";
	}

}
