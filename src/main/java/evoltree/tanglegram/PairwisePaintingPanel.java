package evoltree.tanglegram;

import evoltree.struct.EvolNode;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.swingvis.OneNodeDrawer;
import evoltree.txtdisplay.ReflectGraphicNode;
import phylo.algorithm.RobinsonFouldsMetricCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Swing panel for rendering pairwise phylogenetic tree comparison with tanglegram layout.
 */
class PairwisePaintingPanel<T extends EvolNode> extends JPanel {
	
	final ReflectGraphicNode<T> root1;
	final ReflectGraphicNode<T> root2;
	final Line2D.Double rectDouble = new Line2D.Double();

	final GeneralPath generalPath = new GeneralPath();
	final double curveRatio = 0.5;
	private Font nameFont = new JLabel().getFont();

	BaseRectangularLayoutCalculator leftCalculator = new LeftRectangularLayoutQuickCalculator();
	BaseRectangularLayoutCalculator rightCalculator = new RightRectangularLayoutQuickCalculator();

	Map<String, ReflectGraphicNode<T>> string2basicGraphicNodeMap = new HashMap<>();

	private final OneNodeDrawer<T> oneNodeDrawer1;
	private final OneNodeDrawer<T> oneNodeDrawer2;
	private int currentHeight;
	private int currenWidth;
	private final String rfDistanceStr;

    public PairwisePaintingPanel(ReflectGraphicNode<T> root1, ReflectGraphicNode<T> root2, OneNodeDrawer<T> oneNodeDrawer1,
								 OneNodeDrawer<T> oneNodeDrawer2) {
		this.oneNodeDrawer1 = oneNodeDrawer1;
		this.oneNodeDrawer2 = oneNodeDrawer2;
		this.root1 = root1;
		this.root2 = root2;

        RobinsonFouldsMetricCalculator calculator = new RobinsonFouldsMetricCalculator();
        int countDiff = calculator.countDiff(root1, root2);
		rfDistanceStr = "The Robinson-Foulds distance: " + countDiff;
	}
	
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int height2 = getHeight();
		int width2 = getWidth();
		if (height2 != currentHeight || width2 != currenWidth) {
			calculatePositions(width2,height2);
			currentHeight = height2;
			currenWidth = width2;
		}

//		g.fillRect(0, 0, 100, 100);
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		if (rfDistanceStr != null) {
			g2d.drawString(rfDistanceStr, 6, 16);
		}
		g2d.setFont(this.nameFont);
		iterateTree4root2(g2d, this.root2);
		iterateTree4root1(g2d, this.root1);
	}

	void calculatePositions(int width2, int height2) {
		int width3 = width2 / 2;
		Dimension dimension = new Dimension(width3, height2);
		leftCalculator.calculateTree(root1, dimension);
		rightCalculator.calculateTree(root2, dimension);

		string2basicGraphicNodeMap.clear();
		EvolNodeUtil.iterateByLevelTraverse(root2, node -> {
			node.setXSelf(node.getXSelf() + width3);
			node.setXParent(node.getXParent() + width3);

			if (node.getChildCount() == 0) {
				string2basicGraphicNodeMap.put(node.getReflectNode().getName(), node);
			}
		});
		
		
	}

	@SuppressWarnings("unchecked")
	private void iterateTree4root2(Graphics2D g2d, ReflectGraphicNode<T> node) {
		int childCount = node.getChildCount();

		this.oneNodeDrawer2.draw(g2d, node);

		this.rectDouble.setLine(node.getXSelf(), node.getYSelf(), node.getXParent(), node.getYParent());
		g2d.draw(this.rectDouble);

		if (node.getChildCount() > 0) {
			ReflectGraphicNode<T> firstChild = (ReflectGraphicNode<T>) node.getFirstChild();
			ReflectGraphicNode<T> lastChild = (ReflectGraphicNode<T>) node.getLastChild();
			this.rectDouble.setLine(firstChild.getXParent(), firstChild.getYParent(), lastChild.getXParent(),
					lastChild.getYParent());
			g2d.draw(this.rectDouble);
		}

		for (int i = 0; i < childCount; i++) {
			ReflectGraphicNode<T> childAt = (ReflectGraphicNode<T>) node.getChildAt(i);
			iterateTree4root2(g2d, childAt);
		}
	}

	@SuppressWarnings("unchecked")
	private void iterateTree4root1(Graphics2D g2d, ReflectGraphicNode<T> node) {
		int childCount = node.getChildCount();

		this.oneNodeDrawer1.draw(g2d, node);

		this.rectDouble.setLine(node.getXSelf(), node.getYSelf(), node.getXParent(), node.getYParent());
		g2d.draw(this.rectDouble);

		if (node.getChildCount() > 0) {
			ReflectGraphicNode<T> firstChild = (ReflectGraphicNode<T>) node.getFirstChild();
			ReflectGraphicNode<T> lastChild = (ReflectGraphicNode<T>) node.getLastChild();
			this.rectDouble.setLine(firstChild.getXParent(), firstChild.getYParent(), lastChild.getXParent(),
					lastChild.getYParent());
			g2d.draw(this.rectDouble);
		} else {
			// leaf
			String name = node.getReflectNode().getName();
			ReflectGraphicNode<T> reflectGraphicNode = string2basicGraphicNodeMap.get(name);
			if (reflectGraphicNode != null) {

				FontMetrics fontMetrics = g2d.getFontMetrics();

				int stringWidth = fontMetrics.stringWidth(name);

				double x1 = node.getXSelf() + 10 + stringWidth;
				double y1 = node.getYSelf();
				double x2 = reflectGraphicNode.getXSelf() - 8 - stringWidth;
				double y2 = reflectGraphicNode.getYSelf();
				
				double wanQv = curveRatio  * (x2 - x1);
				generalPath.reset();
				generalPath.moveTo(x1, y1);
				generalPath.curveTo(x1 + wanQv, y1, x2 - wanQv, y2, x2, y2);
				
				g2d.draw(generalPath);
			}
		}

		for (int i = 0; i < childCount; i++) {
			ReflectGraphicNode<T> childAt = (ReflectGraphicNode<T>) node.getChildAt(i);
			iterateTree4root1(g2d, childAt);
		}
	}
	
	public void setNameFont(Font nameFont) {
		this.nameFont = nameFont;
	}
}