package rest.interpro.entrytaxon;

import java.util.List;

/**
 * Record bean for InterPro entry information with taxonomy data.
 * 
 * <p>
 * This class represents a single record containing InterPro database entry information
 * along with taxonomy-related metadata. It serves as a container for organizing
 * protein family/domain data retrieved from the InterPro REST API.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>metadata:</strong> Entry metadata including accession, name, and hierarchy</li>
 *   <li><strong>entries:</strong> List of entry details with locations and statistics</li>
 *   <li><strong>extra_fields:</strong> Additional fields including counters and statistics</li>
 * </ul>
 * 
 * <h2>Usage in InterPro Integration:</h2>
 * <ul>
 *   <li><strong>Database Queries:</strong> Structure data from InterPro REST API responses</li>
 *   <li><strong>Protein Analysis:</strong> Organize domain information for protein sequences</li>
 *   <li><strong>Taxonomy Mapping:</strong> Link protein families to taxonomic classifications</li>
 *   <li><strong>Data Processing:</strong> Standardized container for downstream analysis</li>
 * </ul>
 * 
 * <h2>InterPro Database Context:</h2>
 * <p>
 * InterPro provides a unified resource for protein family, domain, and site signatures
 * from multiple member databases. Each record represents the complete annotation
 * for a specific protein family or domain entry.
 * </p>
 * 
 * <h2>Bioinformatics Applications:</h2>
 * <ul>
 *   <li><strong>Functional Annotation:</strong> Assign functions to unknown proteins</li>
 *   <li><strong>Domain Architecture:</strong> Analyze protein structural domains</li>
 *   <li><strong>Evolutionary Studies:</strong> Track protein family evolution</li>
 *   <li><strong>Cross-References:</strong> Link to external databases and resources</li>
 *   <li><strong>Quality Control:</strong> Validate protein family assignments</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * IPREntryTaxonInfoRecordBean record = new IPREntryTaxonInfoRecordBean();
 * record.setMetadata(metadata);
 * record.setEntries(entriesList);
 * record.setExtraFields(extraFields);
 * 
 * // Process record for analysis
 * String accession = record.getMetadata().getAccession();
 * List&lt;Entries&gt; entries = record.getEntries();
 * </pre>
 * 
 * @see Metadata
 * @see Entries
 * @see Extra_fields
 * @see InterProEntryTaxonInfoParser
 * @author eGPS Development Team
 * @since 1.0
 */
public class IPREntryTaxonInfoRecordBean {

	Metadata metadata;
	
	List<Entries> entries;
	
	Extra_fields extra_fields;

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public List<Entries> getEntries() {
		return entries;
	}

	public void setEntries(List<Entries> entries) {
		this.entries = entries;
	}

	public Extra_fields getExtra_fields() {
		return extra_fields;
	}

	public void setExtra_fields(Extra_fields extra_fields) {
		this.extra_fields = extra_fields;
	}
	
	
}
