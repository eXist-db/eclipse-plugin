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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
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
		_keywords = new ArrayList<>();
	}

	public void load() {
		if (!_loaded) {
			Path path = new Path("resources/keywords.csv");
			XQueryUI plugin = XQueryUI.getDefault();
			try (InputStream inputStream = FileLocator.openStream(
					plugin.getBundle(), path, false);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(inputStream))) {
				String keyword;
				while ((keyword = reader.readLine()) != null) {
					_keywords.add(keyword);
				}
			} catch (IOException e) {
				plugin.getLog().log(
						new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID,
								"Unable to load " + path, e));
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
