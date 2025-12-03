package top.signature;

/**
 * Top-level interface for module signature metadata in the eGPS system.
 * 
 * <p>
 * This interface defines the contract for all modules to provide descriptive information
 * about their functionality and display properties. It serves as a metadata provider for
 * GUI components and documentation generation.
 * </p>
 * 
 * <p>
 * All functional modules in the eGPS-Base library should implement this interface through
 * their respective {@code ZzzModuleSignature} classes to ensure consistent module identification
 * and integration with the GUI framework.
 * </p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * public class ZzzModuleSignature implements IModuleSignature {
 *     &#64;Override
 *     public String getShortDescription() {
 *         return "Convenient tools for FASTA file I/O operations";
 *     }
 *     
 *     &#64;Override
 *     public String getTabName() {
 *         return "FASTA IO";
 *     }
 * }
 * </pre>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 */
public interface IModuleSignature {

	/**
	 * Returns a short description of the module's functionality.
	 * 
	 * <p>
	 * This description is typically displayed as a tooltip in GUI components
	 * and should provide a clear, concise explanation of what the module does.
	 * HTML formatting is supported for enhanced presentation.
	 * </p>
	 * 
	 * @return a human-readable description of the module (HTML tags allowed)
	 */
	String getShortDescription();

	/**
	 * Returns the display name for this module.
	 * 
	 * <p>
	 * This name is used as the tab title in GUI components that organize modules
	 * in a tabbed interface. It should be short, descriptive, and user-friendly.
	 * </p>
	 * 
	 * <p>
	 * <strong>Note:</strong> The method is named {@code getTabName()} rather than
	 * {@code getName()} to avoid conflicts with {@link java.awt.Component#getName()}
	 * which is already defined in Swing's JComponent hierarchy.
	 * </p>
	 * 
	 * @return the tab/display name for this module
	 */
	String getTabName();
}
