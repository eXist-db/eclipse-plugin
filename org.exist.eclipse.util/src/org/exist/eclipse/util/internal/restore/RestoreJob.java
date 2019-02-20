package org.exist.eclipse.util.internal.restore;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.exist.eclipse.DatabaseInstanceLookup;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.util.internal.UtilPlugin;

/**
 * This class is responsible to run the actual restore job. It relies on the
 * eXist API and accesses the according method.
 * 
 * @author Markus Tanner
 * 
 */
public class RestoreJob extends Job {

	private final IBrowseItem item;
	private final String location;

	public RestoreJob(IBrowseItem item, String location) {
		super("Restoring from " + location);
		this.item = item;
		this.location = location;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Restore", 1);
			IConnection connection = item.getConnection();
			DatabaseInstanceLookup.getInstance(connection.getVersion()).restore(connection, location,
					connection.getUri() + item.getPath());
			Display.getDefault().asyncExec(() -> MessageDialog.openInformation(new Shell(), "Restore completed",
					"Restore from '" + location + "' completed"));
			IBrowseService.class.cast(item.getAdapter(IBrowseService.class)).refresh();
		} catch (Exception e) {
			return new Status(IStatus.ERROR, UtilPlugin.getId(), "Failure while restoring from backup", e);
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

}
