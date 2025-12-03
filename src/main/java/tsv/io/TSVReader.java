package tsv.io;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.string.EGPSStringUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Reader for Tab-Separated Values (TSV) files with flexible parsing options.
 * 
 * <p>
 * This class provides multiple methods for reading TSV files, a common format in
 * bioinformatics and data science. TSV files use tabs ('\t') as column delimiters
 * and store tabular data in plain text format.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Header Support:</strong> Optional first-row header parsing</li>
 *   <li><strong>Comment Lines:</strong> Automatically skips lines starting with '#'</li>
 *   <li><strong>Multiple Output Formats:</strong> KitTable, Map&lt;String, List&gt;, Map&lt;String, String&gt;</li>
 *   <li><strong>Column-wise Access:</strong> Read data organized by columns</li>
 *   <li><strong>Annotation Filtering:</strong> Custom comment symbol support</li>
 *   <li><strong>Large File Support:</strong> 100MB buffer for large files</li>
 * </ul>
 * 
 * <h2>TSV Format Example:</h2>
 * <pre>
 * # This is a comment line
 * Name	Age	City
 * Alice	30	NY
 * Bob	25	LA
 * </pre>
 * 
 * <h2>Usage Examples:</h2>
 * <pre>
 * // Read as KitTable
 * KitTable table = TSVReader.readTsvTextFile("data.tsv");
 * 
 * // Read with custom annotation symbol
 * KitTable table2 = TSVReader.readTsvTextFile("data.tsv", true, "//");
 * 
 * // Read as column-wise map
 * Map&lt;String, List&lt;String&gt;&gt; colMap = TSVReader.readAsKey2ListMap("data.tsv");
 * System.out.println(colMap.get("Age")); // ["30", "25"]
 * 
 * // Extract two columns as key-value map
 * Map&lt;String, String&gt; nameToCity = TSVReader.organizeMap("Name", "City", "data.tsv");
 * // {"Alice"="NY", "Bob"="LA"}
 * </pre>
 * 
 * <h2>Auto-generated Headers:</h2>
 * <p>
 * If {@code hasHeader} is false, columns are automatically named V1, V2, V3, etc.
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see KitTable
 * @see TSVWriter
 */
public class TSVReader {

	private static final Logger logger = LoggerFactory.getLogger(TSVReader.class);

	public static KitTable readTsvTextFile(String fileLocation) throws IOException {
		return readTsvTextFile(fileLocation, true);
	}


    public static KitTable readTsvTextFile(String fileLocation, boolean hasHeader, String annotationSymbol) throws IOException {
        if (Objects.isNull(annotationSymbol)){
            return  readTsvTextFile(fileLocation, hasHeader);
        }
        KitTable kitTable = new KitTable();
        kitTable.setPath(fileLocation);

        List<String> strings = FileUtils.readLines(new File(fileLocation), StandardCharsets.UTF_8);
        strings.removeIf(line -> line.startsWith(annotationSymbol));

        Iterator<String> lineIterator = strings.iterator();
        readTsvContents(hasHeader, kitTable, lineIterator);
        return kitTable;
    }

    private static void readTsvContents(boolean hasHeader, KitTable kitTable, Iterator<String> lineIterator) {
        if (hasHeader) {
            String[] headers = EGPSStringUtil.split(lineIterator.next(), '\t');
            List<String> asList = Arrays.asList(headers);
            List<String> header = new ArrayList<>(asList);
            kitTable.getHeaderNames().addAll(header);
        }

        while (lineIterator.hasNext()) {
            String next = lineIterator.next();
            String[] line = EGPSStringUtil.split(next, '\t');
            List<String> asList = Arrays.asList(line);

            List<String> lineContent = new ArrayList<>(asList);

            kitTable.getContents().add(lineContent);
            kitTable.getOriginalLines().add(next);
        }
    }

    public static KitTable readTsvTextFile(String fileLocation, boolean hasHeader) throws IOException {
		KitTable kitTable = new KitTable();
		kitTable.setPath(fileLocation);

		LineIterator lineIterator = FileUtils.lineIterator(new File(fileLocation), "UTF-8");

        readTsvContents(hasHeader, kitTable, lineIterator);

        lineIterator.close();

		return kitTable;
	}

