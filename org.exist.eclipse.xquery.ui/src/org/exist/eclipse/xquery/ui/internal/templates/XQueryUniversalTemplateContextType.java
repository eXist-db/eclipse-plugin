package org.exist.eclipse.xquery.ui.internal.templates;

import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ui.templates.ScriptTemplateContext;
import org.eclipse.dltk.ui.templates.ScriptTemplateContextType;
import org.eclipse.jface.text.IDocument;

/**
 * Context for the template engine.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryUniversalTemplateContextType extends
		ScriptTemplateContextType {

	public static final String CONTEXT_TYPE_ID = "xqueryUniversalTemplateContextType";

	public XQueryUniversalTemplateContextType() {
		// empty constructor
	}

	public XQueryUniversalTemplateContextType(String id, String name) {
		super(id, name);
	}

	public XQueryUniversalTemplateContextType(String id) {
		super(id);
	}

	public ScriptTemplateContext createContext(IDocument document,
			int completionPosition, int length, ISourceModule sourceModule) {
		return new XQueryTemplateContext(this, document, completionPosition,
				length, sourceModule);
	}

}
