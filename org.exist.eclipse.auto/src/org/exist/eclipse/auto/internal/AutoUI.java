package org.exist.eclipse.auto.internal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Markus Tanner
 */
public class AutoUI extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.exist.eclipse.auto";

	// The shared instance
	private static AutoUI plugin;

	// Resource bundle.
	private FormColors _formColors;
	public static final String IMG_HORIZONTAL = "horizontal";
	public static final String IMG_VERTICAL = "vertical";
	public static final String IMG_SAMPLE = "sample";
	public static final String IMG_RUN = "run";
	public static final String IMG_EXIST_ECLIPSE_LOGO = "logo";

	/**
	 * The constructor
	 */
	public AutoUI() {
		plugin = this;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry registry) {
		registerImage(registry, IMG_HORIZONTAL, "th_horizontal.gif");
		registerImage(registry, IMG_VERTICAL, "th_vertical.gif");
		registerImage(registry, IMG_SAMPLE, "sample.gif");
		registerImage(registry, IMG_RUN, "run.gif");
		registerImage(registry, IMG_EXIST_ECLIPSE_LOGO, "hslu_exist_eclipse_logo.jpg");
	}

	/**
	 * Registers the images
	 * 
	 * @param registry
	 * @param key
	 * @param fileName
	 */
	private void registerImage(ImageRegistry registry, String key, String fileName) {
		IPath path = new Path("icons/".concat(fileName));
		try {
			URL url = FileLocator.find(getBundle(), path, null);
			if (url != null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
			AutoUI.getDefault().getLog().log(
					new Status(IStatus.ERROR, PLUGIN_ID, "Unable to register image: " + fileName + " key: " + key, e));
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		try {
			if (_formColors != null) {
				_formColors.dispose();
				_formColors = null;
			}
		} finally {
			super.stop(context);
		}
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static AutoUI getDefault() {
		return plugin;
	}

	/**
	 * Gets the image.
	 * 
	 * @param key the image key
	 * @return the image for the given key
	 */
	public Image getImage(String key) {
		return getImageRegistry().get(key);
	}

	/**
	 * Gets the image descriptor
	 * 
	 * @param key the image key
	 * @return the image descriptor for the given key
	 */
	public ImageDescriptor getImageDescriptor(String key) {
		return getImageRegistry().getDescriptor(key);
	}

	/**
	 * Gets the Form Colors
	 * 
	 * @param display the display instance
	 * @return the from colors for the specified display
	 */
	public FormColors getFormColors(Display display) {
		if (_formColors == null) {
			_formColors = new FormColors(display);
			_formColors.markShared();
		}
		return _formColors;
	}

	/**
	 * Returns a symbolic id of the plugin instance.
	 * 
	 * @return Id
	 */
	public static String getId() {
		return getDefault().getBundle().getSymbolicName();
	}

	public String getFileInput(String filename) {
		File fileToOpen = new File(filename);
		StringBuilder result = new StringBuilder();
		if (fileToOpen.exists() && fileToOpen.isFile()) {

			final IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
			try (InputStream inputStream = fileStore.openInputStream(EFS.NONE, null);
					ByteArrayOutputStream out = new ByteArrayOutputStream();) {

				byte[] buffer = new byte[4096];
				for (int len; (len = inputStream.read(buffer)) != -1;) {
					out.write(buffer, 0, len);
					result.append(new String(out.toByteArray(), Charset.forName("UTF-8").name()).trim());
				}
			} catch (CoreException | IOException e) {
				// ignore
			}
		}
		return result.toString();
	}

	public void errorDialog(String title, String message, IStatus s) {
		// if the 'message' resource string and the IStatus' message are the
		// same,
		// don't show both in the dialog
		if (s != null && message.equals(s.getMessage())) {
			message = null;
		}
		ErrorDialog.openError(getWorkbench().getActiveWorkbenchWindow().getShell(), title, message, s);
	}

	public void infoDialog(String title, String message) {
		Shell shell = getWorkbench().getDisplay().getActiveShell();
		MessageDialog.openInformation(shell, title, message);
	}

}
