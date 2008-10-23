package org.exist.eclipse.xquery.ui.internal.completion;

import org.eclipse.dltk.ui.text.completion.CompletionProposalLabelProvider;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProcessor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.ui.IEditorPart;
import org.exist.eclipse.xquery.core.XQueryCorePlugin;
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

	protected String getNatureId() {
		return XQueryNature.NATURE_ID;
	}

	protected CompletionProposalLabelProvider getProposalLabelProvider() {
		return new CompletionProposalLabelProvider();
	}

	protected IPreferenceStore getPreferenceStore() {
		return XQueryCorePlugin.getDefault().getPreferenceStore();
	}
}
