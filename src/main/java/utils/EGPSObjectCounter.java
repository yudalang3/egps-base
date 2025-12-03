package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.mutable.MutableInt;

/**
 * Generic counter for tracking object occurrences and frequencies.
 * 
 * <p>
 * This class provides an efficient way to count occurrences of objects using a
 * {@link LinkedHashMap} to maintain insertion order. It uses {@link MutableInt}
 * for efficient counting without object allocation on each increment.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Generic Type Support:</strong> Works with any object type</li>
 *   <li><strong>Insertion Order Preservation:</strong> LinkedHashMap maintains order</li>
 *   <li><strong>Efficient Counting:</strong> MutableInt avoids Integer boxing/unboxing</li>
 *   <li><strong>Sorting Capabilities:</strong> Sort by count ascending</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Count string occurrences
 * EGPSObjectCounter&lt;String&gt; counter = new EGPSObjectCounter&lt;&gt;();
 * counter.addOneEntry("apple");
 * counter.addOneEntry("banana");
 * counter.addOneEntry("apple");
 * 
 * // Print in original order
 * counter.printWithOriginalOrder();
 * // Output: apple=2, banana=1
 * 
 * // Print sorted by count
 * counter.printSortedResults();
 * // Output: banana=1, apple=2
 * 
 * // Get count map for custom processing
 * Map&lt;String, MutableInt&gt; counts = counter.getCounterMap();
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Count species occurrences in sequence data</li>
 *   <li>Tally amino acid frequencies</li>
 *   <li>Count domain types in protein annotations</li>
 *   <li>Frequency analysis of any categorical data</li>
 * </ul>
 * 
 * <h2>Performance:</h2>
 * <p>
 * Uses MutableInt to avoid Integer object creation on every count increment,
 * providing better performance for high-frequency counting scenarios.
 * </p>
 * 
 * @param <T> the type of objects to count
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see LinkedHashMap
 * @see MutableInt
 */
public class EGPSObjectCounter<T> {

	private Map<T, MutableInt> counterMap = new LinkedHashMap<>();

	/**
	 * Prints counter results in original insertion order.
	 * 
	 * <p>
	 * Each entry is printed as: key=count
	 * </p>
	 */
	public void printWithOriginalOrder() {
		for (Entry<T, MutableInt> entry : counterMap.entrySet()) {
			System.out.println(entry);
		}
	}
	/**
	 * Prints counter results sorted by count in ascending order.
	 * 
	 * <p>
	 * Sorts entries from lowest count to highest count.
	 * Each entry is printed as: key=count
	 * </p>
	 */
	public void printSortedResults() {
		List<Entry<T, MutableInt>> list = new ArrayList<>(counterMap.entrySet());
		
		Collections.sort(list, new Comparator<Entry<T, MutableInt>>() {
			@Override
			public int compare(Entry<T, MutableInt> o1, Entry<T, MutableInt> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		
		for (Entry<T, MutableInt> entry : list) {
			System.out.println(entry);
		}
	}

	/**
	 * Adds or increments the count for an object.
	 * 
	 * <p>
	 * If the object hasn't been seen before, initializes count to 1.
	 * If the object exists, increments its count by 1.
	 * </p>
	 * 
	 * @param str the object to count
	 */
	public void addOneEntry(T str) {
		MutableInt mutableInt = counterMap.get(str);
		if (mutableInt == null) {
			MutableInt mutableInt2 = new MutableInt(1);
			counterMap.put(str, mutableInt2);
		} else {
			mutableInt.increment();
		}
	}
	
	/**
	 * Clears all counted entries.
	 */
	public void clear() {
		counterMap.clear();
	}
	
	/**
	 * Returns the underlying counter map.
	 * 
	 * <p>
	 * The map keys are the counted objects, and values are MutableInt counts.
	 * The map maintains insertion order (LinkedHashMap).
	 * </p>
	 * 
	 * @return the counter map
	 */
	public Map<T, MutableInt> getCounterMap() {
		return counterMap;
	}

	public static void main(String[] args) {
		EGPSObjectCounter<String> stringCounter = new EGPSObjectCounter<>();
		
		for (int i = 0; i < 10; i++) {
			String ss = String.valueOf(i);
			
			stringCounter.addOneEntry(ss);
		}
		
		stringCounter.printWithOriginalOrder();
	}
}
