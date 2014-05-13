/**
 * ViewContentProvider.java
 */
package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.exist.eclipse.browse.internal.document.DocumentItem;

/**
 * The content provider class is responsible for providing objects to the view.
 * It can wrap existing objects in adapters or simply return objects as-is.
 * These objects may be sensitive to the current input of the view, or ignore it
 * and always show the same content (like Task List, for example).
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ViewContentProvider implements ILazyContentProvider {
	private final DocumentView _view;
	private String[] _elements;

	ViewContentProvider(DocumentView view) {
		_view = view;
	}

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		_elements = (String[]) newInput;
	}

	@Override
	public void updateElement(int index) {
		if (_view.getItem() != null) {
			_view.getViewer().replace(
					new DocumentItem(_elements[index], _view.getItem()), index);
		}
	}

	@Override
	public void dispose() {
	}
}
