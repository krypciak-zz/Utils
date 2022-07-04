package me.krypek.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class LinkedHashMapCollector<T, K, V, R> implements Collector<T, LinkedHashMap<K, V>, LinkedHashMap<K, V>> {

	private final BinaryOperator<LinkedHashMap<K, V>> combiner;
	private final BiConsumer<LinkedHashMap<K, V>, T> accumulator;

	public static <T, K, V, R> LinkedHashMapCollector<T, K, V, R> get(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper) {

		return new LinkedHashMapCollector<>(keyMapper, valueMapper);
	}

	public static <T, K, V, R> LinkedHashMapCollector<T, K, V, R> get(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {

		BiConsumer<LinkedHashMap<K, V>, T> accumulator = (map, element) -> map.merge(keyMapper.apply(element), valueMapper.apply(element), mergeFunction);

		return new LinkedHashMapCollector<>(accumulator, mapMerger(mergeFunction));
	}

	private LinkedHashMapCollector(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {

		this.combiner = (m1, m2) -> {
			for (Map.Entry<K, V> e : m2.entrySet()) {
				K k = e.getKey();
				V v = Objects.requireNonNull(e.getValue());
				V u = m1.putIfAbsent(k, v);

				if(u != null)
					throw new IllegalStateException(String.format("Duplicate key %s (attempted merging values %s and %s)", k, u, v));
			}
			return m1;
		};
		this.accumulator = (map, element) -> {
			K k = keyMapper.apply(element);
			V v = Objects.requireNonNull(valueMapper.apply(element));
			V u = map.putIfAbsent(k, v);
			if(u != null)
				throw new IllegalStateException(String.format("Duplicate key %s (attempted merging values %s and %s)", k, u, v));
		};
	}

	private LinkedHashMapCollector(BiConsumer<LinkedHashMap<K, V>, T> accumulator, BinaryOperator<LinkedHashMap<K, V>> mergeFunction) {

		this.combiner = (BinaryOperator<LinkedHashMap<K, V>>) mergeFunction;
		this.accumulator = accumulator;
	}

	@Override
	public BiConsumer<LinkedHashMap<K, V>, T> accumulator() { return accumulator; }

	@Override
	public Supplier<LinkedHashMap<K, V>> supplier() { return LinkedHashMap::new; }

	@Override
	public BinaryOperator<LinkedHashMap<K, V>> combiner() { return combiner; }

	@Override
	public Function<LinkedHashMap<K, V>, java.util.LinkedHashMap<K, V>> finisher() { return i -> i; }

	@Override
	public Set<Characteristics> characteristics() { return Set.of(Collector.Characteristics.IDENTITY_FINISH); }

	private static <K, V, M extends LinkedHashMap<K, V>> BinaryOperator<M> mapMerger(BinaryOperator<V> mergeFunction) {
		return (m1, m2) -> {
			for (Map.Entry<K, V> e : m2.entrySet())
				m1.merge(e.getKey(), e.getValue(), mergeFunction);
			return m1;
		};
	}
}
