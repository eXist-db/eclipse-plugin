/**
 * CreateTestdata.java
 */
package org.exist.eclipse;

import org.exist.eclipse.exist142.RemoteConnection;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

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
		try (IConnection conn = new RemoteConnection("localhost", "xmldb:exist://localhost:8080/exist/xmlrpc", "admin",
				"")) {
			conn.open();
			// create collections
			createCollections(conn);
			// create documents
			createDocuments(conn);
		}
	}

	private static void createDocuments(IConnection conn) throws XMLDBException {
		String colName = "largeDocs20000";
		String name = "doc";
		CollectionManagementService mgmt = (CollectionManagementService) conn.getRoot()
				.getService("CollectionManagementService", "1.0");
		Collection createCollection = mgmt.createCollection(colName);
		for (int i = 0; i < 2000; i++) {
			String resName = name + i + ".xml";
			Resource createResource = createCollection.createResource(resName, "XMLResource");
			createResource.setContent("<template/>");
			createCollection.storeResource(createResource);
			System.out.println("Create ressource " + resName);
		}
	}

	private static void createCollections(IConnection conn) throws XMLDBException {
		String name = "test";
		CollectionManagementService mgmt = (CollectionManagementService) conn.getRoot()
				.getService("CollectionManagementService", "1.0");
		for (int i = 0; i < 100; i++) {
			mgmt.createCollection(name + i);
			System.out.println("Create collection " + name + i);
		}
	}

}
