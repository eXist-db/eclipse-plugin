package org.exist.eclipse.query.internal.edit;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

/**
 * This is a {@link IStorageEditorInput} implementation, which can you use for
 * starting any editor in eclipse to show a {@link QueryResultStorage}.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class QueryResultStorageEditorInput implements IStorageEditorInput {
	private final QueryResultStorage _storage;

	public QueryResultStorageEditorInput(QueryResultStorage storage) {
		_storage = storage;
	}

	public IStorage getStorage() throws CoreException {
		return _storage;
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
		return _storage.getName();
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	public boolean exists() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QueryResultStorageEditorInput) {
			return QueryResultStorageEditorInput.class.cast(obj)._storage
					.equals(_storage);
		}
		return super.equals(obj);
	}

}
