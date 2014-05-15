/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model;

import java.util.ArrayList;

import org.exist.eclipse.auto.internal.model.QueryOrderType;
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
	private QueryOrderType _queryOrderType;
	private String _autoNote;

	public ResultModel() {
		_queryResults = new ArrayList<>();
	}

	@Override
	public ArrayList<QueryResultEntity> getQueryResults() {
		return _queryResults;
	}

	@Override
	public int getThreadCount() {
		return _threadCount;
	}

	@Override
	public int getQueryCount() {
		return _queryCount;
	}

	@Override
	public void setThreadCount(int threadCount) {
		_threadCount = threadCount;
	}

	@Override
	public void setQueryCount(int queryCount) {
		_queryCount = queryCount;
	}

	@Override
	public void addQueryResultEntity(QueryResultEntity result) {
		_queryResults.add(result);
	}

	@Override
	public Object[] getContents() {
		return _queryResults.toArray();
	}

	@Override
	public QueryOrderType getQueryOrderType() {
		return _queryOrderType == null ? QueryOrderType.SEQUENTIAL : _queryOrderType;
	}
	
	@Override
	public void setQueryOrderType(QueryOrderType type){
		_queryOrderType = type;
	}

	@Override
	public String getAutoNote() {
		return _autoNote == null ? "" : _autoNote;
	}

	@Override
	public void setAutoNote(String note) {
		_autoNote = note;
	}	
	
	@Override
	public int getAvgCompTime() {
		int totCompTime = 0;
		for (QueryResultEntity result : _queryResults) {
			totCompTime += result.getAvgCompTime();
		}
		return totCompTime / _queryResults.size();
	}

	@Override
	public int getAvgExecTime() {
		int totExecTime = 0;
		for (QueryResultEntity result : _queryResults) {
			totExecTime += result.getAvgExecTime();
		}
		return totExecTime / _queryResults.size();
	}

	@Override
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
