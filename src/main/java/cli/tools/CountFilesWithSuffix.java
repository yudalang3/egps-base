package cli.tools;

import org.apache.commons.lang3.mutable.MutableInt;

import java.io.IOException;
import java.nio.file.*;

/**
 * Command-line utility to count files with a specific suffix in a directory.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Suffix Filtering:</strong> Count only files matching the specified suffix</li>
 *   <li><strong>Non-Recursive:</strong> Searches only the specified directory (not subdirectories)</li>
 *   <li><strong>File-Only Counting:</strong> Ignores directories matching the pattern</li>
 *   <li><strong>Efficient Counting:</strong> Uses MutableInt to avoid Integer boxing/unboxing</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * java cli.tools.CountFilesWithSuffix .fasta /path/to/sequences
 * // Output: Number of files ending with '.fasta': 42
 *
 * java cli.tools.CountFilesWithSuffix .nwk /data/trees
 * // Output: Number of files ending with '.nwk': 15
 * </pre>
 *
 * <h2>Command-Line Arguments:</h2>
 * <ul>
 *   <li><strong>args[0]:</strong> File suffix to match (e.g., ".fasta", ".txt", ".nwk")</li>
 *   <li><strong>args[1]:</strong> Folder path to search</li>
 * </ul>
 *
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Count sequence files before batch processing</li>
 *   <li>Verify data collection completeness</li>
 *   <li>Audit directory contents by file type</li>
 *   <li>Quick file inventory generation</li>
 * </ul>
 *
 * <h2>Error Handling:</h2>
 * <ul>
 *   <li>Validates argument count (requires exactly 2)</li>
 *   <li>Verifies path is a valid directory</li>
 *   <li>Catches IOException and DirectoryIteratorException</li>
 * </ul>
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class CountFilesWithSuffix {

    /**
     * The entry point of the application.
     * 
     * @param args Command line arguments: [0] suffix, [1] folder path
     */
    public static void main(String[] args) {
        // Check if the correct number of arguments have been provided
        if (args.length != 2) {
            System.out.println("Usage: java CountFilesWithSuffix <suffix> <folder path>");
            return;
        }

        String suffix = args[0];
        Path folderPath = Paths.get(args[1]);

        // Check if the provided path is a directory and exists
        if (!Files.isDirectory(folderPath)) {
            System.out.println("The provided path is not a valid directory.");
            return;
        }

        MutableInt countOfFiles = new MutableInt(0);

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, "*" + suffix)) {
			for (Path entry : stream) {
				// Only count files, ignore directories that match the pattern
				if (Files.isRegularFile(entry)) {
                    countOfFiles.increment();
                }
			}
		} catch (IOException | DirectoryIteratorException e) {
			// IOException can never be thrown by the iteration.
			// In this snippet, it can only be thrown by newDirectoryStream.
			// DirectoryIteratorException could be thrown if an I/O error occurs while
			// iterating over the directory.
            System.err.println("An error occurred while traversing the directory: " + e.getMessage());
        }

		// Print the result
		System.out.printf("Number of files ending with '%s': %d%n", suffix, countOfFiles.intValue());
    }
}