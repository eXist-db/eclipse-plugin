package org.exist.eclipse.auto.internal.run;

public class QueryToRun {
	
	private Query _query;
	private int _queryId;
	
	public QueryToRun(Query query, int queryId){
		_query = query;
		_queryId = queryId;
	}
	
	public Query getQuery(){
		return _query;
	}
	
	public int getQueryId(){
		return _queryId;
	}
	
}
