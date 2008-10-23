/**
 * 
 */
package org.exist.eclipse.auto.internal.wizard;

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
import org.exist.eclipse.auto.connection.IAutoContext;
import org.exist.eclipse.auto.internal.AutoUI;

/**
 * On this WizardPage the collection for the query can be selected.
 * 
 * @author Markus Tanner
 */
public class RunAutoCollectionPage extends WizardPage {

	IAutoContext _autoContext = null;
	private List _itemList;

	/**
	 * RunAutoCollectionPage Constructor
	 */
	public RunAutoCollectionPage() {
		super("collectionwizardpage");
		setTitle("Run an Automation");
		setDescription("Select a collection for the automation.");
		setImageDescriptor(AutoUI.getDefault().getImageRegistry()
				.getDescriptor(AutoUI.IMG_EXIST_ECLIPSE_LOGO));
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);

		_itemList = new List(container, SWT.SINGLE | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		_itemList.setLayoutData(gd);

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

		setErrorState("Collection needs to be selected");
		setControl(container);
	}

	/**
	 * Refreshes the page
	 */
	public void refresh() {
		fillItems();
	}

	/**
	 * Returns the actual selection.
	 * 
	 * @return Selection
	 */
	public String getSelection() {
		return _itemList.getSelection()[0];
	}

	/**
	 * Sets the selection to the give IAutoContext.
	 * 
	 * @param autoContext
	 */
	public void setSelectedContext(IAutoContext autoContext) {
		_autoContext = autoContext;
		refresh();
	}

	//--------------------------------------------------------------------------
	// Private Methods
	//--------------------------------------------------------------------------

	/**
	 * Fills the item list with the according data.
	 */
	private void fillItems() {
		for (String item : _autoContext.getCollections()) {
			_itemList.add(item);
		}
		_itemList.select(0);
		setErrorMessage(null);
		setPageComplete(true);
	}

	/**
	 * Handles the situation if the dialog changed.
	 */
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
	 * @param message
	 *            message displayed in the header
	 */
	private void setErrorState(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

}
