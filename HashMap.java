/**
 * HashMap.java
 * @author Aidan Mess
 *	HashMap implementation utilizing an array
 * @param <K> - the key in the HashMap
 * @param <V> - the value in the HashMap
 */
//quadratic probing to handle collisions
public class HashMap <K,V> {
	
	/** constants */
	private static final HashEntry D = new HashEntry();
	private static final HashEntry E = new HashEntry();
	private static final int INITIAL_CAPACITY = 5;
	private static final double MAX_LOAD_FACTOR = 0.5;
	/** member variables*/
	private int cap;
	private HashEntry<K, V>[] table;
	private double maxLoadFactor;
	private double currentLoadFactor;
	private int count;
	
	/**
	 * creates an empty internal table (array) for HashEntry of Keys and Values, 
	 * and sets the maximum load factor to be 0.5 (default). 
	 * You must call another constructor to achieve this.
	 * for each index in the table, assign the empty marker
	 */
	//look at arrays class for constructor - think about 112
	public HashMap() {
		//constructor?
		this(INITIAL_CAPACITY,MAX_LOAD_FACTOR);
		
	}
	
	/**
	 * creates an empty internal table (array) for HashEntry of Keys and Values, 
   	   and sets the maximum load factor to be lf. You must call another constructor to achieve this.
   	   for each index in the table, assign the empty marker
	 * @param If
	 */
	public HashMap(double If) {
		this(INITIAL_CAPACITY, If);
	}
	
	/**
	 * creates an empty internal table (array) for HashEntry of Keys and Values with initial capacity cap, 
	 * and sets the maximum load factor to be 0.5. 
	 * You must call another constructor to achieve this.
	 */
	public HashMap(int cap) {
		this(cap, MAX_LOAD_FACTOR);
	}
	
	/**
	 * creates an empty internal table (array) for HashEntry of Keys and Values
	 *  with initial capacity cap, 
	 *  and sets the maximum load factor to be lf.
	 * @param cap
	 * @param If
	 */
	public HashMap(int cap, double If) {
		table = (HashEntry<K,V>[]) new HashEntry[cap];	
		maxLoadFactor = If;
		currentLoadFactor = 0; 
		for(int i = 0; i < table.length; i ++) {
			table[i] = E;
		}
	}
	/**
	 * Hash function to be called on each node
	 * returned index must be in [0, internal_table_size - 1] (inclusive).
	 * @param key
	 * @return
	 */
	public int hash(K key) {
		return key.toString().length() % table.length;
	}
	/**
	 * 1. if this table already contains key, this method replaces the value currently
	 * associated with key with the new value (incoming parameter) and returns the old (replaced) value.
	 * 2. if this table does not contain key, this method adds a new [key, value] 
	 * entry into the table and returns null.
	 * *. try to write a good, readable code for this method. Much of what you do will be done in indexOf as well.
	 * you may NOT use containsKey/indexOf methods.
	 */
	//longest method
	public V put(K key, V value) {
		int pi = hash(key);
		int index =  pi;
		int deleted = -1;
		int numSquared = 1;
		boolean added = false;
		boolean foundDeleted = false;
		
		while(!added) {
			if(table[index].key == key) {
				V temp= table[index].value;
				table[index].value = value;
				return temp;
			}
			if(table[index] == E) {
				table[index] = new HashEntry(key, value);
				++count;
				currentLoadFactor = ((double) count)/table.length;
				if(currentLoadFactor > maxLoadFactor) {
					rehash();
				}
				break;
			}else {
				if(table[index] == D && !foundDeleted) {
					deleted = index;
				}
				if(numSquared > table.length) {
					if(deleted != -1 && ((((count+1)/table.length) > maxLoadFactor))) {
						table[deleted] = new HashEntry(key, value);
						++count;
						break;
					}else {
						rehash();
					}
				}
					int probe = numSquared * numSquared;
					index = (pi+ probe)  % table.length;
					++numSquared;
				
			}
		}
		return null;
	}
	/**
	 * returns the array (table) index in which the 'key' is located using quadratic probing.
	 * returns -1 if not found. 
	 * (This method may be handy for another method or two..)
	 * For testing purposes, leave this method at "default" access level.
	 * @param key
	 * @return
	 */
	int indexOf(K key) {
		int pi = hash(key);
		int index =  pi;
		int numSquared = 1;
		int ran = 0;
		
		while(ran < table.length) {
			if(table[index].key == key) {
				return index;
			}else {
				int probe = numSquared * numSquared;
				index = (pi + probe) % table.length;
				++ran;
				++numSquared;
			
			}
		}
		return -1;
	}
	/**
	 * using the same quadratic probing sequence as in put, 
	 * determine if there is an entry with matching key.
	 */
	public boolean containsKey(K key) {
		return indexOf(key) >= 0;
	}
	/**
	 * returns true if there is an entry with matching value. 
	 */
	public boolean containsValue(V value) {
		for(int i =0; i < table.length; i++) {
			if(table[i].value == value) {
				return true;
			}
		}
		return false;
	}
	/**
	 * returns the value whose associated key matches key
	 * @param key
	 * @return
	 */
	public V get(K key) {
		if(indexOf(key) >= 0) {
			return table[indexOf(key)].value;
		}else {
			return null;
		}
	}
	/**
	 * removes the key and its associated value from this hashtable
	 * returns the removed value, or null if the key does not exist in this hashtable.
	 * A DELETED marker must be placed at the index. 
	 */
	public V remove(K key) {
		int index= indexOf(key);
		System.out.println(index);
		if(index >= 0) {
			V deleted = table[index].value;
			table[index] = D;
			--count;
			currentLoadFactor = ((double) (count))/table.length;
			return deleted;
		}else {
			return null;
		}
	}
	/**
	 * returns the number of key-value pairs stored in this table. This method must run in O(1) time. 
	 */
	public int size() {
		return count;
	}
	/**
	 * returns true if the table is empty and false otherwise in O(1) time. 
	 */
	public boolean isEmpty() {
		return count == 0;
	}
	/**
	 * clears the table. This is a LOT more involved process compared to DList clear().
	 */
	public void clear() {
		count = 0;
		for(int i = 0; i < table.length; i++) {
			table[i] = E;
		}
	}
	
