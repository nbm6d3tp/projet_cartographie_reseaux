package fr.internetcartographier.util.statistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Statistics implements Iterable<Statistic<?>> {

	private final List<Statistic<?>> statistics;

	public Statistics() {
		this(new ArrayList<>());
	}

	public Statistics(List<Statistic<?>> statistics) {
		this.statistics = statistics;
	}

	public void addStatistic(Statistic<?> statistic) {
		statistics.add(statistic);
	}

	public void removeStatistic(Statistic<?> statistic) {
		statistics.remove(statistic);
	}

	@Override
	public Iterator<Statistic<?>> iterator() {
		return new StatisticsIterator();
	}

	private class StatisticsIterator implements Iterator<Statistic<?>> {

		private int current;

		@Override
		public boolean hasNext() {
			return current < statistics.size();
		}

		@Override
		public Statistic<?> next() {
			if (hasNext()) {
				return statistics.get(current++);
			}

			throw new UnsupportedOperationException("Error: Statistics no next element!");
		}

	}

}
