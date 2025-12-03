package evoltree.tanglegram;

import evoltree.struct.EvolNode;
import evoltree.struct.TreeDecoder;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.util.EvolTreeOperator;
import evoltree.swingvis.OneNodeDrawer;
import evoltree.txtdisplay.ReflectGraphicNode;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

/**
 * Utility for quick pairwise comparison and visualization of phylogenetic trees using tanglegram.
 */
public class QuickPairwiseTreeComparator {

    public static <T extends EvolNode> PairwisePaintingPanel plotTree(T root1, T root2, Font font,
                                                                      Dimension dim, OneNodeDrawer<T> drawer1, OneNodeDrawer<T> drawer2) {

        ReflectGraphicNode<T> reflectGraphicNode1 = new ReflectGraphicNode<>(root1);
        ReflectGraphicNode<T> reflectGraphicNode2 = new ReflectGraphicNode<>(root2);

        PairwisePaintingPanel<T> paintingPanel = new PairwisePaintingPanel<>(reflectGraphicNode1, reflectGraphicNode2,
                drawer1, drawer2);
        paintingPanel.setSize(dim);
        paintingPanel.setNameFont(font);

        paintingPanel.calculatePositions(dim.width, dim.height);
        paintingPanel.setBackground(Color.white);

        return paintingPanel;
    }

    public static <T extends EvolNode> JPanel plotTreeWithGUI(T root1, T root2, Font font,
                                                              Dimension dim, OneNodeDrawer<T> drawer1, OneNodeDrawer<T> drawer2) {

        PairwisePaintingPanel<T> paintingPanel = plotTree(root1, root2, font, dim,
                drawer1, drawer2);
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quick pairwise tree");

            frame.add(paintingPanel);

            frame.setSize(dim);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        });
        return paintingPanel;
    }

    @SuppressWarnings("unchecked")
    public static <T extends EvolNode> JPanel plotTree(T root1, T root2, Font font, Dimension dim,
                                                       OneNodeDrawer<T> drawer1, OneNodeDrawer<T> drawer2, String strToRoot) {

        try {
            root1 = (T) EvolTreeOperator.setRootAt(root1, strToRoot);
            root2 = (T) EvolTreeOperator.setRootAt(root2, strToRoot);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        EvolNodeUtil.ladderizeNode(root1, true);
        EvolNodeUtil.ladderizeNode(root2, true);

        return plotTree(root1, root2, font, dim, drawer1, drawer2);
    }

    public static void main(String[] args) throws Exception {

//        InputStream inputStream = FastGraphicsProperties.class.getResourceAsStream("test.tree.txt");
//        List<String> content = EGPSFileUtil.getContentFromInputStreamAsLines(inputStream);
//        String line = content.get(3);
        String line = "Line";

        TreeDecoder treeDecoder = new TreeDecoder();
        /**
         * 用括号标记法可以编码一个进化树，标准的nwk(nh)格式并没有定义内节点的名字，我们这里可以定义，并且传递给name属性
         */
        EvolNode rootNode = treeDecoder.decode(line);
        EvolNode rootNode2 = treeDecoder.decode(line);
        EvolNodeUtil.swapNodeWithSibling(rootNode2.getFirstChild());

        OneNodeDrawer<EvolNode> drawer1 = (g2d, node) -> {
            int xSelf = (int) node.getXSelf();
            int ySelf = (int) node.getYSelf();
            int xParent = (int) node.getXParent();
            if (node.getChildCount() == 0) {
                g2d.drawString(node.getReflectNode().getName(), xSelf + 5, ySelf + 5);
            }

            String lenString = String.valueOf(node.getLength());
            int xx = (xSelf + xParent - g2d.getFontMetrics().stringWidth(lenString)) / 2;
            int yy = ySelf - 5;

//			g2d.drawString(lenString, xx, yy);
        };
        OneNodeDrawer<EvolNode> drawer2 = (g2d, node) -> {
            int xSelf = (int) node.getXSelf();
            int ySelf = (int) node.getYSelf();
            int xParent = (int) node.getXParent();
            if (node.getChildCount() == 0) {
                FontMetrics fontMetrics = g2d.getFontMetrics();
                String name = node.getReflectNode().getName();
                int stringWidth = fontMetrics.stringWidth(name);
                g2d.drawString(name, xSelf - 5 - stringWidth, ySelf + 5);
            }

            String lenString = String.valueOf(node.getLength());
            int xx = (xSelf + xParent - g2d.getFontMetrics().stringWidth(lenString)) / 2;
            int yy = ySelf - 5;

//			g2d.drawString(lenString, xx, yy);
        };
        Font font = new Font("Arial", Font.PLAIN, 12);
//		plotTree(rootNode, rootNode2, "Reroot with certain otu", new Dimension(1200, 800), drawer1, drawer2,"TaxonI");
        plotTreeWithGUI(rootNode, rootNode2, font, new Dimension(1200, 800), drawer1, drawer2);
    }
}