/**
 * RestoreBrowseListener.java
 */
package org.exist.eclipse.util.internal.restore;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.browse.IBrowseService;

/**
 * This is the listener that is invoked if the user would like to start a
 * restore process via the context menu.
 * 
 * @author Markus Tanner
 * 
 */
public class RestoreBrowseListener implements IBrowseListener {

	private IWorkbenchPage _page;

	@Override
	public void actionPerformed(IBrowseItem[] items) {
		IBrowseService service = (IBrowseService) items[0]
				.getAdapter(IBrowseService.class);
		if (service.check()) {
			RestoreWizard wizard = new RestoreWizard(items[0]);
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
