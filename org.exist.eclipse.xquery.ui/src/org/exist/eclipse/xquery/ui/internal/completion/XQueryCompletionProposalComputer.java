package org.exist.eclipse.xquery.ui.internal.completion;

import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposalCollector;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposalComputer;
import org.eclipse.dltk.ui.text.completion.ScriptContentAssistInvocationContext;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.exist.eclipse.xquery.ui.internal.templates.XQueryTemplateCompletionProcessor;

/**
 * Point to create the code assist proposals for the function in the document.
 * This class is registered on an extension.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryCompletionProposalComputer extends
		ScriptCompletionProposalComputer {

	public XQueryCompletionProposalComputer() {
	}

	@Override
	protected ScriptCompletionProposalCollector createCollector(
			ScriptContentAssistInvocationContext context) {
		return new XQueryCompletionProposalCollector(context.getSourceModule());
	}

	@Override
	protected TemplateCompletionProcessor createTemplateProposalComputer(
			ScriptContentAssistInvocationContext context) {
		return new XQueryTemplateCompletionProcessor(context);
	}
}
