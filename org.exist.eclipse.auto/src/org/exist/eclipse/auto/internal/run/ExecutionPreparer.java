package org.exist.eclipse.auto.internal.run;

import java.util.ArrayList;
import java.util.Collections;

import org.exist.eclipse.auto.internal.model.QueryOrderType;
import org.exist.eclipse.auto.internal.model.QueryEntity;

/**
 * Prepares the Execution of the automation depending on the given
 * 
 * @author Markus Tanner
 */
public class ExecutionPreparer {

	private ArrayList<Query> _queryBase;
	private QueryOrderType _type;

	public ExecutionPreparer(ArrayList<QueryEntity> queryEntities, QueryOrderType type, String collection) {
		_type = type;
		initialize(queryEntities, collection);
	}

	private void initialize(ArrayList<QueryEntity> queryEntities, String collection) {

		_queryBase = new ArrayList<>();

		int queryEntityCount = 0;
		for (QueryEntity queryEntity : queryEntities) {
			if (queryEntity.getQuantity() > 0) {
				_queryBase.add(new Query(queryEntity, collection, ++queryEntityCount));
			}
		}

	}

	public ArrayList<Query> getQueriesInConfiguredOrder() {

		ArrayList<Query> queryList = new ArrayList<>();

		if (_type == QueryOrderType.ITERATING) {
			return prepareIterating(queryList);
		} else if (_type == QueryOrderType.RANDOM) {
			return prepareRandom(queryList);
		} else {
			return prepareSequential(queryList);
		}

	}

	protected ArrayList<Query> prepareSequential(ArrayList<Query> queryList) {
		for (Query queryToRun : _queryBase) {
			for (int i = 0; i < queryToRun.getQuantity(); i++) {
				queryList.add(queryToRun);
			}
		}
		return queryList;
	}

	protected ArrayList<Query> prepareRandom(ArrayList<Query> queryList) {
		queryList = prepareSequential(queryList);
		Collections.shuffle(queryList);
		return queryList;
	}

	protected ArrayList<Query> prepareIterating(ArrayList<Query> queryList) {
		int rotations = 0;
		ArrayList<Query> queriesToIgnore = new ArrayList<>();
		while (_queryBase.size() > 0) {
			++rotations;
			for (Query queryToRun : _queryBase) {
				queryList.add(queryToRun);

				if (queryToRun.getQuantity() <= rotations) {
					queriesToIgnore.add(queryToRun);
				}

			}
			_queryBase.removeAll(queriesToIgnore);
			queriesToIgnore.clear();
		}
		return queryList;
	}

}
