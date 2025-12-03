package utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comprehensive general-purpose utility class providing miscellaneous helper methods.
 * 
 * <p>
 * This final utility class contains a diverse collection of static helper methods for:
 * <ul>
 *   <li>Color calculations and interpolation</li>
 *   <li>String manipulation and formatting</li>
 *   <li>Number formatting (decimal formats with various precisions)</li>
 *   <li>Collection to file/writer conversion</li>
 *   <li>System property access</li>
 *   <li>URL constants for biological databases (NCBI, UniProt, PDB)</li>
 *   <li>Date formatting and parsing</li>
 * </ul>
 * </p>
 * 
 * <h2>System Constants:</h2>
 * <ul>
 *   <li>{@link #OS_NAME}, {@link #OS_VERSION}, {@link #OS_ARCH} - Operating system info</li>
 *   <li>{@link #JAVA_VERSION}, {@link #JAVA_VENDOR} - Java runtime info</li>
 *   <li>{@link #FILE_SEPARATOR}, {@link #LINE_SEPARATOR} - Platform-specific separators</li>
 *   <li>{@link #ZERO_DIFF} - Floating-point comparison tolerance (1.0E-9)</li>
 * </ul>
 * 
 * <h2>Number Formatters:</h2>
 * <ul>
 *   <li>{@link #FORMATTER_9} - Up to 9 decimal places</li>
 *   <li>{@link #FORMATTER_6} - Up to 6 decimal places</li>
 *   <li>{@link #FORMATTER_06} - Exactly 6 decimal places (with leading zero)</li>
 *   <li>{@link #FORMATTER_3} - Up to 3 decimal places</li>
 * </ul>
 * 
 * <h2>Biological Database URLs:</h2>
 * <ul>
 *   <li>{@link #NCBI_PROTEIN} - NCBI Protein database</li>
 *   <li>{@link #NCBI_NUCCORE} - NCBI Nucleotide database</li>
 *   <li>{@link #NCBI_GI} - NCBI GI lookup</li>
 *   <li>{@link #UNIPROT_KB} - UniProt Knowledgebase</li>
 *   <li>{@link #PDB} - Protein Data Bank</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * <pre>
 * // Color interpolation
 * Color color = EGPSGeneralUtil.calcColor(0.5, 0.0, 1.0, Color.BLUE, Color.RED);
 * 
 * // Format number
 * String formatted = EGPSGeneralUtil.FORMATTER_6.format(3.14159265);
 * 
 * // Write collection to file
 * EGPSGeneralUtil.collection2file(file, dataList, "\n");
 * 
 * // Check floating point equality
 * boolean equal = Math.abs(a - b) < EGPSGeneralUtil.ZERO_DIFF;
 * </pre>
 * 
 * <h2>Design Note:</h2>
 * <p>
 * This class is declared final with a private constructor to prevent instantiation.
 * All methods are static utility methods.
 * </p>
 * 
 * @author eGPS Development Team
 * @version 1.0
 * @since 1.0
 * @see DecimalFormat
 * @see NumberFormat
 */
public final class EGPSGeneralUtil {
	public final static String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final NumberFormat FORMATTER_06;
	public static final NumberFormat FORMATTER_3;
	public static final NumberFormat FORMATTER_6;
	public static final NumberFormat FORMATTER_9;
	public final static String JAVA_VENDOR = System.getProperty("java.vendor");
	public final static String JAVA_VERSION = System.getProperty("java.version");
	public final static String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String NCBI_GI = "http://www.ncbi.nlm.nih.gov/protein/gi:";
	public static final String NCBI_NUCCORE = "http://www.ncbi.nlm.nih.gov/nuccore/";
	public static final String NCBI_PROTEIN = "http://www.ncbi.nlm.nih.gov/protein/";
	public static final BigDecimal NULL_BD = new BigDecimal(0);
	public final static String OS_ARCH = System.getProperty("os.arch");
	public final static String OS_NAME = System.getProperty("os.name");
	public final static String OS_VERSION = System.getProperty("os.version");
	public static final String PDB = "http://www.pdb.org/pdb/explore/explore.do?pdbId=";
	public final static String UNIPROT_KB = "http://www.uniprot.org/uniprot/";

	/**
	 * 计算机中的数值不可能完全相同，这是两个浮点数不同的最低容忍度！
	 */
	public final static double ZERO_DIFF = 1.0E-9;

	public enum MolecularUnitType {
		RNA, DNA, AA;
	}

	static {
		final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		// dfs.setGroupingSeparator( ( char ) 0 );
		FORMATTER_9 = new DecimalFormat("#.#########", dfs);
		FORMATTER_6 = new DecimalFormat("#.######", dfs);
		FORMATTER_06 = new DecimalFormat("0.######", dfs);
		FORMATTER_3 = new DecimalFormat("#.###", dfs);
	}

	private EGPSGeneralUtil() {
	}

	/**
	 * This calculates a color. If value is equal to min the returned color is
	 * minColor, if value is equal to max the returned color is maxColor, otherwise
	 * a color 'proportional' to value is returned.
	 * 
	 *
	 * @param value    the value
	 * @param min      the smallest value
	 * @param max      the largest value
	 * @param minColor the color for min
	 * @param maxColor the color for max
	 * @return a Color
	 */
	final public static Color calcColor(double value, final double min, final double max, final Color minColor,
			final Color maxColor) {
		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}
		final double x = calculateColorFactor(value, max, min);
		final int red = calculateColorComponent(minColor.getRed(), maxColor.getRed(), x);
		final int green = calculateColorComponent(minColor.getGreen(), maxColor.getGreen(), x);
		final int blue = calculateColorComponent(minColor.getBlue(), maxColor.getBlue(), x);
		return new Color(red, green, blue);
	}

	/**
	 * This calculates a color. If value is equal to min the returned color is
	 * minColor, if value is equal to max the returned color is maxColor, if value
	 * is equal to mean the returned color is meanColor, otherwise a color
	 * 'proportional' to value is returned -- either between min-mean or mean-max
	 *
	 * @param value     the value
	 * @param min       the smallest value
	 * @param max       the largest value
	 * @param mean      the mean/median value
	 * @param minColor  the color for min
	 * @param maxColor  the color for max
	 * @param meanColor the color for mean
	 * @return a Color
	 */
	final public static Color calcColor(double value, final double min, final double max, final double mean,
			final Color minColor, final Color maxColor, final Color meanColor) {
		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}
		if (value < mean) {
			final double x = EGPSGeneralUtil.calculateColorFactor(value, mean, min);
			final int red = EGPSGeneralUtil.calculateColorComponent(minColor.getRed(), meanColor.getRed(), x);
			final int green = EGPSGeneralUtil.calculateColorComponent(minColor.getGreen(), meanColor.getGreen(), x);
			final int blue = EGPSGeneralUtil.calculateColorComponent(minColor.getBlue(), meanColor.getBlue(), x);
			return new Color(red, green, blue);
		} else if (value > mean) {
			final double x = EGPSGeneralUtil.calculateColorFactor(value, max, mean);
			final int red = EGPSGeneralUtil.calculateColorComponent(meanColor.getRed(), maxColor.getRed(), x);
			final int green = EGPSGeneralUtil.calculateColorComponent(meanColor.getGreen(), maxColor.getGreen(), x);
			final int blue = EGPSGeneralUtil.calculateColorComponent(meanColor.getBlue(), maxColor.getBlue(), x);
			return new Color(red, green, blue);
		} else {
			return meanColor;
		}
	}

	/**
	 * Helper method for calcColor methods.
	 *
	 * @param smallercolor_component_x color component the smaller color
	 * @param largercolor_component_x  color component the larger color
	 * @param x                        factor
	 * @return an int representing a color component
	 */
	final private static int calculateColorComponent(final double smallercolor_component_x,
			final double largercolor_component_x, final double x) {
		return (int) (smallercolor_component_x + ((x * (largercolor_component_x - smallercolor_component_x)) / 255.0));
	}

	/**
	 * Helper method for calcColor methods.
	 *
	 *
	 * @param value   the value
	 * @param larger  the largest value
	 * @param smaller the smallest value
	 * @return a normalized value between larger and smaller
	 */
	final private static double calculateColorFactor(final double value, final double larger, final double smaller) {
		return (255.0 * (value - smaller)) / (larger - smaller);
	}

	/**
	 * Example: Give a string "a b", return "ab". I.e. : remove all white spaces.
	 */
	final public static String collapseWhiteSpace(final String s) {
		return s.replaceAll("[\\s]+", " ");
	}

	/**
	 * Java 集合框架，快速写入一个文件！
	 */
	final public static void collection2file(final File file, final Collection<?> data, final String separator)
			throws IOException {
		final Writer writer = new BufferedWriter(new FileWriter(file));
		collection2writer(writer, data, separator);
		writer.close();
	}

	/**
	 * 每个元素都有序地写出！
	 * 
	 * @param writer
	 * @param data
	 * @param separator : Note: this is a line separator!
	 * @throws IOException
	 */
	final public static void collection2writer(final Writer writer, final Collection<?> data, final String separator)
			throws IOException {
		boolean first = true;
		for (final Object object : data) {
			if (!first) {
				writer.write(separator);
			} else {
				first = false;
			}
			writer.write(object.toString());
		}
	}

	/**
	 * change color representation!
	 */
	public static String colorToHex(final Color color) {
		final String rgb = Integer.toHexString(color.getRGB());
		return rgb.substring(2, rgb.length());
	}

	synchronized public static void copyFile(final File in, final File out) throws IOException {
		final FileInputStream in_s = new FileInputStream(in);
		final FileOutputStream out_s = new FileOutputStream(out);
		try {
			final byte[] buf = new byte[1024];
			int i = 0;
			while ((i = in_s.read(buf)) != -1) {
				out_s.write(buf, 0, i);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (in_s != null) {
				in_s.close();
			}
			if (out_s != null) {
				out_s.close();
			}
		}
	}

	final public static int countChars(final String str, final char c) {
		int count = 0;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == c) {
				++count;
			}
		}
		return count;
	}

	final public static BufferedWriter createBufferedWriter(final File file) throws IOException {
		if (file.exists()) {
			throw new IOException("[" + file + "] already exists");
		}
		return new BufferedWriter(new FileWriter(file));
	}

	final public static BufferedWriter createBufferedWriter(final String name) throws IOException {
		return new BufferedWriter(new FileWriter(createFileForWriting(name)));
	}

	final public static File createFileForWriting(final String name) throws IOException {
		final File file = new File(name);
		if (file.exists()) {
			throw new IOException("[" + name + "] already exists");
		}
		return file;
	}

	public static String[][] file22dArray(final File file) throws IOException {
		final List<String> list = new ArrayList<String>();
		final BufferedReader in = new BufferedReader(new FileReader(file));
		String str;
		while ((str = in.readLine()) != null) {
			str = str.trim();
			if ((str.length() > 0) && !str.startsWith("#")) {
				list.add(str);
			}
		}
		in.close();
		final String[][] ary = new String[list.size()][2];
		final Pattern pa = Pattern.compile("(\\S+)\\s+(\\S+)");
		int i = 0;
		for (final String s : list) {
			final Matcher m = pa.matcher(s);
			if (m.matches()) {
				ary[i][0] = m.group(1);
				ary[i][1] = m.group(2);
				++i;
			} else {
				throw new IOException("unexpcted format: " + s);
			}
		}
		return ary;
	}

	final public static String getCurrentDateTime() {
		final DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return format.format(new Date());
	}

	final public static String getFileSeparator() {
		return EGPSGeneralUtil.FILE_SEPARATOR;
	}

	final public static String getFirstLine(final Object source) throws FileNotFoundException, IOException {
		BufferedReader reader = null;
		if (source instanceof File) {
			final File f = (File) source;
			if (!f.exists()) {
				throw new IOException("[" + f.getAbsolutePath() + "] does not exist");
			} else if (!f.isFile()) {
				throw new IOException("[" + f.getAbsolutePath() + "] is not a file");
			} else if (!f.canRead()) {
				throw new IOException("[" + f.getAbsolutePath() + "] is not a readable");
			}
			reader = new BufferedReader(new FileReader(f));
		} else if (source instanceof InputStream) {
			reader = new BufferedReader(new InputStreamReader((InputStream) source));
		} else if (source instanceof String) {
			reader = new BufferedReader(new StringReader((String) source));
		} else if (source instanceof StringBuffer) {
			reader = new BufferedReader(new StringReader(source.toString()));
		} else if (source instanceof URL) {
			final URLConnection url_connection = ((URL) source).openConnection();
			url_connection.setDefaultUseCaches(false);
			reader = new BufferedReader(new InputStreamReader(url_connection.getInputStream()));
		} else {
			throw new IllegalArgumentException("dont know how to read [" + source.getClass() + "]");
		}
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (!EGPSGeneralUtil.isEmpty(line)) {
				if (reader != null) {
					reader.close();
				}
				return line;
			}
		}
		if (reader != null) {
			reader.close();
		}
		return line;
	}

	final public static String getLineSeparator() {
		return LINE_SEPARATOR;
	}

	final public static MolecularUnitType guessMolecularSequenceType(final String mol_seq) {
		if (mol_seq.contains("L") || mol_seq.contains("I") || mol_seq.contains("E") || mol_seq.contains("H")
				|| mol_seq.contains("D") || mol_seq.contains("Q")) {
			return MolecularUnitType.AA;
		} else if (mol_seq.contains("U")) {
			return MolecularUnitType.RNA;
		} else {
			return MolecularUnitType.DNA;
		}
	}

	final public static boolean isEmpty(final List<?> l) {
		if ((l == null) || l.isEmpty()) {
			return true;
		}
		for (final Object o : l) {
			if (o != null) {
				return false;
			}
		}
		return true;
	}

	final public static boolean isEmpty(final Set<?> s) {
		if ((s == null) || s.isEmpty()) {
			return true;
		}
		for (final Object o : s) {
			if (o != null) {
				return false;
			}
		}
		return true;
	}

	final public static boolean isEmpty(final String s) {
		return ((s == null) || (s.length() < 1));
	}

	final public static boolean isNumberEqual(final double a, final double b) {
		return ((Math.abs(a - b)) < ZERO_DIFF);
	}

	final public static boolean isIntegerEven(final int n) {
		return (n % 2) == 0;
	}

	/**
	 * This determines whether String[] a and String[] b have at least one String in
	 * common (intersect). Returns false if at least one String[] is null or empty.
	 *
	 * @param a a String[] b a String[]
	 * @return true if both a and b or not empty or null and contain at least one
	 *         element in common false otherwise
	 */
	final public static boolean isIntersecting(final String[] a, final String[] b) {
		if ((a == null) || (b == null)) {
			return false;
		}
		if ((a.length < 1) || (b.length < 1)) {
			return false;
		}
		for (final String ai : a) {
			for (final String element : b) {
				if ((ai != null) && (element != null) && ai.equals(element)) {
					return true;
				}
			}
		}
		return false;
	}

	final public static double isLargerOrEqualToZero(final double d) {
		if (d > 0.0) {
			return d;
		} else {
			return 0.0;
		}
	}

	final public static boolean isNull(final BigDecimal s) {
		return ((s == null) || (s.compareTo(NULL_BD) == 0));
	}

	/**
	 * 
	 * 检测： 1. 文件是否存在？ 2. 是文件夹吗？ 3. 文件可读吗？ 4. 文件的内容是否为0？ 显然文件没有内容是不值得去读的！
	 */
	final public static String isReadableFile(final File f) {
		if (!f.exists()) {
			return "file [" + f + "] does not exist";
		}
		if (f.isDirectory()) {
			return "[" + f + "] is a directory";
		}
		if (!f.isFile()) {
			return "[" + f + "] is not a file";
		}
		if (!f.canRead()) {
			return "file [" + f + "] is not readable";
		}
		if (f.length() < 1) {
			return "file [" + f + "] is empty";
		}
		return "";
	}

	final public static String isReadableFile(final String s) {
		return isReadableFile(new File(s));
	}

	public final static boolean isWindows() {
		return OS_NAME.toLowerCase().indexOf("win") > -1;
	}

	public final static boolean isMacOS() {
		return OS_NAME.toLowerCase().startsWith("mac");
	}

	final public static String isWritableFile(final File f) {
		if (f.isDirectory()) {
			return "[" + f + "] is a directory";
		}
		if (f.exists()) {
			return "[" + f + "] already exists";
		}
		return "";
	}

	/**
	 * Helper for method "stringToColor".
	 * <p>
	 * (Last modified: 12/20/03)
	 */
	final public static int limitRangeForColor(int i) {
		if (i > 255) {
			i = 255;
		} else if (i < 0) {
			i = 0;
		}
		return i;
	}

	final public static void map2file(final File file, final Map<?, ?> data, final String entry_separator,
			final String data_separator) throws IOException {
		final Writer writer = new BufferedWriter(new FileWriter(file));
		map2writer(writer, data, entry_separator, data_separator);
		writer.close();
	}

	final public static void map2writer(final Writer writer, final Map<?, ?> data, final String entry_separator,
			final String data_separator) throws IOException {
		boolean first = true;
		for (final Entry<?, ?> entry : data.entrySet()) {
			if (!first) {
				writer.write(data_separator);
			} else {
				first = false;
			}
			writer.write(entry.getKey().toString());
			writer.write(entry_separator);
			writer.write(entry.getValue().toString());
		}
	}

	final public static StringBuffer mapToStringBuffer(final Map<Object, Object> map,
			final String key_value_separator) {
		final StringBuffer sb = new StringBuffer();
		for (final Object key : map.keySet()) {
			sb.append(key.toString());
			sb.append(key_value_separator);
			sb.append(map.get(key).toString());
			sb.append(EGPSGeneralUtil.getLineSeparator());
		}
		return sb;
	}

	/**
	 * Padding strings!
	 */
	final public static String normalizeString(final String s, final int length, final boolean left_pad,
			final char pad_char) {
		if (s.length() > length) {
			return s.substring(0, length);
		} else {
			final StringBuffer pad = new StringBuffer(length - s.length());
			for (int i = 0; i < (length - s.length()); ++i) {
				pad.append(pad_char);
			}
			if (left_pad) {
				return pad + s;
			} else {
				return s + pad;
			}
		}
	}

	final public static StringBuffer pad(final double number, final int size, final char pad, final boolean left_pad) {
		return pad(new StringBuffer(number + ""), size, pad, left_pad);
	}

	final public static StringBuffer pad(final String string, final int size, final char pad, final boolean left_pad) {
		return pad(new StringBuffer(string), size, pad, left_pad);
	}

	final public static StringBuffer pad(final StringBuffer string, final int size, final char pad,
			final boolean left_pad) {
		final StringBuffer padding = new StringBuffer();
		final int s = size - string.length();
		if (s < 1) {
			return new StringBuffer(string.substring(0, size));
		}
		for (int i = 0; i < s; ++i) {
			padding.append(pad);
		}
		if (left_pad) {
			return padding.append(string);
		} else {
			return string.append(padding);
		}
	}

	final public static double parseDouble(final String str) throws ParseException {
		if (EGPSGeneralUtil.isEmpty(str)) {
			return 0.0;
		}
		return Double.parseDouble(str);
	}

	final public static int parseInt(final String str) throws ParseException {
		if (isEmpty(str)) {
			return 0;
		}
		return Integer.parseInt(str);
	}

	public static List<String> readUrl(final String url_str) throws IOException {
		final URL url = new URL(url_str);
		final URLConnection urlc = url.openConnection();
		// urlc.setRequestProperty( "User-Agent", "" );
		final BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
		String line;
		final List<String> result = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
			result.add(line);
		}
		in.close();
		return result;
	}

	final public static String removeSuffix(final String file_name) {
		final int i = file_name.lastIndexOf('.');
		if (i > 1) {
			return file_name.substring(0, i);
		}
		return file_name;
	}

	public final static String wordWrap(final String str, final int width) {
		final StringBuilder sb = new StringBuilder(str);
		int start = 0;
		int ls = -1;
		int i = 0;
		while (i < sb.length()) {
			if (sb.charAt(i) == ' ') {
				ls = i;
			}
			if (sb.charAt(i) == '\n') {
				ls = -1;
				start = i + 1;
			}
			if (i > ((start + width) - 1)) {
				if (ls != -1) {
					sb.setCharAt(ls, '\n');
					start = ls + 1;
					ls = -1;
				} else {
					sb.insert(i, '\n');
					start = i + 1;
				}
			}
			i++;
		}
		return sb.toString();
	}

}
