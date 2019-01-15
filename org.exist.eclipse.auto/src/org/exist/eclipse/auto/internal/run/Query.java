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

	@Override
	public int getId() {
		return _id;
	}

	@Override
	public String getName() {
		return _queryEntity.getName();
	}

	@Override
	public String getNotes() {
		return _queryEntity.getNotes();
	}

	@Override
	public String getQuery() {
		return _queryEntity.getQuery();
	}

	@Override
	public int getQuantity() {
		return _queryEntity.getQuantity();
	}

	@Override
	public String getCollection() {
		return _collection;
	}

}
