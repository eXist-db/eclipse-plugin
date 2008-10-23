/**
 * BrowseCoordinator.java
 */
package org.exist.eclipse.browse.document;

import java.util.ArrayList;
import java.util.List;

/**
 * Here you can add and remove {@link IDocumentItemListener} and notify all
 * registered listeners.
 * 
 * @see IDocumentItemListener
 * @author Pascal Schmidiger
 * 
 */
public class DocumentCoordinator implements IDocumentItemListener {
	private static DocumentCoordinator _instance;
	private List<IDocumentItemListener> _listeners;

	private DocumentCoordinator() {
		_listeners = new ArrayList<IDocumentItemListener>();
	}

	public static DocumentCoordinator getInstance() {
		if (_instance == null) {
			_instance = new DocumentCoordinator();
		}
		return _instance;
	}

	/**
	 * Add given <code>listener</code> to this item.
	 * 
	 * @param listener
	 */
	public void addListener(IDocumentItemListener listener) {
		if (listener != null) {
			_listeners.add(listener);
		}
	}

	/**
	 * Remove given <code>listener</code> to this item.
	 * 
	 * @param listener
	 */
	public void removeListener(IDocumentItemListener listener) {
		if (listener != null) {
			_listeners.remove(listener);
		}
	}

	public void removed(IDocumentItem item) {
		for (IDocumentItemListener listener : _listeners) {
			listener.removed(item);
		}
	}

	public void moved(IDocumentItem fromItem, IDocumentItem toItem) {
		for (IDocumentItemListener listener : _listeners) {
			listener.moved(fromItem, toItem);
		}
	}
}
