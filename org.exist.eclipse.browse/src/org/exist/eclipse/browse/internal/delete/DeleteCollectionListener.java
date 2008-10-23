/**
 * CreateBrowseListener.java
 */
package org.exist.eclipse.browse.internal.delete;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;

/**
 * @author Pascal Schmidiger
 * 
 */
public class DeleteCollectionListener implements IBrowseListener {

	private IWorkbenchPage _page;

	public void actionPerformed(IBrowseItem[] items) {
		DeleteCollectionWizard wizard = new DeleteCollectionWizard(items);
		wizard.init(_page.getWorkbenchWindow().getWorkbench(), null);
		WizardDialog dialog = new WizardDialog(_page.getWorkbenchWindow()
				.getShell(), wizard);
		dialog.open();
	}

	public void init(IWorkbenchPage page) {
		_page = page;
	}

}
