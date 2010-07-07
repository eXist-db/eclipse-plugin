/**
 * 
 */
package org.exist.eclipse.auto.internal.wizard;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.exist.eclipse.auto.connection.AutoContextRegistration;
import org.exist.eclipse.auto.connection.IAutoContext;
import org.exist.eclipse.auto.internal.AutoUI;

/**
 * This is the wizardpage on which the connection can be selected.
 * 
 * @author Markus Tanner
 */
public class RunAutoContextPage extends WizardPage {

	private Combo _contextSelection;
	private Text _targetText;
	private String _resultTarget = null;

	protected RunAutoContextPage() {
		super("contextwizardpage");
		setTitle("Run an Automation");
		setDescription("Select a context for the automation.");
		setImageDescriptor(AutoUI.getDefault().getImageRegistry()
				.getDescriptor(AutoUI.IMG_EXIST_ECLIPSE_LOGO));
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 4;

		// collection
		Label collectionLabel = new Label(container, SWT.NULL | SWT.FILL);
		collectionLabel.setText("Context Selection:");
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 1;
		collectionLabel.setLayoutData(gd);

		// combo
		_contextSelection = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);

		for (IAutoContext context : AutoContextRegistration.getInstance()
				.getAll()) {
			_contextSelection.add(context.getName());
		}
		_contextSelection.setText("Select");
		gd = new GridData();
		gd.horizontalSpan = 1;
		_contextSelection.setLayoutData(gd);

		// add selection listener for the combo
		_contextSelection.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		// filler
		Label fillerLabel = new Label(container, SWT.NULL | SWT.FILL);
		gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 2;
		fillerLabel.setLayoutData(gd);

		// target
		Label targetLabel = new Label(container, SWT.NULL);
		targetLabel.setText("Result Location:");
		gd = new GridData();
		gd.horizontalSpan = 1;
		targetLabel.setLayoutData(gd);

		_targetText = new Text(container, SWT.BORDER | SWT.SINGLE);
		_targetText.setText("");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		_targetText.setLayoutData(gd);
		_targetText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				_resultTarget = _targetText.getText();
				dialogChanged();
			}
		});

		// button to select the target via dialog
		Button selectTargetBtn = new Button(container, SWT.Activate);
		selectTargetBtn.setText("Browse");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = SWT.CENTER;
		selectTargetBtn.setLayoutData(gd);

		selectTargetBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectResultLocation();
				dialogChanged();
			}
		});

		setControl(container);
		dialogChanged();
	}

	/**
	 * Returns the AutoContext that was selected on the Dropdown control.
	 * 
	 * @return The selected IAutoContext
	 */
	public IAutoContext getAutoContext() {
		IAutoContext selectedContext = null;
		for (IAutoContext autoContext : AutoContextRegistration.getInstance()
				.getAll()) {
			if (autoContext.getName().compareTo(_contextSelection.getText()) == 0) {
				selectedContext = autoContext;
				break;
			}
		}

		return selectedContext;
	}

	/**
	 * Retunrs the path to where the automation result should be saved.
	 * 
	 * @return Path
	 */
	public String getTarget() {
		return _resultTarget;
	}

	//--------------------------------------------------------------------------
	// Private Methods
	//--------------------------------------------------------------------------

	/**
	 * Handles the error message display.
	 */
	private void dialogChanged() {
		if (_contextSelection.getText().length() < 1) {
			if (AutoContextRegistration.getInstance().getAll().isEmpty()) {
				setErrorState("No context available. Make sure there's"
						+ " at least one open connection!");
			} else {
				setErrorState("Context needs to be selected.");
			}
		} else if (_resultTarget == null || _resultTarget.length() == 0) {
			setMessage("");
			setErrorState("No target defined");
		} else {
			setErrorState(null);
			setMessage("");
			if (targetExists()) {
				setMessage("Attention: The target exists already on the filesystem.\n"
						+ "Running the automation might overwrite existing data!");
			} else {
				setMessage("");

			}
			super.setPageComplete(true);
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

	private void selectResultLocation() {

		Shell shell = new Shell();
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setText("Select a result location");
		dialog.setFileName("automation_result.eqar");

		// open the dialog so that the target can be selected
		String targetName = dialog.open();

		if (targetName != null) {
			_resultTarget = targetName;
			_targetText.setText(_resultTarget);
			dialogChanged();
		}
	}

	private boolean targetExists() {
		File target = new File(_resultTarget);

		if (target.exists()) {
			return true;
		} else {
			return false;
		}
	}
}
