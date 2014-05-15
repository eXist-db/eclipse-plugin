package org.exist.eclipse.browse.internal.views.browse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.browse.BrowseCoordinator;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseItemListener;
import org.exist.eclipse.browse.internal.create.ImportDocumentsListener;
import org.exist.eclipse.browse.internal.delete.DeleteCollectionListener;
import org.exist.eclipse.browse.internal.refresh.RefreshCollectionListener;
import org.exist.eclipse.listener.ConnectionRegistration;
import org.exist.eclipse.listener.IConnectionListener;

/**
 * This view shows all collections from the XMLDB as a tree.
 * 
 * @author Pascal Schmidiger
 * @see IConnectionListener
 * @see ViewContentProvider
 * @see ViewLabelProvider
 * @see NameSorter
 * @see BrowseKeyAdapter
 */
public class BrowseView extends ViewPart implements IConnectionListener,
		IBrowseItemListener {
	public static final String ID = "org.exist.eclipse.browse.internal.views.browse.BrowseView";
	private TreeViewer _viewer;
	private DrillDownAdapter _drillDownAdapter;
	private Action _doubleClickAction;
	private ActionGroup _agCollectionExtension;
	private ActionGroup _agConnectionExtension;
	private ActionGroupMain _agMain;

	/**
	 * The constructor.
	 */
	public BrowseView() {
	}

	@Override
	public void dispose() {
		ConnectionRegistration.removeListener(this);
		BrowseCoordinator.getInstance().removeListener(this);
		super.dispose();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		_viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		_drillDownAdapter = new DrillDownAdapter(_viewer);
		_viewer.setContentProvider(new ViewContentProvider(this));
		_viewer.setLabelProvider(new ViewLabelProvider());
		_viewer.setSorter(new NameSorter());
		_viewer.setInput(getInitInput());
		_viewer.getControl().addKeyListener(new BrowseKeyAdapter(this));
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		ConnectionRegistration.addListener(this);
		BrowseCoordinator.getInstance().addListener(this);
		addDnDSupport();

		IActionBars bars = getViewSite().getActionBars();

		bars.setGlobalActionHandler(ActionFactory.REFRESH.getId(),
				new Action() {
					@Override
					public void run() {
						ActionBrowseListener listener = new ActionBrowseListener(
								BrowseView.this,
								new RefreshCollectionListener());
						listener.run();
					}
				});
		bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), new Action() {
			@Override
			public void run() {
				ActionBrowseListener listener = new ActionBrowseListener(
						BrowseView.this, new DeleteCollectionListener());
				listener.run();
			}
		});

	}

	public Object getInitInput() {
		return getViewSite();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		_viewer.getControl().setFocus();
	}

	/**
	 * @return the viewer
	 */
	public final TreeViewer getViewer() {
		return _viewer;
	}

	@Override
	public void added(IConnection connection) {
		getViewer().add(getViewSite(), connection);
		getViewer().setSelection(new StructuredSelection(connection));
	}

	@Override
	public void removed(IConnection connection) {
		getViewer().remove(connection);
	}

	@Override
	public void closed(IConnection connection) {
		getViewer().refresh(connection);
	}

	@Override
	public void opened(IConnection connection) {
		getViewer().refresh(connection);
	}

	public final DrillDownAdapter getDrillDownAdapter() {
		return _drillDownAdapter;
	}

	@Override
	public void added(IBrowseItem item) {
		getViewer().expandToLevel(item.getParent(), 1);
		getViewer().add(item.getParent(), item);
		getViewer().setSelection(new StructuredSelection(item), true);
	}

	@Override
	public void removed(IBrowseItem[] items) {
		getViewer().remove(items);
	}

	@Override
	public void refresh(IBrowseItem item) {
		getViewer().refresh(item);
	}

	@Override
	public void moved(IBrowseItem fromItem, IBrowseItem toItem) {
		removed(new IBrowseItem[] { fromItem });
		added(toItem);
	}

	//
	// private methods
	//
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				BrowseView.this.fillContextMenu(manager);
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
		_agCollectionExtension.fillContextMenu(manager);
		_agConnectionExtension.fillContextMenu(manager);
	}

	private void makeActions() {
		_agMain = new ActionGroupMain(this);
		_agMain.makeActions();

		// double click
		_doubleClickAction = new ActionDoubleClick(this);

		// browseactiongroup
		_agCollectionExtension = new ActionGroupCollectionExtension(this);

		// connectionactiongroup
		_agConnectionExtension = new ActionGroupConnectionExtension(this);
	}

	private void hookDoubleClickAction() {
		_viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				_doubleClickAction.run();
			}
		});
	}

	private void addDnDSupport() {
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		DropTarget target = new DropTarget(_viewer.getTree(), operations);
		target.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		target.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {

				Object item = event.item.getData();

				if (event.data == null || !(item instanceof IBrowseItem)) {
					event.detail = DND.DROP_NONE;
					return;
				}
				List<File> files = new ArrayList<>();
				for (String it : (String[]) event.data) {
					files.add(new File(it));
				}

				new ImportDocumentsListener().importFiles((IBrowseItem) item,
						files);
			}
		});

	}
}