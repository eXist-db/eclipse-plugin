/**
 * File Name: AbstractConnection.java
 * 
 * Copyright (c) 2014 BISON Schweiz AG, All Rights Reserved.
 */

package org.exist.eclipse.spi;

import java.util.Objects;

import org.exist.eclipse.ConnectionType;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.internal.ConnectionBox;

/**
 * Base implementation of a connecton.
 *
 * @author Patrick Reinhart
 */
public abstract class AbstractConnection implements IConnection {
	private final String version;
	private final ConnectionType type;
	private final String name;
	private final String username;
	private final String password;
	private final String path;

	protected AbstractConnection(String version, ConnectionType type, String name, String username, String password,
			String path) {
		this.version = version;
		this.type = type;
		this.name = checkValue("name", name);
		this.username = checkValue("username", name);
		this.password = checkValue("password", name);
		this.path = checkValue("path", name);
	}

	private String checkValue(String attribute, String value) {
		if (Objects.requireNonNull(value, attribute + " must not be null").isEmpty()) {
			throw new IllegalArgumentException(attribute + " must be set.");
		}
		return value;
	}

	/**
	 * register the current connection to the current open connections.
	 */
	protected final void registerConnection() {
		ConnectionBox.getInstance().openConnection(this);
	}

	/**
	 * unregister the current connection from the current open connections.
	 */
	protected final void deregisterConnection() {
		ConnectionBox.getInstance().closeConnection(this);
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public final <A> A getAdapter(Class<A> adapter) {
		if (adapter.isAssignableFrom(ManagementService.class)) {
			return adapter.cast(new ManagementService(this));
		}
		return null;
	}

	@Override
	public final ConnectionType getType() {
		return type;
	}

	@Override
	public final String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", name, path);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof AbstractConnection)) {
			return false;
		}
		AbstractConnection other = (AbstractConnection)obj;
		return type.equals(other.type) && name.equals(other.name);
	}
}
