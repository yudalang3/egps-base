package evoltree.txtdisplay;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * Line or Arcs!
 *
 */
public class TreeDrawUnit {

	/** line BasicStroke */
	private BasicStroke stroke;
	/**
	 * 节点的圆的半径大小
	 */
	private int circleRadius = 1;
	/**
	 * This is like a temporary variable to draw line!
	 */
	private Color lineColor = Color.black;

	/**
	 * 这个属性设置为true之后，在界面上这个点就会是一个开口朝向根的三角形(一半情况下是一个三角形)。
	 * 现在是鼠标右击节点之后，
	 */
	private boolean isCollapse;

	private boolean drawName = false;

	/**
	 * <pre>
	 * 
	 *      |----- . Leaf1
	 * -----|.Parent
	 *      |----- . Leaf2
	 *      
	 * 对于 Leaf1来说：
	 * branch selected就是最上面那条虚线！
	 * node selected 就是一个点.
	 * 
	 * 
	 * </pre>
	 * 这个属性设置为true之后，在界面上这个点就会是一个选中状态。现在是鼠标单击节点之后，这个节点会被设置为true
	 * 
	 */
	private boolean hasBranchSelected;
	/**
	 * 这个属性设置为true之后，在界面上这个点就会是一个选中状态。现在是Tree Operation 控制栏Search到节点之后，这个节点会被设置为true
	 */
	private boolean hasThisNodeSelected;

	public TreeDrawUnit() {
		recover2PrimaryStates();
	}

	public void recover2PrimaryStates() {
		lineColor = Color.BLACK;
		stroke = new BasicStroke(1.6f);
		circleRadius = 0;
	}

	public final boolean isBranchSelected() {
		return hasBranchSelected;
	}

	public final void setBranchSelected(boolean hasSelected) {
		this.hasBranchSelected = hasSelected;
	}

	public final Color getLineColor() {
		return lineColor;
	}

	public final void setLineColor(Color lineColor) {
 		this.lineColor = lineColor;
	}

	public final BasicStroke getStroke() {
		return stroke;
	}

	public final void setStroke(BasicStroke bStroke) {
		this.stroke = bStroke;
	}

	public final float getStrokeSize() {
		return stroke.getLineWidth();
	}

	public final void setStrokeSize(float bStrokeSize) {
		stroke = new BasicStroke(bStrokeSize);
	}

	public boolean isNodeSelected() {
		return hasThisNodeSelected;
	}

	public void setNodeSelected(boolean isCenSel) {
		this.hasThisNodeSelected = isCenSel;
	}

	public int getCircleRadius() {
		return circleRadius;
	}

	public void setCircleRadius(int circleRadius) {
		this.circleRadius = circleRadius;
	}

	public boolean isCollapse() {
		return isCollapse;
	}

	public void setCollapse(boolean isCollapse) {
		this.isCollapse = isCollapse;
	}

	public boolean isDrawName() {
		return drawName;
	}

	public void setDrawName(boolean drawName) {
		this.drawName = drawName;
	}
	
}
