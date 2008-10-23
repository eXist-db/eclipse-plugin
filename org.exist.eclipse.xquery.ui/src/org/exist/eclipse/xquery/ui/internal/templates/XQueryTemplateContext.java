package org.exist.eclipse.xquery.ui.internal.templates;

import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ui.templates.ScriptTemplateContext;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.templates.TemplateContextType;

/**
 * The template context.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryTemplateContext extends ScriptTemplateContext {

	protected XQueryTemplateContext(TemplateContextType type,
			IDocument document, int completionOffset, int completionLength,
			ISourceModule sourceModule) {
		super(type, document, completionOffset, completionLength, sourceModule);
	}
}