	/**
	 * for debugging purposes, returns a print-friendly String with entire content of this hashtable
	 */
	public String toString() {
		String s = "Current Load Factor: " + String.format("%.2f", currentLoadFactor)  + "\n";
		s += "Max Load Factor:" + maxLoadFactor  + "\n";
		s += "Current size: " + count + "\n";
		s += "Current capacity:" + table.length + "\n\n";
		for(int i = 0 ; i < table.length; i ++) {
			if(table[i] != E) {
				if(table[i] == D) {
					s += i+":"+ "DELETED" +  "\n";
				}else {
					s += i+":"+ table[i].toString() +  "\n";
				}
			}
		}
		return s;
	}
	/**
	 * 
	 * @param n - number to be checked if prime
	 * @return if n is prime
	 */
	private static boolean isPrime(int n) {
		if(n ==0 || n == 1) {
			return false;
		}
		for(int i = 2; i < n; i++) {
			if(n % i == 0) {
				return false;
			}
		}
		return true;
		
	}
	/**
	 * @return the smallest prime number > 2n (to be used in rehash())
	 */
	private static int nextCapacity(int n) {
		for(int i = (2*n)+1; ; i++) {
			if(isPrime(i)) {
				return i;
			}
		}
	}
	/**
	 * increases the internal storage capacity properly by 
	 * finding the smallest prime number greater than the double of current table capacity
	 * correctly insert all existing entries (key-value pairs) to the new table
	 * this method should be automatically called from the put method when 
	 * the fill rate (load factor) of the internal table reaches maxLoadFactor (instance variable) 
	 * OR the quadratic probing is not successful
	 */
	private void rehash() {
		int newLength = nextCapacity(table.length);
		count = 0;
		HashEntry<K, V>[] oldTable = table;
		table = (HashEntry<K,V>[]) new HashEntry[newLength];
		for(int i = 0 ; i < table.length; i++) {
			table[i] = E;
		}
		for(int i =0; i < oldTable.length; i++) {
			if(oldTable[i] != E && oldTable[i] != D) {
					put(oldTable[i].key, oldTable[i].value);
				}
			}
		}
	
	}



