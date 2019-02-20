/**
 * IQueryListener.java
 */
package org.exist.eclipse.xquery.ui.result;

/**
 * With this interface you have the container to trigger the results.<br />
 * The order of method calls are:
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IQueryFrame {
	/**
	 * Call this method before the query is running.
	 * 
	 * @return the frame in which you can add your results.
	 */
	public IResultFrame start();

	/**
	 * @return the content of the query.
	 */
	public String getQuery();

	/**
	 * @return a unique name of the query frame.
	 */
	public String getName();

	/**
	 * Call this method after the query was running and you have add all of your
	 * results to the {@link IResultFrame} from the method {@link #start()}.
	 * 
	 * @param state information about the query running.
	 */
	public void end(IQueryEndState state);
}
