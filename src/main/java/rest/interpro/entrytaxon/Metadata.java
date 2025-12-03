package rest.interpro.entrytaxon;

import java.util.List;

/**
 * Metadata container for InterPro entry information.
 * 
 * <p>
 * This class represents metadata associated with entries in the InterPro database,
 * which integrates information from multiple protein family databases including
 * Pfam, Prosite, SMART, CDD, and others.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>accession:</strong> Unique identifier for the InterPro entry</li>
 *   <li><strong>name:</strong> Human-readable name of the protein family/domain</li>
 *   <li><strong>parent:</strong> Parent entry in the hierarchy</li>
 *   <li><strong>source_database:</strong> Source database from which this entry originates</li>
 *   <li><strong>children:</strong> List of child entries in the hierarchy</li>
 * </ul>
 * 
 * <h2>InterPro Database Context:</h2>
 * <p>
 * InterPro provides a unified classification system for protein families, domains,
 * and functional sites by integrating data from multiple member databases. Each
 * entry represents a consensus classification that is supported by evidence from
 * multiple sources.
 * </p>
 * 
 * <h2>Database Hierarchy:</h2>
 * <ul>
 *   <li><strong>Parent-Child Relationships:</strong> Entries form hierarchical structures</li>
 *   <li><strong>Source Attribution:</strong> Tracks original source databases</li>
 *   <li><strong>Cross-References:</strong> Links to external database entries</li>
 * </ul>
 * 
 * <h2>Usage in Bioinformatics:</h2>
 * <ul>
 *   <li><strong>Functional Annotation:</strong> Assign functions to unknown proteins</li>
 *   <li><strong>Domain Architecture:</strong> Analyze protein domain arrangements</li>
 *   <li><strong>Cross-Database Integration:</strong> Unified access to multiple sources</li>
 *   <li><strong>Evolutionary Analysis:</strong> Study protein family evolution</li>
 *   <li><strong>Quality Assessment:</strong> Multiple evidence sources increase confidence</li>
 * </ul>
 * 
 * <h2>Member Databases:</h2>
 * <ul>
 *   <li><strong>Pfam:</strong> Protein family classifications</li>
 *   <li><strong>PROSITE:</strong> Functional sites and patterns</li>
 *   <li><strong>SMART:</strong> Signaling domains</li>
 *   <li><strong>CDD:</strong> Conserved Domain Database</li>
 *   <li><strong>TIGRFAMs:</strong> Protein families</li>
 * </ul>
 * 
 * @see IPREntryTaxonInfoBean
 * @see IPREntryTaxonInfoRecordBean
 * @author eGPS Development Team
 * @since 1.0
 */
public class Metadata {

	String accession;
	String name;
	
	String parent;
	
	String source_database;
	
	List<String> children;

	public Metadata() {

	}

	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getSource_database() {
		return source_database;
	}

	public void setSource_database(String source_database) {
		this.source_database = source_database;
	}

	public List<String> getChildren() {
		return children;
	}

	public void setChildren(List<String> children) {
		this.children = children;
	}
	
	
}
