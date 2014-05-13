package org.exist.eclipse.query.internal.xquery.result;

import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.query.internal.xquery.run.RunQuery;
import org.exist.eclipse.xquery.ui.result.IQueryFrame;
import org.exist.eclipse.xquery.ui.result.IResultFrame;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

/**
 * This is an {@link Job} implementation for running a query in background.
 * 
 * @author Pascal Schmidiger
 */
public class QueryJob extends Job {

	private final IBrowseItem _item;
	private final IQueryFrame _frame;

	/**
	 * Create a new job.
	 * 
	 * @param frame
	 *            of the job.
	 * @param item
	 *            on which the query will run.
	 */
	public QueryJob(IQueryFrame frame, IBrowseItem item) {
		super("Running Query '" + frame.getName() + "'");
		_frame = frame;
		_item = item;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// notify about query start
		IResultFrame resultFrame = _frame.start();
		long tCompiled = 0;

		ResourceSet result = null;
		try {
			RunQuery existQuery = new RunQuery(_item, _frame.getQuery());
			existQuery.init();

			long t0 = System.nanoTime();
			CompiledExpression compiled = existQuery.compile();
			long t1 = System.nanoTime();
			tCompiled = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
			result = existQuery.execute(compiled);
			long tResult = TimeUnit.NANOSECONDS
					.toMillis(System.nanoTime() - t1);

			ResourceIterator i = result.getIterator();

			monitor.beginTask("Retrieving results...",
					Long.valueOf(result.getSize()).intValue());

			while (i.hasMoreResources() && !monitor.isCanceled()) {
				Resource r = i.nextResource();
				if (!resultFrame.addResult((String) r.getContent())) {
					break;
				}
				monitor.worked(1);
			}

			QueryEndState endState = new QueryEndState(result.getSize(),
					tCompiled, tResult);
			_frame.end(endState);
		} catch (Exception e) {
			QueryEndState endState = new QueryEndState(e, tCompiled);
			_frame.end(endState);
		} finally {
			if (result != null) {
				try {
					result.clear();
				} catch (XMLDBException e) {
					// do nothing
				}
			}
			monitor.done();
		}
		return Status.OK_STATUS;
	}
}