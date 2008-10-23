/**
 * 
 */
package org.exist.eclipse.xquery.ui.context;

/**
 * Event, which is used as parameter in {@link IContextListener}.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class ConnectionContextEvent {
	private final IConnectionContext _context;

	public ConnectionContextEvent(IConnectionContext context) {
		_context = context;
	}

	/**
	 * @return the actual {@link IConnectionContext}.
	 */
	public final IConnectionContext getConnectionContext() {
		return _context;
	}
}
