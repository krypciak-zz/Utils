package me.krypek.utils;

public class TripleObject<K, V, J> {

	public String toString() { return "TripleObject { " + value1 + ", " + value2 + ", : " + value3 + " }"; }

	private K value1;
	private V value2;
	private J value3;

	public TripleObject(K v1, V v2, J v3) {
		value1 = v1;
		value2 = v2;
		value3 = v3;
	}

	public K getValue1() { return value1; }

	public void setValue1(K value1) { this.value1 = value1; }

	public V getValue2() { return value2; }

	public void setValue2(V value2) { this.value2 = value2; }

	public J getValue3() { return value3; }

	public void setValue3(J value3) { this.value3 = value3; }

}
