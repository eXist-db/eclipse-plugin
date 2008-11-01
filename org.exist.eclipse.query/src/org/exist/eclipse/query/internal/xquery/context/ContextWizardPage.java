/**
 * 
 */
package org.exist.eclipse.query.internal.xquery.context;

import java.util.Collection;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
	private TableViewer _viewer;
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
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);

		// Table Viewer
		_viewer = new TableViewer(container, SWT.VIRTUAL);
		_viewer.setContentProvider(new ContextViewContentProvider());
		_viewer.setLabelProvider(new ContextViewLabelProvider());
		_viewer.setUseHashlookup(true);
		_viewer.getTable().setLinesVisible(true);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		_viewer.getTable().setLayoutData(gd);

		_viewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				dialogChanged();
				if (getWizard().canFinish()) {
					getWizard().performFinish();
					WizardDialog.class.cast(getWizard().getContainer()).close();
				}
			}
		});

		_viewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		setMessage("Getting collections...");
		setPageComplete(false);
		setControl(container);
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				fillItems();
			}
		});
	}

	@Override
	public IConnectionContext getConnectionContext() {
		IBrowseItem item = BrowseHelper.getBrowseItem(_connection, _viewer
				.getTable().getSelection()[0].getText());
		return _existContextSwitcher.getConnectionContext(item);
	}

	// --------------------------------------------------------------------------
	// Private Methods
	// --------------------------------------------------------------------------

	/**
	 * Fills the item list with the according data.
	 */
	private void fillItems() {
		Collection<String> collections = BrowseHelper
				.getCollections(_connection);

		_viewer.setItemCount(collections.size());
		_viewer.setInput(collections.toArray(new String[collections.size()]));
		_viewer.getTable().select(0);
		setErrorState(null);
	}

	/**
	 * Handles the situation if the dialog changed.
	 */
	private void dialogChanged() {
		if (_viewer.getTable().getSelectionCount() < 1) {
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
		setMessage(null);
		setErrorMessage(message);
		setPageComplete(message == null);
	}

}
