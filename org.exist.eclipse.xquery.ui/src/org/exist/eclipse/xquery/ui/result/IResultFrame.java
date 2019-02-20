/**
 * 
 */
package org.exist.eclipse.xquery.ui.result;

/**
 * @author Pascal Schmidiger
 * 
 */
public interface IResultFrame {
	/**
	 * Add result item to the whole result. Before you running a xquery, you can
	 * define, how much results the result view should show. The return value gives
	 * you the hint, if you cross this limit. After that you can abort to add more
	 * results. This method is thread saved.
	 * 
	 * @param content of the query result.
	 * @return <code>true</code> if you can add more results, elsewhere
	 *         <code>false</code>.
	 */
	public boolean addResult(String content);
}
