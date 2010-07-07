/**
 * 
 */
package org.exist.eclipse.auto.connection;


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
	 * @return the root collection
	 */
	public String getRootCollection();

}
