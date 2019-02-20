package org.exist.eclipse;

import org.xmldb.api.base.XMLDBException;

/**
 * Represents a eXist database instance.
 *
 * @author Patrick Reinhart
 */
public interface IDatabaseInstance {

	String version();

	IConnection createLocalConnection(String name, String username, String password, String path);

	IConnection createRemoteConnection(String name, String username, String password, String path);

	void backup(ICredentials credentials, String location, String uri) throws XMLDBException;

	void restore(ICredentials credentials, String location, String uri) throws XMLDBException;
}
