package blast.parse;

import top.signature.IModuleSignature;

/**
 * Module signature for the BLAST result parsing utilities.
 * 
 * <p>
 * This class implements the {@link IModuleSignature} interface to provide
 * module identification and metadata for the BLAST (Basic Local Alignment Search Tool)
 * parsing functionality within the eGPS system. It defines the module's purpose and
 * display name for GUI integration and system identification.
 * </p>
 * 
 * <h2>Module Purpose:</h2>
 * <p>
 * The BLAST parser module provides comprehensive tools for parsing and processing
 * BLAST search results, which are fundamental for biological sequence similarity
 * searches. This module handles various BLAST output formats and extracts meaningful
 * information for downstream analysis.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Multiple BLAST Formats:</strong> Support for various BLAST output formats (text, XML, tabular)</li>
 *   <li><strong>HSP Processing:</strong> High-Scoring Pair extraction and analysis</li>
 *   <li><strong>Statistical Data:</strong> E-value, bit score, and identity parsing</li>
 *   <li><strong>Multiple Alignment:</strong> BLASTN, BLASTP, BLASTX, and TBLASTN support</li>
 *   <li><strong>Performance Optimization:</strong> Efficient parsing for large result sets</li>
 * </ul>
 * 
 * <h2>Integration Points:</h2>
 * <ul>
 *   <li><strong>GUI Integration:</strong> Tab name for user interface display</li>
 *   <li><strong>Module Registry:</strong> System-wide module identification</li>
 *   <li><strong>Documentation:</strong> Help text and module descriptions</li>
 *   <li><strong>Workflow Integration:</strong> Module discovery in analysis pipelines</li>
 * </ul>
 * 
 * <h2>Related Classes:</h2>
 * <ul>
 *   <li>{@link BlastHspRecord}: High-Scoring Pair record representation</li>
 *   <li>{@code blast.*}: Related BLAST processing modules</li>
 *   <li>{@link utils.EGPSFileUtil}: File utility functions</li>
 * </ul>
 * 
 * @see IModuleSignature
 * @see BlastHspRecord
 * @author eGPS Development Team
 * @since 1.0
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for blast results parser";
    }

    @Override
    public String getTabName() {
        return "Blast Parser";
    }

}
