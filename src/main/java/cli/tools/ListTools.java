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
 * <p>The registry includes these command-line utilities:</p>
 * <ol>
 *   <li><strong>ClipboardPathNormalized</strong> - Convert file paths to cross-platform format</li>
 *   <li><strong>CountFilesWithSuffix</strong> - Count files matching specific extensions</li>
 *   <li><strong>ListFilesWithSuffix</strong> - List and export files with specific extensions</li>
 * </ol>
 * 
 * <h2>Output Format</h2>
 * <pre>
 * Current available programs are:
 * 1       cli.tools.ClipboardPathNormalized    Convert the file path C:\a\b\c.txt to /
 * 2       cli.tools.CountFilesWithSuffix       Count the files with certain suffix.
 * 3       cli.tools.ListFilesWithSuffix        List the names of files in a directory...
 * </pre>
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
 * @see ClipboardPathNormalized For path normalization utility
 * @see CountFilesWithSuffix For file counting utility
 * @see ListFilesWithSuffix For file listing utility
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
		addEntry(ClipboardPathNormalized.class.getName(), "Convert the file path C:\\a\\b\\c.txt to /");
		addEntry(CountFilesWithSuffix.class.getName(), "Count the files with certain suffix.");
		addEntry(ListFilesWithSuffix.class.getName(), "List the names of files in a directory that end with a specified suffix and generate a TSV file.");

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
