/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.completion;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.completion.IXQueryMethod;

/**
 * Container, which load the static xquery functions from files.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryMixinModel {

	public static final String PREFIX_OP = "op";
	public static final String PREFIX_FN = "fn";

	private static XQueryMixinModel _instance;
	/**
	 * From prefix ('fn') to its methods.
	 */
	private Map<String, LibDocument> _cache;

	public static XQueryMixinModel getInstance() {
		if (_instance == null) {
			_instance = new XQueryMixinModel();
		}
		return _instance;
	}

	private XQueryMixinModel() {
		loadFile(PREFIX_FN, "http://www.w3.org/2005/xpath-functions");
		loadFile(PREFIX_OP, "http://www.w3.org/2005/xpath-functions");
	}

	/**
	 * @param prefix
	 *            selection attribute
	 * @return all methods for the prefix
	 */
	public List<IXQueryMethod> getMethods(String prefix) {
		List<IXQueryMethod> result = new ArrayList<IXQueryMethod>();

		getCache().get(PREFIX_FN).fillMethods(prefix, result);
		getCache().get(PREFIX_OP).fillMethods(prefix, result);

		return result;
	}

	private LibDocument loadFile(String prefix, String namespaceUri) {
		Map<String, LibDocument> cache = getCache();
		LibDocument doc = cache.get(prefix);
		if (doc == null) {
			Path path = getFilePath(prefix);
			InputStream in = getFileStream(path);
			doc = new LibDocument(prefix, namespaceUri, path);
			if (in != null) {
				try {
					doc.parseMethods(in);
					cache.put(prefix, doc);
				} catch (Exception e) {
					// ignore
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}
		return doc;
	}

	/**
	 * @return nullable if none or an error
	 */
	public InputStream getFileStream(Path path) {
		InputStream result = null;
		try {
			result = FileLocator.openStream(XQueryUI.getDefault().getBundle(),
					path, false);
		} catch (Exception e) {
			// ignore
		}
		return result;
	}

	/**
	 * @return nullable if none
	 */
	public LibDocument getLibDocument(String prefix) {
		return getCache().get(prefix);
	}

	private Path getFilePath(String prefix) {
		return new Path("resources/" + prefix + ".functions");
	}

	private Map<String, LibDocument> getCache() {
		if (_cache == null) {
			_cache = new HashMap<String, LibDocument>();
		}
		return _cache;
	}
}
