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
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * This action group will build the menu for the browse extension.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ActionGroupCollectionExtension extends ActionGroup {

	private final BrowseView _view;
	private boolean _seperatorAdded;

	public ActionGroupCollectionExtension(BrowseView view) {
		_view = view;
	}

	@Override
	public void fillContextMenu(IMenuManager manager) {
		if (!_view.getViewer().getSelection().isEmpty()) {
			IBrowseItem[] selection = getSelection();
			if (selection.length > 0) {
				try {
					IExtensionRegistry reg = Platform.getExtensionRegistry();
					IExtensionPoint exPoint = reg.getExtensionPoint(
							BrowsePlugin.getId(), "collection");
					IExtension[] browsers = exPoint.getExtensions();
					for (IExtension extension : browsers) {
						IConfigurationElement[] configurations = extension
								.getConfigurationElements();
						_seperatorAdded = false;
						for (IConfigurationElement element : configurations) {
							ShowOnEnum showOnEnum = ShowOnEnum.valueOf(element
									.getAttribute("showOn"));
							if (showOnEnum.eval(selection)) {
								boolean isMultiSelect = Boolean
										.parseBoolean(element
												.getAttribute("isMultiselect"));
								if (selection.length < 2 || isMultiSelect) {
									IBrowseListener listener = (IBrowseListener) element
											.createExecutableExtension("class");
									Action action = new ActionBrowseListener(
											_view, listener);
									action
											.setText(element
													.getAttribute("name"));
									action.setToolTipText(element
											.getAttribute("description"));
									String icon = element.getAttribute("icon");
									String id = extension
											.getNamespaceIdentifier();

									if (icon != null) {
										action.setImageDescriptor(AbstractUIPlugin
												.imageDescriptorFromPlugin(id,
														icon));
									}

									addSeperator(manager);
									manager.add(action);
								}

							}
						}

					}
				} catch (Exception e) {
					String message = "Error while fill context menu";
					Status status = new Status(IStatus.ERROR, BrowsePlugin
							.getId(), message, e);
					BrowsePlugin.getDefault().errorDialog(message,
							e.getMessage(), status);
				}
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////
	// private methods
	// ////////////////////////////////////////////////////////////////////////////////////////////
	private IBrowseItem[] getSelection() {
		TreeItem[] selection = _view.getViewer().getTree().getSelection();
		IBrowseItem[] result = new IBrowseItem[selection.length];
		for (int i = 0; i < selection.length; i++) {
			Object object = selection[i].getData();
			if (object instanceof IBrowseItem) {
				result[i] = (IBrowseItem) object;
			} else {
				result = new IBrowseItem[0];
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
