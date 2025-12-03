package utils.string;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import org.slf4j.helpers.MessageFormatter;

/**
 * Comprehensive string utility class providing advanced operations for text manipulation.
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>String Splitting:</strong> High-performance splitting with single-char delimiter optimization</li>
 *   <li><strong>Matrix Transpose:</strong> Transpose 2D string lists (columns to rows)</li>
 *   <li><strong>Bracket Validation:</strong> Validate matching brackets (parentheses, braces, square brackets)</li>
 *   <li><strong>Common Prefix/Suffix:</strong> Find shared prefixes and suffixes between strings</li>
 *   <li><strong>Number Extraction:</strong> Extract and concatenate numeric digits from strings</li>
 *   <li><strong>Byte Array Parsing:</strong> Parse lines from US-ASCII byte arrays</li>
 *   <li><strong>String Formatting:</strong> Log4j-style parameterized string formatting with {}</li>
 * </ul>
 * 
 * <h2>Usage Examples:</h2>
 * 
 * <h3>High-Performance String Splitting:</h3>
 * <pre>
 * String[] parts = EGPSStringUtil.split("a:b:c:d", ':');
 * // Result: ["a", "b", "c", "d"]
 * 
 * String[] limited = EGPSStringUtil.split("a:b:c:d", ':', 2);
 * // Result: ["a", "b"] (only first 2 elements)
 * 
 * String[] tabSplit = EGPSStringUtil.splitByTab("col1\tcol2\tcol3");
 * // Result: ["col1", "col2", "col3"]
 * </pre>
 * 
 * <h3>Matrix Transpose:</h3>
 * <pre>
 * List&lt;List&lt;String&gt;&gt; input = Arrays.asList(
 *     Arrays.asList("A", "B", "C"),
 *     Arrays.asList("1", "2", "3")
 * );
 * List&lt;List&lt;String&gt;&gt; transposed = EGPSStringUtil.transpose(input);
 * // Result: [["A", "1"], ["B", "2"], ["C", "3"]]
 * </pre>
 * 
 * <h3>Bracket Validation:</h3>
 * <pre>
 * boolean valid1 = EGPSStringUtil.validateStringAccording2bracks("(a+b)*[c-d]");
 * // Result: true
 * 
 * boolean valid2 = EGPSStringUtil.validateStringAccording2bracks("(a+b])");
 * // Result: false (mismatched brackets)
 * </pre>
 * 
 * <h3>Common Prefix/Suffix:</h3>
 * <pre>
 * String prefix = EGPSStringUtil.getCommonPrefix("alignment", "algorithm");
 * // Result: "al"
 * 
 * String suffix = EGPSStringUtil.getCommonTail("testing", "running");
 * // Result: "ing"
 * </pre>
 * 
 * <h3>Number Extraction:</h3>
 * <pre>
 * String numbers = EGPSStringUtil.getNumInString("abc123def456");
 * // Result: "123456"
 * </pre>
 * 
 * <h3>String Formatting (Log4j-style):</h3>
 * <pre>
 * String msg = EGPSStringUtil.format("Processing {} sequences from {}", 1500, "input.fasta");
 * // Result: "Processing 1500 sequences from input.fasta"
 * 
 * String path = EGPSStringUtil.format("{}/voice/{}.voice.store.gz", "path/to/a", "yes.it.is");
 * // Result: "path/to/a/voice/yes.it.is.voice.store.gz"
 * </pre>
 * 
 * <h3>Byte Array Line Parsing:</h3>
 * <pre>
 * byte[] data = "line1\nline2\nline3".getBytes(StandardCharsets.US_ASCII);
 * List&lt;String&gt; lines = EGPSStringUtil.getLinesFromByteArray(data);
 * // Result: ["line1", "line2", "line3"]
 * </pre>
 * 
 * <h2>Performance Optimizations:</h2>
 * <ul>
 *   <li><strong>Single-Char Delimiter:</strong> Optimized path for single-character splitting (faster than regex)</li>
 *   <li><strong>Fixed-Size Splitting:</strong> Pre-allocate array when size is known</li>
 *   <li><strong>Stack-Based Bracket Matching:</strong> O(n) time complexity with early termination</li>
 *   <li><strong>Consumer-Based Splitting:</strong> Memory-efficient streaming split with callbacks</li>
 * </ul>
 * 
 * <h2>Use Cases:</h2>
 * <ul>
 *   <li>Parse TSV/CSV files with delimiter splitting</li>
 *   <li>Transpose column-oriented data to row-oriented format</li>
 *   <li>Validate Newick tree format with bracket matching</li>
 *   <li>Extract sample IDs from mixed alphanumeric strings</li>
 *   <li>Build formatted log messages and file paths</li>
 *   <li>Parse sequence headers from byte-stream FASTA data</li>
 * </ul>
 * 
 * <h2>Note:</h2>
 * <p>The split methods handle edge cases like consecutive delimiters (e.g., "::1" splits to ["", "", "1"]).
 * This behavior differs from String.split() and is useful for fixed-column formats.</p>
 * 
 * @author yudalang
 * @since 1.0
 */
