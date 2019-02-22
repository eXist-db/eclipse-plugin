/**
 * 
 */
package org.exist.eclipse.auto.internal.run;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.exist.eclipse.auto.connection.ConnectionPool;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.model.IAutoModel;
import org.exist.eclipse.auto.internal.result.AutomationResult;

/**
 * This class represents a Automation Job
 * 
 * @author Markus Tanner
 */
public class AutomationJob extends Job {

	private ConnectionPool _connPool;
	private ExecutorService _executor;
	private IAutoModel _autoModel;
	private String _collection;
	private String _target;

	/**
	 * Creates a AutomationJob
	 * 
	 * @param id
	 * @param connPool
	 * @param executor
	 * @param autoModel
	 * @param collection
	 * @param target
	 */
	public AutomationJob(int id, ConnectionPool connPool, ExecutorService executor, IAutoModel autoModel,
			String collection, String target) {
		super("Automation_" + id);
		_connPool = connPool;
		_executor = executor;
		_autoModel = autoModel;
		_collection = collection;
		_target = target;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {

			ExecutionPreparer queryBase = new ExecutionPreparer(_autoModel.getQueries(), _autoModel.getQueryOrderType(),
					_collection);
			ArrayList<Query> queries = queryBase.getQueriesInConfiguredOrder();

			monitor.beginTask("Running Query Automation", queries.size());
			AutomationResult autoResult = new AutomationResult(queries.size(), _autoModel.getThreadCount(),
					_autoModel.getQueryOrderType(), _autoModel.getAutoNote(), monitor, _target);

			for (Query query : queries) {
				QueryExecution execution = new QueryExecution(query, _connPool, autoResult);

				if (monitor.isCanceled()) {
					break;
				}
				_executor.execute(execution);
			}

			autoResult.join();

		} catch (Exception e) {
			return new Status(IStatus.ERROR, AutoUI.getId(), "Failure while running query", e);
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}
}
