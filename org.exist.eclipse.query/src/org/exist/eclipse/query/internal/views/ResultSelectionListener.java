/**
 * ResultSelectionListener.java
 */
package org.exist.eclipse.query.internal.views;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.exist.eclipse.query.internal.edit.QueryResultStorage;
import org.exist.eclipse.query.internal.edit.QueryResultStorageEditorInput;

/**
 * Open a result in a editor.
 * 
 * @author Markus Tanner
 * 
 */
public class ResultSelectionListener extends MouseAdapter {

	private final IWorkbenchWindow _workbenchWindow;
	private int _id;

	public ResultSelectionListener(IWorkbenchWindow workbenchWindow) {
		_workbenchWindow = workbenchWindow;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		Table source = (Table) e.getSource();
		TableItem[] item = source.getSelection();

		// get the text from the column 1 (column 0 -> enumeration)
		TableItem tableItem = item[0];
		String id = _id + "_" + tableItem.getText(0);
		String resultItem = tableItem.getText(1);

		IStorageEditorInput input = new QueryResultStorageEditorInput(
				new QueryResultStorage(id, resultItem));

		IEditorRegistry registry = PlatformUI.getWorkbench()
				.getEditorRegistry();
		// for the query result there's no file available
		IEditorDescriptor defaultEditor = registry.getDefaultEditor("");

		if (defaultEditor == null) {
			defaultEditor = registry
					.findEditor("org.eclipse.ui.DefaultTextEditor");
		}

		if (defaultEditor != null) {
			try {
				_workbenchWindow.getActivePage().openEditor(input,
						defaultEditor.getId().toString());
			} catch (PartInitException ex) {
				// ignore
				ex.printStackTrace();
			}
		}
	}

	protected final void setId(int id) {
		_id = id;
	}

}
