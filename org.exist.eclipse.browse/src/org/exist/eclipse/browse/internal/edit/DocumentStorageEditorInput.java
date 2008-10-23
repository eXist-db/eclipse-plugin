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

	public IStorage getStorage() throws CoreException {
		return _storage;
	}

	public boolean exists() {
		return _storage.exists();
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return _storage.getName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return _storage.getToolTipText();
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter.getName().equals(IInputSave.class.getName())) {
			return _storage;
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEquals = false;
		if (obj instanceof DocumentStorageEditorInput) {
			isEquals = DocumentStorageEditorInput.class.cast(obj)._storage
					.equals(_storage);
		}
		return isEquals;
	}
}
