/**
 * 
 */
package org.exist.eclipse.auto.connection;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * The ConnectionPool contains a collection of the available
 * {@link IQueryRunner} In order to run queries you need to get a
 * {@link IQueryRunner} and put it back afterwards
 * 
 * @author Markus Tanner
 */
public class ConnectionPool implements IConnectionPool {

	private ArrayBlockingQueue<IQueryRunner> _queryRunners;

	/**
	 * ConnectionPool constructor
	 * 
	 * @param count
	 * @param autoContext
	 */
	public ConnectionPool(int count, IAutoContext autoContext) {
		_queryRunners = new ArrayBlockingQueue<IQueryRunner>(count);
		initialize(count, autoContext);
	}

	public IQueryRunner getQueryRunner() {
		try {
			return _queryRunners.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void putQueryRunner(IQueryRunner queryRunner) {
		try {
			_queryRunners.put(queryRunner);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes all the connections that are represented by the IQueryRunner
	 * instances.
	 */
	public void closeConnections() {
		for (IQueryRunner queryRunner : _queryRunners) {
			queryRunner.close();
		}
		_queryRunners.clear();
	}

	//--------------------------------------------------------------------------
	// Private Methods
	//--------------------------------------------------------------------------

	private void initialize(int count, IAutoContext autoContext) {
		for (int i = 0; i < count; i++) {
			putQueryRunner(autoContext.createQueryRunner());
		}
	}
}
