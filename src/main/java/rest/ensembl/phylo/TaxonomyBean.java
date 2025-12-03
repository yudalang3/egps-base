package rest.ensembl.phylo;

/**
 * Taxonomic information bean for phylogenetic tree nodes.
 * 
 * <p>
 * This class represents taxonomic information associated with nodes
 * in phylogenetic trees from the Ensembl API. It provides essential
 * taxonomic data including common names, scientific names, temporal
 * information, and database identifiers.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>common_name:</strong> Common name for the species or taxon (e.g., "Human")</li>
 *   <li><strong>timetree_mya:</strong> Time in millions of years ago from TimeTree database</li>
 *   <li><strong>scientific_name:</strong> Scientific binomial name (e.g., "Homo sapiens")</li>
 *   <li><strong>id:</strong> Internal taxonomy identifier from Ensembl database</li>
 * </ul>
 * 
 * <h2>Taxonomic Information Context:</h2>
 * <p>This class provides standardized taxonomic information that enables
 * proper identification and classification of species in phylogenetic
 * analyses. The data integrates multiple taxonomic resources including
 * the TimeTree database for divergence time estimates.</p>
 * 
 * <h2>Phylogenetic Analysis Applications:</h2>
 * <ul>
 *   <li><strong>Species Identification:</strong> Link tree nodes to known species</li>
 *   <li><strong>Divergence Time Analysis:</strong> Calculate evolutionary time scales</li>
 *   <li><strong>Taxonomic Mapping:</strong> Cross-reference with external databases</li>
 *   <li><strong>Comparative Studies:</strong> Compare species across different analyses</li>
 *   <li><strong>Database Integration:</strong> Link phylogenetic data to taxonomy databases</li>
 * </ul>
 * 
 * <h2>TimeTree Integration:</h2>
 * <p>The timetree_mya field provides divergence time estimates from the
 * TimeTree database, enabling evolutionary analysis with temporal context.
 * This is particularly useful for dating phylogenetic events and
 * calculating evolutionary rates.</p>
 * 
 * <h2>Database Cross-References:</h2>
 * <ul>
 *   <li><strong>Ensembl TaxID:</strong> Internal Ensembl taxonomy identifier</li>
 *   <li><strong>TimeTree:</strong> Divergence time estimates in millions of years</li>
 *   <li><strong>NCBI Taxonomy:</strong> Cross-references to NCBI taxonomic database</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * TaxonomyBean taxonomy = new TaxonomyBean();
 * taxonomy.setCommonName("Human");
 * taxonomy.setScientificName("Homo sapiens");
 * taxonomy.setTimetreeMya(6.5f);
 * taxonomy.setId(9606);
 * 
 * // Use for phylogenetic analysis
 * String speciesName = taxonomy.getScientificName();
 * float divergenceTime = taxonomy.getTimetreeMya();
 * int taxID = taxonomy.getId();
 * 
 * // Calculate evolutionary rates
 * double evolutionaryRate = calculateRate(branchLength, divergenceTime);
 * </pre>
 * 
 * <h2>Taxonomic Hierarchy:</h2>
 * <p>This bean represents species-level taxonomic information. Higher-level
 * taxonomic classifications (genus, family, order, etc.) can be obtained
 * through additional API calls or by linking to comprehensive taxonomic
 * databases like NCBI Taxonomy.</p>
 * 
 * <h2>Data Quality Considerations:</h2>
 * <ul>
 *   <li>TimeTree estimates may vary between different studies</li>
 *   <li>Common names can have multiple variants for the same species</li>
 *   <li>Ensembl IDs are species-specific within the Ensembl system</li>
 *   <li>Scientific names follow binomial nomenclature standards</li>
 * </ul>
 * 
 * @see TreeBean
 * @see EventsBean
 * @see ConfidenceBean
 * @see EnsJsonTreeParser
 * @author eGPS Development Team
 * @since 1.0
 */
public class TaxonomyBean {

	String common_name;
	float timetree_mya;
	String scientific_name;
	
	int id;

	public String getCommon_name() {
		return common_name;
	}

	public void setCommon_name(String common_name) {
		this.common_name = common_name;
	}

	public float getTimetree_mya() {
		return timetree_mya;
	}

	public void setTimetree_mya(float timetree_mya) {
		this.timetree_mya = timetree_mya;
	}

	public String getScientific_name() {
		return scientific_name;
	}

	public void setScientific_name(String scientific_name) {
		this.scientific_name = scientific_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
