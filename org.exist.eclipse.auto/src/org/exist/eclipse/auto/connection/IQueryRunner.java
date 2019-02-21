/**
 * 
 */
package org.exist.eclipse.auto.connection;

import org.exist.eclipse.auto.query.IQuery;
import org.exist.eclipse.auto.query.IQueryResult;

/**
 * Via a IQueryRunner a query can be performed. Besides that it allows to close
 * the related connection
 * 
 * @author Markus Tanner
 */
public interface IQueryRunner {

	/**
	 * Runs the Query
	 */
	public IQueryResult runQuery(IQuery query);

	/**
	 * Closes the connection
	 */
	public void close();

}
