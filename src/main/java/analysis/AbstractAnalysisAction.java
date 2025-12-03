package analysis;

import com.google.common.base.Stopwatch;

/**
 * Abstract base class for implementing analysis actions in the eGPS system.
 * 
 * <p>
 * This class provides a standardized framework for creating bioinformatics analysis
 * operations with built-in support for input/output path management and execution
 * timing. It follows the Template Method pattern to ensure consistent behavior
 * across all analysis implementations.
 * </p>
 * 
 * <h2>Core Components</h2>
 * <ul>
 *   <li><strong>Input/Output Management</strong>: Standardized path handling for analysis data</li>
 *   <li><strong>Execution Framework</strong>: Abstract {@code doIt()} method for custom logic</li>
 *   <li><strong>Performance Monitoring</strong>: Built-in execution timing with {@code runIt()}</li>
 *   <li><strong>Extensibility</strong>: Template for various bioinformatics analysis types</li>
 * </ul>
 * 
 * <h2>Design Pattern</h2>
 * <p>
 * Implements the Template Method pattern:
 * <ul>
 *   <li>Defines the overall analysis structure (paths, timing)</li>
 *   <li>Leaves specific implementation to subclasses via {@code doIt()}</li>
 *   <li>Provides convenience methods for common operations</li>
 * </ul>
 * </p>
 * 
 * <h2>Usage Example</h2>
 * <pre>
 * public class CustomAnalysis extends AbstractAnalysisAction {
 *     
 *     &#64;Override
 *     public void doIt() throws Exception {
 *         // Custom analysis logic here
 *         processData(getInputPath());
 *         writeResults(getOutputPath());
 *     }
 *     
 *     private void processData(String inputPath) { ... }
 *     private void writeResults(String outputPath) { ... }
 * }
 * 
 * // Usage
 * CustomAnalysis analysis = new CustomAnalysis();
 * analysis.setInputPath("/data/input.fasta");
 * analysis.setOutputPath("/results/output.txt");
 * analysis.runIt(); // Executes with timing
 * </pre>
 * 
 * <h2>Key Methods</h2>
 * <ul>
 *   <li>{@code doIt()} - Abstract method for custom analysis implementation</li>
 *   <li>{@code runIt()} - Convenience method with automatic timing</li>
 *   <li>Getters/Setters for input and output paths</li>
 * </ul>
 * 
 * <h2>Typical Use Cases</h2>
 * <ul>
 *   <li>Sequence analysis workflows</li>
 *   <li>Phylogenetic tree processing</li>
 *   <li>Statistical computations on biological data</li>
 *   <li>Data format conversion operations</li>
 *   <li>Custom bioinformatics algorithms</li>
 * </ul>
 * 
 * <h2>Performance Considerations</h2>
 * <ul>
 *   <li>Uses Guava's Stopwatch for accurate timing measurements</li>
 *   <li>Minimal overhead for analysis operations</li>
 *   <li>Thread-safe for individual analysis instances</li>
 * </ul>
 * 
 * @author yudal
 * @version 1.0
 * @since 1.0
 * @see com.google.common.base.Stopwatch For timing functionality
 * @see analysis.math Mathematical tools for analysis
 */
public abstract class AbstractAnalysisAction {

	protected String inputPath;
	protected String outputPath;

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	/**
	 * 设置好参数之后，执行的动作。
	 * 
	 * @throws Exception
	 */
	public abstract void doIt() throws Exception;

	/**
	 * 具有运行时间的快捷调用方式
	 * 
	 * @throws Exception
	 */
	public void runIt() throws Exception {
		Stopwatch stopwatch = Stopwatch.createStarted();
		doIt();
		stopwatch.stop();
		System.out.println("Elapsed time: ".concat(stopwatch.toString()));
	}

	protected void check() {
		if (inputPath == null) {
			throw new IllegalArgumentException("Please enter input path first.");
		}

		if (outputPath == null) {
			throw new IllegalArgumentException("Please enter output path first.");
		}

	}

}
