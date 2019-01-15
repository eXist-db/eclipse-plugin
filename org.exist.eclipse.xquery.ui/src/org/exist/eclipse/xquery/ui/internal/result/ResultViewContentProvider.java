/**
 * ViewContentProvider.java
 */
package org.exist.eclipse.xquery.ui.internal.result;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * This content provider is the lazy content provider for the
 * {@link ResultViewPart}.
 * 
 * @author Pascal Schmidiger
 */
public class ResultViewContentProvider implements ILazyContentProvider {
	private ResultItem[] _elements;
	private TableViewer _viewer;

	ResultViewContentProvider() {
	}

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		_viewer = (TableViewer) v;
		_elements = (ResultItem[]) newInput;
	}

	@Override
	public void updateElement(int index) {
		_viewer.replace(_elements[index], index);
	}

	@Override
	public void dispose() {
	}
}
