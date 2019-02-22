/**
 * ManagementService.java
 */
package org.exist.eclipse.spi;

import java.util.StringTokenizer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.internal.BasePlugin;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

/**
 * Implementation of {@link IManagementService}.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ManagementService implements IManagementService {
	private final IConnection _connection;

	public ManagementService(IConnection connection) {
		_connection = connection;
	}

	@Override
	public boolean check() {
		boolean result = _connection.isOpen();
		if (!result) {
			String message = "The connection is not open.";
			IStatus status = new Status(IStatus.ERROR, BasePlugin.getId(), message);
			BasePlugin.getDefault().getLog().log(status);
			BasePlugin.getDefault().errorDialog(message, message, status);
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						_connection.close();
					} catch (ConnectionException e) {
						IStatus errorStatus = new Status(IStatus.ERROR, BasePlugin.getId(),
								"Error while closing the connection", e);
						BasePlugin.getDefault().getLog().log(errorStatus);
					}
				}
			});
			result = false;
		}
		return result;
	}

	@Override
	public Collection createCollection(Collection collection, String name) throws ConnectionException {
		try {
			CollectionManagementService mgtService = (CollectionManagementService) collection
					.getService("CollectionManagementService", "1.0");
			return mgtService.createCollection(name);
		} catch (XMLDBException e) {
			throw new ConnectionException("Create collection failed.", e);
		}
	}

	@Override
	public void removeCollection(Collection collection) throws ConnectionException {
		if (collection != null) {
			try {
				if (collection.getName().equals(_connection.getRoot().getName())) {
					throw new ConnectionException("Could not delete root collection.");
				}
				CollectionManagementService mgtService = (CollectionManagementService) collection.getParentCollection()
						.getService("CollectionManagementService", "1.0");
				mgtService.removeCollection(collection.getName());
			} catch (XMLDBException e) {
				throw new ConnectionException("Remove collection failed.", e);
			}
		}
	}

	@Override
	public void removeDocument(Collection collection, String document) throws ConnectionException {
		if (collection != null) {
			try {
				Resource resource = collection.getResource(document);
				if (resource != null) {
					collection.removeResource(resource);
				}
			} catch (XMLDBException e) {
				throw new ConnectionException("Remove collection failed.", e);
			}
		}
	}

	@Override
	public Collection getCollection(String path) throws ConnectionException {
		try {
			Collection collection = _connection.getRoot();
			if (collection.getName().equals(path)) {
				return _connection.getRoot();
			} else {
				// remove '/db/' from path
				path = path.substring(4);
				StringTokenizer tokens = new StringTokenizer(path, "/");
				while (tokens.hasMoreTokens()) {
					collection = collection.getChildCollection(tokens.nextToken());
					if (collection == null) {
						// if collection is null then leave the while loop
						break;
					}
				}
				return collection;
			}
		} catch (XMLDBException e) {
			throw new ConnectionException("Read of collection '" + path + "' failed", e);
		}
	}

	@Override
	public void rename(String fromPath, String toPath) throws ConnectionException {
		CollectionManagementService service;
		try {
			service = (CollectionManagementService) _connection.getRoot().getService("CollectionManagementService",
					"1.0");
			service.move(fromPath, null, toPath);
		} catch (Exception e) {
			throw new ConnectionException("Rename of collection '" + fromPath + "' failed", e);
		}
	}

	@Override
	public void move(String fromPath, String toPath) throws ConnectionException {
		CollectionManagementService service;
		try {
			service = (CollectionManagementService) _connection.getRoot().getService("CollectionManagementService",
					"1.0");
			service.move(fromPath, toPath, null);
		} catch (Exception e) {
			throw new ConnectionException("Move of collection '" + fromPath + "' failed", e);
		}
	}

	@Override
	public void renameResource(Collection collection, String fromName, String toName) throws ConnectionException {
		CollectionManagementService service;
		try {
			service = (CollectionManagementService) collection.getService("CollectionManagementService", "1.0");
			service.moveResource(fromName, null, toName);
		} catch (Exception e) {
			throw new ConnectionException("Rename of document '" + fromName + "' failed", e);
		}
	}
	// //////////////////////////////////////////////////////////////////////////
	// ///////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	// ///////////////////
}
