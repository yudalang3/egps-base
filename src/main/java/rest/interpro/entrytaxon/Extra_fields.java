package rest.interpro.entrytaxon;

/**
 * Additional fields container for InterPro entry information.
 * 
 * <p>
 * This class represents supplementary information associated with InterPro
 * entries that extends beyond the core metadata. It provides additional
 * data structures for storing counters, statistics, and other relevant
 * information that enhances the basic entry description.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>counters:</strong> Statistical counters for the entry (proteins, structures, etc.)</li>
 * </ul>
 * 
 * <h2>InterPro Database Extensions:</h2>
 * <p>
 * InterPro entries often include additional statistical information that
 * provides context about the entry's coverage, usage, and relevance.
 * This class serves as a container for such extended information.
 * </p>
 * 
 * <h2>Usage in Protein Analysis:</h2>
 * <ul>
 *   <li><strong>Statistical Analysis:</strong> Track entry usage and coverage</li>
 *   <li><strong>Quality Metrics:</strong> Assess entry reliability and completeness</li>
 *   <li><strong>Database Statistics:</strong> Aggregate information for system analysis</li>
 *   <li><strong>Performance Monitoring:</strong> Monitor API usage and response patterns</li>
 *   <li><strong>Research Analytics:</strong> Support bibliometric and usage analysis</li>
 * </ul>
 * 
 * <h2>Bioinformatics Context:</h2>
 * <p>Extended fields provide additional metadata that helps researchers
 * understand the scope and impact of protein family entries, supporting
 * decision-making for database selection and research strategies.</p>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * ExtraFields extraFields = new ExtraFields();
 * Counters counters = new Counters();
 * counters.setProteins(15000);
 * counters.setStructures(500);
 * extraFields.setCounters(counters);
 * 
 * // Use for statistical analysis
 * int proteinCount = extraFields.getCounters().getProteins();
 * </pre>
 * 
 * @see Counters
 * @see IPREntryTaxonInfoRecordBean
 * @author eGPS Development Team
 * @since 1.0
 */
public class Extra_fields {
	
	Counters counters;

	public Counters getCounters() {
		return counters;
	}

	public void setCounters(Counters counters) {
		this.counters = counters;
	}

	
}
