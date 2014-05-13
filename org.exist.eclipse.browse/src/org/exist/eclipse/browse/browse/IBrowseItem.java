package org.exist.eclipse.browse.browse;

import org.eclipse.core.runtime.IAdaptable;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.Collection;

/**
 * This is an abstraction of a xmldb collection.
 * 
 * @author Pascal Schmidiger
 */
public interface IBrowseItem extends IAdaptable {
	/**
	 * Check only if the collection does exists in the xmldb.
	 * 
	 * @return <code>true</code> if exists, elsewhere <code>false</code>.
	 */
	public boolean exists();

	/**
	 * @return the name of the item.
	 */
	public String getName();

	/**
	 * Get a child item with the given <code>name</code>.
	 * 
	 * @param name
	 *            the name of the new item.
	 * @return the child item.
	 */
	public IBrowseItem getChild(String name);

	/**
	 * Get the xmldb collection of this element.
	 * 
	 * @return the collection if available, elsewhere <code>null</code>.
	 * @throws ConnectionException
	 */
	public Collection getCollection() throws ConnectionException;

	/**
	 * @return the connection.
	 */
	public IConnection getConnection();

	/**
	 * Get a new {@link IDocumentItem} for given <code>name</code>.<br />
	 * Attention: If the document does not exist, it will not created.
	 * 
	 * @param name
	 *            the name of the document.
	 * @return the document
	 */
	public IDocumentItem getDocument(String name);

	/**
	 * @return the parent of this element.
	 */
	public IBrowseItem getParent();

	/**
	 * @return the absolute path of the collection
	 */
	public String getPath();

	/**
	 * Check, if the collection has children.
	 * 
	 * @return <code>true</code> if the collection has children, elsewhere
	 *         <code>false</code>.
	 * @throws ConnectionException
	 */
	public boolean hasChildren() throws ConnectionException;

	/**
	 * Check if the collection has documents.
	 * 
	 * @return <code>true</code> if the collection has documents, elsewhere
	 *         <code>false</code>.
	 * @throws ConnectionException
	 */
	public boolean hasDocuments() throws ConnectionException;

	/**
	 * @return <code>true</code> if the item is the root element, elsewhere
	 *         <code>false</code>.
	 */
	public boolean isRoot();

	/**
	 * @return <code>true</code> if the given <code>item</code> is a sub item of
	 *         this one, elsewhere <code>false</code>.
	 */
	public boolean contains(IBrowseItem item);
}