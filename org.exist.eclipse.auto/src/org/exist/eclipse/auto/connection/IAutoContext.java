/**
 * 
 */
package org.exist.eclipse.auto.connection;

import java.util.Collection;

/**
 * The IAutoContext represents a connection to the db.
 * 
 * @author Markus Tanner
 */
public interface IAutoContext {

	/**
	 * Gets the name of the context
	 * 
	 * @return context name
	 */
	public String getName();

	/**
	 * Creates a query runner instance
	 * 
	 * @ query runner
	 */
	public IQueryRunner createQueryRunner();

	/**
	 * Gets all the collections for a specific auto context
	 * 
	 * @return Collections
	 */
	public Collection<String> getCollections();

}
