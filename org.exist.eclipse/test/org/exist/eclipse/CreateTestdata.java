/**
 * CreateTestdata.java
 */
package org.exist.eclipse;

import org.exist.eclipse.internal.RemoteConnection;
import org.exist.xmldb.CollectionManagementServiceImpl;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

/**
 * @author Pascal Schmidiger
 * 
 */
public class CreateTestdata {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		IConnection conn = new RemoteConnection("localhost",
				"xmldb:exist://localhost:8080/exist/xmlrpc", "admin", "");
		try {
			conn.open();

			// create collections
			createCollections(conn);

			// create documents
			createDocuments(conn);

		} finally {
			conn.close();
		}
	}

	@SuppressWarnings("deprecation")
	private static void createDocuments(IConnection conn) throws XMLDBException {
		String colName = "largeDocs20000";
		String name = "doc";
		CollectionManagementServiceImpl mgmt = (CollectionManagementServiceImpl) conn
				.getRoot().getService("CollectionManagementService", "1.0");
		Collection createCollection = mgmt.createCollection(colName);

		for (int i = 0; i < 2000; i++) {
			String resName = name + i + ".xml";
			Resource createResource = createCollection.createResource(resName,
					"XMLResource");
			createResource.setContent("<template/>");
			createCollection.storeResource(createResource);
			System.out.println("Create ressource " + resName);
		}
	}

	@SuppressWarnings("deprecation")
	private static void createCollections(IConnection conn)
			throws XMLDBException {
		String name = "test";
		CollectionManagementServiceImpl mgmt = (CollectionManagementServiceImpl) conn
				.getRoot().getService("CollectionManagementService", "1.0");
		for (int i = 0; i < 100; i++) {
			mgmt.createCollection(name + i);
			System.out.println("Create collection " + name + i);
		}

	}

}