	public static Map<String, List<String>> readAsKey2ListMap(InputStream resourceAsStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8), 100 * 1024 * 1024);
		LineIterator lineIterator = new LineIterator(bufferedReader);
		return readAsKey2ListMap(lineIterator, true);

	}

	/**
	 * tsv file most has header line. Note, the string start with # symbol is the
	 * annotation and this will be excluded.
	 * 
	 * @param fileLocation the input tsv file
     */
	public static Map<String, List<String>> readAsKey2ListMap(String fileLocation) throws IOException {
		LineIterator lineIterator = FileUtils.lineIterator(new File(fileLocation), "UTF-8");
		return readAsKey2ListMap(lineIterator, true);
	}

	/**
	 * Tsv file most has header line. Note, the string start with # symbol is the
	 * annotation and this will be excluded.
	 * <p>
	 * If the header line is false: the column names is : V1, V2, V3 ...
	 * </p>
	 * 
	 * @param fileLocation the tsv file
	 * @param hasHeader    has the header line
     */
	public static Map<String, List<String>> readAsKey2ListMap(String fileLocation, boolean hasHeader)
			throws IOException {
		LineIterator lineIterator = FileUtils.lineIterator(new File(fileLocation), "UTF-8");
		return readAsKey2ListMap(lineIterator, hasHeader);
	}

	private static Map<String, List<String>> readAsKey2ListMap(LineIterator lineIterator, boolean hasHeader)
			throws IOException {
		Map<String, List<String>> ret = new LinkedHashMap<>();
        try (lineIterator) {
            MutableObject<String> firstLineObj = new MutableObject<>();
            while (lineIterator.hasNext()) {
                String next = lineIterator.next();
                if (next.charAt(0) == '#') {
                    continue;
                }
                firstLineObj.setValue(next);
                break;
            }

            String firstLine = firstLineObj.getValue();
            String[] headers;
            List<List<String>> listOfList = Lists.newArrayList();
            if (hasHeader) {
                headers = EGPSStringUtil.split(firstLine, '\t');
                for (int i = 0; i < headers.length; i++) {
                    listOfList.add(Lists.newLinkedList());
                }
            } else {
                String[] firstLineContents = EGPSStringUtil.split(firstLine, '\t');

                headers = new String[firstLineContents.length];
                for (int i = 0; i < firstLineContents.length; i++) {
                    headers[i] = "V".concat(String.valueOf(i + 1));
                }
                for (int i = 0; i < headers.length; i++) {
                    LinkedList<String> newLinkedList = Lists.newLinkedList();
                    newLinkedList.add(firstLineContents[i]);
                    listOfList.add(newLinkedList);
                }
            }

            while (lineIterator.hasNext()) {
                String next = lineIterator.next();
                if (next.charAt(0) == '#') {
                    continue;
                }
                String[] line = EGPSStringUtil.split(next, '\t');
                int size = line.length;
                for (int i = 0; i < size; i++) {
                    listOfList.get(i).add(line[i]);
                }
            }

            Iterator<List<String>> iterator2 = listOfList.iterator();
            for (String string : headers) {
                ret.put(string, iterator2.next());
            }

        }

		return ret;
	}


	/**
	 * 读取一个tsv类型的文件，然后将其中两列 key和value，提取出来专门作为一个hash。 一个具体的例子：
	 * 
	 * <pre>
	 * Name	col1	col2	col3
	 * Str1	1.0	3.9	4.9
	 * ...
	 * </pre>
	 * 
	 * 得到一个 col2到col3的哈希就是： key="col2" value = "col3"
	 * 
	 * @param key: the key column name
	 * @param value: the value column name
	 * @param filePath: the file path
	 * @return the hash map
     */
	public static Map<String, String> organizeMap(String key, String value, String filePath) throws IOException {

		Map<String, String> ret = new HashMap<>();

        try (LineIterator lineIterator = FileUtils.lineIterator(new File(filePath), "UTF-8")) {
            String[] headers = EGPSStringUtil.split(lineIterator.next(), '\t');

            int keyIndex = -1;
            int valueIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                String string = headers[i];
                if (Objects.equals(key, string)) {
                    keyIndex = i;
                }
                if (Objects.equals(value, string)) {
                    valueIndex = i;
                }
            }

            if (keyIndex < 0 || valueIndex < 0) {
                throw new IllegalArgumentException("Your input key or value not found in the header line.");
            }

            while (lineIterator.hasNext()) {
                String next = lineIterator.next();
                String[] line = EGPSStringUtil.split(next, '\t');
                String asKey = line[keyIndex];
                String asValue = line[valueIndex];
                ret.put(asKey, asValue);
            }


        }


		return ret;
	}
	/**
	 * 读取一个tsv类型的文件，然后将其中两列 key和value，提取出来专门作为一个hash。 一个具体的例子：
	 * 
	 * <pre>
	 * Name	col1	col2	col3
	 * Str1	1.0	3.9	4.9
	 * ...
	 * </pre>
	 * 
	 * 得到一个 col2到col3的哈希就是： key="col2" value = "col3"
	 * 
	 * @param key: the key column
	 * @param values: one or multiple values, multiple values will return a list of Strings
	 * @param filePath: the input file path
	 * @return the value is immutable list
     */
	public static Map<String, ImmutableList<String>> organizeMap(String key, List<String> values, String filePath)
			throws IOException {
		
		Map<String, ImmutableList<String>> ret = new HashMap<>();

        try (LineIterator LineIterator = FileUtils.lineIterator(new File(filePath), "UTF-8")) {
            String[] headers = EGPSStringUtil.split(LineIterator.next(), '\t');

            int keyIndex = -1;
            List<Integer> valueIndexes = Lists.newArrayList();

            for (int i = 0; i < headers.length; i++) {
                String string = headers[i];
                if (Objects.equals(key, string)) {
                    keyIndex = i;
                }
                if (values.contains(string)) {
                    valueIndexes.add(i);
                }
            }

            if (valueIndexes.isEmpty()) {
                throw new IllegalArgumentException("Your input key and value not found in the header line.");
            }

            while (LineIterator.hasNext()) {
                String next = LineIterator.next();
                String[] line = EGPSStringUtil.split(next, '\t');
                String asKey = line[keyIndex];


                List<String> temp = new ArrayList<>();
                for (Integer index : valueIndexes) {
                    temp.add(line[index]);
                }

                ImmutableList<String> immutableList = ImmutableList.copyOf(temp);
                ret.put(asKey, immutableList);
            }


        }
		
		
		return ret;
	}


}
