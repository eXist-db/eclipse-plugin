package org.exist.eclipse.browse.internal.create;

import org.eclipse.core.runtime.IConfigurationElement;
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
import org.exist.eclipse.browse.document.IDocumentItem;

/**
 * Enter the new document name in this page.
 * 
 * @author Pascal Schmidiger
 */
public class EnterDocumentWizardPage extends WizardPage {
	private final IBrowseItem _item;
	private Text _name;
	private IConfigurationElement _element;

	public EnterDocumentWizardPage(IBrowseItem item) {
		super("enterdocumentwizardpage");
		_item = item;
		setDefaultDescription();
	}

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
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		_name.setFocus();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Change the document provider, with which the document will save at the
	 * end.
	 * 
	 * @param element
	 */
	protected void setDocumentProvider(IConfigurationElement element) {
		_element = element;
		IConfigurationElement[] description = element
				.getChildren("description");
		if (description.length > 0) {
			setDescription(description[0].getValue());
		} else {
			setDefaultDescription();
		}
		String default_filename = element.getAttribute("default");
		if (default_filename == null) {
			_name.setText("");
		} else {
			_name.setText(default_filename);
		}
	}

	protected IConfigurationElement getDocumentProvider() {
		return _element;
	}

	protected IDocumentItem getDocumentItem() {
		if (isPageComplete()) {
			return _item.getDocument(_name.getText());
		}
		return null;
	}

	////////////////////////////////////////////////////////////////////////////
	// private methods
	////////////////////////////////////////////////////////////////////////////

	private void setDefaultDescription() {
		setDescription("Enter a new document name");
	}

	private void dialogChanged() {
		String name = _name.getText();

		// check one param after the other that needs to be activated
		if (name.length() == 0) {
			setErrorState("Enter a name");
		} else if (!isUniqueName(name)) {
			setErrorState("Document already exists");
		} else {
			setErrorState(null);
		}
	}

	private boolean isUniqueName(String name) {
		return !_item.getDocument(name).exists();
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
