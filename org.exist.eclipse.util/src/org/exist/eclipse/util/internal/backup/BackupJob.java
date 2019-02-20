package org.exist.eclipse.util.internal.backup;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.exist.backup.Backup;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.util.internal.UtilPlugin;
import org.exist.xmldb.XmldbURI;

/**
 * This class is responsible to run the actual backup job. It relies on the
 * eXist API and accesses the according method.
 * 
 * @author Markus Tanner
 * 
 */
public class BackupJob extends Job {

	private final IBrowseItem _item;
	private final String _target;

	public BackupJob(IBrowseItem item, String target) {
		super("Backup " + item.getPath() + " to " + target);
		_item = item;
		_target = target;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Backup", 1);
		try {
			Backup backup = new Backup(_item.getConnection().getUsername(), _item.getConnection().getPassword(),
					_target, XmldbURI.xmldbUriFor(_item.getConnection().getUri() + _item.getPath()));
			backup.backup(false, null);

			Display.getDefault().asyncExec(() -> MessageDialog.openInformation(new Shell(), "Backup completed",
					"Backup for '" + _item.getPath() + "' to '" + _target + "' created"));
		} catch (Exception e) {
			return new Status(IStatus.ERROR, UtilPlugin.getId(), "Failure while creating backup", e);
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

}
