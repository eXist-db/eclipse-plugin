/**
 * BrowseCoordinator.java
 */
package org.exist.eclipse.browse.browse;

import java.util.ArrayList;
import java.util.List;

/**
 * Here you can add and remove {@link IBrowseItemListener} and notify all
 * registered listeners.
 * 
 * @see IBrowseItemListener
 * @author Pascal Schmidiger
 * 
 */
public class BrowseCoordinator implements IBrowseItemListener {
	private static BrowseCoordinator _instance;
	private List<IBrowseItemListener> _listeners;

	private BrowseCoordinator() {
		_listeners = new ArrayList<IBrowseItemListener>();
	}

	public static BrowseCoordinator getInstance() {
		if (_instance == null) {
			_instance = new BrowseCoordinator();
		}
		return _instance;
	}

	/**
	 * Add given <code>listener</code> to this item.
	 * 
	 * @param listener
	 */
	public void addListener(IBrowseItemListener listener) {
		if (listener != null) {
			_listeners.add(listener);
		}
	}

	/**
	 * Remove given <code>listener</code> to this item.
	 * 
	 * @param listener
	 */
	public void removeListener(IBrowseItemListener listener) {
		if (listener != null) {
			_listeners.remove(listener);
		}
	}

	public void added(IBrowseItem item) {
		for (IBrowseItemListener listener : _listeners) {
			listener.added(item);
		}
	}

	public void refresh(IBrowseItem item) {
		for (IBrowseItemListener listener : _listeners) {
			listener.refresh(item);
		}
	}

	public void removed(IBrowseItem[] items) {
		for (IBrowseItemListener listener : _listeners) {
			listener.removed(items);
		}
	}

	public void moved(IBrowseItem fromItem, IBrowseItem toItem) {
		for (IBrowseItemListener listener : _listeners) {
			listener.moved(fromItem, toItem);
		}
	}
}
