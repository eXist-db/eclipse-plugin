/**
 * BrowseDeferredTree.java
 */
package org.exist.eclipse.browse.internal.views.browse.asynch;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.progress.DeferredTreeContentManager;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;

/**
 * Interface between the contentprovider and the deferredadapter.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class BrowseDeferredTree extends DeferredTreeContentManager {

	public BrowseDeferredTree(ITreeContentProvider provider,
			AbstractTreeViewer viewer) {
		super(provider, viewer);
	}

	@Override
	protected IDeferredWorkbenchAdapter getAdapter(Object element) {
		return new BrowseDeferredAdapter();
	}

}
