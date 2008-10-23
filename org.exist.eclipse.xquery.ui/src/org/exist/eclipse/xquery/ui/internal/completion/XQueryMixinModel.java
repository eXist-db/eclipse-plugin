/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.completion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

	private static XQueryMixinModel _instance;

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
		List<IXQueryMethod> methods = new ArrayList<IXQueryMethod>();
		List<String> list = loadFile(new Path("resources/fn.functions"));
		fillMethods(methods, list, prefix);
		list = loadFile(new Path("resources/op.functions"));
		fillMethods(methods, list, prefix);

		return methods;
	}

	private void fillMethods(List<IXQueryMethod> methods, List<String> list,
			String prefix) {
		for (String methodString : list) {
			if (methodString.startsWith(prefix)) {
				String name = null;
				name = methodString.substring(0, methodString.indexOf("("));
				XQueryMethod method = new XQueryMethod(name.trim());
				String paramString = methodString.substring(methodString
						.indexOf("("), methodString.indexOf(")"));
				if (paramString.length() > 1) {
					StringTokenizer token = new StringTokenizer(paramString,
							",");
					while (token.hasMoreTokens()) {
						String param = token.nextToken();
						param = param.substring(param.lastIndexOf(" "));
						method.addParameter(param.trim());
					}
				}
				methods.add(method);
			}
		}
	}

	private List<String> loadFile(Path path) {
		List<String> methods = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			InputStream inputStream = FileLocator.openStream(XQueryUI
					.getDefault().getBundle(), path, false);
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String keyword;
			while ((keyword = reader.readLine()) != null) {
				methods.add(keyword);
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
		return methods;
	}

}
