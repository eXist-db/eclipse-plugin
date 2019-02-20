/**
 * CollectionJob.java
 */
package org.exist.eclipse.browse.internal.views.browse.asynch;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.IElementCollector;
import org.exist.eclipse.URIUtils;
import org.exist.eclipse.browse.browse.IBrowseItem;

/**
 * Job for getting child collections.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class GetBrowseItemJob extends Job {

	private IElementCollector _collector;
	private final String[] _children;
	private final IBrowseItem _item;

	public GetBrowseItemJob(String name, IElementCollector collector,
			IBrowseItem item, String[] children) {
		super(name);
		_collector = collector;
		_item = item;
		_children = children;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IStatus status = Status.OK_STATUS;
		for (String child : _children) {
			IBrowseItem item = _item.getChild(URIUtils.urlDecodeUtf8(child));
			_collector.add(item, monitor);
			monitor.worked(1);
		}

		return status;
	}
}
