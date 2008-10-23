/**
 * 
 */
package org.exist.eclipse.query.internal.xquery.completion;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.exist.eclipse.xquery.ui.completion.IXQueryMethod;

/**
 * Representation of an {@link IXQueryMethod} from eXist.
 * 
 * @author Pascal Schmidiger
 */
public class ExistXQueryMethod implements IXQueryMethod {
	private final List<String> _parameters;
	private final String _name;

	public ExistXQueryMethod(String name) {
		_name = name;
		_parameters = new ArrayList<String>();
	}

	/**
	 * Add the given <code>parameter</code> to the method.
	 * 
	 * @param parameter
	 */
	public void addParameter(String parameter) {
		if (parameter != null && parameter.length() > 0) {
			_parameters.add(parameter);
		}
	}

	public int getFlags() {
		return Modifier.PUBLIC;
	}

	public String getName() {
		return _name;
	}

	public String[] getParameters() {
		return _parameters.toArray(new String[_parameters.size()]);
	}

}
