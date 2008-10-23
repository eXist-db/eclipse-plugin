/**
 * IQueryProcessor.java
 */
package org.exist.eclipse.query.internal.proc;

import org.exist.eclipse.exception.ConnectionException;

/**
 * Interface for running a query.
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IQueryProcessor {
	/**
	 * Run a query.
	 * 
	 * @throws ConnectionException
	 */
	public void runQuery() throws ConnectionException;
}
