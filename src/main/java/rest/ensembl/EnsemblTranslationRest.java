package rest.ensembl;

import rest.ensembl.proteins.RestGetProteinDomains;

/**
 * REST client utility for Ensembl translation services and protein domain queries.
 * 
 * <p>
 * This class provides a simple interface for accessing Ensembl REST API endpoints
 * related to protein translation and domain annotation. It serves as a utility
 * for retrieving protein information from the Ensembl database, particularly
 * focusing on protein domain data and translation mappings.
 * </p>
 * 
 * <h2>Functionality:</h2>
 * <p>
 * The primary purpose of this class is to demonstrate and provide access to:
 * </p>
 * <ul>
 *   <li><strong>Protein Domain Queries:</strong> Retrieve domain annotations for specific proteins</li>
 *   <li><strong>Translation Services:</strong> Access protein translation information</li>
 *   <li><strong>Batch Processing:</strong> Support for processing multiple protein identifiers</li>
 *   <li><strong>API Integration:</strong> Bridge between eGPS and Ensembl REST API</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * {@code
 * // Direct usage with specific protein ID
 * String proteinId = "Os01t0974500-01";  // Arabidopsis thaliana protein
 * 
 * // The main method demonstrates protein domain retrieval
 * EnsemblTranslationRest.main(new String[]{});
 * 
 * // The code creates a RestGetProteinDomains instance and queries
 * // a specific protein for its domain annotations
 * }
 * </pre>
 * 
 * <h2>Key Dependencies:</h2>
 * <ul>
 *   <li>{@link RestGetProteinDomains}: Core implementation for protein domain queries</li>
 *   <li>Ensembl REST API: External service for biological data</li>
 *   <li>Network connectivity: Required for API access</li>
 * </ul>
 * 
 * <h2>Integration Points:</h2>
 * <ul>
 *   <li><strong>Bioinformatics Workflows:</strong> Part of protein analysis pipelines</li>
 *   <li><strong>Domain Annotation:</strong> Used in protein function prediction</li>
 *   <li><strong>Comparative Genomics:</strong> Cross-species protein analysis</li>
 *   <li><strong>Database Integration:</strong> Bridges local analysis with Ensembl data</li>
 * </ul>
 * 
 * <h2>Performance Considerations:</h2>
 * <ul>
 *   <li>Network latency: Dependent on Ensembl API response times</li>
 *   <li>Rate limiting: May be subject to Ensembl API usage limits</li>
 *   <li>Error handling: Network errors and API unavailability</li>
 *   <li>Caching: No built-in caching for repeated queries</li>
 * </ul>
 * 
 * <h2>Note:</h2>
 * <p>
 * This class currently serves as a demonstration and utility wrapper.
 * The actual domain retrieval logic is delegated to {@link RestGetProteinDomains}.
 * Future enhancements may include more comprehensive REST functionality.
 * </p>
 * 
 * @author eGPS Development Team
 * @since 1.0
 * @see RestGetProteinDomains
 */
public class EnsemblTranslationRest {


	public static void main(String[] args) throws Exception {
		RestGetProteinDomains restGetProteinDomains = new RestGetProteinDomains();
//		restGetProteinDomains.doIt();
		
		String protID = "Os01t0974500-01";
		
		String outPath = "C:\\Users\\yudal\\Documents\\BaiduSyncdisk\\博士后工作开展\\文献管理\\具体文献\\Wnt通路\\素材\\Curated_data\\arabidopsis\\5_Notum\\ff.txt";
		restGetProteinDomains.setOutputPath(outPath);
		
		restGetProteinDomains.doOnceRest(protID);
	}
}