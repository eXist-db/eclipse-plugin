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
	private final String _basePath;
	private final IBrowseItem _parent;

	public DocumentItem(String name, IBrowseItem parent) {
		_name = name;
		_basePath = parent.getPath().concat("/");
		_parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.exist.eclipse.browse.internal.views.listener.IDocumentItem#exists()
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
	public IBrowseItem getParent() {
		return _parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.exist.eclipse.browse.internal.views.listener.IDocumentItem#getPath()
	 */
	@Override
	public String getPath() {
		return _basePath.concat(_name);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter.getName().equals(IDocumentService.class.getName())) {
			return new DocumentService(this);
		}
		return null;
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof DocumentItem)) {
			return false;
		}
		DocumentItem other = DocumentItem.class.cast(obj);
		return getPath().equals(other.getPath())
				&& getParent().equals(other.getParent());
	}
}
