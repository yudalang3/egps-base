package evoltree.swingvis;

import java.awt.Graphics2D;

import evoltree.struct.EvolNode;
import evoltree.txtdisplay.ReflectGraphicNode;

/**
 * Functional interface for custom rendering of individual phylogenetic tree nodes.
 * 
 * <p>
 * This interface defines a contract for drawing a single node in a phylogenetic tree
 * visualization. It follows the functional interface pattern introduced in Java 8,
 * allowing implementations via lambda expressions for concise and flexible node
 * rendering logic.
 * </p>
 * 
 * <h2>Purpose</h2>
 * <p>
 * The {@code OneNodeDrawer} interface provides a callback mechanism for custom tree
 * node visualization. It allows developers to define how each node should be rendered
 * without modifying the core tree traversal or layout algorithms.
 * </p>
 * 
 * <h2>Key Features</h2>
 * <ul>
 *   <li><strong>Functional Interface:</strong> Single abstract method design</li>
 *   <li><strong>Lambda Support:</strong> Can be implemented with lambda expressions</li>
 *   <li><strong>Graphics2D Integration:</strong> Full access to Java 2D rendering capabilities</li>
 *   <li><strong>Node Metadata Access:</strong> Complete access to node properties via ReflectGraphicNode</li>
 *   <li><strong>Flexible Rendering:</strong> Supports any custom drawing logic</li>
 * </ul>
 * 
 * <h2>Usage Pattern</h2>
 * <p>
 * This interface is typically used in conjunction with tree layout calculators and
 * painting panels. The layout calculator determines node positions, and the drawer
 * is responsible for rendering each node at its calculated position.
 * </p>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * // Example 1: Simple text drawer using lambda
 * OneNodeDrawer&lt;EvolNode&gt; textDrawer = (g2d, node) -&gt; {
 *     if (node.getChildCount() == 0) {  // Leaf nodes only
 *         g2d.drawString(node.getReflectNode().getName(), 
 *                       (int) node.getXSelf() + 5, 
 *                       (int) node.getYSelf());
 *     }
 * };
 * 
 * // Example 2: Custom node shapes
 * OneNodeDrawer&lt;EvolNode&gt; shapeDrawer = (g2d, node) -&gt; {
 *     int x = (int) node.getXSelf();
 *     int y = (int) node.getYSelf();
 *     
 *     if (node.getChildCount() == 0) {
 *         // Draw circle for leaf nodes
 *         g2d.fillOval(x - 3, y - 3, 6, 6);
 *         g2d.drawString(node.getReflectNode().getName(), x + 10, y);
 *     } else {
 *         // Draw square for internal nodes
 *         g2d.fillRect(x - 2, y - 2, 4, 4);
 *     }
 * };
 * 
 * // Example 3: Bootstrap value display
 * OneNodeDrawer&lt;DefaultPhyNode&gt; bootstrapDrawer = (g2d, node) -&gt; {
 *     if (node.getChildCount() &gt; 0) {  // Internal nodes
 *         double bootstrap = node.getReflectNode().getBootstrapValue();
 *         if (bootstrap &gt; 70.0) {
 *             g2d.setColor(Color.RED);
 *             g2d.drawString(String.format("%.0f", bootstrap), 
 *                           (int) node.getXSelf(), 
 *                           (int) node.getYSelf() - 5);
 *         }
 *     }
 * };
 * 
 * // Example 4: Branch length display
 * OneNodeDrawer&lt;EvolNode&gt; lengthDrawer = (g2d, node) -&gt; {
 *     int xSelf = (int) node.getXSelf();
 *     int ySelf = (int) node.getYSelf();
 *     int xParent = (int) node.getXParent();
 *     
 *     // Draw branch length at midpoint
 *     String lenStr = String.format("%.3f", node.getReflectNode().getLength());
 *     int midX = (xSelf + xParent) / 2;
 *     g2d.drawString(lenStr, midX, ySelf - 5);
 * };
 * </pre>
 * 
 * <h2>Implementation Notes</h2>
 * <ul>
 *   <li><strong>Graphics State:</strong> The Graphics2D object state should be restored if modified</li>
 *   <li><strong>Coordinate System:</strong> Uses the coordinate system established by the layout calculator</li>
 *   <li><strong>Performance:</strong> Called once per node during tree rendering</li>
 *   <li><strong>Thread Safety:</strong> Implementations should be thread-safe if used in concurrent contexts</li>
 * </ul>
 * 
 * <h2>Common Drawing Operations</h2>
 * <ul>
 *   <li><strong>Text:</strong> {@code g2d.drawString()}</li>
 *   <li><strong>Lines:</strong> {@code g2d.drawLine()}</li>
 *   <li><strong>Shapes:</strong> {@code g2d.fillOval()}, {@code g2d.fillRect()}</li>
 *   <li><strong>Colors:</strong> {@code g2d.setColor()}</li>
 *   <li><strong>Fonts:</strong> {@code g2d.setFont()}</li>
 *   <li><strong>Strokes:</strong> {@code g2d.setStroke()}</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li>Used by {@link evoltree.tanglegram.PairwisePaintingPanel} for tanglegram drawing</li>
 *   <li>Works with layout calculators for positioned tree rendering</li>
 *   <li>Supports custom visualization strategies via lambda expressions</li>
 * </ul>
 *
 * @param <T> the type of tree node (must extend {@link EvolNode})
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see ReflectGraphicNode For the graphic node wrapper
 * @see evoltree.tanglegram.QuickPairwiseTreeComparator For usage examples
 * @see java.awt.Graphics2D For rendering capabilities
 */
@FunctionalInterface
public interface OneNodeDrawer<T extends EvolNode> {

	/**
	 * Draws a single phylogenetic tree node.
	 * 
	 * <p>
	 * This method is called once for each node in the tree during rendering.
	 * The implementation should use the provided Graphics2D object to draw
	 * the node at the position specified by the ReflectGraphicNode.
	 * </p>
	 * 
	 * @param paramGraphics2D the Graphics2D object for rendering
	 * @param paramReflectGraphicNode the node to draw with position information
	 */
	void draw(Graphics2D paramGraphics2D, ReflectGraphicNode<T> paramReflectGraphicNode);
}