package graphic.engine;

import java.awt.BasicStroke;

/**
 * Utility class for creating and managing stroke styles for graphics rendering.
 */
public class EGPSStrokeUtil {

	public static BasicStroke getStroke(double thickness) {
		return new BasicStroke((float) thickness);
	}

}