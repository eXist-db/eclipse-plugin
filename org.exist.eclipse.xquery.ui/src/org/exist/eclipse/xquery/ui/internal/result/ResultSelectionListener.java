/**
 * ResultSelectionListener.java
 */
package org.exist.eclipse.xquery.ui.internal.result;

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
import org.eclipse.ui.editors.text.TextEditor;
import org.exist.eclipse.xquery.ui.XQueryUI;

/**
 * Listen on mouse double click on the table and open the {@link ResultItem} in
 * a {@link TextEditor}.
 * 
 * @author Pascal Schmidiger
 */
public class ResultSelectionListener extends MouseAdapter {

	public ResultSelectionListener() {
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		Table source = (Table) e.getSource();
		TableItem[] item = source.getSelection();

		if (item.length > 0) {
			ResultItem resultItem = (ResultItem) item[0].getData();
			IStorageEditorInput input = new QueryResultStorageEditorInput(
					new QueryResultStorage(resultItem));

			openEditor(input);
		}
	}

	private void openEditor(IStorageEditorInput input) {
		IEditorRegistry registry = PlatformUI.getWorkbench()
				.getEditorRegistry();

		// take the default text editor
		IEditorDescriptor defaultEditor = registry
				.findEditor("org.eclipse.ui.DefaultTextEditor");

		if (defaultEditor != null) {
			try {
				IWorkbenchWindow workbenchWindow = XQueryUI.getDefault()
						.getWorkbench().getActiveWorkbenchWindow();
				workbenchWindow.getActivePage().openEditor(input,
						defaultEditor.getId().toString());
			} catch (PartInitException ex) {
				ex.printStackTrace();
			}
		}
	}

}
