package tsv.io;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Container class for tabular data (TSV/table format).
 * 
 * <p>
 * KitTable is a JavaBean that stores tabular data in a structured format,
 * including headers, content rows, and original line text. It provides
 * convenient access to table dimensions and data manipulation.
 * </p>
 * 
 * <h2>Data Structure:</h2>
 * <ul>
 *   <li><strong>headerNames:</strong> List of column names</li>
 *   <li><strong>contents:</strong> List of rows, where each row is a List<String></li>
 *   <li><strong>originalLines:</strong> Original text lines from file (for preservation)</li>
 *   <li><strong>path:</strong> Source file path (optional)</li>
 * </ul>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Row-Column Access:</strong> Access data by row and column</li>
 *   <li><strong>Dimension Queries:</strong> Get number of rows and columns</li>
 *   <li><strong>Save to File:</strong> Write table back to TSV format</li>
 *   <li><strong>Original Line Preservation:</strong> Keep original formatting</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Read from file
 * KitTable table = TSVReader.readTsvTextFile("data.tsv");
 * 
 * // Access data
 * System.out.println("Rows: " + table.getNumOfRows());
 * System.out.println("Columns: " + table.getNumOfColum());
 * 
 * // Get headers
 * List<String> headers = table.getHeaderNames();
 * 
 * // Get content
 * List<List<String>> data = table.getContents();
 * String firstRowFirstCol = data.get(0).get(0);
 * 
 * // Save to file
 * table.save2file("output.tsv");
 * </pre>
 * 
 * <h2>Structure Example:</h2>
 * <pre>
 * Headers: ["Name", "Age", "City"]
 * Row 0:   ["Alice", "30", "NY"]
 * Row 1:   ["Bob", "25", "LA"]
 * </pre>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see TSVReader
 * @see TSVWriter
 */
public class KitTable {

	/**
	 * contents per row, for example
	 */
	private List<List<String>> contents;
	/**
	 * original lines
	 */
	private List<String> originalLines;
	/**
	 * header name list
	 */
	private List<String> headerNames;
	// file path
	private String path;
	
	public KitTable() {
		contents = Lists.newArrayList();
		headerNames = Lists.newArrayList();
		originalLines = Lists.newArrayList();
	}

	/**
	 * Returns the number of data rows (excluding header).
	 * 
	 * @return the number of rows
	 */
	public int getNumOfRows() {
		return contents.size();
	}
	
	/**
	 * Returns the number of columns.
	 * 
	 * @return the number of columns
	 */
	public int getNumOfColum() {
		return headerNames.size();
	}

	public List<List<String>> getContents() {
		return contents;
	}

	public void setContents(List<List<String>> contents) {
		this.contents = contents;
	}

	public List<String> getOriginalLines() {
		return originalLines;
	}

	public void setOriginalLines(List<String> originalLines) {
		this.originalLines = originalLines;
	}

	public List<String> getHeaderNames() {
		return headerNames;
	}

	public void setHeaderNames(List<String> headerNames) {
		this.headerNames = headerNames;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		if (path != null) {
			sBuilder.append("Path: ").append(path).append("\n");
		}
		
		sBuilder.append("Header: ").append(headerNames.toString()).append("\n");
		sBuilder.append("Number of cols: ").append(getNumOfColum()).append("\n");
		sBuilder.append("Number of row: ").append(getNumOfRows()).append("\n");
		
		return sBuilder.toString();
	}

	/**
	 * Saves the table to a TSV file.
	 * 
	 * <p>
	 * Writes the table with headers and all content rows to the specified file.
	 * Each row is tab-separated.
	 * </p>
	 * 
	 * @param path the output file path
	 * @throws IOException if an I/O error occurs
	 */
	public void save2file(String path) throws IOException {

		List<String> outputLines = new LinkedList<>();

		if (headerNames != null && !headerNames.isEmpty()) {
			StringJoiner stringJoiner = new StringJoiner("\t");
			for (String string : headerNames) {
				stringJoiner.add(string);
			}
			outputLines.add(stringJoiner.toString());
		}

		for (List<String> list : contents) {
			StringJoiner stringJoiner = new StringJoiner("\t");
			for (String string : list) {
				stringJoiner.add(string);
			}
			outputLines.add(stringJoiner.toString());
		}

		FileUtils.writeLines(new File(path), outputLines);
	}
}
