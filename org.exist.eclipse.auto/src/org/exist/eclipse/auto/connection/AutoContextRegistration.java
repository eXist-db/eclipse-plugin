/**
 * 
 */
package org.exist.eclipse.auto.connection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * AutoContextRegistration handles the actual contexts.
 * 
 * @author Markus Tanner
 */
public final class AutoContextRegistration {

	public static AutoContextRegistration _instance;
	private final Collection<IAutoContext> _autoContexts;

	private AutoContextRegistration() {
		// singleton
		_autoContexts = new ArrayList<IAutoContext>();
	}

	public static AutoContextRegistration getInstance() {
		if (_instance == null) {
			_instance = new AutoContextRegistration();
		}
		return _instance;
	}

	/**
	 * Adds a an additional contexts
	 * 
	 * @param autoContext
	 */
	public void add(IAutoContext autoContext) {
		_autoContexts.add(autoContext);
	}

	/**
	 * Removes a context
	 * 
	 * @param autoContext
	 */
	public void remove(IAutoContext autoContext) {
		_autoContexts.remove(autoContext);
	}

	/**
	 * Provides all the existing contexts
	 * 
	 * @return existing contexts
	 */
	public Collection<IAutoContext> getAll() {
		return _autoContexts;
	}

}
