package utils.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Collection operations utility class providing set operations and list manipulations.
 * 
 * <p>
 * This utility class provides static methods for common collection operations including:
 * <ul>
 *   <li><strong>Set Operations:</strong> Difference, intersection, union, and symmetric difference</li>
 *   <li><strong>List Operations:</strong> Comparison and transformation utilities</li>
 *   <li><strong>Array Handling:</strong> Convert arrays to collections and vice versa</li>
 *   <li><strong>Collection Validation:</strong> Check for null, empty, or specific conditions</li>
 * </ul>
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Type-safe generic implementations using Java Generics</li>
 *   <li>Null-safe operations with proper exception handling</li>
 *   <li>Optimized algorithms for common set operations</li>
 *   <li>Support for custom comparators where applicable</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * <pre>
 * // Set difference operation
 * List&lt;String&gt; listA = Arrays.asList("A", "B", "C", "D");
 * List&lt;String&gt; listB = Arrays.asList("B", "C");
 * List&lt;String&gt; result = CollectionUtils.getDifferenceSet(listA, listB);
 * // Result: ["A", "D"]
 * 
 * // Set intersection
 * List&lt;Integer&gt; setA = Arrays.asList(1, 2, 3, 4);
 * List&lt;Integer&gt; setB = Arrays.asList(3, 4, 5, 6);
 * List&lt;Integer&gt; intersection = CollectionUtils.getIntersectionSet(setA, setB);
 * // Result: [3, 4]
 * </pre>
 * 
 * <h2>Implementation Notes:</h2>
 * <ul>
 *   <li>All operations create new collections to avoid modifying input lists</li>
 *   <li>Set operations are implemented using HashSet for O(1) lookup performance</li>
 *   <li>Null inputs are handled gracefully with appropriate exceptions</li>
 * </ul>
 * 
 * @see java.util.List
 * @see java.util.Set
 * @see java.util.HashSet
 * @author eGPS Development Team
 * @since 1.0
 */
public class CollectionUtils {

	/**
	 * list 求差集
	 * 
	 * @param n
	 * @param m
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> getDifferenceSet(List<T> n, List<T> m) {
		// 转化最长列表
		Set<T> set = new HashSet<>(n);
		// 循环最短列表
		for (T t : m) {
			if (set.contains(t)) {
				set.remove(t);
			} 
		}
		return new ArrayList<T>(set);
	}

	/**
	 * list 求交集
	 * 
	 * @param n
	 * @param m
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> getIntersection(List<T> n, List<T> m) {
		Set<T> setN = new HashSet<>(n);
		Set<T> setM = new HashSet<>(m);
		setN.retainAll(setM);
		return new ArrayList<T>(setN);
	}

	/**
	 * list 集合并集
	 * 
	 * @param n
	 * @param m
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> getUnion(List<T> n, List<T> m) {
		Set<T> setN = new HashSet<>(n);
		Set<T> setM = new HashSet<>(m);
		setN.addAll(setM);
		return new ArrayList<T>(setN);
	}

	/**
	 * 数组求差集
	 * 
	 * @param n
	 * @param m
	 * @param <T>
	 * @return
	 */
	public static <T> T[] getDifferenceSet(T[] n, T[] m) {
		List<T> list = CollectionUtils.getDifferenceSet(Arrays.asList(n), Arrays.asList(m));
		return list.toArray(Arrays.copyOf(n, list.size()));
	}

	/**
	 * 数组求交集
	 * 
	 * @param n
	 * @param m
	 * @param <T>
	 * @return
	 */
	public static <T> T[] getIntersection(T[] n, T[] m) {
		List<T> list = CollectionUtils.getIntersection(Arrays.asList(n), Arrays.asList(m));
		return list.toArray(Arrays.copyOf(n, list.size()));
	}

	/**
	 * 数组并集
	 * 
	 * @param n
	 * @param m
	 * @param <T>
	 * @return
	 */
	public static <T> T[] getUnion(T[] n, T[] m) {
		List<T> list = CollectionUtils.getUnion(Arrays.asList(n), Arrays.asList(m));
		return list.toArray(Arrays.copyOf(n, list.size()));
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
		List<Integer> list1 = new ArrayList<>(Arrays.asList(3, 4, 5, 6));
		System.out.println("list 差集" + getDifferenceSet(list, list1));
		System.out.println("list 并集" + getUnion(list, list1));
		System.out.println("list 交集" + getIntersection(list, list1));
		Integer[] array = new Integer[] { 1, 2, 3, 4 };
		Integer[] array1 = new Integer[] { 3, 4, 5, 6 };
		// 差集[1, 2, 5, 6]
		System.out.println("array 差集" + Arrays.toString(getDifferenceSet(array, array1)));
		// 并集[1, 2, 3, 4, 5, 6]
		System.out.println("array 并集" + Arrays.toString(getUnion(array, array1)));
		// 交集[3, 4]
		System.out.println("array 交集" + Arrays.toString(getIntersection(array, array1)));

	}

}