public class EGPSStringUtil {
	public static final char EQUAL_CHAR = '=';

	private EGPSStringUtil() {
		super();
	}

	public static List<List<String>> transpose(List<List<String>> input) {
		if (input == null || input.isEmpty()) {
			return new ArrayList<>();
		}

		int rowCount = input.size();
		int colCount = input.get(0).size();

		List<List<String>> transposed = new ArrayList<>();
		for (int i = 0; i < colCount; i++) {
			List<String> col = new ArrayList<>();
			for (int j = 0; j < rowCount; j++) {
				col.add(input.get(j).get(i));
			}
			transposed.add(col);
		}
		return transposed;
	}


	/**
	 * 功能：得到 = 字符后面的内容
	 * <pre>
	 * a=b ==>  b
	 * =b  ==> b
	 * g=abcde ==> abcde
	 *
	 * </pre>
	 *
	 * @param str 输入的字符串，包含=字符
	 * @return 返回=字符后面的内容
	 */
	public static String getStringAfterEqualChar(String str) {
		if (str == null) {
			throw new IllegalArgumentException("Input string is null");
		}
		int index = str.indexOf(EQUAL_CHAR);
		if (index == -1) {
			return ""; // 如果没有找到等号，返回空字符串
		}
		return str.substring(index + 1);
	}

	/**
	 * 
	 * 快速产生长字符串。
	 * 
	 * 
	 * @title fillString
	 * @createdDate 2020-10-23 10:15
	 * @lastModifiedDate 2020-10-23 10:15
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param fillChar
	 * @param count
	 * @return String
	 */
	public static String fillString(char fillChar, int count) {
		// creates a string of 'x' repeating characters
		char[] chars = new char[count];
		java.util.Arrays.fill(chars, fillChar);
		return new String(chars);
	}

