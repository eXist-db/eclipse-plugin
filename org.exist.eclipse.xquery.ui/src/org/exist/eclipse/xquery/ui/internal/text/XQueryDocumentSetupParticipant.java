package org.exist.eclipse.xquery.ui.internal.text;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * The document setup participant for XQuery.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryDocumentSetupParticipant implements IDocumentSetupParticipant {

	public XQueryDocumentSetupParticipant() {
	}

	@Override
	public void setup(IDocument document) {
		XQueryTextTools tools = XQueryUI.getDefault().getTextTools();
		tools.setupDocumentPartitioner(document, IXQueryPartitions.XQUERY_PARTITIONING);
	}
}
