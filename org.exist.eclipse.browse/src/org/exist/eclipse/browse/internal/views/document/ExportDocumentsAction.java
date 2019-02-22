package org.exist.eclipse.browse.internal.views.document;

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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.internal.BrowsePlugin;

public class ExportDocumentsAction extends Action {

	public static final String DEFAULT_ENCODING = "UTF-8";

	private final Viewer _viewer;

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

	public ExportDocumentsAction(Viewer viewer) {
		super("Export...", BrowsePlugin.getImageDescriptor("icons/export.png"));
		_viewer = viewer;
	}

	@Override
	public void run() {
		Object[] sel = ((IStructuredSelection) _viewer.getSelection()).toArray();
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		Map<IDocumentItem, File> files = new HashMap<>();

		if (sel.length == 1) {
			IDocumentItem document = (IDocumentItem) sel[0];
			FileDialog dialog = new FileDialog(shell, SWT.SAVE);
			String[] filterExtensions = { "*.*", "*.xml" };
			String[] filterNames = { "All Files (*.*)", "XML Files (*.xml)" };
			dialog.setFilterExtensions(filterExtensions);
			dialog.setFilterNames(filterNames);
			dialog.setFileName(document.getName());
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
					IDocumentItem document = (IDocumentItem) it;
					files.put(document, new File(folderr, document.getName()));
				}
			}
		}

		if (!files.isEmpty()) {
			exportDocuments(files);
		}
	}

	public void exportDocuments(Map<IDocumentItem, File> files) {
		boolean hadErrors = false;
		for (Map.Entry<IDocumentItem, File> all : files.entrySet()) {
			IDocumentItem document = all.getKey();
			File file = all.getValue();
			try {
				String content = (String) document.getResource().getContent();
				try (Reader reader = new StringReader(content);
						OutputStream out = new FileOutputStream(file);
						Writer writer = new OutputStreamWriter(out, DEFAULT_ENCODING)) {
					copy(reader, writer);
				}
			} catch (Exception e) {
				hadErrors = true;
				BrowsePlugin.getDefault().getLog().log(new Status(IStatus.ERROR, BrowsePlugin.getId(),
						"An error occured while exporting the document '" + document.getName() + "': " + e));
			}
		}

		if (hadErrors) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Export Result",
					"Errors occured while exporting documents. See Eclipse Error Log for details.");
		}
	}
}
