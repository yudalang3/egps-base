package evoltree.tanglegram;

import evoltree.struct.EvolNode;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.txtdisplay.ReflectGraphicNode;
import org.apache.commons.lang3.mutable.MutableDouble;

import java.awt.*;
import java.util.List;

/**
 * Calculator for left-side rectangular layout in tanglegram visualization.
 */
public class LeftRectangularLayoutQuickCalculator extends BaseRectangularLayoutCalculator {

	double highUnit;
	int heightIndex = 0;

	@Override
	public <T extends EvolNode> void calculateTree(ReflectGraphicNode<T> root, Dimension dim) {
		heightIndex = 0;
		List<ReflectGraphicNode<T>> leaves = EvolNodeUtil.getLeaves(root);

		MutableDouble depth = new MutableDouble(-100);

		iterate2getMaxDepth(depth, root, 0.0);

		int size = leaves.size();

		double calculateWidth = dim.getWidth() - blankLength - blankLength;
		double calculateHeight = dim.getHeight() - blankLength - blankLength;

		highUnit = calculateHeight / size;

		double widthRatio = calculateWidth / depth.doubleValue();

		// 不计算根
		iterateTree2assignLocation(root, highUnit, widthRatio, -root.getLength());
	}

	private <T extends EvolNode> void iterate2getMaxDepth(MutableDouble depth, ReflectGraphicNode<T> node,
			double oneLeafDepth) {

		oneLeafDepth += node.getReflectNode().getLength();

		int childCount = node.getChildCount();

		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				@SuppressWarnings("unchecked")
				ReflectGraphicNode<T> childAt = (ReflectGraphicNode<T>) node.getChildAt(i);
				iterate2getMaxDepth(depth, childAt, oneLeafDepth);
			}
		} else {
			if (oneLeafDepth > depth.doubleValue()) {
				depth.setValue(oneLeafDepth);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected <T extends EvolNode> void iterateTree2assignLocation(ReflectGraphicNode<T> node, double heighUnit,
																   double widthRatio, double depth) {
		depth += node.getLength();

		double xx = widthRatio * depth + blankLength;

		node.setXSelf(xx);

		ReflectGraphicNode<T> parent = (ReflectGraphicNode<T>) node.getParent();
		if (parent == null) {
			node.setXParent(xx - 10);
		} else {
			node.setXParent(parent.getXSelf());
		}

		int childCount = node.getChildCount();

		if (childCount > 0) {

			double yy = 0.0D;
			for (int i = 0; i < childCount; i++) {
				ReflectGraphicNode<T> child = EvolNodeUtil.getChildrenAt(node, i);
				iterateTree2assignLocation(child, heighUnit, widthRatio, depth);

				yy += child.getYSelf();
			}

			yy /= childCount;

			node.setYSelf(yy);
			node.setYParent(yy);
		} else {

			double yy = blankLength + heightIndex * heighUnit;
			node.setYSelf(yy);
			node.setYParent(yy);

			heightIndex++;
		}
	}
}