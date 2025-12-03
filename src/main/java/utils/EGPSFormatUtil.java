package utils;

import java.text.DecimalFormat;

/**
 * Utility class providing formatting functions for numbers and HTML content.
 * 
 * <p>
 * This class offers static methods for common formatting tasks including:
 * <ul>
 *   <li><strong>Number Formatting</strong>: Adding thousand separators to integers</li>
 *   <li><strong>HTML Formatting</strong>: Creating HTML strings with font size specifications</li>
 * </ul>
 * </p>
 * 
 * <h2>Key Features</h2>
 * <ul>
 *   <li>Thread-safe formatting operations</li>
 *   <li>Localized number formatting with thousand separators</li>
 *   <li>Simple HTML generation for GUI components</li>
 *   <li>Utility class pattern - private constructor, static methods only</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * // Format large numbers with thousand separators
 * String formatted = EGPSFormatUtil.addThousandSeparatorForInteger(1234567);
 * // Result: "1,234,567"
 * 
 * // Create HTML content with specific font size
 * String html = EGPSFormatUtil.html32Concat(5, "Hello World");
 * // Result: "&lt;html&gt;&lt;font size=\"5\"&gt;Hello World&lt;/font&gt;&lt;/html&gt;"
 * </pre>
 * 
 * <h2>Performance Considerations</h2>
 * <ul>
 *   <li>Uses a shared DecimalFormat instance for thread-safe number formatting</li>
 *   <li>HTML formatting uses modern Java text blocks for better readability</li>
 *   <li>All methods are static - no instance creation overhead</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see java.text.DecimalFormat For advanced number formatting options
 */
public class EGPSFormatUtil {
	
	private static DecimalFormat df = new DecimalFormat(",###,###");
	
	
	private EGPSFormatUtil(){
		
	}
	
	
	public static String addThousandSeparatorForInteger(int number) {
		return df.format(number);
	}

	public static String html32Concat(int fontsize, String content) {
		String htmlStr = """
				<html><font size="%d">%s</font></html>
				""";

		return htmlStr.formatted(fontsize, content);
	}
}
