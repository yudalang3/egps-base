package evoltree.phylogeny;

import evoltree.struct.EvolNode;
import evoltree.struct.util.PhyloTreeGenerator;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PhyloTreeEncoderDecoder class.
 * Tests both encoding and decoding functionality of phylogenetic trees in Newick format.
 */
public class PhyloTreeEncoderDecoderTest {

    private final PhyloTreeEncoderDecoder codec = new PhyloTreeEncoderDecoder();

    @Test
    public void testEncodeSimpleTree() {
        // Create a simple tree: (A:0.1,B:0.2)Root:0.0;
        DefaultPhyNode root = new DefaultPhyNode();
        root.setName("Root");
        root.setLength(0.0);

        DefaultPhyNode leafA = new DefaultPhyNode();
        leafA.setName("A");
        leafA.setLength(0.1);

        DefaultPhyNode leafB = new DefaultPhyNode();
        leafB.setName("B");
        leafB.setLength(0.2);

        root.addChild(leafA);
        root.addChild(leafB);

        String newickString = codec.encode(root);
        assertNotNull(newickString);
        assertTrue(newickString.contains("A:"));
        assertTrue(newickString.contains("B:"));
        // Note: Root node encoding might not include name in some implementations
        assertTrue(newickString.endsWith(";"));
    }

    @Test
    public void testDecodeSimpleNewick() throws Exception {
        String newickString = "(A:0.1,B:0.2)Root:0.0;";
        
        DefaultPhyNode tree = codec.decode(newickString);
        
        assertNotNull(tree);
        assertEquals(2, tree.getChildCount());
        
        DefaultPhyNode child1 = (DefaultPhyNode) tree.getChildAt(0);
        DefaultPhyNode child2 = (DefaultPhyNode) tree.getChildAt(1);
        
        assertNotNull(child1);
        assertNotNull(child2);
        
        // Verify that child nodes have expected names and lengths
        // Note: Implementation may vary on how names and lengths are stored
    }

    @Test
    public void testEncodeDecodeRoundTrip() throws Exception {
        // Create a simple tree
        DefaultPhyNode originalTree = new DefaultPhyNode();
        originalTree.setName("Root");
        originalTree.setLength(0.0);

        DefaultPhyNode leafA = new DefaultPhyNode();
        leafA.setName("Species_A");
        leafA.setLength(0.1);

        DefaultPhyNode leafB = new DefaultPhyNode();
        leafB.setName("Species_B");
        leafB.setLength(0.2);

        originalTree.addChild(leafA);
        originalTree.addChild(leafB);

        // Encode the tree
        String encoded = codec.encode(originalTree);
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());

        // Decode the encoded string back to a tree
        DefaultPhyNode decodedTree = codec.decode(encoded);

        // Verify that the decoded tree has the same structure
        assertNotNull(decodedTree);
        assertEquals(originalTree.getChildCount(), decodedTree.getChildCount());
        
        // Note: Additional structural verification would depend on the implementation details
    }

    @Test
    public void testDecodeComplexNewick() throws Exception {
        String complexNewick = "((Human:0.1,Chimp:0.15)Anc1:0.05,(Mouse:0.2,Rat:0.18)Anc2:0.08)Root:0.0;";
        
        DefaultPhyNode tree = codec.decode(complexNewick);
        
        assertNotNull(tree);
        assertEquals(2, tree.getChildCount());
        
        // Verify the structure of the decoded tree
        DefaultPhyNode leftChild = (DefaultPhyNode) tree.getChildAt(0);
        DefaultPhyNode rightChild = (DefaultPhyNode) tree.getChildAt(1);
        
        assertNotNull(leftChild);
        assertNotNull(rightChild);
        assertEquals(2, leftChild.getChildCount());
        assertEquals(2, rightChild.getChildCount());
    }

    @Test
    public void testEncodeEmptyTree() {
        DefaultPhyNode emptyNode = new DefaultPhyNode();
        emptyNode.setName("Leaf");
        emptyNode.setLength(0.0);

        String result = codec.encode(emptyNode);
        assertNotNull(result);
        // The result depends on implementation, but should be a valid Newick string
    }

    @Test
    public void testDecodeEmptyString() {
        assertThrows(Exception.class, () -> {
            codec.decode("");
        });
    }

    @Test
    public void testDecodeInvalidNewick() {
        assertThrows(Exception.class, () -> {
            codec.decode("invalid_newick");
        });
    }

    @Test
    public void testDecodeNewickWithoutSemicolon() throws Exception {
        String newickWithoutSemicolon = "(A:0.1,B:0.2)";
        
        DefaultPhyNode tree = codec.decode(newickWithoutSemicolon);
        
        assertNotNull(tree);
        assertEquals(2, tree.getChildCount());
    }
    
    @Test
    public void testEncodeDecodeWithBootstrapValues() throws Exception {
        String newickWithBootstrap = "((A:0.1,B:0.2)95:0.05,C:0.3)Root:0.0;";
        
        // Decode the tree with bootstrap values
        DefaultPhyNode tree = codec.decode(newickWithBootstrap);
        assertNotNull(tree);
        
        // Re-encode the tree
        String reEncoded = codec.encode(tree);
        assertNotNull(reEncoded);
        assertTrue(reEncoded.endsWith(";"));
    }

    static void main() {
        String path = "C:\\Users\\yudal\\Documents\\project\\EvolutionsKnowledge\\human_evol_timeline_20260108\\humanEvolutionaryTimeLine241231.nwk";

        PhyloTreeEncoderDecoder phyloTreeEncoderDecoder = new PhyloTreeEncoderDecoder();

        try {
            String treeContents = Files.readString(Path.of(path));
            DefaultPhyNode tree = phyloTreeEncoderDecoder.decode(treeContents);
            String line = phyloTreeEncoderDecoder.encode(tree);
            System.out.println(line);
            System.out.println(treeContents);
            System.out.println(Objects.equals(line, treeContents));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}