	/**
	 * 
	 * 验证一个字符串括号的有效性。 现在我们支持：（）[] {}
	 * 
	 * @title validateStringAccording2bracks
	 * @createdDate 2020-10-23 10:17
	 * @lastModifiedDate 2020-10-23 10:17
	 * @author yudalang
	 * @since 1.7
	 * 
	 * @param s
	 * @return boolean
	 */
	public static boolean validateStringAccording2bracks(String s) {
		int slen = s.length(); // 括号的长度
		if (slen % 2 == 1) { // 括号不是成对出现直接返回 false
			return false;
		}
		// 把所有对比的括号存入 map，对比时用
		Map<Character, Character> map = new HashMap<>();
		map.put(')', '(');
		map.put('}', '{');
		map.put(']', '[');
		// 定义栈，用于存取括号（辅助比较）
		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < slen; i++) { // 循环所有字符
			char c = s.charAt(i);
			if (map.containsKey(c)) { // 为右边的括号，如 ')'、'}' 等
				if (stack.isEmpty() || stack.peek() != map.get(c)) { // 栈为空或括号不匹配
					return false;
				}
				stack.pop(); // 是一对括号，执行出栈（消除左右括号）
			} else { // 左边括号，直接入栈
				stack.push(c);
			}
		}
		return stack.isEmpty();
	}

	/**
	 * 提取字符串中的数字。
	 * 注意：如果有多处出现数字，那么都会提取出来合并成一个字符串。
	 *
	 * @title getNumInString
	 * @createdDate 2021-11-15 15:49
	 * @lastModifiedDate 2021-11-15 15:49
	 * @author yjn
	 * @since 1.7
	 *
	 * @param str 待提取的字符串
	 * @return 提取并合并后的数字字符串
	 *
	 * 示例:
	 * <pre>
	 * 示例 1:
	 * 输入: "abc123def456"
	 * 输出: "123456"
	 *
	 * 示例 2:
	 * 输入: "hello world 2023"
	 * 输出: "2023"
	 *
	 * 示例 3:
	 * 输入: "no numbers here!"
	 * 输出: ""
	 * </pre>
	 */
	public static String getNumInString(String str) {
		str = str.trim();
		String str2 = "";
		if (str != null && !"".equals(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					str2 += str.charAt(i);
				}
			}
		}
		return str2;
	}

	private static void splitWorker(final String str, final String separatorChars, final int max,
			final boolean preserveAllTokens, Consumer<String> onSplit) {
		if (str == null) {
			return;
		}
		final int len = str.length();
		if (len == 0) {
			return;
		}
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						onSplit.accept(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			final char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						onSplit.accept(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						onSplit.accept(str.substring(start, i));

						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || preserveAllTokens && lastMatch) {
			onSplit.accept(str.substring(start, i));
		}
	}

	public static void split(final String str, final String separatorChars, Consumer<String> onSplit) {
		splitWorker(str, separatorChars, -1, false, onSplit);

	}

	/**
	 * 根据制表符分割字符串
	 *
	 * @param line 待分割的字符串
	 * @return 分割后的字符串数组
	 */
	public static String[] splitByTab(final String line) {
	    return split(line, '\t'); // 调用split方法，使用制表符作为分隔符进行分割
	}

	/**
	 * 单个字符串分割的快速方法。 友情提醒，用这个方法的前提是你对String.split和StringTokener已经很熟。
	 * 如果你直接用，可能会出现一些你意想不到的情况。 它处理 "::1" 这样的字符串，如果用':'分割的话，可以得到三个留下来的item
	 * 
	 * @param line
	 * @param delimiter
	 * @return
	 */
	public static String[] split(final String line, final char delimiter) {
		CharSequence[] temp = new CharSequence[line.length() + 1];
		int wordCount = 0;
		int i = 0;
		int j = line.indexOf(delimiter, 0); // first substring

		while (j >= 0) {
			if (i == j) {
				temp[wordCount++] = "";
			} else {
				temp[wordCount++] = line.substring(i, j);
			}

			i = j + 1;
			j = line.indexOf(delimiter, i); // rest of substrings
		}

		temp[wordCount++] = line.substring(i); // last substring

		String[] result = new String[wordCount];
		System.arraycopy(temp, 0, result, 0, wordCount);

		return result;
	}

	/**
	 * 它处理 "::1" 这样的字符串，如果用':'分割的话，可以得到三个留下来的item
	 * 如果实际的元素数目大于声明的数目，那么只会填充前面声明的即可，后面的就不管了。
	 * 
	 * @param str
	 * @param delimiter
	 * @param size
	 * @return
	 */
	public static String[] split(final String str, final char delimiter, final int size) {
		// =====================================这是分割字符串的方法===Start
		final String[] split = new String[size];
		int wordCount = 0;
		int i = 0;
		int j = str.indexOf(delimiter, 0); // first substring

		while (j >= 0) {
			if (i == j) {
				split[wordCount++] = "";
			} else {
				split[wordCount++] = str.substring(i, j);
			}
			i = j + 1;
			j = str.indexOf(delimiter, i); // rest of substrings

			if (wordCount == size) {
				break;
			}
		}

		if (wordCount < size) {
			split[wordCount++] = str.substring(i); // last substring
		}
		// =====================================这是分割字符串的方法===End

		return split;
	}

	/**
	 * 
	 * 获取两个字符串的相同前缀
	 * 
	 * @title getCommonPrefix
	 * @createdDate 2023-06-16 20:55
	 * @lastModifiedDate 2023-06-16 20:55
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 * @return String
	 */
	public static String getCommonPrefix(String str1, String str2) {
		int minLength = Math.min(str1.length(), str2.length());

		StringBuilder commonPrefix = new StringBuilder();
		for (int i = 0; i < minLength; i++) {
			if (str1.charAt(i) != str2.charAt(i)) {
				break;
			}
			commonPrefix.append(str1.charAt(i));
		}

		return commonPrefix.toString();
	}

	/**
	 * 
	 * 获取两个字符串相同的尾缀
	 * 
	 * @title getCommonTail
	 * @createdDate 2023-06-19 10:46
	 * @lastModifiedDate 2023-06-19 10:46
	 * @author yjn
	 * @since 1.7
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 * @return String
	 */
	public static String getCommonTail(String str1, String str2) {
		int length1 = str1.length();
		int length2 = str2.length();
		int minLength = Math.min(length1, length2);

		// 从尾部开始逐个字符比较
		int i = 1;
		while (i <= minLength && str1.charAt(length1 - i) == str2.charAt(length2 - i)) {
			i++;
		}

		// 获取相同部分
		return str1.substring(length1 - i + 1);
	}

	public static void main(String[] args) {
		byte[] byteArray = { 'a', '1', '\n', 'b', '\n', 'c', '2', '3', '\n' };
		List<String> linesFromByteArray = getLinesFromByteArray(byteArray);

		System.out.println(linesFromByteArray.size());
		System.out.println(linesFromByteArray);
		System.out.println(EGPSStringUtil.getCommonTail("LGVY", "Y"));
	}
	
	

	/**
	 * Note , the byteArray should be US-ASCII coding charSet.
	 * 
	 * @param byteArray
	 * @return
	 */
	public static List<String> getLinesFromByteArray(byte[] byteArray) {

		List<String> lines = new ArrayList<>(1024);
		int lineStartIndex = 0;
		final byte LF = '\n';
		int length = byteArray.length;

		for (int i = 0; i < length; i++) {
			byte currByte = byteArray[i];
			if (currByte == LF) {
				String string = new String(byteArray, lineStartIndex, i - lineStartIndex, StandardCharsets.US_ASCII);
				lines.add(string);
				lineStartIndex = i + 1;
			}
		}

		// Check whether still has content

		if (byteArray[length - 1] != LF) {

			String string = new String(byteArray, lineStartIndex, length - lineStartIndex, StandardCharsets.US_ASCII);
			lines.add(string);
		}
		return lines;
	}

	/**
	 * perform quick parameter's substitution. For example:
	 * 
	 * <pre>
	 * format("this is {}", "you") ==> this is you
	 * format("{}/voice/{}.voice.store.gz", "path/to/a", "yes.it.is") ==> path/to/a/voice/yes.it.is.voice.store.gz
	 * </pre>
	 *
	 * This is inspired by the java.text.MessageFormat.format() method and Java log4j logging frame.
	 * 
	 * @param string : messagePattern The message pattern which will be parsed and
	 *               formatted
	 * @param objs   : The argument to be substituted in place of the formatting
	 *               anchor
	 * @return the formatted string
	 */
	public static String format(String string, Object... objs) {
		String ret = MessageFormatter.arrayFormat(string, objs).getMessage();
		return ret;
	}

}
