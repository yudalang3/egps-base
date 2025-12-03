package utils.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.mutable.MutableInt;

/**
 * Simple string frequency counter using MutableInt for efficient counting.
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Efficient Counting:</strong> Uses MutableInt to avoid boxing/unboxing overhead</li>
 *   <li><strong>Insertion Order:</strong> HashMap preserves no specific order (use LinkedHashMap if order matters)</li>
 *   <li><strong>Sorted Output:</strong> Provides sorted printing by string keys</li>
 *   <li><strong>Clear Operation:</strong> Reset counter for reuse</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * StringCounter counter = new StringCounter();
 * counter.addOneEntry("apple");
 * counter.addOneEntry("banana");
 * counter.addOneEntry("apple");
 * counter.addOneEntry("orange");
 * counter.addOneEntry("banana");
 * counter.addOneEntry("apple");
 * 
 * // Print in original insertion order
 * counter.printWithOriginalOrder();
 * // Output: apple=3, banana=2, orange=1 (order may vary)
 * 
 * // Print sorted by key
 * counter.printSortedResults();
 * // Output: apple=3, banana=2, orange=1 (alphabetically sorted)
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Count species names in phylogenetic data</li>
 *   <li>Tally codon frequencies in sequences</li>
 *   <li>Track taxonomy rank occurrences</li>
 *   <li>Count file extension frequencies in directories</li>
 * </ul>
 * 
 * <h2>Performance Note:</h2>
 * <p>Uses Apache Commons MutableInt for efficient in-place increment operations,
 * avoiding Integer object creation on each count update.</p>
 * 
 * @see utils.EGPSObjectCounter for generic type-safe alternative
 * @author yudalang
 * @since 1.0
 */
public class StringCounter {

	private Map<String, MutableInt> counterMap = new HashMap<>();

	public void printWithOriginalOrder() {
		for (Entry<String, MutableInt> entry : counterMap.entrySet()) {
			System.out.println(entry);
		}
	}
	public void printSortedResults() {
		List<Entry<String, MutableInt>> list = new ArrayList<>(counterMap.entrySet());
		
		Collections.sort(list, new Comparator<Entry<String, MutableInt>>() {
			@Override
			public int compare(Entry<String, MutableInt> o1, Entry<String, MutableInt> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});
		
		for (Entry<String, MutableInt> entry : list) {
			System.out.println(entry);
		}
	}

	public void addOneEntry(String str) {
		MutableInt mutableInt = counterMap.get(str);
		if (mutableInt == null) {
			MutableInt mutableInt2 = new MutableInt(1);
			counterMap.put(str, mutableInt2);
		} else {
			mutableInt.increment();
		}
	}
	
	public void clear() {
		counterMap.clear();
	}
	
	public Map<String, MutableInt> getCounterMap() {
		return counterMap;
	}

	public static void main(String[] args) {
		StringCounter stringCounter = new StringCounter();
		
		for (int i = 0; i < 10; i++) {
			String ss = String.valueOf(i);
			
			stringCounter.addOneEntry(ss);
		}
		
		stringCounter.printWithOriginalOrder();
	}
}
