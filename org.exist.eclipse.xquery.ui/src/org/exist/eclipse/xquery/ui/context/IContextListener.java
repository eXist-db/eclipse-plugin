/**
 * 
 */
package org.exist.eclipse.xquery.ui.context;

/**
 * Listener, which inform you about changes on the {@link IConnectionContext}.
 * 
 * @see IConnectionContext
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IContextListener {
	/**
	 * Call if you have to refresh the context. For e.g. the context was renamed.
	 * 
	 * @param event
	 */
	public void refresh(ConnectionContextEvent event);

	/**
	 * Call if the context is no longer valid. For e.g. the cotext was removed.
	 * 
	 * @param event
	 */
	public void disposed(ConnectionContextEvent event);
}
