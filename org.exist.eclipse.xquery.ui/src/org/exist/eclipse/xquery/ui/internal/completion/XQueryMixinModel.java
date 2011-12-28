/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.completion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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
	public static final String MORE = "...";

	private static XQueryMixinModel _instance;
	/**
	 * From prefix ('fn') to its methods.
	 */
	private Map<String, List<IXQueryMethod>> _cache;

	public static XQueryMixinModel getInstance() {
		if (_instance == null) {
			_instance = new XQueryMixinModel();
		}
		return _instance;
	}

	private XQueryMixinModel() {
	}

	/**
	 * @param prefix
	 *            selection attribute
	 * @return all methods for the prefix
	 */
	public List<IXQueryMethod> getMethods(String prefix) {
		List<IXQueryMethod> result = new ArrayList<IXQueryMethod>();

		fillMethods(prefix, loadFile(PREFIX_FN), result);
		fillMethods(prefix, loadFile(PREFIX_OP), result);

		return result;
	}

	private void fillMethods(String prefix, List<IXQueryMethod> list,
			List<IXQueryMethod> result) {
		for (IXQueryMethod method : list) {
			String name = method.getName();
			name = XQueryCompletionEngine.removeFnPrefix(name);
			if (name.startsWith(prefix)) {
				result.add(method);
			}
		}
	}

	private void parseAndAddMethod(String methodString,
			List<IXQueryMethod> methods) {

		int posLeft = methodString.indexOf('(');
		int posAs = methodString.lastIndexOf("as");
		int rightFrom;
		if (posAs != -1) {
			rightFrom = posAs;
		} else {
			rightFrom = methodString.length() - 1;
		}

		int posR = methodString.lastIndexOf(')', rightFrom);
		String name = methodString.substring(0, posLeft);
		XQueryMethod method = new XQueryMethod(name.trim());
		String paramString = methodString.substring(posLeft + 1, posR);
		if (!paramString.isEmpty()) {
			StringTokenizer token = new StringTokenizer(paramString, ",");
			while (token.hasMoreTokens()) {
				String param = token.nextToken().trim();
				if (param.length() > 0) {
					String pName;
					String pType;
					if (param.equals(MORE)) {
						pType = "";
						pName = param;
					} else {
						int pAs = param.indexOf("as");
						if (pAs == -1) {
							pName = param.substring(0, param.indexOf(' '));
							pType = "";
						} else {
							pName = param.substring(0, pAs).trim();
							pType = param.substring(pAs + "as".length() + 1)
									.trim();
						}
					}
					method.addParameter(pName, pType);
				}
			}
		}
		methods.add(method);
	}

	private List<IXQueryMethod> loadFile(String prefix) {
		Map<String, List<IXQueryMethod>> cache = getCache();
		List<IXQueryMethod> methods = cache.get(prefix);
		if (methods == null) {
			methods = new ArrayList<IXQueryMethod>();
			BufferedReader reader = null;
			try {
				InputStream inputStream = getFileStream(getFilePath(prefix));
				if (inputStream != null) {
					reader = new BufferedReader(new InputStreamReader(
							inputStream));
					String methodString;
					while ((methodString = reader.readLine()) != null) {
						parseAndAddMethod(methodString, methods);
					}

					cache.put(prefix, methods);
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
		}
		return methods;
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

	public Path getFilePath(String prefix) {
		return new Path("resources/" + prefix + ".functions");
	}

	private Map<String, List<IXQueryMethod>> getCache() {
		if (_cache == null) {
			_cache = new HashMap<String, List<IXQueryMethod>>();
		}
		return _cache;
	}
}
