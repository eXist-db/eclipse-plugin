/**
 * QueryStartEvent.java
 */
package org.exist.eclipse.query.internal.proc;

/**
 * This class contains query start result informations.
 * 
 * @author Pascal Schmidiger
 */
public class QueryStartEvent {
	private final int _id;

	/**
	 * Create a new start event.
	 * 
	 * @param id
	 *            of the query.
	 */
	public QueryStartEvent(int id) {
		_id = id;
	}

	public final int getId() {
		return _id;
	}
}
