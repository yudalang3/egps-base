package graphic.engine.guirelated;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;

import de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D;

/**
 * Utility for exporting Swing graphics and charts to PDF vector format.
 */
public class EGPSVectorExporter {

	public static void exportAsPDF(SwingDisplayInterface display, Dimension chart, File file) throws IOException {
		PDDocument document = new PDDocument();
		PDRectangle mediaBox = null;
		PDPage page = null;
		PDPageContentStream contentStream = null;
		PdfBoxGraphics2D pdfBoxGraphics2D = null;
		PDFormXObject xform = null;

		mediaBox = new PDRectangle(chart.width, chart.height);
		page = new PDPage(mediaBox);
		// add page
		document.addPage(page);
		pdfBoxGraphics2D = new PdfBoxGraphics2D(document, chart.width, chart.height);

		display.visualize(pdfBoxGraphics2D);

		pdfBoxGraphics2D.dispose();
		xform = pdfBoxGraphics2D.getXFormObject();

		contentStream = new PDPageContentStream(document, page);
		contentStream.drawForm(xform);
		contentStream.close();

		document.save(new FileOutputStream(file));
		document.close();
	}

	public static <T extends Chart<?, ?>> void displayMatrixAndSave(List<T> charts, int numRows, int numColumns,
			File file) throws IOException {

		// Create and set up the window.
		final JFrame frame = new JFrame("Xchart");

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				frame.getContentPane().setLayout(new GridLayout(numRows, numColumns));

				for (T chart : charts) {
					if (chart != null) {
						XChartPanel<T> chartPanel = new XChartPanel<T>(chart);
						frame.add(chartPanel);
					} else {
						JPanel chartPanel = new JPanel();
						frame.getContentPane().add(chartPanel);
					}
				}

				// Display the window.
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("This is window close..");

				try {
					exportAsPDF(g2d -> {

						Container contentPane = frame.getContentPane();
						contentPane.paintComponents(g2d);
					}, frame.getSize(), file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				super.windowClosed(e);
			}

			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("This is window closing..");

				try {
					exportAsPDF(g2d -> {

						Container contentPane = frame.getContentPane();
						contentPane.paintComponents(g2d);
					}, frame.getSize(), file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				super.windowClosing(e);
			}
		});

	}

}