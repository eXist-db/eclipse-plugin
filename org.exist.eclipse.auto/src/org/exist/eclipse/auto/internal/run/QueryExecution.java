/**
 * 
 */
package org.exist.eclipse.auto.internal.run;

import org.exist.eclipse.auto.connection.IConnectionPool;
import org.exist.eclipse.auto.connection.IQueryRunner;
import org.exist.eclipse.auto.internal.result.AutomationResult;
import org.exist.eclipse.auto.internal.result.QueryResult;
import org.exist.eclipse.auto.query.IQuery;
import org.exist.eclipse.auto.query.IQueryResult;

/**
 * In this class the execution of a query is handled. It implements the Runnable
 * interface so that it can be executed by the executor.
 * 
 * @author Markus Tanner
 */
public class QueryExecution implements Runnable {

	private final IQuery _query;
	private final IConnectionPool _connectionPool;
	private final AutomationResult _automationResult;

	/**
	 * Constructor
	 * 
	 * @param query
	 * @param connectionPool
	 * @param automationResult
	 */
	public QueryExecution(IQuery query, IConnectionPool connectionPool,
			AutomationResult automationResult) {
		_query = query;
		_connectionPool = connectionPool;
		_automationResult = automationResult;
	}

	@Override
	public void run() {

		// fetch a query runner
		IQueryRunner queryRunner = _connectionPool.getQueryRunner();

		// run the query
		IQueryResult result = new QueryResult(_query);

		try {
			result = queryRunner.runQuery(result);

		} finally {
			// put the QueryRunner back into the pool
			_connectionPool.putQueryRunner(queryRunner);

			// add the result
			_automationResult.addQueryResult(result);
		}

	}
}
