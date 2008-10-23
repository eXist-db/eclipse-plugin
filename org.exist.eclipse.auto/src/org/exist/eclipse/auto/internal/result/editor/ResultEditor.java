/**
 * 
 */
package org.exist.eclipse.auto.internal.result.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.exception.AutoException;

/**
 * This is the main class of the result editor. It is responsible to instantiate
 * the formpage and the textpage of this editor.
 * 
 * 
 * @author Markus Tanner
 */
public class ResultEditor extends FormEditor {

	private ResultTextPage _textPage;
	private ResultFormPage _formPage;

	protected FormToolkit createToolkit(Display display) {
		// Create a toolkit that shares colors between editors.
		return new FormToolkit(AutoUI.getDefault().getFormColors(display));
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		super.init(site, editorInput);
		super.setPartName(editorInput.getName());
	}

	@Override
	protected void addPages() {
		try {
			// TextEditor Page
			_textPage = new ResultTextPage(this);
			int id = addPage(_textPage, getEditorInput());
			setPageText(id, "Source");

			// AutomationEditor Page
			_formPage = new ResultFormPage(this, _textPage);
			addPage(0, _formPage);
		} catch (AutoException ae) {
			Status status = new Status(Status.ERROR, AutoUI.getId(), ae
					.getMessage(), ae);
			AutoUI.getDefault().getLog().log(status);
			ErrorDialog
					.openError(
							Display.getCurrent().getActiveShell(),
							"Loading Automation Result failed",
							"Correct the file in the text editor. Re-open it afterwards.",
							status);

		} catch (PartInitException e) {
			Status status = new Status(Status.ERROR, AutoUI.getId(), e
					.getMessage(), e);
			AutoUI.getDefault().getLog().log(status);

		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// do nothing
	}

	@Override
	public void doSaveAs() {
		// do nothing
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
