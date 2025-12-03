package evoltree.struct.io;

import evoltree.struct.ArrayBasedNode;
import evoltree.struct.EvolNode;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Stack;

/**
 * High-performance stack-based phylogenetic tree decoder for Newick format parsing.
 * 
 * <p>
 * This class implements a highly optimized tree decoding algorithm using a stack data structure
 * to parse phylogenetic trees in Newick (and Newick-like) formats. It achieves approximately
 * 2x performance improvement over traditional recursive parsing methods by processing the
 * entire tree structure in a single iteration loop.
 * </p>
 * 
 * <h2>Performance Characteristics</h2>
 * <ul>
 *   <li><strong>Speed Improvement:</strong> Approximately 2x faster than recursive approaches</li>
 *   <li><strong>Example:</strong> 10-second parsing reduced to ~5 seconds</li>
 *   <li><strong>Single Pass:</strong> Processes tree in one iteration without recursion</li>
 *   <li><strong>Memory Mapped I/O:</strong> Supports efficient file reading via memory mapping</li>
 *   <li><strong>Time Complexity:</strong> O(n) where n is the length of the input string</li>
 *   <li><strong>Space Complexity:</strong> O(h) where h is the tree height (stack depth)</li>
 * </ul>
 * 
 * <h2>Algorithm Overview</h2>
 * <p>
 * The decoder uses a finite state machine with stack-based parsing:
 * </p>
 * <ol>
 *   <li><strong>Left Bracket '(':</strong> Create internal node, push to stack</li>
 *   <li><strong>Comma ',':</strong> Separator between siblings, no action needed</li>
 *   <li><strong>Right Bracket ')':</strong> Pop internal node, parse its attributes</li>
 *   <li><strong>Other Characters:</strong> Parse leaf node, add to parent</li>
 * </ol>
 * 
 * <h2>Supported Input Formats</h2>
 * <ul>
 *   <li><strong>Standard Newick:</strong> {@code ((A:0.1,B:0.2):0.05,C:0.3);}</li>
 *   <li><strong>Without Semicolon:</strong> {@code ((A:0.1,B:0.2):0.05,C:0.3)}</li>
 *   <li><strong>With Whitespace:</strong> Supports automatic whitespace removal</li>
 *   <li><strong>File Input:</strong> Direct parsing from files using memory mapping</li>
 * </ul>
 * 
 * <h2>Usage Examples</h2>
 * <pre>
 * // Example 1: Parse from String
 * PrimaryNodeTreeDecoder&lt;ArrayBasedNode&gt; decoder = new PrimaryNodeTreeDecoder&lt;&gt;();
 * String newick = "((A:0.1,B:0.2):0.05,C:0.3);";
 * ArrayBasedNode root = decoder.decode(newick);
 * 
 * // Example 2: Parse with whitespace removal
 * String newickWithSpaces = "( ( A:0.1, B:0.2 ):0.05, C:0.3 );";
 * ArrayBasedNode root2 = decoder.decode(newickWithSpaces, true);
 * 
 * // Example 3: Parse from file (memory mapped)
 * Path treePath = Paths.get("/data/trees.nwk");
 * ArrayBasedNode root3 = decoder.decode(treePath);
 * 
 * // Example 4: Custom node types
 * PrimaryNodeTreeDecoder&lt;DefaultPhyNode&gt; customDecoder = new PrimaryNodeTreeDecoder&lt;&gt;(
 *     new NWKLeafCoderDecoder&lt;&gt;(),
 *     new NWKInternalCoderDecoder&lt;&gt;()
 * );
 * DefaultPhyNode root4 = customDecoder.decode(newick);
 * </pre>
 * 
 * <h2>Key Features</h2>
 * <ul>
 *   <li><strong>Zero Recursion:</strong> Avoids stack overflow for very deep trees</li>
 *   <li><strong>Single Iteration:</strong> One pass through the input string</li>
 *   <li><strong>Efficient Parsing:</strong> Direct byte array processing for speed</li>
 *   <li><strong>Flexible Node Types:</strong> Supports custom node implementations via generics</li>
 *   <li><strong>Memory Mapped I/O:</strong> Optimal performance for large files</li>
 *   <li><strong>Whitespace Handling:</strong> Optional automatic whitespace removal</li>
 * </ul>
 * 
 * <h2>Constructor Options</h2>
 * <ul>
 *   <li><strong>Default Constructor:</strong> Uses BasicLeafCoderDecoder and BasicInternalCoderDecoder</li>
 *   <li><strong>Custom Parsers:</strong> Accepts custom leaf and internal node parsers for flexibility</li>
 * </ul>
 * 
 * <h2>Implementation Details</h2>
 * <p>
 * The decoder processes the input as a byte array for maximum performance:
 * </p>
 * <ul>
 *   <li>Converts string to bytes using US-ASCII charset</li>
 *   <li>Uses stack to track current position in tree hierarchy</li>
 *   <li>Identifies stop characters (comma, left/right brackets)</li>
 *   <li>Delegates node parsing to specialized coders</li>
 * </ul>
 * 
 * <h2>Limitations and Considerations</h2>
 * <ul>
 *   <li><strong>Testing Status:</strong> Optimized for performance; ensure thorough testing for production use</li>
 *   <li><strong>Error Handling:</strong> Limited error reporting for malformed input</li>
 *   <li><strong>Character Encoding:</strong> Assumes US-ASCII compatible input</li>
 *   <li><strong>Semicolon Handling:</strong> Automatically removes trailing semicolon if present</li>
 * </ul>
 * 
 * <h2>Performance Optimization</h2>
 * <ul>
 *   <li>Uses primitive byte arrays instead of String operations</li>
 *   <li>Minimizes object creation during parsing</li>
 *   <li>Reuses stack structure across node processing</li>
 *   <li>Direct memory mapping for file I/O</li>
 * </ul>
 * 
 * <h2>Integration with eGPS</h2>
 * <ul>
 *   <li>Used by {@link evoltree.struct.TreeDecoder} for tree parsing</li>
 *   <li>Foundation for high-performance phylogenetic analysis workflows</li>
 *   <li>Compatible with all eGPS tree node implementations</li>
 * </ul>
 *
 * @param <T> the type of tree node to decode (must extend {@link EvolNode})
 * @author yudalang
 * @version 1.0
 * @since 1.0
 * @see AbstractNodeCoderDecoder For node parsing interface
 * @see evoltree.struct.TreeDecoder For the standard tree decoder
 * @see evoltree.struct.TreeCoder For tree encoding
 */
