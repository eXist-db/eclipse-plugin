/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model;

import java.util.ArrayList;

import org.exist.eclipse.auto.query.State;

/**
 * This is an Implementation of the IResultModel interface. It contains all the
 * automation result data.
 * 
 * @author Markus Tanner
 */
public class ResultModel implements IResultModel {
	private int _threadCount;
	private int _queryCount;
	private State _state;
	private Long _resultCount;
	private ArrayList<QueryResultEntity> _queryResults;

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

	public State getState() {
		if (_state == null) {
			_state = State.SUCCESS;
			for (QueryResultEntity result : _queryResults) {
				if (!result.isSuccessful()) {
					_state = State.FAILURE;
					break;
				}
			}
		}
		return _state;
	}

	@Override
	public long getResultCount() {
		if (_resultCount == null) {
			long count = 0;
			for (QueryResultEntity result : _queryResults) {
				count += result.getResultCount();
			}
			_resultCount = new Long(count);
		}
		return _resultCount.longValue();
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
