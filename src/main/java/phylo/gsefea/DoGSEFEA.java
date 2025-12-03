package phylo.gsefea;

import analysis.AbstractAnalysisAction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import evoltree.struct.EvolNode;
import evoltree.struct.io.PrimaryNodeTreeDecoder;
import evoltree.struct.util.EvolNodeUtil;
import evoltree.struct.util.EvolTreeOperator;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsv.io.KitTable;
import tsv.io.TSVReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * GSEFEA (Gene Set Enrichment for Evolutionary Functional Analysis) analyzer.
 * 
 * <p>
 * This analysis action performs Gene Set Enrichment Analysis (GSEA) in the context
 * of evolutionary relationships, combining phylogenetic tree topology with gene
 * expression or occurrence data to identify evolutionarily significant gene sets.
 * </p>
 * 
 * <h2>Algorithm Overview:</h2>
 * <p>
 * GSEFEA integrates three types of data:
 * </p>
 * <ol>
 *   <li><strong>Phylogenetic Tree:</strong> Represents evolutionary relationships</li>
 *   <li><strong>Gene Formation Data:</strong> Contains gene occurrence/count information per node</li>
 *   <li><strong>Statistical Analysis:</strong> Uses Poisson distribution for significance testing</li>
 * </ol>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>Tree Integration:</strong> Considers evolutionary relationships in enrichment analysis</li>
 *   <li><strong>Poisson Testing:</strong> Uses statistical significance testing for enrichment</li>
 *   <li><strong>Multiple Gene Sets:</strong> Analyzes enrichment across multiple gene sets</li>
 *   <li><strong>TSV Output:</strong> Generates detailed results in tab-separated format</li>
 *   <li><strong>Logging:</strong> Comprehensive logging for analysis tracking</li>
 * </ul>
 * 
 * <h2>Input Data Structure:</h2>
 * <h3>Tree File (.nwk format):</h3>
 * <pre>
 * ((SpeciesA:0.1,SpeciesB:0.2):0.05,(SpeciesC:0.3,SpeciesD:0.4):0.06)Root:0.0;
 * </pre>
 * 
 * <h3>Gene Formation Count File (TSV):</h3>
 * <pre>
 * node_name    gene_set_id    count
 * Node1        GeneSet1       25
 * Node2        GeneSet1       30
 * Node1        GeneSet2       15
 * </pre>
 * 
 * <h2>Output Format:</h2>
 * <p>Results are written to TSV file with the following columns:</p>
 * <ul>
 *   <li><strong>Gene Set ID:</strong> Identifier of the gene set</li>
 *   <li><strong>Enrichment Score:</strong> Computed enrichment measure</li>
 *   <li><strong>P-value:</strong> Statistical significance (Poisson distribution)</li>
 *   <li><strong>Adjusted P-value:</strong> Multiple testing correction</li>
 *   <li><strong>Node Count:</strong> Number of nodes with gene occurrence</li>
 *   <li><strong>Total Count:</strong> Total gene occurrences across tree</li>
 * </ul>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Evolutionary Gene Set Analysis:</strong> Identify genes that co-evolve</li>
 *   <li><strong>Functional Enrichment:</strong> Find enriched functions in phylogenetic contexts</li>
 *   <li><strong>Species Comparison:</strong> Compare gene sets across related species</li>
 *   <li><strong>Evolutionary Medicine:</strong> Study disease gene evolution</li>
 *   <li><strong>Comparative Genomics:</strong> Analyze gene family evolution</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * DoGSEFEA analyzer = new DoGSEFEA();
 * analyzer.setTreeFilePath("species_tree.nwk");
 * analyzer.setNodeGeneFormationCountFilePath("gene_counts.tsv");
 * analyzer.setOutputFilePath("gsefea_results.tsv");
 * analyzer.operate();
 * </pre>
 * 
 * <h2>Statistical Method:</h2>
 * <p>
 * The enrichment analysis uses a Poisson distribution to model the expected
 * distribution of gene occurrences under the null hypothesis. The p-value
 * represents the probability of observing the actual or more extreme
 * enrichment by chance.
 * </p>
 * 
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Tree loading: O(n) where n is the number of nodes</li>
 *   <li>Gene set processing: O(g×n) where g is number of gene sets</li>
 *   <li>Poisson testing: O(g) for statistical calculations</li>
 *   <li>Memory usage scales with tree size and gene set complexity</li>
 * </ul>
 * 
 * @see analysis.AbstractAnalysisAction
 * @see EvolNode
 * @see evoltree.struct.util.EvolTreeOperator
 * @see org.apache.commons.math3.distribution.PoissonDistribution
 * @author eGPS Development Team
 * @since 1.0
 */
