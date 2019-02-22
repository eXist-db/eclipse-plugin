/**
 * CreateBrowseListener.java
 */

package org.exist.eclipse.browse.internal.create;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.document.IDocumentItem;
import org.exist.eclipse.browse.document.IDocumentService;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.browse.internal.views.document.ExportDocumentsAction;

/**
 * Imports external files into new XML documents.
 * 
 * @author Oetterli Christian
 */
public class ImportDocumentsListener implements IBrowseListener {

	private static final String XML_RESOURCE_TYPE = "XML Resource";

	static void saveDoc(IBrowseItem item, IConfigurationElement xmlCfg, String name, InputStream content) {
		if (item.getConnection().getAdapter(IManagementService.class).check()) {
			IBrowseService itemService = item.getAdapter(IBrowseService.class);
			if (itemService.check()) {
				IDocumentItem documentItem = item.getDocument(name);
				try {
					IDocumentService documentService = documentItem.getAdapter(IDocumentService.class);
					StringWriter tmp = new StringWriter();
					ExportDocumentsAction.copy(new InputStreamReader(content, ExportDocumentsAction.DEFAULT_ENCODING),
							tmp);
					documentService.create(xmlCfg, tmp.toString());
					itemService.refresh();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	protected static IConfigurationElement getXMLConfigurationElement() {
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint exPoint = reg.getExtensionPoint(BrowsePlugin.getId(), "createdocument");
		IExtension[] documents = exPoint.getExtensions();
		IConfigurationElement xmlCfg = null;
		for (IExtension extension : documents) {
			IConfigurationElement[] configurations = extension.getConfigurationElements();
			for (IConfigurationElement element : configurations) {
				String name = element.getAttribute("name");
				if (name.equals(XML_RESOURCE_TYPE)) {
					xmlCfg = element;
					break;
				}
			}
		}

		if (xmlCfg == null) {
			throw new RuntimeException(
					"Missing 'createdocument' extension for resource type '" + XML_RESOURCE_TYPE + "'.");
		}
		return xmlCfg;
	}

	@Override
	public void actionPerformed(IBrowseItem[] items) {
		IBrowseItem browseItem = items[0];
		IBrowseService service = browseItem.getAdapter(IBrowseService.class);
		if (service.check()) {

			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
			dialog.setText("Select the file(s) to import");
			String[] filterExtensions = { "*.*", "*.xml" };
			String[] filterNames = { "All Files (*.*)", "XML Files (*.xml)" };
			dialog.setFilterExtensions(filterExtensions);
			dialog.setFilterNames(filterNames);
			String aFile = dialog.open();
			if (aFile != null) {
				File folder = new File(aFile).getParentFile();
				List<File> allFiles = new ArrayList<>();
				for (String file : dialog.getFileNames()) {
					allFiles.add(new File(folder, file));
				}

				importFiles(browseItem, allFiles);
			}
		}
	}

	public void importFiles(IBrowseItem browseItem, List<File> allFiles) {
		boolean hadErrors = false;
		IConfigurationElement xmlCfg = getXMLConfigurationElement();
		for (File theFile : allFiles) {
			try {
				saveDoc(browseItem, xmlCfg, theFile.getName(), new FileInputStream(theFile));
			} catch (Exception e) {
				hadErrors = true;
				BrowsePlugin.getDefault().getLog().log(new Status(IStatus.ERROR, BrowsePlugin.getId(),
						"An error occured while importing '" + theFile + "': " + e, e));
			}
		}

		if (hadErrors) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Import Documents",
					"Errors occured while importing. See the Eclipse Error Log for details.");
		}
	}

	@Override
	public void init(IWorkbenchPage page) {
	}

}
