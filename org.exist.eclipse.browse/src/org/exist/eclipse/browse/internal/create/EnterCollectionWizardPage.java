package org.exist.eclipse.browse.internal.create;

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
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * Enter the new collection name in this page.
 * 
 * @author Pascal Schmidiger
 */
public class EnterCollectionWizardPage extends WizardPage {
	private Text _name;
	private ISelection _selection;
	private final IBrowseItem _item;

	public EnterCollectionWizardPage(ISelection selection, IBrowseItem item) {
		super("entercollectionwizardpage");
		_item = item;
		setTitle("Create a collection");
		setDescription("Enter a new collection name");
		setImageDescriptor(BrowsePlugin
				.getImageDescriptor("icons/existdb.png"));
		_selection = selection;
	}

	@Override
	public String getName() {
		String name = _name.getText();
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
		layout.numColumns = 3;

		// collection
		Label collectionLabel = new Label(container, SWT.NULL);
		collectionLabel.setText("Collection");

		Text collection = new Text(container, SWT.BORDER | SWT.SINGLE);
		collectionLabel.setText("Collection");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		collection.setLayoutData(gd);
		collection.setText(_item.getPath());
		collection.setEnabled(false);

		// name
		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText("Name");
		_name = new Text(container, SWT.BORDER | SWT.SINGLE);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		_name.setLayoutData(gd);
		_name.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		dialogChanged();
		setControl(container);
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

	private void dialogChanged() {
		String name = getName();

		// check one param after the other that needs to be activated
		if (name.length() == 0) {
			setErrorState("Enter a name");
		} else if (!isUniqueName(name)) {
			setErrorState("Collection already exists");
		} else {
			setErrorState(null);
		}
	}

	private boolean isUniqueName(String name) {
		return !_item.getChild(name).exists();
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
