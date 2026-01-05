package evoltree.phylogeny;

import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;

import evoltree.struct.io.AbstractNodeCoderDecoder;
import utils.string.EGPSStringUtil;

import java.util.InputMismatchException;

/**
 * Decoder/Encoder for internal nodes in Newick (NWK) format.
 *
 * This class handles the parsing and formatting of internal nodes in phylogenetic trees
 * according to the Newick format specification. It supports nodes with name, bootstrap,
 * and branch length attributes.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class NWKInternalCoderDecoder<T extends DefaultPhyNode> extends AbstractNodeCoderDecoder<T> {

	private StringBuilder sBuilder = new StringBuilder();

	/**
	 * Parses a Newick format string into a node object.
	 * 
	 * @param node the node to populate
	 * @param str the Newick format string
	 * @throws InputMismatchException if the input format is invalid
	 */
	@Override
	public void parseNode(T node, String str) {

		if (str.isEmpty()) {
			return;
		}
		String[] split = EGPSStringUtil.split(str, ':');

		if (split.length < 2) {
			throw new InputMismatchException("Sorry, please check your nwk format: ".concat(str));
		}

		if (split.length == 3) {
			node.setName(split[0]);
			node.setBootstrapValue(Double.parseDouble(split[1]));
			node.setLength(Double.parseDouble(split[2]));
		}

		// has bootstrap
		String firstStr = split[0];
		Double tryParse = Doubles.tryParse(firstStr);
		if (tryParse == null) {
			node.setName(firstStr);
		} else {
			node.setBootstrapValue(tryParse);
		}

		node.setLength(Double.parseDouble(split[1]));

	}

	/**
	 * Converts a node object to its Newick format string representation.
	 * 
	 * @param node the node to convert
	 * @return the Newick format string
	 */
	@Override
	public String codeNode(T node) {

		sBuilder.setLength(0);

		if (!Strings.isNullOrEmpty(node.getName())) {
			sBuilder.append(node.getName());
		}

		if (node.getBootstrapValue() != 0) {
			if (sBuilder.length() > 0) {
				sBuilder.append(COLON);
			}
			sBuilder.append(Double.toString(node.getLength()));
		}

		sBuilder.append(COLON);
		sBuilder.append(convertRate2Decimal(node.getLength()));

		return sBuilder.toString();
	}

	/**
	 * Creates a new instance of the node type.
	 * 
	 * @return a new DefaultPhyNode instance
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T createNode() {
		DefaultPhyNode defaultPhyNode = new DefaultPhyNode();
		return (T) defaultPhyNode;
	}

}
