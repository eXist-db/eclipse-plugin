/**
 * 
 */
package org.exist.eclipse.query.internal.auto;

import javax.xml.transform.OutputKeys;

import org.exist.eclipse.IConnection;
import org.exist.eclipse.auto.connection.IQueryRunner;
import org.exist.eclipse.auto.query.IQueryResult;
import org.exist.eclipse.auto.query.State;
import org.exist.eclipse.browse.browse.BrowseHelper;
import org.exist.eclipse.browse.browse.IBrowseItem;
import org.exist.eclipse.exception.ConnectionException;
import org.xmldb.api.base.CompiledExpression;
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

	public void close() {
		try {
			_connection.close();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	public IQueryResult runQuery(IQueryResult result) {

		XQueryService service;
		try {
			IBrowseItem item = BrowseHelper.getBrowseItem(_connection, result
					.getQuery().getCollection());

			long tCompiled = 0;
			service = (XQueryService) item.getCollection().getService(
					"XQueryService", "1.0");
			service.setProperty(OutputKeys.INDENT, "yes");

			long t0 = System.currentTimeMillis();
			CompiledExpression compiled = service.compile(result.getQuery()
					.getQuery());
			long t1 = System.currentTimeMillis();
			tCompiled = t1 - t0;
			service.execute(compiled);
			long tResult = System.currentTimeMillis() - t1;
			result.setQueryState(State.SUCCESS);
			result.setCompileTime(tCompiled);
			result.setExecutionTime(tResult);

		} catch (Exception e) {
			result.setQueryState(State.FAILURE);
		}
		return result;
	}
}
