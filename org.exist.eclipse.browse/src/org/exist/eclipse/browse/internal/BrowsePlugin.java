package org.exist.eclipse.browse.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.exist.eclipse.browse.internal.views.browse.BrowseViewListener;
import org.exist.eclipse.listener.ViewRegistration;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BrowsePlugin extends AbstractUIPlugin {
	// The shared instance
	private static BrowsePlugin plugin;

	private BrowseViewListener _viewListener;

	public BrowsePlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		_viewListener = new BrowseViewListener();
		ViewRegistration.getInstance().addListener(_viewListener);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		ViewRegistration.getInstance().removeListener(_viewListener);
		_viewListener = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static BrowsePlugin getDefault() {
		return plugin;
	}

	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(getId(), path);
	}

	public void errorDialog(String title, String message, IStatus s) {
		// if the 'message' resource string and the IStatus' message are the
		// same,
		// don't show both in the dialog
		if (s != null && message.equals(s.getMessage())) {
			message = null;
		}
		ErrorDialog.openError(getWorkbench().getActiveWorkbenchWindow()
				.getShell(), title, message, s);
	}

	public void infoDialog(String title, String message) {
		Shell shell = getWorkbench().getDisplay().getActiveShell();
		MessageDialog.openInformation(shell, title, message);
	}
}
