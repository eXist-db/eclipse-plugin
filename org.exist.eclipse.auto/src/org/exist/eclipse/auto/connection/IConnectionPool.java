/**
 * 
 */
package org.exist.eclipse.auto.connection;

/**
 * The ConnectionPool contains a collection of the available
 * {@link IQueryRunner} In order to run queries you need to get a
 * {@link IQueryRunner} and put it back afterwards
 * 
 * @author Markus Tanner
 */
public interface IConnectionPool {

	/**
	 * Returns a query runner instance so that a query can be executed via this
	 * instance.
	 * 
	 * @return An IQueryRunner instance
	 */
	public IQueryRunner getQueryRunner();

	/**
	 * Adds the IQueryRunner back to the pool after the query was performed.
	 * 
	 * @param queryRunner
	 */
	public void putQueryRunner(IQueryRunner queryRunner);

}
