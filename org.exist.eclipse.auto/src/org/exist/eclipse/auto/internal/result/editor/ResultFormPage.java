/**
 * 
 */
package org.exist.eclipse.auto.internal.result.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.exception.AutoException;
import org.exist.eclipse.auto.internal.result.control.ResultContentProvider;
import org.exist.eclipse.auto.internal.result.model.IResultModel;
import org.exist.eclipse.auto.internal.result.view.AutoResultSection;
import org.exist.eclipse.auto.internal.result.view.ResultBlock;

/**
 * This is the formpage of the result editor. It's the first page of the
 * automation editor.
 * 
 * @author Markus Tanner
 */
public class ResultFormPage extends FormPage {

	private ResultContentProvider _contentProvider;
	private ResultTextPage _textPage;
	private ResultBlock _block;

	/**
	 * ResultFormPage Constructor
	 * 
	 * @param editor
	 * @param textPage
	 * @throws AutoException
	 */
	public ResultFormPage(ResultEditor editor, ResultTextPage textPage)
			throws AutoException {
		super(editor, "result", "Result");
		_textPage = textPage;
		_contentProvider = new ResultContentProvider(_textPage.getContent());
		_block = new ResultBlock(this, _contentProvider, getResultDataModel());
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		form.setText("Automation Result");
		toolkit.decorateFormHeading(managedForm.getForm().getForm());

		try {
			AutoResultSection autoResultSection = new AutoResultSection(form
					.getBody(), getResultDataModel(), toolkit);
			autoResultSection.init();
		} catch (AutoException e) {
			Status status = new Status(IStatus.ERROR, AutoUI.getId(), e
					.getMessage(), e);
			AutoUI.getDefault().getLog().log(status);
			ErrorDialog
					.openError(
							Display.getCurrent().getActiveShell(),
							"Loading Automation Result failed",
							"Correct the file in the text editor. Re-open it afterwards.",
							status);
		}

		_block.createContent(managedForm);
	}

	/**
	 * Returns the actual IResultModel
	 * 
	 * @return IResultModel
	 * @throws AutoException
	 */
	public IResultModel getResultDataModel() throws AutoException {
		try {
			return _contentProvider.getModel();
		} catch (AutoException e) {
			throw new AutoException(e);
		}
	}
}
