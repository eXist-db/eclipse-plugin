/**
 * 
 */
package org.exist.eclipse.xquery.ui.editor;

import org.exist.eclipse.xquery.ui.context.IConnectionContext;

/**
 * Interfaces for the XQuery Editor.
 * 
 * @author Pascal Schmidiger
 */
public interface IXQueryEditor {
	/**
	 * Set the given <code>context</code> on this editor.
	 * 
	 * @param context
	 *            in which the xquery will run.
	 */
	public void setConnectionContext(IConnectionContext context);

	/**
	 * @return the actual {@link IConnectionContext} if one is set, elsewhere
	 *         <code>null</code>.
	 */
	public IConnectionContext getConnectionContext();
}
