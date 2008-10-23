package org.exist.eclipse.query.internal.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.exist.eclipse.query.internal.control.IQueryInit;
import org.exist.eclipse.query.internal.proc.IQueryListener;
import org.exist.eclipse.query.internal.proc.IQueryResultListener;
import org.exist.eclipse.query.internal.proc.QueryEndEvent;
import org.exist.eclipse.query.internal.proc.QueryNotifier;
import org.exist.eclipse.query.internal.proc.QueryResultEvent;
import org.exist.eclipse.query.internal.proc.QueryResultNotifier;
import org.exist.eclipse.query.internal.proc.QueryStartEvent;

/**
 * This class is responsible for the query view body.
 * 
 * @author Markus Tanner
 * @uml.dependency supplier="org.exist.eclipse.query.internal.control.IQueryInit"
 */
public class BodyContainer extends Composite implements IQueryListener,
		IQueryResultListener {
	private Text _queryInput;
	private Table _resultTable;
	private TableColumn _queryResultCol;
	private TableColumn _queryResultColNr;
	private final QueryView _view;
	private IQueryInit _queryInitiator;
	private ResultSelectionListener _resultMouseListener;

	public BodyContainer(Composite parent, int style, QueryView view,
			IQueryInit queryInitiator) {
		super(parent, style);
		_view = view;
		_queryInitiator = queryInitiator;
	}

	public void init() {
		FillLayout bodyLayout = new FillLayout();
		this.setLayout(bodyLayout);
		GridData gd = new GridData();
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		this.setLayoutData(gd);
		composeSash(this);

		QueryResultNotifier.getInstance().addListener(this);
		QueryNotifier.getInstance().addListener(this);
	}

	@Override
	public void dispose() {
		QueryResultNotifier.getInstance().removeListener(this);
		QueryNotifier.getInstance().removeListener(this);
		super.dispose();
	}

	/**
	 * Returns the query text.
	 * 
	 * @return query
	 */
	public String getQueryInput() {
		return _queryInput.getText();
	}

	public void end(QueryEndEvent event) {
		// ignore
	}

	public void start(final QueryStartEvent event) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				_resultTable.removeAll();
				_resultMouseListener.setId(event.getId());
			}
		});
	}

	public void addResult(final QueryResultEvent event) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				int row = _resultTable.getItemCount();
				TableItem resultItem = new TableItem(_resultTable, row);
				resultItem.setText(new String[] { Integer.toString(row + 1),
						event.getContent() });
				_queryResultColNr.pack();
				_queryResultCol.pack();
			}
		});

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// /////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method creates a sash-form which contains two text-areas. The
	 * resulting composite represents the body of the query view.
	 * 
	 * @param sashContainer
	 */
	private void composeSash(Composite sashContainer) {

		SashForm sashForm = new SashForm(sashContainer, SWT.VERTICAL);

		// query input area
		_queryInput = new Text(sashForm, SWT.LEFT | SWT.V_SCROLL | SWT.BORDER);
		_queryInput.setToolTipText("Query Input");
		Font font = _queryInput.getFont();
		_queryInput.setFont(new Font(font.getDevice(), font.getFontData()[0]
				.getName(), 10, SWT.NORMAL));
		_queryInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.stateMask & SWT.CTRL) != 0) {
					if ((e.character) == SWT.CR) {
						_queryInitiator.triggerQuery();
					}
				}
			}
		});

		// query result area
		_resultTable = new Table(sashForm, SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.BORDER | SWT.WRAP | SWT.FULL_SELECTION);
		_resultTable.setHeaderVisible(true);
		_resultTable.setLinesVisible(true);
		_queryResultColNr = new TableColumn(_resultTable, SWT.NONE);
		_queryResultColNr.setText("Nr.");
		_queryResultColNr.pack();
		_queryResultCol = new TableColumn(_resultTable, SWT.NONE);
		_queryResultCol.setText("Query Result");
		_queryResultCol.pack();

		_resultMouseListener = new ResultSelectionListener(_view.getViewSite()
				.getWorkbenchWindow());
		_resultTable.addMouseListener(_resultMouseListener);
	}
}
