package org.exist.eclipse.internal;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.exist.eclipse.IConnection;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BasePlugin extends AbstractUIPlugin {
	private static final String CONNECTIONS_XML = "connections.xml";
	// The shared instance
	private static BasePlugin plugin;

	/**
	 * The constructor
	 */
	public BasePlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		IPath connectionPath = getStateLocation().append(CONNECTIONS_XML);
		ConnectionBoxMemento memento = new ConnectionBoxMemento(connectionPath.toFile().getAbsolutePath());
		ConnectionBox.getInstance().setMemento(memento);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ConnectionBox box = ConnectionBox.getInstance();
		for (IConnection connection : box.getConnections()) {
			connection.close();
		}
		IPath connectionPath = getStateLocation().append(CONNECTIONS_XML);
		box.getMemento().writeStateAsXml(connectionPath.toFile().getAbsolutePath());
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static BasePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative
	 * path
	 * 
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(getId(), path);
	}

	/**
	 * Returns the current plugin id.
	 * 
	 * @return the symbolic bundle name of the plugin
	 */
	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}

	/**
	 * Logs the given {@code message} for the given {@code severity}.
	 * 
	 * @param severity the message severity
	 * @param message the log message
	 */
	public static void log(int severity, String message) {
		log(severity, message, null);
	}

	/**
	 * Logs the given {@code message} for the given {@code severity} and {@code exception}.
	 * 
	 * @param severity the message severity
	 * @param message the log message
	 * @param exception the exception
	 */
	public static void log(int severity, String message, Throwable exception) {
		plugin.getLog().log(new Status(severity, getId(), message, exception));
	}

	/**
	 * Shows the error dialog with given {@code title}, {@code message} and {@code status}.
	 * 
	 * @param title the message title
	 * @param message the message
	 * @param status the status
	 */
	public void errorDialog(String title, String message, IStatus status) {
		// if the 'message' resource string and the IStatus' message are the
		// same,
		// don't show both in the dialog
		if (status != null && message.equals(status.getMessage())) {
			message = null;
		}
		ErrorDialog.openError(getWorkbench().getActiveWorkbenchWindow().getShell(), title, message, status);
	}
}
