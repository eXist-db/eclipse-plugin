/**
 * 
 */
package org.exist.eclipse.auto.internal.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.mod.AutoModEvent;
import org.exist.eclipse.auto.internal.mod.IAutoModificationNotifier;
import org.exist.eclipse.auto.internal.model.QueryEntity;

/**
 * This class displays the details of a query that was selected in the master
 * part {@link AutomationBlock}
 * 
 * @author Markus Tanner
 */
public class QueryDetailsSection implements IDetailsPage, ModifyListener,
		FocusListener {
	private IManagedForm _mform;
	private QueryEntity _queryEntity;
	private Text _name;
	private Text _quantity;
	private Text _query;
	private boolean _dirty;
	private IAutoModificationNotifier _notifier;
	private Composite _parent;

	/**
	 * Constructor
	 */
	public QueryDetailsSection(IAutoModificationNotifier notifier) {
		_notifier = notifier;
	}

	public void initialize(IManagedForm mform) {
		_mform = mform;
	}

	public void createContents(Composite parent) {
		_parent = parent;
		FillLayout layout = new FillLayout();
		layout.marginWidth = 10;
		layout.marginHeight = 5;
		_parent.setLayout(layout);
		_parent.setLayoutData(layout);

		// header
		FormToolkit toolkit = _mform.getToolkit();
		Section queryDetailsSection = toolkit.createSection(_parent,
				Section.TITLE_BAR);
		queryDetailsSection.marginWidth = 10;
		queryDetailsSection.marginHeight = 5;
		queryDetailsSection.setText("Query Details");

		Composite client = toolkit.createComposite(queryDetailsSection);
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 2;
		client.setLayout(glayout);
		client.setLayoutData(new GridData(GridData.FILL_BOTH));

		// body
		createControls(toolkit, client);

		toolkit.paintBordersFor(client);
		queryDetailsSection.setClient(client);
	}

	public void commit(boolean onSave) {
	}

	public void setFocus() {
		// do not set focus
	}

	public void dispose() {
	}

	public boolean isDirty() {
		return _dirty;
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
		update();
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	// --------------------------------------------------------------------------
	// Actions
	// --------------------------------------------------------------------------

	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.size() == 1) {
			_queryEntity = (QueryEntity) structuredSelection.getFirstElement();
		} else
			_queryEntity = null;
		update();
	}

	public void modifyText(ModifyEvent e) {
		if (e.getSource() instanceof Text) {
			Text modifiedText = (Text) e.getSource();
			if (modifiedText == _name) {
				if (_queryEntity != null) {
					String previousText = _queryEntity.getName();
					_queryEntity.setName(_name.getText());
					createNotification(_queryEntity.getName(), previousText);
				}
			} else if (modifiedText == _quantity) {
				handleQuantityInput();
			} else if (modifiedText == _query) {
				if (_queryEntity != null) {
					String previousText = _queryEntity.getQuery();
					_queryEntity.setQuery(_query.getText());
					createNotification(_queryEntity.getQuery(), previousText);
				}
			}
		}
	}

	public void focusGained(FocusEvent e) {
		// This action is not of interest

	}

	public void focusLost(FocusEvent e) {
		if (_quantity.getText().compareTo("") == 0) {
			_quantity.setText("1");
		}
	}

	// --------------------------------------------------------------------------
	// Private Methods
	// --------------------------------------------------------------------------

	/**
	 * Creates a notification if the new input is not the same as the existing
	 * one.
	 */
	private void createNotification(String newInput, String input) {
		if (input.compareTo(newInput) != 0) {
			_notifier.automationModified(new AutoModEvent(
					"Query Details changed"));
		}
	}

	/**
	 * Creates a given space in the view
	 */
	private void createSpacer(FormToolkit toolkit, Composite parent, int span) {
		Label spacer = toolkit.createLabel(parent, "");
		GridData gd = new GridData();
		gd.horizontalSpan = span;
		spacer.setLayoutData(gd);
	}

	/**
	 * update the entities
	 */
	private void update() {
		_name.setText(_queryEntity != null && _queryEntity.getName() != null ? _queryEntity
				.getName() : "");
		_quantity.setText(Integer.toString(_queryEntity.getQuantity()));
		_query.setText(_queryEntity != null && _queryEntity.getQuery() != null ? _queryEntity
				.getQuery() : "");
	}

	/**
	 * Creates the controls on the details page
	 * 
	 * @param toolkit
	 * @param client
	 */
	private void createControls(FormToolkit toolkit, Composite client) {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = 10;

		// name
		toolkit.createLabel(client, "Name:");
		_name = toolkit.createText(client, "", SWT.SINGLE);
		_name.setToolTipText("Name");
		_name.addModifyListener(this);
		_name.setLayoutData(gd);

		createSpacer(toolkit, client, 2);

		// quantity
		toolkit.createLabel(client, "Quantity:");
		_quantity = toolkit.createText(client, "", SWT.SINGLE);
		_quantity.setToolTipText("Quantity");
		_quantity.addModifyListener(this);
		_quantity.addFocusListener(this);
		_quantity.setLayoutData(gd);

		createSpacer(toolkit, client, 2);

		// query
		Label queryLabel = toolkit.createLabel(client, "Query:");
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		queryLabel.setLayoutData(gd);

		_query = toolkit.createText(client, "", SWT.MULTI | SWT.WRAP
				| SWT.V_SCROLL);
		_query.setToolTipText("Query");
		_query.addModifyListener(this);
		addDropSupport(_query);

		gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		_query.setLayoutData(gd);
	}

	/**
	 * Indicates whether string represents a number
	 * 
	 * @param input
	 * @return boolean
	 */
	private boolean isValidInt(String input) {
		char[] chars = new char[input.length()];
		input.getChars(0, chars.length, chars, 0);
		for (int i = 0; i < chars.length; i++) {
			if (!('0' <= chars[i] && chars[i] <= '9')) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Handles the input concerning the quantity field. Make sure that only
	 * numbers can be added.
	 */
	private void handleQuantityInput() {
		boolean sendNotification = false;
		if (_queryEntity != null) {
			if (isValidInt(_quantity.getText())
					&& _quantity.getText().compareTo("") != 0) {
				// if string represents valid number or is empty
				int previousValue = _queryEntity.getQuantity();
				long newValue = Long.parseLong(_quantity.getText());
				if (newValue >= 1 && newValue <= 100000) {
					_queryEntity.setQuantity((int) newValue);
					if (previousValue != _queryEntity.getQuantity()) {
						sendNotification = true;
					}
				} else {
					MessageDialog.openInformation(_parent.getShell(),
							"Invalid thread count",
							"The quantity needs to be a value "
									+ "between 1 and 100'000.");
					_quantity.setText("100000");
					sendNotification = true;
				}
				// if it's empty
			} else if (_quantity.getText().compareTo("") == 0) {
				_queryEntity.setQuantity(Integer.parseInt(_quantity.getText()));
				sendNotification = true;
			} else {
				// if the current input contains only one character and is not a
				// number, the field should be cleared
				if (_quantity.getText().length() != 1) {
					_quantity.setText(Integer.toString(_queryEntity
							.getQuantity()));
				} else {
					_quantity.setText("");
				}
			}
		}
		if (sendNotification) {
			_notifier
					.automationModified(new AutoModEvent("Quantity modified."));
		}
	}

	private void addDropSupport(final Text text) {
		final int ops = DND.DROP_COPY;
		DropTarget target = new DropTarget(text, ops);
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] types = new Transfer[] { fileTransfer };
		target.setTransfer(types);

		target.addDropListener(new DropTargetAdapter() {
			@Override
			public void dragEnter(DropTargetEvent event) {
				event.detail = DND.DROP_COPY;
			}

			@Override
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
			}

			public void drop(DropTargetEvent event) {
				if (fileTransfer.isSupportedType(event.currentDataType)) {
					String[] filenames = (String[]) event.data;
					if (filenames.length > 0) {
						String input = AutoUI.getDefault().getFileInput(
								filenames[0]);
						if (input != null) {
							String previousText = _queryEntity.getQuery();
							_queryEntity.setQuery(input);
							createNotification(_queryEntity.getQuery(),
									previousText);
							_query.setText(input);
						}
					}
				}
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
			}
		});
	}
}
