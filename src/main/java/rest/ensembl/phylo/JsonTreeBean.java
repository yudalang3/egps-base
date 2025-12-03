package rest.ensembl.phylo;

/**
 * Container bean for Ensembl phylogenetic tree JSON responses.
 * 
 * <p>
 * This class serves as a wrapper container for phylogenetic tree data
 * returned by Ensembl's JSON API. It provides a structured representation
 * of the complete JSON response including tree metadata and the actual
 * phylogenetic tree structure.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>type:</strong> Tree type identifier (e.g., "gene tree")</li>
 *   <li><strong>rooted:</strong> Indicates whether the tree is rooted (1=true, 0=false)</li>
 *   <li><strong>id:</strong> Unique identifier for the phylogenetic tree</li>
 *   <li><strong>tree:</strong> Complete tree structure with all nodes and relationships</li>
 * </ul>
 * 
 * <h2>JSON Response Structure:</h2>
 * <p>This class represents the top-level structure of Ensembl phylogenetic
 * tree JSON responses, which typically include metadata at the top level
 * and the actual tree data in the tree field.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * JsonTreeBean treeBean = new JsonTreeBean();
 * treeBean.setType("gene tree");
 * treeBean.setRooted(1);
 * treeBean.setId("gene_tree_12345");
 * treeBean.setTree(treeObject);
 * 
 * // Access metadata
 * String treeType = treeBean.getType();
 * boolean isRooted = treeBean.getRooted() == 1;
 * 
 * // Access tree structure
 * TreeBean phylogeneticTree = treeBean.getTree();
 * </pre>
 * 
 * @see TreeBean
 * @see EnsJsonTreeParser
 * @author eGPS Development Team
 * @since 1.0
 */
public class JsonTreeBean {

	String type;
	int rooted;
	String id;
	
	TreeBean tree;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getRooted() {
		return rooted;
	}

	public void setRooted(int rooted) {
		this.rooted = rooted;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TreeBean getTree() {
		return tree;
	}

	public void setTree(TreeBean tree) {
		this.tree = tree;
	}
	
	
	
}
