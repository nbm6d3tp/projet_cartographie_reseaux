package fr.internetcartographier.util.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class PathTest {

	@Test
	void testConstructorAndGetters() {
		List<String> elements = Arrays.asList("A", "B", "C");
		double cost = 10.0;

		Path<String> path = new Path<>(elements, cost);

		assertEquals(Arrays.asList("A", "B", "C"), path.getElements());
		assertEquals(10.0, path.getCost(), 0.0001);
	}

	@Test
	void testGetReversedOrder() {
		List<Integer> elements = Arrays.asList(1, 2, 3);
		double cost = 5.0;

		Path<Integer> path = new Path<>(elements, cost);
		Path<Integer> reversedPath = path.getReversedOrder();

		assertEquals(Arrays.asList(3, 2, 1), reversedPath.getElements());
		assertEquals(cost, reversedPath.getCost(), 0.0001);
	}

	@Test
	void testGetSource() {
		List<Character> elements = Arrays.asList('X', 'Y', 'Z');
		double cost = 8.0;

		Path<Character> path = new Path<>(elements, cost);
		assertEquals('X', path.getSource());
	}

	@Test
	void testGetDestination() {
		List<String> elements = Arrays.asList("P", "Q", "R");
		double cost = 15.0;

		Path<String> path = new Path<>(elements, cost);
		assertEquals("R", path.getDestination());
	}

	@Test
	void testEquals() {
		Path<String> path1 = new Path<>(Arrays.asList("A", "B", "C"), 10.0);
		Path<String> path2 = new Path<>(Arrays.asList("A", "B", "C"), 10.0);
		Path<String> path3 = new Path<>(Arrays.asList("X", "Y", "Z"), 5.0);

		assertEquals(path1, path2);
		assertNotEquals(path1, path3);
	}

}
