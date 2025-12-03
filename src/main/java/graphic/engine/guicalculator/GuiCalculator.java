package graphic.engine.guicalculator;

import java.awt.geom.Point2D;

/**
 * Calculator for GUI coordinate transformations including circular and spiral layouts.
 */
public class GuiCalculator {

	public static Point2D.Double calculateCircularLocation(double angle, double r, double x, double y) {

		double radians = Math.toRadians(angle);
		double a = x + r * Math.cos(radians);
		double b = y - r * Math.sin(radians);

		return new Point2D.Double(a, b);
	}

	public static Point2D.Double calculateSpiralLocation(double alpha, double beta, double angle, double x, double y) {

		double radians = Math.toRadians(angle);
		double r = alpha + beta * radians;
		double a = x + r * Math.cos(radians);
		double b = y - r * Math.sin(radians);

		return new Point2D.Double(a, b);
	}
}