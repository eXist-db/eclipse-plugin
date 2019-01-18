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

	@Override
	public IStorage getStorage() throws CoreException {
		return _storage;
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
		return _storage.getName();
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public int hashCode() {
		return _storage.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof QueryResultStorageEditorInput)) {
			return false;
		}
		return QueryResultStorageEditorInput.class.cast(obj)._storage
				.equals(_storage);
	}

}
