package cli.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Command-line interface registry and documentation utility for eGPS tools.
 * 
 * <p>
 * This class serves as a comprehensive catalog and help system for all available
 * command-line tools within the eGPS bioinformatics framework. It provides a
 * centralized registry of CLI utilities, their descriptions, and usage information.
 * </p>
 * 
 * <h2>Registry System</h2>
 * <p>
 * Uses a record-based approach ({@code FileEntry}) to maintain tool information:
 * </p>
 * <ul>
 *   <li><strong>Tool Name:</strong> Fully qualified class name for unique identification</li>
 *   <li><strong>Description:</strong> Human-readable explanation of tool functionality</li>
 *   <li><strong>Indexing:</strong> Automatic numbering for easy reference</li>
 * </ul>
 * 
 * <h2>Available Tools</h2>
 * <p>The registry includes these command-line utilities grouped by functionality:</p>
 * 
 * <h3>Clipboard Utilities</h3>
 * <ul>
 *   <li><strong>ClipboardPath4Win2WSL</strong> - Convert Windows paths to WSL format via clipboard</li>
 *   <li><strong>ClipboardPathNormalized</strong> - Normalize file path separators for cross-platform use</li>
 * </ul>
 * 
 * <h3>File Operations</h3>
 * <ul>
 *   <li><strong>CountFilesWithSuffix</strong> - Count files matching specific extensions in a directory</li>
 *   <li><strong>ListFilesWithSuffix</strong> - List and export filenames with specific suffixes to TSV</li>
 * </ul>
 * 
 * <h3>Phylogenetic Tree Utilities</h3>
 * <ul>
 *   <li><strong>CheckNwkFormat</strong> - Validate Newick format phylogenetic tree files</li>
 *   <li><strong>RemoveInternalNodeNames</strong> - Clean internal node names from tree structures</li>
 *   <li><strong>NodeNames4Space4Underline</strong> - Replace spaces with underscores in node names</li>
 * </ul>
 * 
 * <h3>FASTA Sequence Analysis</h3>
 * <ul>
 *   <li><strong>FastaComparer</strong> - Calculate sequence match coverage using alignment results</li>
 *   <li><strong>PairwiseSeqDiffPrinter</strong> - Visualize pairwise sequence alignment differences</li>
 *   <li><strong>UniqueStat</strong> - Detect duplicate sequences and calculate repeat ratios</li>
 * </ul>
 * 
 * <h3>System Utilities</h3>
 * <ul>
 *   <li><strong>SeeModulesWeHave</strong> - Display all available eGPS modules and descriptions</li>
 * </ul>
 * 
 * <h2>Output Format</h2>
 * <pre>
 * Current available programs are:
 * 1    cli.tools.ClipboardPath4Win2WSL           Convert Windows file paths from clipboard to WSL format...
 * 2    cli.tools.ClipboardPathNormalized         Normalize file paths from clipboard by converting...
 * 3    cli.tools.CountFilesWithSuffix            Count the number of files in a directory that match...
 * 4    cli.tools.ListFilesWithSuffix             List all files in a directory with a specified suffix...
 * 5    cli.tools.CheckNwkFormat                  Validate whether a file contains phylogenetic trees...
 * ...</pre>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li><strong>Tool Discovery:</strong> Help users find appropriate utilities for tasks</li>
 *   <li><strong>Documentation:</strong> Central registry for CLI tool capabilities</li>
 *   <li><strong>Onboarding:</strong> Quick reference for new eGPS users</li>
 *   <li><strong>Pipeline Design:</strong> Identify tools for automated workflows</li>
 *   <li><strong>Training:</strong> Educational resource for command-line bioinformatics</li>
 * </ul>
 * 
 * <h2>Extensibility</h2>
 * <p>
 * The registry system is designed for easy extension:
 * </p>
 * <ul>
 *   <li><strong>Static Registration:</strong> New tools added via {@code addEntry()}</li>
 *   <li><strong>Record-Based Storage:</strong> Immutable entries ensure data integrity</li>
 *   <li><strong>Automatic Indexing:</strong> Tools automatically numbered in display</li>
 *   <li><strong>Consistent Formatting:</strong> Standardized output for all entries</li>
 * </ul>
 * 
 * <h2>Technical Implementation</h2>
 * <ul>
 *   <li><strong>Java Records:</strong> Uses Java 16+ record feature for type-safe entries</li>
 *   <li><strong>Concurrent Indexing:</strong> AtomicInteger for thread-safe numbering</li>
 *   <li><strong>Stream Processing:</strong> Modern Java streams for efficient output</li>
 *   <li><strong>Static Registry:</strong> Class-level tool registry shared across instances</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li><strong>Framework Discovery:</strong> Entry point for exploring eGPS capabilities</li>
 *   <li><strong>Tool Integration:</strong> Serves as registry for all CLI components</li>
 *   <li><strong>Workflow Design:</strong> Reference for building command-line pipelines</li>
 * </ul>
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 * @see ClipboardPath4Win2WSL For Windows to WSL path conversion
 * @see ClipboardPathNormalized For path normalization utility
 * @see CountFilesWithSuffix For file counting utility
 * @see ListFilesWithSuffix For file listing utility
 * @see CheckNwkFormat For Newick format validation
 * @see RemoveInternalNodeNames For tree cleaning utility
 * @see NodeNames4Space4Underline For node name formatting
 * @see SeeModulesWeHave For module discovery
 */
