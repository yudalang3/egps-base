package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableInt;

import utils.string.StringCounter;

/**
 * Utility class providing operations for List manipulation and analysis.
 * 
 * <p>
 * This class contains static methods for common list operations including:
 * <ul>
 *   <li>Element counting and frequency analysis</li>
 *   <li>List partitioning and chunking</li>
 *   <li>Set operations (intersection, difference, subtraction)</li>
 *   <li>Containment percentage calculations</li>
 * </ul>
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Counting:</strong> Two implementations for element frequency counting</li>
 *   <li><strong>Partitioning:</strong> Split lists into equal-sized chunks</li>
 *   <li><strong>Set Operations:</strong> Intersection, difference, and subtraction</li>
 *   <li><strong>Containment:</strong> Check if list contains percentage of another list</li>
 * </ul>
 * 
 * <h2>Performance Notes:</h2>
 * <ul>
 *   <li>{@link #countString(List)} is optimized for large lists (10M+ elements)</li>
 *   <li>{@link #countSameComponents(List)} is simpler for smaller lists</li>
 * </ul>
 * 
 * @author yjn, ydl
 * @version 1.0
 * @since 1.7
 * @see StringCounter
 */
public class EGPSListUtil {

	/**
	 * Counts the frequency of each element in a string list.
	 * 
	 * <p>
	 * This method creates a frequency map where each unique string maps to
	 * the number of times it appears in the input list.
	 * </p>
	 * 
	 * <p><strong>Example:</strong></p>
	 * <pre>
	 * List&lt;String&gt; list = Arrays.asList("a", "b", "a", "c", "b", "a");
	 * Map&lt;String, Integer&gt; counts = countSameComponents(list);
	 * // Result: {"a"=3, "b"=2, "c"=1}
	 * </pre>
	 * 
	 * <p><strong>Performance:</strong> Suitable for lists of any size, but
	 * {@link #countString(List)} may be faster for very large lists (10M+ elements).</p>
	 * 
	 * @param inputList the list of strings to count
	 * @return a map of string to frequency count
	 * @see #countString(List)
	 */
	public static Map<String, Integer> countSameComponents(List<String> inputList) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String str : inputList) {
			Integer i = 1; // 定义一个计数器，用来记录重复数据的个数
			if (map.get(str) != null) {
				i = map.get(str) + 1;
			}
			map.put(str, i);
		}
		return map;
	}

	/**
	 * Counts the frequency of each element in a string list (optimized version).
	 * 
	 * <p>
	 * This method uses {@link StringCounter} for improved performance on very large
	 * lists (10 million+ elements). It uses mutable integers to avoid repeated
	 * Integer object creation.
	 * </p>
	 * 
	 * <p><strong>Performance:</strong> Faster than {@link #countSameComponents(List)}
	 * on lists with 10M+ elements. For smaller lists, the performance difference
	 * is negligible.</p>
	 * 
	 * <p><strong>Example:</strong></p>
	 * <pre>
	 * List&lt;String&gt; hugeList = ...; // 20 million elements
	 * Map&lt;String, MutableInt&gt; counts = countString(hugeList);
	 * // Access counts: counts.get("someString").intValue()
	 * </pre>
	 * 
	 * @param inputList the list of strings to count
	 * @return a map of string to MutableInt count (more efficient than Integer)
	 * @see #countSameComponents(List)
	 * @see StringCounter
	 */
	public static Map<String, MutableInt> countString(List<String> inputList) {
		StringCounter stringCounter = new StringCounter();
		for (String string : inputList) {
			stringCounter.addOneEntry(string);
		}
		return stringCounter.getCounterMap();
	}

	/**
	 * Partitions a list into a specified number of roughly equal-sized sublists.
	 * 
	 * <p>
	 * Divides the input list into {@code numOfParts} sublists, where each sublist
	 * contains approximately the same number of elements. The last sublist may be
	 * smaller if the total size doesn't divide evenly.
	 * </p>
	 * 
	 * <p><strong>Example:</strong></p>
	 * <pre>
	 * List&lt;String&gt; list = Arrays.asList("a", "b", "c", "d", "e");
	 * List&lt;List&lt;String&gt;&gt; parts = partition(list, 2);
	 * // Result: [["a", "b", "c"], ["d", "e"]]
	 * </pre>
	 * 
	 * <p><strong>Algorithm:</strong> Calculates chunk size as ceil(totalSize / numOfParts)
	 * to ensure all elements are included.</p>
	 * 
	 * @param <T> the type of elements in the list
	 * @param bigList the list to partition
	 * @param numOfParts the number of partitions to create (must be >= 1)
	 * @return a list of sublists, each containing roughly equal number of elements
	 * @throws IllegalArgumentException if numOfParts < 1
	 */
	public static <T> List<List<T>> partition(List<T> bigList, int numOfParts) {

		if (numOfParts < 1) {
			throw new IllegalArgumentException("The numOfParts should greater than 0");
		}
		List<List<T>> chunks = new ArrayList<>(numOfParts);

		int size = bigList.size();
		int sizeOfEachChunk = (int) Math.ceil(size / (double) numOfParts);

		for (int i = 0; i < size; i += sizeOfEachChunk) {
			int toIndex = i + sizeOfEachChunk;
			if (toIndex > size) {
				toIndex = size;
			}
			List<T> subList = bigList.subList(i, toIndex);
			List<T> chunk = new ArrayList<>(subList);
			chunks.add(chunk);
		}

		return chunks;
	}

	/**
	 * Checks if list2 contains at least a specified percentage of elements from list1.
	 * 
	 * <p>
	 * Calculates what percentage of list1's elements are present in list2,
	 * and returns true if this percentage meets or exceeds the threshold.
	 * </p>
	 * 
	 * <p><strong>Example:</strong></p>
	 * <pre>
	 * List&lt;String&gt; list1 = Arrays.asList("a", "b", "c", "d");
	 * List&lt;String&gt; list2 = Arrays.asList("a", "b", "e", "f");
	 * boolean result = containsPercent(list1, list2, 0.5);
	 * // Result: true (2 out of 4 = 50% >= 50%)
	 * </pre>
	 * 
	 * <p><strong>Note:</strong> This method has O(n*m) complexity. For large lists,
	 * consider converting list2 to a Set first for O(n) performance.</p>
	 * 
	 * @param <T> the type of elements in the lists
	 * @param list1 the reference list to check against
	 * @param list2 the list to check for containment
	 * @param percentThreshold the minimum percentage required (0.0 to 1.0)
	 * @return true if list2 contains >= percentThreshold of list1's elements
	 */
	public static <T> boolean containsPercent(List<T> list1, List<T> list2, double percentThreshold) {
		int count = 0;

		for (T num : list1) {
			if (list2.contains(num)) {
				count++;
			}
		}

		double percentage = (double) count / list1.size();

		return percentage >= percentThreshold;
	}

	/**
	 * Calculates the difference between two lists (elements in list1 but not in list2).
	 * 
	 * <p>
	 * Returns all elements that are present in list1 but absent from list2.
	 * This is equivalent to the set operation list1 \ list2.
	 * </p>
	 * 
	 * <p><strong>Example:</strong></p>
	 * <pre>
	 * List&lt;String&gt; list1 = Arrays.asList("a", "b", "c", "d");
	 * List&lt;String&gt; list2 = Arrays.asList("b", "d", "e");
	 * List&lt;String&gt; diff = calculateDifference(list1, list2);
	 * // Result: ["a", "c"]
	 * </pre>
	 * 
	 * @param <T> the type of elements in the lists
	 * @param list1 the list to find unique elements from
	 * @param list2 the list to exclude
	 * @return a new list containing elements in list1 but not in list2
	 * @see #calculateIntersection(List, List)
	 * @see #calculateSubtract(List, List)
	 */
	public static <T> List<T> calculateDifference(List<T> list1, List<T> list2) {
		return list1.stream().filter(element -> !list2.contains(element)).collect(Collectors.toList());
	}

	/**
	 * Calculates the intersection of two lists (elements present in both lists).
	 * 
	 * <p>
	 * Returns all elements that are present in both list1 and list2.
	 * This is equivalent to the set operation list1 ∩ list2.
	 * </p>
	 * 
	 * <p><strong>Example:</strong></p>
	 * <pre>
	 * List&lt;String&gt; list1 = Arrays.asList("a", "b", "c", "d");
	 * List&lt;String&gt; list2 = Arrays.asList("b", "d", "e", "f");
	 * List&lt;String&gt; intersection = calculateIntersection(list1, list2);
	 * // Result: ["b", "d"]
	 * </pre>
	 * 
	 * @param <T> the type of elements in the lists
	 * @param list1 the first list
	 * @param list2 the second list
	 * @return a new list containing elements present in both lists
	 * @see #calculateDifference(List, List)
	 */
	public static <T> List<T> calculateIntersection(List<T> list1, List<T> list2) {
		return list1.stream().filter(element -> list2.contains(element)).collect(Collectors.toList());
	}

	/**
	 * Subtracts list2 from list1 (alias for {@link #calculateDifference(List, List)}).
	 * 
	 * <p>
	 * This method is functionally identical to {@link #calculateDifference(List, List)}.
	 * Returns elements present in list1 but not in list2.
	 * </p>
	 * 
	 * @param <T> the type of elements in the lists
	 * @param list1 the list to subtract from
	 * @param list2 the list to subtract
	 * @return a new list containing elements in list1 but not in list2
	 * @see #calculateDifference(List, List)
	 */
	public static <T> List<T> calculateSubtract(List<T> list1, List<T> list2) {
		return list1.stream().filter(element -> !list2.contains(element)).collect(Collectors.toList());
	}
	
	public static void main(String[] args) {
		List<String> list = Arrays.asList("a", "b", "c", "d", "e");
		List<String> list2 = Arrays.asList("a", "b", "c","d","e");
		List<List<String>> partition = partition(list, 78);
		System.out.println(calculateSubtract(list, list2).size());

//		for (List<String> l : partition) {
//			System.out.println(l);
//		}
	}

}
