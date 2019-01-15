package org.exist.eclipse.xquery.ui.internal.preferences;

import java.io.InputStream;

import org.eclipse.dltk.internal.ui.editor.ScriptSourceViewer;
import org.eclipse.dltk.ui.preferences.AbstractScriptEditorColoringConfigurationBlock;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.preferences.PreferencesMessages;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.ITextEditor;
import org.exist.eclipse.xquery.ui.internal.text.IXQueryPartitions;
import org.exist.eclipse.xquery.ui.internal.text.SimpleXQuerySourceViewerConfiguration;
import org.exist.eclipse.xquery.ui.internal.text.XQueryDocumentSetupParticipant;

/**
 * The syntax coloring page, which make a preview.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryEditorColoringConfigurationBlock extends
		AbstractScriptEditorColoringConfigurationBlock implements
		IPreferenceConfigurationBlock {

	private static final String PREVIEW_FILE_NAME = "PreviewFile.txt";

	private static final String[][] _syntaxColorListModel = new String[][] {
			{ "Comment", XQueryPreferenceConstants.EDITOR_COMMENT_COLOR,
					sCommentsCategory },
			{ PreferencesMessages.DLTKEditorPreferencePage_keywords,
					XQueryPreferenceConstants.EDITOR_KEYWORD_COLOR,
					sCoreCategory },

			{ PreferencesMessages.DLTKEditorPreferencePage_returnKeyword,
					XQueryPreferenceConstants.EDITOR_KEYWORD_RETURN_COLOR,
					sCoreCategory },

			{ PreferencesMessages.DLTKEditorPreferencePage_strings,
					XQueryPreferenceConstants.EDITOR_STRING_COLOR,
					sCoreCategory },
			{ "Arguments", XQueryPreferenceConstants.EDITOR_ARGUMENT_COLOR,
					sCoreCategory },
			{ PreferencesMessages.DLTKEditorPreferencePage_function_colors,
					XQueryPreferenceConstants.EDITOR_FUNCTION_DEFINITON_COLOR,
					sCoreCategory } };

	public XQueryEditorColoringConfigurationBlock(OverlayPreferenceStore store) {
		super(store);
	}

	@Override
	protected String[][] getSyntaxColorListModel() {
		return _syntaxColorListModel;
	}

	@Override
	protected ProjectionViewer createPreviewViewer(Composite parent,
			IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
			boolean showAnnotationsOverview, int styles, IPreferenceStore store) {
		return new ScriptSourceViewer(parent, verticalRuler, overviewRuler,
				showAnnotationsOverview, styles, store);
	}

	@Override
	protected ScriptSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IColorManager colorManager, IPreferenceStore preferenceStore,
			ITextEditor editor, boolean configureFormatter) {
		return new SimpleXQuerySourceViewerConfiguration(colorManager,
				preferenceStore, editor, IXQueryPartitions.XQUERY_PARTITIONING,
				configureFormatter);
	}

	@Override
	protected void setDocumentPartitioning(IDocument document) {
		XQueryDocumentSetupParticipant participant = new XQueryDocumentSetupParticipant();
		participant.setup(document);
	}

	@Override
	protected InputStream getPreviewContentReader() {
		return getClass().getResourceAsStream(PREVIEW_FILE_NAME);
	}
}
