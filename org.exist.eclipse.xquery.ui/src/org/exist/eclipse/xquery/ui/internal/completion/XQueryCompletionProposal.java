package org.exist.eclipse.xquery.ui.internal.completion;

import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposal;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Image;
import org.exist.eclipse.xquery.core.XQueryCorePlugin;

/**
 * A proposal for content assist.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryCompletionProposal extends ScriptCompletionProposal {
	public XQueryCompletionProposal(String replacementString,
			int replacementOffset, int replacementLength, Image image,
			String displayString, int relevance) {
		super(replacementString, replacementOffset, replacementLength, image,
				displayString, relevance);
	}

	public XQueryCompletionProposal(String replacementString,
			int replacementOffset, int replacementLength, Image image,
			String displayString, int relevance, boolean isInDoc) {
		super(replacementString, replacementOffset, replacementLength, image,
				displayString, relevance, isInDoc);
	}

	@Override
	protected boolean isSmartTrigger(char trigger) {
		if (trigger == '.') {
			return true;
		}
		return false;
	}

	@Override
	protected boolean insertCompletion() {
		IPreferenceStore preference = XQueryCorePlugin.getDefault()
				.getPreferenceStore();
		return preference
				.getBoolean(PreferenceConstants.CODEASSIST_INSERT_COMPLETION);
	}
}
