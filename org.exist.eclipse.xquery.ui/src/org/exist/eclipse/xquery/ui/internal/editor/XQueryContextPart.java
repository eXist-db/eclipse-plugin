/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.ITextEditor;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.context.ConnectionContextEvent;
import org.exist.eclipse.xquery.ui.context.IConnectionContext;
import org.exist.eclipse.xquery.ui.context.IContextListener;
import org.exist.eclipse.xquery.ui.internal.result.IQueryFrameInfo;
import org.exist.eclipse.xquery.ui.internal.result.ResultView;
import org.exist.eclipse.xquery.ui.internal.wizards.SelectContextWizard;
import org.exist.eclipse.xquery.ui.result.IQueryFrame;

/**
 * Define the context section in the xquery editor.
 * 
 * @author Pascal Schmidiger
 */
public class XQueryContextPart implements IContextListener, IQueryFrameInfo {
	private static final String TAB_CHECK_PREFERENCES = "tab_check";
	private Image _imgRun;
	private Image _imgSearch;
	private IConnectionContext _context;
	private Text _contextName;
	private Spinner _querySpinner;
	private final ITextEditor _editor;
	private Button _tabCheck;

	public XQueryContextPart(ITextEditor editor) {
		_editor = editor;
	}

	public void createToolbarControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 8;
		parent.setLayout(layout);

		// Add Context
		createContextInfo(parent);

		// Add Max-Disp.
		createMaxDisplay(parent);

		// Add New Tab control
		createTabCheck(parent);

		// Add Toolbar
		createToolbar(parent);
	}

	public void refresh() {
		if (_context == null) {
			_contextName.setText("Please set a context...");
		} else {
			_contextName.setText(_context.getName());
		}
	}

	/**
	 * @param context
	 */
	public void setConnectionContext(IConnectionContext context) {
		if (_context != null) {
			_context.removeContextListener(this);
		}
		_context = context;
		_context.addContextListener(this);
		refresh();
	}

	/**
	 * @return the actual {@link IConnectionContext} if one is set, elsewhere
	 *         <code>null</code>.
	 */
	public IConnectionContext getConnectionContext() {
		return _context;
	}

	public void refresh(ConnectionContextEvent event) {
		_context = event.getConnectionContext();
		refresh();
	}

	public void disposed(ConnectionContextEvent event) {
		dispose();
		refresh();
	}

	/**
	 * Dispose the part.
	 */
	public void dispose() {
		if (_context != null) {
			_context.removeContextListener(this);
			_context = null;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////

	private void createContextInfo(Composite parent) {
		Label contextLabel = new Label(parent, SWT.LEFT);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		contextLabel.setLayoutData(gd);
		contextLabel.setText("Context: ");
		contextLabel.pack();

		_contextName = new Text(parent, SWT.LEFT | SWT.BORDER | SWT.SINGLE
				| SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 5;
		_contextName.setLayoutData(gd);
		_contextName.setText("Please set a context...");
		_contextName.pack();

		ToolBar bar = new ToolBar(parent, SWT.RIGHT);
		gd = new GridData();
		gd.horizontalSpan = 1;
		bar.setLayoutData(gd);

		final ToolItem runItem = new ToolItem(bar, SWT.PUSH);
		runItem.setImage(getSearchImage());
		runItem.setToolTipText("Select Context");
		runItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SelectContextWizard wizard = new SelectContextWizard();
				wizard.init(XQueryUI.getDefault().getWorkbench(), null);
				wizard.setForcePreviousAndNextButtons(true);
				WizardDialog dialog = new WizardDialog(XQueryUI.getDefault()
						.getWorkbench().getDisplay().getActiveShell(), wizard);
				dialog.open();
			}
		});
	}

	private void createTabCheck(Composite parent) {
		_tabCheck = new Button(parent, SWT.CHECK);
		_tabCheck.setText("Create tab");
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		_tabCheck.setLayoutData(gd);

		final IPreferenceStore store = XQueryUI.getDefault()
				.getPreferenceStore();
		_tabCheck.setSelection(store.getBoolean(TAB_CHECK_PREFERENCES));
		_tabCheck.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				Button button = (Button) e.getSource();
				store.setValue(TAB_CHECK_PREFERENCES, button.getSelection());
			}
		});
	}

	private void createMaxDisplay(Composite parent) {
		Label querySpinnerLabel = new Label(parent, SWT.RIGHT);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 5;
		querySpinnerLabel.setLayoutData(gd);
		querySpinnerLabel.setText("Max-Display:");

		_querySpinner = new Spinner(parent, SWT.BORDER | SWT.LEFT);
		gd = new GridData();
		gd.horizontalSpan = 1;
		_querySpinner.setLayoutData(gd);
		_querySpinner.setLayoutData(gd);
		_querySpinner.setMinimum(1);
		_querySpinner.setMaximum(1000000);
		_querySpinner.setIncrement(1);
		_querySpinner.setPageIncrement(100);
		_querySpinner.setSelection(100);
	}

	private void createToolbar(Composite parent) {
		ToolBar bar = new ToolBar(parent, SWT.RIGHT);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		bar.setLayoutData(gd);

		final ToolItem runItem = new ToolItem(bar, SWT.PUSH);
		runItem.setImage(getRunImage());
		runItem.setToolTipText("Run Query");
		final XQueryContextPart instance = this;
		runItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (_context != null) {
					IWorkbenchPage activePage = XQueryUI.getDefault()
							.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage();
					try {
						ResultView view = (ResultView) activePage
								.showView(ResultView.ID);
						IQueryFrame frame = view.createQueryFrame(instance);
						_context.run(frame);
					} catch (PartInitException e1) {
						Status status = new Status(IStatus.ERROR,
								XQueryUI.PLUGIN_ID, e1.getMessage(), e1);
						XQueryUI.getDefault().errorDialog("Run XQuery",
								e1.getMessage(), status);
						e1.printStackTrace();
					}
				} else {
					XQueryUI.getDefault().infoDialog("Run XQuery",
							"No context available.");
				}
			}
		});
	}

	public String getFilename() {
		return _editor.getTitle();
	}

	public int getMaxCount() {
		return _querySpinner.getSelection();
	}

	public String getQuery() {
		return _editor.getDocumentProvider().getDocument(
				_editor.getEditorInput()).get();
	}

	public boolean isCreatedNewTab() {
		return _tabCheck.getSelection();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////
	private Image getRunImage() {
		if (_imgRun == null) {
			_imgRun = XQueryUI.getImageDescriptor("icons/run.gif")
					.createImage();
		}
		return _imgRun;
	}

	private Image getSearchImage() {
		if (_imgSearch == null) {
			_imgSearch = XQueryUI.getImageDescriptor("icons/search.png")
					.createImage();
		}
		return _imgSearch;
	}

}
