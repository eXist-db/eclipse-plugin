/**
 * 
 */
package org.exist.eclipse.auto.internal.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.model.IAutoModel;
import org.exist.eclipse.auto.internal.wizard.RunAutoWizard;

/**
 * The Toolbar of the form page is assembled in this class.
 * 
 * @author Markus Tanner
 * 
 */
public class ToolBarActions {

	private IManagedForm _managedForm;
	private SashForm _sashForm;
	private IAutoModel _autoModel;

	/**
	 * Constructor
	 * 
	 * @param managedForm
	 * @param sashForm
	 */
	public ToolBarActions(IManagedForm managedForm, SashForm sashForm,
			IAutoModel autoModel) {
		_managedForm = managedForm;
		_sashForm = sashForm;
		_autoModel = autoModel;
	}

	/**
	 * Create the toolbar as such.
	 */
	protected void create() {
		final ScrolledForm form = _managedForm.getForm();

		// run automation
		Action runAction = new Action("run", Action.AS_PUSH_BUTTON) {
			public void run() {
				if (_autoModel.getQueries().size() == 0) {
					MessageDialog.openInformation(form.getShell(),
							"No Query available",
							"An automation can only be executed if there's "
									+ "at least one query configured.\nAdd"
									+ " a query to the automation.");
				} else {
					RunAutoWizard wizard = new RunAutoWizard(_autoModel);
					wizard.init(AutoUI.getDefault().getWorkbench(), null);
					wizard.setForcePreviousAndNextButtons(true);
					WizardDialog dialog = new WizardDialog(AutoUI.getDefault()
							.getWorkbench().getDisplay().getActiveShell(),
							wizard);
					dialog.open();
				}
			}
		};
		runAction.setToolTipText("Run automation");
		runAction.setImageDescriptor(AutoUI.getDefault().getImageRegistry()
				.getDescriptor(AutoUI.IMG_RUN));

		// horizontal perspecitve
		Action hAction = new Action("hor", Action.AS_RADIO_BUTTON) {
			public void run() {
				_sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};
		hAction.setChecked(true);
		hAction.setToolTipText("Horizontal orientation");
		hAction.setImageDescriptor(AutoUI.getDefault().getImageRegistry()
				.getDescriptor(AutoUI.IMG_HORIZONTAL));

		// vertical perspective
		Action vAction = new Action("ver", Action.AS_RADIO_BUTTON) {
			public void run() {
				_sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vAction.setChecked(false);
		vAction.setToolTipText("Vertical orientation");
		vAction.setImageDescriptor(AutoUI.getDefault().getImageRegistry()
				.getDescriptor(AutoUI.IMG_VERTICAL));

		// add the actions to the toolbar
		form.getToolBarManager().add(runAction);
		form.getToolBarManager().add(hAction);
		form.getToolBarManager().add(vAction);
	}
}
