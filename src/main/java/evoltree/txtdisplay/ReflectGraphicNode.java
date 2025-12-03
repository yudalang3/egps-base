
package evoltree.txtdisplay;

import evoltree.struct.EvolNode;

import java.awt.*;

/**
 * In fact, this is an agent to draw a phylogenetic tree. The reflectNode is the node to carry the phylogenetic information.
 * @lastModifiedDate 20250407
 * @author yudalang
 *
 * @param <T>
 */
public class ReflectGraphicNode<T extends EvolNode> extends BaseGraphicNode {
	T reflectNode;

	public ReflectGraphicNode() {
	}

	public void setReflectNode(T reflectNode) {
		this.reflectNode = reflectNode;
	}

	public T getReflectNode() {
		return this.reflectNode;
	}

	public ReflectGraphicNode(T root) {
		String name = root.getName();
		if (name != null) {
			this.name = name;
		}
		this.ID = root.getID();

		setLength(root.getLength());
		setReflectNode(root);
		setSize(root.getSize());

		int childCount = root.getChildCount();
		for (int i = 0; i < childCount; i++) {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			ReflectGraphicNode<T> tt = new ReflectGraphicNode(root.getChildAt(i));
			addChild(tt);
		}
	}
	public BasicStroke getStroke() {
		return drawUnit.getStroke()	;
	}
	
	public void setStroke(BasicStroke stroke) {
		this.drawUnit.setStroke(stroke);
	}

	public boolean isCollapse() {
		return drawUnit.isCollapse();
	}

	public void setCollapse(boolean isCollapse) {
		this.drawUnit.setCollapse(isCollapse);
	}

}
