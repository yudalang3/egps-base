package evoltree.tanglegram;

import evoltree.struct.EvolNode;
import evoltree.txtdisplay.ReflectGraphicNode;

import java.awt.*;

/**
 * Abstract base class for rectangular layout calculators in tanglegram visualizations.
 * 
 * <p>
 * This abstract class provides the foundation for tree layout algorithms that calculate
 * node positions in a rectangular (phylogram-style) layout. It defines the contract for
 * layout calculation and provides shared configuration for spacing and margins.
 * </p>
 * 
 * <h2>Layout Strategy</h2>
 * <p>
 * Rectangular layout is a common phylogenetic tree visualization style where:
 * </p>
 * <ul>
 *   <li><strong>Horizontal axis:</strong> Represents evolutionary time or distance</li>
 *   <li><strong>Vertical axis:</strong> Distributes taxa for visual clarity</li>
 *   <li><strong>Branch lengths:</strong> Proportional to evolutionary distance</li>
 *   <li><strong>Leaves:</strong> Aligned vertically at the same depth</li>
 * </ul>
 * 
 * <h2>Key Features</h2>
 * <ul>
 *   <li><strong>Abstract Design:</strong> Template for left/right tree layouts</li>
 *   <li><strong>Blank Margins:</strong> Consistent spacing around trees (30 pixels)</li>
 *   <li><strong>Dimension-Based:</strong> Adapts to available display area</li>
 *   <li><strong>Generic Support:</strong> Works with any EvolNode subtype</li>
 * </ul>
 * 
 * <h2>Implementation Classes</h2>
 * <ul>
 *   <li>{@link LeftRectangularLayoutQuickCalculator}: Left tree layout (roots on left)</li>
 *   <li>{@link RightRectangularLayoutQuickCalculator}: Right tree layout (roots on right)</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * <pre>
 * // Create layout calculators for tanglegram
 * BaseRectangularLayoutCalculator leftCalc = new LeftRectangularLayoutQuickCalculator();
 * BaseRectangularLayoutCalculator rightCalc = new RightRectangularLayoutQuickCalculator();
 * 
 * // Calculate positions
 * Dimension halfDim = new Dimension(400, 600);
 * leftCalc.calculateTree(leftTreeRoot, halfDim);
 * rightCalc.calculateTree(rightTreeRoot, halfDim);
 * </pre>
 * 
 * <h2>Coordinate System</h2>
 * <p>
 * Layout calculators set these ReflectGraphicNode properties:
 * </p>
 * <ul>
 *   <li><strong>xSelf:</strong> X coordinate of this node</li>
 *   <li><strong>ySelf:</strong> Y coordinate of this node</li>
 *   <li><strong>xParent:</strong> X coordinate of parent connection point</li>
 *   <li><strong>yParent:</strong> Y coordinate of parent connection point</li>
 * </ul>
 *
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see LeftRectangularLayoutQuickCalculator
 * @see RightRectangularLayoutQuickCalculator
 * @see ReflectGraphicNode
 */
public abstract class BaseRectangularLayoutCalculator {
	
	/**
	 * Blank margin space in pixels around the tree visualization.
	 * This provides padding between the tree and the edge of the display area.
	 */
	public final int blankLength = 30;
	
	/**
	 * Calculates the rectangular layout positions for all nodes in the tree.
	 * 
	 * <p>
	 * Implementing classes must define how to:
	 * </p>
 * <ol>
	 *   <li>Calculate horizontal positions based on branch lengths</li>
	 *   <li>Calculate vertical positions for leaf distribution</li>
	 *   <li>Set xSelf, ySelf, xParent, yParent for each node</li>
	 * </ol>
	 * 
	 * @param root the root of the tree to layout
	 * @param dim the available dimensions for the layout
	 * @param <T> the type of tree node
	 */
	public abstract <T extends EvolNode> void calculateTree(ReflectGraphicNode<T> root, Dimension dim);

	
}