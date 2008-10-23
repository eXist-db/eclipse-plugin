/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.result;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.result.IQueryEndState;
import org.exist.eclipse.xquery.ui.result.IQueryFrame;
import org.exist.eclipse.xquery.ui.result.IResultFrame;

/**
 * This part represents the elements for one query running result.
 * 
 * @author Pascal Schmidiger
 */
public class ResultViewPart implements IQueryFrame, IResultFrame {

	private TableViewer _viewer;
	private Label _status;
	private Label _info;
	private int _actualCount;
	private int _maxCount;
	private String _query;
	private String _filename;
	private Collection<ResultItem> _results;
	private TableColumn _columnNr;
	private TableColumn _columnResult;
	private int _uniqueNr;

	public void initCreation(IQueryFrameInfo creation, int uniqueNr) {
		_uniqueNr = uniqueNr;
		_maxCount = creation.getMaxCount();
		_query = creation.getQuery();
		_filename = creation.getFilename();
	}

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);

		// Status label
		_info = new Label(parent, SWT.LEFT);
		_info.pack();
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		_info.setLayoutData(gd);

		// Table Viewer
		_viewer = new TableViewer(parent, SWT.VIRTUAL | SWT.FILL
				| SWT.FULL_SELECTION);
		_viewer.setContentProvider(new ResultViewContentProvider());
		_viewer.setLabelProvider(new ResultViewLabelProvider());
		_viewer.setUseHashlookup(true);
		_viewer.setSorter(new NameSorter());

		_columnNr = new TableColumn(_viewer.getTable(), SWT.NONE);
		_columnNr.setWidth(50);
		_columnNr.setText("Nr");
		_columnResult = new TableColumn(_viewer.getTable(), SWT.NONE);
		_columnResult.setWidth(200);
		_columnResult.setText("Result");
		_viewer.getTable().setLinesVisible(true);
		_viewer.getTable().setHeaderVisible(true);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		_viewer.getTable().setLayoutData(gd);

		ResultSelectionListener listener = new ResultSelectionListener();
		_viewer.getTable().addMouseListener(listener);

		// Status label
		_status = new Label(parent, SWT.LEFT);
		_status.pack();
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		_status.setLayoutData(gd);

	}

	public void dispose() {
	}

	// //////////////////////////////////////////////////////////////////////////
	// protected methods
	// //////////////////////////////////////////////////////////////////////////

	protected final TableViewer getViewer() {
		return _viewer;
	}

	public void end(final IQueryEndState state) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (state.getState().equals(IQueryEndState.State.OK)) {
					StringBuilder msg = new StringBuilder();
					msg.append(state.getFoundedItems()).append(" items found");
					msg.append(" Compilation: ")
							.append(state.getCompiledTime()).append(" ms");
					msg.append(" Execution: ").append(state.getExecutionTime())
							.append(" ms");
					_status.setText(msg.toString());
				} else {
					_status.setText(state.getException().getMessage());
					XQueryUI.getDefault().getLog().log(
							new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID,
									"Failure while running queries", state
											.getException()));
				}
				_viewer.setItemCount(_results.size());
				_viewer.setInput(_results.toArray(new ResultItem[_results
						.size()]));
				_columnResult.pack();
				_status.pack();
			}
		});
	}

	public String getQuery() {
		return _query;
	}

	public IResultFrame start() {
		_actualCount = 0;
		_results = new ArrayList<ResultItem>(_maxCount);
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				_info.setText("File: " + _filename);
				_info.pack();
				_status.setText("Query Processing...");
				_status.pack();
			}
		});
		return this;
	}

	public boolean addResult(String content) {
		if (_actualCount < _maxCount) {
			_results.add(new ResultItem(_uniqueNr, _filename, _actualCount,
					content));
			_actualCount++;
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return _filename;
	}
}
