package evoltree.tanglegram;

import top.signature.IModuleSignature;

/**
 * Module signature for phylogenetic tanglegram visualization tools.
 * 
 * <p>
 * This class implements the {@link IModuleSignature} interface to provide
 * module identification and metadata for the tanglegram visualization functionality
 * within the eGPS system. It defines the module's purpose and display name for
 * GUI integration and system identification.
 * </p>
 * 
 * <h2>Module Purpose:</h2>
 * <p>
 * The tanglegram module provides specialized tools for side-by-side phylogenetic tree
 * comparison and visualization. A tanglegram is a graphical representation that displays
 * two phylogenetic trees facing each other with connecting lines between corresponding
 * leaves, enabling visual assessment of topological concordance and discordance.
 * </p>
 * 
 * <h2>What is a Tanglegram?</h2>
 * <p>
 * A tanglegram is a powerful visualization technique that:
 * </p>
 * <ul>
 *   <li>Displays two trees side by side (left and right)</li>
 *   <li>Connects corresponding taxa between trees with lines</li>
 *   <li>Reveals topological differences through crossing lines</li>
 *   <li>Calculates Robinson-Foulds distance automatically</li>
 *   <li>Supports interactive tree exploration</li>
 * </ul>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Side-by-Side Display:</strong> Parallel tree visualization</li>
 *   <li><strong>Automatic Alignment:</strong> Matches corresponding leaves</li>
 *   <li><strong>Distance Calculation:</strong> Built-in Robinson-Foulds metric</li>
 *   <li><strong>Custom Rendering:</strong> Flexible node drawing via OneNodeDrawer</li>
 *   <li><strong>Rectangular Layout:</strong> Optimized layout algorithms</li>
 *   <li><strong>Interactive Display:</strong> Swing-based GUI integration</li>
 * </ul>
 * 
 * <h2>Core Components:</h2>
 * <ul>
 *   <li>{@link QuickPairwiseTreeComparator}: Main entry point for tanglegram creation</li>
 *   <li>{@link PairwisePaintingPanel}: Swing panel for rendering tanglegrams</li>
 *   <li>{@link LeftRectangularLayoutQuickCalculator}: Layout calculator for left tree</li>
 *   <li>{@link RightRectangularLayoutQuickCalculator}: Layout calculator for right tree</li>
 *   <li>{@link BaseRectangularLayoutCalculator}: Abstract base for layout algorithms</li>
 * </ul>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li><strong>Method Comparison:</strong> Compare trees from different phylogenetic methods</li>
 *   <li><strong>Gene vs Species Trees:</strong> Visualize gene tree/species tree incongruence</li>
 *   <li><strong>Hypothesis Testing:</strong> Evaluate alternative phylogenetic hypotheses</li>
 *   <li><strong>Sensitivity Analysis:</strong> Assess impact of parameter changes</li>
 *   <li><strong>Cophylogeny:</strong> Study host-parasite coevolution</li>
 *   <li><strong>Quality Assessment:</strong> Evaluate phylogenetic reconstruction quality</li>
 * </ul>
 * 
 * <h2>Integration Points:</h2>
 * <ul>
 *   <li><strong>GUI Integration:</strong> Tab name for user interface display</li>
 *   <li><strong>Module Registry:</strong> System-wide module identification</li>
 *   <li><strong>Documentation:</strong> Help text and module descriptions</li>
 *   <li><strong>Visualization Pipeline:</strong> Tree comparison workflows</li>
 * </ul>
 * 
 * <h2>Related Modules:</h2>
 * <ul>
 *   <li>{@link evoltree.swingvis}: Base visualization infrastructure</li>
 *   <li>{@link phylo.algorithm.RobinsonFouldsMetricCalculator}: Distance calculation</li>
 *   <li>{@link evoltree.txtdisplay}: Text-based tree display</li>
 * </ul>
 *
 * @see IModuleSignature
 * @see QuickPairwiseTreeComparator
 * @see PairwisePaintingPanel
 * @author eGPS Development Team
 * @since 1.0
 */
public class ZzzModuleSignature implements IModuleSignature {
    @Override
    public String getShortDescription() {
        return "Convenient tools for drawing the tangle gram.";
    }

    @Override
    public String getTabName() {
        return "Tree Tangle-gram";
    }

}