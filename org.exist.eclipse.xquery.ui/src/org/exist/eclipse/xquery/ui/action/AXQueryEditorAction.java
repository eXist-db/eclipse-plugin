package org.exist.eclipse.xquery.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.exist.eclipse.xquery.ui.internal.editor.XQueryEditor;

abstract public class AXQueryEditorAction extends Action {

	@Override
	public void run() {
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof XQueryEditor) {
			XQueryEditor xQueryEditor = (XQueryEditor) editor;
			doRun(xQueryEditor);
		}
	}

	protected abstract void doRun(XQueryEditor editor);

	protected IDocument getDocument(XQueryEditor xQueryEditor) {
		IDocument document = xQueryEditor.getDocumentProvider().getDocument(
				xQueryEditor.getEditorInput());
		return document;
	}

	protected void selectAndReveal(XQueryEditor editor, int pos, int len) {
		editor.getViewer().setSelectedRange(pos, len);
		editor.getViewer().revealRange(pos, len);
	}

}
