/**
 * 
 */
package org.exist.eclipse.auto.internal.result.model;

import java.util.ArrayList;

import org.exist.eclipse.auto.query.State;

/**
 * QueryResultEntity represents the result of all the runs of a specified query.
 * 
 * @author Markus Tanner
 */
public class QueryResultEntity {

	private String _name;
	private String _notes;
	private int _quantity;
	private String _query;
	private int _avgCompTime;
	private int _avgExecTime;
	private ArrayList<RunEntity> _runs;
	private IResultModel _model;

	/**
	 * QueryResultEntity Constructor
	 * 
	 * @param model
	 *            the result model
	 */
	public QueryResultEntity(IResultModel model) {
		_model = model;
		_runs = new ArrayList<>();
		_model.addQueryResultEntity(this);
	}

	/**
	 * Gets the name of the query.
	 * 
	 * @return name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @return the query notes
	 */
	public String getNotes() {
		return _notes;
	}

	/**
	 * Gets the quantity.
	 * 
	 * @return quantity
	 */
	public int getQuantity() {
		return _quantity;
	}

	/**
	 * Gets the query.
	 * 
	 * @return query
	 */
	public String getQuery() {
		return _query;
	}

	/**
	 * Gets the average execution time.
	 * 
	 * @return execution time
	 */
	public int getAvgExecutionTime() {
		return _avgExecTime;
	}

	/**
	 * Gets the average compilation time
	 * 
	 * @return compilation time
	 */
	public int getAvgCompTime() {
		return _avgCompTime;
	}

	/**
	 * Gets the run entities.
	 * 
	 * @return run entities
	 */
	public ArrayList<RunEntity> getRuns() {
		return _runs;
	}

	public void add(RunEntity runEntity) {
		_runs.add(runEntity);
	}

	/**
	 * Sets the auto model.
	 * 
	 * @param model
	 */
	public void setModel(IResultModel model) {
		_model = model;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setNotes(String notes) {
		_notes = notes;
	}

	public void setQuantity(int quantity) {
		_quantity = quantity;
	}

	public void setQuery(String query) {
		_query = query;
	}

	public int getAvgExecTime() {
		return _avgExecTime;
	}

	public void setAvgExecTime(int avgExecTime) {
		_avgExecTime = avgExecTime;
	}

	public void setAvgCompTime(int avgCompTime) {
		_avgCompTime = avgCompTime;
	}

	public boolean isSuccessful() {
		boolean successful = true;
		for (RunEntity run : _runs) {
			if (!run.getState().equals(State.SUCCESS)) {
				successful = false;
				break;
			}
		}
		return successful;
	}

	public long getResultCount() {
		long count = 0;
		for (RunEntity run : _runs) {
			count += run.getResultCount();
		}
		return count;
	}
}
