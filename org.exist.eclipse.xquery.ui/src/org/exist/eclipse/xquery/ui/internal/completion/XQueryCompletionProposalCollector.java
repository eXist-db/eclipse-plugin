package org.exist.eclipse.xquery.ui.internal.completion;

import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ui.text.completion.IScriptCompletionProposal;
import org.eclipse.dltk.ui.text.completion.LazyScriptCompletionProposal;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposal;
import org.eclipse.dltk.ui.text.completion.ScriptCompletionProposalCollector;
import org.eclipse.swt.graphics.Image;
import org.exist.eclipse.xquery.core.XQueryNature;

/**
 * The collector for content assist.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryCompletionProposalCollector extends
		ScriptCompletionProposalCollector {

	protected final static char[] VAR_TRIGGER = new char[] { '\t', ' ', '=',
			';', '.' };

	@Override
	protected char[] getVarTrigger() {
		return VAR_TRIGGER;
	}

	public XQueryCompletionProposalCollector(ISourceModule module) {
		super(module);
	}

	@Override
	protected IScriptCompletionProposal createScriptCompletionProposal(
			CompletionProposal proposal) {

		switch (proposal.getKind()) {
		case CompletionProposal.METHOD_REF:
		case CompletionProposal.METHOD_NAME_REFERENCE:
			return createMethodReferenceProposal(proposal);
		default:
			break;
		}

		return super.createScriptCompletionProposal(proposal);
	}
	
	@Override
	protected IScriptCompletionProposal createMethodReferenceProposal(
			CompletionProposal methodProposal) {
		LazyScriptCompletionProposal proposal = new XQueryScriptMethodCompletionProposal(
				methodProposal, getInvocationContext());
		// adaptLength(proposal, methodProposal);
		return proposal;
	}

	@Override
	protected ScriptCompletionProposal createScriptCompletionProposal(
			String completion, int replaceStart, int length, Image image,
			String displayString, int i) {
		return new XQueryCompletionProposal(completion, replaceStart, length,
				image, displayString, i);
	}

	@Override
	protected ScriptCompletionProposal createScriptCompletionProposal(
			String completion, int replaceStart, int length, Image image,
			String displayString, int i, boolean isInDoc) {
		return new XQueryCompletionProposal(completion, replaceStart, length,
				image, displayString, i, isInDoc);
	}

	@Override
	protected ScriptCompletionProposal createOverrideCompletionProposal(
			IScriptProject scriptProject, ISourceModule compilationUnit,
			String name, String[] paramTypes, int start, int length,
			String displayName, String completionProposal) {
		return new XQueryCompletionProposal(completionProposal, start, length,
				null, displayName, 0);
	}

	@Override
	protected String getNatureId() {
		return XQueryNature.NATURE_ID;
	}
}
