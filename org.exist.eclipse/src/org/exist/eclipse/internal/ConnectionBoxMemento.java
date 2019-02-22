/**
 * ConnectionBoxMemento.java
 */
package org.exist.eclipse.internal;

import static org.exist.eclipse.DatabaseInstanceLookup.createLocal;
import static org.exist.eclipse.DatabaseInstanceLookup.createRemote;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.Status;
import org.exist.eclipse.ConnectionType;
import org.exist.eclipse.IConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Memento implementation for {@link ConnectionBox}.
 * 
 * @author Pascal Schmidiger
 */
public class ConnectionBoxMemento {
	private static final String URI = "uri";
	private static final String CONNECTIONS = "connections";
	private static final String CONNECTION = "connection";
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String VERSION = "version";

	private final Collection<IConnection> _connections;

	ConnectionBoxMemento(Collection<IConnection> connections) {
		_connections = connections;
	}

	/**
	 * Create a memento with the given <code>filepath</code>. The file must be the
	 * connection xml.
	 * 
	 * @param filepath
	 */
	public ConnectionBoxMemento(String filepath) {
		_connections = new ArrayList<>();
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filepath);
			NodeList childNodes = doc.getDocumentElement().getElementsByTagName(CONNECTION);
			for (int index = 0, total = childNodes.getLength(); index < total; index++) {
				Element connection = (Element) childNodes.item(index);
				initConnection(connection.getAttribute(TYPE), connection.getAttribute(NAME),
						connection.getAttribute(USERNAME), connection.getAttribute(PASSWORD),
						connection.getAttribute(URI), connection.getAttribute(VERSION));
			}
		} catch (Exception e) {
			BasePlugin.log(Status.ERROR, "Unable to load configured connections", e);
		}
	}

	private void initConnection(String type, String name, String username, String password, String uri,
			String version) {
		boolean complete = true;
		if (version == null) {
			BasePlugin.log(Status.ERROR, "Version missing");
			complete = false;
		}
		if (type == null) {
			BasePlugin.log(Status.ERROR, "Type missing");
			complete = false;
		}
		if (name == null) {
			BasePlugin.log(Status.ERROR, "Name missing");
			complete = false;
		}
		if (username == null) {
			BasePlugin.log(Status.ERROR, "Username missing");
			complete = false;
		}
		if (password == null) {
			BasePlugin.log(Status.ERROR, "Password missing");
			complete = false;
		}
		if (complete) {
			switch (ConnectionType.valueOfName(type)) {
			case REMOTE:
				_connections.add(createRemote(version, name, username, password, uri));
				break;
			case LOCAL:
				_connections.add(createLocal(version, name, username, password, uri));
				break;
			}
		}
	}

	Collection<IConnection> getConnections() {
		return _connections;
	}

	/**
	 * Write the actual state into the given <code>filepath</code>.
	 * 
	 * @param filepath
	 */
	public void writeStateAsXml(String filepath) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element connections = (Element) doc.appendChild(doc.createElement(CONNECTIONS));
			for (IConnection connection : _connections) {
				Element connectionElement = doc.createElement(CONNECTION);
				connectionElement.setAttribute(TYPE, connection.getType().name());
				connectionElement.setAttribute(NAME, connection.getName());
				connectionElement.setAttribute(USERNAME, connection.getUsername());
				connectionElement.setAttribute(PASSWORD, connection.getPassword());
				connectionElement.setAttribute(URI, connection.getPath());
				connectionElement.setAttribute(VERSION, connection.getVersion());
				connections.appendChild(connectionElement);
			}
			try (FileOutputStream out = new FileOutputStream(filepath)) {
				TransformerFactory factory = TransformerFactory.newInstance();
				factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
				Transformer transformer = factory.newTransformer();
				transformer.transform(new DOMSource(doc), new StreamResult(out));
			}
		} catch (Exception e) {
			BasePlugin.log(Status.ERROR, "Unable to store configured connections", e);
		}
	}
}
