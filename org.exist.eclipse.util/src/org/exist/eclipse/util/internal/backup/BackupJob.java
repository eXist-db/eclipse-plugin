package org.exist.eclipse.util.internal.backup;

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
import org.exist.eclipse.util.internal.UtilPlugin;

/**
 * This class is responsible to run the actual backup job. It relies on the
 * eXist API and accesses the according method.
 * 
 * @author Markus Tanner
 */
public class BackupJob extends Job {

	private final IBrowseItem item;
	private final String location;

	public BackupJob(IBrowseItem item, String location) {
		super("Backup " + item.getPath() + " to " + location);
		this.item = item;
		this.location = location;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Backup", 1);
		try {
			IConnection connection = item.getConnection();
			DatabaseInstanceLookup.getInstance(connection.getVersion()).backup(connection, location,
					connection.getUri() + item.getPath());
			Display.getDefault().asyncExec(() -> MessageDialog.openInformation(new Shell(), "Backup completed",
					"Backup for '" + item.getPath() + "' to '" + location + "' created"));
		} catch (Exception e) {
			return new Status(IStatus.ERROR, UtilPlugin.getId(), "Failure while creating backup", e);
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

}
