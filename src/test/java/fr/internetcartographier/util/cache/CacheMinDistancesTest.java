package fr.internetcartographier.util.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.util.path.Path;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CacheMinDistancesTest {
	private CacheMinimumDistances cacheMinDistances;

	@BeforeEach
	void setUp() {
		cacheMinDistances = new CacheMinimumDistances();
	}

	public CacheMinimumDistances getCacheMinDistances() {
		return cacheMinDistances;
	}

	@Test
	void resetCache() {
		Path<String> pathABConstant = new Path<>(Arrays.asList("A", "B"), 1);
		Path<String> pathABResponseTime = new Path<>(Arrays.asList("A", "B"), 10);

		cacheMinDistances.addNode("A");
		cacheMinDistances.addNode("B");
		cacheMinDistances.updateCache(WeightMetric.CONSTANT, "Service", pathABConstant);
		cacheMinDistances.updateCache(WeightMetric.RESPONSE_TIME, "Service", pathABResponseTime);

		assertEquals(pathABConstant, cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "A", "B").get());
		assertEquals(new Path<>(Arrays.asList("B", "A"), 1),
				cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "B", "A").get());

		cacheMinDistances.resetCache();
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "A", "B").isEmpty());
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "B", "A").isEmpty());
	}

	@Test
	void addNode() {
		assertThrows(IllegalArgumentException.class,
				() -> cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "B"));
		cacheMinDistances.addNode("A");
		assertThrows(IllegalArgumentException.class,
				() -> cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "B"));
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "A").get()
				.equals(new Path<>(Arrays.asList("A"), 0)));
		cacheMinDistances.addNode("B");
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "B").isEmpty());
		cacheMinDistances.updateCache(WeightMetric.RESPONSE_TIME, "Service", new Path<>(Arrays.asList("A", "B"), 5));
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "B").get()
				.equals(new Path<>(Arrays.asList("A", "B"), 5)));
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "B", "A").get()
				.equals(new Path<>(Arrays.asList("B", "A"), 5)));
		cacheMinDistances.addNode("C");
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "B").get()
				.equals(new Path<>(Arrays.asList("A", "B"), 5)));
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "B", "A").get()
				.equals(new Path<>(Arrays.asList("B", "A"), 5)));
		cacheMinDistances.addNode("C");
		cacheMinDistances.addNode("D");
		cacheMinDistances.addNode("D");
		cacheMinDistances.updateCache(WeightMetric.CONSTANT, "Service2", new Path<>(Arrays.asList("A", "D"), 10));
	}

	@Test
	void removeNode() {
		cacheMinDistances.addNode("A");
		cacheMinDistances.addNode("B");
		cacheMinDistances.addNode("C");
		cacheMinDistances.updateCache(WeightMetric.RESPONSE_TIME, "Service", new Path<>(Arrays.asList("A", "B"), 5));
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "B").get()
				.equals(new Path<>(Arrays.asList("A", "B"), 5)));
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "B", "A").get()
				.equals(new Path<>(Arrays.asList("B", "A"), 5)));
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "C").isEmpty());
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "B", "C").isEmpty());
		cacheMinDistances.removeNode("C");
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "B").isEmpty());
		assertThrows(IllegalArgumentException.class,
				() -> cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "C"));
		cacheMinDistances.removeNode("B");
		assertThrows(IllegalArgumentException.class,
				() -> cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "B"));
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "A").get()
				.equals(new Path<>(Arrays.asList("A"), 0)));
	}

	@Test
	void updateCacheAndGetMinPath() {
		Path<String> pathABConstant = new Path<>(Arrays.asList("A", "B"), 1);
		Path<String> pathABResponseTime = new Path<>(Arrays.asList("A", "B"), 10);

		assertThrows(IllegalArgumentException.class,
				() -> cacheMinDistances.updateCache(WeightMetric.CONSTANT, "Service", pathABConstant));
		cacheMinDistances.addNode("A");
		cacheMinDistances.addNode("B");
		cacheMinDistances.updateCache(WeightMetric.CONSTANT, "Service", pathABConstant);
		cacheMinDistances.updateCache(WeightMetric.RESPONSE_TIME, "Service", pathABResponseTime);

		assertEquals(pathABConstant, cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "A", "B").get());
		assertEquals(new Path<>(Arrays.asList("B", "A"), 1),
				cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "B", "A").get());
		assertEquals(pathABConstant, cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service1", "A", "B").get());
		assertEquals(new Path<>(Arrays.asList("B", "A"), 1),
				cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service1", "B", "A").get());

		assertEquals(pathABResponseTime,
				cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "B").get());
		assertEquals(new Path<>(Arrays.asList("B", "A"), 10),
				cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "B", "A").get());
		assertEquals(pathABResponseTime,
				cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service1", "A", "B").get());
		assertEquals(new Path<>(Arrays.asList("B", "A"), 10),
				cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service1", "B", "A").get());

		Path<String> pathABCConstant = new Path<>(Arrays.asList("A", "B", "C"), 2);
		assertThrows(IllegalArgumentException.class,
				() -> cacheMinDistances.updateCache(WeightMetric.CONSTANT, "Service", pathABCConstant));
		cacheMinDistances.addNode("C");
		cacheMinDistances.updateCache(WeightMetric.CONSTANT, "Service", pathABCConstant);
		assertEquals(pathABCConstant, cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "A", "C").get());
		assertEquals(new Path<>(Arrays.asList("C", "B", "A"), 2),
				cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "C", "A").get());
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "B", "C").isEmpty());
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "C").isEmpty());

		cacheMinDistances.updateCache(WeightMetric.CONSTANT, "Service1", new Path<>(Arrays.asList("A", "C", "B"), 2));
		assertNotEquals(pathABConstant, cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "A", "B").get());
		assertNotEquals(new Path<>(Arrays.asList("B", "A"), 1),
				cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "B", "A").get());
		assertEquals(new Path<>(Arrays.asList("A", "C", "B"), 2),
				cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "A", "B").get());
		assertEquals(new Path<>(Arrays.asList("B", "C", "A"), 2),
				cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "B", "A").get());

		cacheMinDistances.updateCache(WeightMetric.RESPONSE_TIME, "Service1", new Path<>(Arrays.asList("A", "C"), 8));
		assertEquals(pathABCConstant, cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "A", "C").get());
		assertEquals(new Path<>(Arrays.asList("C", "B", "A"), 2),
				cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "C", "A").get());
		assertEquals(new Path<>(Arrays.asList("A", "C"), 8),
				cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "A", "C").get());
		assertEquals(new Path<>(Arrays.asList("C", "A"), 8),
				cacheMinDistances.getMinPath(WeightMetric.RESPONSE_TIME, "Service", "C", "A").get());

		assertTrue(cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service", "A", "B").isEmpty());
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service1", "A", "B").isEmpty());
		cacheMinDistances.updateCache(WeightMetric.DISTANCE, "Service", new Path<>(Arrays.asList("A", "B"), 150));
		assertEquals(new Path<>(Arrays.asList("A", "B"), 150),
				cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service", "A", "B").get());
		assertEquals(new Path<>(Arrays.asList("B", "A"), 150),
				cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service", "B", "A").get());
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service1", "A", "B").isEmpty());
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service1", "B", "A").isEmpty());
		cacheMinDistances.updateCache(WeightMetric.DISTANCE, "Service1", new Path<>(Arrays.asList("A", "B"), 120));
		assertEquals(new Path<>(Arrays.asList("A", "B"), 120),
				cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service1", "A", "B").get());
		assertEquals(new Path<>(Arrays.asList("B", "A"), 120),
				cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service1", "B", "A").get());
		assertEquals(new Path<>(Arrays.asList("A", "B"), 150),
				cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service", "A", "B").get());
		assertEquals(new Path<>(Arrays.asList("B", "A"), 150),
				cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service", "B", "A").get());

		assertThrows(IllegalArgumentException.class,
				() -> cacheMinDistances.getMinPath(WeightMetric.CONSTANT, "Service", "A", "D"));
		assertTrue(cacheMinDistances.getMinPath(WeightMetric.DISTANCE, "Service2", "A", "B").isEmpty());
	}
}
