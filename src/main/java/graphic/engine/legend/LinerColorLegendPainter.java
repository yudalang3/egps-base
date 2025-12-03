package graphic.engine.legend;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import graphic.engine.AxisTickCalculator;
import graphic.engine.GradientColorHolder;

/**
 * Painter for rendering continuous linear color legends with gradient bars and tick marks.
 */
public class LinerColorLegendPainter {

	final int markInterval = 4;
	private double maxValue;
	private double minValue;
	private List<String> tickLabels;
	private List<Integer> tickLocations;
	private GradientColorHolder gColorHolder;
	private Color[] reverseArrays;
	private LinearGradientPaint linearGradientPaint;
	private Rectangle2D areaLocation;

	public LinerColorLegendPainter(GradientColorHolder gColorHolder, double maxValue, double minValue,
			Rectangle2D areaLocation) {
		this.maxValue = maxValue;
		this.minValue = minValue;
		AxisTickCalculator cal = new AxisTickCalculator();

		cal.setMinAndMaxPair(Pair.of(minValue, maxValue));

		cal.setWorkingSpace((int) areaLocation.getHeight());
		cal.setWorkSpaceRatio(1f);
		cal.determineAxisTick();

		tickLabels = cal.getTickLabels();
		tickLocations = cal.getTickLocations();

		Color[] clone = gColorHolder.getColors().clone();
		reverseArrays = reverseArray(clone);
		this.gColorHolder = gColorHolder;

		float x = (float) areaLocation.getX();
		float y = (float) areaLocation.getY();
		float w = (float) areaLocation.getWidth();
		float h = (float) areaLocation.getHeight();

		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);

		linearGradientPaint = new LinearGradientPaint(x, y, x + w, y + h, gColorHolder.getDist(),
				reverseArrays);
		this.areaLocation = (Rectangle2D) areaLocation.clone();

	}

	public void painting(Graphics2D g2d) {

		g2d.setPaint(linearGradientPaint);
		g2d.fill(areaLocation);

		// 覆盖了
		g2d.setPaint(Color.black);
		int fht = g2d.getFontMetrics().getHeight();

		double range = maxValue - minValue;

		Iterator<String> labelIterator = tickLabels.iterator();
		for (Integer color : tickLocations) {
			int yy = (int) (areaLocation.getY() + areaLocation.getHeight() - color);
			String label = labelIterator.next();

			int xx = (int) (areaLocation.getX() + areaLocation.getWidth() + 6);
			g2d.drawString(label, xx, yy);

		}

	}

	private Color[] reverseArray(Color[] arr) {
		List<Color> list = Arrays.asList(arr);
		Collections.reverse(list);
		return (Color[]) list.toArray();
	}

}