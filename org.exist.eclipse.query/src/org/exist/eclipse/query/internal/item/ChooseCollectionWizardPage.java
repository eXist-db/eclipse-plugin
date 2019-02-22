package org.exist.eclipse.query.internal.item;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.browse.BrowseHelper;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.query.internal.QueryPlugin;

/**
 * Chose the collection on this page.
 * 
 * @author Pascal Schmidiger
 */
public class ChooseCollectionWizardPage extends WizardPage {

	private List _itemList;
	private final IConnection _connection;
	private final ISelection _selection;
	private final IBrowseItem _item;

	public ChooseCollectionWizardPage(ISelection selection, IConnection connection, IBrowseItem item) {
		super("choosecollectionwizardpage");
		_selection = selection;
		_connection = connection;
		_item = item;
		setTitle("Choose Collection");
		setDescription("Choose a collection for the query");
		setImageDescriptor(QueryPlugin.getImageDescriptor("icons/existdb.png"));
	}

	/**
	 * Get Credentials for the connection to the database.
	 * 
	 * @see IDialogPage#createControl(Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);

		_itemList = new List(container, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		_itemList.setLayoutData(gd);

		refresh();
		_itemList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		_itemList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				dialogChanged();
				if (getWizard().canFinish()) {
					getWizard().performFinish();
					WizardDialog.class.cast(getWizard().getContainer()).close();
				}
			}
		});
		initialize();
		dialogChanged();
		setControl(container);
	}

	public IBrowseItem getSelection() {
		return BrowseHelper.getBrowseItem(_connection, _itemList.getSelection()[0]);
	}

	public void refresh() {
		fillItems();
		_itemList.pack();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////
	private void initialize() {
		if (_selection != null && _selection.isEmpty() == false && _selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) _selection;
			if (ssel.size() > 1)
				return;
		}
	}

	private void fillItems() {
		_itemList.removeAll();
		try {
			IBrowseItem rootBrowseItem = BrowseHelper.getRootBrowseItem(_connection);
			IBrowseService service = (IBrowseService) rootBrowseItem.getAdapter(IBrowseService.class);
			Set<IBrowseItem> children = service.getChildren(true, true);
			boolean selected = _item == null;
			for (IBrowseItem item : children) {
				_itemList.add(item.getPath());
				if (!selected && _item.equals(item)) {
					_itemList.select(_itemList.getItemCount() - 1);
					selected = true;
				}
			}
		} catch (ConnectionException e) {
			QueryPlugin.getDefault().getLog()
					.log(new Status(IStatus.ERROR, QueryPlugin.getId(), "Failure while fill items.", e));
		}
	}

	private void dialogChanged() {
		if (_itemList.getSelectionCount() < 1) {
			setMessage("Select a collection.");
		} else {
			setErrorState(null);
		}
	}

	/**
	 * Sets the wizard to an error state. In the header an error message gets
	 * displayed.
	 * 
	 * @param message message displayed in the header
	 */
	private void setErrorState(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

}
