/**
 * File Name: ExistConnectionProvider.java
 * 
 * Copyright (c) 2019 BISON Schweiz AG, All Rights Reserved.
 */

package org.exist.eclipse.db.v141;

import org.exist.eclipse.IConnection;
import org.exist.eclipse.IDatabaseInstance;

/**
 * @author Patrick Reinhart
 */
public class ExistConnectionProvider implements IDatabaseInstance {
	static final String VERSION = "1.4.1";

	@Override
	public String version() {
		return null;
	}

	@Override
	public IConnection createLocalConnection(String name, String username, String password, String path) {
		return new LocalConnection(name, username, password, path);
	}

	@Override
	public IConnection createRemoteConnection(String name, String username, String password, String path) {
		return new RemoteConnection(name, username, password, path);
	}

}
