package org.exist.eclipse.auto.internal.result.view;

import java.util.ArrayList;
import java.util.Collection;

import org.exist.eclipse.auto.query.IQuery;
import org.exist.eclipse.auto.query.IQueryResult;

/**
 * The QueryGroup is a representation of the results for a specific query. It's
 * used while generating the result xml.
 * 
 * @author Pascal Schmidiger
 */
public class QueryGroup {
	private long _compilation;
	private long _execution;
	private final IQuery _query;
	private final Collection<IQueryResult> _results;

	/**
	 * QueryGroup constructor
	 * 
	 * @param query
	 */
	public QueryGroup(IQuery query) {
		_query = query;
		_compilation = 0;
		_execution = 0;
		_results = new ArrayList<IQueryResult>();
	}

	/**
	 * Adds the result
	 * 
	 * @param result
	 */
	public void addResult(IQueryResult result) {
		_compilation += result.getCompileTime();
		_execution += result.getExecutionTime();
		_results.add(result);
	}

	/**
	 * Gets the execution time
	 * 
	 * @return execution time
	 */
	public long getExecution() {
		return _execution;
	}

	/**
	 * This gets the compilation time
	 * 
	 * @return compilation time
	 */
	public long getCompilation() {
		return _compilation;
	}

	/**
	 * Gets the average compilation time
	 * 
	 * @return average compilation time
	 */
	public long getAverageCompilation() {
		return _compilation / _query.getQuantity();
	}

	/**
	 * Gets the average execution
	 * 
	 * @return average execution
	 */
	public long getAverageExecution() {
		return _execution / _query.getQuantity();
	}

	/**
	 * Gets the query
	 * 
	 * @return query
	 */
	public IQuery getQuery() {
		return _query;
	}

	/**
	 * Gets the results
	 * 
	 * @return Collection of IResults
	 */
	public Collection<IQueryResult> getResults() {
		return _results;
	}
}
