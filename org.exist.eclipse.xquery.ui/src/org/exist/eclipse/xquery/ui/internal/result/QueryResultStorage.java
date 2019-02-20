package org.exist.eclipse.xquery.ui.internal.result;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.eclipse.core.resources.IEncodedStorage;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * Implement {@link IStorage} interface to show a query result in editor.
 * 
 * @author Pascal Schmidiger
 */
public class QueryResultStorage implements IEncodedStorage {
	private static final String ENCODING_DEFAULT = "UTF-16";
	private Charset _encoding;
	private final ResultItem _item;

	public QueryResultStorage(ResultItem item) {
		_item = item;
		_encoding = Charset.forName(ENCODING_DEFAULT);
	}

	@Override
	public InputStream getContents() throws CoreException {
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(_item.getContent().getBytes(_encoding.displayName()));
		} catch (UnsupportedEncodingException e) {
			StringBuilder message = new StringBuilder(50).append("Error while loading text'");
			throw new CoreException(new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID, message.toString(), e));
		}
		return is;

	}

	@Override
	public String getCharset() throws CoreException {
		return _encoding.name();
	}

	@Override
	public IPath getFullPath() {
		return null;
	}

	@Override
	public String getName() {
		StringBuilder name = new StringBuilder();
		name.append(_item.getGroup()).append("_").append(_item.getUniqueNr()).append(" (")
				.append((_item.getIndex() + 1)).append(")");
		return name.toString();
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_item == null) ? 0 : _item.hashCode());
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
		QueryResultStorage other = (QueryResultStorage) obj;
		if (_item == null) {
			if (other._item != null)
				return false;
		} else if (!_item.equals(other._item))
			return false;
		return true;
	}

}
