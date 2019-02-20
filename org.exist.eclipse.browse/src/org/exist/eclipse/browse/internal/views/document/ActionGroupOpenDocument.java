/**
 * DocumentOpenActionGroup.java
 */

package org.exist.eclipse.browse.internal.views.document;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionGroup;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.browse.internal.create.CreateDocumentListener;
import org.exist.eclipse.browse.internal.create.ImportDocumentsFromWorkingSetListener;
import org.exist.eclipse.browse.internal.create.ImportDocumentsListener;
import org.exist.eclipse.browse.internal.document.DocumentItem;
import org.xmldb.api.modules.XMLResource;

/**
 * This action group builds the menu to open a document.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionGroupOpenDocument extends ActionGroup {
	private final DocumentView _view;
	private Action _importFromWorkingSetAction;

	public ActionGroupOpenDocument(DocumentView view) {
		_view = view;
		_importFromWorkingSetAction = new Action("Import from Working Set...",
				BrowsePlugin.getImageDescriptor("icons/import.png")) {
			@Override
			public void run() {
				ImportDocumentsFromWorkingSetListener createDoc = new ImportDocumentsFromWorkingSetListener();
				createDoc.init(_view.getSite().getPage());
				createDoc.actionPerformed(new IBrowseItem[] { _view.getItem() });
			}
		};
	}

	@Override
	public void fillActionBars(IActionBars actionBars) {
		actionBars.getMenuManager().add(_importFromWorkingSetAction);
	}

	@Override
	public void fillContextMenu(IMenuManager manager) {
		ISelection selection = _view.getViewer().getSelection();
		// Add editors to the menu
		Object element = IStructuredSelection.class.cast(selection).getFirstElement();
		if (element instanceof IDocumentItem) {

			IDocumentItem item = DocumentItem.class.cast(element);
			IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
			IEditorDescriptor[] editors = registry.getEditors(getName(item));
			IEditorDescriptor[] editorsXml = null;
			if (isXmlResource(item)) {
				editorsXml = registry.getEditors("test.xml");
			}

			Collection<IEditorDescriptor> usedEditors = new ArrayList<>();

			if (editors.length > 0) {

				manager.add(new ActionOpenDocument(editors[0].getId(), _view.getViewer()));

				MenuManager submenu = new MenuManager("Open With");
				for (IEditorDescriptor editorDescriptor : editors) {
					ActionOpenDocument actionOpen = new ActionOpenDocument(editorDescriptor.getId(), _view.getViewer());
					actionOpen.setText(editorDescriptor.getLabel());
					actionOpen.setImageDescriptor(editorDescriptor.getImageDescriptor());
					submenu.add(actionOpen);
					usedEditors.add(editorDescriptor);
				}

				if (editorsXml != null) {
					for (IEditorDescriptor editorDescriptor : editorsXml) {
						if (!usedEditors.contains(editorDescriptor)) {
							ActionOpenDocument actionOpen = new ActionOpenDocument(editorDescriptor.getId(),
									_view.getViewer());
							actionOpen.setText(editorDescriptor.getLabel());
							actionOpen.setImageDescriptor(editorDescriptor.getImageDescriptor());
							submenu.add(actionOpen);
						}
					}
				}
				manager.add(submenu);
			}

		}

		Action createDocumentAction = new Action("New Document...",
				BrowsePlugin.getImageDescriptor("icons/document-new.png")) {
			@Override
			public void run() {
				CreateDocumentListener createDoc = new CreateDocumentListener();
				createDoc.init(_view.getSite().getPage());
				createDoc.actionPerformed(new IBrowseItem[] { _view.getItem() });
			}
		};
		ExportDocumentsAction exportAction = new ExportDocumentsAction(_view.getViewer());
		Action importAction = new Action("Import...", BrowsePlugin.getImageDescriptor("icons/import.png")) {
			@Override
			public void run() {
				ImportDocumentsListener createDoc = new ImportDocumentsListener();
				createDoc.init(_view.getSite().getPage());
				createDoc.actionPerformed(new IBrowseItem[] { _view.getItem() });
			}
		};

		createDocumentAction.setEnabled(_view.hasItem());
		exportAction.setEnabled(_view.hasItem());
		importAction.setEnabled(_view.hasItem());

		manager.add(new Separator());
		manager.add(createDocumentAction);
		manager.add(new Separator());
		manager.add(importAction);
		manager.add(_importFromWorkingSetAction);
		manager.add(exportAction);

		updateActionBars();
	}

	@Override
	public void updateActionBars() {
		super.updateActionBars();
		_importFromWorkingSetAction.setEnabled(_view.hasItem());
	}

	/**
	 * @return nullable if none
	 */
	public static IEditorDescriptor getDefaultEditor(IDocumentItem item) {
		IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
		IEditorDescriptor defaultEditor = registry.getDefaultEditor(getName(item));

		if (defaultEditor == null) {
			defaultEditor = registry.findEditor("org.eclipse.ui.DefaultTextEditor");
		}
		return defaultEditor;
	}

	private static String getName(IDocumentItem item) {
		String name = item.getName();
		if (isXmlResource(item) && name.lastIndexOf(".") != name.length() - 4) {
			name += ".xml";
		}
		return name;
	}

	private static boolean isXmlResource(IDocumentItem item) {
		boolean result = false;
		try {
			if (item.getResource().getResourceType().equals(XMLResource.RESOURCE_TYPE)) {
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
}
