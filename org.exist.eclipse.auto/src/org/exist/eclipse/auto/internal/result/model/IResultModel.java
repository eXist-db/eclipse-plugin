/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model;

import java.util.ArrayList;

import org.exist.eclipse.auto.query.State;

/**
 * The ResultModel represents the central automation result data unit. Based on
 * this model the form page is put together.
 * 
 * @author Markus Tanner
 */
public interface IResultModel {

	/**
	 * Returns the of an automation query-results that are placed on the model
	 * 
	 * @return The query-results from the model
	 */
	public ArrayList<QueryResultEntity> getQueryResults();

	/**
	 * Returns the query-results from the model as an array
	 * 
	 * @return The query-results from the model
	 */
	public Object[] getContents();

	/**
	 * Returns the automation thread count
	 * 
	 * @return ThreadCount
	 */
	public int getThreadCount();

	/**
	 * Returns the automation query count
	 * 
	 * @return query count
	 */
	public int getQueryCount();
	
	/**
	 * @return {@link State#SUCCESS}, if all query runs was successful, else {@link State#FAILURE}.
	 */
	public State getState();

	/**
	 * Sets the query results
	 * 
	 * @param queryResults
	 */
	public void addQueryResultEntity(QueryResultEntity result);

	/**
	 * Sets the thread count
	 * 
	 * @param threadCount
	 */
	public void setThreadCount(int threadCount);

	/**
	 * Sets the query count
	 * 
	 * @param queryCount
	 */
	public void setQueryCount(int queryCount);

	/**
	 * Returns the average compilation time
	 * 
	 * @return average compilation time
	 */
	public int getAvgCompTime();

	/**
	 * Returns the average exectution time
	 * 
	 * @return exectution compilation time
	 */
	public int getAvgExecTime();

	/**
	 * Returns the totally result count
	 * 
	 * @return
	 */
	public long getResultCount();

}
