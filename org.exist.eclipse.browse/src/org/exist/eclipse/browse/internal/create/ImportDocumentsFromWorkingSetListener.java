/**
 * CreateBrowseListener.java
 */

package org.exist.eclipse.browse.internal.create;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetSelectionDialog;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.browse.internal.BrowsePlugin;

/**
 * Imports files contained in {@link IWorkingSet} into new XML documents.
 * 
 * @author Oetterli Christian
 */
public class ImportDocumentsFromWorkingSetListener implements IBrowseListener {

	public static List<String> _lastSelectedWorkingSets;

	protected static List<String> getLastSelectedWorkingSets() {
		if (_lastSelectedWorkingSets == null) {
			_lastSelectedWorkingSets = new ArrayList<String>();
		}
		return _lastSelectedWorkingSets;
	}

	public static IWorkingSetManager getWorkingSetManager() {
		return PlatformUI.getWorkbench().getWorkingSetManager();
	}

	public void actionPerformed(IBrowseItem[] items) {
		IBrowseItem browseItem = items[0];
		IBrowseService service = (IBrowseService) browseItem
				.getAdapter(IBrowseService.class);
		if (service.check()) {

			IWorkingSetManager workingSetManager = getWorkingSetManager();
			IWorkingSetSelectionDialog dialog = workingSetManager
					.createWorkingSetSelectionDialog(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(), true);

			List<String> lastSelectedWorkingSets = getLastSelectedWorkingSets();
			if (!lastSelectedWorkingSets.isEmpty()) {
				List<IWorkingSet> sel = new ArrayList<IWorkingSet>();
				for (String it : lastSelectedWorkingSets) {
					IWorkingSet set = workingSetManager.getWorkingSet(it);
					if (set != null) {
						sel.add(set);
					}
				}
				dialog.setSelection(sel.toArray(new IWorkingSet[sel.size()]));
			}
			dialog.open();
			IWorkingSet[] newSel = dialog.getSelection();
			if (newSel != null) {
				lastSelectedWorkingSets.clear();
				for (IWorkingSet it : newSel) {
					lastSelectedWorkingSets.add(it.getName());
				}
				importFiles(browseItem, newSel);
			}
		}
	}

	public void importFiles(IBrowseItem browseItem, IWorkingSet[] sets) {

		if (sets.length == 0) {
			return;
		}

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		List<IFile> files = collectFiles(sets);
		if (files.isEmpty()) {
			MessageDialog.openInformation(shell, "Import from WorkingSet",
					"No files found in the selected working sets.");
		} else {
			IConfigurationElement xmlCfg = ImportDocumentsListener
					.getXMLConfigurationElement();
			boolean hadErrors = false;
			for (IFile file : files) {
				try {
					ImportDocumentsListener.saveDoc(browseItem, xmlCfg, file
							.getName(), file.getContents());
				} catch (Exception e) {
					hadErrors = true;
					BrowsePlugin.getDefault().getLog().log(
							new Status(IStatus.ERROR, BrowsePlugin.getId(),
									"An error occured while importing '" + file
											+ "': " + e, e));
				}
			}

			if (hadErrors) {
				MessageDialog
						.openError(shell, "Import Documents",
								"Errors occured while importing. See the Eclipse Error Log for details.");
			}
		}
	}

	protected List<IFile> collectFiles(IWorkingSet[] sets) {
		try {
			List<IFile> result = new ArrayList<IFile>();
			for (IWorkingSet set : sets) {
				for (IAdaptable elem : set.getElements()) {
					IFile file = (IFile) elem.getAdapter(IFile.class);
					if (file != null) {
						result.add(file);
					} else {
						IFolder folder = (IFolder) elem
								.getAdapter(IFolder.class);
						if (folder != null) {
							for (IResource m : folder.members()) {
								file = (IFile) m.getAdapter(IFile.class);
								if (file != null) {
									result.add(file);
								}
							}
						}
					}
				}
			}
			return result;
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public void init(IWorkbenchPage page) {
	}

}
