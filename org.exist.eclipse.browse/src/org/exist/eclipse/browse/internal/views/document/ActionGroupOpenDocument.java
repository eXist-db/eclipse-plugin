/**
 * DocumentOpenActionGroup.java
 */
package org.exist.eclipse.browse.internal.views.document;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionGroup;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
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
	private IDoubleClickListener _doubleClickListener;
	private Action _doubleClickAction;

	public ActionGroupOpenDocument(DocumentView view) {
		_view = view;
	}

	@Override
	public void fillContextMenu(IMenuManager manager) {
		ISelection selection = _view.getViewer().getSelection();
		// Add editors to the menu
		Object element = IStructuredSelection.class.cast(selection)
				.getFirstElement();
		if (element instanceof IDocumentItem) {
			createDoubleClickAction();
			manager.add(_doubleClickAction);

			IDocumentItem item = DocumentItem.class.cast(element);
			IEditorRegistry registry = PlatformUI.getWorkbench()
					.getEditorRegistry();
			IEditorDescriptor[] editors = registry.getEditors(getName(item));
			IEditorDescriptor[] editorsXml = null;
			if (isXmlResource(item)) {
				editorsXml = registry.getEditors("test.xml");
			}

			Collection<IEditorDescriptor> usedEditors = new ArrayList<IEditorDescriptor>();

			if (editors.length > 0) {
				MenuManager submenu = new MenuManager("Open With");
				for (IEditorDescriptor editorDescriptor : editors) {
					ActionOpenDocument actionOpen = new ActionOpenDocument(
							_view, editorDescriptor.getId(), item);
					actionOpen.setText(editorDescriptor.getLabel());
					actionOpen.setImageDescriptor(editorDescriptor
							.getImageDescriptor());
					submenu.add(actionOpen);
					usedEditors.add(editorDescriptor);
				}

				if (editorsXml != null) {
					for (IEditorDescriptor editorDescriptor : editorsXml) {
						if (!usedEditors.contains(editorDescriptor)) {
							ActionOpenDocument actionOpen = new ActionOpenDocument(
									_view, editorDescriptor.getId(), item);
							actionOpen.setText(editorDescriptor.getLabel());
							actionOpen.setImageDescriptor(editorDescriptor
									.getImageDescriptor());
							submenu.add(actionOpen);
						}
					}
				}
				manager.add(submenu);
			}
		}
	}

	public void hookDoubleClickAction() {
		if (_doubleClickListener != null) {
			_view.getViewer().removeDoubleClickListener(_doubleClickListener);
		}

		ISelection selection = _view.getViewer().getSelection();

		if (_view.hasItem()
				&& IManagementService.class.cast(
						_view.getItem().getConnection().getAdapter(
								IManagementService.class)).check()
				&& IBrowseService.class.cast(
						_view.getItem().getAdapter(IBrowseService.class))
						.check() && !selection.isEmpty()) {
			Object element = IStructuredSelection.class.cast(selection)
					.getFirstElement();
			if (element instanceof IDocumentItem) {
				createDoubleClickAction();
				_doubleClickListener = new IDoubleClickListener() {

					public void doubleClick(DoubleClickEvent event) {
						_doubleClickAction.run();
					}
				};

				_view.getViewer().addDoubleClickListener(_doubleClickListener);
			}
		}
	}

	//
	// private methods
	//
	private void createDoubleClickAction() {
		IStructuredSelection selection = (IStructuredSelection) _view
				.getViewer().getSelection();
		Object element = selection.getFirstElement();
		if (element instanceof IDocumentItem) {
			IDocumentItem item = DocumentItem.class.cast(element);

			IEditorRegistry registry = PlatformUI.getWorkbench()
					.getEditorRegistry();
			IEditorDescriptor defaultEditor = registry
					.getDefaultEditor(getName(item));

			if (defaultEditor == null) {
				defaultEditor = registry
						.findEditor("org.eclipse.ui.DefaultTextEditor");
			}

			if (defaultEditor != null) {
				_doubleClickAction = new ActionOpenDocument(_view,
						defaultEditor.getId(), item);
				_doubleClickAction.setText("Open");
				_doubleClickAction.setImageDescriptor(defaultEditor
						.getImageDescriptor());
			}
		}
	}

	private String getName(IDocumentItem item) {
		String name = item.getName();
		if (isXmlResource(item) && name.lastIndexOf(".") != name.length() - 4) {
			name += ".xml";
		}
		return name;
	}

	private boolean isXmlResource(IDocumentItem item) {
		boolean result = false;
		try {
			if (item.getResource().getResourceType().equals(
					XMLResource.RESOURCE_TYPE)) {
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
}
