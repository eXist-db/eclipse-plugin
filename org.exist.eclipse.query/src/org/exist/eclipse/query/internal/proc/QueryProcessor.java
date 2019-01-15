package org.exist.eclipse.query.internal.proc;

import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.exception.ConnectionException;

/**
 * This class handles a query request.
 * 
 * @author Markus Tanner
 * 
 */
public class QueryProcessor implements IQueryProcessor {

	private static int _id = 1;
	private final IBrowseItem _item;
	private final String _query;
	private final int _maxDisplay;
	private QueryJob _job;

	/**
	 * Create e new processor.
	 * 
	 * @param item
	 *            on which the query will run.
	 * @param query
	 *            the query which will run.
	 */
	public QueryProcessor(IBrowseItem item, String query, int maxDisplay) {
		_item = item;
		_query = query;
		_maxDisplay = maxDisplay;
	}

	@Override
	public void runQuery() throws ConnectionException {
		if (_item != null
				&& IManagementService.class.cast(
						_item.getConnection().getAdapter(
								IManagementService.class)).check()) {
			if (IBrowseService.class.cast(
					_item.getAdapter(IBrowseService.class)).check()) {
				_job = new QueryJob(getNextId(), _item, _query, _maxDisplay);
				_job.setUser(true);
				_job.schedule();
			}
		}
	}

	private static int getNextId() {
		return _id++;
	}
}
