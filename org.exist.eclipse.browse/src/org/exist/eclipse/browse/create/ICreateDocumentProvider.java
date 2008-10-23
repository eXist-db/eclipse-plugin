/**
 * 
 */
package org.exist.eclipse.browse.create;

import org.exist.eclipse.browse.document.IDocumentItem;

/**
 * Provider to create a new document. You have to register the implementation on
 * extension "org.exist.eclipse.browse.createdocument".
 * 
 * @author Pascal Schmidiger
 */
public interface ICreateDocumentProvider {
	/**
	 * Implement the creation of document in the xmldb.
	 * 
	 * @param item
	 *            which should be created.
	 * @throws CreateDocumentException
	 *             if the document could not created.
	 */
	public void create(IDocumentItem item) throws CreateDocumentException;
}
