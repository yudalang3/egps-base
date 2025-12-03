package phylo.algorithm;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import evoltree.struct.EvolNode;
import evoltree.struct.TreeDecoder;
import evoltree.struct.util.EvolNodeUtil;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Robinson-Foulds distance calculator for comparing phylogenetic trees.
 * 
 * <p>
 * This utility class implements the Robinson-Foulds metric (also known as the partition
 * distance) for quantifying the topological differences between two phylogenetic trees.
 * The metric counts the number of different splits (bipartitions) between trees,
 * providing a measure of their structural similarity.
 * </p>
 * 
 * <h2>Robinson-Foulds Metric Background:</h2>
 * <p>
 * The Robinson-Foulds distance measures how similar two trees are by comparing their
 * internal partitions. For two trees with n taxa:
 * </p>
 * <ul>
 *   <li>Maximum possible distance: 2(n-3)</li>
 *   <li>Distance of 0: Trees are identical</li>
 *   <li>Distance approaches max: Trees are very different</li>
 * </ul>
 * 
 * <h2>Algorithm Overview:</h2>
 * <ol>
 *   <li>Extract all internal bipartitions (splits) from both trees</li>
 *   <li>Compare splits and count shared vs. unique partitions</li>
 *   <li>Calculate the Robinson-Foulds distance</li>
 * </ol>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Binary Tree Support:</strong> Works with rooted and unrooted binary trees</li>
 *   <li><strong>Equal Taxa Requirement:</strong> Both trees must have the same leaf set</li>
 *   <li><strong>Distance Calculation:</strong> Returns integer count of differing splits</li>
 *   <li><strong>Memory Efficient:</strong> Uses Set operations for fast comparisons</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Load two phylogenetic trees
 * EvolTree tree1 = loadTree("species_tree1.nwk");
 * EvolTree tree2 = loadTree("species_tree2.nwk");
 * 
 * // Calculate Robinson-Foulds distance
 * RobinsonFouldsMetricCalculator calculator = new RobinsonFouldsMetricCalculator();
 * int distance = calculator.countDiff(tree1, tree2);
 * 
 * System.out.println("Robinson-Foulds distance: " + distance);
 * // Output: Robinson-Foulds distance: 8
 * </pre>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Tree Comparison:</strong> Evaluate phylogenetic reconstruction methods</li>
 *   <li><strong>Bootstrap Analysis:</strong> Compare bootstrap trees to consensus</li>
 *   <li><strong>Method Validation:</strong> Assess tree-building algorithm accuracy</li>
 *   <li><strong>Evolutionary Analysis:</strong> Compare competing evolutionary hypotheses</li>
 *   <li><strong>Quality Assessment:</strong> Measure tree confidence and reliability</li>
 * </ul>
 * 
 * <h2>Implementation Notes:</h2>
 * <ul>
 *   <li>Only considers internal nodes with children (excludes root and leaves)</li>
 *   <li>Ignores branch lengths in distance calculation</li>
 *   <li>Uses sorted leaf sets for consistent partition representation</li>
 *   <li>Performance: O(n log n) where n is the number of leaves</li>
 * </ul>
 * 
 * <h2>Limitations:</h2>
 * <ul>
 *   <li>Requires same taxa set in both trees</li>
 *   <li>Only measures tree topology, not branch lengths</li>
 *   <li>Maximum distance may not be intuitive for interpretation</li>
 *   <li>No normalization for tree size</li>
 * </ul>
 * 
 * @see EvolNode
 * @see evoltree.struct.util.EvolNodeUtil
 * @author eGPS Development Team
 * @since 1.0
 * @reference Robinson, D.F., & Foulds, L.R. (1981). Comparison of phylogenetic trees. 
 *           Mathematical Biosciences, 53(1-2), 131-147.
 */
public class RobinsonFouldsMetricCalculator {

    public int countDiff(EvolNode tree1, EvolNode tree2) {

        List<String> internalNodeElements1 = Lists.newArrayList();
        Set<String> internalNodeElements2 = Sets.newHashSet();

        EvolNodeUtil.recursiveIterateTreeIF(tree1, node -> {
            if (node.getParentCount() == 0) {
                return;
            }
            if (node.getChildCount() == 0) {
                return;
            }
            List<EvolNode> leaves = EvolNodeUtil.getLeaves(node);
            List<String> stringsOfNode1 = Lists.newLinkedList();
            for (EvolNode set : leaves) {
                stringsOfNode1.add(set.getName());
            }
            internalNodeElements1.add(setToSortedString(stringsOfNode1));
        });

        EvolNodeUtil.recursiveIterateTreeIF(tree2, node -> {
            if (node.getParentCount() == 0) {
                return;
            }
            if (node.getChildCount() == 0) {
                return;
            }
            List<EvolNode> leaves = EvolNodeUtil.getLeaves(node);
            List<String> stringsOfNode1 = Lists.newLinkedList();
            for (EvolNode set : leaves) {
                stringsOfNode1.add(set.getName());
            }
            internalNodeElements2.add(setToSortedString(stringsOfNode1));
        });

        int ret = 0;

        for (String string : internalNodeElements1) {
            if (!internalNodeElements2.contains(string)) {
                ret++;
            }
        }

        // 不要忘记乘以2
        return ret + ret;
    }

    private String setToSortedString(List<String> sortedList) {
        Collections.sort(sortedList);
        StringBuilder sb = new StringBuilder();
        for (String s : sortedList) {
            sb.append(s).append(",");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        TreeDecoder treeDecoder = new TreeDecoder();

        {
            String line1 = "(A,(B,(H,(D,(J,(((G,E),(F,I)),C))))));".replaceAll(" ", "");
            String line2 = "(A,(B,(D,((J, H),(((G, E),(F,I)),C)))));".replaceAll(" ", "");

            EvolNode root1 = treeDecoder.decode(line1);
            EvolNode root2 = treeDecoder.decode(line2);

            RobinsonFouldsMetricCalculator cal = new RobinsonFouldsMetricCalculator();
            int countDiff = cal.countDiff(root1, root2);
            System.out.println(countDiff == 4);
        }

        {
            String line1 = "(A,(B,(D,(H,(J,(((G,E),(F,I)), C))))));".replaceAll(" ", "");
            String line2 = "(A,(B,(E,(G,((F,I),((J,(H, D)), C))))));".replaceAll(" ", "");

            EvolNode root1 = treeDecoder.decode(line1);
            EvolNode root2 = treeDecoder.decode(line2);

            RobinsonFouldsMetricCalculator cal = new RobinsonFouldsMetricCalculator();
            int countDiff = cal.countDiff(root1, root2);
            System.out.println(countDiff == 10);
        }

        {
            String line1 = "(A,(B,(E,(G,((F,I),(((J, H), D), C))))));".replaceAll(" ", "");
            String line2 = "(A,(B,(E,((F,I),(G,((J,(H, D)), C))))));".replaceAll(" ", "");

            EvolNode root1 = treeDecoder.decode(line1);
            EvolNode root2 = treeDecoder.decode(line2);

            RobinsonFouldsMetricCalculator cal = new RobinsonFouldsMetricCalculator();
            int countDiff = cal.countDiff(root1, root2);
            System.out.println(countDiff == 4);
        }
    }

}
