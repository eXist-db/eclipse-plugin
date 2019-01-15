/**
 * BrowseViewListener.java
 */
package org.exist.eclipse.browse.internal.views.browse;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.listener.IViewListener;

/**
 * With this implementation you will inform, when a connection is active and you
 * can open the browse view.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class BrowseViewListener implements IViewListener {

	@Override
	public void openView(IWorkbenchPage page) {
		try {
			page.showView(BrowseView.ID);
		} catch (PartInitException e) {
			String message = "Error while open browse view";
			Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(),
					message, e);
			BrowsePlugin.getDefault().errorDialog(message, e.getMessage(),
					status);
		}

	}

}
