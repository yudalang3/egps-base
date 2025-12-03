package rest.interpro.entrytaxon;

import java.util.List;

/**
 * Container bean for InterPro entry information with results list.
 * 
 * <p>
 * This class serves as the main container for organizing multiple InterPro
 * entry information records returned from taxonomy-related database queries.
 * It provides a standardized structure for processing batch queries to
 * the InterPro REST API.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>results:</strong> List of IPREntryTaxonInfoRecordBean objects containing individual entry data</li>
 * </ul>
 * 
 * <h2>REST API Integration:</h2>
 * <p>
 * This bean is designed to receive and structure data from InterPro REST API
 * calls that return multiple protein family entries. It serves as the top-level
 * container for organizing complex API responses.
 * </p>
 * 
 * <h2>Usage in Bioinformatics:</h2>
 * <ul>
 *   <li><strong>Batch Processing:</strong> Handle multiple protein family queries</li>
 *   <li><strong>Taxonomy Analysis:</strong> Process protein family data across taxonomic lineages</li>
 *   <li><strong>Database Integration:</strong> Standardize InterPro API response handling</li>
 *   <li><strong>Statistical Analysis:</strong> Aggregate data for quantitative analysis</li>
 *   <li><strong>Cross-Database Queries:</strong> Support multi-source protein annotation</li>
 * </ul>
 * 
 * <h2>API Response Structure:</h2>
 * <p>Typically used to structure responses from InterPro taxonomy-related endpoints
 * such as entries by taxonomic classification or protein families with taxonomy annotations.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * IPREntryTaxonInfoBean bean = new IPREntryTaxonInfoBean();
 * List&lt;IPREntryTaxonInfoRecordBean&gt; records = bean.getResults();
 * 
 * // Process each record
 * for (IPREntryTaxonInfoRecordBean record : records) {
 *     String accession = record.getMetadata().getAccession();
 *     processEntry(record);
 * }
 * </pre>
 * 
 * @see IPREntryTaxonInfoRecordBean
 * @see InterProEntryTaxonInfoParser
 * @author eGPS Development Team
 * @since 1.0
 */
public class IPREntryTaxonInfoBean {
	List<IPREntryTaxonInfoRecordBean> results;

	public List<IPREntryTaxonInfoRecordBean> getResults() {
		return results;
	}

	public void setResults(List<IPREntryTaxonInfoRecordBean> results) {
		this.results = results;
	}
	
	
}
