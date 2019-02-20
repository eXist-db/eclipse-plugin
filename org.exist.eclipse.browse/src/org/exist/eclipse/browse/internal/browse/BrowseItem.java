/**
 * TreeParent.java
 */
package org.exist.eclipse.browse.internal.browse;

import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
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
public class BrowseItem implements IBrowseItem {
	private static final String DELIMITER = "/";

	private final String _path;
	private final IConnection _connection;

	private Collection _collection;
	private IBrowseItem _parent;

	/**
	 * Create a new item.
	 * 
	 * @param connection
	 *            the connection object
	 * @param path
	 *            the actual collection path
	 */
	public BrowseItem(IConnection connection, String path) {
		_connection = connection;
		_path = path;
	}

	@Override
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

	@Override
	public boolean exists() {
		try {
			return getCollection() != null;
		} catch (ConnectionException e) {
			// ignore
			return false;
		}
	}

	@Override
	public final Collection getCollection() throws ConnectionException {
		if (_collection == null) {
			IManagementService service = _connection.getAdapter(IManagementService.class);
			_collection = service.getCollection(getPath());
		}
		return _collection;
	}

	@Override
	public IDocumentItem getDocument(String name) {
		return new DocumentItem(name, this);
	}

	@Override
	public IBrowseItem getParent() {
		return _parent;
	}

	@Override
	public String getPath() {
		return _path;
	}

	@Override
	public boolean hasChildren() throws ConnectionException {
		boolean has = false;
		try {
			has = getCollection().getChildCollectionCount() > 0;
		} catch (XMLDBException e) {
			throw new ConnectionException(e);
		}
		return has;
	}

	@Override
	public boolean hasDocuments() throws ConnectionException {
		boolean has = false;
		try {
			has = getCollection().getResourceCount() > 0;
		} catch (XMLDBException e) {
			throw new ConnectionException(e);
		}
		return has;
	}

	@Override
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

	@Override
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
	public int hashCode() {
		return getPath().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof BrowseItem)) {
			return false;
		}
		BrowseItem item = (BrowseItem) obj;
		return getPath().equals(item.getPath())
				&& getConnection().equals(item.getConnection());
	}

	@Override
	public IConnection getConnection() {
		return _connection;
	}

	@Override
	public <A> A getAdapter(Class<A> adapter) {
		if (adapter.isAssignableFrom(BrowseService.class)) {
			return adapter.cast(new BrowseService(this));
		}
		return null;
	}

	@Override
	public int compareTo(IBrowseItem o) {
		return getPath().compareTo(o.getPath());
	}

	@Override
	public boolean contains(IBrowseItem item) {
		return item != null && item.getPath().startsWith(getPath() + DELIMITER);
	}
}
