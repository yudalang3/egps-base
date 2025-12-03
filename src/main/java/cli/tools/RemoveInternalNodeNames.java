package cli.tools;

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
 * Command-line utility to remove internal node names from phylogenetic trees.
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Internal Node Cleanup:</strong> Removes names from all internal (non-leaf) nodes</li>
 *   <li><strong>Leaf Preservation:</strong> Keeps all leaf node names intact</li>
 *   <li><strong>Newick Format:</strong> Reads and writes Newick (NWK) format phylogenetic trees</li>
 *   <li><strong>Multi-Line Support:</strong> Processes multiple trees from a single input file</li>
 *   <li><strong>Structure Preservation:</strong> Maintains tree topology, branch lengths, and bootstrap values</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * java cli.tools.RemoveInternalNodeNames trees.nwk
 *
 * // Input tree:
 * // (Species_A:0.1,(Species_B:0.05,Species_C:0.06)Node1:0.02)Root;
 *
 * // Output tree:
 * // (Species_A:0.1,(Species_B:0.05,Species_C:0.06):0.02);
 * </pre>
 *
 * <h2>Command-Line Arguments:</h2>
 * <ul>
 *   <li><strong>args[0]:</strong> Input file path containing Newick format tree(s)</li>
 * </ul>
 *
 * <h2>Input Format:</h2>
 * <p>Expects Newick format phylogenetic trees, one per line. Internal nodes may have names:</p>
 * <pre>
 * (Leaf1:0.1,(Leaf2:0.05,Leaf3:0.06)InternalNode:0.02)RootNode;
 * </pre>
 *
 * <h2>Output:</h2>
 * <p>Prints modified trees to standard output with internal node names removed:</p>
 * <pre>
 * (Leaf1:0.1,(Leaf2:0.05,Leaf3:0.06):0.02);
 * </pre>
 *
 * <h2>Node Type Detection:</h2>
 * <ul>
 *   <li><strong>Internal Node:</strong> Any node with childCount &gt; 0 (has descendants)</li>
 *   <li><strong>Leaf Node:</strong> Any node with childCount == 0 (terminal node)</li>
 * </ul>
 *
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Remove bootstrap values mistakenly stored as internal node names</li>
 *   <li>Clean trees for phylogenetic software that doesn't support internal names</li>
 *   <li>Prepare trees for visualization tools requiring specific formats</li>
 *   <li>Standardize tree format for consistency across analyses</li>
 *   <li>Remove temporary labels added during tree construction</li>
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
 *   <li>For each internal node (childCount &gt; 0), set name to empty string</li>
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
public class RemoveInternalNodeNames {
    /**
     * Removes names from internal nodes in a phylogenetic tree.
     * 
     * @param decode the root node of the phylogenetic tree
     */
    private static void removeInternalNodeNames(EvolNode decode) {
        EvolNodeUtil.recursiveIterateTreeIF(decode, node -> {
            if (node.getChildCount() > 0) { // Check if the node is an internal node
                node.setName(""); // Remove the name of the internal node
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
            System.out.println("Usage: java RemoveInternalNodeNames /input/file/path");
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
                removeInternalNodeNames(decode);
                String encode = phyloTreeEncoderDecoder.encode(decode);
                System.out.println(encode);
            } catch (Exception e) {
                System.out.println("The provided file is not in NWK format.");
                throw new RuntimeException(e);
            }
        }
    }
}