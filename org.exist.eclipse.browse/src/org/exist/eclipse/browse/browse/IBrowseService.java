/**
 * IBrowseService.java
 */
package org.exist.eclipse.browse.browse;

import java.util.Set;

import org.exist.eclipse.browse.internal.browse.BrowseItem;
import org.exist.eclipse.exception.ConnectionException;

/**
 * This interface provide service methods for a {@link IBrowseItem}. Get a new
 * implemenation over {@link BrowseItem#getAdapter(Class)} with this interface
 * as class.
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IBrowseService {
	/**
	 * Create this item on xmldb if does not exist.
	 * 
	 * @throws ConnectionException
	 */
	public void create() throws ConnectionException;

	/**
	 * Delete this item on the xmldb.
	 * 
	 * @throws ConnectionException
	 */
	public void delete() throws ConnectionException;

	/**
	 * Check if the collection exists. If the collection does not exist then
	 * <ul>
	 * <li>a message dialog will show</li>
	 * <li>call {@link BrowseCoordinator#removed(IBrowseItem)}</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if the item exists, elsewhere
	 *         <code>false</code>.
	 */
	public boolean check();

	/**
	 * Refresh this item.
	 * 
	 * @see IBrowseItemListener#refresh(IBrowseItem)
	 */
	public void refresh();

	/**
	 * @param recursive
	 *            <code>true</code> then return also the children of the
	 *            children, <code>false</code> return also the directly
	 *            children
	 * @param self
	 *            <code>true</code>then add itself to the result
	 * @return all collections from the xmldb as {@link IBrowseItem}.
	 * @throws ConnectionException
	 */
	public Set<IBrowseItem> getChildren(boolean self, boolean recursive)
			throws ConnectionException;

	/**
	 * Move the {@link IBrowseItem} to the given <code>item</code>.
	 * 
	 * @param item
	 * @return <code>true</code> if the item was moved, elsewhere
	 *         <code>false</code>.
	 * @throws ConnectionException
	 */
	public boolean move(IBrowseItem item) throws ConnectionException;
}
