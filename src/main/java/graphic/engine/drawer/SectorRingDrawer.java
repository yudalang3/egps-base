package graphic.engine.drawer;

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

/**
 * Utility for drawing sector ring shapes (annular sectors) in graphics.
 */
public class SectorRingDrawer {
	/**
	 * 
	 * @param g2d
	 * @param x0       : center x of circle
	 * @param y0       : center y of circle
	 * @param r1       : big circle radius.
	 * @param r2       : small circle radius
	 * @param startDeg : start The starting angle of the arc in degrees.
	 * @param extent   : extent The angular extent of the arc in degrees
	 */
	public void drawSectorRing2(Graphics2D g2d, double x0, double y0, double r1, double r2, double startDeg,
			double extent) {

		Arc2D outer = new Arc2D.Double(x0 - r1, y0 - r1, 2 * r1, 2 * r1, startDeg, extent, Arc2D.PIE);
		Arc2D inner = new Arc2D.Double(x0 - r2, y0 - r2, 2 * r2, 2 * r2, startDeg, extent, Arc2D.PIE);
		Area area = new Area(outer);
		area.subtract(new Area(inner));

		g2d.fill(area);
	}

	public GeneralPath drawSectorRing1(Graphics2D g2d, double x0, double y0, double r1, double r2, double startDeg,
			double extent) {
		GeneralPath path = getSectorRingGeneralPath(g2d, x0, y0, r1, r2, startDeg, extent);
		g2d.fill(path);
		return path;
	}

	public GeneralPath getSectorRingGeneralPath(Graphics2D g2d, double x0, double y0, double r1, double r2,
			double startDeg, double extent) {
		// The center of circle
		double xx = x0;
		double yy = y0;
		double endDeg = startDeg + extent;

		double startRadians = Math.toRadians(startDeg);
		double cosStart = Math.cos(startRadians);
		double sinStart = Math.sin(startRadians);

		double x1 = xx + r1 * cosStart;
		double y1 = yy - r1 * sinStart;

		double x4 = xx + r2 * cosStart;
		double y4 = yy - r2 * sinStart;

		Arc2D arc1 = new Arc2D.Double(x0 - r1, y0 - r1, 2 * r1, 2 * r1, startDeg, extent, Arc2D.OPEN);

		double endRadians = Math.toRadians(endDeg);

		double cosEnd = Math.cos(endRadians);
		double sinEnd = Math.sin(endRadians);

		double x3 = xx + r2 * cosEnd;
		double y3 = yy - r2 * sinEnd;

		Arc2D arc2 = new Arc2D.Double(xx - r2, yy - r2, 2 * r2, 2 * r2, startDeg + extent, -extent, Arc2D.OPEN);

		// 可以精简
		GeneralPath path = new GeneralPath();
		path.append(arc1, true);
		path.append(arc2, true);
		path.closePath();

//		GeneralPath path = new GeneralPath();
//		path.moveTo(x4, y4);
//		path.lineTo(x1, y1);
//		path.append(arc1, true);
//		path.lineTo(x3, y3);
//		path.moveTo(x4, y4);
//		path.append(arc2, true);
//		path.closePath();

		return path;
	}
}