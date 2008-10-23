/**
 * ChangeItemCoordinator.java
 */
package org.exist.eclipse.query.internal.item;

import java.util.ArrayList;
import java.util.List;

import org.exist.eclipse.browse.browse.IBrowseItem;

/**
 * Here you can add and remove {@link IChangeItemListener} and notify all
 * registered listeners.
 * 
 * @see IChangeItemListener.
 * @author Pascal Schmidiger
 * 
 */
public class ChangeItemNotifier implements IChangeItemListener {
	private static ChangeItemNotifier _instance;
	private List<IChangeItemListener> _listeners;

	public static ChangeItemNotifier getInstance() {
		if (_instance == null) {
			_instance = new ChangeItemNotifier();
		}
		return _instance;
	}

	private ChangeItemNotifier() {
		_listeners = new ArrayList<IChangeItemListener>();
	}

	public void addListener(IChangeItemListener listener) {
		if (listener != null) {
			_listeners.add(listener);
		}
	}

	public void removeListener(IChangeItemListener listener) {
		if (listener != null) {
			_listeners.remove(listener);
		}
	}

	public void change(IBrowseItem item) {
		for (IChangeItemListener listener : _listeners) {
			listener.change(item);
		}
	}
}
