package org.exist.eclipse.xquery.ui.internal.completion;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.exist.eclipse.xquery.ui.completion.IXQueryMethod;

/**
 * A wrapper for a static xquery function.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryMethod implements IXQueryMethod {

	private List<String> _parameterNames;
	private List<String> _parameterTypes;
	private final String _name;
	private final String _signature;
	private final String _comment;

	public XQueryMethod(String name, String signature, String comment) {
		_name = name;
		_signature = signature;
		_comment = comment;
	}

	@Override
	public int getFlags() {
		return Modifier.PUBLIC;
	}

	@Override
	public String[] getParameterNames() {
		parseSignature();
		return _parameterNames.toArray(new String[_parameterNames.size()]);
	}

	@Override
	public String[] getParameterTypes() {
		parseSignature();
		return _parameterTypes.toArray(new String[_parameterTypes.size()]);
	}

	private void parseSignature() {
		_parameterNames = new ArrayList<>();
		_parameterTypes = new ArrayList<>();

		String signature = getSignature();

		int posLeft = signature.indexOf('(');
		int rightFrom = signature.lastIndexOf(" ");

		int posR = signature.lastIndexOf(')', rightFrom);

		String paramString = signature.substring(posLeft + 1, posR);
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
					_parameterNames.add(pName);
					_parameterTypes.add(pType);
				}
			}
		}
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public String getSignature() {
		return _signature;
	}

	@Override
	public String getComment() {
		return _comment;
	}

}
