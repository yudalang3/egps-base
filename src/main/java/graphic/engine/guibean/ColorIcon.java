package graphic.engine.guibean;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/** 
 * JButton set icon background color
 * Usage: Icon icon = new ColorIcon(Color.RED, 50, 50);
 * JButton label = new JButton(icon);
 *  
 * @title ColorIcon
 * @createdDate 2020-11-27 10:21
 * @lastModifiedDate 2020-11-27 10:21
 * @author mhl,yjn,ydl
 *
 */
public class ColorIcon implements Icon {
    private Color color;
    private final int width;
    private final int height;

    public ColorIcon(Color color, int width, int height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }

    public ColorIcon(Color color) {
        this.color = color;
        this.width = 17;
        this.height = 17;
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
    
    public void setColor(Color color) {
		this.color = color;
	}
    public Color getColor() {
		return color;
	}

}
