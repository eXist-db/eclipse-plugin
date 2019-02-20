/**
 * 
 */
package org.exist.eclipse.xquery.ui.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

/**
 * Singelton instance for registration the {@link IContextSwitcher} instances.
 * 
 * @author Pascal Schmidiger
 */
public class ContextSwitcherRegistration {
	private static ContextSwitcherRegistration _instance;
	private final Collection<IContextSwitcher> _switchers;

	private ContextSwitcherRegistration() {
		_switchers = new ArrayList<>();
	}

	/**
	 * @return the singleton instance.
	 */
	public synchronized static ContextSwitcherRegistration getInstance() {
		if (_instance == null) {
			_instance = new ContextSwitcherRegistration();
		}
		return _instance;
	}

	/**
	 * Add the given <code>switcher</code> to the list.
	 * 
	 * @param switcher {@link IContextSwitcher} to be registered; not null
	 */
	public void addContextSwitcher(IContextSwitcher switcher) {
		Assert.isNotNull(switcher);
		_switchers.add(switcher);
	}

	/**
	 * Remove the given <code>switcher</code> from the list.
	 * 
	 * @param switcher the registered {@link IContextSwitcher}; not null
	 */
	public void removeContextSwitcher(IContextSwitcher switcher) {
		Assert.isNotNull(switcher);
		_switchers.remove(switcher);
	}

	public final Collection<IContextSwitcher> getContextSwitchers() {
		return _switchers;
	}

	/**
	 * @return all {@link IContextSwitcher} as key=
	 *         {@link IContextSwitcher#getName()}; value= {@link IContextSwitcher}
	 */
	public final Map<String, IContextSwitcher> getContextSwitchersAsMap() {
		Map<String, IContextSwitcher> result = new HashMap<>(_switchers.size());
		synchronized (_switchers) {
			for (IContextSwitcher contextSwitcher : _switchers) {
				result.put(contextSwitcher.getName(), contextSwitcher);
			}

		}
		return result;
	}
}
