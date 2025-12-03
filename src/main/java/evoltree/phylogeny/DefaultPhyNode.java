package evoltree.phylogeny;

import evoltree.struct.ArrayBasedNode;

/**
 * Default implementation of phylogenetic tree nodes with bootstrap value support.
 * 
 * <p>
 * This class provides a standard implementation of phylogenetic tree nodes designed
 * for evolutionary analysis and tree manipulation. It extends the ArrayBasedNode
 * class and adds specialized support for storing and managing bootstrap values,
 * which are essential for phylogenetic inference and tree confidence assessment.
 * </p>
 * 
 * <h2>Key Features</h2>
 * <ul>
 *   <li><strong>Bootstrap Values:</strong> Store statistical support values for tree nodes</li>
 *   <li><strong>Tree Structure:</strong> Maintains parent-child relationships in tree topology</li>
 *   <li><strong>Array-based Storage:</strong> Efficient memory usage for node collections</li>
 *   <li><strong>Evolutionary Context:</strong> Optimized for phylogenetic tree operations</li>
 *   <li><strong>Extensible Design:</strong> Foundation for specialized phylogenetic nodes</li>
 * </ul>
 * 
 * <h2>Bootstrap Values</h2>
 * <p>
 * Bootstrap values represent the confidence level or statistical support for a
 * particular branch or clade in a phylogenetic tree. They are typically expressed
 * as percentages (0-100) or as probabilities (0.0-1.0), where higher values
 * indicate greater confidence in the evolutionary relationship.
 * </p>
 * 
 * <h3>Common Bootstrap Value Interpretations:</h3>
 * <ul>
 *   <li><strong>90-100%:</strong> Very strong support for the clade</li>
 *   <li><strong>70-89%:</strong> Moderate to strong support</li>
 *   <li><strong>50-69%:</strong> Weak support, relationship uncertain</li>
 *   <li><strong>&lt;50%:</strong> Very weak support, clade may be unreliable</li>
 * </ul>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li><strong>Tree Visualization:</strong> Display confidence values in tree graphics</li>
 *   <li><strong>Tree Filtering:</strong> Remove poorly supported branches based on thresholds</li>
 *   <li><strong>Statistical Analysis:</strong> Analyze support value distributions</li>
 *   <li><strong>Tree Comparison:</strong> Compare confidence levels across different analyses</li>
 *   <li><strong>Publication Quality:</strong> Generate trees with proper statistical annotations</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li><strong>Tree Encoding/Decoding:</strong> Works with PhyloTreeEncoderDecoder</li>
 *   <li><strong>Tree Manipulation:</strong> Compatible with EvolNodeUtil operations</li>
 *   <li><strong>Phylogenetic Analysis:</strong> Used in evolutionary distance calculations</li>
 *   <li><strong>Visualization:</strong> Supports tree drawing and display components</li>
 * </ul>
 * 
 * <h2>Example Usage</h2>
 * <pre>
 * // Create a phylogenetic tree node
 * DefaultPhyNode node = new DefaultPhyNode();
 * node.setName("Species_A");
 * node.setBootstrapValue(92.5);
 * 
 * // Set branch length to parent
 * node.setBranchLengthToParent(0.15);
 * 
 * // Access bootstrap value for analysis
 * double support = node.getBootstrapValue();
 * if (support > 80.0) {
 *     System.out.println("Strong phylogenetic support: " + support + "%");
 * }
 * </pre>
 * 
 * <h2>Performance Characteristics</h2>
 * <ul>
 *   <li><strong>Memory:</strong> Single double value for bootstrap (8 bytes)</li>
 *   <li><strong>Access Time:</strong> O(1) for bootstrap value getter/setter</li>
 *   <li><strong>Tree Operations:</strong> Efficient inheritance from ArrayBasedNode</li>
 * </ul>
 * 
 * <h2>Related Classes</h2>
 * <ul>
 *   <li><strong>ArrayBasedNode:</strong> Base class providing tree structure implementation</li>
 *   <li><strong>EvolNode:</strong> Interface defining node behavior</li>
 *   <li><strong>PhyloTreeEncoderDecoder:</strong> Serialization/deserialization</li>
 *   <li><strong>EvolNodeUtil:</strong> Tree manipulation utilities</li>
 * </ul>
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 * @see evoltree.struct.ArrayBasedNode For the base node implementation
 * @see evoltree.struct.EvolNode For the node interface
 * @see evoltree.phylogeny.PhyloTreeEncoderDecoder For tree encoding/decoding
 */
public class DefaultPhyNode extends ArrayBasedNode {
	
	protected double bootstrapValue;
	
	
    /**
     * Gets the bootstrap value of the node.
     * 
     * @return the bootstrap value
     */
    public double getBootstrapValue() {
		return bootstrapValue;
	}
	
    /**
     * Sets the bootstrap value of the node.
     * 
     * @param bootstrapValue the bootstrap value to set
     */
    public void setBootstrapValue(double bootstrapValue) {
		this.bootstrapValue = bootstrapValue;
	}

}
