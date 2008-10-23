/**
 * 
 */
package org.exist.eclipse.query.internal.xquery.context;

import org.eclipse.jface.wizard.WizardDialog;
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
import org.exist.eclipse.query.internal.QueryPlugin;
import org.exist.eclipse.xquery.ui.context.AbstractContextWizardPage;
import org.exist.eclipse.xquery.ui.context.IConnectionContext;

/**
 * In this wizard page, the user can choose the collection for the selected
 * connection.
 * 
 * @author Pascal Schmidiger
 */
public class ContextWizardPage extends AbstractContextWizardPage {
	private List _itemList;
	private final IConnection _connection;
	private final ContextSwitcher _existContextSwitcher;

	/**
	 * @param existContextSwitcher
	 * @param pageName
	 */
	protected ContextWizardPage(IConnection connection,
			ContextSwitcher existContextSwitcher) {
		super("existcontextwizardpage");
		_connection = connection;
		_existContextSwitcher = existContextSwitcher;
		setTitle("Select the context");
		setDescription("Select a collection for the context.");
		setImageDescriptor(QueryPlugin
				.getImageDescriptor("icons/hslu_exist_eclipse_logo.jpg"));
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);

		_itemList = new List(container, SWT.SINGLE | SWT.V_SCROLL
				| SWT.H_SCROLL);
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

		fillItems();

		setErrorState("Collection needs to be selected");
		setControl(container);
	}

	/**
	 * Returns the actual selection.
	 * 
	 * @return Selection
	 */
	public String getSelection() {
		return _itemList.getSelection()[0];
	}

	@Override
	public IConnectionContext getConnectionContext() {
		IBrowseItem item = BrowseHelper.getBrowseItem(_connection,
				getSelection());
		return _existContextSwitcher.getConnectionContext(item);
	}

	// --------------------------------------------------------------------------
	// Private Methods
	// --------------------------------------------------------------------------

	/**
	 * Fills the item list with the according data.
	 */
	private void fillItems() {
		for (String item : BrowseHelper.getCollections(_connection)) {
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
