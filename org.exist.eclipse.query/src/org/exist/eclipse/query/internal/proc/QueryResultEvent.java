/**
 * QueryResultEvent.java
 */
package org.exist.eclipse.query.internal.proc;

/**
 * This class contains query result informations.
 * 
 * @author Pascal Schmidiger
 * 
 */
public class QueryResultEvent {
	private final String _content;
	private final int _id;

	/**
	 * Create a new result.
	 * 
	 * @param id
	 *            of the query.
	 * @param content
	 *            of the result.
	 */
	public QueryResultEvent(int id, String content) {
		_id = id;
		_content = content;
	}

	public final String getContent() {
		return _content;
	}

	public final int getId() {
		return _id;
	}
}
