package me.krypek.utils;

import java.io.Serializable;
import java.util.Objects;

public class Pair<K, V> implements Serializable {
	private static final long serialVersionUID = -1117001897695490806L;
	
	private K first;
	private V second;

	public Pair(final K first, final V second) {
		this.first = first;
		this.second = second;
	}

	public K getFirst() { return first; }

	public void setFisrt(K first) { this.first = first; }

	public V getSecond() { return second; }

	public void setSecond(V second) { this.second = second; }

	@Override
	public String toString() { return "Pair { " + first + ", " + second + "}"; }

	@Override
	public int hashCode() { return Objects.hash(first, second); }

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Pair pair)
			return first == pair.first && second == pair.second;
		return false;
	}

}
