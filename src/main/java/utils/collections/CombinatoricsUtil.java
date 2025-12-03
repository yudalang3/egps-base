package utils.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Combinatorics utility class providing mathematical combination and permutation operations.
 * 
 * <p>
 * This utility class provides static methods for generating all possible combinations
 * from a given collection, supporting various combinatorial algorithms commonly used
 * in bioinformatics and computational biology applications.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Combination Generation:</strong> Generate all possible k-element combinations from n-element sets</li>
 *   <li><strong>Generic Implementation:</strong> Works with any comparable object type</li>
 *   <li><strong>Multiple Sizes:</strong> Support for combinations of different sizes from the same dataset</li>
 *   <li><strong>Order-Independent:</strong> Combinations ignore ordering (order doesn't matter)</li>
 * </ul>
 * 
 * <h2>Mathematical Background:</h2>
 * <p>
 * A combination is a selection of items from a larger set where the order doesn't matter.
 * For n elements taken k at a time, the number of combinations is C(n,k) = n!/(k!(n-k)!).
 * </p>
 * 
 * <h2>Usage Examples:</h2>
 * <pre>
 * List&lt;String&gt; elements = Arrays.asList("A", "B", "C", "D");
 * 
 * // Generate all 2-element combinations
 * List&lt;List&lt;String&gt;&gt; pairs = CombinatoricsUtil.combination(elements, 2);
 * // Results: [[A, B], [A, C], [A, D], [B, C], [B, D], [C, D]]
 * 
 * // Generate all 1-element combinations
 * List&lt;List&lt;String&gt;&gt; singles = CombinatoricsUtil.combination(elements, 1);
 * // Results: [[A], [B], [C], [D]]
 * 
 * // Generate all possible combinations (different sizes)
 * for (int i = 1; i &lt;= elements.size(); i++) {
 *     List&lt;List&lt;String&gt;&gt; combos = CombinatoricsUtil.combination(elements, i);
 *     System.out.println("Size " + i + ": " + combos);
 * }
 * </pre>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Sequence Analysis:</strong> Generate all possible k-mer combinations</li>
 *   <li><strong>Motif Finding:</strong> Test all possible motif length combinations</li>
 *   <li><strong>Parameter Space:</strong> Generate all possible parameter combinations for analysis</li>
 *   <li><strong>Cross-Validation:</strong> Create all possible training/test set splits</li>
 *   <li><strong>Phylogenetic Analysis:</strong> Generate all possible species subsets</li>
 * </ul>
 * 
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Time complexity: O(C(n,k)) where C is the binomial coefficient</li>
 *   <li>Memory usage grows exponentially with the number of combinations</li>
 *   <li>For large datasets, consider filtering or sampling strategies</li>
 *   <li>Results are returned as List&lt;List&lt;T&gt;&gt; for easy iteration</li>
 * </ul>
 * 
 * @see java.util.List
 * @see java.util.Arrays
 * @see java.util.Collections
 * @author eGPS Development Team
 * @since 1.0
 */
public class CombinatoricsUtil {

	public static void main(String[] args) {

		List<String> input = Arrays.asList("a", "b", "c", "d");

		for (int i = 1; i <= input.size(); i++) {
			List<List<String>> combination = combination(input, i);
			System.out.println(combination.size());
			for (List<String> list : combination) {
				System.out.println(list);
			}
		}

	}

	/**
	 * 这里计算的是集合的组合数有多少。 例如：上面的例子，abcd四个元素，任意取出其中一个有多少种组合数。
	 * 
	 * @param <T>
	 * @param values
	 * @param size
	 * @return
	 */
	public static <T> List<List<T>> combination(List<T> values, int size) {

		if (0 == size) {
			return Collections.singletonList(Collections.<T>emptyList());
		}

		if (values.isEmpty()) {
			return Collections.emptyList();
		}

		List<List<T>> combination = new LinkedList<List<T>>();

		T actual = values.iterator().next();

		List<T> subSet = new LinkedList<T>(values);
		subSet.remove(actual);

		List<List<T>> subSetCombination = combination(subSet, size - 1);

		for (List<T> set : subSetCombination) {
			List<T> newSet = new LinkedList<T>(set);
			newSet.add(0, actual);
			combination.add(newSet);
		}

		combination.addAll(combination(subSet, size));

		return combination;
	}
}
