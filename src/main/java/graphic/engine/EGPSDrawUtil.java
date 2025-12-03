package graphic.engine;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Line2D;

/**
 * Utility class providing common drawing operations for EGPS graphics including arrows, shapes, and geometric primitives.
 */
public class EGPSDrawUtil {

	/**
	 * 绘制一个箭头
	 * 
	 * @param g2d  Graphics2D 对象
	 * @param line Line2D，箭头的起点和终点
	 * @param size int，箭头的大小
	 */
	public static void drawArrow(Graphics2D g2d, Line2D line, int size) {
		// 绘制主线
		g2d.draw(line);

		// 计算箭头的向量
		double x1 = line.getX1();
		double y1 = line.getY1();
		double x2 = line.getX2();
		double y2 = line.getY2();

		// 计算线段方向角度
		double angle = Math.atan2(y2 - y1, x2 - x1);

		// 箭头两翼的角度
		double arrowAngle = Math.PI / 6; // 30度

		// 箭头两侧的坐标点
		double xArrow1 = x2 - size * Math.cos(angle - arrowAngle);
		double yArrow1 = y2 - size * Math.sin(angle - arrowAngle);

		double xArrow2 = x2 - size * Math.cos(angle + arrowAngle);
		double yArrow2 = y2 - size * Math.sin(angle + arrowAngle);

		// 绘制箭头的两翼
		g2d.drawLine((int) x2, (int) y2, (int) xArrow1, (int) yArrow1);
		g2d.drawLine((int) x2, (int) y2, (int) xArrow2, (int) yArrow2);
	}

	public static Double getRoundGeom(double x, double y, int size) {
		int dimeter = size + size;
		Double double2 = new Double(x - size, y - size, dimeter, dimeter);
		return double2;
	}

	public static Line2D.Double getLineGeom(double x1, double y1, double x2, double y2) {
		Line2D.Double double2 = new Line2D.Double(x1, y1, x2, y2);
		return double2;
	}

	public static java.awt.geom.RoundRectangle2D.Double getRoundRectangle(double x, double y, double w, double h,
			double arc, double arch) {
		java.awt.geom.RoundRectangle2D.Double ret = new java.awt.geom.RoundRectangle2D.Double(x, y, w, h, arc, arch);
		return ret;
	}

	public static java.awt.geom.Rectangle2D.Double getRectangle(double x, double y, double w, double h) {
		java.awt.geom.Rectangle2D.Double ret = new java.awt.geom.Rectangle2D.Double(x, y, w, h);
		return ret;
	}


}