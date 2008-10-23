package org.exist.eclipse.xquery.core;

import org.eclipse.dltk.core.AbstractLanguageToolkit;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;

/**
 * Here you find the skeletal structure about the xquery language.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryLanguageToolkit extends AbstractLanguageToolkit {
	private static XQueryLanguageToolkit _default;

	public static IDLTKLanguageToolkit getDefault() {
		if (_default == null) {
			_default = new XQueryLanguageToolkit();
		}
		return _default;
	}

	public String getLanguageName() {
		return "XQuery";
	}

	public String getNatureId() {
		return XQueryNature.NATURE_ID;
	}

	public String getLanguageContentType() {
		return "org.exist.eclipse.xquery.core.content-type";
	}
}
