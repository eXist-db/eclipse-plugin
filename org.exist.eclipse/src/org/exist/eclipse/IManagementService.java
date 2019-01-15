/**
 * IManagementService.java
 */
package org.exist.eclipse;

import org.eclipse.core.runtime.IAdaptable;
import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.Collection;

/**
 * Get the implementation over {@link IAdaptable#getAdapter(Class)} from
 * {@link IConnection}.
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IManagementService {
	/**
	 * Check if the connection is valid.
	 * 
	 * @return <code>true</code> if the connection is open and valid, elsewhere
	 *         <code>false</code>.
	 */
	public boolean check();

	/**
	 * Get the collection for the given <code>path</code>.
	 * 
	 * @param path
	 *            absolute path of the collection.
	 * @return the collection for the path or <code>null</code> if the
	 *         collection does not exist.
	 * @throws ConnectionException
	 */
	public Collection getCollection(String path) throws ConnectionException;

	/**
	 * Create a new collection for the given <code>name</code> in the given
	 * <code>collection</code>.
	 * 
	 * @param collection
	 *            the parent of the new collection.
	 * @param name
	 *            of the new collection.
	 * @return the created collection.
	 * @throws ConnectionException
	 */
	public Collection createCollection(Collection collection, String name)
			throws ConnectionException;

	/**
	 * Remove the given <code>collection</code>.
	 * 
	 * @param collection
	 *            which will removed.
	 */
	public void removeCollection(Collection collection)
			throws ConnectionException;

	/**
	 * Remove the given <code>document</code> in the given
	 * <code>collection</code>.
	 * 
	 * @param collection
	 *            in which the document should remove
	 * @param document
	 *            which should remove
	 * @throws ConnectionException
	 */
	public void removeDocument(Collection collection, String document)
			throws ConnectionException;

	/**
	 * Rename the collection <code>fromPath</code> to <code>toPath</code>.
	 * 
	 * @param fromPath
	 *            Source path.
	 * @param toPath
	 *            Source destination.
	 * @throws ConnectionException
	 */
	public void rename(String fromPath, String toPath)
			throws ConnectionException;

	/**
	 * Move the collection <code>fromPath</code> to <code>toPath</code>.
	 * 
	 * @param fromPath
	 *            Source path.
	 * @param toPath
	 *            Source destination.
	 * @throws ConnectionException
	 */
	public void move(String fromPath, String toPath) throws ConnectionException;

	/**
	 * Rename the document <code>fromName</code> from given collection
	 * <code>collection</code> to <code>toName</code>.
	 * 
	 * @param collection
	 *            where the document is.
	 * @param fromName
	 *            Source name.
	 * @param toName
	 *            New name.
	 * @throws ConnectionException
	 */
	public void renameResource(Collection collection, String fromName,
			String toName) throws ConnectionException;
}
