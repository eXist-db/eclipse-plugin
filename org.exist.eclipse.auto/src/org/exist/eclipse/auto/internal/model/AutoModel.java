/**
 * 
 */
package org.exist.eclipse.auto.internal.model;

import java.util.ArrayList;

/**
 * This class represents the actual data model. It contains all the actual
 * queries as a collection and know the automation specific parameters.
 * 
 * @author Markus Tanner
 */
public class AutoModel implements IAutoModel {

	private ArrayList<QueryEntity> _queries;
	private int _threadCount;
	private QueryOrderType _queryOrderType;
	private String _autoNote;

	/**
	 * @return the queries
	 */
	@Override
	public ArrayList<QueryEntity> getQueries() {
		return _queries;
	}

	/**
	 * Constructor class
	 */
	public AutoModel() {
		_queries = new ArrayList<>();
	}

	@Override
	public Object[] getContents() {
		return _queries.toArray();
	}

	@Override
	public void addQuery(QueryEntity query) {
		_queries.add(query);
	}

	/**
	 * Updates the model
	 */
	public void UpdateModel() {
		for (QueryEntity queryEntity : _queries) {
			queryEntity.setModel(this);
		}
	}

	@Override
	public void removeQuery(QueryEntity query) {
		_queries.remove(query);
	}

	@Override
	public void setThreadCount(int threadCount) {
		_threadCount = threadCount;
	}

	@Override
	public int getThreadCount() {
		return _threadCount;
	}

	@Override
	public QueryOrderType getQueryOrderType() {
		return _queryOrderType;
	}
	
	@Override
	public void setQueryOrderType(QueryOrderType type){
		_queryOrderType = type;
	}

	@Override
	public String getAutoNote() {
		return _autoNote;
	}

	@Override
	public void setAutoNote(String note) {
		_autoNote = note;
	}
	

}
