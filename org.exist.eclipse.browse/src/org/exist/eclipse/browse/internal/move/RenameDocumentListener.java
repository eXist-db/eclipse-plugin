/**
 * RenameDocumentListener.java
 */
package org.exist.eclipse.browse.internal.move;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentListener;

/**
 * @author Pascal Schmidiger
 * 
 */
public class RenameDocumentListener implements IDocumentListener {

	private IWorkbenchPage _page;

	public void actionPerformed(IDocumentItem item) {
		RenameDocumentWizard wizard = new RenameDocumentWizard(item);
		wizard.init(_page.getWorkbenchWindow().getWorkbench(), null);
		WizardDialog dialog = new WizardDialog(_page.getWorkbenchWindow()
				.getShell(), wizard);
		dialog.open();
	}

	public void init(IWorkbenchPage page) {
		_page = page;
	}

}
