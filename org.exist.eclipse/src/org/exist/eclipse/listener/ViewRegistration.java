/**
 * ViewRegistration.java
 */
package org.exist.eclipse.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Register the interface {@link IViewListener}.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ViewRegistration {
	private static ViewRegistration _instance;
	private Collection<IViewListener> _listeners;

	public static ViewRegistration getInstance() {
		if (_instance == null) {
			_instance = new ViewRegistration();
		}
		return _instance;
	}

	private ViewRegistration() {
		_listeners = new ArrayList<IViewListener>();
	}

	public void addListener(IViewListener listener) {
		if (listener != null) {
			_listeners.add(listener);
		}
	}

	public void removeListener(IViewListener listener) {
		if (listener != null) {
			_listeners.remove(listener);
		}
	}

	public Iterator<IViewListener> getListeners() {
		return _listeners.iterator();
	}
}
