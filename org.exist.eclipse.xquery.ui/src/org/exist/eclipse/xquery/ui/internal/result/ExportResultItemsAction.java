package org.exist.eclipse.xquery.ui.internal.result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * 
 * Exports selected ResultItems to destination folder.
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class ExportResultItemsAction extends Action {

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static void copy(final Reader in, final Writer out) {
		try {
			final char[] chars = new char[4096];
			int charsRead;
			while ((charsRead = in.read(chars)) > -1) {
				out.write(chars, 0, charsRead);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// ignore
			}
			try {
				out.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	private final TableViewer _viewer;

	public ExportResultItemsAction(TableViewer viewer) {
		super("Export...", XQueryUI.getImageDescriptor("icons/export.png"));
		_viewer = viewer;
	}

	@Override
	public void run() {
		Object[] sel = ((IStructuredSelection) _viewer.getSelection())
				.toArray();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		Map<ResultItem, File> files = new HashMap<>();

		if (sel.length == 1) {
			ResultItem document = (ResultItem) sel[0];
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			String[] filterExtensions = { "*.*", "*.xml" };
			String[] filterNames = { "All Files (*.*)", "XML Files (*.xml)" };
			dialog.setFilterExtensions(filterExtensions);
			dialog.setFilterNames(filterNames);
			String destFile = dialog.open();
			if (destFile != null) {
				files.put(document, new File(destFile));
			}
		} else if (sel.length > 1) {
			DirectoryDialog directoryDialog = new DirectoryDialog(shell);
			directoryDialog.setMessage("Select the destination folder.");
			String folder = directoryDialog.open();
			if (folder != null) {
				File folderr = new File(folder);
				for (Object it : sel) {
					ResultItem resultItem = (ResultItem) it;
					files.put(resultItem, new File(folderr,
							getSafeFileName(resultItem.getGroup()) + "_result_"
									+ (resultItem.getIndex() + 1) + ".txt"));
				}
			}
		}

		if (!files.isEmpty()) {
			boolean hadErrors = false;
			for (Map.Entry<ResultItem, File> all : files.entrySet()) {
				ResultItem resultItem = all.getKey();
				File file = all.getValue();

				String content = resultItem.getContent();
				try (Reader reader = new StringReader(content);
						OutputStream out = new FileOutputStream(file);
						Writer writer = new OutputStreamWriter(out,
								DEFAULT_ENCODING)) {
					copy(reader, writer);
				} catch (Exception e) {
					hadErrors = true;
					XQueryUI.getDefault()
							.getLog()
							.log(new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID,
									"An error occured while exporting result to '"
											+ file + "': " + e));
				}
			}

			if (hadErrors) {
				MessageDialog
						.openError(shell, "Export Results",
								"Errors occured while exporting results. See Eclipse Error Log for details.");
			}
		}
	}

	private String getSafeFileName(String group) {
		StringBuilder result = new StringBuilder();
		String forbidden = "\\/:*?\"<>|";
		for (char c : group.toCharArray()) {
			if (forbidden.indexOf(c) == -1) {
				result.append(c);
			}
		}
		return result.toString();
	}
}
