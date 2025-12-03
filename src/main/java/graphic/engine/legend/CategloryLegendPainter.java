package graphic.engine.legend;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Painter for rendering categorical legends with color boxes and labels.
 */
public class CategloryLegendPainter {

	private int size = 10;
	private Rectangle2D location;
	private Collection<Pair<String, Color>> listOfItems;

	public CategloryLegendPainter(int size, Rectangle2D location, Collection<Pair<String, Color>> listOfItems) {
		super();
		this.size = size;
		this.location = location;
		this.listOfItems = listOfItems;
	}


	public void paint(Graphics2D g2d) {

		FontMetrics fontMetrics = g2d.getFontMetrics();
		int height = fontMetrics.getHeight();
		g2d.drawString("Annotation", (float) location.getX(), (float) location.getY());

		Iterator<Pair<String, Color>> iterator = listOfItems.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			Pair<String, Color> pair = iterator.next();
			double yy = location.getY() + 5 + index * size;
			g2d.setColor(pair.getRight());
			Rectangle2D.Double s = new Rectangle2D.Double(location.getX(), yy, size, size);
			g2d.fill(s);

			g2d.setColor(Color.black);
			g2d.drawString(pair.getLeft(), (float) (s.getMaxX() + 6), (float) s.getCenterY());

			index++;
		}

	}

}