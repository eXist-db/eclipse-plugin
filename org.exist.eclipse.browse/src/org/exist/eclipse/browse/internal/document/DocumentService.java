/**
 * DocumentService.java
 */
package org.exist.eclipse.browse.internal.document;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.widgets.Display;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.create.CreateDocumentException;
import org.exist.eclipse.browse.create.ICreateDocumentProvider;
import org.exist.eclipse.browse.document.DocumentCoordinator;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentService;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.exception.ConnectionException;

/**
 * Implementation of {@link IDocumentService}.
 * 
 * @author Pascal Schmidiger
 */
public class DocumentService implements IDocumentService {

	private final IDocumentItem _item;

	DocumentService(IDocumentItem item) {
		_item = item;
	}

	public boolean check() {
		boolean isOk = _item.exists();
		if (!isOk) {
			String message = "The document '" + _item + "' does not exist.";
			BrowsePlugin.getDefault().infoDialog("eXist", message);

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					DocumentCoordinator.getInstance().removed(_item);
				}
			});

		}
		return isOk;
	}

	public void create(IConfigurationElement providerElement)
			throws CreateDocumentException {
		Assert.isNotNull(providerElement);
		try {
			ICreateDocumentProvider provider = (ICreateDocumentProvider) providerElement
					.createExecutableExtension("class");
			provider.create(_item);
		} catch (Exception e) {
			throw new CreateDocumentException(_item, e);
		}
	}

	public void delete() throws ConnectionException {
		IManagementService service = IManagementService.class.cast(_item
				.getParent().getConnection().getAdapter(
						IManagementService.class));
		service.removeDocument(_item.getParent().getCollection(), _item
				.getName());
		DocumentCoordinator.getInstance().removed(_item);
	}

	public boolean move(IDocumentItem item) throws ConnectionException {
		boolean value = false;
		if (_item.exists() && !item.exists()) {
			IManagementService service = (IManagementService) _item.getParent()
					.getConnection().getAdapter(IManagementService.class);

			IDocumentItem renamedItem = _item;

			// firstly, rename the document if necessary
			if (!_item.getName().equals(item.getName())) {
				renamedItem = _item.getParent().getDocument(item.getName());
				service.renameResource(_item.getParent().getCollection(), _item
						.getName(), renamedItem.getName());
			}

			// secondary, move the document if necessary
			if (!renamedItem.getParent().equals(item.getParent())) {
				throw new RuntimeException("Not supported yet");
			}

			// Inform listener about the move
			DocumentCoordinator.getInstance().moved(_item, item);
		}

		return value;
	}
}
