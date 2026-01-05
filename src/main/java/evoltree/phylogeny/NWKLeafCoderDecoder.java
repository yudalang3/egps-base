package evoltree.phylogeny;

import evoltree.struct.io.BasicLeafCoderDecoder;

/**
 * Encoder/Decoder for leaf nodes in Newick (NWK) format.
 * 
 * <p>
 * This class handles the parsing and formatting of leaf nodes (terminal nodes)
 * in phylogenetic trees according to the Newick format specification. It extends
 * {@link BasicLeafCoderDecoder} and provides DefaultPhyNode-specific creation.
 * </p>
 * 
 * <h2>Leaf Node Format:</h2>
 * <p>Leaf nodes in Newick format typically appear as:</p>
 * <pre>
 * species_name:0.123
 * </pre>
 * <p>Where the name is followed by a colon and the branch length.</p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Node Creation:</strong> Creates DefaultPhyNode instances</li>
 *   <li><strong>Name Parsing:</strong> Extracts species/gene names</li>
 *   <li><strong>Branch Length:</strong> Parses branch lengths</li>
 *   <li><strong>Standard Format:</strong> Follows Newick specification</li>
 * </ul>
 * 
 * <h2>Usage:</h2>
 * <p>
 * This class is typically used internally by {@link PhyloTreeEncoderDecoder}
 * and is not called directly by end users.
 * </p>
 * 
 * <h2>Example:</h2>
 * <pre>
 * // Used internally by tree decoder
 * NWKLeafCoderDecoder<DefaultPhyNode> leafCoder = new NWKLeafCoderDecoder<>();
 * DefaultPhyNode leaf = leafCoder.createNode();
 * leafCoder.parseNode(leaf, "human:0.5");
 * </pre>
 * 
 * @param <T> the type of phylogenetic node (DefaultPhyNode or subclass)
 * @author yudalang
 * @version 1.0
 * @since 2020-09-25
 * @see BasicLeafCoderDecoder
 * @see NWKInternalCoderDecoder
 * @see DefaultPhyNode
 */
public class NWKLeafCoderDecoder<T extends DefaultPhyNode> extends BasicLeafCoderDecoder<T> {

	/**
	 * Creates a new DefaultPhyNode instance for leaf nodes.
	 * 
	 * <p>
	 * This method is called during tree parsing to instantiate leaf nodes.
	 * It returns a DefaultPhyNode which can hold species name and branch length.
	 * </p>
	 * 
	 * @return a new DefaultPhyNode instance
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T createNode() {
		return (T) new DefaultPhyNode();
	}
}
