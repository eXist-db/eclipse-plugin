/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.result;

import org.exist.eclipse.xquery.ui.result.IQueryFrame;

/**
 * This interface is used for creating a new {@link IQueryFrame}.
 * 
 * @author Pascal Schmidiger
 */
public interface IQueryFrameInfo {
	/**
	 * @return the max count of results, which will show.
	 */
	public int getMaxCount();

	/**
	 * @return the xquery from the editor.
	 */
	public String getQuery();

	/**
	 * @return the filename from which the xquery is.
	 */
	public String getFilename();

	/**
	 * @return <code>true</code> if you should create a new tab for result,
	 *         elsewhere <code>false</code>.
	 */
	public boolean isCreatedNewTab();
}
