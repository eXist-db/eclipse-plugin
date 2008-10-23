/**
 * IQueryListener.java
 */
package org.exist.eclipse.query.internal.proc;

/**
 * Implement this interface and you will inform about the basic conditions while
 * a query is running. You have to add the listener on {@link QueryNotifier}.
 * 
 * @see QueryNotifier
 * @author Pascal Schmidiger
 * 
 */
public interface IQueryListener {
	/**
	 * Notify before the query will run.
	 * 
	 * @param event
	 */
	public void start(QueryStartEvent event);

	/**
	 * Notify after the query was run.
	 * 
	 * @param event
	 */
	public void end(QueryEndEvent event);
}
