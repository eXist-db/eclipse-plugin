package org.exist.eclipse;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.internal.BasePlugin;

/**
 * @author Patrick Reinhart
 */
public final class DatabaseInstanceLookup {
	private static Comparator<String> versionComparator =   (o1, o2) -> 
		DatabaseVersion.valueOf(o2).compareTo(DatabaseVersion.valueOf(o1));

	static Map<String, IDatabaseInstance> providers() {
		TreeMap<String, IDatabaseInstance> providers = new TreeMap<>(versionComparator);
		for (IConfigurationElement element : Platform.getExtensionRegistry()
				.getConfigurationElementsFor(BasePlugin.getId(), "database")) {
			try {
				IDatabaseInstance provider = (IDatabaseInstance) element.createExecutableExtension("class");
				providers.putIfAbsent(provider.version(), provider);
			} catch (CoreException e) {
				BasePlugin.log(Status.ERROR, "Unable to get provider", e);
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
