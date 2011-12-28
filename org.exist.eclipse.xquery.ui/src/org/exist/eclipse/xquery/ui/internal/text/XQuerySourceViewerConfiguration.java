package org.exist.eclipse.xquery.ui.internal.text;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptPresentationReconciler;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.SingleTokenScriptScanner;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;
import org.exist.eclipse.xquery.ui.internal.completion.XQueryCompletionProcessor;
import org.exist.eclipse.xquery.ui.internal.completion.XQueryContentAssistPreference;

/**
 * Point, where the different Scanner are created, the outline presenter, the
 * content assist; to make a long story short: all functions about the text on
 * editor.
 * 
 * @author Pascal Schmidiger
 */
public class XQuerySourceViewerConfiguration extends
		ScriptSourceViewerConfiguration {
	private AbstractScriptScanner _codeScanner;
	private AbstractScriptScanner _stringScanner;
	private AbstractScriptScanner _commentScanner;

	public XQuerySourceViewerConfiguration(IColorManager colorManager,
			IPreferenceStore preferenceStore, ITextEditor editor,
			String partitioning) {
		super(colorManager, preferenceStore, editor, partitioning);
	}

	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		return new IAutoEditStrategy[] { new DefaultIndentLineAutoEditStrategy() };
	}

	public String[] getIndentPrefixes(ISourceViewer sourceViewer,
			String contentType) {
		return new String[] { "\t", "        " };
	}

	protected ContentAssistPreference getContentAssistPreference() {
		return XQueryContentAssistPreference.getDefault();
	}

	protected IInformationControlCreator getOutlinePresenterControlCreator(
			ISourceViewer sourceViewer, final String commandId) {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				int shellStyle = SWT.RESIZE;
				int treeStyle = SWT.V_SCROLL | SWT.H_SCROLL;
				return new XQueryOutlineInformationControl(parent, shellStyle,
						treeStyle, commandId);
			}
		};
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return IXQueryPartitions.XQUERY_PARITION_TYPES;
	}

	protected void initializeScanners() {
		this._codeScanner = new XQueryCodeScanner(this.getColorManager(),
				this.fPreferenceStore);
		this._stringScanner = new SingleTokenScriptScanner(this
				.getColorManager(), this.fPreferenceStore,
				IXQueryColorConstants.XQUERY_STRING);
		this._commentScanner = new XQueryCommentScanner(this.getColorManager(),
				this.fPreferenceStore);
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new ScriptPresentationReconciler();
		reconciler.setDocumentPartitioning(this
				.getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
				this._codeScanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(this._stringScanner);
		reconciler.setDamager(dr, IXQueryPartitions.XQUERY_STRING);
		reconciler.setRepairer(dr, IXQueryPartitions.XQUERY_STRING);

		dr = new DefaultDamagerRepairer(this._commentScanner);
		reconciler.setDamager(dr, IXQueryPartitions.XQUERY_COMMENT);
		reconciler.setRepairer(dr, IXQueryPartitions.XQUERY_COMMENT);

		return reconciler;
	}

	public void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (this._codeScanner.affectsBehavior(event)) {
			this._codeScanner.adaptToPreferenceChange(event);
		}
		if (this._stringScanner.affectsBehavior(event)) {
			this._stringScanner.adaptToPreferenceChange(event);
		}
	}

	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		return _codeScanner.affectsBehavior(event)
				|| _stringScanner.affectsBehavior(event);
	}

	protected void alterContentAssistant(ContentAssistant assistant) {
		// IDocument.DEFAULT_CONTENT_TYPE
		// IContentAssistProcessor processor = new
		// MultiplexContentAssistProcessor(
		// new char[] { '$' }, new XQueryCompletionProcessor(getEditor(),
		// assistant, IDocument.DEFAULT_CONTENT_TYPE),
		// new HippieProposalProcessor());
		assistant.setContentAssistProcessor(new XQueryCompletionProcessor(
				getEditor(), assistant, IDocument.DEFAULT_CONTENT_TYPE),
				IDocument.DEFAULT_CONTENT_TYPE);
	}
}
