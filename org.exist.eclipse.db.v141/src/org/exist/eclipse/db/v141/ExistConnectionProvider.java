package org.exist.eclipse.db.v141;

import static org.exist.xmldb.XmldbURI.xmldbUriFor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.exist.backup.Backup;
import org.exist.backup.Restore;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.ICredentials;
import org.exist.eclipse.IDatabaseInstance;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;

/**
 * @author Patrick Reinhart
 */
public class ExistConnectionProvider implements IDatabaseInstance {
	
	static String versionId() {
		return "1.4.1";
	}

	@Override
	public String version() {
		return versionId();
	}

	@Override
	public IConnection createLocalConnection(String name, String username, String password, String path) {
		return new LocalConnection(name, username, password, path);
	}

	@Override
	public IConnection createRemoteConnection(String name, String username, String password, String path) {
		return new RemoteConnection(name, username, password, path);
	}

	@Override
	public void backup(ICredentials credentials, String location, String uri)throws XMLDBException {
		try {
			Backup backup = new Backup(credentials.getUsername(), credentials.getPassword(), location, xmldbUriFor(uri));
			backup.backup(false, null);
		} catch (URISyntaxException | IOException | SAXException e) {
			throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e);
		}
	}

	@Override
	public void restore(ICredentials credentials, String location, String uri) throws XMLDBException{
		try {
			Restore restore = new Restore(credentials.getUsername(), credentials.getPassword(),
					null, new File(location), uri);
			restore.restore(false, null);
		} catch (ParserConfigurationException | SAXException | URISyntaxException | IOException e) {
			throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e);
		}
	}

}
