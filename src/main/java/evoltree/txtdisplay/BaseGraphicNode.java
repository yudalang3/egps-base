
package evoltree.txtdisplay;

import evoltree.struct.ArrayBasedNode;

/**
 * Base class for phylogenetic tree nodes with graphical display properties.
 */
public class BaseGraphicNode extends ArrayBasedNode {
	protected double x1 = 0.0;
	protected double x2 = 0.0;
	protected double y1 = 0.0;
	protected double y2 = 0.0;

	protected boolean hideNodeFlag = true;
	protected TreeDrawUnit drawUnit = new TreeDrawUnit();
	/**
	 * 原来那个 branchLength属性用来作为 displayLength了
	 */
	protected double realBranchLength = 1;
	/**
	 * 针对spiral等旋转型的树显示方式，节点的旋转角
	 */
	protected double angle;
	/** the depth of the node; if the node is leaf, then depth = 0 */
	protected int depth;

	protected double radius;

	public BaseGraphicNode() {
		super();

	}

	public BaseGraphicNode(String name) {
		this();
		setName(name);
	}

	public double getXParent() {
		return this.x1;
	}

	public double getXSelf() {
		return this.x2;
	}

	public double getYSelf() {
		return this.y2;
	}

	public void setXParent(double x11) {
		this.x1 = x11;
	}

	public void setXSelf(double x22) {
		this.x2 = x22;
	}

	public void setYSelf(double yy) {
		this.y2 = yy;
	}

	public double getYParent() {
		return this.y1;
	}

	public void setYParent(double y11) {
		this.y1 = y11;
	}

	public void setAngleIfNeeded(double angles) {
		this.angle = angles;
	}

	public double getAngleIfNeeded() {
		return this.angle;
	}

	public int getDepth() {
		return this.depth;
	}

	public void setDepth(int dep) {
		this.depth = dep;
	}

	public boolean getCollapse() {
		return drawUnit.isCollapse();
	}

	/**
     * 存放树的相关信息:
     * 一些关于设计方面的考虑：
     * 1. 进化树要支持大树的可视化
     * 2. 存放了一个 root node在这里，是一个独立的树
     *
     */

}