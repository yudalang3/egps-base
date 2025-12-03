package msaoperator;


import org.apache.commons.lang3.tuple.Pair;

/**
 * Default implementation of DataForamtPrivateInfor interface.
 * 
 * <p>
 * This class provides a universal, format-agnostic implementation that is compatible
 * with all other format information objects. It serves as a fallback or default option
 * when specific format details are not required or when universal compatibility is desired.
 * </p>
 * 
 * <h2>Key Characteristics</h2>
 * <ul>
 *   <li><strong>Universal Compatibility</strong>: Always returns compatible with any format</li>
 *   <li><strong>Generic Format</strong>: Represents a default, non-specific data format</li>
 *   <li><strong>Fallback Usage</strong>: Used when specific format information is unavailable</li>
 *   <li><strong>Zero Constraints</strong>: Imposes no format-specific restrictions</li>
 * </ul>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li>Default format information for uninitialized MSA objects</li>
 *   <li>Testing scenarios where format compatibility is not critical</li>
 *   <li>Placeholder when specific format details are to be determined later</li>
 *   <li>Universal format adapter in mixed-format processing pipelines</li>
 * </ul>
 * 
 * <h2>Compatibility Behavior</h2>
 * <p>
 * This implementation follows a "compatibility-first" approach:
 * </p>
 * <ul>
 *   <li>Always returns {@code true} for compatibility checks</li>
 *   <li>Returns {@code null} as error message (no errors to report)</li>
 *   <li>Can be safely used with any other DataForamtPrivateInfor implementation</li>
 * </ul>
 * 
 * <h2>Design Considerations</h2>
 * <p>
 * While this implementation provides maximum flexibility, consider using specific
 * format implementations when:
 * </p>
 * <ul>
 *   <li>Format validation is required</li>
 *   <li>Specific format constraints need to be enforced</li>
 *   <li>Detailed error reporting is necessary</li>
 *   <li>Format-specific optimizations are desired</li>
 * </ul>
 * 
 * <h2>Example Usage</h2>
 * <pre>
 * // Create default format info
 * DataForamtPrivateInfor defaultInfo = new DefaultDataFormatPrivateInfor();
 * 
 * // Check compatibility with any other format
 * DataForamtPrivateInfor fastaInfo = new FastaFormatInfo();
 * Pair&lt;Boolean, String&gt; result = defaultInfo.isCompatiable(fastaInfo);
 * // result.getLeft() == true (always compatible)
 * // result.getRight() == null (no error message)
 * 
 * // Get format name
 * String formatName = defaultInfo.getFormatName();
 * // Returns: "default data format private information"
 * </pre>
 * 
 * <h2>Thread Safety</h2>
 * <p>
 * This class is thread-safe as it contains no mutable state and all operations
 * are pure functions with deterministic behavior.
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see DataForamtPrivateInfor Interface definition
 * @see msaoperator.io MSA I/O operations that use format information
 */
public class DefaultDataFormatPrivateInfor implements DataForamtPrivateInfor{

	@Override
	public Pair<Boolean, String> isCompatiable(DataForamtPrivateInfor anotherInfor) {
		return Pair.of(true, null);
	}

	@Override
	public String getFormatName() {
		return "default data format private information ";
	}

}
