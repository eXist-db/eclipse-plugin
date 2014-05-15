package org.exist.eclipse.query.internal.proc;

import java.util.ArrayList;
import java.util.List;

/**
 * Here you can add and remove {@link IQueryResultListener} and notify all
 * registered listeners.
 * 
 * @see IQueryResultListener
 * @author Markus Tanner
 */
public class QueryResultNotifier implements IQueryResultListener {

	private static QueryResultNotifier _instance;
	private List<IQueryResultListener> _listener;

	public static QueryResultNotifier getInstance() {
		if (_instance == null) {
			_instance = new QueryResultNotifier();

		}
		return _instance;
	}

	private QueryResultNotifier() {
		_listener = new ArrayList<>();
	}

	/**
	 * Add a listener.
	 * 
	 * @param listener
	 */
	public void addListener(IQueryResultListener listener) {
		if (listener != null) {
			_listener.add(listener);
		}
	}

	/**
	 * Remove a listener.
	 * 
	 * @param listener
	 */
	public void removeListener(IQueryResultListener listener) {
		if (listener != null) {
			_listener.remove(listener);
		}
	}

	/**
	 * Updates the query result content.
	 * 
	 * @param event
	 */
	@Override
	public void addResult(final QueryResultEvent event) {
		for (IQueryResultListener listener : _listener) {
			listener.addResult(event);
		}
	}

}
