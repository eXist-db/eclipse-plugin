package org.exist.eclipse.browse.document;

import org.eclipse.core.runtime.IAdaptable;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.Resource;

/**
 * This is an abstraction of a xmldb document.
 * 
 * @author Pascal Schmidiger
 */
public interface IDocumentItem extends IAdaptable {

	/**
	 * Check only if the document does exists in the xmldb.
	 * 
	 * @return <code>true</code> if exists, elsewhere <code>false</code>.
	 */
	public boolean exists();

	/**
	 * @return the name of the item.
	 */
	public String getName();

	/**
	 * Get the xmldb resource of this element.
	 * 
	 * @return the resource if available, elsewhere <code>null</code>.
	 * @throws ConnectionException
	 */
	public Resource getResource() throws ConnectionException;

	/**
	 * @return the parent of this element, which means the {@link IBrowseItem}
	 *         (collection).
	 */
	public IBrowseItem getParent();

	/**
	 * @return the absolute path of the document
	 */
	public String getPath();

}