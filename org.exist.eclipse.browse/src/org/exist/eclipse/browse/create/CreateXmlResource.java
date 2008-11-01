/**
 * 
 */
package org.exist.eclipse.browse.create;

import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.xquery.util.URIUtils;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.XMLResource;

/**
 * Provider implementation to create xml resource.
 * 
 * @author Pascal Schmidiger
 */
public class CreateXmlResource implements ICreateDocumentProvider {

	public void create(IDocumentItem item) throws CreateDocumentException {
		XMLResource result = null;
		try {
			checkFileName(item);
			Collection collection = item.getParent().getCollection();
			result = (XMLResource) collection.createResource(URIUtils
					.urlEncodeUtf8(item.getName()), XMLResource.RESOURCE_TYPE);
			result.setContent(getContent()); //$NON-NLS-1$
			collection.storeResource(result);
			collection.close();
		} catch (Exception e) {
			throw new CreateDocumentException(item, e);
		}
	}

	protected void checkFileName(IDocumentItem item) {
		// no checkins
	}

	protected String getContent() {
		return "<template></template>";
	}

}