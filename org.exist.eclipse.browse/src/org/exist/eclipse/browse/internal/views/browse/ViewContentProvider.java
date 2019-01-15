/**
 * ViewContentProvider.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.progress.DeferredTreeContentManager;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.browse.internal.browse.BrowseItem;
import org.exist.eclipse.browse.internal.browse.BrowseItemInvisible;
import org.exist.eclipse.browse.internal.views.browse.asynch.BrowseDeferredTree;
import org.xmldb.api.base.XMLDBException;

/**
 * The content provider class is responsible for providing objects to the view.
 * It can wrap existing objects in adapters or simply return objects as-is.
 * These objects may be sensitive to the current input of the view, or ignore it
 * and always show the same content (like Task List, for example).
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {
	private final BrowseView _view;

	public ViewContentProvider(BrowseView view) {
		_view = view;
	}

	private Object[] asChildrenArray(Object... objects) {
		return objects;
	}

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}

	@Override
	public Object getParent(Object child) {
		if (child instanceof IBrowseItem) {
			IBrowseItem item = IBrowseItem.class.cast(child);
			IBrowseItem parent = item.getParent();
			if (parent == null) {
				return item.getConnection();
			}
			return parent;
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof IConnection) {
			IConnection connection = IConnection.class.cast(parent);
			if (connection.isOpen()) {
				try {
					return asChildrenArray(new BrowseItem(connection,
							connection.getRoot().getName()));
				} catch (XMLDBException e) {
					Status status = new Status(IStatus.ERROR,
							BrowsePlugin.getId(),
							"Error while getting root collection", e);
					BrowsePlugin.getDefault().getLog().log(status);
					return asChildrenArray();
				}
			}
		} else if (parent instanceof IBrowseItem) {
			if (parent instanceof BrowseItemInvisible) {
				return BrowseItemInvisible.class.cast(parent).getChildren();
			} else if (parent instanceof BrowseItem) {
				DeferredTreeContentManager deferredTree = new BrowseDeferredTree(
						this, _view.getViewer());
				return asChildrenArray(deferredTree.getChildren(parent));
			}
		}
		return asChildrenArray();
	}

	@Override
	public boolean hasChildren(Object parent) {
		if (parent instanceof IConnection) {
			return IConnection.class.cast(parent).isOpen();
		}
		return true;
	}
}