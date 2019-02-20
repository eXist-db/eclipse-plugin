/**
 * 
 */

package org.exist.eclipse.xquery.ui.internal.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
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
	private static final String SELECT_A_CONTEXT = "Select...";
	private static final String TAB_CHECK_PREFERENCES = "tab_check";
	private Image _imgRun;
	private IConnectionContext _context;
	private Spinner _querySpinner;
	private final ITextEditor _editor;
	private Button _tabCheck;
	private Label _selectContextLabel;

	public XQueryContextPart(ITextEditor editor) {
		_editor = editor;
	}

	public void createToolbarControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 8;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		parent.setLayout(layout);

		// Add Context
		createContextInfo(parent);

		Label filler = new Label(parent, SWT.NONE);
		filler.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Add Max-Disp.
		createMaxDisplay(parent);

		// Add New Tab control
		createTabCheck(parent);

		// Add Toolbar
		Label q = new Label(parent, SWT.SEPARATOR);
		GridData aa = new GridData(GridData.FILL_VERTICAL);
		aa.heightHint = 0;
		q.setLayoutData(aa);
		createToolbar(parent);
	}

	public void refresh() {
		String name;
		if (_context == null) {
			name = SELECT_A_CONTEXT;
		} else {
			name = _context.getName();
		}

		_selectContextLabel.setText(name);
		_selectContextLabel.getParent().layout();
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

	@Override
	public void refresh(ConnectionContextEvent event) {
		_context = event.getConnectionContext();
		refresh();
	}

	@Override
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
		if (_imgRun != null) {
			_imgRun.dispose();
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////

	private void createContextInfo(Composite parent) {
		Label contextLabel = new Label(parent, SWT.LEFT);
		GridData gd = new GridData();
		contextLabel.setLayoutData(gd);
		contextLabel.setText("Context:");
		contextLabel.pack();

		final boolean[] labelEnter = new boolean[1];

		_selectContextLabel = new Label(parent, SWT.NONE);
		_selectContextLabel.setCursor(_selectContextLabel.getDisplay().getSystemCursor(SWT.CURSOR_HAND));
		_selectContextLabel.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (labelEnter[0]) {
					GC gc = e.gc;
					gc.setForeground(_selectContextLabel.getForeground());
					int h = e.height - 1;
					gc.drawLine(e.x, e.y + h, e.width, e.y + h);
				}
			}
		});

		_selectContextLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Rectangle q = e.display.map(_selectContextLabel.getParent(), _selectContextLabel,
						_selectContextLabel.getBounds());
				if (q.contains(e.x, e.y)) {
					onSelectContext();
				}
			}
		});

		_selectContextLabel.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				onMouseEnter(true);
			}

			@Override
			public void mouseExit(MouseEvent e) {
				onMouseEnter(false);
			}

			private void onMouseEnter(boolean enter) {
				labelEnter[0] = enter;
				_selectContextLabel.redraw();
				_selectContextLabel.update();
			}
		});
		_selectContextLabel.setForeground(JFaceColors.getHyperlinkText(_selectContextLabel.getDisplay()));
		gd = new GridData(0);
		_selectContextLabel.setLayoutData(gd);
		_selectContextLabel.setToolTipText("Select Context");
		refresh();
	}

	private void createTabCheck(Composite parent) {
		_tabCheck = new Button(parent, SWT.CHECK);
		_tabCheck.setText("Create Tab");
		_tabCheck.setToolTipText("For each run, create a new tab in "
				+ PlatformUI.getWorkbench().getViewRegistry().find(ResultView.ID).getLabel() + " view");
		GridData gd = new GridData();
		gd.horizontalIndent = 5;
		_tabCheck.setLayoutData(gd);

		final IPreferenceStore store = XQueryUI.getDefault().getPreferenceStore();
		_tabCheck.setSelection(store.getBoolean(TAB_CHECK_PREFERENCES));
		_tabCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button button = (Button) e.getSource();
				store.setValue(TAB_CHECK_PREFERENCES, button.getSelection());
			}
		});
	}

	private void createMaxDisplay(Composite parent) {
		String toolTip = "Maximum number of rows to display";
		Label querySpinnerLabel = new Label(parent, SWT.NONE);
		querySpinnerLabel.setText("Max Display:");
		querySpinnerLabel.setToolTipText(toolTip);

		_querySpinner = new Spinner(parent, SWT.BORDER | SWT.LEFT);
		_querySpinner.setMinimum(1);
		_querySpinner.setMaximum(1000000);
		_querySpinner.setIncrement(1);
		_querySpinner.setPageIncrement(100);
		_querySpinner.setSelection(100);
		_querySpinner.setToolTipText(toolTip);
	}

	private void createToolbar(Composite parent) {
		ToolBar bar = new ToolBar(parent, SWT.RIGHT);

		final ToolItem runItem = new ToolItem(bar, SWT.NONE);
		runItem.setImage(getRunImage());
		runItem.setToolTipText("Run Query");
		runItem.setText("Run Query");

		runItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runQuery();
			}
		});
	}

	@Override
	public String getFilename() {
		return _editor.getTitle();
	}

	@Override
	public int getMaxCount() {
		return _querySpinner.getSelection();
	}

	@Override
	public String getQuery() {
		return _editor.getDocumentProvider().getDocument(_editor.getEditorInput()).get();
	}

	@Override
	public boolean isCreatedNewTab() {
		return _tabCheck.getSelection();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////
	private Image getRunImage() {
		if (_imgRun == null) {
			_imgRun = XQueryUI.getImageDescriptor("icons/run.gif").createImage();
		}
		return _imgRun;
	}

	public void runQuery() {

		if (_context == null) {
			onSelectContext();
		}

		if (_context != null) {
			IWorkbenchPage activePage = XQueryUI.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				ResultView view = (ResultView) activePage.showView(ResultView.ID, null, IWorkbenchPage.VIEW_VISIBLE);
				IQueryFrame frame = view.createQueryFrame(this);
				_context.run(frame);
			} catch (PartInitException e1) {
				Status status = new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID, e1.getMessage(), e1);
				XQueryUI.getDefault().errorDialog("Run XQuery", e1.getMessage(), status);
				e1.printStackTrace();
			}
		}
	}

	private void onSelectContext() {
		SelectContextWizard wizard = new SelectContextWizard();
		wizard.init(XQueryUI.getDefault().getWorkbench(), null);
		wizard.setForcePreviousAndNextButtons(true);
		WizardDialog dialog = new WizardDialog(XQueryUI.getDefault().getWorkbench().getDisplay().getActiveShell(),
				wizard);
		dialog.open();
	}

}
