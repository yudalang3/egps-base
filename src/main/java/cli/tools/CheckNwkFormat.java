package cli.tools;

import evoltree.struct.TreeDecoder;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Command-line utility for validating Newick (NWK) format phylogenetic tree files.
 * 
 * <p>
 * This class provides a simple command-line interface to verify whether a file
 * contains valid Newick format phylogenetic tree descriptions. It validates each
 * line of the input file by attempting to parse it using the TreeDecoder component.
 * </p>
 * 
 * <h2>Newick Format Overview</h2>
 * <p>
 * The Newick format is a widely-used standard for representing phylogenetic trees
 * as parenthesized nested lists. Each tree is represented as a string of text
 * containing nested parentheses, species names, and branch lengths.
 * </p>
 * 
 * <h3>Example Newick Format:</h3>
 * <pre>
 * ((A:0.1,B:0.2):0.05,C:0.3);
 * ((Human:0.2,Chimp:0.3):0.1,Gorilla:0.4);
 * </pre>
 * 
 * <h2>Validation Process</h2>
 * <ul>
 *   <li><strong>File Reading:</strong> Reads all lines from the specified file</li>
 *   <li><strong>Line Processing:</strong> Processes each line individually</li>
 *   <li><strong>Parsing Test:</strong> Attempts to decode each line using TreeDecoder</li>
 *   <li><strong>Error Detection:</strong> Reports failure if any line cannot be parsed</li>
 * </ul>
 * 
 * <h2>Usage</h2>
 * <pre>
 * java CheckNwkFormat /path/to/trees.nwk
 * 
 * // Successful output:
 * Success!
 * 
 * // Failed output:
 * The provided file is not in NWK format.
 * </pre>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li><strong>Quality Control:</strong> Validate phylogenetic tree files before analysis</li>
 *   <li><strong>Data Import:</strong> Check tree files before importing into phylogenetic software</li>
 *   <li><strong>Pipeline Integration:</strong> Verify input files in automated workflows</li>
 *   <li><strong>Debugging:</strong> Identify malformed Newick strings in tree collections</li>
 *   <li><strong>Batch Processing:</strong> Validate multiple tree files in sequence</li>
 * </ul>
 * 
 * <h2>Error Handling</h2>
 * <ul>
 *   <li><strong>Missing Arguments:</strong> Shows usage message and exits</li>
 *   <li><strong>Directory Input:</strong> Requests file path instead of directory</li>
 *   <li><strong>Parsing Errors:</strong> Reports format failure and shows first error</li>
 *   <li><strong>I/O Errors:</strong> Propagates IOException for file system issues</li>
 * </ul>
 * 
 * <h2>Performance Characteristics</h2>
 * <ul>
 *   <li><strong>Time Complexity:</strong> O(n*m) where n = lines, m = average line length</li>
 *   <li><strong>Memory Usage:</strong> Loads entire file into memory (suitable for small-medium files)</li>
 *   <li><strong>I/O Efficiency:</strong> Uses Java NIO for optimal file reading</li>
 *   <li><strong>Early Termination:</strong> Stops at first parsing error</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li><strong>Tree Processing:</strong> Validates input for downstream phylogenetic analysis</li>
 *   <li><strong>Pipeline Validation:</strong> Quality control in computational workflows</li>
 *   <li><strong>Data Preparation:</strong> Ensures proper format before tree manipulation</li>
 * </ul>
 * 
 * <h2>Limitations</h2>
 * <ul>
 *   <li>Entire file loaded into memory (not suitable for very large files)</li>
 *   <li>Stops at first error (doesn't report all errors in file)</li>
 *   <li>Limited error reporting (doesn't indicate specific parsing issues)</li>
 * </ul>
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 * @see evoltree.struct.TreeDecoder For the underlying Newick parser
 * @see java.nio.file.Files For file I/O operations
 */
public class CheckNwkFormat {

    /**
     * Main method to check if a file is in Newick (NWK) format.
     * 
     * @param args command line arguments (single parameter: input file path)
     * @throws IOException if there is an error reading the file
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java CheckNwkFormat /input/file/path");
            return;
        }

        Path filePath = Paths.get(args[0]);
        // Check if the provided path is a directory and exists
        if (Files.isDirectory(filePath)) {
            System.out.println("Please input a path");
            return;
        }

        TreeDecoder treeDecoder = new TreeDecoder();
        // Get all contents of file

        List<String> contents = Files.readAllLines(filePath);
        for (String line : contents) {
            try {
                treeDecoder.decode(line);
            } catch (Exception e) {
                System.out.println("The provided file is not in NWK format.");
                throw new RuntimeException(e);
            }
        }

        System.out.println("Success!");
    }
}