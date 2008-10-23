/**
 * BrowseDeferredAdapter.java
 */
package org.exist.eclipse.browse.internal.views.browse.asynch;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.progress.IElementCollector;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * Adapter class for getting collections asynchronous.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class BrowseDeferredAdapter implements IDeferredWorkbenchAdapter {

	public BrowseDeferredAdapter() {
	}

	public void fetchDeferredChildren(Object object,
			IElementCollector collector, IProgressMonitor monitor) {
		if (object instanceof IBrowseItem) {
			IBrowseItem item = IBrowseItem.class.cast(object);
			String[] collections;
			try {
				monitor.beginTask("Loading", item.getCollection()
						.getChildCollectionCount());
				collections = item.getCollection().listChildCollections();
				GetBrowseItemJob job = new GetBrowseItemJob("Get " + item,
						collector, item, collections);
				job.schedule();
				job.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (Exception e) {
				StringBuilder message = new StringBuilder(50).append(
						"Error while fetching children for collection '")
						.append(item).append("'");
				IStatus status = new Status(Status.ERROR, BrowsePlugin.getId(),
						message.toString(), e);
				BrowsePlugin.getDefault().getLog().log(status);
			} finally {
				monitor.done();
			}
		}
	}

	public ISchedulingRule getRule(Object object) {
		return null;
	}

	public boolean isContainer() {
		return false;
	}

	public Object[] getChildren(Object o) {
		return null;
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return null;
	}

	public String getLabel(Object o) {
		return null;
	}

	public Object getParent(Object o) {
		return null;
	}

}
