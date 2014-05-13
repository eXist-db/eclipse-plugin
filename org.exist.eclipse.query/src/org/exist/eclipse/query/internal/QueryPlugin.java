package org.exist.eclipse.query.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.exist.eclipse.listener.ConnectionRegistration;
import org.exist.eclipse.query.internal.auto.ContextListener;
import org.exist.eclipse.query.internal.xquery.context.ContextSwitcherListener;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class QueryPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.exist.eclipse.query";

	// The shared instance
	private static QueryPlugin plugin;

	// The connection listener
	private ContextListener _contextListener;

	// The connection listener for context switch in xquery plugin
	private ContextSwitcherListener _contextSwitcherListener;

	/**
	 * The constructor
	 */
	public QueryPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		_contextListener = new ContextListener();
		ConnectionRegistration.addListener(_contextListener);
		_contextSwitcherListener = new ContextSwitcherListener();
		ConnectionRegistration.addListener(_contextSwitcherListener);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ConnectionRegistration.removeListener(_contextListener);
		ConnectionRegistration.removeListener(_contextSwitcherListener);
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static QueryPlugin getDefault() {
		return plugin;
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
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
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
