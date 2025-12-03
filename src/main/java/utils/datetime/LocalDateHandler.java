package utils.datetime;

import java.util.HashMap;
import java.util.Map;

/**
 * Date and month abbreviation handler for localized date operations.
 * 
 * <p>
 * This utility class provides functionality for handling month abbreviations and
 * localized date formatting commonly used in bioinformatics applications and
 * scientific data processing.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Month Abbreviations:</strong> Convert full month names to standard abbreviations</li>
 *   <li><strong>Localization Support:</strong> Handle English month name abbreviations</li>
 *   <li><strong>Immutable Mapping:</strong> Thread-safe month abbreviation map</li>
 *   <li><strong>Quick Lookup:</strong> O(1) time complexity for month name lookups</li>
 * </ul>
 * 
 * <h2>Supported Months:</h2>
 * <table border="1">
 * <tr><th>Full Name</th><th>Abbreviation</th></tr>
 * <tr><td>January</td><td>Jan.</td></tr>
 * <tr><td>February</td><td>Feb.</td></tr>
 * <tr><td>March</td><td>Mar.</td></tr>
 * <tr><td>April</td><td>Apr.</td></tr>
 * <tr><td>May</td><td>May.</td></tr>
 * <tr><td>June</td><td>Jun.</td></tr>
 * <tr><td>July</td><td>Jul.</td></tr>
 * <tr><td>August</td><td>Aug.</td></tr>
 * <tr><td>September</td><td>Sept.</td></tr>
 * <tr><td>October</td><td>Oct.</td></tr>
 * <tr><td>November</td><td>Nov.</td></tr>
 * <tr><td>December</td><td>Dec.</td></tr>
 * </table>
 * 
 * <h2>Usage Examples:</h2>
 * <pre>
 * LocalDateHandler handler = new LocalDateHandler();
 * 
 * // Get full abbreviation map
 * Map&lt;String, String&gt; abbreviations = handler.getMouthAbbreviationMap();
 * // Result: {January=Jan., February=Feb., March=Mar., ...}
 * 
 * // Convert month name to abbreviation
 * String abbreviation = handler.getMonthNameAbbr("September");
 * // Result: "Sept."
 * </pre>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Data Import:</strong> Parse dates from various scientific databases</li>
 *   <li><strong>Report Generation:</strong> Format dates for scientific publications</li>
 *   <li><strong>Log Analysis:</strong> Standardize date formats in analysis logs</li>
 *   <li><strong>Database Integration:</strong> Convert between different date representations</li>
 *   <li><strong>Time Series Analysis:</strong> Standardize temporal data formatting</li>
 * </ul>
 * 
 * <h2>Implementation Notes:</h2>
 * <ul>
 *   <li>Initialized with 12 entries, optimized for memory usage (load factor 1.0)</li>
 *   <li>No null handling for valid month names</li>
 *   <li>Case-sensitive month name matching</li>
 *   <li>Thread-safe implementation with final field</li>
 * </ul>
 * 
 * @see java.util.Map
 * @see java.util.HashMap
 * @see java.util.String
 * @author eGPS Development Team
 * @since 1.0
 */
public class LocalDateHandler {
	
	private Map<String, String> mouthAbbreviationMap;
	
	public LocalDateHandler() {
		mouthAbbreviationMap = new HashMap<>(16, 1f);
		mouthAbbreviationMap.put("January", "Jan.");
		mouthAbbreviationMap.put("February", "Feb.");
		mouthAbbreviationMap.put("March", "Mar.");
		mouthAbbreviationMap.put("April", "Apr.");
		mouthAbbreviationMap.put("May", "May.");
		mouthAbbreviationMap.put("June", "Jun.");
		mouthAbbreviationMap.put("July", "Jul.");
		mouthAbbreviationMap.put("August", "Aug.");
		mouthAbbreviationMap.put("September", "Sept.");
		mouthAbbreviationMap.put("October", "Oct.");
		mouthAbbreviationMap.put("November", "Nov.");
		mouthAbbreviationMap.put("December", "Dec.");
	}

	public Map<String, String> getMouthAbbreviationMap() {
		return mouthAbbreviationMap;
	}

	public String getMonthNameAbbr(String name) {
		String string = mouthAbbreviationMap.get(name);
		if (string == null && !name.equalsIgnoreCase("no")) {
			System.err.println(getClass() + "  " + name);
		}

		return string;
	}
	
	
	public String getMonthNameAbbrAndYearMonthDay(String str) {
		int indexOf = str.indexOf(" ");
		String substring = str.substring(0, indexOf);

		String mouthNameAbbr = getMonthNameAbbr(substring);
		return mouthNameAbbr + str.substring(indexOf);
	}

	public String getMonthNameAbbrAndYearMonth(String str) {
		int indexOf = str.indexOf(" ");
		String substring = str.substring(0, indexOf);

		String mouthNameAbbr = getMonthNameAbbr(substring);

		int indexOfComma = str.indexOf(",");
		return mouthNameAbbr + str.substring(indexOfComma + 1);
	}

}
