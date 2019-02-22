/**
 * ConnectionBox.java
 */
package org.exist.eclipse.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.IConnectionBox;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.listener.IConnectionListener;

/**
 * This box manages all active connections and inform all
 * {@link IConnectionListener}.
 * 
 * @author Pascal Schmidiger
 */
public final class ConnectionBox implements IConnectionBox {
	private static ConnectionBox _instance;
	private final Collection<IConnection> _connections;
	private final Collection<String> _uniqueNames;
	private final Collection<IConnectionListener> _listeners;

	private ConnectionBox() {
		// singleton
		_listeners = new ArrayList<>();
		_connections = new ArrayList<>();
		_uniqueNames = new ArrayList<>();
	}

	public static ConnectionBox getInstance() {
		if (_instance == null) {
			_instance = new ConnectionBox();
		}
		return _instance;
	}

	public void addConnection(IConnection connection) {
		if (connection != null && !_connections.contains(connection)) {
			_connections.add(connection);
			_uniqueNames.add(connection.getName());
			fireAdded(connection);
			if (connection.isOpen()) {
				fireOpened(connection);
			}
		}
	}

	@Override
	public void removeConnection(IConnection connection) {
		if (connection != null && _connections.contains(connection)) {
			if (connection.isOpen()) {
				try {
					connection.close();
				} catch (ConnectionException e) {
					StringBuilder message = new StringBuilder(50).append("Error while close connection.");
					IStatus status = new Status(IStatus.ERROR, BasePlugin.getId(), message.toString(), e);
					BasePlugin.getDefault().getLog().log(status);
				}
			}
			_connections.remove(connection);
			_uniqueNames.remove(connection.getName());
			fireRemoved(connection);
		}
	}

	public void openConnection(IConnection connection) {
		if (connection != null && _connections.contains(connection)) {
			fireOpened(connection);
		}
	}

	public void closeConnection(IConnection connection) {
		if (connection != null && _connections.contains(connection)) {
			fireClosed(connection);
		}
	}

	@Override
	public final Collection<IConnection> getConnections() {
		return _connections;
	}

	public final boolean isUnique(String name) {
		return !_uniqueNames.contains(name);
	}

	@Override
	public void addListener(IConnectionListener listener) {
		if (listener != null) {
			_listeners.add(listener);
			synchronized (_connections) {
				for (IConnection connection : _connections) {
					listener.added(connection);
					if (connection.isOpen()) {
						listener.opened(connection);
					}
				}
			}
		}
	}

	@Override
	public void removeListener(IConnectionListener listener) {
		if (listener != null) {
			_listeners.remove(listener);
		}
	}

	public ConnectionBoxMemento getMemento() {
		return new ConnectionBoxMemento(_connections);
	}

	public void setMemento(ConnectionBoxMemento memento) {
		for (IConnection connection : memento.getConnections()) {
			addConnection(connection);
		}
	}

	//
	// private methods
	//
	private void fireAdded(IConnection connection) {
		synchronized (_listeners) {
			for (IConnectionListener listener : _listeners) {
				try {
					listener.added(connection);
				} catch (Exception e) {
					StringBuilder message = new StringBuilder(50).append("Error while added event.");
					IStatus status = new Status(IStatus.ERROR, BasePlugin.getId(), message.toString(), e);
					BasePlugin.getDefault().getLog().log(status);
				}
			}
		}
	}

	private void fireRemoved(IConnection connection) {
		synchronized (_listeners) {
			for (IConnectionListener listener : _listeners) {
				try {
					listener.removed(connection);
				} catch (Exception e) {
					StringBuilder message = new StringBuilder(50).append("Error while removed event.");
					IStatus status = new Status(IStatus.ERROR, BasePlugin.getId(), message.toString(), e);
					BasePlugin.getDefault().getLog().log(status);
				}
			}
		}
	}

	private void fireOpened(IConnection connection) {
		synchronized (_listeners) {
			for (IConnectionListener listener : _listeners) {
				try {
					listener.opened(connection);
				} catch (Exception e) {
					StringBuilder message = new StringBuilder(50).append("Error while opened event.");
					IStatus status = new Status(IStatus.ERROR, BasePlugin.getId(), message.toString(), e);
					BasePlugin.getDefault().getLog().log(status);
				}
			}
		}
	}

	private void fireClosed(IConnection connection) {
		synchronized (_listeners) {
			for (IConnectionListener listener : _listeners) {
				try {
					listener.closed(connection);
				} catch (Exception e) {
					StringBuilder message = new StringBuilder(50).append("Error while closed event.");
					IStatus status = new Status(IStatus.ERROR, BasePlugin.getId(), message.toString(), e);
					BasePlugin.getDefault().getLog().log(status);
				}
			}
		}
	}
}
