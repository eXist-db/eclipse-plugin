package org.exist.eclipse.browse.internal.views.document;

import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.BrowseCoordinator;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseItemListener;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.DocumentCoordinator;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentItemListener;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.listener.ConnectionRegistration;
import org.exist.eclipse.listener.IConnectionListener;
import org.exist.xquery.util.URIUtils;
import org.xmldb.api.base.XMLDBException;

/**
 * This view shows all documents from a collection from the XMLDB.
 * 
 * @author Pascal Schmidiger
 */
public class DocumentView extends ViewPart implements IConnectionListener,
		IBrowseItemListener, IDocumentItemListener {
	public static final String ID = "org.exist.eclipse.browse.internal.views.document.DocumentView";
	private TableViewer _viewer;
	private ActionGroupMain _agMain;
	private ActionGroupOpenDocument _agOpenDocument;
	private ActionGroupDocumentExtension _agDocumentExtension;
	private IBrowseItem _item;
	private String _origPartname;
	private Text _textFilter;
	private Text _textCount;
	private Button _buttonSubmit;

	/**
	 * The constructor.
	 */
	public DocumentView() {
	}

	@Override
	public void dispose() {
		ConnectionRegistration.removeListener(this);
		BrowseCoordinator.getInstance().removeListener(this);
		DocumentCoordinator.getInstance().removeListener(this);
		super.dispose();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 6;
		parent.setLayout(layout);

		// text control - filter
		_textFilter = new Text(parent, SWT.LEFT | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 5;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		_textFilter.setLayoutData(gd);
		_textFilter.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.character == SWT.CR) {
					refresh();
					_textFilter.setFocus();
				}
			}
		});

		// button control - filter
		_buttonSubmit = new Button(parent, SWT.LEFT);
		_buttonSubmit.setText("Filter");
		gd = new GridData();
		gd.horizontalSpan = 1;
		_buttonSubmit.setLayoutData(gd);
		_buttonSubmit.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				refresh();

			}
		});

		_viewer = new TableViewer(parent, SWT.VIRTUAL | SWT.FILL);
		// _documentFilter = new DocumentFilter();
		// _viewer.setFilters(new ViewerFilter[] { _documentFilter });
		_viewer.setContentProvider(new ViewContentProvider(this));
		_viewer.setLabelProvider(new ViewLabelProvider());
		_viewer.setUseHashlookup(true);
		_viewer.setSorter(new NameSorter());
		_viewer.getTable().setLinesVisible(true);
		_viewer.getControl().addKeyListener(new DocumentKeyAdapter(this));
		_viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				_agOpenDocument.hookDoubleClickAction();
			}
		});
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 6;
		_viewer.getTable().setLayoutData(gd);

		// text control - count
		_textCount = new Text(parent, SWT.LEFT | SWT.BORDER);
		_textCount.setEnabled(false);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 6;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		_textCount.setLayoutData(gd);

		createInput();
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		ConnectionRegistration.addListener(this);
		BrowseCoordinator.getInstance().addListener(this);
		DocumentCoordinator.getInstance().addListener(this);
		_origPartname = getPartName();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		_viewer.getControl().setFocus();
	}

	public void added(IConnection connection) {
		// do nothing
	}

	public void removed(IConnection connection) {
		// do nothing
	}

	public void closed(IConnection connection) {
		if (_item != null && connection != null) {
			if (connection.equals(_item.getConnection())) {
				setItem(null);
			}
		}
	}

	public void opened(IConnection connection) {
		// do nothing
	}

	public void added(IBrowseItem item) {
		// do nothing
	}

	public void removed(IBrowseItem[] items) {
		if (items != null) {
			for (IBrowseItem item : items) {
				if (item.equals(_item) || item.contains(_item)) {
					setItem(null);
					break;
				}
			}
		}
	}

	public void refresh(IBrowseItem item) {
		if (item != null) {
			if (item.equals(_item)) {
				refresh();
			}
		}
	}

	public void moved(IBrowseItem fromItem, IBrowseItem toItem) {
		if (fromItem.equals(_item)) {
			setItemInternal(toItem);
		}

	}

	public void removed(IDocumentItem item) {
		if (item != null) {
			if (item.getParent().equals(_item)) {
				refresh();
			}
		}
	}

	public void moved(IDocumentItem fromItem, IDocumentItem toItem) {
		if (fromItem.getParent().equals(_item)) {
			setItem(toItem.getParent());
		}
	}

	public void reload(IDocumentItem item) {
		refresh(item.getParent());
	}

	// //////////////////////////////////////////////////////////////////////////
	// //////////////////
	// protected methods
	// //////////////////////////////////////////////////////////////////////////
	// //////////////////
	protected final IBrowseItem getItem() {
		return _item;
	}

	protected void setItem(IBrowseItem item) {
		setItemInternal(item);
		createInput();
	}

	protected boolean hasItem() {
		return _item != null;
	}

	protected final TableViewer getViewer() {
		return _viewer;
	}

	protected void refresh() {
		createInput();
		setFocus();
	}

	// //////////////////////////////////////////////////////////////////////////
	// //////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	// //////////////////

	private void setItemInternal(IBrowseItem item) {
		_item = item;
		if (_item == null) {
			setPartName(_origPartname);
		} else {
			setPartName(_item.getPath() + " ("
					+ _item.getConnection().getName() + ")");
		}
	}

	private void createInput() {
		if (getItem() != null
				&& IManagementService.class.cast(
						getItem().getConnection().getAdapter(
								IManagementService.class)).check()
				&& IBrowseService.class.cast(
						getItem().getAdapter(IBrowseService.class)).check()) {
			String[] elements;
			TreeSet<String> sorted = new TreeSet<String>();
			try {
				elements = getItem().getCollection().listResources();
				String filter = _textFilter.getText();
				boolean applyFilter = filter.length() > 0;
				for (String element : elements) {
					String decElement = URIUtils.urlDecodeUtf8(element);
					if (applyFilter) {
						if (decElement.matches(filter)) {
							sorted.add(decElement);
						}
					} else {
						sorted.add(decElement);
					}
				}
			} catch (XMLDBException e) {
				StringBuilder message = new StringBuilder(50).append(
						"Error while fetching documents for collection '")
						.append(getItem()).append("'");
				IStatus status = new Status(Status.ERROR, BrowsePlugin.getId(),
						message.toString(), e);
				BrowsePlugin.getDefault().getLog().log(status);
				BrowsePlugin.getDefault().errorDialog(message.toString(),
						e.getMessage(), status);
			} catch (ConnectionException e) {
				StringBuilder message = new StringBuilder(50).append(
						"Error while fetching documents for collection '")
						.append(getItem()).append("'");
				IStatus status = new Status(Status.ERROR, BrowsePlugin.getId(),
						message.toString(), e);
				BrowsePlugin.getDefault().getLog().log(status);
				BrowsePlugin.getDefault().errorDialog(message.toString(),
						e.getMessage(), status);
			} catch (PatternSyntaxException e) {
				String message = "Error in regex expression";
				Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(),
						message, e);
				BrowsePlugin.getDefault().errorDialog(message, e.getMessage(),
						status);
			}
			_viewer.setItemCount(sorted.size());
			_viewer.setInput(sorted.toArray(new String[sorted.size()]));
			_textCount.setText("Count: " + sorted.size());
		} else {
			_viewer.setItemCount(0);
			_viewer.setInput(new String[0]);
			_textCount.setText("Count: 0");
		}
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				DocumentView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(_viewer.getControl());
		_viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, _viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		_agMain.fillActionBars(bars);
	}

	private void fillContextMenu(IMenuManager manager) {
		_agMain.fillContextMenu(manager);
		manager.add(new Separator());
		_agOpenDocument.fillContextMenu(manager);
		_agDocumentExtension.fillContextMenu(manager);
	}

	private void makeActions() {
		_agMain = new ActionGroupMain(this);
		_agMain.makeActions();
		_agOpenDocument = new ActionGroupOpenDocument(this);
		_agDocumentExtension = new ActionGroupDocumentExtension(this);
	}
}