/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model;

import org.exist.eclipse.auto.query.State;

/**
 * A RunEntity represents the outcome of a single query run.
 * 
 * @author Markus Tanner
 */
public class RunEntity {

	State _state;
	int _compilation;
	int _execution;
	int _index;

	/**
	 * RunEntity Constructor
	 */
	public RunEntity() {
	}

	/**
	 * Gets the state of the query run
	 * 
	 * @return state
	 */
	public State getState() {
		return _state;
	}

	/**
	 * Gets the compilation time.
	 * 
	 * @return compilation time
	 */
	public int getCompilation() {
		return _compilation;
	}

	/**
	 * Gets the execution time.
	 * 
	 * @return execution time
	 */
	public int getExecution() {
		return _execution;
	}

	/**
	 * Table index for display purposes.
	 * 
	 * @return index
	 */
	public int getIndex() {
		return _index;
	}

	/**
	 * Sets the state
	 * 
	 * @param state
	 */
	public void setState(State state) {
		_state = state;
	}

	/**
	 * Sets the compilation time
	 * 
	 * @param compilation
	 */
	public void setCompilation(int compilation) {
		_compilation = compilation;
	}

	/**
	 * Sets the execution time
	 * 
	 * @param execution
	 */
	public void setExecution(int execution) {
		_execution = execution;
	}

	/**
	 * Sets the index
	 * 
	 * @param index
	 */
	public void setIndex(int index) {
		_index = index;
	}

}
