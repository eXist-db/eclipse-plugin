/**
 * BrowseService.java
 */
package org.exist.eclipse.browse.internal.browse;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.widgets.Display;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.URIUtils;
import org.exist.eclipse.browse.browse.BrowseCoordinator;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.XMLDBException;

/**
 * Implementation of {@link IBrowseService}.
 * 
 * @author Pascal Schmidiger
 */
public class BrowseService implements IBrowseService {

	private final BrowseItem _item;

	BrowseService(BrowseItem item) {
		_item = item;
	}

	@Override
	public boolean check() {
		boolean isOk = _item.exists();
		if (!isOk) {
			if (IBrowseService.class.cast(_item.getParent().getAdapter(IBrowseService.class)).check()) {
				String message = "The collection '" + _item.getPath() + "' does not exist.";
				BrowsePlugin.getDefault().infoDialog("eXist", message);
				fireRemoved();
			}
		}
		return isOk;
	}

	@Override
	public void create() throws ConnectionException {
		if (!_item.getParent().exists()) {
			IBrowseService service = _item.getParent().getAdapter(IBrowseService.class);
			service.create();
		}
		// create this collection
		IManagementService service = _item.getConnection().getAdapter(IManagementService.class);
		service.createCollection(_item.getParent().getCollection(), _item.getName());

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				BrowseCoordinator.getInstance().added(_item);
			}
		});
	}

	@Override
	public void delete() throws ConnectionException {
		IManagementService service = _item.getConnection().getAdapter(IManagementService.class);
		service.removeCollection(_item.getCollection());
		fireRemoved();
	}

	@Override
	public void refresh() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				BrowseCoordinator.getInstance().refresh(_item);
			}
		});
	}

	@Override
	public Set<IBrowseItem> getChildren(boolean self, boolean recursive) throws ConnectionException {
		Set<IBrowseItem> result = new TreeSet<>();
		if (self) {
			result.add(_item);
		}
		String[] children;
		try {
			children = _item.getCollection().listChildCollections();
			for (String child : children) {
				IBrowseItem childItem = _item.getChild(URIUtils.urlDecodeUtf8(child));
				result.add(childItem);
				if (recursive) {
					Set<IBrowseItem> childResult = IBrowseService.class.cast(childItem.getAdapter(IBrowseService.class))
							.getChildren(false, recursive);
					result.addAll(childResult);
				}
			}
		} catch (XMLDBException e) {
			throw new ConnectionException(e);
		}
		return result;
	}

	@Override
	public boolean move(IBrowseItem item) throws ConnectionException {
		boolean value = false;
		if (_item.exists() && !item.exists()) {
			IManagementService service = _item.getConnection().getAdapter(IManagementService.class);
			IBrowseItem renamedItem = _item;
			// firstly, rename the collection if necessary
			if (!_item.getName().equals(item.getName())) {
				renamedItem = _item.getParent().getChild(item.getName());
				service.rename(_item.getPath(), renamedItem.getName());
			}
			// secondary, move the collection if necessary
			if (!renamedItem.getParent().equals(item.getParent())) {
				service.move(renamedItem.getPath(), item.getParent().getPath());
			}
			// Inform listener about the move
			BrowseCoordinator.getInstance().moved(_item, item);
		}

		return value;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// /////////////////////////////////////////////////////////////////////////////////////////////
	private void fireRemoved() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				BrowseCoordinator.getInstance().removed(new IBrowseItem[] { _item });
			}
		});
	}
}
