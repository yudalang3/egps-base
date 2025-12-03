package graphic.engine.guibean;

import java.awt.Insets;

/**
 * Custom insets class providing working area calculations for GUI components.
 */
@SuppressWarnings("serial")
public class EGPSInsets extends Insets{

	public EGPSInsets() {
		super(20,20,20,20);
	}
	
	public EGPSInsets(int top, int left, int bottom, int right) {
		super(top, left, bottom, right);
	}
	
	public int getWorkWidth(int width) {
		return width - left - right;
	}
	
	public int getWorkHeight(int height) {
		return height - top - bottom;
	}
}

