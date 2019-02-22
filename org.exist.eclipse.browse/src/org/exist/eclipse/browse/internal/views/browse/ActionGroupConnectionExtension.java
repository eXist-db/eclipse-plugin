/**
 * BrowseActionGroup.java
 */
package org.exist.eclipse.browse.internal.views.browse;

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
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.connection.IConnectionListener;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * This action group will build the menu for the browse extension.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionGroupConnectionExtension extends ActionGroup {

	private final BrowseView _view;
	private boolean _seperatorAdded;

	public ActionGroupConnectionExtension(BrowseView view) {
		_view = view;
	}

	@Override
	public void fillContextMenu(IMenuManager manager) {
		if (!_view.getViewer().getSelection().isEmpty()) {
			IConnection[] selection = getSelection();
			if (selection.length > 0) {
				try {
					IExtensionRegistry reg = Platform.getExtensionRegistry();
					IExtensionPoint exPoint = reg.getExtensionPoint(BrowsePlugin.getId(), "connection");
					IExtension[] connections = exPoint.getExtensions();
					for (IExtension extension : connections) {
						IConfigurationElement[] configurations = extension.getConfigurationElements();
						_seperatorAdded = false;
						for (IConfigurationElement element : configurations) {
							StateEnum stateEnum = StateEnum.valueOf(element.getAttribute("state"));
							if (stateEnum.eval(selection)) {
								boolean isMultiSelect = Boolean.parseBoolean(element.getAttribute("isMultiselect"));
								if (selection.length < 2 || isMultiSelect) {
									IConnectionListener listener = (IConnectionListener) element
											.createExecutableExtension("class");
									Action action = new ActionConnectionListener(_view, listener);
									action.setText(element.getAttribute("name"));
									String icon = element.getAttribute("icon");
									String id = extension.getNamespaceIdentifier();

									if (icon != null) {
										action.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(id, icon));
									}

									addSeperator(manager);
									manager.add(action);
								}
							}
						}

					}
				} catch (Exception e) {
					String message = "Error while fill context menu";
					Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(), message, e);
					BrowsePlugin.getDefault().errorDialog(message, e.getMessage(), status);
				}
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////
	private IConnection[] getSelection() {
		TreeItem[] selection = _view.getViewer().getTree().getSelection();
		IConnection[] result = new IConnection[selection.length];
		for (int i = 0; i < selection.length; i++) {
			Object object = selection[i].getData();
			if (object instanceof IConnection) {
				result[i] = (IConnection) object;
			} else {
				result = new IConnection[0];
				break;
			}
		}
		return result;
	}

	private void addSeperator(IMenuManager manager) {
		if (!_seperatorAdded) {
			manager.add(new Separator());
			_seperatorAdded = true;
		}
	}
}
