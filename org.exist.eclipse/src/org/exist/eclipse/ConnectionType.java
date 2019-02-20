/**
 * ConnectionType.java
 */
package org.exist.eclipse;

/**
 * Enum of the connection types.
 * 
 * @author Pascal Schmidiger
 * 
 */
public enum ConnectionType {
	REMOTE("Remote"), LOCAL("Local");

	private final String description;

	private ConnectionType(String description) {
		this.description = description;
	}

	public boolean isRemote() {
		return REMOTE == this;
	}

	public static ConnectionType valueOfName(String name) {
		return valueOf(name.toUpperCase());
	}

	public String description() {
		return description;
	}
}
