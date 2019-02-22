package org.exist.eclipse.xquery.ui.internal.result;

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_storage == null) ? 0 : _storage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryResultStorageEditorInput other = (QueryResultStorageEditorInput) obj;
		if (_storage == null) {
			if (other._storage != null)
				return false;
		} else if (!_storage.equals(other._storage))
			return false;
		return true;
	}

}
