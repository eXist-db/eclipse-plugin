/**
 * File Name: DefaultProvider.java
 * 
 * Copyright (c) 2019 BISON Schweiz AG, All Rights Reserved.
 */

package org.exist.eclipse.internal;

import org.exist.eclipse.IConnection;
import org.exist.eclipse.IDatabaseInstance;

enum DefaultProvider implements IDatabaseInstance {
	INSTANCE;

	@Override
	public String version() {
		return null;
	}

	@Override
	public IConnection createLocalConnection(String name, String username, String password, String path) {
		return null;
	}

	@Override
	public IConnection createRemoteConnection(String name, String username, String password, String path) {
		return null;
	}
}