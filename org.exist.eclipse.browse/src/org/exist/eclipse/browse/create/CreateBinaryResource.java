/**
 * 
 */
package org.exist.eclipse.browse.create;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.preferences.ExistPreferences;
import org.exist.xquery.util.URIUtils;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.BinaryResource;

/**
 * Provider implementation to create binary resource.
 * 
 * @author Pascal Schmidiger
 */
public class CreateBinaryResource implements ICreateDocumentProvider {

	public void create(IDocumentItem item, String content)
			throws CreateDocumentException {
		try {
			checkFileName(item);
			Collection collection = item.getParent().getCollection();
			BinaryResource result = (BinaryResource) collection.createResource(
					URIUtils.urlEncodeUtf8(item.getName()),
					BinaryResource.RESOURCE_TYPE);
			if (content == null) {
				content = "";
			}
			result.setContent(content.getBytes(ExistPreferences.getEncoding()
					.name()));
			collection.storeResource(result);
			collection.close();
		} catch (Exception e) {
			CreateDocumentException createDocumentException = new CreateDocumentException(
					item, e);
			IStatus status = new Status(Status.ERROR, BrowsePlugin.getId(),
					"Failure while create binary resource.",
					createDocumentException);
			BrowsePlugin.getDefault().getLog().log(status);
			throw createDocumentException;
		}
	}

	protected void checkFileName(IDocumentItem item) {
		// no checkins
	}

}
