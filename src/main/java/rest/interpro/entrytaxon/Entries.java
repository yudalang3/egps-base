package rest.interpro.entrytaxon;

import java.util.List;

/**
 * Individual InterPro entry with detailed annotation information.
 * 
 * <p>
 * This class represents a single InterPro entry containing comprehensive
 * information about a protein family, domain, or site. It provides
 * detailed metadata including accession information, protein characteristics,
 * and spatial location data for proteins associated with this entry.
 * </p>
 * 
 * <h2>Key Properties:</h2>
 * <ul>
 *   <li><strong>accession:</strong> InterPro entry accession number (e.g., IPR000001)</li>
 *   <li><strong>protein_length:</strong> Representative protein length for this entry</li>
 *   <li><strong>source_database:</strong> Original member database (Pfam, Prosite, SMART, etc.)</li>
 *   <li><strong>entry_type:</strong> Type classification (Family, Domain, Site, Repeat)</li>
 *   <li><strong>entry_integrated:</strong> Integration status and relationships</li>
 *   <li><strong>entry_protein_locations:</strong> Location information for associated proteins</li>
 * </ul>
 * 
 * <h2>InterPro Database Structure:</h2>
 * <p>Each entry represents a consensus classification from multiple
 * member databases. The entry_integrated field indicates whether this
 * entry is an integrated consensus or a member-specific annotation.</p>
 * 
 * <h2>Protein Analysis Applications:</h2>
 * <ul>
 *   <li><strong>Domain Architecture:</strong> Define protein domain boundaries</li>
 *   <li><strong>Functional Annotation:</strong> Assign biological functions to proteins</li>
 *   <li><strong>Cross-Database Mapping:</strong> Link annotations across databases</li>
 *   <li><strong>Quality Assessment:</strong> Evaluate annotation reliability</li>
 *   <li><strong>Comparative Analysis:</strong> Compare protein families across species</li>
 * </ul>
 * 
 * <h2>Entry Types:</h2>
 * <ul>
 *   <li><strong>Family:</strong> Protein families (e.g., PFAM, TIGRFAMs)</li>
 *   <li><strong>Domain:</strong> Structural/functional domains (e.g., SMART, CDD)</li>
 *   <li><strong>Site:</strong> Functional sites and active sites (e.g., PROSITE)</li>
 *   <li><strong>Repeat:</strong> Repeated sequence motifs</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>
 * Entries entry = new Entries();
 * entry.setAccession("IPR000001");
 * entry.setProteinLength(300);
 * entry.setSourceDatabase("Pfam");
 * entry.setEntryType("Domain");
 * 
 * // Analyze entry properties
 * boolean isIntegrated = entry.getEntryIntegrated() != null;
 * List&lt;EntryProteinLocations&gt; locations = entry.getEntryProteinLocations();
 * </pre>
 * 
 * @see Entry_protein_locations
 * @see Fragments
 * @see Metadata
 * @author eGPS Development Team
 * @since 1.0
 */
public class Entries {
	
	String accession;
	
	int protein_length;
	
	String source_database;
	
	String entry_type;
	
	String entry_integrated;
	
	List<Entry_protein_locations> entry_protein_locations;

	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public int getProtein_length() {
		return protein_length;
	}

	public void setProtein_length(int protein_length) {
		this.protein_length = protein_length;
	}

	public String getSource_database() {
		return source_database;
	}

	public void setSource_database(String source_database) {
		this.source_database = source_database;
	}

	public String getEntry_type() {
		return entry_type;
	}

	public void setEntry_type(String entry_type) {
		this.entry_type = entry_type;
	}

	public String getEntry_integrated() {
		return entry_integrated;
	}

	public void setEntry_integrated(String entry_integrated) {
		this.entry_integrated = entry_integrated;
	}

	public List<Entry_protein_locations> getEntry_protein_locations() {
		return entry_protein_locations;
	}

	public void setEntry_protein_locations(List<Entry_protein_locations> entry_protein_locations) {
		this.entry_protein_locations = entry_protein_locations;
	}
	
	

}
