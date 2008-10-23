/**
 * QueryJob.java
 */
package org.exist.eclipse.query.internal.proc;

import javax.xml.transform.OutputKeys;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.query.internal.QueryPlugin;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XQueryService;

/**
 * This is an {@link Job} implementation for running a query in background.
 * 
 * @author Pascal Schmidiger
 */
public class QueryJob extends Job {

	private final IBrowseItem _item;
	private final int _id;
	private final String _query;
	private final int _maxDisplay;
	private int count = 0;

	/**
	 * Create a new job.
	 * 
	 * @param id
	 *            of the job.
	 * @param item
	 *            on which the query will run.
	 * @param query
	 *            the query which will run.
	 */
	public QueryJob(int id, IBrowseItem item, String query, int maxDisplay) {
		super("Query_" + id);
		_id = id;
		_item = item;
		_query = query;
		_maxDisplay = maxDisplay;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		XQueryService service;

		// notify listener about query start
		QueryNotifier.getInstance().start(new QueryStartEvent(_id));
		long tCompiled = 0;

		try {
			ResourceSet result;
			service = (XQueryService) _item.getCollection().getService(
					"XQueryService", "1.0");
			service.setProperty(OutputKeys.INDENT, "yes");

			long t0 = System.currentTimeMillis();
			CompiledExpression compiled = service.compile(_query);
			long t1 = System.currentTimeMillis();
			tCompiled = t1 - t0;
			result = service.execute(compiled);
			long tResult = System.currentTimeMillis() - t1;

			ResourceIterator i = result.getIterator();
			QueryResultEvent queryResultEvent = null;

			monitor.beginTask("Retrieving results...", new Long(result
					.getSize()).intValue());
			while (i.hasMoreResources() && !monitor.isCanceled()
					&& count < _maxDisplay) {
				Resource r = i.nextResource();
				queryResultEvent = new QueryResultEvent(_id, (String) r
						.getContent());

				// notify listener about query result
				QueryResultNotifier.getInstance().addResult(queryResultEvent);
				monitor.worked(1);
				count++;
			}

			// notify listener about query end
			QueryNotifier.getInstance()
					.end(
							new QueryEndEvent(_id, result.getSize(), tCompiled,
									tResult));

		} catch (Exception e) {
			QueryNotifier.getInstance().end(
					new QueryEndEvent(_id, tCompiled, e));
			return new Status(IStatus.ERROR, QueryPlugin.getId(),
					"Failure while running queries", e);
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}
}