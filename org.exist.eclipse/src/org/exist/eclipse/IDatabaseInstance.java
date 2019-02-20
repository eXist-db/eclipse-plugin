/**
 * File Name: IDatabaseInstance.java
 * 
 * Copyright (c) 2019 BISON Schweiz AG, All Rights Reserved.
 */

package org.exist.eclipse;

/**
 * Represents a eXist database instance.
 *
 * @author Patrick Reinhart
 */
public interface IDatabaseInstance {

	String version();

	IConnection createLocalConnection(String name, String username, String password, String path);

	IConnection createRemoteConnection(String name, String username, String password, String path);

}
