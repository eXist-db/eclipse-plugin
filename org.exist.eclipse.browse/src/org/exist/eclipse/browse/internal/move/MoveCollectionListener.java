/**
 * CreateBrowseListener.java
 */
package org.exist.eclipse.browse.internal.move;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.browse.IBrowseService;

/**
 * @author Pascal Schmidiger
 * 
 */
public class MoveCollectionListener implements IBrowseListener {

	private IWorkbenchPage _page;

	@Override
	public void actionPerformed(IBrowseItem[] items) {
		IBrowseService service = items[0]
				.getAdapter(IBrowseService.class);
		if (service.check()) {
			MoveCollectionWizard wizard = new MoveCollectionWizard(items[0]);
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
