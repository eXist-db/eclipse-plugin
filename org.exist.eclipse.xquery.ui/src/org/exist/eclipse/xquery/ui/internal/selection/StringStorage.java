package org.exist.eclipse.xquery.ui.internal.selection;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Used to open a String (i.e. contents of external file) in an editor.
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class StringStorage implements IStorage {
	private final String _name;
	private final String _content;
	private final Path _path;

	public StringStorage(String name, Path path, String content) {
		_name = name;
		_path = path;
		_content = content;
	}

	@Override
	public boolean equals(Object that) {
		return that == this || that instanceof StringStorage
				&& ((StringStorage) that).getFullPath().equals(getFullPath());
	}

	@Override
	public int hashCode() {
		return getFullPath().hashCode();
	}

	public Object getAdapter(@SuppressWarnings("unchecked") Class adapter) {
		return null;
	}

	public boolean isReadOnly() {
		return true;
	}

	public String getName() {
		return _name;
	}

	public IPath getFullPath() {
		return _path;
	}

	public InputStream getContents() throws CoreException {
		try {
			return new ByteArrayInputStream(_content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}