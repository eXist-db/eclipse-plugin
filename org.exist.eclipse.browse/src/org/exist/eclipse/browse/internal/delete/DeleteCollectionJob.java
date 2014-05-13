/**
 * DeleteCollectionJob.java
 */
package org.exist.eclipse.browse.internal.delete;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.BrowseCoordinator;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.exception.ConnectionException;

/**
 * Job for deleting a collection.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class DeleteCollectionJob extends Job {

	private final IBrowseItem[] _items;

	public DeleteCollectionJob(IBrowseItem[] items) {
		super("Delete");
		_items = items;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Delete", _items.length);
		final Collection<IBrowseItem> removedItems = new ArrayList<IBrowseItem>();
		try {
			for (IBrowseItem item : _items) {
				setName("Delete '" + item + "'");
				IManagementService service = IManagementService.class.cast(item
						.getConnection().getAdapter(IManagementService.class));
				service.removeCollection(item.getCollection());
				removedItems.add(item);
				monitor.worked(1);
			}
		} catch (ConnectionException e) {
			return new Status(Status.ERROR, BrowsePlugin.getId(),
					"Error while deleting collections", e);
		} finally {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					BrowseCoordinator.getInstance().removed(
							removedItems.toArray(new IBrowseItem[removedItems
									.size()]));
				}
			});
		}
		return Status.OK_STATUS;
	}
}
