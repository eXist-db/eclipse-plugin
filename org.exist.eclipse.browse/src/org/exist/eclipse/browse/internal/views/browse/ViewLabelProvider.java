/**
 * ViewLabelProvider.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * The responsibility of this class is the presentation of the tree.<br />
 * For example: The parent needs a folder icon and the document an other icon.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ViewLabelProvider extends LabelProvider {
	private Image _imgFolder;
	private Image _imgConnectionOpen;
	private Image _imgConnectionClose;

	@Override
	public String getText(Object obj) {
		String text;
		if (obj instanceof IBrowseItem || obj instanceof IConnection) {
			text = obj.toString();
		} else {
			text = "pending...";
		}
		return text;
	}

	@Override
	public Image getImage(Object obj) {
		if (obj instanceof IBrowseItem) {
			return getFolderImage();
		} else if (obj instanceof IConnection) {
			if (IConnection.class.cast(obj).isOpen()) {
				return getConnectionOpenImage();
			} else {
				return getConnectionCloseImage();
			}
		} else {
			return null;
		}
	}

	//
	// private methods
	//
	private Image getFolderImage() {
		if (_imgFolder == null) {
			_imgFolder = BrowsePlugin.getImageDescriptor(
					"icons/folder_icon.png").createImage();
		}
		return _imgFolder;
	}

	private Image getConnectionOpenImage() {
		if (_imgConnectionOpen == null) {
			_imgConnectionOpen = BrowsePlugin.getImageDescriptor(
					"icons/connection_open.png").createImage();
		}
		return _imgConnectionOpen;
	}

	private Image getConnectionCloseImage() {
		if (_imgConnectionClose == null) {
			_imgConnectionClose = BrowsePlugin.getImageDescriptor(
					"icons/connection_close.png").createImage();
		}
		return _imgConnectionClose;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (_imgFolder != null) {
			_imgFolder.dispose();
		}

		if (_imgConnectionClose != null) {
			_imgConnectionClose.dispose();
		}

		if (_imgConnectionOpen != null) {
			_imgConnectionOpen.dispose();
		}
	}
}
