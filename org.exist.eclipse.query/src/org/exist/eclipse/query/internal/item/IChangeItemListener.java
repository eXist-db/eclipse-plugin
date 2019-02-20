/**
 * IChangeItemListener.java
 */
package org.exist.eclipse.query.internal.item;

import org.exist.eclipse.browse.browse.IBrowseItem;

/**
 * Implement this interface and you will inform about {@link IBrowseItem}
 * changes in the query plugin. You have to add the listener on
 * {@link ChangeItemNotifier}.
 * 
 * @see ChangeItemNotifier
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IChangeItemListener {
	/**
	 * Notify if the {@link IBrowseItem} is changed and give the new item as
	 * parameter.
	 * 
	 * @param item the new {@link IBrowseItem}.
	 */
	public void change(IBrowseItem item);
}
