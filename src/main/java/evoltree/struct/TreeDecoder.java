package evoltree.struct;

import evoltree.struct.io.PrimaryNodeTreeDecoder;

/**
 * Tree decoder class for parsing evolutionary tree strings into tree structures.
 *
 * This class acts as a proxy to the actual tree decoding implementation and provides
 * a simple interface for converting Newick-like format strings to tree structures.
 *
 * This is the code from the eGPS develop team.
 *
 * @author yudal
 * @version 1.0
 * @since 2025.07.23
 */
public class TreeDecoder {


    /**
     * Constructs a new TreeDecoder instance.
     */
    public TreeDecoder() {
    }

    /**
     * Parses a Newick-like format string into an evolutionary tree structure.
     * 
     * @param line the input string (should not contain the ';' character)
     * @return the root node of the parsed tree
     * @throws Exception if there is an error during parsing
     */
    public EvolNode decode(String line) throws Exception {
        PrimaryNodeTreeDecoder<EvolNode> treeDecoderWithMap = new PrimaryNodeTreeDecoder<>();
        return treeDecoderWithMap.decode(line);
    }


}
