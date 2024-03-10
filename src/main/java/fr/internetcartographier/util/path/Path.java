package fr.internetcartographier.util.path;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a path in a graph with elements of type T. The path is defined by
 * a list of elements
 * and has an associated cost.
 */
// TODO Could be a record
public class Path<T> implements Iterable<T> {

	private final List<T> elements;
	private final double cost;

	/**
	 * Constructs a new Path with the given list of elements and cost.
	 *
	 * @param elements The list of elements in the path.
	 * @param cost     The cost associated with the path.
	 */
	public Path(List<T> elements, double cost) {
		this.elements = elements;
		this.cost = cost;
	}

	/**
	 * Gets the list of elements in the path.
	 *
	 * @return The list of elements.
	 */
	public List<T> getElements() {
		return elements;
	}

	/**
	 * Gets the cost associated with the path.
	 *
	 * @return The cost of the path.
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Gets a human-readable representation of the cost.
	 *
	 * @return The readable cost.
	 */
	public String getCostToString() {
		if (cost == Double.MAX_VALUE) {
			return "Infinity";
		}

		return String.valueOf(Math.round(cost * 100.0) / 100.0);
	}

	/**
	 * Gets a new path with the reversed order of elements.
	 *
	 * @return A new path with reversed order.
	 */
	public Path<T> getReversedOrder() {
		List<T> reversedOrder = new LinkedList<>(elements);
		Collections.reverse(reversedOrder);

		return new Path<>(reversedOrder, cost);
	}

	/**
	 * Gets the source element of the path.
	 *
	 * @return The source element.
	 */
	public T getSource() {
		return elements.get(0);
	}

	/**
	 * Gets the destination element of the path.
	 *
	 * @return The destination element.
	 */
	public T getDestination() {
		return elements.get(elements.size() - 1);
	}

	/**
	 * Returns an iterator over the elements of the path.
	 *
	 * @return An iterator.
	 */
	@Override
	public Iterator<T> iterator() {
        return new PathIterator();
	}

	/**
	 * Returns a string representation of the path.
	 *
	 * @return A string representation.
	 */
	@Override
	public String toString() {
		return "Path [elements=" + elements + ", cost=" + cost + "]";
	}

	/**
	 * Computes the hash code for the path.
	 *
	 * @return The hash code.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(cost, elements);
	}

	/**
	 * Checks if the path is equal to another object.
	 *
	 * @param  obj The object to compare.
	 * @return     True if equal, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Path<?> other = (Path<?>) obj;

		return Double.doubleToLongBits(cost) == Double.doubleToLongBits(other.cost)
				&& Objects.equals(elements, other.elements);
	}

	/**
	 * Iterator for iterating over the elements of the path.
	 */
	private class PathIterator implements Iterator<T> {

		private int current;

		@Override
		public boolean hasNext() {
			return current < elements.size();
		}

		@Override
		public T next() {
			if (hasNext()) {
				return elements.get(current++);
			}

			throw new UnsupportedOperationException("Error: Path no next element!");
		}

	}

}
