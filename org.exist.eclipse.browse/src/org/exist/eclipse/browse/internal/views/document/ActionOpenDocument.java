/**
 * DocumentActionOpen.java
 */

package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
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
	private final IStructuredSelection _selection;

	public ActionOpenDocument(String editorId, IDocumentItem item) {
		this(editorId, new StructuredSelection(item));
	}

	public ActionOpenDocument(String editorId, Viewer viewer) {
		this(editorId, (IStructuredSelection) viewer.getSelection());
	}

	protected ActionOpenDocument(String editorId, IStructuredSelection selection) {
		_editorId = editorId;
		_selection = selection;
		setText("Open");
	}

	@Override
	public void run() {

		for (Object it : _selection.toArray()) {
			IDocumentItem _item = (IDocumentItem) it;

			IBrowseService bService = _item.getParent().getAdapter(IBrowseService.class);
			IDocumentService service = _item.getAdapter(IDocumentService.class);

			if (_item.getParent().getConnection().getAdapter(IManagementService.class).check() && bService.check()
					&& service.check()) {
				IStorageEditorInput input = new DocumentStorageEditorInput(new DocumentStorage(_item));
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, _editorId);
				} catch (PartInitException e) {
					String message = "Error while open document";
					Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(), message, e);
					BrowsePlugin.getDefault().errorDialog(message, e.getMessage(), status);
				}
			}
		}
	}
}
