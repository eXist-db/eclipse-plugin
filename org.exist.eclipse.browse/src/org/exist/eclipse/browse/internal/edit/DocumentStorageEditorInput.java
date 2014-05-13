/**
 * DocumentStorageEditorInput.java
 */
package org.exist.eclipse.browse.internal.edit;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

/**
 * This is a {@link IStorageEditorInput} implementation, which can you use for
 * starting any editor in eclipse.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class DocumentStorageEditorInput implements IStorageEditorInput {
	private final DocumentStorage _storage;

	public DocumentStorageEditorInput(DocumentStorage storage) {
		_storage = storage;
	}

	@Override
	public IStorage getStorage() throws CoreException {
		return _storage;
	}

	@Override
	public boolean exists() {
		return _storage.exists();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return _storage.getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return _storage.getToolTipText();
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter.getName().equals(IInputSave.class.getName())) {
			return _storage;
		}
		return null;
	}

	@Override
	public int hashCode() {
		return _storage.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof DocumentStorageEditorInput)) {
			return false;
		}
		DocumentStorageEditorInput other = (DocumentStorageEditorInput) obj;
		return _storage.equals(other._storage);
	}
}
