package org.exist.eclipse.query.internal.edit;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.eclipse.core.resources.IEncodedStorage;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.preferences.ExistPreferences;
import org.exist.eclipse.query.internal.QueryPlugin;

/**
 * Implement {@link IStorage} interface to show a query result in editor.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class QueryResultStorage implements IEncodedStorage {

	private Charset _encoding;
	private final String _content;
	private final String _id;

	public QueryResultStorage(String id, String content) {
		_id = id;
		_encoding = ExistPreferences.getEncoding();
		_content = content;
	}

	public InputStream getContents() throws CoreException {
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(_content.getBytes(_encoding
					.displayName()));
		} catch (UnsupportedEncodingException e) {
			StringBuilder message = new StringBuilder(50)
					.append("Error while loading text'");
			throw new CoreException(new Status(Status.ERROR, QueryPlugin
					.getId(), message.toString(), e));
		}
		return is;

	}

	public String getCharset() throws CoreException {
		return _encoding.name();
	}

	public IPath getFullPath() {
		return null;
	}

	public String getName() {
		return "Query Result " + _id;
	}

	public boolean isReadOnly() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof QueryResultStorage) {
			return QueryResultStorage.class.cast(obj)._id.equals(_id);
		}
		return super.equals(obj);
	}
}
