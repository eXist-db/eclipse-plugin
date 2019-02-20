/**
 * 
 */
package org.exist.eclipse.query.internal.xquery.result;

import org.exist.eclipse.xquery.ui.result.IQueryEndState;

/**
 * Represents an {@link IQueryEndState}.
 * 
 * @author Pascal Schmidiger
 */
public class QueryEndState implements IQueryEndState {

	private final long _foundedItems;
	private final long _compiledTime;
	private final long _executionTime;
	private final Exception _exception;
	private final State _state;

	public QueryEndState(long foundedItems, long compiledTime, long executionTime) {
		this(State.OK, foundedItems, compiledTime, executionTime, null);
	}

	public QueryEndState(Exception exception, long compiledTime) {
		this(State.ERROR, 0, compiledTime, 0, exception);
	}

	private QueryEndState(State state, long foundedItems, long compiledTime, long executionTime, Exception exception) {
		_state = state;
		_foundedItems = foundedItems;
		_compiledTime = compiledTime;
		_executionTime = executionTime;
		_exception = exception;

	}

	@Override
	public long getCompiledTime() {
		return _compiledTime;
	}

	@Override
	public Exception getException() {
		return _exception;
	}

	@Override
	public long getExecutionTime() {
		return _executionTime;
	}

	@Override
	public long getFoundedItems() {
		return _foundedItems;
	}

	@Override
	public State getState() {
		return _state;
	}

}
