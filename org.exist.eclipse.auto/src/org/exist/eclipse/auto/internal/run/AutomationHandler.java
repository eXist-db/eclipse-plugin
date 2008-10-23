/**
 * 
 */
package org.exist.eclipse.auto.internal.run;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.exist.eclipse.auto.connection.ConnectionPool;
import org.exist.eclipse.auto.connection.IAutoContext;
import org.exist.eclipse.auto.internal.model.IAutoModel;

/**
 * The AutomationHandler initializes the execution of a job. It is responsible
 * to create an Executor an a ConnectionPool. Besides that it start an Eclipse
 * Job, so that information is shown while the automation is being processed.
 * 
 * @author Markus Tanner
 */
public final class AutomationHandler {

	private static int _id = 1;
	private AutomationJob _job;
	private ConnectionPool _connPool;
	private ExecutorService _executor;

	public static AutomationHandler _instance;

	/**
	 * Returns an AutomationHandler instance (singleton).
	 * 
	 * @return AutomationHandler instance
	 */
	public static AutomationHandler getInstance() {
		if (_instance == null) {
			_instance = new AutomationHandler();
		}
		return _instance;
	}

	/**
	 * Run the Query Automation.
	 * 
	 * @param autoModel
	 * @param autoContext
	 * @param target
	 */
	public void run(IAutoModel autoModel, IAutoContext autoContext,
			String collection, String target) {

		_executor = Executors.newFixedThreadPool(autoModel.getThreadCount());
		_connPool = new ConnectionPool(autoModel.getThreadCount(), autoContext);

		_job = new AutomationJob(getNextId(), _connPool, _executor, autoModel,
				collection, target);
		_job.setUser(true);
		_job.schedule();
	}

	/**
	 * Shuts down the executor and closes the connections in the connection
	 * pool.
	 */
	public void cleanup() {
		_executor.shutdown();
		_connPool.closeConnections();
	}

	//--------------------------------------------------------------------------
	// Private Methods
	//--------------------------------------------------------------------------

	/**
	 * Gets the next higher integer
	 * 
	 * @return id
	 */
	private static int getNextId() {
		return _id++;
	}

}
