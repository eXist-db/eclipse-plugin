package org.exist.eclipse.query.internal.proc;

/**
 * This class contains query end result informations.
 * 
 * @author Markus Tanner
 */
public class QueryEndEvent {

	private long _count;
	private long _executionTime;
	private final Exception _exception;
	private final int _id;
	private final long _compiledTime;

	/**
	 * Create a new event, if the query was successful
	 * 
	 * @param id
	 *            of the query.
	 * @param count
	 *            of founded results
	 * @param compiled
	 *            of the query.
	 * @param duration
	 *            of the query.
	 */
	public QueryEndEvent(int id, long count, long compiledTime,
			long executionTime) {
		this(id, count, compiledTime, executionTime, null);
	}

	/**
	 * Create a new event, if the query was performed with failures.
	 * 
	 * @param id
	 *            of the query.
	 * @param compiled
	 *            of the query
	 * @param exception
	 *            which throws while query running.
	 */
	public QueryEndEvent(int id, long compiledTime, Exception exception) {
		this(id, 0, compiledTime, 0, exception);
	}

	private QueryEndEvent(int id, long count, long compiledTime,
			long executionTime, Exception exception) {
		_id = id;
		_count = count;
		_compiledTime = compiledTime;
		_executionTime = executionTime;
		_exception = exception;
	}

	public long getExectionTime() {
		return _executionTime;
	}

	public long getCount() {
		return _count;
	}

	public final Exception getException() {
		return _exception;
	}

	public final int getId() {
		return _id;
	}

	public final long getCompiledTime() {
		return _compiledTime;
	}

	public boolean wasSuccessful() {
		return _exception == null;
	}

}
