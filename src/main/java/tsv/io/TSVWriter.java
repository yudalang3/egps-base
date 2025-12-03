package tsv.io;

import com.google.common.collect.Lists;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

/**
 * Writer for Tab-Separated Values (TSV) files.
 * 
 * <p>
 * This class provides methods for writing tabular data to TSV format files.
 * It takes column-oriented data (Map<String, List<String>>) and converts it
 * to row-oriented TSV output.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Column-to-Row Conversion:</strong> Transforms column-wise data to row-wise output</li>
 *   <li><strong>Header Generation:</strong> Automatically creates header row from map keys</li>
 *   <li><strong>Tab Delimiter:</strong> Uses standard tab character separator</li>
 *   <li><strong>Buffered Writing:</strong> Efficient file I/O</li>
 * </ul>
 * 
 * <h2>Input Format:</h2>
 * <p>
 * Map<String, List<String>> where:
 * <ul>
 *   <li>Keys are column names (header)</li>
 *   <li>Values are lists of data for each column</li>
 * </ul>
 * </p>
 * 
 * <h2>Output Format:</h2>
 * <pre>
 * ColumnA	ColumnB	ColumnC
 * value1	value2	value3
 * value4	value5	value6
 * </pre>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * // Prepare column-wise data
 * Map<String, List<String>> data = new LinkedHashMap<>();
 * data.put("Name", Arrays.asList("Alice", "Bob", "Charlie"));
 * data.put("Age", Arrays.asList("30", "25", "35"));
 * data.put("City", Arrays.asList("NY", "LA", "SF"));
 * 
 * // Write to TSV file
 * TSVWriter.write(data, "output.tsv");
 * 
 * // Result:
 * // Name	Age	City
 * // Alice	30	NY
 * // Bob	25	LA
 * // Charlie	35	SF
 * </pre>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Export analysis results to spreadsheet-compatible format</li>
 *   <li>Save processed data for downstream tools</li>
 *   <li>Generate reports in tabular format</li>
 *   <li>Data interchange between bioinformatics tools</li>
 * </ul>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see TSVReader
 * @see KitTable
 */
public class TSVWriter {

	/**
	 * Writes column-oriented data to a TSV file.
	 * 
	 * <p>
	 * The method converts a map of column names to column data into
	 * a row-oriented TSV file with headers.
	 * </p>
	 * 
	 * <p><strong>Data Requirements:</strong></p>
	 * <ul>
	 *   <li>All lists in the map must have the same length</li>
	 *   <li>Map keys become column headers</li>
	 *   <li>Map iteration order determines column order (use LinkedHashMap for control)</li>
	 * </ul>
	 * 
	 * @param key2ListMap map of column names to column data
	 * @param tsvFile output file path
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(Map<String, List<String>> key2ListMap, String tsvFile) throws IOException {

		List<StringBuilder> output = Lists.newArrayList();

		StringJoiner header = new StringJoiner("\t");

		Iterator<Entry<String, List<String>>> iterator = key2ListMap.entrySet().iterator();
		{
			Entry<String, List<String>> entry = iterator.next();
			String key = entry.getKey();
			header.add(key);

			List<String> value = entry.getValue();


			int size = value.size();
            for (String string : value) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(string).append("\t");

                output.add(stringBuilder);
            }
		}
		while (iterator.hasNext()) {
			Entry<String, List<String>> entry = iterator.next();
			String key = entry.getKey();
			header.add(key);

			List<String> value = entry.getValue();
			int size = value.size();
			for (int i = 0; i < size; i++) {
				String string = value.get(i);

				StringBuilder stringBuilder = output.get(i);
				stringBuilder.append(string).append("\t");
			}
		}

		try (BufferedWriter br = new BufferedWriter(new FileWriter(tsvFile))) {
			br.write(header.toString());
			br.write("\n");
			for (StringBuilder sb : output) {
				sb.deleteCharAt(sb.length() - 1);
				String string = sb.toString();
				br.write(string);
				br.write("\n");
			}
		}

	}


	public static void main(String[] args) throws IOException {
		// Read the tsv file
		KitTable tsvTextFile1 = TSVReader.readTsvTextFile("input.tsv");
		KitTable tsvTextFile2 = TSVReader.readTsvTextFile("input.tsv", false);
		Map<String, List<String>> asKey2ListMap = TSVReader.readAsKey2ListMap("input.tsv");

		// Write the tsv file
		TSVWriter.write(asKey2ListMap, "output.tsv");
		// Or you can directly write
		try (BufferedWriter br = new BufferedWriter(new FileWriter("output.tsv"))) {
			br.write("");
		}
	}

}
