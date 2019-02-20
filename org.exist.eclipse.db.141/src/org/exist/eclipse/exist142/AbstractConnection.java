/**
 * File Name: AbstractConnection.java
 * 
 * Copyright (c) 2014 BISON Schweiz AG, All Rights Reserved.
 */

package org.exist.eclipse.exist142;

import org.exist.eclipse.IConnection;

/**
 * Base implementation of a conneciton
 *
 * @author Patrick Reinhart
 */
public abstract class AbstractConnection implements IConnection {

	@Override
	public final String getVersion() {
		return ExistConnectionProvider.VERSION;
	}
}
