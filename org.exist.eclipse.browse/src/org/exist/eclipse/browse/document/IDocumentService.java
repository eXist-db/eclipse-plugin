/**
 * IBrowseService.java
 */
package org.exist.eclipse.browse.document;

import org.eclipse.core.runtime.IConfigurationElement;
import org.exist.eclipse.browse.create.CreateDocumentException;
import org.exist.eclipse.browse.internal.document.DocumentItem;
import org.exist.eclipse.exception.ConnectionException;

/**
 * This interface provide service methods for a {@link IDocumentItem}. Get a new
 * implemenation over {@link DocumentItem#getAdapter(Class)} with this interface
 * as class.
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IDocumentService {
	/**
	 * Check if the document exists. If the document does not exist then
	 * <ul>
	 * <li>a message dialog will show</li>
	 * <li>call {@link DocumentCoordinator#removed(IDocumentItem)}</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if the item exists, elsewhere
	 *         <code>false</code>.
	 */
	public boolean check();

	/**
	 * Create this item on xmldb if does not exist.
	 * 
	 * @param providerElement
	 *            the configuration of the provider which is from extension
	 *            "org.exist.eclipse.browse.createdocument".
	 * @throws CreateDocumentException
	 */
	public void create(IConfigurationElement providerElement)
			throws CreateDocumentException;

	/**
	 * Delete this item on the xmldb.
	 * 
	 * @throws ConnectionException
	 */
	public void delete() throws ConnectionException;

	/**
	 * Move the {@link IDocumentItem} to the given <code>item</code>.
	 * 
	 * @param item
	 * @return <code>true</code> if the item was moved, elsewhere
	 *         <code>false</code>.
	 * @throws ConnectionException
	 */
	public boolean move(IDocumentItem item) throws ConnectionException;
}
