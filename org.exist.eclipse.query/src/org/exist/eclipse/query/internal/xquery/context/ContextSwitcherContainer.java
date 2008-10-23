package org.exist.eclipse.query.internal.xquery.context;

import java.util.HashMap;
import java.util.Map;

import org.exist.eclipse.IConnection;

/**
 * Container which hold all {@link ConnectionContext} to the
 * {@link IConnection} objects.
 * 
 * @author Pascal Schmidiger
 */
public class ContextSwitcherContainer {
	private static ContextSwitcherContainer _instance;

	private Map<IConnection, ContextSwitcher> _switchers;

	public static ContextSwitcherContainer getInstance() {
		if (_instance == null) {
			_instance = new ContextSwitcherContainer();
		}

		return _instance;
	}

	private ContextSwitcherContainer() {
		_switchers = new HashMap<IConnection, ContextSwitcher>();
	}

	/**
	 * Store the given <code>switcher</code> for given key=
	 * <code>connection</code>.
	 * 
	 * @param connection
	 * @param switcher
	 */
	public void put(IConnection connection, ContextSwitcher switcher) {
		_switchers.put(connection, switcher);
	}

	/**
	 * Remove the {@link ConnectionContext} for the given
	 * <code>connection</code>.
	 * 
	 * @param connection
	 */
	public void remove(IConnection connection) {
		_switchers.remove(connection);
	}

	/**
	 * @param connection
	 * @return the {@link ConnectionContext} for the given
	 *         <code>connection</code>.
	 */
	public ContextSwitcher getContextSwitcher(IConnection connection) {
		return _switchers.get(connection);
	}
}
