/**
 * 
 */
package org.exist.eclipse.auto.query;

/**
 * This interface represents a query that holds all the information needed to be
 * executed.
 * 
 * @author Markus Tanner
 */
public interface IQuery {

	/**
	 * Returns the name of the query
	 * 
	 * @return name
	 */
	public String getName();

	/**
	 * Returns the notes for a given query
	 * 
	 * @return notes
	 */
	public String getNotes();

	/**
	 * Returns the quantity for the given query
	 * 
	 * @return quantity
	 */
	public int getQuantity();

	/**
	 * Returns the query as such
	 * 
	 * @return query
	 */
	public String getQuery();

	/**
	 * Returns the id in order to run the query
	 * 
	 * @return id
	 */
	public int getId();

	/**
	 * Returns the collection on which the query needs to be performed
	 * 
	 * @return collection
	 */
	public String getCollection();
}
