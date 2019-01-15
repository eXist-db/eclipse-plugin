/**
 * File Name: AbstractConnection.java
 * 
 * Copyright (c) 2014 BISON Schweiz AG, All Rights Reserved.
 */

package org.exist.eclipse.internal;

import org.exist.eclipse.IConnection;
import org.xmldb.api.base.Collection;

/**
 * TD2:patrick.reinhart Auto-generated comment for class
 *
 * @author Patrick Reinhart
 */
public abstract class AbstractConnection implements IConnection {

	@Override
	public Collection getCollection(String path) {
		return null;
	}

	@Override
	public abstract Collection getRoot();
}
