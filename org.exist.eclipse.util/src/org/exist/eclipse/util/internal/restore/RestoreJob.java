package org.exist.eclipse.util.internal.restore;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.exist.backup.Restore;
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

	private final IBrowseItem _item;
	private final String _location;

	public RestoreJob(IBrowseItem item, String location) {
		super("Restoring from " + location);
		_item = item;
		_location = location;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Restore", 1);
			Restore restore = new Restore(_item.getConnection().getUsername(), _item.getConnection().getPassword(),
					null, new File(_location), _item.getConnection().getUri());
			restore.restore(false, null);
			Display.getDefault().asyncExec(() -> MessageDialog.openInformation(new Shell(), "Restore completed",
					"Restore from '" + _location + "' completed"));
			IBrowseService.class.cast(_item.getAdapter(IBrowseService.class)).refresh();
		} catch (Exception e) {
			return new Status(IStatus.ERROR, UtilPlugin.getId(), "Failure while restoring from backup", e);
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

}
