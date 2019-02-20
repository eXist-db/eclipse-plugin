/**
 * File Name: ConnectionLookup.java
 * 
 * Copyright (c) 2019 BISON Schweiz AG, All Rights Reserved.
 */

package org.exist.eclipse.internal;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.IDatabaseInstance;

/**
 * @author Patrick Reinhart
 */
public final class ConnectionLookup {

	private static final Map<String, IDatabaseInstance> providers;

	static {
		providers = new TreeMap<>();
		for (IConfigurationElement element : Platform.getExtensionRegistry()
				.getExtensionPoint(BasePlugin.getId(), "provider").getExtension("class").getConfigurationElements()) {
			try {
				IDatabaseInstance provider = (IDatabaseInstance) element.createExecutableExtension("provider");
				providers.putIfAbsent(provider.version(), provider);
			} catch (CoreException e) {
				BasePlugin.getDefault().getLog()
						.log(new Status(Status.ERROR, BasePlugin.getId(), "Unable to get provider", e));
			}
		}
	}

	private ConnectionLookup() {
	}

	public static IConnection createLocal(String existVersion, String name, String user, String password, String path) {
		return providers.getOrDefault(existVersion, DefaultProvider.INSTANCE).createLocalConnection(name, user,
				password, path);
	}

	public static IConnection createRemote(String existVersion, String name, String user, String password,
			String path) {
		return providers.getOrDefault(existVersion, DefaultProvider.INSTANCE).createRemoteConnection(name, user,
				password, path);
	}

	public static Set<String> availableVersions() {
		return Collections.unmodifiableSet(providers.keySet());
	}
}
