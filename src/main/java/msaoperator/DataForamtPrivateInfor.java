package msaoperator;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Interface defining the contract for multiple sequence alignment (MSA) format private information.
 * 
 * <p>
 * This interface provides a standardized way to handle format-specific metadata and compatibility
 * checks for different MSA file formats. It enables the system to determine whether different
 * MSA formats can be safely merged or processed together.
 * </p>
 * 
 * <h2>Core Responsibilities</h2>
 * <ul>
 *   <li><strong>Format Identification</strong>: Provide unique format names for MSA types</li>
 *   <li><strong>Compatibility Checking</strong>: Determine if different formats can be combined</li>
 *   <li><strong>Error Reporting</strong>: Provide detailed error messages for incompatibilities</li>
 * </ul>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li>Validating format compatibility before merging MSA files</li>
 *   <li>Providing format-specific metadata for processing pipelines</li>
 *   <li>Enabling dynamic format detection and validation</li>
 *   <li>Supporting format conversion workflows</li>
 * </ul>
 * 
 * <h2>Implementation Guidelines</h2>
 * <p>
 * Classes implementing this interface should:
 * </p>
 * <ol>
 *   <li>Provide clear, descriptive format names via {@code getFormatName()}</li>
 *   <li>Implement thorough compatibility logic in {@code isCompatible()}</li>
 *   <li>Return meaningful error messages for incompatibility scenarios</li>
 *   <li>Consider format-specific constraints (sequence length, character sets, etc.)</li>
 * </ol>
 * 
 * <h2>Example Implementation</h2>
 * <pre>
 * public class FastaFormatInfo implements DataForamtPrivateInfor {
 *     
 *     &#64;Override
 *     public Pair&lt;Boolean, String&gt; isCompatible(DataForamtPrivateInfor anotherInfor) {
 *         if (anotherInfor instanceof FastaFormatInfo) {
 *             return Pair.of(true, "Compatible FASTA formats");
 *         } else {
 *             return Pair.of(false, "FASTA format incompatible with " + anotherInfor.getFormatName());
 *         }
 *     }
 *     
 *     &#64;Override
 *     public String getFormatName() {
 *         return "FASTA";
 *     }
 * }
 * </pre>
 * 
 * <h2>Compatibility Strategy</h2>
 * <p>
 * The compatibility checking should consider:
 * </p>
 * <ul>
 *   <li>Format family (e.g., all FASTA variants are compatible)</li>
 *   <li>Character set compatibility</li>
 *   <li>Sequence length constraints</li>
 *   <li>Metadata preservation requirements</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see DefaultDataFormatPrivateInfor Default implementation
 * @see msaoperator.io MSA I/O operations
 */
public interface DataForamtPrivateInfor {
	
	/**
	 * 传入另一个 DataForamtPrivateInfor，判断两者是否兼容！
	 * @param anotherInfor
	 * @return 第一个是否兼容；若不兼容第二个返回描述错误的String!
	 */
	Pair<Boolean, String> isCompatiable(DataForamtPrivateInfor anotherInfor);

	String getFormatName();
}
