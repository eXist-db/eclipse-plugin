/**
 * 
 */
package org.exist.eclipse.query.internal.xquery.context;

import org.exist.eclipse.IConnection;
import org.exist.eclipse.listener.IConnectionListener;
import org.exist.eclipse.xquery.ui.context.ContextSwitcherRegistration;
import org.exist.eclipse.xquery.ui.context.IContextSwitcher;

/**
 * This class implement the {@link IConnectionListener} and registered on open
 * event an {@link IContextSwitcher} in the {@link ContextSwitcherRegistration}.
 * Otherwise on close event it will deregister it.
 * 
 * @author Pascal Schmidiger
 */
public class ContextSwitcherListener implements IConnectionListener {

	@Override
	public void added(IConnection connection) {
		ContextSwitcher contextSwitcher = new ContextSwitcher(connection);
		ContextSwitcherRegistration.getInstance().addContextSwitcher(
				contextSwitcher);
		ContextSwitcherContainer.getInstance().put(connection, contextSwitcher);
	}

	@Override
	public void closed(IConnection connection) {
	}

	@Override
	public void opened(IConnection connection) {
	}

	@Override
	public void removed(IConnection connection) {
		ContextSwitcher contextSwitcher = ContextSwitcherContainer
				.getInstance().getContextSwitcher(connection);
		if (contextSwitcher != null) {
			ContextSwitcherRegistration.getInstance().removeContextSwitcher(
					contextSwitcher);
			ContextSwitcherContainer.getInstance().remove(connection);
		}
	}

}
