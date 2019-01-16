package org.exist.eclipse.browse.internal.move;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.exist.eclipse.browse.browse.BrowseHelper;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * Enter the new name or path of the collection.
 */
public class MoveCollectionWizardPage extends WizardPage {
	private Text _newCol;
	private Text _enterCol;
	private ISelection _selection;
	private final IBrowseItem _item;
	private IBrowseItem _rootItem;

	public MoveCollectionWizardPage(ISelection selection, IBrowseItem item) {
		super("movecollectionwizardpage");
		_item = item;
		_rootItem = BrowseHelper.getRootBrowseItem(_item.getConnection());
		setTitle("Rename/Move a collection");
		setDescription("Enter a new collection name");
		setImageDescriptor(BrowsePlugin
				.getImageDescriptor("icons/existdb.png"));
		_selection = selection;
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
		container.setLayout(layout);
		layout.numColumns = 4;

		// current collection
		Label curLabel = new Label(container, SWT.NULL);
		curLabel.setText("Current:");

		Text curCol = new Text(container, SWT.BORDER);
		curCol.setText(_item.getPath());
		curCol.setEnabled(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		curCol.setLayoutData(gd);

		// new collection
		Label newLabel = new Label(container, SWT.NULL);
		newLabel.setText("New:");

		_newCol = new Text(container, SWT.BORDER);
		_newCol.setEnabled(false);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		_newCol.setLayoutData(gd);

		// enter collection
		Label enterLabel = new Label(container, SWT.NULL);
		enterLabel.setText("Input:");

		_enterCol = new Text(container, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		_enterCol.setLayoutData(gd);
		_enterCol.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		if (_item.getParent().isRoot()) {
			_enterCol.setText("New" + _item.getName());
		} else {
			_enterCol.setText(_item.getParent().getPath().substring(4)
					+ "/New" + _item.getName());
		}
		_enterCol.selectAll();

		initialize();
		dialogChanged();
		setControl(container);
	}

	public IBrowseItem getNewItem() {
		return _rootItem.getChild(getColName());
	}

	//
	// private methods
	//

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */
	private void initialize() {
		if (_selection != null && _selection.isEmpty() == false
				&& _selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) _selection;
			if (ssel.size() > 1)
				return;
		}
	}

	private String getColName() {
		String name = _enterCol.getText();
		if (name.length() > 0) {
			while (name.startsWith("/")) {
				name.substring(1);
			}
			while (name.lastIndexOf("/") == name.length() - 1) {
				name = name.substring(0, name.length() - 1);
			}
		}
		return name;
	}

	private void dialogChanged() {
		String name = getColName();
		_newCol.setText(_rootItem.getChild(name).getPath());

		// check one param after the other that needs to be activated
		if (name.length() < 1) {
			setErrorState("Enter a name");
		} else if (!isUniqueName(name)) {
			setErrorState("Collection already exists");
		} else {
			setErrorState(null);
		}
	}

	private boolean isUniqueName(String name) {
		return !_rootItem.getChild(name).exists();
	}

	/**
	 * Sets the wizard to an error state. In the header an error message gets
	 * displayed.
	 * 
	 * @param message
	 *            message displayed in the header
	 */
	private void setErrorState(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

}
