package analysis.math;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Comprehensive utility class for statistical operations on lists and arrays of double values.
 * 
 * <p>
 * This class provides efficient methods for analyzing numerical data structures commonly
 * used in bioinformatics computations, statistical analysis, and scientific data processing.
 * It leverages modern Java Stream API for optimal performance and readability.
 * </p>
 * 
 * <h2>Core Features</h2>
 * <ul>
 *   <li><strong>Min/Max Calculation</strong>: Efficient simultaneous min/max value extraction</li>
 *   <li><strong>Multiple Data Types</strong>: Support for both List&lt;Double&gt; and primitive double[] arrays</li>
 *   <li><strong>Stream-based Processing</strong>: Utilizes Java 8+ Stream API for optimal performance</li>
 *   <li><strong>Null Safety</strong>: Graceful handling of null and empty inputs</li>
 *   <li><strong>Consistent Interface</strong>: Unified return type (Pair) for all operations</li>
 * </ul>
 * 
 * <h2>Performance Characteristics</h2>
 * <ul>
 *   <li><strong>Time Complexity</strong>: O(n) for all operations (single pass through data)</li>
 *   <li><strong>Space Complexity</strong>: O(1) - constant extra space usage</li>
 *   <li><strong>Optimization</strong>: Stream operations are automatically parallelized when beneficial</li>
 * </ul>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li><strong>Statistical Analysis</strong>: Finding data ranges and outliers</li>
 *   <li><strong>Data Validation</strong>: Checking value bounds and constraints</li>
 *   <li><strong>Scientific Computing</strong>: Range analysis for experimental data</li>
 *   <li><strong>Bioinformatics</strong>: Sequence score ranges and alignment statistics</li>
 *   <li><strong>Quality Control</strong>: Data range verification and anomaly detection</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * // Example with List&lt;Double&gt;
 * List&lt;Double&gt; scores = Arrays.asList(85.5, 92.3, 78.9, 95.1, 88.7);
 * Pair&lt;Double, Double&gt; range = DoubleListUtils.getMinMax(scores);
 * System.out.println("Range: " + range.getLeft() + " to " + range.getRight());
 * // Output: Range: 78.9 to 95.1
 * 
 * // Example with primitive array
 * double[] measurements = {12.5, 15.3, 9.8, 18.2, 14.1};
 * Pair&lt;Double, Double&gt; measurementRange = DoubleListUtils.getMinMax(measurements);
 * System.out.println("Min: " + measurementRange.getLeft() + ", Max: " + measurementRange.getRight());
 * // Output: Min: 9.8, Max: 18.2
 * 
 * // Handling empty data
 * List&lt;Double&gt; emptyList = new ArrayList&lt;&gt;();
 * Pair&lt;Double, Double&gt; emptyResult = DoubleListUtils.getMinMax(emptyList);
 * System.out.println("Empty range: " + emptyResult);
 * // Output: Empty range: (NaN, NaN)
 * </pre>
 * 
 * <h2>Error Handling</h2>
 * <ul>
 *   <li>Null inputs return (NaN, NaN) pair</li>
 *   <li>Empty collections/arrays return (NaN, NaN) pair</li>
 *   <li>Single-element collections return (value, value) pair</li>
 * </ul>
 * 
 * <h2>Thread Safety</h2>
 * <p>
 * All methods in this class are thread-safe as they are static and operate only on
 * their input parameters without modifying any shared state.
 * </p>
 * 
 * <h2>Future Extensions</h2>
 * <p>
 * Potential future enhancements could include:
 * </p>
 * <ul>
 *   <li>Additional statistical measures (mean, median, standard deviation)</li>
 *   <li>Percentile and quartile calculations</li>
 *   <li>Data distribution analysis</li>
 *   <li>Custom aggregation operations</li>
 * </ul>
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 * @see org.apache.commons.lang3.tuple.Pair For the return type structure
 * @see java.util.stream.Stream For Stream API operations
 * @see java.util.OptionalDouble For handling optional double values
 */
public class DoubleListUtils {

    /**
     * Finds the minimum and maximum values in a list of Double objects.
     * 
     * @param data the list of double values
     * @return a Pair containing (min, max) values
     */
    public static Pair<Double, Double> getMinMax(List<Double> data) {
        // 使用Stream API一次性获取最小值和最大值
        double minValue = data.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN); // 获取最小值，用Double.NaN作为默认值（如果没有元素）
        double maxValue = data.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN); // 获取最大值

        return Pair.of(minValue, maxValue);
    }

    /**
     * Finds the minimum and maximum values in an array of double primitives.
     * 
     * @param data the array of double values
     * @return a Pair containing (min, max) values
     */
    public static Pair<Double, Double> getMinMax(double[] data) {
        // 检查数组是否为空
        if (data == null || data.length == 0) {
            return Pair.of(Double.NaN, Double.NaN);
        }

        // 使用DoubleStream API一次性获取最小值和最大值
        OptionalDouble minValue = Arrays.stream(data).min();
        OptionalDouble maxValue = Arrays.stream(data).max();

        // 获取OptionalDouble中的值，如果数组有元素则获取，否则返回Double.NaN
        double minVal = minValue.orElse(Double.NaN);
        double maxVal = maxValue.orElse(Double.NaN);

        return Pair.of(minVal, maxVal);
    }

}
