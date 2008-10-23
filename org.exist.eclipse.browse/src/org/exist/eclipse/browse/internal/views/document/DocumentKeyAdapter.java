/**
 * BrowseKey.java
 */
package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Key Listener on {@link DocumentView}.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class DocumentKeyAdapter extends KeyAdapter {
	private final DocumentView _view;

	public DocumentKeyAdapter(DocumentView view) {
		_view = view;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.keyCode) {
		case SWT.F5:
			if (event.getSource() instanceof Tree) {
				Tree tree = (Tree) event.getSource();
				TreeItem[] selection = tree.getSelection();
				for (TreeItem treeItem : selection) {
					_view.getViewer().refresh(treeItem.getData());
				}
			}
		}
	}
}
