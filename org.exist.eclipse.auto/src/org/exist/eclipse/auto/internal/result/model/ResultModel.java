/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model;

import java.util.ArrayList;

/**
 * This is an Implementation of the IResultModel interface. It contains all the
 * automation result data.
 * 
 * @author Markus Tanner
 */
public class ResultModel implements IResultModel {

	int _threadCount;
	int _queryCount;
	int _averageCompilationTime;
	int _averageExecutionTime;
	ArrayList<QueryResultEntity> _queryResults;

	public ResultModel() {
		_queryResults = new ArrayList<QueryResultEntity>();
	}

	public ArrayList<QueryResultEntity> getQueryResults() {
		return _queryResults;
	}

	public int getThreadCount() {
		return _threadCount;
	}

	public int getQueryCount() {
		return _queryCount;
	}

	public void setThreadCount(int threadCount) {
		_threadCount = threadCount;
	}

	public void setQueryCount(int queryCount) {
		_queryCount = queryCount;
	}

	public void addQueryResultEntity(QueryResultEntity result) {
		_queryResults.add(result);
	}

	public Object[] getContents() {
		return _queryResults.toArray();
	}

	public int getAvgCompTime() {
		int totCompTime = 0;
		for (QueryResultEntity result : _queryResults) {
			totCompTime += result.getAvgCompTime();
		}
		return totCompTime / _queryResults.size();
	}

	public int getAvgExecTime() {
		int totExecTime = 0;
		for (QueryResultEntity result : _queryResults) {
			totExecTime += result.getAvgExecTime();
		}
		return totExecTime / _queryResults.size();
	}

	/**
	 * Updates the model
	 */
	public void updateModel() {
		for (QueryResultEntity queryResultEntity : _queryResults) {
			queryResultEntity.setModel(this);
		}
	}
}
