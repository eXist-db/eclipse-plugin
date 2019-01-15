package org.exist.eclipse.query.internal.proc;

import java.util.ArrayList;
import java.util.List;

/**
 * Here you can add and remove {@link IQueryListener} and notify all registered
 * listeners.
 * 
 * @see IQueryListener
 * @author Markus Tanner
 */
public class QueryNotifier implements IQueryListener {

	private static QueryNotifier _instance;
	private List<IQueryListener> _listener;

	public static QueryNotifier getInstance() {
		if (_instance == null) {
			_instance = new QueryNotifier();

		}
		return _instance;
	}

	private QueryNotifier() {
		_listener = new ArrayList<>();
	}

	/**
	 * Add a listener.
	 * 
	 * @param listener
	 */
	public void addListener(IQueryListener listener) {
		if (listener != null) {
			_listener.add(listener);
		}
	}

	/**
	 * Remove a listener.
	 * 
	 * @param listener
	 */
	public void removeListener(IQueryListener listener) {
		if (listener != null) {
			_listener.remove(listener);
		}
	}

	@Override
	public void end(final QueryEndEvent event) {
		for (IQueryListener listener : _listener) {
			listener.end(event);
		}
	}

	@Override
	public void start(final QueryStartEvent event) {
		for (IQueryListener listener : _listener) {
			listener.start(event);
		}
	}

}
