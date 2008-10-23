/**
 * 
 */
package org.exist.eclipse.auto.internal.result.view;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.exist.eclipse.auto.internal.result.control.RunsContentProvider;
import org.exist.eclipse.auto.internal.result.control.RunsLabelProvider;
import org.exist.eclipse.auto.internal.result.model.QueryResultEntity;
import org.exist.eclipse.auto.internal.result.model.RunEntity;

/**
 * This section show the single runs of a query.
 * 
 * @author Markus Tanner
 */
public class QueryRunsSection implements IDetailsPage {

	private IManagedForm _mform;
	private QueryResultEntity _queryResultEntity;
	private Composite _parent;
	private TableViewer _viewer;
	private TableColumn _columnNr;
	private TableColumn _stateCol;
	private TableColumn _compileTimeCol;
	private TableColumn _execTimeCol;
	private Section _queryRunsSection;

	public void createContents(Composite parent) {
		_parent = parent;
		GridLayout layout = new GridLayout();
		layout.marginWidth = 10;
		layout.marginHeight = 5;
		_parent.setLayout(layout);
		_parent.setLayoutData(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		_parent.setLayoutData(gd);

		// header
		FormToolkit toolkit = _mform.getToolkit();
		_queryRunsSection = toolkit.createSection(_parent, Section.TITLE_BAR
				| ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED);
		_queryRunsSection.marginWidth = 10;
		_queryRunsSection.marginHeight = 5;
		_queryRunsSection.setText("Query Runs Overview");
		_queryRunsSection.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite client = toolkit.createComposite(_queryRunsSection);
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 2;
		client.setLayout(glayout);
		client.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Table Viewer
		_viewer = new TableViewer(client, SWT.VIRTUAL | SWT.FULL_SELECTION);
		_viewer.setContentProvider(new RunsContentProvider());
		_viewer.setLabelProvider(new RunsLabelProvider());
		_viewer.setUseHashlookup(true);

		// Create the single columns
		_columnNr = new TableColumn(_viewer.getTable(), SWT.NONE);
		_columnNr.setWidth(50);
		_columnNr.setText("Nr");
		_stateCol = new TableColumn(_viewer.getTable(), SWT.NONE);
		_stateCol.setWidth(100);
		_stateCol.setText("State");
		_compileTimeCol = new TableColumn(_viewer.getTable(), SWT.NONE);
		_compileTimeCol.setWidth(100);
		_compileTimeCol.setText("Compilation");
		_execTimeCol = new TableColumn(_viewer.getTable(), SWT.NONE);
		_execTimeCol.setWidth(100);
		_execTimeCol.setText("Execution");

		_viewer.getTable().setLinesVisible(true);
		_viewer.getTable().setHeaderVisible(true);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		_viewer.getTable().setLayoutData(gd);
		_queryRunsSection.setClient(client);
	}

	public void commit(boolean onSave) {
	}

	public void dispose() {

	}

	public void initialize(IManagedForm mform) {
		_mform = mform;
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isStale() {
		return false;
	}

	public void refresh() {
	}

	public void setFocus() {
	}

	public boolean setFormInput(Object input) {
		return false;
	}

	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		if (structuredSelection.size() == 1) {
			_queryResultEntity = (QueryResultEntity) structuredSelection
					.getFirstElement();
			_viewer.refresh();
		} else {
			_queryResultEntity = null;
		}
		if (_queryResultEntity != null) {
			ArrayList<RunEntity> runs = _queryResultEntity.getRuns();
			_viewer.setItemCount(runs.size());
			_viewer.setInput(runs.toArray(new RunEntity[runs.size()]));
			_viewer.refresh();
		} else {
			_viewer.setItemCount(0);
			_viewer.setInput(null);
		}
	}

}
