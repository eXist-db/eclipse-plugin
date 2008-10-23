/**
 * 
 */
package org.exist.eclipse.xquery.ui.result;

/**
 * This interface must be implemented by usage the {@link IQueryFrame}.
 * 
 * @see IQueryFrame
 * 
 * @author Pascal Schmidiger
 */
public interface IQueryEndState {
	/**
	 * @return the state about the query running.
	 */
	public State getState();

	/**
	 * This value represents the whole count of founded items and not how much
	 * you have to notified over {@link IResultFrame#addResult(String)}.
	 * 
	 * @return the count of founded items.
	 */
	public long getFoundedItems();

	/**
	 * @return the time of execution for the xquery.
	 */
	public long getExecutionTime();

	/**
	 * @return the time of compiling the xquery.
	 */
	public long getCompiledTime();

	/**
	 * @return if {{@link #getState()}=={@link State#ERROR} then return the
	 *         {@link Exception}, elsewhere return <code>null</code>.
	 */
	public Exception getException();

	/**
	 * State about the query running.
	 */
	public enum State {
		OK, ERROR
	}
}
