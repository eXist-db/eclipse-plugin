package org.exist.eclipse.query.internal.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.browse.BrowseCoordinator;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseItemListener;
import org.exist.eclipse.listener.ConnectionRegistration;
import org.exist.eclipse.listener.IConnectionListener;
import org.exist.eclipse.query.internal.control.IQueryInit;
import org.exist.eclipse.query.internal.item.ChangeItemNotifier;
import org.exist.eclipse.query.internal.item.IChangeItemListener;

/**
 * This class is responsible for the query view top.
 * 
 * @author Markus Tanner
 * @uml.dependency supplier="org.exist.eclipse.query.internal.control.IQueryInit"
 */
public class TopContainer extends Composite implements IChangeItemListener,
		IConnectionListener, IBrowseItemListener {

	private Text _contextSelection;
	private Button _execQueryButton;
	private IBrowseItem _item;
	private IQueryInit _queryInit;
	private Spinner _querySpinner;
	private Label _querySpinnerLabel;
	private final String _noContextSelected = "< no collection selected >";

	public TopContainer(Composite parent, int style, IQueryInit queryInit) {
		super(parent, style);
		_queryInit = queryInit;
	}

	public void init() {
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 7;
		this.setLayout(topLayout);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		this.setLayoutData(gd);
		composeContext(this);
		refresh();
		ChangeItemNotifier.getInstance().addListener(this);
		ConnectionRegistration.addListener(this);
		BrowseCoordinator.getInstance().addListener(this);
	}

	/**
	 * Disposing all resources.
	 */
	public void dispose() {
		ChangeItemNotifier.getInstance().removeListener(this);
		ConnectionRegistration.removeListener(this);
		BrowseCoordinator.getInstance().removeListener(this);
	}

	/**
	 * Returns the context that is currently selected.
	 * 
	 * @return contextselection
	 */
	public IBrowseItem getContextSelection() {
		return _item;
	}

	public int getMaxDisplay() {
		_execQueryButton.setFocus();
		return _querySpinner.getSelection();
	}

	public void change(IBrowseItem item) {
		_item = item;
		refresh();
	}

	public void added(IConnection connection) {
		// TODO
	}

	public void removed(IConnection connection) {
		// TODO
	}

	public void closed(IConnection connection) {
		_item = null;
		refresh();
	}

	public void opened(IConnection connection) {
		refresh();
	}

	public void added(IBrowseItem item) {
	}

	public void refresh(IBrowseItem item) {
	}

	public void removed(IBrowseItem[] items) {
		if (items != null) {
			for (IBrowseItem item : items) {
				if (item.equals(_item) || item.contains(_item)) {
					_item = null;
					break;
				}
			}
		}
		refresh();
	}

	public void moved(IBrowseItem fromItem, IBrowseItem toItem) {
		if (fromItem.equals(_item)) {
			change(toItem);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// /////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method puts together the context composite of the view. The
	 * mentioned composite consists of a Combo, a Button and a Label.
	 * 
	 * @param contextContainer
	 */
	private void composeContext(Composite contextContainer) {
		GridData gd = new GridData();

		// label control - context
		_contextSelection = new Text(contextContainer, SWT.LEFT | SWT.BORDER);
		_contextSelection.setEditable(false);
		_contextSelection.setEnabled(false);
		if (_item != null) {
			_contextSelection.setText(_item.getPath() + " ("
					+ _item.getConnection().getName() + ")");
		} else {
			_contextSelection.setText(_noContextSelected);
		}

		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		_contextSelection.setLayoutData(gd);

		// spinner
		_querySpinnerLabel = new Label(contextContainer, SWT.NULL);
		_querySpinnerLabel.setText("Max-Disp:");
		_querySpinner = new Spinner(contextContainer, SWT.BORDER);
		_querySpinner.setMinimum(1);
		_querySpinner.setMaximum(1000000);
		_querySpinner.setIncrement(1);
		_querySpinner.setPageIncrement(100);
		_querySpinner.setSelection(100);

		// button control - query execution
		_execQueryButton = new Button(contextContainer, SWT.Activate
				| SWT.CENTER);
		_execQueryButton.setText("Submit Query");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = SWT.CENTER;
		_execQueryButton.setLayoutData(gd);

		_execQueryButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				_queryInit.triggerQuery();
			}
		});
	}

	private void refresh() {
		// button
		_execQueryButton.setEnabled(_item != null);
		_querySpinner.setEnabled(_item != null);
		_querySpinnerLabel.setEnabled(_item != null);

		if (_item != null) {
			_contextSelection.setText(_item.getPath() + " ("
					+ _item.getConnection().getName() + ")");
		} else {
			_contextSelection.setText(_noContextSelected);
		}
	}
}
