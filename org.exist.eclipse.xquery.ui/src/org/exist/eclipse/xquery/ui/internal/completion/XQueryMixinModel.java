/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.completion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
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
		List<IXQueryMethod> result = new ArrayList<>();

		getCache().get(PREFIX_FN).fillMethods(prefix, result);
		getCache().get(PREFIX_OP).fillMethods(prefix, result);

		return result;
	}

	private LibDocument loadFile(String prefix, String namespaceUri) {
		Map<String, LibDocument> cache = getCache();
		LibDocument doc = cache.get(prefix);
		if (doc == null) {
			Path path = getFilePath(prefix);
			doc = new LibDocument(prefix, namespaceUri, path);
			XQueryUI plugin = XQueryUI.getDefault();
			try (InputStream in = FileLocator.openStream(plugin.getBundle(),
					path, false)) {
				doc.parseMethods(in);
				cache.put(prefix, doc);
			} catch (Exception e) {
				plugin.getLog().log(
						new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID, String
								.format("Unable to lobrary %s:%s", prefix,
										namespaceUri), e));
			}
		}
		return doc;
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
			_cache = new HashMap<>();
		}
		return _cache;
	}
}
