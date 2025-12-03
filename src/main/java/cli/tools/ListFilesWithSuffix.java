package cli.tools;

import org.apache.commons.compress.utils.Lists;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Command-line utility to list files with a specific suffix and optionally write to a TSV file.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Suffix Filtering:</strong> List only files matching the specified suffix</li>
 *   <li><strong>Non-Recursive:</strong> Searches only the specified directory (not subdirectories)</li>
 *   <li><strong>File-Only Listing:</strong> Ignores directories matching the pattern</li>
 *   <li><strong>Dual Output:</strong> Console output or file export (TSV format)</li>
 *   <li><strong>TSV Export:</strong> Includes "FileName" header for spreadsheet compatibility</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 *
 * <h3>Console Output:</h3>
 * <pre>
 * java cli.tools.ListFilesWithSuffix .fasta /path/to/sequences
 * // Output:
 * // FileName
 * // sequence1.fasta
 * // sequence2.fasta
 * // ...
 * // Number of files ending with '.fasta': 42
 * </pre>
 *
 * <h3>File Export:</h3>
 * <pre>
 * java cli.tools.ListFilesWithSuffix .nwk /data/trees output.tsv
 * // Creates output.tsv with:
 * // FileName
 * // tree1.nwk
 * // tree2.nwk
 * // ...
 * </pre>
 *
 * <h2>Command-Line Arguments:</h2>
 * <ul>
 *   <li><strong>args[0]:</strong> File suffix to match (e.g., ".fasta", ".txt", ".nwk")</li>
 *   <li><strong>args[1]:</strong> Folder path to search</li>
 *   <li><strong>args[2]:</strong> (Optional) Output file path for TSV export</li>
 * </ul>
 *
 * <h2>Output Format:</h2>
 * <p>When writing to file, creates a TSV with:</p>
 * <ul>
 *   <li><strong>Header:</strong> "FileName"</li>
 *   <li><strong>Content:</strong> One filename per line (no path, just filename)</li>
 * </ul>
 *
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Generate file inventory for batch processing scripts</li>
 *   <li>Create input lists for pipeline tools</li>
 *   <li>Audit directory contents by file type</li>
 *   <li>Export file lists for documentation</li>
 *   <li>Prepare manifest files for data transfer</li>
 * </ul>
 *
 * <h2>Error Handling:</h2>
 * <ul>
 *   <li>Validates argument count (requires at least 2)</li>
 *   <li>Verifies path is a valid directory</li>
 *   <li>Catches IOException for directory traversal and file writing</li>
 * </ul>
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class ListFilesWithSuffix {

    /**
     * The entry point of the application.
     * 
     * @param args Command line arguments: [0] suffix, [1] folder path, [2] optional output file path
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ListFilesWithSuffix <suffix> <folder path> [output file path]");
            return;
        }

        String suffix = args[0];
        Path folderPath = Paths.get(args[1]);

        String outputFilePath = args.length > 2 ? args[2] : null;
        List<String> outputs = Lists.newArrayList();
        outputs.add("FileName");

        // Check if the provided path is a directory and exists
        if (!Files.isDirectory(folderPath)) {
            System.out.println("The provided path is not a valid directory.");
            return;
        }


		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, "*" + suffix)) {
			for (Path entry : stream) {
				if (Files.isRegularFile(entry)) {
                    outputs.add(entry.getFileName().toString());
                }
			}
		} catch (IOException | DirectoryIteratorException e) {
            System.err.println("An error occurred while traversing the directory: " + e.getMessage());
        }

		// Print the result
		System.out.printf("Number of files ending with '%s': %d%n", suffix, outputs.size());
        if (outputFilePath != null){
            // output the outputs to a file
            try {
                Files.write(Paths.get(outputFilePath), outputs);
            } catch (IOException e) {
                System.err.println("An error occurred while writing to the file: " + e.getMessage());
            }
        }else {
            for (String output : outputs){
                System.out.println(output);
            }
        }
    }
}