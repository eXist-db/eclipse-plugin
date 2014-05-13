package org.exist.eclipse.xquery.ui.internal.completion;

import org.eclipse.dltk.ui.text.completion.ScriptCompletionProcessor;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.ui.IEditorPart;
import org.exist.eclipse.xquery.core.XQueryNature;

/**
 * The skeleton of the the completion.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryCompletionProcessor extends ScriptCompletionProcessor {
	public XQueryCompletionProcessor(IEditorPart editor,
			ContentAssistant assistant, String partition) {
		super(editor, assistant, partition);
	}

	@Override
	protected String getNatureId() {
		return XQueryNature.NATURE_ID;
	}
}
