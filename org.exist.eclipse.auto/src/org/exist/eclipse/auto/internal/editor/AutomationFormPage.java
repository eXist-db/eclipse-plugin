/**
 * 
 */
package org.exist.eclipse.auto.internal.editor;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.control.AutoContentProvider;
import org.exist.eclipse.auto.internal.exception.AutoException;
import org.exist.eclipse.auto.internal.model.AutoModelConverter;
import org.exist.eclipse.auto.internal.model.IAutoModel;
import org.exist.eclipse.auto.internal.view.AutomationBlock;

/**
 * This class represents the Automation Form Editor. It contains all the
 * ui-controls and the according content provider.
 * 
 * @author Markus Tanner
 */
public class AutomationFormPage extends FormPage {

	private AutomationBlock _block;
	private AutoContentProvider _contentProvider;
	private AutomationTextPage _textPage;

	/**
	 * AutomationFormPage constructor
	 * 
	 * @param editor
	 * @param textPage
	 * @throws AutoException
	 */
	public AutomationFormPage(AutomationEditor editor,
			AutomationTextPage textPage) throws AutoException {
		super(editor, "auto", "Automation");
		_textPage = textPage;
		_contentProvider = new AutoContentProvider(_textPage.getContent());
		_block = new AutomationBlock(this, _contentProvider,
				getAutomationDataModel());
		_block.addModificationListener(editor);
	}

	protected void createFormContent(final IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		form.setText("Automation");
		toolkit.decorateFormHeading(managedForm.getForm().getForm());
		_block.createContent(managedForm);
	}

	/**
	 * Fetching the Xml representation of the current model as a string.
	 * 
	 * @throws AutoException
	 */
	public String getAutomationData() throws AutoException {
		try {
			return AutoModelConverter.getXml(_contentProvider.getModel());
		} catch (AutoException e) {
			throw new AutoException(e);
		}
	}

	/**
	 * Returns the actual IAutoModel
	 * 
	 * @return IAutoModel
	 * @throws AutoException
	 */
	public IAutoModel getAutomationDataModel() throws AutoException {
		try {
			return _contentProvider.getModel();
		} catch (AutoException e) {
			throw new AutoException(e);
		}
	}

	@Override
	public boolean canLeaveThePage() {
		try {
			_textPage.setContent(getAutomationData());
		} catch (AutoException e) {
			Status status = new Status(Status.ERROR, AutoUI.getId(), e
					.getMessage(), e);
			AutoUI.getDefault().getLog().log(status);
			ErrorDialog.openError(Display.getCurrent().getActiveShell(),
					"Changing the page failed.",
					"Close the automation and open it again.", status);
		}
		return true;
	}

}
