package org.exist.eclipse.xquery.ui.internal.completion;

/**
 * Used for additional method parameter types.
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class MethodCompletionExtraInfo {

	private final String[] _parameterTypes;

	public MethodCompletionExtraInfo(String[] parameterTypes) {
		_parameterTypes = parameterTypes;
	}

	public String[] getParameterTypes() {
		return _parameterTypes;
	}
}