package org.exist.eclipse.xquery.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.exist.eclipse.xquery.ui.editor.IXQueryEditor;
import org.exist.eclipse.xquery.ui.internal.text.KeyWordContainer;
import org.exist.eclipse.xquery.ui.internal.text.XQueryTextTools;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Pascal Schmidiger
 */
public class XQueryUI extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.exist.eclipse.xquery.ui";

	// The shared instance
	private static XQueryUI _plugin;

	private XQueryTextTools _xqueryTextTools;
	private KeyWordContainer _keyWordContainer;

	/**
	 * The constructor
	 */
	public XQueryUI() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		_plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		_plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static XQueryUI getDefault() {
		return _plugin;
	}

	public synchronized XQueryTextTools getTextTools() {
		if (_xqueryTextTools == null)
			_xqueryTextTools = new XQueryTextTools(true);
		return _xqueryTextTools;
	}

	public synchronized KeyWordContainer getKeyWordContainer() {
		if (_keyWordContainer == null) {
			_keyWordContainer = new KeyWordContainer();
			_keyWordContainer.load();
		}
		return _keyWordContainer;
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

	public IXQueryEditor getActiveXQueryEditor() {
		IEditorPart activeEditor = null;
		IWorkbenchWindow window = getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				activeEditor = page.getActiveEditor();
			}
		}
		if (activeEditor != null && activeEditor instanceof IXQueryEditor) {
			return IXQueryEditor.class.cast(activeEditor);
		} else {
			return null;
		}
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
