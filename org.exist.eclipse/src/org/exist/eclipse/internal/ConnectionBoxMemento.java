/**
 * ConnectionBoxMemento.java
 */
package org.exist.eclipse.internal;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.exist.eclipse.IConnection;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

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
	private static final long serialVersionUID = 7451211731519576573L;
	private final Collection<IConnection> _connections;

	ConnectionBoxMemento(Collection<IConnection> connections) {
		_connections = connections;
	}

	/**
	 * Create a memento with the given <code>filepath</code>. The file must be
	 * the connection xml.
	 * 
	 * @param filepath
	 */
	public ConnectionBoxMemento(String filepath) {
		_connections = new ArrayList<IConnection>();
		Document doc;
		try {
			doc = new SAXBuilder().build(filepath);
			Element connections = doc.getRootElement();
			Iterator<?> it = connections.getChildren(CONNECTION).iterator();
			while (it.hasNext()) {
				Element connection = (Element) it.next();
				try {
					String type = connection.getAttributeValue(TYPE);
					String name = connection.getAttributeValue(NAME);
					String username = connection.getAttributeValue(USERNAME);
					String password = connection.getAttributeValue(PASSWORD);
					String uri = connection.getAttributeValue(URI);
					if (type != null && name != null && username != null
							&& password != null && uri != null) {
						if (ConnectionEnum.valueOf(type).equals(
								ConnectionEnum.local)) {
							_connections.add(new LocalConnection(name,
									username, password, uri));
						} else if (ConnectionEnum.valueOf(type).equals(
								ConnectionEnum.remote)) {
							_connections.add(new RemoteConnection(name,
									username, password, uri));
						}
					}
				} catch (Exception e) {
					// do nothing, try the next element
				}
			}
		} catch (Exception e1) {
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
			Element connections = new Element(CONNECTIONS);
			connections.removeChildren(CONNECTION);
			for (IConnection connection : _connections) {
				Element element = new Element(CONNECTION);
				element.setAttribute(TYPE, connection.getType().name());
				element.setAttribute(NAME, connection.getName());
				element.setAttribute(USERNAME, connection.getUsername());
				element.setAttribute(PASSWORD, connection.getPassword());
				element.setAttribute(URI, connection.getPath());
				connections.addContent(element);
			}
			Document document = new Document(connections);
			XMLOutputter outp = new XMLOutputter();
			outp.output(document, new FileOutputStream(filepath));
		} catch (Exception e1) {
			// do nothing
		}
	}
}
