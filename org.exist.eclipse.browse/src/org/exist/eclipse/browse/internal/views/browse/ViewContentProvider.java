/**
 * ViewContentProvider.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.progress.DeferredTreeContentManager;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.browse.internal.browse.BrowseItem;
import org.exist.eclipse.browse.internal.browse.BrowseItemInvisible;
import org.exist.eclipse.browse.internal.views.browse.asynch.BrowseDeferredTree;
import org.exist.eclipse.exception.ConnectionException;
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

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
		// clear();
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
		Object[] result = null;
		if (parent instanceof IConnection) {
			IConnection connection = IConnection.class.cast(parent);
			if (connection.isOpen()) {
				try {
					result = new Object[1];
					result[0] = new BrowseItem(connection, connection.getRoot()
							.getName());
				} catch (XMLDBException e) {
					Status status = new Status(Status.ERROR, BrowsePlugin
							.getId(), "Error while getting root collection", e);
					BrowsePlugin.getDefault().getLog().log(status);
					result = null;
				}
			}
		} else if (parent instanceof IBrowseItem) {
			IBrowseItem item = IBrowseItem.class.cast(parent);
			if (IManagementService.class.cast(
					item.getConnection().getAdapter(IManagementService.class))
					.check()) {
				if (parent instanceof BrowseItemInvisible) {
					result = BrowseItemInvisible.class.cast(parent)
							.getChildren();
				} else if (parent instanceof BrowseItem) {
					IBrowseItem browseItem = BrowseItem.class.cast(parent);
					if (IBrowseService.class.cast(
							browseItem.getAdapter(IBrowseService.class))
							.check()) {
						DeferredTreeContentManager deferredTree = new BrowseDeferredTree(
								this, _view.getViewer());
						result = deferredTree.getChildren(parent);
					}
				}
			}
		}

		if (result == null) {
			result = new Object[0];
		}
		return result;
	}

	@Override
	public boolean hasChildren(Object parent) {
		boolean result = false;
		if (parent instanceof IConnection) {
			IConnection connection = IConnection.class.cast(parent);
			if (connection.isOpen()) {
				result = true;
			}
		} else if (parent instanceof IBrowseItem) {
			final IBrowseItem item = IBrowseItem.class.cast(parent);
			if (IManagementService.class.cast(
					item.getConnection().getAdapter(IManagementService.class))
					.check()) {
				if (IBrowseService.class.cast(
						item.getAdapter(IBrowseService.class)).check()) {
					try {
						result = item.hasChildren();
					} catch (ConnectionException ce) {
						String message = "Error while method hasChildren";
						Status status = new Status(Status.ERROR, BrowsePlugin
								.getId(), message, ce);
						BrowsePlugin.getDefault().getLog().log(status);
						BrowsePlugin.getDefault().errorDialog(message,
								ce.getMessage(), status);
					}
				}
			}
		}
		return result;
	}

}
