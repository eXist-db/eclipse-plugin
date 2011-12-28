package org.exist.eclipse.xquery.ui.internal.completion;

import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationExtension;
import org.eclipse.swt.graphics.Image;

/**
 * Copied from DLTK and added parameter types. code has not further been
 * changed/refactored/cleaned.
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class XQueryProposalContextInformation implements IContextInformation,
		IContextInformationExtension {

	private final String _contextDisplayString;
	private final String _informationDisplayString;
	private final Image _image;
	private int _position;

	public XQueryProposalContextInformation(CompletionProposal proposal) {
		// don't cache the core proposal because the ContentAssistant might
		// hang on to the context info.
		// TODO REVIEW THIS
		String res = createParametersList(proposal);
		_informationDisplayString = res;// labelProvider.createParameterList(proposal);
		// ImageDescriptor descriptor=
		// labelProvider.createImageDescriptor(proposal);
		// if (descriptor != null)
		// fImage= JavaPlugin.getImageDescriptorRegistry().get(descriptor);
		// else
		_image = null;
		if (proposal.getCompletion().length() == 0) {
			_position = proposal.getCompletionLocation() + 1;
		} else {
			_position = -1;
		}
		_contextDisplayString = res;// labelProvider.createLabel(proposal);
	}

	private String createParametersList(CompletionProposal proposal) {
		StringBuffer bf = new StringBuffer();
		String[] pNames = proposal.findParameterNames(null);

		String[] pTypes;
		Object extraInfo = proposal.getExtraInfo();
		if (extraInfo instanceof MethodCompletionExtraInfo) {
			pTypes = ((MethodCompletionExtraInfo) extraInfo)
					.getParameterTypes();
		} else {
			pTypes = null;
		}

		for (int a = 0; a < pNames.length; a++) {
			bf.append(pNames[a]);
			if (pTypes != null) {
				String t = pTypes[a];
				if (!t.isEmpty()) {
					bf.append(" as ");
					bf.append(t);
				}
			}
			if (a != pNames.length - 1) {
				bf.append(", ");
			}
		}
		String res = bf.toString();
		return res;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof IContextInformation) {
			IContextInformation contextInformation = (IContextInformation) object;
			boolean equals = getInformationDisplayString().equalsIgnoreCase(
					contextInformation.getInformationDisplayString());
			if (getContextDisplayString() != null) {
				equals = equals
						&& getContextDisplayString().equalsIgnoreCase(
								contextInformation.getContextDisplayString());
			}
			return equals;
		}
		return false;
	}

	public String getInformationDisplayString() {
		return _informationDisplayString;
	}

	public Image getImage() {
		return _image;
	}

	public String getContextDisplayString() {
		return _contextDisplayString;
	}

	public int getContextInformationPosition() {
		return _position;
	}

	public void setContextInformationPosition(int position) {
		_position = position;
	}
}
