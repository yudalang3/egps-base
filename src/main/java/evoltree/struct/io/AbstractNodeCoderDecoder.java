package evoltree.struct.io;

import evoltree.struct.EvolNode;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Abstract base class for encoding and decoding phylogenetic tree nodes to/from string representations.
 * 
 * <p>
 * This abstract class defines the standard interface for converting tree nodes to and from string
 * representations (such as Newick format). It provides a unified framework for serialization and
 * deserialization of phylogenetic tree structures, supporting various tree formats and node types.
 * </p>
 * 
 * <h2>Core Functionality</h2>
 * <ul>
 *   <li><strong>Encoding:</strong> Convert tree nodes to string representation</li>
 *   <li><strong>Decoding:</strong> Parse strings and reconstruct tree node structures</li>
 *   <li><strong>Node Creation:</strong> Abstract method for node instantiation</li>
 *   <li><strong>Number Formatting:</strong> Unified numerical formatting utilities</li>
 * </ul>
 * 
 * <h2>Design Pattern</h2>
 * <p>
 * Implements the Template Method pattern, defining the algorithm skeleton for encoding/decoding:
 * </p>
 * <ul>
 *   <li><strong>Encoding workflow:</strong> Calls {@code codeNode()} method</li>
 *   <li><strong>Decoding workflow:</strong> Calls {@code parseNode()} and {@code createNode()} methods</li>
 *   <li><strong>Subclass implementation:</strong> Concrete subclasses provide specific encoding/decoding logic</li>
 * </ul>
 * 
 * <h2>Implementation Guide</h2>
 * <p>
 * Implementing classes must:
 * </p>
 * <ol>
 *   <li>Implement {@code codeNode()} method to define encoding logic</li>
 *   <li>Implement {@code parseNode()} method to define parsing logic</li>
 *   <li>Implement {@code createNode()} method to provide node instantiation</li>
 *   <li>Optionally reuse parent class numerical formatting features</li>
 * </ol>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li><strong>Leaf Node Codec:</strong> Handle terminal nodes in phylogenetic trees</li>
 *   <li><strong>Internal Node Codec:</strong> Handle ancestor nodes in phylogenetic trees</li>
 *   <li><strong>Unified Codec:</strong> Process both leaf and internal nodes uniformly</li>
 * </ul>
 * 
 * <h2>Number Formatting</h2>
 * <p>
 * Built-in number formatter configuration:
 * </p>
 * <ul>
 *   <li><strong>Maximum fraction digits:</strong> 6 decimal places</li>
 *   <li><strong>Rounding mode:</strong> HALF_UP (standard rounding)</li>
 *   <li><strong>Purpose:</strong> Format branch lengths, confidence values, and other numeric data</li>
 * </ul>
 * 
 * <h2>Implementation Example</h2>
 * <pre>
 * public class NewickLeafDecoder extends AbstractNodeCoderDecoder&lt;DefaultPhyNode&gt; {
 *     
 *     &#64;Override
 *     public String codeNode(DefaultPhyNode node) {
 *         // Implement leaf node encoding logic
 *         return node.getName() + ":" + convertRate2Decimal(node.getLength());
 *     }
 *     
 *     &#64;Override
 *     public void parseNode(DefaultPhyNode node, String str) {
 *         // Implement leaf node parsing logic
 *         String[] parts = str.split(":");
 *         node.setName(parts[0]);
 *         if (parts.length > 1) {
 *             node.setLength(Double.parseDouble(parts[1]));
 *         }
 *     }
 *     
 *     &#64;Override
 *     public DefaultPhyNode createNode() {
 *         return new DefaultPhyNode();
 *     }
 * }
 * </pre>
 * 
 * <h2>Performance Considerations</h2>
 * <ul>
 *   <li><strong>Avoid reflection:</strong> {@code createNode()} should directly instantiate rather than use reflection</li>
 *   <li><strong>Number formatting:</strong> Reuse parent class formatter for better performance</li>
 *   <li><strong>Memory management:</strong> Consider streaming approaches for very large trees</li>
 *   <li><strong>String building:</strong> Use StringBuilder for efficient string concatenation</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li>Foundation for Newick format parsing ({@link evoltree.phylogeny.PhyloTreeEncoderDecoder})</li>
 *   <li>Supports custom tree formats through subclassing</li>
 *   <li>Enables bidirectional tree serialization for file I/O and data exchange</li>
 * </ul>
 *
 * @param <S> the tree node type, must extend {@link EvolNode}
 * @author yudalang
 * @version 1.0
 * @since 1.0
 * @see EvolNode For the tree node interface
 * @see evoltree.phylogeny.PhyloTreeEncoderDecoder For complete tree encoder/decoder implementation
 * @see BasicLeafCoderDecoder For leaf node codec implementation
 * @see BasicInternalCoderDecoder For internal node codec implementation
 */
public abstract class AbstractNodeCoderDecoder<S extends EvolNode> {

	protected static final char COLON = ':';
	protected static final char COMMA = ',';
	NumberFormat numberFormat = NumberFormat.getNumberInstance();

	public AbstractNodeCoderDecoder() {

		numberFormat.setMaximumFractionDigits(6);// 设置保留小数位
		numberFormat.setRoundingMode(RoundingMode.HALF_UP); // 设置舍入模式
	}

	public abstract void parseNode(S node, String str);

	public abstract String codeNode(S node);

	/**
	 * 派生类需要实现这个方法去创建对象，不要用反射，那个太慢了。
	 * 
	 * @return the concrete node
	 */
	public abstract S createNode();

	/**
	 * https://www.baeldung.com/java-double-to-string
	 * 
	 * @param value
	 * @return the string representation of the double value
	 */
	protected String convertRate2Decimal(double value) {
		return numberFormat.format(value);
	}

}