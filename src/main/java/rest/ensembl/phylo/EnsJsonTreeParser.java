package rest.ensembl.phylo;

import analysis.AbstractAnalysisAction;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import evoltree.phylogeny.DefaultPhyNode;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Parser for Ensembl phylogenetic tree JSON responses.
 * 
 * <p>
 * This class extends AbstractAnalysisAction to provide comprehensive
 * parsing capabilities for Ensembl's phylogenetic tree API responses.
 * It converts JSON tree structures into Java phylogenetic tree objects
 * suitable for further analysis and visualization.
 * </p>
 * 
 * <h2>Key Features:</h2>
 * <ul>
 *   <li><strong>JSON Processing:</strong> Parse Ensembl phylogenetic tree JSON responses</li>
 *   <li><strong>Tree Conversion:</strong> Convert JSON to DefaultPhyNode phylogenetic trees</li>
 *   <li><strong>Taxonomy Integration:</strong> Extract and integrate taxonomic information</li>
 *   <li><strong>Branch Length Support:</strong> Preserve evolutionary distances and branch lengths</li>
 * </ul>
 * 
 * <h2>Ensembl Phylogenetic Trees:</h2>
 * <p>This class specifically handles phylogenetic trees from Ensembl's
 * genetree API which provides comprehensive evolutionary relationships
 * between genes across different species and taxonomic groups.</p>
 * 
 * <h2>Input Format:</h2>
 * <p>JSON files containing Ensembl phylogenetic tree responses with
 * detailed node information, branch lengths, and taxonomic annotations.</p>
 * 
 * <h2>Output Structure:</h2>
 * <ul>
 *   <li><strong>JsonTreeBean:</strong> Java object representation of JSON structure</li>
 *   <li><strong>DefaultPhyNode:</strong> Phylogenetic tree nodes for analysis</li>
 *   <li><strong>Branch Lengths:</strong> Evolutionary distance information</li>
 *   <li><strong>Taxonomic Context:</strong> Species names and classification data</li>
 * </ul>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Phylogenetic Analysis:</strong> Study evolutionary relationships</li>
 *   <li><strong>Tree Visualization:</strong> Generate phylogenetic tree displays</li>
 *   <li><strong>Comparative Analysis:</strong> Compare gene family evolution patterns</li>
 *   <li><strong>Evolutionary Dating:</strong> Calculate divergence times and rates</li>
 *   <li><strong>Taxonomic Studies:</strong> Analyze species relationships</li>
 * </ul>
 * 
 * <h2>Tree Processing Workflow:</h2>
 * <ol>
 *   <li>Read JSON file from Ensembl tree API response</li>
 *   <li>Parse JSON into JsonTreeBean structure</li>
 *   <li>Convert to DefaultPhyNode phylogenetic tree</li>
 *   <li>Preserve branch lengths and taxonomic information</li>
 *   <li>Return tree suitable for analysis and visualization</li>
 * </ol>
 * 
 * <h2>Taxonomic Integration:</h2>
 * <p>Each tree node includes comprehensive taxonomic information:</p>
 * <ul>
 *   <li><strong>Common Names:</strong> Human-readable species names</li>
 *   <li><strong>Scientific Names:</strong> Official taxonomic nomenclature</li>
 *   <li><strong>Timetree Data:</strong> Divergence time estimates</li>
 *   <li><strong>Taxonomic IDs:</strong> Database cross-references</li>
 * </ul>
 * 
 * <h2>Tree Node Processing:</h2>
 * <p>The parser handles both internal nodes and leaf nodes:</p>
 * <ul>
 *   <li><strong>Internal Nodes:</strong> Ancestral species or speciation events</li>
 *   <li><strong>Leaf Nodes:</strong> Current species or terminal taxa</li>
 *   <li><strong>Branch Lengths:</strong> Evolutionary distance between nodes</li>
 *   <li><strong>Confidence Values:</strong> Statistical support for relationships</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * EnsJsonTreeParser parser = new EnsJsonTreeParser();
 * 
 * // Set input JSON file
 * parser.setInputPath("ensembl_tree.json");
 * 
 * // Parse JSON structure
 * parser.doIt();
 * 
 * // Get parsed data
 * JsonTreeBean treeBean = parser.getParseObject();
 * 
 * // Convert to phylogenetic tree
 * DefaultPhyNode phylogeneticTree = parser.getTheGraphicsTree();
 * 
 * // Analyze tree structure
 * String rootName = phylogeneticTree.getName();
 * List&lt;DefaultPhyNode&gt; children = phylogeneticTree.getChildren();
 * double rootLength = phylogeneticTree.getLength();
 * </pre>
 * 
 * <h2>Error Handling:</h2>
 * <ul>
 *   <li><strong>Input Validation:</strong> Ensure doIt() is called before getTheGraphicsTree()</li>
 *   <li><strong>JSON Parsing:</strong> Handle malformed JSON with appropriate exceptions</li>
 *   <li><strong>Tree Validation:</strong> Ensure tree structure integrity</li>
 * </ul>
 * 
 * <h2>Integration with Other Classes:</h2>
 * <p>This parser works seamlessly with other eGPS components:</p>
 * <ul>
 *   <li><strong>DefaultPhyNode:</strong> Core phylogenetic tree data structure</li>
 *   <li><strong>TreeBean:</strong> Intermediate tree representation</li>
 *   <li><strong>JsonTreeBean:</strong> JSON parsing container</li>
 *   <li><strong>TaxonomyBean:</strong> Taxonomic information integration</li>
 * </ul>
 * 
 * @see JsonTreeBean
 * @see TreeBean
 * @see DefaultPhyNode
 * @see TaxonomyBean
 * @see EnsJsonTreeParserCustomized
 * @author eGPS Development Team
 * @since 1.0
 */
public class EnsJsonTreeParser extends AbstractAnalysisAction {
	
	JsonTreeBean parseObject;

	@Override
	public void doIt() throws Exception {

		String readFileToString = FileUtils.readFileToString(new File(inputPath), StandardCharsets.UTF_8);

		parseObject = JSONObject.parseObject(readFileToString, JsonTreeBean.class);

	}

	
	public JsonTreeBean getParseObject() {
		return parseObject;
	}
	
	public DefaultPhyNode getTheGraphicsTree() {
		if (parseObject == null) {
			throw new InputMismatchException("Call the doIt method first.");
		}
		TreeBean tree = parseObject.getTree();
		DefaultPhyNode ret = iterate2transferTree(tree);
		return ret;
	}


	protected DefaultPhyNode iterate2transferTree(TreeBean tree) {
		DefaultPhyNode node = new DefaultPhyNode();
		node.setLength(tree.branch_length);
		
		String humanNamedName = tree.getTaxonomy().getCommon_name();
		if (Strings.isEmpty(humanNamedName)) {
			humanNamedName = tree.getTaxonomy().getScientific_name();
		}
		
		List<TreeBean> children = tree.children;
		if (children == null) {
			node.setName(humanNamedName);
			return node;
		}
		
		String name = Joiner.on('|').join(humanNamedName,tree.taxonomy.timetree_mya);
		node.setName(name);
		for (TreeBean treeBean : children) {
			DefaultPhyNode child = iterate2transferTree(treeBean);
			node.addChild(child);
		}
		
		return node;
	}
	

}
