package org.exist.eclipse.util.internal.backup;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
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
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.util.internal.UtilPlugin;

/**
 * On this Wizardpage the user defines the target of the backup during the
 * backup process.
 * 
 * @author Markus Tanner
 * 
 */
public class BackupTargetWizardPage extends WizardPage {

	private final IBrowseItem _item;
	private boolean _ableToFinish;
	private String _backupTarget;
	private Text _targetText;

	protected BackupTargetWizardPage(ISelection selection, IBrowseItem item) {
		super("backuptargetwizardpage");
		_ableToFinish = true;
		_item = item;
		setTitle("Create a backup");
		setDescription("Enter a location for the backup.");
		setImageDescriptor(UtilPlugin
				.getImageDescriptor("icons/hslu_exist_eclipse_logo.jpg"));
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 4;

		// collection
		Label collectionLabel = new Label(container, SWT.NULL);
		collectionLabel.setText("Collection");
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		collectionLabel.setLayoutData(gd);

		Text collection = new Text(container, SWT.BORDER | SWT.SINGLE);
		collection.setText("");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		collection.setLayoutData(gd);
		collection.setText(_item.getPath());
		collection.setEnabled(false);

		// target
		Label targetLabel = new Label(container, SWT.NULL);
		targetLabel.setText("Target");
		gd = new GridData();
		gd.horizontalSpan = 1;
		targetLabel.setLayoutData(gd);

		_targetText = new Text(container, SWT.BORDER | SWT.SINGLE);
		_targetText.setText("");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		_targetText.setLayoutData(gd);
		_targetText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				_backupTarget = _targetText.getText();
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

		dialogChanged();
		setControl(container);
	}

	public String getBackupLocation() {
		return _backupTarget;
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
		if (_backupTarget == null || _backupTarget.length() == 0) {
			setMessage("");
			setErrorState("No target defined");
			_ableToFinish = false;
		} else {
			setErrorState(null);
			_ableToFinish = true;
			if (targetExists()) {
				setMessage("Attention: The target exists already on the filesystem.\n"
						+ "By creating the backup might overwrite existing data!!!");
			} else {
				setMessage("");

			}
		}
	}

	/**
	 * Opens a FileDialog so that the backup location can be defined. Returns
	 * the path of the backup location. The return-value is empty in case the
	 * selection is not valid.
	 * 
	 * The returned value either needs to be a directory or a zip-file.
	 */
	private void selectBackupLocation() {

		FileDialog dialog = new FileDialog(getControl().getShell(), SWT.SAVE);
		dialog.setText("Select a backup location");
		dialog.setFileName("eXist-backup.zip");

		// open the dialog so that the target can be selected
		String targetName = dialog.open();

		if (targetName != null) {
			_backupTarget = targetName;
			_targetText.setText(_backupTarget);
			dialogChanged();
		}
	}

	/**
	 * @return boolean telling whether the current target exists already
	 */
	private boolean targetExists() {
		File target = new File(_backupTarget);
		return target.exists();
	}

}
