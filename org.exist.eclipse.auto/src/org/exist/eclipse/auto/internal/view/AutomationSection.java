/**
 * 
 */
package org.exist.eclipse.auto.internal.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.exist.eclipse.auto.internal.mod.AutoModEvent;
import org.exist.eclipse.auto.internal.mod.IAutoModificationNotifier;
import org.exist.eclipse.auto.internal.model.IAutoModel;

/**
 * This class represents the automation configuration section.
 * 
 * @author Markus Tanner
 */
public class AutomationSection implements ModifyListener, FocusListener {

	private Text _threadCount;
	private FormToolkit _toolkit;
	private IAutoModel _autoModel;
	private Composite _navigation;
	private IAutoModificationNotifier _notifier;

	public AutomationSection(Composite navigation, IAutoModel model,
			FormToolkit toolkit, IAutoModificationNotifier notifier) {
		_navigation = navigation;
		_autoModel = model;
		_toolkit = toolkit;
		_notifier = notifier;
	}

	/**
	 * Initializes the Automation configuration section.
	 */
	public void init() {
		Section autoSection = _toolkit.createSection(_navigation,
				Section.TITLE_BAR);
		autoSection.setText("Automation Details");
		autoSection
				.setDescription("Automation specific values can be edited here.");
		autoSection.marginWidth = 10;
		autoSection.marginHeight = 5;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		autoSection.setLayoutData(gd);

		Composite autoClient = _toolkit.createComposite(autoSection, SWT.WRAP);
		GridLayout autoLayout = new GridLayout();
		autoLayout.numColumns = 2;
		autoLayout.marginWidth = 2;
		autoLayout.marginHeight = 2;
		autoClient.setLayout(autoLayout);

		// thread count
		_toolkit.createLabel(autoClient, "Thread Count:");
		_threadCount = _toolkit.createText(autoClient, "", SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = 10;
		_threadCount.addModifyListener(this);
		_threadCount.addFocusListener(this);
		_threadCount.setLayoutData(gd);

		autoSection.setClient(autoClient);
		_threadCount.setText(Integer.toString(_autoModel.getThreadCount()));
	}

	//--------------------------------------------------------------------------
	// Actions
	//--------------------------------------------------------------------------

	public void modifyText(ModifyEvent e) {
		handleThreadCountInput();
	}

	public void focusGained(FocusEvent e) {
		// This event is not of interest
	}

	public void focusLost(FocusEvent e) {
		if (_threadCount.getText().compareTo("") == 0) {
			_threadCount.setText("1");
		}
	}

	//--------------------------------------------------------------------------
	// Private Methods
	//--------------------------------------------------------------------------

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
	 * Handles the input concerning a number field. Make sure that only numbers
	 * can be added.
	 */
	private void handleThreadCountInput() {
		boolean sendNotification = false;
		if (_autoModel != null) {
			if (isValidInt(_threadCount.getText())
					&& _threadCount.getText().compareTo("") != 0) {
				// if string represents valid number
				int value = Integer.parseInt(_threadCount.getText());
				if (value >= 1 && value <= 20) {
					_autoModel.setThreadCount(Integer.parseInt(_threadCount
							.getText()));
				} else {
					MessageDialog
							.openInformation(_navigation.getShell(),
									"Invalid thread count",
									"The thread count needs to be a value between 1 and 20.");
					_threadCount.setText("1");
				}
				sendNotification = true;
				// if it's empty
			} else if (_threadCount.getText().compareTo("") == 0) {
				_autoModel.setThreadCount(Integer.parseInt(_threadCount
						.getText()));
				sendNotification = true;
			} else {
				// if the current input contains only one character and is not a
				// number, the field should be cleared
				if (_threadCount.getText().length() != 1) {
					_threadCount.setText(Integer.toString(_autoModel
							.getThreadCount()));
				} else {
					_threadCount.setText("");
				}
			}
		}
		if (sendNotification) {
			_notifier.automationModified(new AutoModEvent(
					"Thread Count modified."));
		}
	}

}
