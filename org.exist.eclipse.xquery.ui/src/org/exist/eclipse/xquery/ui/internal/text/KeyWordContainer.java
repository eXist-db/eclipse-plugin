/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * Container, which load from the file "resources/keywords.csv" the keywords.
 * 
 * @author Pascal Schmidiger
 */
public final class KeyWordContainer {
	private List<String> _keywords;
	private boolean _loaded;

	public KeyWordContainer() {
		_keywords = new ArrayList<String>();
	}

	public void load() {
		if (!_loaded) {
			BufferedReader reader = null;
			try {
				InputStream inputStream = FileLocator.openStream(XQueryUI
						.getDefault().getBundle(), new Path(
						"resources/keywords.csv"), false);
				reader = new BufferedReader(new InputStreamReader(inputStream));
				String keyword;
				while ((keyword = reader.readLine()) != null) {
					_keywords.add(keyword);
				}
			} catch (IOException e) {
				// ignore
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
					}
				}
			}
			_loaded = true;
		}
	}

	public void reload() {
		_loaded = false;
		load();
	}

	public final List<String> getKeyWords() {
		return _keywords;
	}
}
