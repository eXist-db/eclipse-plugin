package org.exist.eclipse.xquery.ui.internal.text;

import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.information.IInformationPresenter;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * A simple {@linkplain XQuerySourceViewerConfiguration XQuery source viewer
 * configuration}.
 * <p>
 * This simple source viewer configuration basically provides syntax coloring
 * and disables all other features like code assist, quick outlines,
 * hyperlinking, etc.
 * </p>
 * This is used for the syntax coloring preference page.
 * 
 * @author Pascal Schmidiger
 */
public class SimpleXQuerySourceViewerConfiguration extends
		XQuerySourceViewerConfiguration {

	private boolean _configureFormatter;

	/**
	 * Creates a new XQuery source viewer configuration for viewers in the given
	 * editor using the given preference store, the color manager and the
	 * specified document partitioning.
	 * 
	 * @param colorManager
	 *            the color manager
	 * @param preferenceStore
	 *            the preference store, can be read-only
	 * @param editor
	 *            the editor in which the configured viewer(s) will reside, or
	 *            <code>null</code> if none
	 * @param partitioning
	 *            the document partitioning for this configuration, or
	 *            <code>null</code> for the default partitioning
	 * @param configureFormatter
	 *            <code>true</code> if a content formatter should be configured
	 */
	public SimpleXQuerySourceViewerConfiguration(IColorManager colorManager,
			IPreferenceStore preferenceStore, ITextEditor editor,
			String partitioning, boolean configureFormatter) {
		super(colorManager, preferenceStore, editor, partitioning);
		_configureFormatter = configureFormatter;
	}

	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		return null;
	}

	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return null;
	}

	public IAnnotationHover getOverviewRulerAnnotationHover(
			ISourceViewer sourceViewer) {
		return null;
	}

	public int[] getConfiguredTextHoverStateMasks(ISourceViewer sourceViewer,
			String contentType) {
		return null;
	}

	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType, int stateMask) {
		return null;
	}

	public ITextHover getTextHover(ISourceViewer sourceViewer,
			String contentType) {
		return null;
	}

	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		if (_configureFormatter)
			return super.getContentFormatter(sourceViewer);
		else
			return null;
	}

	public IInformationControlCreator getInformationControlCreator(
			ISourceViewer sourceViewer) {
		return null;
	}

	public IInformationPresenter getInformationPresenter(
			ISourceViewer sourceViewer) {
		return null;
	}

	public IInformationPresenter getOutlinePresenter(
			ISourceViewer sourceViewer, boolean doCodeResolve) {
		return null;
	}

	public IInformationPresenter getHierarchyPresenter(
			ISourceViewer sourceViewer, boolean doCodeResolve) {
		return null;
	}

	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		return null;
	}
}
