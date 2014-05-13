package org.exist.eclipse.query.internal.xquery.context;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Pascal Schmidiger
 * 
 */
public class ContextViewContentProvider implements ILazyContentProvider {

	private TableViewer _viewer;
	private String[] _elements;

	@Override
	public void updateElement(int index) {
		_viewer.replace(_elements[index], index);
	}

	@Override
	public void dispose() {
		_elements = null;
		_viewer = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		_viewer = (TableViewer) viewer;
		_elements = (String[]) newInput;
	}

}
