/**
 * 
 */
package org.exist.eclipse.query.internal.auto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.auto.connection.IAutoContext;
import org.exist.eclipse.auto.connection.IQueryRunner;
import org.exist.eclipse.browse.browse.BrowseHelper;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.browse.browse.IBrowseService;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.query.internal.QueryPlugin;

/**
 * The AutoContext represents a connection to the database. Besides that it can
 * create {@link Queryrunner} instances
 * 
 * @author Markus Tanner
 */
public class AutoContext implements IAutoContext {

	private IConnection _connection;

	/**
	 * Creates an AutoContext
	 * 
	 * @param connection
	 */
	public AutoContext(IConnection connection) {
		_connection = connection;
	}

	public IQueryRunner createQueryRunner() {
		try {
			return new QueryRunner(_connection.duplicate());
		} catch (ConnectionException e) {
			QueryPlugin.getDefault().getLog().log(
					new Status(IStatus.ERROR, QueryPlugin.getId(),
							"Failure while duplicating the connection.", e));
			return null;
		}
	}

	public String getName() {
		return _connection.getName();
	}

	public Collection<String> getCollections() {
		Collection<String> collections = new ArrayList<String>();

		try {
			IBrowseItem rootBrowseItem = BrowseHelper
					.getRootBrowseItem(_connection);
			IBrowseService service = (IBrowseService) rootBrowseItem
					.getAdapter(IBrowseService.class);
			Set<IBrowseItem> children = service.getChildren(true, true);
			for (IBrowseItem item : children) {
				collections.add(item.getPath());
			}
		} catch (ConnectionException e) {
			QueryPlugin.getDefault().getLog().log(
					new Status(IStatus.ERROR, QueryPlugin.getId(),
							"Failure while fill items.", e));
		}

		return collections;
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
		AutoContext other = (AutoContext) obj;
		if (_connection == null) {
			if (other._connection != null)
				return false;
		} else if (!_connection.equals(other._connection))
			return false;
		return true;
	}

}
