/**
 * ResultSelectionListener.java
 */

package org.exist.eclipse.xquery.ui.internal.result;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
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
public class ResultSelectionListener implements IDoubleClickListener {

	public ResultSelectionListener() {
	}

	public void openEditor(IStructuredSelection selection) {
		for (Object it : selection.toArray()) {
			IStorageEditorInput input = new QueryResultStorageEditorInput(new QueryResultStorage((ResultItem) it));
			openEditor(input);
		}
	}

	private void openEditor(IStorageEditorInput input) {
		IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();

		// take the default text editor
		IEditorDescriptor defaultEditor;
		IEditorDescriptor[] editors = registry.getEditors("dummy.xml");
		if (editors.length > 0) {
			defaultEditor = editors[0];
		} else {
			defaultEditor = registry.findEditor("org.eclipse.ui.DefaultTextEditor");
		}

		if (defaultEditor != null) {
			try {
				IWorkbenchWindow workbenchWindow = XQueryUI.getDefault().getWorkbench().getActiveWorkbenchWindow();
				workbenchWindow.getActivePage().openEditor(input, defaultEditor.getId().toString());
			} catch (PartInitException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		openEditor((IStructuredSelection) event.getSelection());
	}

}
