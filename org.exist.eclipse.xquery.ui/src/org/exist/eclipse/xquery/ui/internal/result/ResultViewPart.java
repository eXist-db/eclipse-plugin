/**
 * 
 */

package org.exist.eclipse.xquery.ui.internal.result;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
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

	public static class PartFrame extends Composite {

		private ResultViewPart _part;

		public ResultViewPart getPart() {
			return _part;
		}

		protected void setPart(ResultViewPart part) {
			_part = part;
		}

		public PartFrame(Composite parent) {
			super(parent, SWT.NONE);
		}
	}

	private TableViewer _viewer;
	private Label _status;
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

	public void createPartControl(PartFrame parent) {
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);

		parent.setPart(this);

		GridData gd = new GridData();

		// Table Viewer
		_viewer = new TableViewer(parent, SWT.VIRTUAL | SWT.MULTI
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
		_viewer.getTable().setLayoutData(gd);

		ResultSelectionListener listener = new ResultSelectionListener();
		_viewer.addDoubleClickListener(listener);

		// Status label
		_status = new Label(parent, SWT.NONE);
		_status.pack();
		gd = new GridData();
		gd.horizontalIndent = 2;
		_status.setLayoutData(gd);
		hookContextMenu();
		addDnDSupport();
	}

	private void addDnDSupport() {
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		DragSource source = new DragSource(_viewer.getTable(), operations);
		source.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		source.addDragListener(new DragSourceAdapter() {
			@Override
			public void dragSetData(DragSourceEvent event) {
				try {
					if (TextTransfer.getInstance().isSupportedType(
							event.dataType)) {
						event.data = new CopyResultItemsAction(_viewer)
								.getCopyContent();
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(_viewer.getControl());
		_viewer.getControl().setMenu(menu);
	}

	protected void fillContextMenu(IMenuManager manager) {
		Action openInEditorAction = new Action("Open in Editor") {
			@Override
			public void run() {
				new ResultSelectionListener()
						.openEditor((IStructuredSelection) _viewer
								.getSelection());
			}
		};

		manager.add(openInEditorAction);

		manager.add(new Separator());
		manager.add(new CopyResultItemsAction(_viewer));
		manager.add(new Separator());
		manager.add(new ExportResultItemsAction(_viewer));
	}

	public void dispose() {
	}

	// //////////////////////////////////////////////////////////////////////////
	// protected methods
	// //////////////////////////////////////////////////////////////////////////

	protected final TableViewer getViewer() {
		return _viewer;
	}

	@Override
	public void end(final IQueryEndState state) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (state.getState().equals(IQueryEndState.State.OK)) {
					StringBuilder msg = new StringBuilder();
					msg.append(state.getFoundedItems()).append(" items found.");
					msg.append(" Compilation: ")
							.append(state.getCompiledTime()).append(" ms.");
					msg.append(" Execution: ").append(state.getExecutionTime())
							.append(" ms.");
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

	@Override
	public String getQuery() {
		return _query;
	}

	@Override
	public IResultFrame start() {
		_actualCount = 0;
		_results = new ArrayList<ResultItem>(_maxCount);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				_status
						.setText("Query Processing... (start at "
								+ DateFormat.getTimeInstance().format(
										new Date()) + ")");
				_status.pack();
			}
		});
		return this;
	}

	@Override
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

	@Override
	public String getName() {
		return _filename;
	}
}
