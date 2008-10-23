package org.exist.eclipse.xquery.ui.internal.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.context.AbstractContextWizardPage;
import org.exist.eclipse.xquery.ui.context.ContextSwitcherRegistration;
import org.exist.eclipse.xquery.ui.context.IConnectionContext;
import org.exist.eclipse.xquery.ui.editor.IXQueryEditor;

/**
 * Start wizard for selecting the context for query running.
 * 
 * @author Pascal Schmidiger
 */
public class SelectContextWizard extends Wizard implements IWorkbenchWizard {
	public static final String WIZARD_TITLE = "Select the context";
	static final String WIZARD_DESCRIPTION = "Select the context for running queries.";
	private IWorkbench _workbench;
	private IStructuredSelection _selection;
	private AbstractContextWizardPage[] _pages;

	/**
	 * Adding the page to the wizard.
	 */
	public void addPages() {
		if (XQueryUI.getDefault().getActiveXQueryEditor() == null) {
			addPage(new NoActiveEditorWizardPage());
		} else if (ContextSwitcherRegistration.getInstance()
				.getContextSwitchers().size() < 1) {
			addPage(new NoContextWizardPage());
		} else if (_pages == null) {
			addPage(new ChooseContextWizardPage(_workbench, _selection));
		} else {
			for (AbstractContextWizardPage page : _pages) {
				addPage(page);
			}
		}
	}

	/**
	 * This method figures out whether the 'Finish' button should be enabled.
	 * The button should only be enabled on the NewConnectionWizardPage.
	 */
	public boolean canFinish() {
		return getContainer().getCurrentPage().isPageComplete();
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_workbench = workbench;
		_selection = selection;
		setWindowTitle(WIZARD_TITLE);
		setNeedsProgressMonitor(true);
	}

	@Override
	public boolean performFinish() {
		IWizardPage currentPage = getContainer().getCurrentPage();
		IConnectionContext context = null;
		if (currentPage instanceof ChooseContextWizardPage) {
			ChooseContextWizardPage page = (ChooseContextWizardPage) currentPage;
			context = page.getSelected().getDefault();
		} else if (currentPage instanceof AbstractContextWizardPage) {
			AbstractContextWizardPage page = (AbstractContextWizardPage) currentPage;
			context = page.getConnectionContext();
		}
		if (context != null) {
			IXQueryEditor editor = XQueryUI.getDefault()
					.getActiveXQueryEditor();
			if (editor != null) {
				editor.setConnectionContext(context);
			}
		}

		return true;
	}

	public void setPages(AbstractContextWizardPage[] pages) {
		_pages = pages;
	}
}
