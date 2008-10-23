package org.exist.eclipse.xquery.ui.internal.templates;

import org.eclipse.dltk.ui.templates.ScriptTemplateAccess;
import org.eclipse.dltk.ui.templates.ScriptTemplateCompletionProcessor;
import org.eclipse.dltk.ui.text.completion.ScriptContentAssistInvocationContext;

/**
 * XQuery template completion processor.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryTemplateCompletionProcessor extends
		ScriptTemplateCompletionProcessor {

	private static char[] IGNORE = new char[] { '.' };

	public XQueryTemplateCompletionProcessor(
			ScriptContentAssistInvocationContext context) {
		super(context);
	}

	protected String getContextTypeId() {
		return XQueryUniversalTemplateContextType.CONTEXT_TYPE_ID;
	}

	protected char[] getIgnore() {
		return IGNORE;
	}

	protected ScriptTemplateAccess getTemplateAccess() {
		return XQueryTemplateAccess.getInstance();
	}
}
