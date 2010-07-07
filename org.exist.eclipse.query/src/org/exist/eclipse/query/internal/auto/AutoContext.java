/**
 * 
 */
package org.exist.eclipse.query.internal.auto;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.auto.connection.IAutoContext;
import org.exist.eclipse.auto.connection.IQueryRunner;
import org.exist.eclipse.browse.browse.BrowseHelper;
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

	public String getRootCollection(){
		return BrowseHelper.getRootBrowseItem(_connection).getPath();
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
