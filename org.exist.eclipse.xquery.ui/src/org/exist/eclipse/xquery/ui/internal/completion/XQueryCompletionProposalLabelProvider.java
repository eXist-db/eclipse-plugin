package org.exist.eclipse.xquery.ui.internal.completion;

import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.ui.text.completion.CompletionProposalLabelProvider;

public class XQueryCompletionProposalLabelProvider extends
		CompletionProposalLabelProvider {

	public XQueryCompletionProposalLabelProvider() {
	}

	@Override
	protected StringBuffer appendParameterList(StringBuffer buffer,
			CompletionProposal methodProposal) {
		String[] parameterNames = methodProposal.findParameterNames(null);
		Object info = methodProposal.getExtraInfo();
		String[] parameterTypes;
		if (info instanceof MethodCompletionExtraInfo) {
			MethodCompletionExtraInfo extraInfo = (MethodCompletionExtraInfo) info;
			parameterTypes = extraInfo.getParameterTypes();
		} else {
			parameterTypes = null;
		}
		return appendParameterSignature(buffer, parameterTypes, parameterNames);
	}

	@Override
	protected StringBuffer appendParameterSignature(StringBuffer buffer,
			String[] parameterTypes, String[] parameterNames) {

		if (parameterNames != null) {
			for (int i = 0; i < parameterNames.length; i++) {
				if (i > 0) {
					buffer.append(',');
					buffer.append(' ');
				}
				String name = parameterNames[i];
				buffer.append(name);
				if (parameterTypes != null
						&& !name.equals(XQueryMixinModel.MORE)) {
					String type = parameterTypes[i];
					if (!type.isEmpty()) {
						buffer.append(" as ").append(type);
					}
				}
			}
		}
		return buffer;
	}

}
