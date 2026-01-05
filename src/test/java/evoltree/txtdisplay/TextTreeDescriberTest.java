package evoltree.txtdisplay;

import evoltree.struct.EvolNode;
import evoltree.struct.util.PhyloTreeGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextTreeDescriberTest {

    @Test
    void displayNodesWithoutGuiByTextLines() {

    }

    @Test
    void displayNodesWithoutGui() {

        PhyloTreeGenerator phyloTreeGenerator = new PhyloTreeGenerator();
        EvolNode theTree = phyloTreeGenerator.getTheTree(4);

        TextTreeDescriber textTreeDescriber = new TextTreeDescriber();
        textTreeDescriber.displayNodesWithoutGui(theTree, 80, 20, false);
//        textTreeDescriber.displayNodesWithoutGuiByTextLines(theTree);
    }

    public static void main(String[] args) {
        TextTreeDescriberTest textTreeDescriberTest = new TextTreeDescriberTest();
        textTreeDescriberTest.displayNodesWithoutGui();
    }
}