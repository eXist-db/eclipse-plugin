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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.exist.eclipse.xquery.core.XQueryLanguageToolkit;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.action.RunXQueryAction;
import org.exist.eclipse.xquery.ui.action.refactoring.ExtractLocalVariableAction;
import org.exist.eclipse.xquery.ui.action.refactoring.ExtractMethodAction;
import org.exist.eclipse.xquery.ui.action.refactoring.ToggleCommentAction;
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

	public static final String EDITING_XQUERY_SOURCE_CONTEXT = "org.exist.eclipse.xquery.ui.xqueryEditorScope";
	public static final String EDITOR_ID = "org.exist.eclipse.xquery.ui.editor.XQueryEditor";

	public static final String EDITOR_CONTEXT_MENU_ID = "#XQueryEditorContext";

	private XQueryFoldingStructureProvider _foldingProvider;

	private XQueryContextPart _xqueryContextPart;

	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		setEditorContextMenuId(EDITOR_CONTEXT_MENU_ID);
	}

	@Override
	public String getEditorId() {
		return EDITOR_ID;
	}

	@Override
	public IPreferenceStore getScriptPreferenceStore() {
		return XQueryUI.getDefault().getPreferenceStore();
	}

	@Override
	public IDLTKLanguageToolkit getLanguageToolkit() {
		return XQueryLanguageToolkit.getDefault();
	}

	@Override
	public ScriptTextTools getTextTools() {
		return XQueryUI.getDefault().getTextTools();
	}

	@Override
	protected void connectPartitioningToElement(IEditorInput input, IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension = (IDocumentExtension3) document;
			if (extension.getDocumentPartitioner(IXQueryPartitions.XQUERY_PARTITIONING) == null) {
				XQueryDocumentSetupParticipant participant = new XQueryDocumentSetupParticipant();
				participant.setup(document);
			}
		}
		if (!(input instanceof IFileEditorInput)) {
			// disable folding
			IPreferenceStore store = XQueryUI.getDefault().getPreferenceStore();
			store.setDefault(PreferenceConstants.EDITOR_FOLDING_ENABLED, false);
			store.setDefault(PreferenceConstants.EDITOR_COMMENTS_FOLDING_ENABLED, false);
		}
	}

	public void runQuery() {
		_xqueryContextPart.runQuery();
	}

	@Override
	public void createPartControl(Composite parent) {

		if (!isEditable()) {
			super.createPartControl(parent);
			return;
		}

		Layout defaultLayout = parent.getLayout();

		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		parent.setLayout(layout);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		Composite toolbar = new Composite(parent, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		toolbar.setLayoutData(gd);
		_xqueryContextPart = new XQueryContextPart(this);
		_xqueryContextPart.createToolbarControl(toolbar);

		GridData separatorGd = new GridData(GridData.FILL_HORIZONTAL);
		separatorGd.heightHint = 1;
		Label separator = new Label(parent, SWT.NONE);
		separator.setLayoutData(separatorGd);
		separator.setBackground(separator.getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

		Composite editorContainer = new Composite(parent, SWT.NONE);

		editorContainer.setLayout(defaultLayout);
		gd = new GridData(GridData.FILL_BOTH);
		editorContainer.setLayoutData(gd);

		super.createPartControl(editorContainer);

		setGlobalActionHandlers();
		activateContext();
	}

	private void setGlobalActionHandlers() {
		IActionBars actionBars = getEditorSite().getActionBars();
		actionBars.setGlobalActionHandler(RunXQueryAction.ID, new RunXQueryAction());
		actionBars.setGlobalActionHandler(ToggleCommentAction.ID, new ToggleCommentAction());
		actionBars.setGlobalActionHandler(ExtractLocalVariableAction.ID, new ExtractLocalVariableAction());
		actionBars.setGlobalActionHandler(ExtractMethodAction.ID, new ExtractMethodAction());

		actionBars.updateActionBars();
	}

	private void activateContext() {
		IContextService service = (IContextService) getSite().getService(IContextService.class);
		if (service != null) {
			service.activateContext("org.eclipse.dltk.ui.scriptEditorScope");
			service.activateContext(EDITING_XQUERY_SOURCE_CONTEXT);
		}
	}

	@Override
	public void setConnectionContext(IConnectionContext context) {
		_xqueryContextPart.setConnectionContext(context);
	}

	@Override
	public final IConnectionContext getConnectionContext() {
		return _xqueryContextPart.getConnectionContext();
	}

	@Override
	public void dispose() {
		if (_xqueryContextPart != null) {
			_xqueryContextPart.dispose();
		}
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
	 * special provider is used. For StorageEditorInputs, use a custom provider that
	 * creates a usable ResourceAnnotationModel. For everything else, use the base
	 * support.
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#setDocumentProvider(org.eclipse.ui.IEditorInput)
	 */
	@Override
	protected void setDocumentProvider(IEditorInput input) {
		IDocumentProvider provider = DocumentProviderRegistry.getDefault().getDocumentProvider(input);
		if (input instanceof IStorageEditorInput && !(input instanceof IFileEditorInput)) {
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

	@Override
	protected boolean isTabsToSpacesConversionEnabled() {
		// needed for proper node location detection
		return true;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////
}
