package rest.ensembl.phylo;

/**
 * Confidence statistics for phylogenetic tree nodes.
 * 
 * <p>
 * This class represents statistical confidence measures associated with
 * nodes in phylogenetic trees from the Ensembl API. It provides
 * quantitative assessments of the reliability and support for tree
 * topology and branching patterns.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>bootstrap:</strong> Bootstrap support value (typically 0-100 percentage)</li>
 * </ul>
 * 
 * <h2>Bootstrap Support Context:</h2>
 * <p>Bootstrap values are statistical measures that indicate the support
 * for a particular node or relationship in the phylogenetic tree.
 * They are derived from bootstrap resampling analyses where sequence
 * alignments are repeatedly sampled with replacement to assess the
 * robustness of tree topology.</p>
 * 
 * <h2>Phylogenetic Significance:</h2>
 * <ul>
 *   <li><strong>High Support (90-100%):</strong> Strong evidence for the relationship</li>
 *   <li><strong>Moderate Support (70-89%):</strong> Moderate evidence, interpretation caution</li>
 *   <li><strong>Low Support (50-69%):</strong> Weak evidence, relationship uncertain</li>
 *   <li><strong>Very Low Support (&lt;50%):</strong> Poor evidence, relationship unresolved</li>
 * </ul>
 * 
 * <h2>Statistical Analysis Applications:</h2>
 * <ul>
 *   <li><strong>Tree Reliability Assessment:</strong> Evaluate confidence in phylogenetic hypotheses</li>
 *   <li><strong>Comparative Phylogenetics:</strong> Compare support across different analyses</li>
 *   <li><strong>Topology Testing:</strong> Identify well-supported vs. unresolved relationships</li>
 *   <li><strong>Method Comparison:</strong> Compare phylogenetic methods and datasets</li>
 *   <li><strong>Publication Quality:</strong> Assess tree quality for scientific publication</li>
 * </ul>
 * 
 * <h2>Bootstrap Methodology:</h2>
 * <p>Bootstrap analysis involves resampling columns from the original alignment,
 * reconstructing trees from each bootstrap replicate, and counting how often
 * each node appears in replicate trees, expressing support as percentage.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * ConfidenceBean confidence = new ConfidenceBean();
 * confidence.setBootstrap(85);
 * 
 * // Analyze confidence levels
 * int bootstrapValue = confidence.getBootstrap();
 * if (bootstrapValue >= 90) {
 *     processHighSupportNode(node);
 * } else if (bootstrapValue >= 70) {
 *     processModerateSupportNode(node);
 * }
 * </pre>
 * 
 * @see TreeBean
 * @see TaxonomyBean
 * @see EventsBean
 * @author eGPS Development Team
 * @since 1.0
 */
public class ConfidenceBean {

	int bootstrap;

	public int getBootstrap() {
		return bootstrap;
	}

	public void setBootstrap(int bootstrap) {
		this.bootstrap = bootstrap;
	}
	
	
	
}
