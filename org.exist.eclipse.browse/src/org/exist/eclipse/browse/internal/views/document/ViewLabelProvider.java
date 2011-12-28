/**
 * ViewLabelProvider.java
 */
package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * The responsibility of this class is the presentation of the table.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ViewLabelProvider extends LabelProvider {
	private Image _imgDocument;
	private Image _imgFolder;

	@Override
	public String getText(Object element) {
		if (element instanceof IDocumentItem) {
			return element.toString();
		} else if (element instanceof IBrowseItem) {
			return IBrowseItem.class.cast(element).getPath();
		}
		return "Pending...";

	}

	public Image getImage(Object obj) {
		if (obj instanceof IDocumentItem) {
			return getDocumentImage();
		} else if (obj instanceof IBrowseItem) {
			return getFolderImage();
		}
		return null;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// /////////////////////////////////////////////////////////////////////////////////////////////
	private Image getDocumentImage() {
		if (_imgDocument == null) {
			_imgDocument = BrowsePlugin.getImageDescriptor(
					"icons/document_icon.png").createImage();
		}
		return _imgDocument;
	}

	private Image getFolderImage() {
		if (_imgFolder == null) {
			_imgFolder = BrowsePlugin.getImageDescriptor(
					"icons/folder_icon.png").createImage();
		}
		return _imgFolder;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (_imgDocument != null) {
			_imgDocument.dispose();
		}
		if (_imgFolder != null) {
			_imgFolder.dispose();
		}
	}
}
