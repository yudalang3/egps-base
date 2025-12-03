package evoltree.struct.io;

import evoltree.struct.ArrayBasedNode;

/**
 * Basic encoder/decoder for leaf nodes in phylogenetic trees.
 * 
 * <p>
 * This class provides a straightforward implementation for encoding and decoding leaf
 * (terminal) nodes in Newick format phylogenetic trees. Leaf nodes represent the tips
 * of the tree, typically corresponding to species, genes, or sequences.
 * </p>
 * 
 * <h2>Newick Format for Leaf Nodes</h2>
 * <p>
 * Leaf nodes in Newick format typically appear as:
 * </p>
 * <pre>
 * species_name:branch_length
 * 
 * Examples:
 * Homo_sapiens:0.15
 * Human:0.2
 * Species_A:0.05
 * taxon_1            // no branch length specified
 * </pre>
 * 
 * <h2>Key Features</h2>
 * <ul>
 *   <li><strong>Name Parsing:</strong> Extracts species/taxon names from leaf nodes</li>
 *   <li><strong>Branch Length:</strong> Parses branch length values when present</li>
 *   <li><strong>Flexible Format:</strong> Handles nodes with or without branch lengths</li>
 *   <li><strong>Node Creation:</strong> Creates ArrayBasedNode instances for leaves</li>
 *   <li><strong>StringBuilder Optimization:</strong> Reuses StringBuilder for efficient encoding</li>
 * </ul>
 * 
 * <h2>Parsing Rules</h2>
 * <ul>
 *   <li>Leaf name comes before the colon (:)</li>
 *   <li>Branch length comes after the colon (optional)</li>
 *   <li>Empty strings result in empty name</li>
 *   <li>Missing branch length is not set (retains default value)</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * BasicLeafCoderDecoder&lt;ArrayBasedNode&gt; codec = new BasicLeafCoderDecoder&lt;&gt;();
 * 
 * // Create and parse leaf node
 * ArrayBasedNode leaf = codec.createNode();
 * codec.parseNode(leaf, "Homo_sapiens:0.15");
 * System.out.println(leaf.getName());     // "Homo_sapiens"
 * System.out.println(leaf.getLength());   // 0.15
 * 
 * // Encode leaf node to string
 * leaf.setName("Pan_troglodytes");
 * leaf.setLength(0.2);
 * String encoded = codec.codeNode(leaf);  // "Pan_troglodytes:0.2"
 * </pre>
 * 
 * <h2>Integration with Tree Parsing</h2>
 * <p>
 * This class is typically used as a component in larger tree parsing systems:
 * </p>
 * <ul>
 *   <li>Used by {@link PrimaryNodeTreeDecoder} during tree reconstruction</li>
 *   <li>Works with {@link evoltree.struct.TreeCoder} for tree encoding</li>
 *   <li>Paired with {@link BasicInternalCoderDecoder} for complete tree support</li>
 * </ul>
 * 
 * <h2>Performance Characteristics</h2>
 * <ul>
 *   <li><strong>Parsing:</strong> O(n) where n is the string length</li>
 *   <li><strong>Encoding:</strong> O(1) with StringBuilder reuse</li>
 *   <li><strong>Memory:</strong> Minimal overhead with StringBuilder caching</li>
 * </ul>
 * 
 * <h2>String Building Optimization</h2>
 * <p>
 * This class maintains a reusable StringBuilder instance ({@code sbuilder}) to minimize
 * object creation during encoding operations. The buffer is reset between encoding calls
 * using {@code setLength(0)}.
 * </p>
 *
 * @param <T> the type of node (must extend ArrayBasedNode)
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see AbstractNodeCoderDecoder For the base codec interface
 * @see BasicInternalCoderDecoder For internal node encoding/decoding
 * @see ArrayBasedNode For the node implementation
 */
public class BasicLeafCoderDecoder<T extends ArrayBasedNode> extends AbstractNodeCoderDecoder<T> {

	private StringBuilder sbuilder = new StringBuilder();

	
	@Override
	public void parseNode(T node, String str) {
		// be aware of split!
		if (str.isEmpty()) {
			node.setName("");
		} else {
			String[] split = str.split(":", -1);
			node.setName(split[0]);
			if (split.length > 1) {
				if (split[1].length() > 0) {
					node.setLength(Double.parseDouble(split[1]));
				}
			}
		}

	}

	@Override
	public String codeNode(T node) {
		sbuilder.setLength(0);
		sbuilder.append(node.getName());
		sbuilder.append(":");
		sbuilder.append(convertRate2Decimal(node.getLength()));

		return sbuilder.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createNode() {
		return (T) new ArrayBasedNode();
	}

}