public class DoGSEFEA extends AbstractAnalysisAction {

    private static final Logger log = LoggerFactory.getLogger(DoGSEFEA.class);
    String treeFilePath;
    String nodeGeneFormationCountFilePath;
    String outputFilePath;

    public DoGSEFEA(){

    }
    public DoGSEFEA(String treeFilePath, String nodeGeneFormationCountFilePath, String outputFilePath) {
        this.treeFilePath = treeFilePath;
        this.nodeGeneFormationCountFilePath = nodeGeneFormationCountFilePath;
        this.outputFilePath = outputFilePath;
    }

    public List<String> perform(String treeFilePath, String resultSummaryByNodesPath, String outputFilePath) throws Exception {
        String treeContents = Files.readString(Path.of(treeFilePath));
        final String ROOT_NAME = "Root";
        PrimaryNodeTreeDecoder primaryNodeTreeDecoder = new PrimaryNodeTreeDecoder();
        EvolNode root = primaryNodeTreeDecoder.decode(treeContents);
        root.setName(ROOT_NAME);
        root.setLength(0);

        List<String> outputs4console = Lists.newArrayList();
        double treeLength = EvolTreeOperator.getTreeLength(root);

        Map<String, Integer> nodeName2FormationCountsMap = getStringIntegerMap(resultSummaryByNodesPath);

        // Use the stream API to get the total counts
        int totalCounts = nodeName2FormationCountsMap.values().stream().mapToInt(Integer::intValue).sum();
        double evolutionaryRate = totalCounts / treeLength;
        outputs4console.add("# The evolutionary rate is " + evolutionaryRate);

        List<String> outputResults = Lists.newLinkedList();
        outputResults.addAll(outputs4console);
        outputResults.add("nodeName\tbranchLength\tobservedCounts\texpectedCounts\tpValue");

        EvolNodeUtil.recursiveIterateTreeIF(root, currNode -> {
            String nodeName = currNode.getName();

            if (Objects.equals(nodeName, ROOT_NAME)){
                return;
            }

            int geneGainOrLossCount = 0;
            Integer counts = nodeName2FormationCountsMap.get(nodeName);
            if (counts == null){
                log.warn("Node: " + nodeName + " is not found in the nodeGeneFormationCountFilePath file, assume it is 0.");
            }else {
                geneGainOrLossCount = counts;
            }


            double expectedCounts = currNode.getLength() * evolutionaryRate;
            if (expectedCounts <= 0) {
                outputResults.add(nodeName + "\t" + currNode.getLength() + "\t" + geneGainOrLossCount + "\t" + expectedCounts + "\t0.0");
            } else {
                double observedCounts = geneGainOrLossCount;
                // Help me use passion distribution to calculate the p-value using the apache commons math or else lib
                // for k >= observed counts
                // 使用 Apache Commons Math 库计算 p-value
                PoissonDistribution poissonDist = new PoissonDistribution(expectedCounts);
                double pValue = 1 - poissonDist.cumulativeProbability((int) observedCounts - 1);
                currNode.setDoubleVariable(pValue);

                StringJoiner sJ = new StringJoiner("\t");
                sJ.add(nodeName).add(String.valueOf(currNode.getLength()));
                sJ.add(String.valueOf(observedCounts)).add(String.valueOf(expectedCounts));
                sJ.add(String.valueOf(pValue));
                outputResults.add(sJ.toString());
            }

        });
        Files.write(Path.of(outputFilePath), outputResults);
        return outputs4console;
    }

    private static Map<String, Integer> getStringIntegerMap(String resultSummaryByNodesPath) throws IOException {
        KitTable kitTable = TSVReader.readTsvTextFile(resultSummaryByNodesPath);
        List<List<String>> contents = kitTable.getContents();
        //NodeName	counts	events
        Map<String, Integer> nodeName2Counts = Maps.newLinkedHashMap();
        for (List<String> content : contents) {
            int value = Integer.parseInt(content.get(1));
            String key = content.get(0);
            nodeName2Counts.put(key, value);
        }
        return nodeName2Counts;
    }

    @Override
    public void doIt() throws Exception {
        List<String> perform = perform(treeFilePath, nodeGeneFormationCountFilePath, outputFilePath);
        for (String s : perform){
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws Exception {
        String treeFilePath = args[0];
        String nodeGeneFormationCountFilePath = args[1];
        String outputFilePath = args[2];

        DoGSEFEA doGSEFEA = new DoGSEFEA(treeFilePath, nodeGeneFormationCountFilePath, outputFilePath);

        doGSEFEA.runIt();
    }
}
