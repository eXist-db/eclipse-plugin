package org.exist.eclipse.browse.internal.views.document;

import java.util.TreeSet;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
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
import org.exist.eclipse.browse.internal.delete.DeleteDocumentListener;
import org.exist.eclipse.browse.internal.views.browse.BrowseView;
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
	private Label _labelCount;

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
		layout.marginHeight = 2;
		layout.marginWidth = 0;
		layout.verticalSpacing = 2;
		parent.setLayout(layout);

		Composite inner = new Composite(parent, SWT.NONE);
		GridLayout innerLayout = new GridLayout();
		innerLayout.marginHeight = 0;
		innerLayout.marginWidth = 2;
		innerLayout.numColumns = 2;
		GridData innerGd = new GridData(GridData.FILL_HORIZONTAL);
		inner.setLayoutData(innerGd);

		inner.setLayout(innerLayout);
		// text control - filter
		Label filterLabel = new Label(inner, SWT.NONE);
		filterLabel.setText("Filter:");
		filterLabel.setLayoutData(new GridData(0));
		_textFilter = new Text(inner, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		_textFilter.setLayoutData(gd);

		_textFilter.addModifyListener(new ModifyListener() {

			Runnable _refresher = new Runnable() {
				public void run() {
					refresh();
				}
			};

			public void modifyText(ModifyEvent e) {
				e.display.timerExec(-1, _refresher);
				e.display.timerExec(e.display.getDoubleClickTime(), _refresher);
			}
		});

		_viewer = new TableViewer(parent, SWT.VIRTUAL | SWT.MULTI);
		_viewer.setContentProvider(new ViewContentProvider(this));
		_viewer.setLabelProvider(new ViewLabelProvider());
		_viewer.setUseHashlookup(true);
		_viewer.setSorter(new NameSorter());
		_viewer.getTable().addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				onPaintTable(e);
			}
		});

		// SWT.FULL_SELECTION does not have an effect. create effect with
		// TableColumn that fill's horizontally
		_viewer.getTable().addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				_viewer.getTable().getColumns()[0]
						.setWidth(_viewer.getTable().getSize().x
								- (_viewer.getTable().getVerticalBar()
										.getSize().x + 5));
			}
		});
		new TableColumn(_viewer.getTable(), SWT.NONE);
		_viewer.getControl().addKeyListener(new DocumentKeyAdapter(this));
		_viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IDocumentItem first = (IDocumentItem) ((IStructuredSelection) _viewer
						.getSelection()).getFirstElement();
				if (first != null) {
					new ActionOpenDocument(ActionGroupOpenDocument
							.getDefaultEditor(first).getId(), _viewer).run();
				}
			}
		});
		gd = new GridData(GridData.FILL_BOTH);
		_viewer.getTable().setLayoutData(gd);

		// text control - count
		_labelCount = new Label(parent, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = 2;
		_labelCount.setLayoutData(gd);

		createInput();
		makeActions();
		hookContextMenu();
		contributeToActionBars();
		ConnectionRegistration.addListener(this);
		BrowseCoordinator.getInstance().addListener(this);
		DocumentCoordinator.getInstance().addListener(this);
		_origPartname = getPartName();

		DocumentDnD.install(this);
	}

	protected void onPaintTable(PaintEvent e) {
		String msg = null;
		if (!hasItem()) {
			String explorerName = PlatformUI.getWorkbench().getViewRegistry()
					.find(BrowseView.ID).getLabel();
			msg = "To display documents, double click a collection\nin "
					+ explorerName + ".";
		} else if (getViewer().getTable().getItemCount() == 0) {
			msg = "No documents found";
		}

		if (msg != null) {
			GC gc = e.gc;
			gc.drawText(msg, 4, 4);
		}
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
		_agOpenDocument.updateActionBars();
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
				String filter = _textFilter.getText().toLowerCase();
				boolean applyFilter = filter.length() > 0;
				for (String element : elements) {
					String decElement = URIUtils.urlDecodeUtf8(element);
					if (applyFilter) {
						if (decElement.toLowerCase().indexOf(filter) != -1) {
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
			_labelCount.setText("Count: " + sorted.size());
		} else {
			_viewer.setItemCount(0);
			_viewer.setInput(new String[0]);
			_labelCount.setText("Count: 0");
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

		_agOpenDocument.fillActionBars(bars);

		bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(),
				new ActionRefresh(this));

		bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), new Action() {
			@Override
			public void run() {
				ActionDocumentListener listener = new ActionDocumentListener(
						DocumentView.this, new DeleteDocumentListener());
				listener.run();
			}
		});
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