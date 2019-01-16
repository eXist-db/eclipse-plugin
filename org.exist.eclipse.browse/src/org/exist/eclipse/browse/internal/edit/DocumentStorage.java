/**
 * DocumentStorage.java
 */
package org.exist.eclipse.browse.internal.edit;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.eclipse.core.resources.IEncodedStorage;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentService;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.preferences.ExistPreferences;
import org.xmldb.api.base.Resource;
import org.xmldb.api.modules.XMLResource;

/**
 * Implement {@link IStorage} interface for a document in the eXist XMLDB.
 * 
 * @author Pascal Schmidiger
 */
public class DocumentStorage implements IEncodedStorage, IInputSave {
	private final IDocumentItem _item;

	private boolean _corrupt;
	private IPath _path;
	private Charset _encoding;

	public DocumentStorage(IDocumentItem item) {
		_item = item;
		_encoding = ExistPreferences.getEncoding();
		_corrupt = false;
	}

	@Override
	public InputStream getContents() throws CoreException {
		if (check()) {
			InputStream is = null;
			try {
				Resource resource = _item.getResource();
				if (resource.getResourceType()
						.equals(XMLResource.RESOURCE_TYPE)) {
					String content = String.class.cast(resource.getContent());
					is = new ByteArrayInputStream(content.getBytes(_encoding
							.name()));
				} else {
					String content = new String(byte[].class.cast(resource
							.getContent()), _encoding.name());
					is = new ByteArrayInputStream(content.getBytes(_encoding
							.name()));
				}
			} catch (UnsupportedEncodingException e) {
				StringBuilder message = new StringBuilder(50)
						.append("Error while encode resource '").append(_item)
						.append("' with '").append(_encoding).append('\'');
				throw new CoreException(new Status(IStatus.ERROR,
						BrowsePlugin.getId(), message.toString(), e));
			} catch (Exception e) {
				StringBuilder message = new StringBuilder(50)
						.append("Error while loading resource '")
						.append(_item.getName()).append('\'');
				throw new CoreException(new Status(IStatus.ERROR,
						BrowsePlugin.getId(), message.toString(), e));
			}
			return is;
		} else {
			throw new CoreException(new Status(IStatus.ERROR,
					BrowsePlugin.getId(), " Error while getting contents."));
		}
	}

	@Override
	public IPath getFullPath() {
		if (_path == null) {
			_path = new Path("", _item.getPath());
		}
		return _path;
	}

	@Override
	public String getName() {
		return _item.getName() + " ("
				+ _item.getParent().getConnection().getName() + ")";
	}

	public String getToolTipText() {
		return _item.getParent().getPath() + "/" + _item.getName() + " ("
				+ _item.getParent().getConnection().getName() + ")";
	}

	@Override
	public boolean isReadOnly() {
		return _corrupt;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public void setContents(IProgressMonitor monitor, IDocument document)
			throws CoreException {
		monitor.beginTask("save document '" + _item.getName() + "'", 1);
		try {
			if (check()) {
				try {
					Resource resource = _item.getParent().getCollection()
							.getResource(_item.getName());
					if (resource.getResourceType().equals(
							XMLResource.RESOURCE_TYPE)) {
						resource.setContent(document.get());
					} else {
						resource.setContent(document.get().getBytes(
								_encoding.name()));
					}
					_item.getParent().getCollection().storeResource(resource);
				} catch (UnsupportedEncodingException e) {
					throw new CoreException(new Status(IStatus.ERROR,
							BrowsePlugin.getId(), "Problem with encoding"));
				} catch (Exception e) {
					throw new CoreException(new Status(IStatus.ERROR,
							BrowsePlugin.getId(),
							"Could not store the document"));
				}
			} else {
				_corrupt = true;
				throw new CoreException(new Status(IStatus.ERROR,
						BrowsePlugin.getId(), "Could not store the document"));
			}
		} finally {
			monitor.done();
		}
	}

	@Override
	public String getCharset() throws CoreException {
		return _encoding.name();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof DocumentStorage)) {
			return false;
		}
		DocumentStorage other = DocumentStorage.class.cast(obj);
		return _corrupt == other._corrupt && _item.equals(other._item);
	}

	protected final boolean exists() {
		return _item.exists();
	}

	// //////////////////////////////////////////////////////////////////////////
	// //////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	// //////////////////
	private boolean check() {
		if (!_corrupt) {
			IBrowseService bService = (IBrowseService) _item.getParent()
					.getAdapter(IBrowseService.class);
			IDocumentService dService = (IDocumentService) _item
					.getAdapter(IDocumentService.class);
			return IManagementService.class.cast(
					_item.getParent().getConnection()
							.getAdapter(IManagementService.class)).check()
					&& bService.check() && dService.check();
		} else {
			return false;
		}
	}
}
