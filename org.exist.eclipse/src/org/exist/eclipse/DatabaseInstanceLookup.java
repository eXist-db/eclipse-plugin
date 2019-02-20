package org.exist.eclipse;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.internal.BasePlugin;

/**
 * @author Patrick Reinhart
 */
public final class DatabaseInstanceLookup {

	static Map<String, IDatabaseInstance> providers() {
		TreeMap<String, IDatabaseInstance> providers = new TreeMap<>();
		String id = BasePlugin.getId();
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		for (IConfigurationElement element : extensionRegistry.getConfigurationElementsFor(id, "database")) {
			try {
				IDatabaseInstance provider = (IDatabaseInstance) element.createExecutableExtension("class");
				providers.putIfAbsent(provider.version(), provider);
			} catch (CoreException e) {
				BasePlugin.getDefault().getLog().log(new Status(Status.ERROR, id, "Unable to get provider", e));
			}
		}
		return providers;
	}

	private DatabaseInstanceLookup() {
	}

	public static IDatabaseInstance getInstance(String existVersion) {
		IDatabaseInstance instance = providers().get(existVersion);
		if (instance == null) {
			throw new IllegalArgumentException("No database instance found for version: " + existVersion);
		}
		return instance;
	}

	public static IConnection createLocal(String existVersion, String name, String user, String password, String path) {
		return getInstance(existVersion).createLocalConnection(name, user, password, path);
	}

	public static IConnection createRemote(String existVersion, String name, String user, String password,
			String path) {
		return getInstance(existVersion).createRemoteConnection(name, user, password, path);
	}

	public static Set<String> availableVersions() {
		return Collections.unmodifiableSet(providers().keySet());
	}

	public static String defaultVersion() {
		return availableVersions().stream().findFirst().orElse("unkown");
	}
}
