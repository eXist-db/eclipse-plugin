/**
 * 
 */
package org.exist.eclipse.auto.internal.run;

import org.exist.eclipse.auto.internal.model.QueryEntity;
import org.exist.eclipse.auto.query.IQuery;

/**
 * The query that will be executed via the query runner
 * 
 * @author Markus Tanner
 */
public class Query implements IQuery {

	QueryEntity _queryEntity;
	String _collection;
	int _id;

	/**
	 * Constructor of Query
	 * 
	 * @param queryEntity
	 * @param collection
	 * @param id
	 */
	public Query(QueryEntity queryEntity, String collection, int id) {
		_queryEntity = queryEntity;
		_collection = collection;
		_id = id;

	}

	public int getId() {
		return _id;
	}

	public String getName() {
		return _queryEntity.getName();
	}

	public String getNotes() {
		return _queryEntity.getNotes();
	}

	public String getQuery() {
		return _queryEntity.getQuery();
	}

	public int getQuantity() {
		return _queryEntity.getQuantity();
	}

	public String getCollection() {
		return _collection;
	}

}
