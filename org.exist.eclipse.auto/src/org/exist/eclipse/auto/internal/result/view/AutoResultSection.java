/**
 * 
 */
package org.exist.eclipse.auto.internal.result.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.exist.eclipse.auto.internal.result.model.IResultModel;

/**
 * This section contains global information about the automation.
 * 
 * @author Markus Tanner
 */
public class AutoResultSection {

	private Text _threadCount;
	private Text _queryCount;
	private Text _avgCompTime;
	private Text _avgExecTime;
	private Text _state;
	private Text _queryOrderType;
	private Text _resultCount;
	private Text _autoNote;
	Composite _navigation;
	IResultModel _model;
	FormToolkit _toolkit;

	public AutoResultSection(Composite navigation, IResultModel model,
			FormToolkit toolkit) {
		_navigation = navigation;
		_model = model;
		_toolkit = toolkit;
	}

	/**
	 * Initializes the automation result section
	 */
	public void init() {

		Section autoSection = _toolkit.createSection(_navigation,
				ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
						| ExpandableComposite.EXPANDED);
		autoSection.setText("Automation Details");
		autoSection
				.setDescription("Automation specific values can be edited here.");
		autoSection.marginWidth = 10;
		autoSection.marginHeight = 5;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		autoSection.setLayoutData(gd);

		Composite autoClient = _toolkit.createComposite(autoSection);
		GridLayout autoLayout = new GridLayout();
		autoLayout.numColumns = 5;
		autoLayout.marginWidth = 2;
		autoLayout.marginHeight = 6;
		autoClient.setLayout(autoLayout);

		// query count
		_toolkit.createLabel(autoClient, "Query Count: ");
		_queryCount = _toolkit.createText(autoClient, "", SWT.SINGLE
				| SWT.READ_ONLY);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL);
		gd.widthHint = 10;
		_queryCount.setLayoutData(gd);

		// filler
		_toolkit.createLabel(autoClient, "      ");

		// average compilation time
		_toolkit.createLabel(autoClient, "Average Compilation Time: ");
		_avgCompTime = _toolkit.createText(autoClient, "", SWT.SINGLE
				| SWT.READ_ONLY);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL);
		gd.widthHint = 10;
		_avgCompTime.setLayoutData(gd);

		// thread count
		_toolkit.createLabel(autoClient, "Thread Count: ");
		_threadCount = _toolkit.createText(autoClient, "", SWT.SINGLE
				| SWT.READ_ONLY);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL);
		gd.widthHint = 10;
		_threadCount.setLayoutData(gd);

		// filler
		_toolkit.createLabel(autoClient, "      ");

		// average execution time
		_toolkit.createLabel(autoClient, "Average Execution Time: ");
		_avgExecTime = _toolkit.createText(autoClient, "", SWT.SINGLE
				| SWT.READ_ONLY);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL);
		gd.widthHint = 10;
		_avgExecTime.setLayoutData(gd);

		// successful
		_toolkit.createLabel(autoClient, "State: ");
		_state = _toolkit
				.createText(autoClient, "", SWT.SINGLE | SWT.READ_ONLY);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL);
		gd.widthHint = 10;
		_state.setLayoutData(gd);

		// filler
		_toolkit.createLabel(autoClient, "      ");

		// resultCount
		_toolkit.createLabel(autoClient, "Total Result Count: ");
		_resultCount = _toolkit.createText(autoClient, "", SWT.SINGLE
				| SWT.READ_ONLY);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL);
		gd.widthHint = 10;
		_resultCount.setLayoutData(gd);

		// query order type
		Label qotLabel = _toolkit.createLabel(autoClient, "Query OrderType: ");
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		qotLabel.setLayoutData(gd);
		_queryOrderType = _toolkit.createText(autoClient, "", SWT.SINGLE
				| SWT.READ_ONLY);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL);
		gd.widthHint = 10;
		_queryOrderType.setLayoutData(gd);

		// filler
		_toolkit.createLabel(autoClient, "      ");

		// auto note
		if (!_model.getAutoNote().isEmpty()) {
			Label noteLabel = _toolkit.createLabel(autoClient, "Note: ");
			gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			noteLabel.setLayoutData(gd);
			_autoNote = _toolkit.createText(autoClient, "", SWT.MULTI
					| SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
			gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
					| GridData.FILL_HORIZONTAL);
			gd.heightHint = 30;
			gd.widthHint = 10;
			_autoNote.setLayoutData(gd);
			_autoNote.setText(_model.getAutoNote());
		}

		autoSection.setClient(autoClient);
		_queryCount.setText(Integer.toString(_model.getQueryCount()));
		_threadCount.setText(Integer.toString(_model.getThreadCount()));
		_avgCompTime.setText(Integer.toString(_model.getAvgCompTime()) + " ms");
		_avgExecTime.setText(Integer.toString(_model.getAvgExecTime()) + " ms");
		_state.setText(_model.getState().name());
		_resultCount.setText(Long.toString(_model.getResultCount()));
		_queryOrderType.setText(_model.getQueryOrderType().toString());
	}

}
