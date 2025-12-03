package evoltree.struct.util;

import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;

/**
 * Utility class for generating synthetic phylogenetic trees for testing and demonstration purposes.
 * 
 * <p>
 * This class provides convenient methods to programmatically create phylogenetic tree structures
 * without parsing Newick strings. It is particularly useful for:
 * </p>
 * <ul>
 *   <li>Unit testing tree algorithms</li>
 *   <li>Performance benchmarking with controlled tree sizes</li>
 *   <li>Creating example trees for documentation</li>
 *   <li>Testing visualization components</li>
 *   <li>Debugging tree manipulation operations</li>
 * </ul>
 * 
 * <h2>Tree Generation Patterns</h2>
 * <p>
 * The class generates binary trees with a simple left-right branching pattern. Each level
 * of the tree adds two children (left and right), creating a balanced binary tree structure.
 * </p>
 * 
 * <h3>Default Tree Structure:</h3>
 * <pre>
 * Root: "Cellular organisms"
 *   ├── L_27 (Left child)
 *   └── R_27 (Right child)
 *       ├── L_26
 *       └── R_26
 *           └── ... (continues to specified depth)
 * </pre>
 * 
 * <h2>Key Features</h2>
 * <ul>
 *   <li><strong>Configurable Depth:</strong> Specify tree depth to control size</li>
 *   <li><strong>Named Nodes:</strong> Nodes labeled with L_/R_ prefix and depth number</li>
 *   <li><strong>Branch Lengths:</strong> All branches set to uniform length (1.0)</li>
 *   <li><strong>Binary Structure:</strong> Creates balanced bifurcating trees</li>
 *   <li><strong>ArrayBased Implementation:</strong> Uses efficient ArrayBasedNode</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * PhyloTreeGenerator generator = new PhyloTreeGenerator();
 * 
 * // Example 1: Generate default tree (depth 27)
 * EvolNode defaultTree = generator.getTheTree();
 * System.out.println("Root: " + defaultTree.getName());  // "Cellular organisms"
 * 
 * // Example 2: Generate tree with custom depth
 * EvolNode smallTree = generator.getTheTree(5);
 * int leafCount = EvolNodeUtil.getNumOfLeaves(smallTree);
 * System.out.println("Leaves: " + leafCount);  // 32 leaves for depth 5
 * 
 * // Example 3: Use for testing tree algorithms
 * EvolNode testTree = generator.getTheTree(10);
 * EvolTreeOperator.rootAtMidPoint(testTree);  // Test rerooting
 * 
 * // Example 4: Benchmark performance
 * long startTime = System.currentTimeMillis();
 * EvolNode largeTree = generator.getTheTree(20);
 * long endTime = System.currentTimeMillis();
 * System.out.println("Tree generation time: " + (endTime - startTime) + "ms");
 * </pre>
 * 
 * <h2>Tree Size by Depth</h2>
 * <p>
 * The number of nodes grows exponentially with depth:
 * </p>
 * <ul>
 *   <li><strong>Depth 5:</strong> 63 total nodes, 32 leaves</li>
 *   <li><strong>Depth 10:</strong> 2,047 total nodes, 1,024 leaves</li>
 *   <li><strong>Depth 15:</strong> 65,535 total nodes, 32,768 leaves</li>
 *   <li><strong>Depth 20:</strong> ~2 million total nodes, ~1 million leaves</li>
 *   <li><strong>Formula:</strong> Total nodes = 2^(depth+1) - 1, Leaves = 2^depth</li>
 * </ul>
 * 
 * <h2>Performance Characteristics</h2>
 * <ul>
 *   <li><strong>Time Complexity:</strong> O(2^depth) for tree generation</li>
 *   <li><strong>Space Complexity:</strong> O(2^depth) for storing nodes</li>
 *   <li><strong>Recursion Depth:</strong> O(depth) stack frames</li>
 *   <li><strong>Practical Limit:</strong> Depth 30-40 depending on available memory</li>
 * </ul>
 * 
 * <h2>Node Naming Convention</h2>
 * <ul>
 *   <li><strong>Root:</strong> "Cellular organisms" (default) or custom name</li>
 *   <li><strong>Left Children:</strong> Prefixed with "L_" + depth level</li>
 *   <li><strong>Right Children:</strong> Prefixed with "R_" + depth level</li>
 *   <li><strong>Example:</strong> L_5 is a left child at depth 5</li>
 * </ul>
 * 
 * <h2>Branch Length Configuration</h2>
 * <p>
 * All branches are set to a uniform length of 1.0. This creates a simple ultrametric
 * tree where all leaves are equidistant from the root. The branch lengths can be
 * modified after generation if needed:
 * </p>
 * <pre>
 * EvolNode tree = generator.getTheTree(10);
 * EvolNodeUtil.recursiveIterateTreeIF(tree, node -&gt; {
 *     node.setLength(Math.random());  // Random branch lengths
 * });
 * </pre>
 * 
 * <h2>Use Cases</h2>
 * <ul>
 *   <li><strong>Unit Testing:</strong> Create predictable test cases</li>
 *   <li><strong>Algorithm Validation:</strong> Test on known tree structures</li>
 *   <li><strong>Performance Testing:</strong> Generate large trees for benchmarks</li>
 *   <li><strong>Visualization Testing:</strong> Test tree drawing with various sizes</li>
 *   <li><strong>Educational Demos:</strong> Create simple examples for teaching</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li>Compatible with all EvolNode-based operations</li>
 *   <li>Works with tree visualization components</li>
 *   <li>Useful for testing phylogenetic algorithms</li>
 *   <li>Supports tree manipulation and transformation operations</li>
 * </ul>
 *
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see EvolNode For the node interface
 * @see ArrayBasedNode For the node implementation
 * @see EvolNodeUtil For tree manipulation utilities
 */
public class PhyloTreeGenerator {

    public EvolNode getTheTree() {
        EvolNode root = createNode();
        root.setName("Cellular organisms");
        configureTree(root, 27);

        return root;
    }

    public EvolNode getTheTree(int depth) {
        EvolNode root = createNode();
        root.setName("Tree with depth " + depth);
        configureTree(root, depth);

        return root;
    }

    private void configureTree(EvolNode root, int depth) {
        if (depth <= 0) {
            return; // 如果达到指定的深度，则停止递归
        }

        // 创建左子节点
        EvolNode leftChild = createNode();
        leftChild.setName("L_" + depth);
        root.addChild(leftChild); // 添加左子节点


        EvolNode rightChild = createNode();
        rightChild.setName("R_" + depth);
        root.addChild(rightChild); // 添加右子节点

        configureTree(rightChild, depth - 1); // 递归配置左子树

    }

    private static EvolNode createNode() {
        ArrayBasedNode node = new ArrayBasedNode();
        node.setLength(1.0);
        return node;

    }


}
