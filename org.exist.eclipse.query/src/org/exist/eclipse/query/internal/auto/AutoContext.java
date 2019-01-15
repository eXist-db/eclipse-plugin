/**
 * 
 */
package org.exist.eclipse.query.internal.auto;

import java.util.Objects;

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
 * create {@link IQueryRunner} instances.
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

	@Override
	public IQueryRunner createQueryRunner() {
		try {
			return new QueryRunner(_connection.duplicate());
		} catch (ConnectionException e) {
			QueryPlugin
					.getDefault()
					.getLog()
					.log(new Status(IStatus.ERROR, QueryPlugin.getId(),
							"Failure while duplicating the connection.", e));
			return null;
		}
	}

	@Override
	public String getName() {
		return _connection.getName();
	}

	@Override
	public String getRootCollection() {
		return BrowseHelper.getRootBrowseItem(_connection).getPath();
	}

	@Override
	public int hashCode() {
		return ((_connection == null) ? 0 : _connection.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof AutoContext)) {
			return false;
		}
		AutoContext other = (AutoContext) obj;
		return Objects.equals(_connection, other._connection);
	}

}
