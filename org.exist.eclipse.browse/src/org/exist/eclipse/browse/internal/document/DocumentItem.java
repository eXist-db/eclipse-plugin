/**
 * DocumentItem.java
 */
package org.exist.eclipse.browse.internal.document;

import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentService;
import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

/**
 * An abstraction of a document from the xmldb.
 * 
 * @author Pascal Schmidiger
 */
public class DocumentItem implements IDocumentItem {

	private final String _name;
	private final IBrowseItem _parent;

	public DocumentItem(String name, IBrowseItem parent) {
		_name = name;
		_parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.exist.eclipse.browse.internal.views.listener.IDocumentItem#exists()
	 */
	public boolean exists() {
		boolean exist = false;
		if (IBrowseService.class.cast(_parent.getAdapter(IBrowseService.class))
				.check()) {
			try {
				return _parent.getCollection().getResource(_name) != null;
			} catch (Exception e) {
				return false;
			}
		}
		return exist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.exist.eclipse.browse.internal.views.listener.IDocumentItem#getName()
	 */
	public final String getName() {
		return _name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.exist.eclipse.browse.internal.views.listener.IDocumentItem#getResource
	 * ()
	 */
	public Resource getResource() throws ConnectionException {
		try {
			return getParent().getCollection().getResource(getName());
		} catch (XMLDBException e) {
			throw new ConnectionException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.exist.eclipse.browse.internal.views.listener.IDocumentItem#getParent
	 * ()
	 */
	public IBrowseItem getParent() {
		return _parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.exist.eclipse.browse.internal.views.listener.IDocumentItem#getPath()
	 */
	public String getPath() {
		return getParent().getPath() + "/" + getName();
	}

	@Override
	public String toString() {
		return getName();
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter.getName().equals(IDocumentService.class.getName())) {
			return new DocumentService(this);
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEquals = false;
		if (obj != null && obj instanceof DocumentItem) {
			DocumentItem item = DocumentItem.class.cast(obj);
			isEquals = item.getPath().equals(getPath());
			if (isEquals) {
				isEquals = item.getParent().equals(getParent());
			}
		}
		return isEquals;
	}
}
