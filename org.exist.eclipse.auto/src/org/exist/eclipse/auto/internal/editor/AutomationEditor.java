package org.exist.eclipse.auto.internal.editor;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.IDE;
import org.exist.eclipse.auto.internal.AutoUI;
import org.exist.eclipse.auto.internal.exception.AutoException;
import org.exist.eclipse.auto.internal.mod.AutoModEvent;
import org.exist.eclipse.auto.internal.mod.IAutoModificationListener;

/**
 * The AutomationEditor represents the basic Editor that is used to edit
 * automations. It contains two pages: On the first page the form-editor as such
 * is displayed. On the second page a TextEditor is placed, where the automation
 * can be edited based on the XML-structure.
 * 
 * @author Markus Tanner
 */
public class AutomationEditor extends FormEditor implements
		IAutoModificationListener {

	private AutomationFormPage _formPage;
	private AutomationTextPage _textPage;
	private boolean _dirty;

	public AutomationEditor() {
	}

	@Override
	protected FormToolkit createToolkit(Display display) {
		// Create a toolkit that shares colors between editors.
		return new FormToolkit(AutoUI.getDefault().getFormColors(display));
	}

	public void gotoMarker(IMarker marker) {
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException {
		super.init(site, editorInput);
		super.setPartName(editorInput.getName());
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (getActivePage() == _formPage.getIndex()) {
			// call doSave() to propagate the changes from the form-page to the
			// text editor
			try {
				_textPage.setContent(_formPage.getAutomationData());
			} catch (AutoException e) {
				Status status = new Status(IStatus.ERROR, AutoUI.getId(), e
						.getMessage(), e);
				AutoUI.getDefault().getLog().log(status);
				ErrorDialog.openError(Display.getCurrent().getActiveShell(),
						"Loading Automation failed",
						"Correct the Automation file in the text editor."
								+ " Re-open the automation file afterwards.",
						status);
			}
			_formPage.doSave(monitor);
		}
		_textPage.doSave(monitor);
		// clear dirty flag of the editor
		setDirty(false);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void doSaveAs() {
		// unused interface method, save as is not allowed
	}

	@Override
	protected void addPages() {
		try {
			// TextEditor Page
			_textPage = new AutomationTextPage(this);
			int id = addPage(_textPage, getEditorInput());
			setPageText(id, "Source");

			// AutomationEditor Page
			_formPage = new AutomationFormPage(this, _textPage);
			addPage(0, _formPage);

		} catch (AutoException ae) {
			Status status = new Status(IStatus.ERROR, AutoUI.getId(), ae
					.getMessage(), ae);
			AutoUI.getDefault().getLog().log(status);
			ErrorDialog
					.openError(
							Display.getCurrent().getActiveShell(),
							"Loading Automation failed",
							"Correct the Automation file in the text editor. Re-open the automation file afterwards.",
							status);

		} catch (PartInitException e) {
			Status status = new Status(IStatus.ERROR, AutoUI.getId(), e
					.getMessage(), e);
			AutoUI.getDefault().getLog().log(status);

		}
	}

	/**
	 * Sets the new dirty state of the editor.
	 * 
	 * @param dirty
	 *            Set to true if the editor should be dirty and false otherwise.
	 */
	protected void setDirty(boolean dirty) {
		if (_dirty != dirty) {
			_dirty = dirty;
			editorDirtyStateChanged();
		}
	}

	/**
	 * @return True if the file content has been changed inside the editor.
	 */
	@Override
	public boolean isDirty() {
		return _dirty;
	}

	@Override
	public void automationModified(AutoModEvent event) {
		setDirty(true);
	}

	@Override
	public void modificationCleared(AutoModEvent event) {
		setDirty(false);
	}

}
