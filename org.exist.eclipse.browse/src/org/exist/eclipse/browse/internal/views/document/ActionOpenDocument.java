/**
 * DocumentActionOpen.java
 */
package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentService;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.browse.internal.edit.DocumentStorage;
import org.exist.eclipse.browse.internal.edit.DocumentStorageEditorInput;

/**
 * An action to open an editor.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionOpenDocument extends Action {

	private final String _editorId;
	private final DocumentView _view;
	private final IDocumentItem _item;

	public ActionOpenDocument(DocumentView view, String editorId,
			IDocumentItem item) {
		_view = view;
		_editorId = editorId;
		_item = item;
	}

	@Override
	public void run() {
		IBrowseService bService = (IBrowseService) _item.getParent()
				.getAdapter(IBrowseService.class);
		IDocumentService service = (IDocumentService) _item
				.getAdapter(IDocumentService.class);

		if (IManagementService.class.cast(
				_item.getParent().getConnection().getAdapter(
						IManagementService.class)).check()
				&& bService.check() && service.check()) {
			IStorageEditorInput input = new DocumentStorageEditorInput(
					new DocumentStorage(_item));
			try {
				_view.getViewSite().getWorkbenchWindow().getActivePage()
						.openEditor(input, _editorId);
			} catch (PartInitException e) {
				String message = "Error while open document";
				Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(),
						message, e);
				BrowsePlugin.getDefault().errorDialog(message, e.getMessage(),
						status);
			}
		}
	}

}
