/**
 * 
 */
package org.exist.eclipse.auto.internal.view;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.mod.AutoModEvent;
import org.exist.eclipse.auto.internal.mod.IAutoModificationNotifier;
import org.exist.eclipse.auto.internal.model.IAutoModel;
import org.exist.eclipse.auto.internal.model.QueryEntity;

/**
 * Creates the section that contains the query-selection. The Section is based
 * on a {@link TableViewer}
 * 
 * @author Markus Tanner
 */
public class QueriesSection implements SelectionListener {

	private FormToolkit _toolkit;
	private IAutoModel _autoModel;
	private Composite _navigation;
	private TableViewer _viewer;
	private IAutoModificationNotifier _notifier;

	public QueriesSection(Composite navigation, IAutoModel model, FormToolkit toolkit,
			IAutoModificationNotifier notifier) {
		_navigation = navigation;
		_toolkit = toolkit;
		_autoModel = model;
		_notifier = notifier;
	}

	/**
	 * Initializes the construction of the section and finally returns the built
	 * section.
	 * 
	 * @return Section
	 */
	public Section init() {
		Section querySection = _toolkit.createSection(_navigation, ExpandableComposite.TITLE_BAR);
		querySection.setText("Queries");
		querySection.marginWidth = 10;
		querySection.marginHeight = 5;
		GridData gd = new GridData(GridData.FILL_BOTH);
		querySection.setLayoutData(gd);

		// table viewer
		Composite queryClient = _toolkit.createComposite(querySection, SWT.WRAP);
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 2;
		glayout.marginWidth = 2;
		glayout.marginHeight = 2;
		queryClient.setLayout(glayout);
		Table queryTable = _toolkit.createTable(queryClient, SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		queryTable.setLayoutData(gd);
		_toolkit.paintBordersFor(queryClient);

		// buttons
		Composite buttons = _toolkit.createComposite(queryClient, SWT.WRAP);
		glayout = new GridLayout();
		glayout.numColumns = 1;
		glayout.marginWidth = 1;
		glayout.marginHeight = 1;
		buttons.setLayout(glayout);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		buttons.setLayoutData(gd);

		Button addButton = _toolkit.createButton(buttons, "Add...", SWT.PUSH);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(this);

		Button removeButton = _toolkit.createButton(buttons, "Remove", SWT.PUSH);
		removeButton.setLayoutData(gd);
		removeButton.addSelectionListener(this);

		querySection.setClient(queryClient);
		_viewer = new TableViewer(queryTable);
		addDropSupport(_viewer.getTable());

		return querySection;
	}

	/**
	 * Returns the TableViewer the init() method needs to be called first
	 * 
	 * @return TableViewer
	 */
	public TableViewer getTableViewer() {
		return _viewer;
	}

	// --------------------------------------------------------------------------
	// Actions
	// --------------------------------------------------------------------------

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() instanceof Button) {
			Button eventButton = (Button) e.getSource();

			if (eventButton.getText().compareTo("Add...") == 0) {
				// add a query
				QueryEntity newQueryEntity = new QueryEntity("Automation - Query", "Note", 1, "//query");
				newQueryEntity.setModel(_autoModel);
				_autoModel.addQuery(newQueryEntity);
				_viewer.refresh();
				_notifier.automationModified(new AutoModEvent("Query added."));
				// remove a query
			} else if (eventButton.getText().compareTo("Remove") == 0) {
				IStructuredSelection structuredSelection = (IStructuredSelection) _viewer.getSelection();
				QueryEntity toDelete;
				if (structuredSelection.size() == 1) {
					toDelete = (QueryEntity) structuredSelection.getFirstElement();

					boolean confirmed = MessageDialog.openConfirm(_navigation.getShell(), "Delete",
							"Delete the query '" + toDelete.getName() + "' ?");
					if (confirmed) {
						_autoModel.removeQuery(toDelete);
						_viewer.setSelection(new StructuredSelection(_viewer.getElementAt(0)));
						_viewer.refresh();
						_notifier.automationModified(new AutoModEvent("Query removed."));
					}
				}
			}
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// SelectionListener interface method - not used
	}

	private void addDropSupport(final Table table) {
		final int ops = DND.DROP_COPY;
		DropTarget target = new DropTarget(table, ops);
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

			@Override
			public void drop(DropTargetEvent event) {
				if (fileTransfer.isSupportedType(event.currentDataType)) {
					String[] filenames = (String[]) event.data;
					for (String filename : filenames) {
						String input = AutoUI.getDefault().getFileInput(filename);
						if (input != null) {
							File file = new File(filename);
							QueryEntity entity = new QueryEntity(file.getName(), file.getName(), 1, input);
							_autoModel.addQuery(entity);
							_viewer.refresh();
							_notifier.automationModified(new AutoModEvent("Query added."));
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
