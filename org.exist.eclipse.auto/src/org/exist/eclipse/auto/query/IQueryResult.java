/**
 * 
 */
package org.exist.eclipse.auto.query;

/**
 * The IQueryResult is the entity into which the result for a query is stored.
 * 
 * @author Markus Tanner
 */
public interface IQueryResult {

	/**
	 * Returns the query State
	 * 
	 * @return State
	 */
	public State getQueryState();

	/**
	 * Sets the query state
	 * 
	 * @param state
	 */
	public void setQueryState(State state);

	/**
	 * Returns the compile time
	 * 
	 * @return compile time
	 */
	public long getCompileTime();

	/**
	 * Set compile time
	 * 
	 * @param time
	 */
	public void setCompileTime(long time);

	/**
	 * Returns the execution time
	 * 
	 * @return execution time
	 */
	public long getExecutionTime();

	/**
	 * Returns the result count of the query.
	 * 
	 * @return the result count
	 */
	public long getResultCount();

	/**
	 * Set the result count.
	 * 
	 * @param resultCount
	 */
	public void setResultCount(long resultCount);

	/**
	 * Set execution time
	 * 
	 * @param time
	 */
	public void setExecutionTime(long time);

	/**
	 * Returns the query
	 * 
	 * @return the query
	 */
	public IQuery getQuery();

}
