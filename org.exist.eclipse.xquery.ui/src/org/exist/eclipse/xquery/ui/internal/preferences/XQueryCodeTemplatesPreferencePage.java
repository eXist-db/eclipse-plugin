package org.exist.eclipse.xquery.ui.internal.preferences;

import org.eclipse.dltk.ui.templates.ScriptTemplateAccess;
import org.eclipse.dltk.ui.templates.ScriptTemplatePreferencePage;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.text.IDocument;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.internal.templates.XQueryTemplateAccess;
import org.exist.eclipse.xquery.ui.internal.text.IXQueryPartitions;
import org.exist.eclipse.xquery.ui.internal.text.SimpleXQuerySourceViewerConfiguration;
import org.exist.eclipse.xquery.ui.internal.text.XQueryTextTools;

/**
 * XQuery code templates preference page.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryCodeTemplatesPreferencePage extends
		ScriptTemplatePreferencePage {

	protected ScriptSourceViewerConfiguration createSourceViewerConfiguration() {
		return new SimpleXQuerySourceViewerConfiguration(getTextTools()
				.getColorManager(), getPreferenceStore(), null,
				IXQueryPartitions.XQUERY_PARTITIONING, false);
	}

	protected void setDocumentParticioner(IDocument document) {
		getTextTools().setupDocumentPartitioner(document,
				IXQueryPartitions.XQUERY_PARTITIONING);
	}

	protected void setPreferenceStore() {
		setPreferenceStore(XQueryUI.getDefault().getPreferenceStore());
	}

	protected ScriptTemplateAccess getTemplateAccess() {
		return XQueryTemplateAccess.getInstance();
	}

	private XQueryTextTools getTextTools() {
		return XQueryUI.getDefault().getTextTools();
	}
}