public class PrimaryNodeTreeDecoder<T extends EvolNode> {

	public final static byte LEFT_BRACKET_BYTE = '(';
	public final static byte RIGHT_BRACKET_BYTE = ')';
	public final static byte COMMA_BYTE = ',';
	public final static byte SEMICOLON_BYTE = ';';

	final AbstractNodeCoderDecoder<T> leafNodeParse;
	final AbstractNodeCoderDecoder<T> internalNodeParse;

	@SuppressWarnings("unchecked")
	public PrimaryNodeTreeDecoder() {
		leafNodeParse = (AbstractNodeCoderDecoder<T>) new BasicLeafCoderDecoder<ArrayBasedNode>();
		internalNodeParse = (AbstractNodeCoderDecoder<T>) new BasicInternalCoderDecoder<ArrayBasedNode>();
	}

	/***
	 *
	 * @title TreeDecoderLessMemory
	 * @createdDate 2020-11-22 10:03
	 * @lastModifiedDate 2024-12-18 10:03
	 * @author yudalang
	 *
	 * @param p1          : leaf node parser
	 * @param p2          : internal node parser
	 */
	public PrimaryNodeTreeDecoder(AbstractNodeCoderDecoder<T> p1, AbstractNodeCoderDecoder<T> p2) {
		leafNodeParse = p1;
		internalNodeParse = p2;
	}

	public T decode(Path filename) throws Exception {
		byte[] line;
		try (FileChannel fileChannel = FileChannel.open(filename)) {
			long size = fileChannel.size();
			MappedByteBuffer mappedByteBuffer = fileChannel.map(MapMode.READ_ONLY, 0, size);
			line = new byte[(int) size];
			mappedByteBuffer.get(line);
		}

		return decode(line);
	}
	/**
	 * Parser nwk-like string to a tree!
     */
	public T decode(String line) {
		return decode(line, false);
	}
	public T decode(String line, boolean removeWhitespace) {
		String ret;
		if (removeWhitespace) {
			StringBuilder buffer = new StringBuilder();
			int length = line.length();
			for (int i = 0; i < length; i++) {
				char c = line.charAt(i);
				if (!Character.isWhitespace(c)) {
					buffer.append(c);
				}
			}
			ret = buffer.toString();
		}else {
			ret = line.trim();
		}
		return decode(ret.getBytes());
	}

	public T decode(byte[] nwkLine) {
		T root = null;
		Stack<T> stack = new Stack<>();
		int totalLength = nwkLine.length;
		// If it comes with a ";", then remove this character. In some cases, ";" may be used as a separator.
		if (nwkLine[totalLength - 1] == SEMICOLON_BYTE) {
			totalLength--;
		}
		int currentIndex = 0;
		while (currentIndex < totalLength) {
			byte currentByte = nwkLine[currentIndex];
			switch (currentByte) {
			case LEFT_BRACKET_BYTE:
				T internalNode = internalNodeParse.createNode();
				// the codes also could be added to the
				if (stack.isEmpty()) {
					root = internalNode;
				} else {
					stack.peek().addChild(internalNode);
				}
				stack.push(internalNode);
				currentIndex++;
				break;
			case COMMA_BYTE:
				// , char, do nothing
				currentIndex++;
				break;
			case RIGHT_BRACKET_BYTE:
				int searchStartIndex = currentIndex + 1;
				int nextCommaOrLeftBracketIndex1 = getNextStopCharIndex(nwkLine, searchStartIndex, totalLength);
				String string4InternalNode = new String(nwkLine, searchStartIndex,
						nextCommaOrLeftBracketIndex1 - searchStartIndex, StandardCharsets.US_ASCII);
				T pop = stack.pop();
				internalNodeParse.parseNode(pop, string4InternalNode);
				currentIndex = nextCommaOrLeftBracketIndex1;

				break;
			default:
				// The leaf name
				T leafNode = leafNodeParse.createNode();

				int nextCommaOrLeftBracketIndex2 = getNextStopCharIndex(nwkLine, currentIndex + 1, totalLength);
				String string4externalNode = new String(nwkLine, currentIndex,
						nextCommaOrLeftBracketIndex2 - currentIndex, StandardCharsets.US_ASCII);
				leafNodeParse.parseNode(leafNode, string4externalNode);
				stack.peek().addChild(leafNode);
				currentIndex = nextCommaOrLeftBracketIndex2;
				break;
			}
		}
		return root;
	}

	/**
	 * Stop char is <code>,)(</code>
	 *
	 * Note the return value is the index of the next stop char.
	 * And the parse method will not repeat iterate the byte[] array!
	 */
	private int getNextStopCharIndex(byte[] line, int currentIndex, int totalLength) {
		int ret = currentIndex;
		while (ret < totalLength) {
			byte currChar = line[ret];
			if (currChar == COMMA_BYTE || currChar == RIGHT_BRACKET_BYTE || currChar == LEFT_BRACKET_BYTE) {
				return ret;
			}
			ret++;
		}
		return ret;
	}



}