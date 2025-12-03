package graphic.engine.guirelated;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * A convenience class used to display a Chart in a barebones Swing application
 *
 */
public class EGPSSwingWrapper {

	private String windowTitle = "XChart";
	private boolean isCentered = true;

	/** Display the chart in a Swing JFrame */
	public JFrame displayChart(SwingDisplayInterface chart,Dimension dim) throws Exception {

		JPanel jPanel = new JPanel() {
			private static final long serialVersionUID = -6641395584513630276L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				Graphics2D g2d = (Graphics2D) g;
				chart.visualize(g2d);
			}
		};
		
		jPanel.setPreferredSize(dim);

		// Create and set up the window.
		final JFrame frame = new JFrame(windowTitle);

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

			@Override
			public void run() {

				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.add(jPanel);
				frame.setSize(dim.width, dim.height);

				// Display the window.
				if (isCentered) {
					frame.setLocationRelativeTo(null);
				}
				frame.setVisible(true);
			}
		});

		return frame;
	}

	/**
	 * Set the Window in the center of screen
	 *
	 * @param isCentered
	 * @return
	 */
	public EGPSSwingWrapper isCentered(boolean isCentered) {
		this.isCentered = isCentered;
		return this;
	}

	/**
	 * Set the Window Title
	 *
	 * @param windowTitle
	 * @return
	 */
	public EGPSSwingWrapper setTitle(String windowTitle) {
		this.windowTitle = windowTitle;
		return this;
	}
	
	public static void main(String[] args) throws Exception {
		EGPSSwingWrapper swingWrapper = new EGPSSwingWrapper();
		swingWrapper.displayChart(g -> {
			g.drawString("Hello world !!!", 400, 400);
		}, new Dimension(800, 800));
	}
	
}
