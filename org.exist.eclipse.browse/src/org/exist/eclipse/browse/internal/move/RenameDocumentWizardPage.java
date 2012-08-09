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
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * Enter the new name of the document in this page.
 */
public class RenameDocumentWizardPage extends WizardPage {
	private Text _newText;
	private ISelection _selection;
	private final IDocumentItem _item;

	public RenameDocumentWizardPage(ISelection selection, IDocumentItem item) {
		super("movecollectionwizardpage");
		_item = item;
		setTitle("Rename a document");
		setDescription("Enter a new document name");
		setImageDescriptor(BrowsePlugin
				.getImageDescriptor("icons/hslu_exist_eclipse_logo.jpg"));
		_selection = selection;
	}

	/**
	 * Get Credentials for the connection to the database.
	 * 
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 4;

		// collection
		Label curLabel = new Label(container, SWT.NULL);
		curLabel.setText("Current:");

		Text curText = new Text(container, SWT.SINGLE);
		curText.setText(_item.getName());
		curText.setEnabled(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		curText.setLayoutData(gd);

		// name
		Label newLabel = new Label(container, SWT.NULL);
		newLabel.setText("New:");

		_newText = new Text(container, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		_newText.setLayoutData(gd);
		_newText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		_newText.setText("New" + _item.getName());
		_newText.selectAll();
		initialize();
		dialogChanged();
		setControl(container);
	}

	public IDocumentItem getNewItem() {
		return _item.getParent().getDocument(_newText.getText());
	}

	////////////////////////////////////////////////////////////////////////////
	// //////////////////
	// private methods
	////////////////////////////////////////////////////////////////////////////
	// //////////////////

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
		String name = _newText.getText();

		// check one param after the other that needs to be activated
		if (name.length() < 1) {
			setErrorState("Enter a name");
		} else if (!isUniqueName(name)) {
			setErrorState("Document already exists");
		} else {
			setErrorState(null);
		}
	}

	private boolean isUniqueName(String name) {
		return !_item.getParent().getDocument(name).exists();
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
