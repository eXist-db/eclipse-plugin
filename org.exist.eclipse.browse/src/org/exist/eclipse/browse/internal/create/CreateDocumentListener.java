/**
 * CreateBrowseListener.java
 */
package org.exist.eclipse.browse.internal.create;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.browse.IBrowseService;

/**
 * Listener to create a new document.
 * 
 * @author Pascal Schmidiger
 */
public class CreateDocumentListener implements IBrowseListener {

	private IWorkbenchPage _page;

	@Override
	public void actionPerformed(IBrowseItem[] items) {
		IBrowseService service = items[0]
				.getAdapter(IBrowseService.class);
		if (service.check()) {
			CreateDocumentWizard wizard = new CreateDocumentWizard(items[0]);
			wizard.init(_page.getWorkbenchWindow().getWorkbench(), null);
			WizardDialog dialog = new WizardDialog(_page.getWorkbenchWindow()
					.getShell(), wizard);
			dialog.open();
		}
	}

	@Override
	public void init(IWorkbenchPage page) {
		_page = page;
	}

}
