package org.exist.eclipse.xquery.ui.internal.editor;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.folding.IFoldingStructureProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.exist.eclipse.xquery.core.XQueryLanguageToolkit;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.context.IConnectionContext;
import org.exist.eclipse.xquery.ui.editor.IXQueryEditor;
import org.exist.eclipse.xquery.ui.internal.folding.XQueryFoldingStructureProvider;
import org.exist.eclipse.xquery.ui.internal.text.IXQueryPartitions;
import org.exist.eclipse.xquery.ui.internal.text.XQueryDocumentSetupParticipant;

/**
 * The base xquery editor.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryEditor extends ScriptEditor implements IXQueryEditor {

	public static final String EDITOR_ID = "org.exist.eclipse.xquery.ui.editor.XQueryEditor";

	public static final String EDITOR_CONTEXT = "#XQueryEditorContext";

	private XQueryFoldingStructureProvider _foldingProvider;

	private XQueryContextPart _xqueryContextPart;

	protected void initializeEditor() {
		super.initializeEditor();
		setEditorContextMenuId(EDITOR_CONTEXT);
	}

	public String getEditorId() {
		return EDITOR_ID;
	}

	protected IPreferenceStore getScriptPreferenceStore() {
		return XQueryUI.getDefault().getPreferenceStore();
	}

	public IDLTKLanguageToolkit getLanguageToolkit() {
		return XQueryLanguageToolkit.getDefault();
	}

	public ScriptTextTools getTextTools() {
		return XQueryUI.getDefault().getTextTools();
	}

	protected void connectPartitioningToElement(IEditorInput input,
			IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension = (IDocumentExtension3) document;
			if (extension
					.getDocumentPartitioner(IXQueryPartitions.XQUERY_PARTITIONING) == null) {
				XQueryDocumentSetupParticipant participant = new XQueryDocumentSetupParticipant();
				participant.setup(document);
			}
		}
		if (!(input instanceof IFileEditorInput)) {
			// disable folding
			IPreferenceStore store = XQueryUI.getDefault().getPreferenceStore();
			store.setDefault(PreferenceConstants.EDITOR_FOLDING_ENABLED, false);
			store.setDefault(
					PreferenceConstants.EDITOR_COMMENTS_FOLDING_ENABLED, false);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		Layout defaultLayout = parent.getLayout();

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);

		Composite toolbar = new Composite(parent, SWT.LEFT | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		toolbar.setLayoutData(gd);

		_xqueryContextPart = new XQueryContextPart(this);
		_xqueryContextPart.createToolbarControl(toolbar);

		Composite editor = new Composite(parent, SWT.LEFT | SWT.BORDER);
		editor.setLayout(defaultLayout);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		editor.setLayoutData(gd);

		super.createPartControl(editor);
	}

	public void setConnectionContext(IConnectionContext context) {
		_xqueryContextPart.setConnectionContext(context);
	}

	public final IConnectionContext getConnectionContext() {
		return _xqueryContextPart.getConnectionContext();
	}

	@Override
	public void dispose() {
		_xqueryContextPart.dispose();
		super.dispose();
	}

	@Override
	protected IFoldingStructureProvider getFoldingStructureProvider() {
		if (_foldingProvider == null) {
			_foldingProvider = new XQueryFoldingStructureProvider();
		}

		return _foldingProvider;
	}

	/**
	 * Ensure that the correct IDocumentProvider is used. For direct models, a
	 * special provider is used. For StorageEditorInputs, use a custom provider
	 * that creates a usable ResourceAnnotationModel. For everything else, use
	 * the base support.
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#setDocumentProvider(org.eclipse.ui.IEditorInput)
	 */
	@Override
	protected void setDocumentProvider(IEditorInput input) {
		IDocumentProvider provider = DocumentProviderRegistry.getDefault()
				.getDocumentProvider(input);
		if (input instanceof IStorageEditorInput
				&& !(input instanceof IFileEditorInput)) {
			if (provider != null) {
				setDocumentProvider(provider);
			} else {
				super.setDocumentProvider(input);
			}
		} else {
			super.setDocumentProvider(input);
		}
		super.setDocumentProvider(input);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////
}
