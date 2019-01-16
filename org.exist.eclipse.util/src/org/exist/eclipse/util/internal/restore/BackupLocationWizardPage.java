package org.exist.eclipse.util.internal.restore;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.exist.eclipse.util.internal.UtilPlugin;

/**
 * This is the Wizardpage where the user can define the backup location from
 * where he'd like restore.
 * 
 * @author Markus Tanner
 * 
 */
public class BackupLocationWizardPage extends WizardPage {

	private String _backupLocation = null;
	private ISelection _selection;
	private Text _locationText;
	private boolean _ableToFinish = true;

	protected BackupLocationWizardPage(ISelection selection) {
		super("backuplocationwizardpage");
		setTitle("Restore from backup");
		setDescription("Enter the location of the backup you want to restore from. (__contents__.xml)");
		setImageDescriptor(UtilPlugin
				.getImageDescriptor("icons/existdb.png"));
		_selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 4;

		// target
		Label targetLabel = new Label(container, SWT.NULL);
		targetLabel.setText("Location");
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		targetLabel.setLayoutData(gd);

		_locationText = new Text(container, SWT.BORDER | SWT.SINGLE);
		_locationText.setText("");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		_locationText.setLayoutData(gd);
		_locationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				_backupLocation = _locationText.getText();
				dialogChanged();
			}
		});

		// button to select the target via dialog
		Button selectCollectionBtn = new Button(container, SWT.Activate);
		selectCollectionBtn.setText("Browse...");
		gd = new GridData();
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = SWT.CENTER;
		selectCollectionBtn.setLayoutData(gd);

		selectCollectionBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectBackupLocation();
				dialogChanged();
			}
		});

		initialize();
		dialogChanged();
		setControl(container);
	}

	public String getBackupLocation() {
		return _backupLocation;
	}

	public boolean isAbleToFinish() {
		return _ableToFinish;
	}

	// //////////////////////////////////////////////////////////////////////////
	// ///////////////////
	// private methods
	// //////////////////////////////////////////////////////////////////////////
	// ///////////////////

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

	private void dialogChanged() {
		// check one param after the other that needs to be activated
		if (_backupLocation == null || _backupLocation.length() == 0) {
			setMessage("");
			setErrorState("No backup selected");
			_ableToFinish = false;
		} else {
			setErrorState(null);
			_ableToFinish = true;
			if (locationExists()) {
				setMessage("");
			} else {
				setMessage("");

			}
		}
	}

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

	/**
	 * Opens a FileDialog so that the backup location can be defined. Returns
	 * the path of the backup location. The return-value is empty in case the
	 * selection is not valid.
	 * 
	 * The returned value either needs to point to a __contents__.xml file.
	 * 
	 */
	private void selectBackupLocation() {

		FileDialog dialog = new FileDialog(getControl().getShell(), SWT.OPEN);
		dialog.setText("Select a backup location");
		String[] filterExtensions = { "*.xml", "*.*" };
		String[] filterNames = { "XML Files (*.xml)", "All Files (*.*)" };
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterNames(filterNames);

		// open the dialog so that the target can be selected
		String targetName = dialog.open();

		if (targetName != null) {
			_backupLocation = targetName;
			_locationText.setText(_backupLocation);
			dialogChanged();
		}
	}

	/**
	 * @return boolean telling whether the current location exists
	 */
	private boolean locationExists() {
		File target = new File(_backupLocation);

		if (target.exists()) {
			return true;
		} else {
			return false;
		}
	}

}
