/**
 * RunQueryBrowseListener.java
 */
package org.exist.eclipse.query.internal.xquery.browse;

import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseListener;
import org.exist.eclipse.query.internal.xquery.context.ContextSwitcherContainer;
import org.exist.eclipse.query.internal.xquery.context.ContextSwitcher;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.editor.IXQueryEditor;

/**
 * Set the context in the active xquery editor. If the active editor is not the
 * {@link IXQueryEditor}, then do nothing.
 * 
 * @author Pascal Schmidiger
 */
public class RunQueryBrowseListener implements IBrowseListener {

	@Override
	public void actionPerformed(IBrowseItem[] items) {
		IXQueryEditor editor = XQueryUI.getDefault().getActiveXQueryEditor();
		if (editor != null) {
			IBrowseItem item = items[0];
			ContextSwitcher switcher = ContextSwitcherContainer.getInstance().getContextSwitcher(item.getConnection());
			editor.setConnectionContext(switcher.getConnectionContext(item));
		}
	}

	@Override
	public void init(IWorkbenchPage page) {
	}
}
