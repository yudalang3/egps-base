package evoltree.struct;

import java.util.List;

import evoltree.struct.io.AbstractNodeCoderDecoder;
import evoltree.struct.io.BasicInternalCoderDecoder;
import evoltree.struct.io.BasicLeafCoderDecoder;
import evoltree.struct.io.PrimaryNodeTreeDecoder;
import evoltree.struct.util.EvolNodeUtil;

/**
 * Tree coder class for encoding evolutionary trees into string format.
 *
 * This class provides functionality to convert tree structures into string representations
 * using specific coders for leaf and internal nodes. It supports custom node coders
 * or defaults to BasicLeafCoderDecoder and BasicInternalCoderDecoder.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class TreeCoder<T extends EvolNode> {

	final static String LEFT_BRACKET = "(";
	final static String RIGHT_BRACKET = ")";
	final static String COMMA = ",";
	final static String COLON = ":";

	AbstractNodeCoderDecoder<T> leafCoder;
	AbstractNodeCoderDecoder<T> internalNodeCoder;

	/**
	 * Constructs a TreeCoder with default leaf and internal node coders.
	 */
	@SuppressWarnings("unchecked")
	public TreeCoder() {
		this.leafCoder = (AbstractNodeCoderDecoder<T>) new BasicLeafCoderDecoder<ArrayBasedNode>();
		this.internalNodeCoder =  (AbstractNodeCoderDecoder<T>) new BasicInternalCoderDecoder<ArrayBasedNode>();
	}

	/**
	 * Constructs a TreeCoder with custom leaf and internal node coders.
	 * 
	 * @param leafCoder the coder for leaf nodes
	 * @param internalNodeCoder the coder for internal nodes
	 */
	public TreeCoder(AbstractNodeCoderDecoder<T> leafCoder, AbstractNodeCoderDecoder<T> internalNodeCoder) {
		this.leafCoder = leafCoder;
		this.internalNodeCoder = internalNodeCoder;
	}

	/**
	 * Converts a tree structure into its string representation.
	 * 
	 * @param root the root node of the tree
	 * @return the string representation of the tree
	 */
	public String code(T root) {
		StringBuilder sBuilder = new StringBuilder(262144);
		codeForInternalUse(root, sBuilder);
		return sBuilder.toString();
	}

	/**
	 * Recursive method to encode a subtree into the string builder.
	 * 
	 * @param node the current node being encoded
	 * @param sBuilder the string builder to append the encoded tree to
	 */
	private void codeForInternalUse(T node, StringBuilder sBuilder) {
		int childCount = node.getChildCount();

		if (childCount == 0) {
			sBuilder.append( leafCoder.codeNode(node));
			return;
		}

		sBuilder.append(LEFT_BRACKET);
		for (int i = 0; i < childCount; i++) {
			codeForInternalUse((T) node.getChildAt(i), sBuilder);
			if (i < childCount - 1) {
				sBuilder.append(COMMA);
			}
		}
		sBuilder.append(RIGHT_BRACKET);
		
		sBuilder.append(internalNodeCoder.codeNode(node));
		
	}
	
	/**
	 * Main method for testing the TreeCoder functionality.
	 * 
	 * @param args command line arguments (not used)
	 * @throws Exception if there is an error during tree decoding
	 */
	public static void main(String[] args) throws Exception {
		PrimaryNodeTreeDecoder<ArrayBasedNode> treeDecoderWithMap = new PrimaryNodeTreeDecoder<>();
		ArrayBasedNode root = treeDecoderWithMap
				.decode("     (  ((a:8.5,b:8.5)ab:2.5,e:11.0)abe:5.5,   (c:14.0,d:14.0)cd:2.5   )root:0.0           ;", true);
		
		List<ArrayBasedNode> leaves = EvolNodeUtil.getLeaves(root);
		System.out.println(root);
		System.out.println(leaves.size());

		ArrayBasedNode firstChild = (ArrayBasedNode) root.getFirstChild();
		System.out.println(new TreeCoder<ArrayBasedNode>().code(firstChild));



	}

}
