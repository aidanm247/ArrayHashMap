/**
 * HashEntry.java
 * @author Aidan Mess
 * represents one pair of a key and value in a hashmap
 * @param <K>
 * @param <V>
 */
class HashEntry<K,V> {
	K key;
	V value;
	
	public HashEntry() {
		
	}
	
	public HashEntry(K k, V v) {
		key = k;
		value = v;
	}
	
	public String toString() {
		return key + "," + value;
	}
	
}
