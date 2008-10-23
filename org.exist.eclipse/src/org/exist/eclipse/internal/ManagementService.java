/**
 * ManagementService.java
 */
package org.exist.eclipse.internal;

import java.util.StringTokenizer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.xmldb.CollectionManagementServiceImpl;
import org.exist.xmldb.XmldbURI;
import org.exist.xquery.util.URIUtils;
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

	protected ManagementService(IConnection connection) {
		_connection = connection;
	}

	public boolean check() {
		boolean result = _connection.isOpen();
		if (!result) {
			String message = "The connection is not open.";
			IStatus status = new Status(Status.ERROR, BasePlugin.getId(),
					message);
			BasePlugin.getDefault().getLog().log(status);
			BasePlugin.getDefault().errorDialog(message, message, status);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					try {
						_connection.close();
					} catch (ConnectionException e) {
						IStatus status = new Status(Status.ERROR, BasePlugin
								.getId(), "Error while closing the connection",
								e);
						BasePlugin.getDefault().getLog().log(status);
					}
				}
			});
			result = false;
		}
		return result;
	}

	public Collection createCollection(Collection collection, String name)
			throws ConnectionException {
		try {
			CollectionManagementService mgtService = (CollectionManagementServiceImpl) collection
					.getService("CollectionManagementService", "1.0");
			return mgtService.createCollection(URIUtils.urlEncodeUtf8(name));
		} catch (XMLDBException e) {
			throw new ConnectionException("Create collection failed.", e);
		}
	}

	public void removeCollection(Collection collection)
			throws ConnectionException {
		if (collection != null) {
			try {
				if (collection.getName()
						.equals(_connection.getRoot().getName())) {
					throw new ConnectionException(
							"Could not delete root collection.");
				}
				CollectionManagementService mgtService = (CollectionManagementServiceImpl) collection
						.getParentCollection().getService(
								"CollectionManagementService", "1.0");
				mgtService.removeCollection(collection.getName());
			} catch (XMLDBException e) {
				throw new ConnectionException("Remove collection failed.", e);
			}
		}
	}

	public void removeDocument(Collection collection, String document)
			throws ConnectionException {
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
					collection = collection.getChildCollection(URIUtils
							.urlEncodeUtf8(tokens.nextToken()));
					if (collection == null) {
						// if collection is null then leave the while loop
						break;
					}
				}
				return collection;
			}
		} catch (XMLDBException e) {
			throw new ConnectionException("Read of collection '" + path
					+ "' failed", e);
		}
	}

	public void rename(String fromPath, String toPath)
			throws ConnectionException {
		CollectionManagementServiceImpl service;
		try {
			service = (CollectionManagementServiceImpl) _connection.getRoot()
					.getService("CollectionManagementService", "1.0");
			service.move(XmldbURI.xmldbUriFor(fromPath), null, XmldbURI
					.xmldbUriFor(toPath));
		} catch (Exception e) {
			throw new ConnectionException("Rename of collection '" + fromPath
					+ "' failed", e);
		}
	}

	public void move(String fromPath, String toPath) throws ConnectionException {
		CollectionManagementServiceImpl service;
		try {
			service = (CollectionManagementServiceImpl) _connection.getRoot()
					.getService("CollectionManagementService", "1.0");
			service.move(XmldbURI.xmldbUriFor(fromPath), XmldbURI
					.xmldbUriFor(toPath), null);
		} catch (Exception e) {
			throw new ConnectionException("Move of collection '" + fromPath
					+ "' failed", e);
		}
	}

	public void renameResource(Collection collection, String fromName,
			String toName) throws ConnectionException {
		CollectionManagementServiceImpl service;
		try {
			service = (CollectionManagementServiceImpl) collection.getService(
					"CollectionManagementService", "1.0");
			service.moveResource(XmldbURI.xmldbUriFor(fromName), null, XmldbURI
					.xmldbUriFor(toName));
		} catch (Exception e) {
			throw new ConnectionException("Rename of document '" + fromName
					+ "' failed", e);
		}
	}
	// //////////////////////////////////////////////////////////////////////////
	// ///////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	// ///////////////////
}
