package cli.tools;

import com.google.common.base.Strings;
import evoltree.phylogeny.DefaultPhyNode;
import evoltree.phylogeny.PhyloTreeEncoderDecoder;
import evoltree.struct.EvolNode;
import evoltree.struct.util.EvolNodeUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Command-line utility to replace spaces in phylogenetic tree node names with underscores.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Space Replacement:</strong> Converts all spaces to underscores in node names</li>
 *   <li><strong>Recursive Processing:</strong> Processes all nodes (leaf and internal) in the tree</li>
 *   <li><strong>Newick Format:</strong> Reads and writes Newick (NWK) format phylogenetic trees</li>
 *   <li><strong>Multi-Line Support:</strong> Processes multiple trees from a single input file</li>
 *   <li><strong>Preserves Structure:</strong> Maintains tree topology, branch lengths, and bootstrap values</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * java cli.tools.NodeNames4Space4Underline trees.nwk
 *
 * // Input tree:
 * // (Homo sapiens:0.1,(Pan troglodytes:0.05,Gorilla gorilla:0.06):0.02);
 *
 * // Output tree:
 * // (Homo_sapiens:0.1,(Pan_troglodytes:0.05,Gorilla_gorilla:0.06):0.02);
 * </pre>
 *
 * <h2>Command-Line Arguments:</h2>
 * <ul>
 *   <li><strong>args[0]:</strong> Input file path containing Newick format tree(s)</li>
 * </ul>
 *
 * <h2>Input Format:</h2>
 * <p>Expects Newick format phylogenetic trees, one per line. Example:</p>
 * <pre>
 * (Species A:0.1,Species B:0.2);
 * ((Taxa 1:0.05,Taxa 2:0.06):0.02,Taxa 3:0.08);
 * </pre>
 *
 * <h2>Output:</h2>
 * <p>Prints modified trees to standard output (console), with spaces replaced by underscores:</p>
 * <pre>
 * (Species_A:0.1,Species_B:0.2);
 * ((Taxa_1:0.05,Taxa_2:0.06):0.02,Taxa_3:0.08);
 * </pre>
 *
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Prepare trees for phylogenetic software requiring underscore-separated names</li>
 *   <li>Normalize node names for consistent formatting</li>
 *   <li>Fix compatibility issues with strict Newick parsers</li>
 *   <li>Batch process multiple trees for standardization</li>
 *   <li>Convert species names with spaces for bioinformatics pipelines</li>
 * </ul>
 *
 * <h2>Error Handling:</h2>
 * <ul>
 *   <li>Validates argument count (requires exactly 1)</li>
 *   <li>Verifies path is a file (not directory)</li>
 *   <li>Catches invalid Newick format with descriptive error message</li>
 *   <li>Throws RuntimeException on parse failure</li>
 * </ul>
 *
 * <h2>Algorithm:</h2>
 * <ol>
 *   <li>Read all lines from input file</li>
 *   <li>For each line, parse as Newick tree</li>
 *   <li>Recursively iterate through all nodes</li>
 *   <li>Replace spaces with underscores in node names</li>
 *   <li>Encode modified tree back to Newick format</li>
 *   <li>Print to console</li>
 * </ol>
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class NodeNames4Space4Underline {
    
    /**
     * Recursively replaces spaces with underscores in node names of a phylogenetic tree.
     * 
     * @param decode the root node of the phylogenetic tree
     */
    private static void nodeNameChange(EvolNode decode) {
        EvolNodeUtil.recursiveIterateTreeIF(decode, node -> {
            String name = node.getName();
            if (!Strings.isNullOrEmpty(name)){
                String replace = name.replace(' ', '_');
                node.setName(replace);
            }
        });
    }

    /**
     * The entry point of the application.
     * 
     * @param args command line arguments: [0] input file path
     * @throws IOException if there is an error reading the input file
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java NodeNames4Space4Underline /input/file/path");
            return;
        }

        Path filePath = Paths.get(args[0]);
        // Check if the provided path is a directory and exists
        if (Files.isDirectory(filePath)) {
            System.out.println("Please input a path");
            return;
        }

        PhyloTreeEncoderDecoder phyloTreeEncoderDecoder = new PhyloTreeEncoderDecoder();
        // Get all contents of file

        List<String> contents = Files.readAllLines(filePath);
        for (String line : contents) {
            try {
                DefaultPhyNode decode = phyloTreeEncoderDecoder.decode(line);
                nodeNameChange(decode);
                String encode = phyloTreeEncoderDecoder.encode(decode);
                System.out.println(encode);
            } catch (Exception e) {
                System.out.println("The provided file is not in NWK format.");
                throw new RuntimeException(e);
            }
        }
    }
}