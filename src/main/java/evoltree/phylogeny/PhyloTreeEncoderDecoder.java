package evoltree.phylogeny;

import evoltree.struct.TreeCoder;
import evoltree.struct.io.PrimaryNodeTreeDecoder;

/**
 * Comprehensive Newick format encoder/decoder for phylogenetic tree structures.
 * 
 * <p>
 * This class provides bidirectional conversion between phylogenetic tree objects
 * and the standardized Newick (NWK) format, which is the most widely used format
 * for representing phylogenetic trees in computational biology and bioinformatics.
 * </p>
 * 
 * <h2>Newick Format Overview</h2>
 * <p>
 * The Newick format represents phylogenetic trees as nested parenthetical structures
 * where each node is represented by its name (if any), branch length (if specified),
 * and relationship to other nodes. This format is supported by virtually all
 * phylogenetic analysis software and databases.
 * </p>
 * 
 * <h3>Newick Format Structure:</h3>
 * <pre>
 * (leaf1:branch_length,leaf2:branch_length)internal_node:branch_length;
 * 
 * // Example:
 * (Homo_sapiens:0.2,Pan_troglodytes:0.3)Homininae:0.1;
 * </pre>
 * 
 * <h2>Key Components</h2>
 * <ul>
 *   <li><strong>Leaf Nodes:</strong> Represent terminal taxa (species, sequences, etc.)</li>
 *   <li><strong>Internal Nodes:</strong> Represent common ancestors and evolutionary splits</li>
 *   <li><strong>Branch Lengths:</strong> Represent evolutionary distance or time</li>
 *   <li><strong>Node Names:</strong> Optional labels for identification and reference</li>
 * </ul>
 * 
 * <h2>Encoding Process</h2>
 * <p>
 * The encoding process recursively traverses the tree structure and generates
 * a Newick string representation:
 * </p>
 * <ol>
 *   <li><strong>Leaf Processing:</strong> Handle terminal nodes with their names and branch lengths</li>
 *   <li><strong>Internal Node Processing:</strong> Handle ancestor nodes with their children</li>
 *   <li><strong>Parentheses Generation:</strong> Create proper nesting structure</li>
 *   <li><strong>String Assembly:</strong> Combine all components into valid Newick format</li>
 * </ol>
 * 
 * <h2>Decoding Process</h2>
 * <p>
 * The decoding process parses Newick strings and reconstructs tree objects:
 * </p>
 * <ol>
 *   <li><strong>Tokenization:</strong> Break input string into manageable components</li>
 *   <li><strong>Tree Construction:</strong> Build node hierarchy from parenthetical structure</li>
 *   <li><strong>Branch Length Assignment:</strong> Set evolutionary distances</li>
 *   <li><strong>Name Assignment:</strong> Assign labels to nodes where specified</li>
 * </ol>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * // Encoding a tree to Newick format
 * PhyloTreeEncoderDecoder codec = new PhyloTreeEncoderDecoder();
 * DefaultPhyNode tree = buildSampleTree();
 * String newickString = codec.encode(tree);
 * System.out.println(newickString);
 * // Output: ((Species_A:0.1,Species_B:0.2):0.05,Species_C:0.3);
 * 
 * // Decoding a Newick string to tree structure
 * String inputNewick = "((Homo_sapiens:0.2,Pan_troglodytes:0.3):0.1,Gorilla_gorilla:0.4);";
 * DefaultPhyNode decodedTree = codec.decode(inputNewick);
 * </pre>
 * 
 * <h2>Specialized Components</h2>
 * <p>
 * This class delegates specific encoding/decoding tasks to specialized components:
 * </p>
 * <ul>
 *   <li><strong>NWKLeafCoderDecoder:</strong> Handles leaf node encoding/decoding</li>
 *   <li><strong>NWKInternalCoderDecoder:</strong> Handles internal node encoding/decoding</li>
 *   <li><strong>TreeCoder:</strong> Orchestrates the overall encoding process</li>
 *   <li><strong>PrimaryNodeTreeDecoder:</strong> Orchestrates the overall decoding process</li>
 * </ul>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li><strong>File I/O:</strong> Save and load phylogenetic trees from disk</li>
 *   <li><strong>Data Exchange:</strong> Share trees between different software packages</li>
 *   <li><strong>Database Storage:</strong> Store trees in standardized format</li>
 *   <li><strong>Web Services:</strong> Exchange trees via APIs and web services</li>
 *   <li><strong>Analysis Integration:</strong> Bridge eGPS with external phylogenetic tools</li>
 * </ul>
 * 
 * <h2>Error Handling</h2>
 * <ul>
 *   <li><strong>Malformed Input:</strong> Throws exceptions for invalid Newick strings</li>
 *   <li><strong>Parse Errors:</strong> Provides detailed error messages for debugging</li>
 *   <li><strong>Tree Validation:</strong> Ensures decoded trees maintain valid structure</li>
 * </ul>
 * 
 * <h2>Performance Characteristics</h2>
 * <ul>
 *   <li><strong>Time Complexity:</strong> O(n) for both encoding and decoding (n = number of nodes)</li>
 *   <li><strong>Space Complexity:</strong> O(n) for tree storage and string generation</li>
 *   <li><strong>Scalability:</strong> Efficient for trees with thousands of taxa</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li><strong>Tree Visualization:</strong> Supports tree drawing and display components</li>
 *   <li><strong>Analysis Workflows:</strong> Enables tree-based computational biology pipelines</li>
 *   <li><strong>Data Import/Export:</strong> Provides standard format for external data exchange</li>
 * </ul>
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 * @see DefaultPhyNode For the tree node implementation
 * @see NWKLeafCoderDecoder For leaf node encoding/decoding
 * @see NWKInternalCoderDecoder For internal node encoding/decoding
 * @see evoltree.struct.TreeCoder For the encoding framework
 * @see evoltree.struct.io.PrimaryNodeTreeDecoder For the decoding framework
 */
public class PhyloTreeEncoderDecoder {

    /**
     * Converts a phylogenetic tree to its Newick format string representation.
     * 
     * @param tree the root node of the tree to encode
     * @return the Newick format string
     */
    public String encode(DefaultPhyNode tree) {
        TreeCoder<DefaultPhyNode> coder = new TreeCoder<>(new NWKLeafCoderDecoder<DefaultPhyNode>(), new NWKInternalCoderDecoder<DefaultPhyNode>());
        return coder.code(tree);
    }

    /**
     * Parses a Newick format string into a phylogenetic tree structure.
     * 
     * @param line the Newick format string
     * @return the root node of the parsed tree
     * @throws Exception if there is an error during parsing
     */
    public DefaultPhyNode decode(String line) throws Exception {
        PrimaryNodeTreeDecoder<DefaultPhyNode> decoder = new PrimaryNodeTreeDecoder<>(new NWKLeafCoderDecoder<DefaultPhyNode>(), new NWKInternalCoderDecoder<DefaultPhyNode>());
        return decoder.decode(line);
    }
}
