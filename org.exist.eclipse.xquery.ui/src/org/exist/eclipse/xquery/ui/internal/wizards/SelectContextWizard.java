package org.exist.eclipse.xquery.ui.internal.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.context.AbstractContextWizardPage;
import org.exist.eclipse.xquery.ui.context.IConnectionContext;
import org.exist.eclipse.xquery.ui.context.IContextSwitcher;
import org.exist.eclipse.xquery.ui.editor.IXQueryEditor;

/**
 * Start wizard for selecting the context for query running.
 * 
 * @author Pascal Schmidiger
 */
public class SelectContextWizard extends Wizard implements IWorkbenchWizard {

	private static String _lastSelectedConnection;

	protected static void setLastSelectedConnection(String lastSelectedConnection) {
		_lastSelectedConnection = lastSelectedConnection;
	}

	protected static String getLastSelectedConnection() {
		if (_lastSelectedConnection == null) {
			_lastSelectedConnection = "";
		}
		return _lastSelectedConnection;
	}

	public static final String WIZARD_TITLE = "Select a connection";
	static final String WIZARD_DESCRIPTION = "Select a connection for running queries.";
	private IWorkbench _workbench;
	private IStructuredSelection _selection;

	@Override
	public boolean needsPreviousAndNextButtons() {
		return false;
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		if (XQueryUI.getDefault().getActiveXQueryEditor() == null) {
			addPage(new NoActiveEditorWizardPage());
		} else {
			// do nothing
			addPage(new ChooseContextWizardPage(_workbench, _selection));
		}
	}

	/**
	 * This method figures out whether the 'Finish' button should be enabled. The
	 * button should only be enabled on the NewConnectionWizardPage.
	 */
	@Override
	public boolean canFinish() {
		return getContainer().getCurrentPage().isPageComplete();
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
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
			IContextSwitcher switcher = page.getSelected();
			setLastSelectedConnection(switcher.getName());
			context = switcher.getDefault();
		} else if (currentPage instanceof AbstractContextWizardPage) {
			AbstractContextWizardPage page = (AbstractContextWizardPage) currentPage;
			context = page.getConnectionContext();
		}
		if (context != null) {
			IXQueryEditor editor = XQueryUI.getDefault().getActiveXQueryEditor();
			if (editor != null) {
				editor.setConnectionContext(context);
			}
		}

		return true;
	}
}
