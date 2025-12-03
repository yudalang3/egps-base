package graphic.engine.guirelated;

import java.awt.Graphics2D;


/**
 * Functional interface for rendering graphics to a Graphics2D context.
 */
@FunctionalInterface
public interface SwingDisplayInterface {
	
	void visualize(Graphics2D graphics2d);

}