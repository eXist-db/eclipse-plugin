/**
 * 
 */
package org.exist.eclipse.xquery.ui.internal.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.exist.eclipse.xquery.ui.internal.editor.XQueryEditor;

/**
 * Wizard for creating a new XQuery file.
 * 
 * @author Pascal Schmidiger
 */
public class NewXQueryFileWizard extends Wizard implements INewWizard {
	private static final String WIZARD_TITLE = "Create new XQuery";
	private NewXQueryFileWizardPage _page;
	private IStructuredSelection _selection;

	public NewXQueryFileWizard() {
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		_page = new NewXQueryFileWizardPage(_selection);
		_page.setTitle(WIZARD_TITLE);
		addPage(_page);
	}

	public boolean performFinish() {
		boolean ok = _page.finish();
		if (ok) {
			final IFile newFile = _page.getNewFile();
			_page.getControl().getDisplay().asyncExec(new Runnable() {
				public void run() {
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								.getActivePage().openEditor(
										new FileEditorInput(newFile),
										XQueryEditor.EDITOR_ID);
					} catch (PartInitException e) {
						throw new RuntimeException(e);
					}
				}
			});

		}
		return ok;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_selection = selection;
		setWindowTitle(WIZARD_TITLE);
	}

}
