package org.exist.eclipse.query.internal.proc;

/**
 * Implement this interface and you will inform about result of a running query .
 * You have to add the listener on {@link QueryResultNotifier}.
 * 
 * @see QueryResultNotifier
 * 
 * @author Markus Tanner
 */
public interface IQueryResultListener {

	/**
	 * Add a new result from the query.
	 * 
	 * @param event
	 */
	public void addResult(QueryResultEvent event);

}
