package org.exist.eclipse.xquery.ui.action;

import org.exist.eclipse.xquery.ui.internal.editor.XQueryEditor;

/**
 * Runs the currently active {@link XQueryEditor}, does nothing if another
 * editor is active.
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class RunXQueryAction extends AXQueryEditorAction {
	public static final String ID = RunXQueryAction.class.getName();

	public RunXQueryAction() {
		setActionDefinitionId(ID);
	}

	@Override
	protected void doRun(XQueryEditor editor) {
		editor.runQuery();
	}
}
