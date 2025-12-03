package graphic.engine.guirelated;

import java.awt.*;
import java.awt.geom.PathIterator;

/**
 * Debugging utility for visualizing the internal structure of Shape objects.
 */
public class SwingDebugger {
    /**
     * Usage： describePath(new Ellipse2D.Double(0, 0, 72, 72)); describePath(new
     * java.awt.geom.Arc2D.Double(0, 0, 100, 100, 0, 30, 0));
     *
     * 可以直接看到一个图形底层的绘制。
     *
     * @param s 一个Shape
     */
    public static void describePath(Shape s) {
        PathIterator pi = s.getPathIterator(null);

        while (!pi.isDone()) {
            describeCurrentSegment(pi);
            pi.next();
        }

    }

    private static void describeCurrentSegment(PathIterator pi) {
        double[] coordinates = new double[6];
        int type = pi.currentSegment(coordinates);
        switch (type) {
            case PathIterator.SEG_MOVETO:
                System.out.println("move to " + coordinates[0] + ", " + coordinates[1]);
                break;
            case PathIterator.SEG_LINETO:
                System.out.println("line to " + coordinates[0] + ", " + coordinates[1]);
                break;
            case PathIterator.SEG_QUADTO:
                System.out.println("quadratic to " + coordinates[0] + ", " + coordinates[1] + ", " + coordinates[2] + ", "
                        + coordinates[3]);
                break;
            case PathIterator.SEG_CUBICTO:
                System.out.println("cubic to " + coordinates[0] + ", " + coordinates[1] + ", " + coordinates[2] + ", "
                        + coordinates[3] + ", " + coordinates[4] + ", " + coordinates[5]);
                break;
            case PathIterator.SEG_CLOSE:
                System.out.println("close");
                break;
            default:
                break;
        }
    }

}