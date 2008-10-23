package org.exist.eclipse.browse.internal.delete;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * This page shows all collections, which will deleted.
 */
public class ListCollectionWizardPage extends WizardPage {
	private ISelection _selection;
	private final IBrowseItem[] _items;
	private List _colList;

	public ListCollectionWizardPage(ISelection selection, IBrowseItem[] items) {
		super("listcollectionwizardpage");
		_items = items;
		setTitle("Delete");
		setDescription("Delete the following collection(s)");
		setImageDescriptor(BrowsePlugin
				.getImageDescriptor("icons/hslu_exist_eclipse_logo.jpg"));
		_selection = selection;
		setPageComplete(true);
	}

	/**
	 * Get Credentials for the connection to the database.
	 * 
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);
		_colList = new List(container, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		gd.verticalSpan = 1;
		_colList.setLayoutData(gd);

		fillList();
		initialize();
		setControl(container);
	}

	private void fillList() {
		for (IBrowseItem item : _items) {
			_colList.add(item.getPath());
		}
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

}