public class ListTools {
	record FileEntry(String name, String description) {
	}

	static List<FileEntry> listOfFiles = new ArrayList<>();

	/**
	 * The entry point of the application.
	 * 
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {
		// Clipboard utilities
		addEntry(ClipboardPath4Win2WSL.class.getName(), "Convert Windows file paths from clipboard to WSL format (e.g., C:\\Users\\... → /mnt/c/Users/...) and paste back to clipboard.");
		addEntry(ClipboardPathNormalized.class.getName(), "Normalize file paths from clipboard by converting all backslashes to forward slashes for cross-platform compatibility.");
		
		// File operations
		addEntry(CountFilesWithSuffix.class.getName(), "Count the number of files in a directory that match a specific file extension (non-recursive).");
		addEntry(ListFilesWithSuffix.class.getName(), "List all files in a directory with a specified suffix and optionally export the filenames to a TSV file for batch processing.");
		
		// Phylogenetic tree utilities
		addEntry(CheckNwkFormat.class.getName(), "Validate whether a file contains phylogenetic trees in valid Newick (NWK) format by attempting to parse each line.");
		addEntry(RemoveInternalNodeNames.class.getName(), "Remove internal node names from phylogenetic trees in Newick format while preserving leaf names, branch lengths, and tree topology.");
		addEntry(NodeNames4Space4Underline.class.getName(), "Replace all spaces in phylogenetic tree node names with underscores to ensure compatibility with phylogenetic analysis tools.");
		
		// FASTA sequence analysis
		addEntry("fasta.comparison.FastaComparer", "Compare two FASTA files using BLAST/Diamond alignment results (fmt6 format) to calculate sequence match coverage ratios.");
		addEntry("fasta.comparison.PairwiseSeqDiffPrinter", "Visualize pairwise sequence alignment differences with customizable marking modes, showing matches or mismatches and identity percentage.");
		addEntry("fasta.stat.UniqueStat", "Analyze FASTA file for duplicate sequences, reporting frequency counts and calculating the repeat ratio for quality control.");
		
		// System utilities
		addEntry(SeeModulesWeHave.class.getName(), "Display a comprehensive list of all available eGPS modules that implement the IModuleSignature interface with their descriptions.");


		System.out.println("Current available programs are:");

		// Use a more compact approach with streams and forEach
		AtomicInteger index = new AtomicInteger(1);
		listOfFiles.forEach(entry -> {
			System.out.println(index.getAndIncrement() + "\t" + entry.name() + "\t" + entry.description());
		});
	}

	/**
	 * Adds a tool entry to the list of available tools.
	 * 
	 * @param name the fully qualified class name of the tool
	 * @param description a description of the tool's functionality
	 */
	private static void addEntry(String name, String description) {
		listOfFiles.add(new FileEntry(name, description));
	}
}
