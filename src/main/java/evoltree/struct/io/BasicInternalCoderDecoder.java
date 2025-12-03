package evoltree.struct.io;

import evoltree.struct.ArrayBasedNode;

/**
 * Basic encoder/decoder for internal nodes in phylogenetic trees.
 * 
 * <p>
 * This class provides a straightforward implementation for encoding and decoding internal
 * (non-leaf) nodes in Newick format phylogenetic trees. Internal nodes represent ancestral
 * states or common ancestors in evolutionary trees and may contain names and branch lengths.
 * </p>
 * 
 * <h2>Newick Format for Internal Nodes</h2>
 * <p>
 * Internal nodes in Newick format typically appear after the closing parenthesis:
 * </p>
 * <pre>
 * (child1,child2)internal_name:branch_length
 * 
 * Examples:
 * (A,B)ancestor:0.5
 * (A,B):0.5          // unnamed internal node
 * (A,B)node1         // no branch length specified
 * </pre>
 * 
 * <h2>Key Features</h2>
 * <ul>
 *   <li><strong>Name Parsing:</strong> Extracts optional internal node names</li>
 *   <li><strong>Branch Length:</strong> Parses branch length values from string</li>
 *   <li><strong>Flexible Format:</strong> Handles nodes with or without names/lengths</li>
 *   <li><strong>Node Creation:</strong> Creates ArrayBasedNode instances</li>
 * </ul>
 * 
 * <h2>Parsing Rules</h2>
 * <ul>
 *   <li>Node name comes before the colon (:)</li>
 *   <li>Branch length comes after the colon</li>
 *   <li>If no colon present, only node name is parsed</li>
 *   <li>Empty strings are handled gracefully</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * BasicInternalCoderDecoder&lt;ArrayBasedNode&gt; codec = new BasicInternalCoderDecoder&lt;&gt;();
 * 
 * // Create and parse node
 * ArrayBasedNode node = codec.createNode();
 * codec.parseNode(node, "ancestor:0.5");
 * System.out.println(node.getName());     // "ancestor"
 * System.out.println(node.getLength());   // 0.5
 * 
 * // Encode node back to string
 * node.setName("root");
 * node.setLength(1.2);
 * String encoded = codec.codeNode(node);  // "root:1.2"
 * </pre>
 * 
 * <h2>Integration with Tree Parsing</h2>
 * <p>
 * This class is typically used as a component in larger tree parsing systems:
 * </p>
 * <ul>
 *   <li>Used by {@link PrimaryNodeTreeDecoder} during tree reconstruction</li>
 *   <li>Works with {@link evoltree.struct.TreeCoder} for tree encoding</li>
 *   <li>Paired with {@link BasicLeafCoderDecoder} for complete tree support</li>
 * </ul>
 * 
 * <h2>Performance Characteristics</h2>
 * <ul>
 *   <li><strong>Parsing:</strong> O(n) where n is the string length</li>
 *   <li><strong>Encoding:</strong> O(1) with fixed formatting</li>
 *   <li><strong>Memory:</strong> Minimal overhead, creates single node instance</li>
 * </ul>
 *
 * @param <T> the type of node (must extend ArrayBasedNode)
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see AbstractNodeCoderDecoder For the base codec interface
 * @see BasicLeafCoderDecoder For leaf node encoding/decoding
 * @see ArrayBasedNode For the node implementation
 */
public class BasicInternalCoderDecoder<T extends ArrayBasedNode> extends AbstractNodeCoderDecoder<T> {

	@Override
	public void parseNode(T node, String str) {
		if (str.length() > 0) {
			String[] split = str.split(":", -1);
			node.setName(split[0]);
			if (split.length > 1) {
				node.setLength(Double.parseDouble(split[1]));
			}
		}

	}

	@Override
	public String codeNode(T node) {
		String name = node.getName();
		
		if (name == null) {
			return ":" + node.getLength();
		} else {
			return name + ":" + node.getLength();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T createNode() {
		return (T) new ArrayBasedNode();
	}

}