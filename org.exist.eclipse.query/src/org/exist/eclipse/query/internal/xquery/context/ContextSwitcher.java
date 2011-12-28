/**
 * 
 */
package org.exist.eclipse.query.internal.xquery.context;

import org.eclipse.swt.widgets.Display;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.IManagementService;
import org.exist.eclipse.browse.browse.BrowseHelper;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.query.internal.xquery.completion.GetFunctionJob;
import org.exist.eclipse.xquery.ui.context.IConnectionContext;
import org.exist.eclipse.xquery.ui.context.IContextSwitcher;

/**
 * This class represents the possible {@link IContextSwitcher} for eXist.
 * 
 * @author Pascal Schmidiger
 */
public class ContextSwitcher implements IContextSwitcher {

	private final IConnection _connection;
	private GetFunctionJob _functionJob;

	public ContextSwitcher(IConnection connection) {
		_connection = connection;
	}

	public String getName() {
		return _connection.getName();
	}

	private void openConnection() throws ConnectionException {
		if (!_connection.isOpen()) {
			_connection.open();
		}
	}

	public IConnectionContext getDefault() {
		openConnection();
		return getConnectionContext(BrowseHelper.getRootBrowseItem(_connection));
	}

	public IConnectionContext getConnectionContext(final IBrowseItem item) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (item != null) {
					_functionJob = new GetFunctionJob(BrowseHelper
							.getRootBrowseItem(_connection));
					_functionJob.schedule();
					IManagementService.class.cast(
							item.getConnection().getAdapter(
									IManagementService.class)).check();
					IBrowseService.class.cast(
							item.getAdapter(IBrowseService.class)).check();
				}
			}
		});
		return new ConnectionContext(item, _functionJob);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_connection == null) ? 0 : _connection.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContextSwitcher other = (ContextSwitcher) obj;
		if (_connection == null) {
			if (other._connection != null)
				return false;
		} else if (!_connection.equals(other._connection))
			return false;
		return true;
	}
}
