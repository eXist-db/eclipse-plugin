/**
 * TreeParent.java
 */
package org.exist.eclipse.browse.internal.browse;

import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.internal.document.DocumentItem;
import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

/**
 * To stand for a item in the browse tree. Contain also the collection which is
 * the link to the XMLDB.
 * 
 * @author Pascal Schmidiger
 */
public class BrowseItem implements IBrowseItem, Comparable<IBrowseItem> {
	private static final String DELIMITER = "/";
	private IBrowseItem _parent;
	private final String _path;
	private final IConnection _connection;

	/**
	 * Create a new item.
	 * 
	 * @param name
	 *            of the item.
	 * @param collection
	 *            a valid collection from the XMLDB.
	 */
	public BrowseItem(IConnection connection, String path) {
		_connection = connection;
		_path = path;
	}

	public IBrowseItem getChild(String name) {
		if (name.startsWith(DELIMITER)) {
			name.substring(1);
		}
		int index = name.indexOf(DELIMITER);
		String theName = name;
		if (index != -1) {
			theName = name.substring(0, index);
		}
		String path = getPath() + DELIMITER + theName;
		BrowseItem child = new BrowseItem(_connection, path);
		child.setParent(this);
		if (index == -1) {
			return child;
		} else {
			return child.getChild(name.substring(index + 1));
		}
	}

	public boolean exists() {
		try {
			return getCollection() != null;
		} catch (ConnectionException e) {
			return false;
		}
	}

	public final Collection getCollection() throws ConnectionException {
		IManagementService service = IManagementService.class.cast(_connection
				.getAdapter(IManagementService.class));
		return service.getCollection(getPath());
	}

	public IDocumentItem getDocument(String name) {
		return new DocumentItem(name, this);
	}

	public IBrowseItem getParent() {
		return _parent;
	}

	public String getPath() {
		return _path;
	}

	public boolean hasChildren() throws ConnectionException {
		boolean has = false;
		try {
			has = getCollection().getChildCollectionCount() > 0;
		} catch (XMLDBException e) {
			throw new ConnectionException(e);
		}
		return has;
	}

	public boolean hasDocuments() throws ConnectionException {
		boolean has = false;
		try {
			has = getCollection().getResourceCount() > 0;
		} catch (XMLDBException e) {
			throw new ConnectionException(e);
		}
		return has;
	}

	public boolean isRoot() {
		return getParent() == null
				|| (getParent() instanceof BrowseItemInvisible);

	}

	/**
	 * Set the given <code>parent</code>.
	 * 
	 * @param parent
	 */
	public void setParent(IBrowseItem parent) {
		_parent = parent;
	}

	public String getName() {
		int index = getPath().lastIndexOf(DELIMITER);
		if (index == -1) {
			return getPath();
		} else {
			return getPath().substring(index + 1);
		}
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEquals = false;
		if (obj != null && obj instanceof BrowseItem) {
			BrowseItem item = BrowseItem.class.cast(obj);
			isEquals = item.getPath().equals(getPath());
			if (isEquals) {
				isEquals = item.getConnection().equals(getConnection());
			}
		}
		return isEquals;
	}

	public IConnection getConnection() {
		return _connection;
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter.getName().equals(IBrowseService.class.getName())) {
			return new BrowseService(this);
		}
		return null;
	}

	public int compareTo(IBrowseItem o) {
		return getPath().compareTo(o.getPath());
	}

	public boolean contains(IBrowseItem item) {
		return item != null && item.getPath().startsWith(getPath() + DELIMITER);
	}
}
