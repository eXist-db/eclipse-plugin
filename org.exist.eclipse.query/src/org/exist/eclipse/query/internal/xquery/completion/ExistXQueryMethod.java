package org.exist.eclipse.query.internal.xquery.completion;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.exist.eclipse.xquery.ui.completion.IXQueryMethod;

/**
 * A wrapper for a static xquery function.
 * 
 * @author Pascal Schmidiger
 */
public class ExistXQueryMethod implements IXQueryMethod {
	private final List<String> _parameterNames;
	private final List<String> _parameterTypes;
	private final String _name;

	public ExistXQueryMethod(String name) {
		_name = name;
		_parameterNames = new ArrayList<>();
		_parameterTypes = new ArrayList<>();
	}

	/**
	 * Add the given <code>parameter</code> to the method.
	 * 
	 * @param name
	 * @param type empty for none
	 */
	public void addParameter(String name, String type) {
		_parameterNames.add(name);
		_parameterTypes.add(type);
	}

	@Override
	public int getFlags() {
		return Modifier.PUBLIC;
	}

	@Override
	public String[] getParameterNames() {
		return _parameterNames.toArray(new String[_parameterNames.size()]);
	}

	@Override
	public String[] getParameterTypes() {
		return _parameterTypes.toArray(new String[_parameterTypes.size()]);
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public String getComment() {
		return "";
	}

	@Override
	public String getSignature() {
		return "";
	}

}
