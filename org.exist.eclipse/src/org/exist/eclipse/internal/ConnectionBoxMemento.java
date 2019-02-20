/**
 * ConnectionBoxMemento.java
 */
package org.exist.eclipse.internal;

import static org.exist.eclipse.internal.ConnectionLookup.createLocal;
import static org.exist.eclipse.internal.ConnectionLookup.createRemote;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.exist.eclipse.ConnectionEnum;
import org.exist.eclipse.IConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Memento implementation for {@link ConnectionBox}.
 * 
 * @author Pascal Schmidiger
 */
public class ConnectionBoxMemento implements Serializable {
	private static final String URI = "uri";
	private static final String CONNECTIONS = "connections";
	private static final String CONNECTION = "connection";
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String NAME = "name";
	private static final String TYPE = "type";
	private static final String VERSION = "version";
	private static final long serialVersionUID = 7451211731519576573L;
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
			Element connections = (Element) doc.getDocumentElement().getFirstChild();
			NodeList childNodes = connections.getElementsByTagName(CONNECTION);
			for (int index = 0, total = childNodes.getLength(); index < total; index++) {
				Element connection = (Element) childNodes.item(index);
				String type = connection.getAttribute(TYPE);
				String name = connection.getAttribute(NAME);
				String username = connection.getAttribute(USERNAME);
				String password = connection.getAttribute(PASSWORD);
				String uri = connection.getAttribute(URI);
				String version = connection.getAttribute(VERSION);
				if (version == null) {
					version = "1.4.1";
				}
				if (type != null && name != null && username != null && password != null && uri != null) {
					switch (ConnectionEnum.valueOfName(type)) {
					case REMOTE:
						_connections.add(createRemote(version, name, username, password, uri));
						break;
					case LOCAL:
						_connections.add(createLocal(version, name, username, password, uri));
						break;
					}
				}
			}
		} catch (Exception e) {
			// do nothing
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
			Element connections = (Element) doc.getDocumentElement().appendChild(doc.createElement(CONNECTIONS));
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
		}
	}
}
