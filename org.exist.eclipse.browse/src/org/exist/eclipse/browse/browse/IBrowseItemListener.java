/**
 * IBrowseItemListener.java
 */
package org.exist.eclipse.browse.browse;

/**
 * Interface, on which you will inform about {@link IBrowseItem} changes. You
 * have to register it on {@link BrowseCoordinator}.
 * 
 * @see BrowseCoordinator
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IBrowseItemListener {
	/**
	 * The given <code>item</code> was added.
	 * 
	 * @param item
	 */
	public void added(IBrowseItem item);

	/**
	 * The given <code>item</code> was removed.
	 * 
	 * @param items
	 *            the removed items.
	 */
	public void removed(IBrowseItem[] items);

	/**
	 * You have to refresh the given <code>item</code>.
	 * 
	 * @param item
	 */
	public void refresh(IBrowseItem item);

	/**
	 * The given <code>fromItem</code> has moved to <code>toItem</code>.
	 * 
	 * @param fromItem
	 * @param toItem
	 */
	public void moved(IBrowseItem fromItem, IBrowseItem toItem);
}
