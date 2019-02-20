/**
 * BrowseActionGroup.java
 */

package org.exist.eclipse.browse.internal.views.document;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentListener;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * This action group will build the menu for the browse extension.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionGroupDocumentExtension extends ActionGroup {

	private final DocumentView _view;
	private boolean _seperatorAdded;

	public ActionGroupDocumentExtension(DocumentView view) {
		_view = view;
	}

	@Override
	public void fillContextMenu(IMenuManager manager) {
		ISelection selection = _view.getViewer().getSelection();
		// Add editors to the menu
		Object obj = IStructuredSelection.class.cast(selection).getFirstElement();
		if (obj instanceof IDocumentItem) {
			try {
				IExtensionRegistry reg = Platform.getExtensionRegistry();
				IExtensionPoint exPoint = reg.getExtensionPoint(BrowsePlugin.getId(), "document");
				IExtension[] documents = exPoint.getExtensions();
				for (IExtension extension : documents) {
					IConfigurationElement[] configurations = extension.getConfigurationElements();
					_seperatorAdded = false;
					for (IConfigurationElement element : configurations) {
						IDocumentListener listener = (IDocumentListener) element.createExecutableExtension("class");
						Action action = new ActionDocumentListener(_view, listener);
						action.setText(element.getAttribute("name"));
						action.setToolTipText(element.getAttribute("name"));
						String icon = element.getAttribute("icon");
						String id = extension.getNamespaceIdentifier();

						if (icon != null) {
							action.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(id, icon));
						}
						addSeperator(manager);
						manager.add(action);
					}
				}
			} catch (Exception e) {
				String message = "Error while fill context menu";
				Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(), message, e);
				BrowsePlugin.getDefault().errorDialog(message, e.getMessage(), status);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////
	private void addSeperator(IMenuManager manager) {
		if (!_seperatorAdded) {
			manager.add(new Separator());
			_seperatorAdded = true;
		}
	}
}
