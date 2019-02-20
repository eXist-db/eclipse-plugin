/**
 * File Name: AbstractConnection.java
 * 
 * Copyright (c) 2014 BISON Schweiz AG, All Rights Reserved.
 */

package org.exist.eclipse.spi;

import org.exist.eclipse.IConnection;
import org.exist.eclipse.internal.ConnectionBox;

/**
 * Base implementation of a connecton.
 *
 * @author Patrick Reinhart
 */
public abstract class AbstractConnection implements IConnection {
	private final String version;

	protected AbstractConnection(String version) {
		this.version = version;
	}

	/**
	 * register the current connection to the current open connections.
	 */
	protected final void registerConnection() {
		// open it from the box
		ConnectionBox.getInstance().openConnection(this);
	}

	/**
	 * unregister the current connection from the current open connections.
	 */
	protected final void deregisterConnection() {
		ConnectionBox.getInstance().closeConnection(this);
	}

	@Override
	public final String getVersion() {
		return version;
	}
}
