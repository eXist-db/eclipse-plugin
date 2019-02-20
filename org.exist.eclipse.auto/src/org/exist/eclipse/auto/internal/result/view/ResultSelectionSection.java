/**
 * 
 */
package org.exist.eclipse.auto.internal.result.view;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.exist.eclipse.auto.internal.result.control.RunsContentProvider;
import org.exist.eclipse.auto.internal.result.control.RunsLabelProvider;

/**
 * On this section the query results can be selected. As soon a query is
 * selected, the single runs will be displayed on the details page.
 * 
 * @author Markus Tanner
 */
public class ResultSelectionSection {

	private FormToolkit _toolkit;
	private Composite _navigation;
	private TableViewer _viewer;
	private TableColumn _queryNameCol;
	private TableColumn _avgCompTimeCol;
	private TableColumn _avgExecTimeCol;

	public ResultSelectionSection(Composite navigation, FormToolkit toolkit) {
		_navigation = navigation;
		_toolkit = toolkit;
	}

	/**
	 * Initializes the construction of the section and finally returns the built
	 * section.
	 * 
	 * @return Section
	 */
	public Section init() {
		Section resultSelectionSection = _toolkit.createSection(_navigation,
				ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.COMPACT);
		resultSelectionSection.setText("Query Results");
		resultSelectionSection.marginWidth = 10;
		resultSelectionSection.marginHeight = 5;
		GridData gd = new GridData(GridData.FILL_BOTH);
		resultSelectionSection.setLayoutData(gd);

		// table viewer
		Composite client = _toolkit.createComposite(resultSelectionSection, SWT.WRAP);
		GridLayout glayout = new GridLayout();
		glayout.numColumns = 2;
		glayout.marginWidth = 2;
		glayout.marginHeight = 2;
		client.setLayout(glayout);

		_viewer = new TableViewer(client, SWT.VIRTUAL | SWT.FILL | SWT.FULL_SELECTION);
		_viewer.setContentProvider(new RunsContentProvider());
		_viewer.setLabelProvider(new RunsLabelProvider());
		_viewer.setUseHashlookup(true);

		// Create the columns
		_queryNameCol = new TableColumn(_viewer.getTable(), SWT.NONE);
		_queryNameCol.setWidth(150);
		_queryNameCol.setText("Query Name");
		_avgCompTimeCol = new TableColumn(_viewer.getTable(), SWT.NONE);
		_avgCompTimeCol.setWidth(150);
		_avgCompTimeCol.setText("Average Compilation Time");
		_avgExecTimeCol = new TableColumn(_viewer.getTable(), SWT.NONE);
		_avgExecTimeCol.setWidth(150);
		_avgExecTimeCol.setText("Average Exectuion Time");

		_viewer.getTable().setLinesVisible(true);
		_viewer.getTable().setHeaderVisible(true);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		_viewer.getTable().setLayoutData(gd);
		resultSelectionSection.setClient(client);
		return resultSelectionSection;
	}

	/**
	 * Returns the TableViewer the init() method needs to be called first
	 * 
	 * @return TableViewer
	 */
	public TableViewer getTableViewer() {
		return _viewer;
	}

}
