package org.exist.eclipse.auto.internal.run;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.exist.eclipse.auto.internal.model.QueryOrderType;
import org.exist.eclipse.auto.internal.model.QueryEntity;
import org.junit.Test;

public class ExecutionPreparerTest {

	@Test
	public void testPrepareSequential() {

		// arrange
		ArrayList<QueryEntity> queryEntities = new ArrayList<QueryEntity>();
		QueryEntity ent1 = new QueryEntity("name1", "note", 10, "query1");
		QueryEntity ent2 = new QueryEntity("name2", "note", 20, "query2");
		QueryEntity ent3 = new QueryEntity("name3", "note", 30, "query3");
		QueryEntity ent4 = new QueryEntity("name4", "note", 40, "query4");
		queryEntities.add(ent1);
		queryEntities.add(ent2);
		queryEntities.add(ent3);
		queryEntities.add(ent4);

		// act
		ExecutionPreparer prep = new ExecutionPreparer(queryEntities,
				QueryOrderType.SEQUENTIAL, "collection");
		ArrayList<Query> queries = prep.getQueriesInConfiguredOrder();

		// assert
		assertEquals(100, queries.size());
		for (int i = 0; i < 100; i++) {
			if (i < 10) {
				assertEquals("name1", queries.get(i).getName());
			} else if (i < 30) {
				assertEquals("name2", queries.get(i).getName());
			} else if (i < 60) {
				assertEquals("name3", queries.get(i).getName());
			} else {
				assertEquals("name4", queries.get(i).getName());
			}
		}

	}

	@Test
	public void testPrepareIterating() {

		// arrange
		ArrayList<QueryEntity> queryEntities = new ArrayList<QueryEntity>();
		QueryEntity ent1 = new QueryEntity("name1", "note", 10, "query1");
		QueryEntity ent2 = new QueryEntity("name2", "note", 20, "query2");
		QueryEntity ent3 = new QueryEntity("name3", "note", 40, "query3");
		QueryEntity ent4 = new QueryEntity("name4", "note", 30, "query4");
		queryEntities.add(ent1);
		queryEntities.add(ent2);
		queryEntities.add(ent3);
		queryEntities.add(ent4);

		// act
		ExecutionPreparer prep = new ExecutionPreparer(queryEntities,
				QueryOrderType.ITERATING, "collection");
		ArrayList<Query> queries = prep.getQueriesInConfiguredOrder();

		// assert
		assertEquals(100, queries.size());
		assertEquals("name1", queries.get(0).getName());
		assertEquals("name2", queries.get(1).getName());
		assertEquals("name3", queries.get(2).getName());
		assertEquals("name4", queries.get(3).getName());
		assertEquals("name4", queries.get(39).getName());
		assertEquals("name2", queries.get(40).getName());
		assertEquals("name3", queries.get(41).getName());
		assertEquals("name4", queries.get(69).getName());
		assertEquals("name3", queries.get(70).getName());
		assertEquals("name4", queries.get(89).getName());
		assertEquals("name3", queries.get(90).getName());
		assertEquals("name3", queries.get(91).getName());
		assertEquals("name3", queries.get(98).getName());
		assertEquals("name3", queries.get(99).getName());

	}

	@Test
	public void testPrepareRandom() {

		// arrange
		ArrayList<QueryEntity> queryEntities = new ArrayList<QueryEntity>();
		QueryEntity ent1 = new QueryEntity("name1", "note", 10, "query1");
		QueryEntity ent2 = new QueryEntity("name2", "note", 20, "query2");
		QueryEntity ent3 = new QueryEntity("name3", "note", 30, "query3");
		QueryEntity ent4 = new QueryEntity("name4", "note", 40, "query4");
		queryEntities.add(ent1);
		queryEntities.add(ent2);
		queryEntities.add(ent3);
		queryEntities.add(ent4);

		// act
		ExecutionPreparer prep = new ExecutionPreparer(queryEntities,
				QueryOrderType.RANDOM, "collection");
		ArrayList<Query> queries1 = prep.getQueriesInConfiguredOrder();
		ArrayList<Query> queries2 = prep.getQueriesInConfiguredOrder();

		// assert
		assertEquals(100, queries1.size());
		int unequal = 0;
		for (int i = 0; i < 100; i++) {
			unequal += queries1.get(i).getName()
					.compareTo(queries2.get(2).getName()) == 0 ? 0 : 1;
		}
		assertTrue(unequal > 50);

	}

}
