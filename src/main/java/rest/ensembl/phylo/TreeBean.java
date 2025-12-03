package rest.ensembl.phylo;

import java.util.List;

/**
 * Data model for phylogenetic tree nodes from Ensembl API.
 * 
 * <p>
 * This class represents a single node in a phylogenetic tree structure
 * returned by the Ensembl REST API. It provides a comprehensive model
 * for storing tree topology, branch lengths, taxonomic information,
 * and evolutionary events associated with each node.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>events:</strong> Evolutionary events associated with this node</li>
 *   <li><strong>branch_length:</strong> Length of the branch connecting to parent</li>
 *   <li><strong>children:</strong> List of child nodes in the tree topology</li>
 *   <li><strong>taxonomy:</strong> Taxonomic information for this node</li>
 *   <li><strong>confidence:</strong> Bootstrap or confidence values for this node</li>
 * </ul>
 * 
 * <h2>Tree Structure Context:</h2>
 * <p>This class represents nodes in hierarchical phylogenetic trees used
 * for evolutionary analysis. Each node can have multiple children,
 * forming a branching structure that represents evolutionary relationships.</p>
 * 
 * <h2>Ensembl Phylogeny API Integration:</h2>
 * <p>This class is designed to receive and structure data from Ensembl's
 * phylogenetic tree API endpoints. It provides a standardized model
 * for representing complex tree structures returned by the API.</p>
 * 
 * <h2>Phylogenetic Analysis Applications:</h2>
 * <ul>
 *   <li><strong>Species Relationships:</strong> Model evolutionary relationships between species</li>
 *   <li><strong>Gene Family Evolution:</strong> Track gene family evolution across lineages</li>
 *   <li><strong>Dating Analysis:</strong> Calculate divergence times and evolutionary rates</li>
 *   <li><strong>Comparative Genomics:</strong> Support comparative analysis across species</li>
 *   <li><strong>Phylogenetic Reconstruction:</strong> Visualize and analyze tree structures</li>
 * </ul>
 * 
 * <h2>Node Properties:</h2>
 * <ul>
 *   <li><strong>Internal Nodes:</strong> Represent ancestral species or gene duplications</li>
 *   <li><strong>Leaf Nodes:</strong> Represent current species or genes (no children)</li>
 *   <li><strong>Branch Lengths:</strong> Evolutionary distance or time between nodes</li>
 *   <li><strong>Taxonomy:</strong> Species identification and classification</li>
 *   <li><strong>Confidence Values:</strong> Statistical support for tree topology</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * TreeBean treeNode = new TreeBean();
 * treeNode.setBranchLength(0.25f);
 * treeNode.setTaxonomy(taxonomyBean);
 * treeNode.setEvents(eventsBean);
 * 
 * // Process tree structure
 * List&lt;TreeBean&gt; children = treeNode.getChildren();
 * if (children.isEmpty()) {
 *     // This is a leaf node
 *     String species = treeNode.getTaxonomy().getScientificName();
 * }
 * 
 * // Calculate subtree statistics
 * int totalDescendants = countDescendants(treeNode);
 * </pre>
 * 
 * <h2>Note on External Properties:</h2>
 * <p>External nodes have additional properties not implemented here.
 * The timetree_mya attribute is not available for external nodes.
 * Additional properties can be added as needed for specific analysis requirements.</p>
 * 
 * @see EventsBean
 * @see TaxonomyBean
 * @see ConfidenceBean
 * @see EnsJsonTreeParser
 * @author yudal
 * @since 1.0
 */
public class TreeBean {

	EventsBean events;
	float branch_length;
	
	List<TreeBean> children;
	
	TaxonomyBean taxonomy;
	
	ConfidenceBean confidence;

	public EventsBean getEvents() {
		return events;
	}

	public void setEvents(EventsBean events) {
		this.events = events;
	}

	public float getBranch_length() {
		return branch_length;
	}

	public void setBranch_length(float branch_length) {
		this.branch_length = branch_length;
	}

	public List<TreeBean> getChildren() {
		return children;
	}

	public void setChildren(List<TreeBean> children) {
		this.children = children;
	}

	public TaxonomyBean getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(TaxonomyBean taxonomy) {
		this.taxonomy = taxonomy;
	}

	public ConfidenceBean getConfidence() {
		return confidence;
	}

	public void setConfidence(ConfidenceBean confidence) {
		this.confidence = confidence;
	}
	
	
	
}
