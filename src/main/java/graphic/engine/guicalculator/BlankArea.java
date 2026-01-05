package graphic.engine.guicalculator;

/**
 * It is bound with the fast calculation method to give full play to the advantages of object-oriented programming.
 * 
 * @author yudal
 *
 */
public class BlankArea{
	
    /**
     * The inset from the top.
     * This value is added to the Top of the rectangle
     * to yield a new location for the Top.
     *
     */
	private int top;

    /**
     * The inset from the left.
     * This value is added to the Left of the rectangle
     * to yield a new location for the Left edge.
     *
     */
    private int left;

    /**
     * The inset from the bottom.
     * This value is subtracted from the Bottom of the rectangle
     * to yield a new location for the Bottom.
     *
     */
    private int bottom;

    /**
     * The inset from the right.
     * This value is subtracted from the Right of the rectangle
     * to yield a new location for the Right edge.
     *
     */
    private int right;

	public BlankArea() {
		this(20, 20, 80, 20);
	}

	public BlankArea(int top, int left, int bottom, int right) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	@Override
	public String toString() {
		return "[top=" + top + ", left=" + left + ", bottom=" + bottom + ", right=" + right + "]";
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getWorkWidth(int width) {
		return width - left - right;
	}
	
	public int getWorkHeight(int height) {
		return height - top - bottom;
	}
}
