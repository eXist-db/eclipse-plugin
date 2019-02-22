/**
 * 
 */
package org.exist.eclipse.query.internal.auto;

import java.util.concurrent.TimeUnit;

import javax.xml.transform.OutputKeys;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.auto.connection.IQueryRunner;
import org.exist.eclipse.auto.query.IQuery;
import org.exist.eclipse.auto.query.IQueryResult;
import org.exist.eclipse.auto.query.State;
import org.exist.eclipse.browse.browse.BrowseHelper;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.exception.ConnectionException;
import org.exist.eclipse.query.internal.QueryPlugin;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XQueryService;

/**
 * QueryRunner represents a context over which a query can be run. Besides that
 * it's able to close connections.
 * 
 * @author Markus Tanner
 */
public class QueryRunner implements IQueryRunner {
	private IConnection _connection;

	public QueryRunner(IConnection connection) {
		_connection = connection;
	}

	@Override
	public void close() {
		try {
			_connection.close();
		} catch (ConnectionException e) {
			// ignore
			e.printStackTrace();
		}
	}

	@Override
	public IQueryResult runQuery(IQuery query) {
		QueryResult	result = new QueryResult(query);
		try {
			IBrowseItem item = BrowseHelper.getBrowseItem(_connection, query.getCollection());
			long tCompiled = 0;
			XQueryService service = (XQueryService) item.getCollection().getService("XQueryService", "1.0");
			service.setProperty(OutputKeys.INDENT, "yes");
			long t0 = System.nanoTime();
			CompiledExpression compiled = service.compile(query.getQuery());
			long t1 = System.nanoTime();
			tCompiled = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
			ResourceSet resourceSet = service.execute(compiled);
			long tResult = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t1);
			result.setQueryState(State.SUCCESS);
			result.setCompileTime(tCompiled);
			result.setExecutionTime(tResult);
			result.setResultCount(resourceSet.getSize());
		} catch (Exception e) {
			String message = "Error while run query";
			Status status = new Status(IStatus.ERROR, QueryPlugin.getId(), message, e);
			QueryPlugin.getDefault().getLog().log(status);
			result.setQueryState(State.FAILURE);
		}
		return result;
	}
}
